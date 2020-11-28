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
    public MutableLiveData<String> registration(User user, Context context) {
        dialog = GlobalVariables.loadingDialog(context);
        dialog.show();
        final MutableLiveData<String> msg = new MutableLiveData<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        Map<String, String> params = new HashMap<>(); //untuk passing data ke server/webservice
        params.put("email", user.getEmail());
        params.put("no_handphone", user.getNo_handphone());
        params.put("password", user.getPassword());
        params.put("referral_code", user.getReferral_code());
        JSONObject parameters = new JSONObject(params);
        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url+"auth/register", parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.cancel();
                        String hasil = null; //jika kembaliannya dalam string
                        try {
                            hasil = response.getString("mresult");
                            msg.setValue(hasil);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.toString());
                        dialog.cancel();
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
        return msg;
    }


    public MutableLiveData<String> login(final String email, final String password, Context context) {
        prefManager = new PrefManager(context);
        dialog = GlobalVariables.loadingDialog(context);
        dialog.show();
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
                        dialog.cancel();
                        int code = 0; //jika kembaliannya dalam string
                        String token = "";
                        boolean status = false;
                        JSONObject res;
                        try {
                            code = response.getInt("code");
                            status = response.getBoolean("status");
                            if(code == 200 & status == true){
                                token = response.getString("token");
                                res = response.getJSONObject("result");
                                String uid = res.getString("uid");
                                int type = res.getInt("type");
                                String client_type = res.getString("client_type");
                                String avatar = res.getString("avatar");
                                String name = res.getString("name");
                                int avantee_verif = res.getInt("avantee_verif");
                                UserData ud = new UserData(uid,type,client_type,avatar,name,avantee_verif,token,0);
                                prefManager.setUserData(ud);
                                msg.setValue("ok");
                            }else{
                                msg.setValue("no");
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
                        dialog.cancel();
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
        prefManager = new PrefManager(context);
        dialog = GlobalVariables.loadingDialog(context);
        dialog.show();
        final MutableLiveData<String> msg = new MutableLiveData<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url+"internal/signout", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.cancel();
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
                        dialog.cancel();
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

}
