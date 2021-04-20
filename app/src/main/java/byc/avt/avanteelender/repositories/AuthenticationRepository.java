package byc.avt.avanteelender.repositories;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.InputStreamVolleyRequest;
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

    public MutableLiveData<JSONObject> getAccountData(final String uid, final String token, Context context) {
        final MutableLiveData<JSONObject> result = new MutableLiveData<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url+"internal/lender/dokumen", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("AccountData", response.toString());
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

    public MutableLiveData<JSONObject> login(final String email, final String password, Context context) {
        prefManager = PrefManager.getInstance(context);
        final MutableLiveData<JSONObject> result = new MutableLiveData<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        Map<String, String> params = new HashMap<>();
        params.put("password", password);
        params.put("email", email);
        JSONObject parameters = new JSONObject(params);
        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url+"internal/signin", parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("LoginResp", response.toString());
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


//    public MutableLiveData<String> login(final String email, final String password, Context context) {
//        prefManager = PrefManager.getInstance(context);
//        final MutableLiveData<String> msg = new MutableLiveData<>();
//        final MutableLiveData<Boolean> status = new MutableLiveData<>();
//        requestQueue = Volley.newRequestQueue(context, new HurlStack());
//        Map<String, String> params = new HashMap<>();
//        params.put("password", password);
//        params.put("email", email);
//        JSONObject parameters = new JSONObject(params);
//        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url+"internal/signin", parameters,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.e("LoginResp", response.toString());
//                        int code = 0; //jika kembaliannya dalam string
//                        String token = "";
//                        boolean status = false;
//                        JSONObject res;
//                        try {
//                            code = response.getInt("code");
//                            status = response.getBoolean("status");
//                            if(code == 200 & status == true){
//                                res = response.getJSONObject("result");
//                                int avantee_verif = res.getInt("avantee_verif");
//                                if(avantee_verif == 1){
//                                    token = response.getString("token");
//                                    String uid = res.getString("uid");
//                                    int type = res.getInt("type");
//                                    String client_type = res.getString("client_type");
//                                    String avatar = res.getString("avatar");
//                                    String name = res.getString("name");
//                                    UserData ud = new UserData(email,password,uid,type,client_type,avatar,name,avantee_verif,token,0);
//                                    prefManager.setUserData(ud);
//                                    msg.setValue("success");
//                                }else if(avantee_verif == 0){
//                                    token = response.getString("token");
//                                    String uid = res.getString("uid");
//                                    int type = res.getInt("type");
//                                    String client_type = res.getString("client_type");
//                                    String avatar = res.getString("avatar");
//                                    String name = res.getString("name");
//                                    UserData ud = new UserData(email,password,uid,type,client_type,avatar,name,avantee_verif,token,0);
//                                    prefManager.setUserData(ud);
//                                    msg.setValue("not_verified");
//                                }else{
//                                    msg.setValue("failed2");
//                                }
//
//                            }else{
//                                msg.setValue("failed");
//                                Log.e("Login Response", "Email atau password tidak sesuai");
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("Volley", error.toString());
//                    }
//                }
//        )
//
//        {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                return GlobalVariables.API_ACCESS();
//            }
//        };
//        requestQueue.getCache().clear();
//        requestQueue.add(jor).setRetryPolicy(new RetryPolicy() {
//            @Override
//            public int getCurrentTimeout() {
//                return 60000;
//            }
//            @Override
//            public int getCurrentRetryCount() {
//                return 0;
//            }
//            @Override
//            public void retry(VolleyError error) throws VolleyError {
//            }
//        });
//
//        return msg;
//    }

    public MutableLiveData<JSONObject> createPersonalDocument(final String uid, final String token, Context context) {
        final MutableLiveData<JSONObject> result = new MutableLiveData<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        final StringRequest jor = new StringRequest(Request.Method.POST, url+"internal/lender/dokumen/create",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            result.setValue(new JSONObject(response));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.e("createPersonalDocResult", response);
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
                return GlobalVariables.API_ACCESS_IN(uid,token);
            }

            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = GlobalVariables.perRegData;
                return params;
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

    public MutableLiveData<JSONObject> resendEmailVerification(final String email, Context context) {
        final MutableLiveData<JSONObject> result = new MutableLiveData<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        final StringRequest jor = new StringRequest(Request.Method.POST, url+"internal/auth/resend",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            result.setValue(new JSONObject(response));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Map<String,String> msg = new HashMap<>();
                            msg.put("code","400");
                            result.setValue(new JSONObject(msg));
                        }
                        Log.e("resetPassResult", response);
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

            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("email", email);
                return params;
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


    public MutableLiveData<JSONObject> resetPassword(final String email, Context context) {
        final MutableLiveData<JSONObject> result = new MutableLiveData<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        final StringRequest jor = new StringRequest(Request.Method.POST, url+"internal/forgot_password",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            result.setValue(new JSONObject(response));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.e("resetPassResult", response);
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

            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("email", email);
                return params;
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

    public MutableLiveData<JSONObject> setNewPassword(final String newPass, final String key, Context context) {
        final MutableLiveData<JSONObject> result = new MutableLiveData<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        Log.e("MyResetKey", key);
        final StringRequest jor = new StringRequest(Request.Method.POST, url+"internal/action_recover_password/"+key,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            result.setValue(new JSONObject(response));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.e("setNewPassResult", response);
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

            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("password", newPass);
                params.put("authkey", key);
                return params;
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


    public MutableLiveData<String> sendOTPVerification(final String uid, final String token, Context context) {
        prefManager = PrefManager.getInstance(context);
        final MutableLiveData<String> msg = new MutableLiveData<>();
        final MutableLiveData<Boolean> status = new MutableLiveData<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        Map<String, String> params = new HashMap<>();
        params.put("type", "verification");
        JSONObject parameters = new JSONObject(params);
        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url+"merchand/bulk/otp", parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        int code = 0; //jika kembaliannya dalam string
                        boolean status = false;
                        JSONObject res;
                        try {
                            code = response.getInt("code");
                            status = response.getBoolean("status");
                            if(code == 200 & status == true){
                                res = response.getJSONObject("result");
                                msg.setValue(res.getString("message"));
                            }else{
                                msg.setValue("failed");
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

    public MutableLiveData<String> verifyOTP(final String uid, final String token, final String otpCode, Context context) {
        prefManager = PrefManager.getInstance(context);
        final MutableLiveData<String> msg = new MutableLiveData<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        Map<String, String> params = new HashMap<>();
        params.put("type", "verification");
        params.put("verification_code", otpCode);
        JSONObject parameters = new JSONObject(params);
        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url+"merchand/bulk/verification", parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        int code = 0; //jika kembaliannya dalam string
                        boolean status = false;
                        JSONObject res;
                        try {
                            code = response.getInt("code");
                            status = response.getBoolean("status");
                            if(code == 200 & status == true){
                                res = response.getJSONObject("result");
                                msg.setValue("success: " + res.getString("message"));
                            }else{
                                res = response.getJSONObject("result");
                                msg.setValue("failed: " + res.getString("message"));
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

    public MutableLiveData<String> verifyOTPDoc(final String uid, final String token, final String otpCode, Context context) {
        prefManager = PrefManager.getInstance(context);
        final MutableLiveData<String> msg = new MutableLiveData<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        Map<String, String> params = new HashMap<>();
        params.put("type", "document");
        params.put("verification_code", otpCode);
        JSONObject parameters = new JSONObject(params);
        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url+"merchand/bulk/verification", parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        int code = 0; //jika kembaliannya dalam string
                        boolean status = false;
                        JSONObject res;
                        try {
                            code = response.getInt("code");
                            status = response.getBoolean("status");
                            if(code == 200 & status == true){
                                res = response.getJSONObject("result");
                                msg.setValue("success: " + res.getString("message"));
                            }else{
                                res = response.getJSONObject("result");
                                msg.setValue("failed: " + res.getString("message"));
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

    public MutableLiveData<JSONObject> getSettingDataNoAuth(Context context) {
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

    public MutableLiveData<String> cekSuratKuasa(final String uid, final String token, final Context context) {
        final MutableLiveData<String> result = new MutableLiveData<>();
        String myurl = url+"internal/portofolio/download_suratkuasa";
        InputStreamVolleyRequest request = new InputStreamVolleyRequest(Request.Method.GET, myurl,
                new Response.Listener<byte[]>() {
                    @Override
                    public void onResponse(byte[] response) {
                        // TODO handle the response
                        try {
                            if (response!=null) {
                                result.setValue(response.toString());
                                Log.e("ResponSuratKuasa", ""+response.length);
//                                String filename = "AvanteeSuratKuasa.pdf";
////                                ContextWrapper contextWrapper = new ContextWrapper(context);
////                                File directory = contextWrapper.getDir(context.getFilesDir().getName(), Context.MODE_PRIVATE);
////                                File file =  new File(directory,filename);
//                                FileOutputStream outputStream;
//                                outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
//                                //outputStream = new FileOutputStream(new File(context.getFilesDir(),filename));
//                                outputStream.write(response);
//                                outputStream.close();

                                Toast.makeText(context, "Download selesai.", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            Log.d("KEY_ERROR", "UNABLE TO DOWNLOAD FILE");
                            e.printStackTrace();
                        }
                    }
                } ,new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }, null)
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return GlobalVariables.API_ACCESS_IN(uid, token);
            }
        };
        RequestQueue mRequestQueue = Volley.newRequestQueue(context, new HurlStack());
        mRequestQueue.add(request);
        return result;
    }

    public MutableLiveData<String> cekSuratKuasa2(final String uid, final String token, Context context) {
        final MutableLiveData<String> result = new MutableLiveData<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        final StringRequest jor = new StringRequest(Request.Method.GET, url+"internal/portofolio/download_suratkuasa",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        result.setValue(response);
                        Log.e("ResponseCekSuratKuasa", response);
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
                Log.e("VolleyError", error.toString());
            }
        });
        return result;
    }

    public MutableLiveData<String> cekSuratPerjanjian(final String uid, final String token, Context context) {
        final MutableLiveData<String> result = new MutableLiveData<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        final StringRequest jor = new StringRequest(Request.Method.GET, url+"internal/portofolio/download_suratperjanjian",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        result.setValue(response);
                        Log.e("ResponseCekSuratKuasa", response);
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
                Log.e("VolleyError", error.toString());
            }
        });
        return result;
    }

    public MutableLiveData<String> cekSuratKuasa2x(final String uid, final String token, Context context) {
        prefManager = PrefManager.getInstance(context);
        final MutableLiveData<String> msg = new MutableLiveData<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        Map<String, String> params = new HashMap<>();
        params.put("doc_type", "Surat Kuasa");
        JSONObject parameters = new JSONObject(params);
        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url+"merchand/privy/doctoken", parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        msg.setValue(response.toString());
                        Log.e("ResponseCekSurat", response.toString());
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

}
