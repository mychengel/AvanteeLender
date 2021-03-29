package byc.avt.avanteelender.repositories;

import android.app.Dialog;
import android.content.Context;
import android.util.Base64;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.model.User;
import byc.avt.avanteelender.model.UserData;

public class AuthenticationRepository {

    private static AuthenticationRepository repository;
    private String url = GlobalVariables.BASE_URL;
    RequestQueue requestQueue;
    Dialog dialog;
    private PrefManager prefManager;

    private AuthenticationRepository() {
    }

    public static AuthenticationRepository getInstance() {
        if (repository == null) {
            repository = new AuthenticationRepository();
        }
        return repository;
    }

    ///Method to post user data for register
    public MutableLiveData<JSONObject> registration(User user, Context context) {
        final MutableLiveData<JSONObject> result = new MutableLiveData<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        Map<String, String> params = new HashMap<>();
        params.put("userType", "1");
        params.put("email", user.getEmail());
        params.put("phone", user.getNo_handphone());
        params.put("password", user.getPassword());
        params.put("repeatPassword", user.getPassword());
        params.put("reffCode", user.getReferral_code());
        params.put("term", "1");
        JSONObject parameters = new JSONObject(params);
        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url+"internal/register", parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("REGISTRASI", response.toString());
                        result.setValue(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.toString());
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return GlobalVariables.API_ACCESS();
            }
        };
        requestQueue.getCache().clear();
        requestQueue.add(jor).setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 60000;
            }
            @Override
            public int getCurrentRetryCount() {
                return 0;
            }
            @Override
            public void retry(VolleyError error) throws VolleyError {
            }
        });
        return result;
    }


    public MutableLiveData<String> login(final String email, final String password, Context context) {
        prefManager = PrefManager.getInstance(context);
        final MutableLiveData<String> msg = new MutableLiveData<>();
        final MutableLiveData<Boolean> status = new MutableLiveData<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        Map<String, String> params = new HashMap<>();
        params.put("password", password);
        params.put("email", email);
        JSONObject parameters = new JSONObject(params);
        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url+"internal/signin", parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        int code = 0; //jika kembaliannya dalam string
                        String token = "";
                        boolean status = false;
                        JSONObject res;
                        try {
                            code = response.getInt("code");
                            status = response.getBoolean("status");
                            if(code == 200 & status == true){
                                res = response.getJSONObject("result");
                                int avantee_verif = res.getInt("avantee_verif");
                                if(avantee_verif == 1){
                                    token = response.getString("token");
                                    String uid = res.getString("uid");
                                    int type = res.getInt("type");
                                    String client_type = res.getString("client_type");
                                    String avatar = res.getString("avatar");
                                    String name = res.getString("name");
                                    UserData ud = new UserData(uid,type,client_type,avatar,name,avantee_verif,token,0);
                                    prefManager.setUserData(ud);
                                    msg.setValue("success");
                                }else if(avantee_verif == 0){
                                    msg.setValue("not_verified");
                                }else{
                                    msg.setValue("failed2");
                                }

                            }else{
                                msg.setValue("failed");
                                Log.e("Login Response", "Email atau password tidak sesuai");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.toString());
                    }
                }
        )

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return GlobalVariables.API_ACCESS();
            }

//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("password", password);
//                params.put("email", email);
//                return params;
//            }
        };
        requestQueue.getCache().clear();
        requestQueue.add(jor).setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 60000;
            }
            @Override
            public int getCurrentRetryCount() {
                return 0;
            }
            @Override
            public void retry(VolleyError error) throws VolleyError {
            }
        });

        return msg;
    }

    public MutableLiveData<String> logout(final String uid, final String token, Context context) {
        prefManager = PrefManager.getInstance(context);
        final MutableLiveData<String> msg = new MutableLiveData<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url+"internal/signout", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        int code = 0;
                        boolean status = false;
                        JSONObject res;
                        try {
                            code = response.getInt("code");
                            status = response.getBoolean("status");
                            if(code == 200 & status == true){
                                prefManager.clearUserData();
                                msg.setValue("ok");
                            }else{
                                String errorMsg = "";
                                //res = response.getJSONObject("result");
                                errorMsg = response.getString("message");
                                msg.setValue(errorMsg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.toString());
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return GlobalVariables.API_ACCESS_IN(uid, token);
            }
        };
        requestQueue.getCache().clear();
        requestQueue.add(jor).setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 60000;
            }
            @Override
            public int getCurrentRetryCount() {
                return 0;
            }
            @Override
            public void retry(VolleyError error) throws VolleyError {
            }
        });
        return msg;
    }

    public MutableLiveData<String> forceLogout(final String uid, final String token, Context context) {
        prefManager = PrefManager.getInstance(context);
        final MutableLiveData<String> msg = new MutableLiveData<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url+"internal/signout", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        int code = 0;
                        boolean status = false;
                        JSONObject res;
                        try {
                            code = response.getInt("code");
                            status = response.getBoolean("status");
                            if(code == 200 & status == true){
                                prefManager.clearUserData();
                                msg.setValue("ok");
                            }else{
                                String errorMsg = "";
                                //res = response.getJSONObject("result");
                                errorMsg = response.getString("message");
                                msg.setValue(errorMsg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.toString());
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return GlobalVariables.API_ACCESS_IN(uid, token);
            }
        };
        requestQueue.getCache().clear();
        requestQueue.add(jor).setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 60000;
            }
            @Override
            public int getCurrentRetryCount() {
                return 0;
            }
            @Override
            public void retry(VolleyError error) throws VolleyError {
            }
        });
        return msg;
    }


    public MutableLiveData<JSONObject> getSettingData(final String uid, final String token, Context context) {
        final MutableLiveData<JSONObject> result = new MutableLiveData<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url+"internal/setting", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        result.setValue(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.toString());
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return GlobalVariables.API_ACCESS_IN(uid, token);
            }
        };
        requestQueue.getCache().clear();
        requestQueue.add(jor).setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 60000;
            }
            @Override
            public int getCurrentRetryCount() {
                return 0;
            }
            @Override
            public void retry(VolleyError error) throws VolleyError {
            }
        });
        return result;
    }

}
