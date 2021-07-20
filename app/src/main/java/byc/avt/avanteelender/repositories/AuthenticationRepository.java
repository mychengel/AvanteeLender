package byc.avt.avanteelender.repositories;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
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

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.HttpsTrustManager;
import byc.avt.avanteelender.helper.InputStreamVolleyRequest;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.helper.VolleyMultipartRequest;
import byc.avt.avanteelender.helper.VolleySingleton;
import byc.avt.avanteelender.model.DataPart;
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
        HttpsTrustManager.allowAllSSL();
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
        HttpsTrustManager.allowAllSSL();
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


    public MutableLiveData<JSONObject> createPersonalDocument(final String uid, final String token, final Context context) {
        final MutableLiveData<JSONObject> result = new MutableLiveData<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        final VolleyMultipartRequest jor = new VolleyMultipartRequest(Request.Method.POST, url+"internal/lender/dokumen/create",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        String resultResponse = new String(response.data);
                        //new Fungsi(context).showMessageLong(resultResponse);
                        try {
                            result.setValue(new JSONObject(resultResponse));
                        } catch (JSONException e) {
                            Log.e("ERRORNYA", resultResponse);
                            e.printStackTrace();
                            if(resultResponse.contains("\"code\":200,\"status\":true")){
                                String resp = "{\"code\":200,\"status\":true,\"result\":{\"messages\":\"Dokument Sukses, Kode verifikasi telah dikirim melalui SMS ke telepon anda.\"}}";
                                try {
                                    result.setValue(new JSONObject(resp));
                                } catch (JSONException jsonException) {
                                    jsonException.printStackTrace();
                                }
                            }else{
                                e.printStackTrace();
                                Map<String,Object> msg = new HashMap<>();
                                msg.put("code",900);
                                msg.put("msg", context.getString(R.string.system_in_trouble));
                                result.setValue(new JSONObject(msg));
                            }
                        }
                        Log.e("createPersonalDocResult", resultResponse);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.toString());
                        Map<String,Object> msg = new HashMap<>();
                        msg.put("code",900);
                        msg.put("msg", context.getString(R.string.system_in_trouble));
                        result.setValue(new JSONObject(msg));
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

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = GlobalVariables.perRegDataFile;
                return params;
            }

        };

        VolleySingleton.getInstance(context).addToRequestQueue(jor);

        return result;
    }

    public MutableLiveData<JSONObject> updatePersonalDocumentx(final String uid, final String token, final Context context) {
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
                            Map<String,Object> msg = new HashMap<>();
                            msg.put("code",400);
                            result.setValue(new JSONObject(msg));
                        }
                        Log.e("updateDocResult", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.toString());
                        Map<String,Object> msg = new HashMap<>();
                        msg.put("code",400);
                        result.setValue(new JSONObject(msg));
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return GlobalVariables.API_ACCESS_IN(uid,token);
            }

            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params = GlobalVariables.perEditData;
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


    public MutableLiveData<JSONObject> updatePersonalDocument(final String uid, final String token, final Context context) {
        final MutableLiveData<JSONObject> result = new MutableLiveData<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        final VolleyMultipartRequest jor = new VolleyMultipartRequest(Request.Method.POST, url+"internal/lender/dokumen/create",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        String resultResponse = new String(response.data);
                        //new Fungsi(context).showMessageLong(resultResponse);
                        try {
                            result.setValue(new JSONObject(resultResponse));
                        } catch (JSONException e) {
                            Log.e("ERRORNYA", resultResponse);
                            e.printStackTrace();
                            if(resultResponse.contains("\"code\":200,\"status\":true")){
                                String resp = "{\"code\":200,\"status\":true,\"result\":{\"messages\":\"Dokumen berhasil diupdate.\"}}";
                                try {
                                    result.setValue(new JSONObject(resp));
                                } catch (JSONException jsonException) {
                                    jsonException.printStackTrace();
                                }
                            }else{
                                e.printStackTrace();
                                Map<String,Object> msg = new HashMap<>();
                                msg.put("code",400);
                                msg.put("msg", context.getString(R.string.doc_not_valid));
                                result.setValue(new JSONObject(msg));
                            }
                        }
                        Log.e("updatePersonalDocResult", resultResponse);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.toString());
                        Map<String,Object> msg = new HashMap<>();
                        msg.put("code",400);
                        msg.put("msg", context.getString(R.string.doc_not_valid));
                        result.setValue(new JSONObject(msg));
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
                Map<String, String> params = GlobalVariables.perEditData;
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = GlobalVariables.perEditDataFile;
                return params;
            }

        };

        VolleySingleton.getInstance(context).addToRequestQueue(jor);

        return result;
    }

    public MutableLiveData<JSONObject> createInstitutionDocument(final String uid, final String token, final Context context) {
        final MutableLiveData<JSONObject> result = new MutableLiveData<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        final VolleyMultipartRequest jor = new VolleyMultipartRequest(Request.Method.POST, url+"internal/lender/dokumen/create",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        String resultResponse = new String(response.data);
                        try {
                            JSONObject resp = new JSONObject(resultResponse);
                            if(resp.getInt("code") == 200){
                                result.setValue(resp);
                            }else{
                                Map<String,Object> msg = new HashMap<>();
                                msg.put("code",400);
                                msg.put("msg", context.getString(R.string.doc_not_valid));
                                result.setValue(new JSONObject(msg));
                            }

                        } catch (JSONException e) {
                            if(resultResponse.contains("\"code\":200,\"status\":true")){
                                String resp = "{\"code\":200,\"status\":true,\"result\":{\"messages\":\"Dokument Sukses, Kode verifikasi telah dikirim melalui SMS ke telepon anda.\"}}";
                                try {
                                    result.setValue(new JSONObject(resp));
                                } catch (JSONException jsonException) {
                                    jsonException.printStackTrace();
                                }
                            }else{
                                e.printStackTrace();
                                Map<String,Object> msg = new HashMap<>();
                                msg.put("code",400);
                                msg.put("msg", context.getString(R.string.doc_not_valid));
                                result.setValue(new JSONObject(msg));
                            }
                        }
                        Log.e("createInsDocResult", resultResponse);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.toString());
                        Map<String,Object> msg = new HashMap<>();
                        msg.put("code",400);
                        msg.put("msg", context.getString(R.string.doc_not_valid));
                        result.setValue(new JSONObject(msg));
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
                Map<String, String> params = GlobalVariables.insRegData;
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = GlobalVariables.insRegDataFile;
                return params;
            }

        };

        VolleySingleton.getInstance(context).addToRequestQueue(jor);

        return result;
    }

    public MutableLiveData<JSONObject> updateInstitusiDocument(final String uid, final String token, final Context context) {
        final MutableLiveData<JSONObject> result = new MutableLiveData<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        final VolleyMultipartRequest jor = new VolleyMultipartRequest(Request.Method.POST, url+"internal/lender/dokumen/create",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        String resultResponse = new String(response.data);
                        //new Fungsi(context).showMessageLong(resultResponse);
                        try {
                            result.setValue(new JSONObject(resultResponse));
                        } catch (JSONException e) {
                            Log.e("ERRORNYA", resultResponse);
                            e.printStackTrace();
                            if(resultResponse.contains("\"code\":200,\"status\":true")){
                                String resp = "{\"code\":200,\"status\":true,\"result\":{\"messages\":\"Dokumen berhasil diupdate.\"}}";
                                try {
                                    result.setValue(new JSONObject(resp));
                                } catch (JSONException jsonException) {
                                    jsonException.printStackTrace();
                                }
                            }else{
                                e.printStackTrace();
                                Map<String,Object> msg = new HashMap<>();
                                msg.put("code",400);
                                msg.put("msg", context.getString(R.string.doc_not_valid));
                                result.setValue(new JSONObject(msg));
                            }
                        }
                        Log.e("updateInsDocResult", resultResponse);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.toString());
                        Map<String,Object> msg = new HashMap<>();
                        msg.put("code",400);
                        msg.put("msg", context.getString(R.string.doc_not_valid));
                        result.setValue(new JSONObject(msg));
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
                Map<String, String> params = GlobalVariables.insEditData;
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = GlobalVariables.insEditDataFile;
                return params;
            }

        };

        VolleySingleton.getInstance(context).addToRequestQueue(jor);

        return result;
    }


    public MutableLiveData<JSONObject> updateProfile(final String uid, final String token, final Context context) {
        final MutableLiveData<JSONObject> result = new MutableLiveData<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        final VolleyMultipartRequest jor = new VolleyMultipartRequest(Request.Method.POST, url+"internal/profile/update",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        String resultResponse = new String(response.data);
                        try {
                            result.setValue(new JSONObject(resultResponse));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Map<String,Object> msg = new HashMap<>();
                            msg.put("status",false);
                            msg.put("msg", context.getString(R.string.update_profile_failed));
                            result.setValue(new JSONObject(msg));
                        }
                        Log.e("createPersonalDocResult", resultResponse);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.toString());
                        Map<String,Object> msg = new HashMap<>();
                        msg.put("status",false);
                        msg.put("msg", context.getString(R.string.update_profile_failed));
                        result.setValue(new JSONObject(msg));
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
                Map<String, String> params = GlobalVariables.profileData;
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = GlobalVariables.profileDataFile;
                return params;
            }

        };

        VolleySingleton.getInstance(context).addToRequestQueue(jor);

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
                            Map<String,Object> msg = new HashMap<>();
                            msg.put("code",400);
                            result.setValue(new JSONObject(msg));
                        }
                        Log.e("resetPassResult", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.toString());
                        Map<String,Object> msg = new HashMap<>();
                        msg.put("code",400);
                        result.setValue(new JSONObject(msg));
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
                            Map<String,Object> msg = new HashMap<>();
                            msg.put("code",400);
                            msg.put("status",false);
                            result.setValue(new JSONObject(msg));
                        }
                        Log.e("resetPassResult", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.toString());
                        Map<String,Object> msg = new HashMap<>();
                        msg.put("code",400);
                        msg.put("status",false);
                        result.setValue(new JSONObject(msg));
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

    public MutableLiveData<JSONObject> emailVerification(final String key, Context context) {
        final MutableLiveData<JSONObject> result = new MutableLiveData<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        Log.e("MyAuthKey", key);
        final StringRequest jor = new StringRequest(Request.Method.GET, url+"internal/confirm/yes/"+key,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            result.setValue(new JSONObject(response));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Map<String,Object> msg = new HashMap<>();
                            msg.put("code",400);
                            msg.put("status",false);
                            result.setValue(new JSONObject(msg));
                        }
                        Log.e("setNewPassResult", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.toString());
                        Map<String,Object> msg = new HashMap<>();
                        msg.put("code",400);
                        msg.put("status",false);
                        result.setValue(new JSONObject(msg));
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return GlobalVariables.API_ACCESS();
            }

//            @Override
//            protected Map<String,String> getParams(){
//                Map<String,String> params = new HashMap<>();
//                params.put("authkey", key);
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
                            Map<String,Object> msg = new HashMap<>();
                            msg.put("code",400);
                            msg.put("status",false);
                            result.setValue(new JSONObject(msg));
                        }
                        Log.e("setNewPassResult", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.toString());
                        Map<String,Object> msg = new HashMap<>();
                        msg.put("code",400);
                        msg.put("status",false);
                        result.setValue(new JSONObject(msg));
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

    public MutableLiveData<JSONObject> changePassword(final String oldPass, final String newPass, final String uid, final String token, Context context) {
        final MutableLiveData<JSONObject> result = new MutableLiveData<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        Log.e("newpass", newPass);
        Log.e("oldpass", oldPass);
        Map<String, String> params = new HashMap<>();
        params.put("settings-current-password", oldPass);
        params.put("settings-new-password", newPass);
        params.put("settings-repeat-password", newPass);
        JSONObject parameters = new JSONObject(params);
        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url+"internal/profile/changepassword", parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        result.setValue(response);
                        Log.e("changePassResult", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.toString());
                        Map<String,Object> msg = new HashMap<>();
                        msg.put("code",400);
                        msg.put("status",false);
                        result.setValue(new JSONObject(msg));
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return GlobalVariables.API_ACCESS_IN(uid,token);
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

    public MutableLiveData<String> sendOTPVerification(final String type, final String uid, final String token, final Context context) {
        prefManager = PrefManager.getInstance(context);
        final MutableLiveData<String> msg = new MutableLiveData<>();
        final MutableLiveData<Boolean> status = new MutableLiveData<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        Map<String, String> params = new HashMap<>();
        params.put("type", type);
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
                            msg.setValue("failed");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.toString());
                        msg.setValue("failed: " + context.getString(R.string.failed));
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

    public MutableLiveData<String> verifyOTP(final String uid, final String token, final String otpCode, final Context context) {
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
                        msg.setValue("failed: " + context.getString(R.string.failed));
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

    public MutableLiveData<String> verifyOTPDoc(final String uid, final String token, final String otpCode, final Context context) {
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
                        msg.setValue("failed: " + context.getString(R.string.failed));
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

    public MutableLiveData<String> verifyOTPSettings(final String uid, final String token, final String type, final String otpCode, final Context context) {
        prefManager = PrefManager.getInstance(context);
        Log.e("type", type);
        Log.e("otpCode", otpCode);
        Long code = Long.parseLong(otpCode);
        final MutableLiveData<String> msg = new MutableLiveData<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        Map<String, Object> params = new HashMap<>();
        params.put("type", type);
        params.put("verifikasi_code", code);
        JSONObject parameters = new JSONObject(params);
        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url+"internal/profile/verification", parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("VerifySettings", response.toString());
                        int code = 0; //jika kembaliannya dalam string
                        boolean status = false;
                        String resp = "";
                        try {
                            code = response.getInt("code");
                            status = response.getBoolean("status");
                            resp = response.getString("msg");
                            if(code == 200 & status == true){
                                msg.setValue("success: " + resp);
                            }else{
                                msg.setValue("failed: " + resp);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            msg.setValue("failed: " + context.getString(R.string.failed));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.toString());
                        msg.setValue("failed: " + context.getString(R.string.failed));
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

    public MutableLiveData<String> resendOTPSettings(final String uid, final String token, final String type, final Context context) {
        prefManager = PrefManager.getInstance(context);
        final MutableLiveData<String> msg = new MutableLiveData<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url+"internal/profile/resendotp/"+type, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        boolean status = false;
                        String resp = "";
                        try {
                            status = response.getBoolean("status");
                            resp = response.getString("msg");
                            if(status == true){
                                msg.setValue("success: " + resp);
                            }else{
                                msg.setValue("failed: " + resp);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            msg.setValue("failed: " + context.getString(R.string.failed));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.toString());
                        msg.setValue("failed: " + context.getString(R.string.failed));
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
        HttpsTrustManager.allowAllSSL();
        final MutableLiveData<JSONObject> result = new MutableLiveData<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url+"internal/setting", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        result.setValue(response);
                        Log.e("SettingData", response.toString());
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
        HttpsTrustManager.allowAllSSL();
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

    public MutableLiveData<JSONObject> getDocToken(final String doc_type, final String uid, final String token, Context context) {
        prefManager = PrefManager.getInstance(context);
        final MutableLiveData<JSONObject> result = new MutableLiveData<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        Map<String, String> params = new HashMap<>();
        params.put("doc_type", doc_type);
        JSONObject parameters = new JSONObject(params);
        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url+"merchand/privy/doctoken", parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        result.setValue(response);
                        Log.e("RespGetToken", response.toString());
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


    public MutableLiveData<JSONObject> getSigner(final String doc_token, final String uid, final String token, Context context) {
        prefManager = PrefManager.getInstance(context);
        final MutableLiveData<JSONObject> result = new MutableLiveData<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url+"merchand/privy/signers/"+doc_token, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        result.setValue(response);
                        Log.e("RespSigners", response.toString());
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
