package byc.avt.avanteelender.repositories;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.model.User;

public class AuthenticationRepository {

    private static AuthenticationRepository repository;
    private String url = GlobalVariables.BASE_URL;
    RequestQueue requestQueue;

    private AuthenticationRepository() {
    }

    public static AuthenticationRepository getInstance() {
        if (repository == null) {
            repository = new AuthenticationRepository();
        }
        return repository;
    }

    public MutableLiveData<String> registration2(User user, Context context) {
        final MutableLiveData<String> msg = new MutableLiveData<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        Map<String, String> params = new HashMap<>(); //untuk passing data ke server/webservice
        params.put("email", user.getEmail());
        params.put("no_handphone", user.getNo_handphone());
        params.put("password", user.getPassword());
        params.put("referral_code", user.getReferral_code());
        JSONObject parameters = new JSONObject(params);
        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url+"daftar/mregister", parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String hasil = null; //jika kembaliannya dalam string
                        try {
                            hasil = response.getString("msg");
                            msg.setValue(hasil);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");
                    }
                }
        )
//        {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> header = new HashMap<>();
//                header.put("Content-Type: ", "application/json");
//                header.put("Accept: ", "application/json");
//                header.put("Authorization: ", "S4pt4_D4n4_B3rs4m4");
//                return header;
//            }
//        }
        ;
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


    ///cara akses API
    public MutableLiveData<String> registration(User user, Context context) {
        final MutableLiveData<String> msg = new MutableLiveData<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        Map<String, String> params = new HashMap<>(); //untuk passing data ke server/webservice
        params.put("email", user.getEmail());
        params.put("no_handphone", user.getNo_handphone());
        params.put("password", user.getPassword());
        params.put("referral_code", user.getReferral_code());
        JSONObject parameters = new JSONObject(params);
        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url+"baycode/checkapi", parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String hasil = null; //jika kembaliannya dalam string
                        try {
                            hasil = response.getString("status");
                            msg.setValue(hasil);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");
                    }
                }
        )

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return GlobalVariables.API_ACCESS();
//                Map<String, String> header = new HashMap<>();
//                header.put("Content-Type: ", "application/json");
//                header.put("Accept: ", "application/json");
//                header.put("Authorization", basicAuth);
//                header.put("X-API-KEY", "G2HN@D4N483RS@MA");
//                return header;
            }
        }
                ;
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
