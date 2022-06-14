package byc.avt.avanteelender.repositories;

import android.app.Dialog;
import android.content.Context;
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

import java.util.Map;

import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;

public class SplashRepository {

    private static SplashRepository repository;
    private String url = GlobalVariables.BASE_URL;
    RequestQueue requestQueue;
    private PrefManager prefManager;

    private SplashRepository() {
    }

    public static SplashRepository getInstance() {
        if (repository == null) {
            repository = new SplashRepository();
        }
        return repository;
    }

    public MutableLiveData<String> sessionCheck(final String uid, final String token, Context context) {
        final MutableLiveData<String> msg = new MutableLiveData<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url+"internal/lender/dashboard", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        boolean status = false;
                        JSONObject res;
                        try {
                            status = response.getBoolean("status");
                            Log.e("Status", status+"");
                            if(status){
                                msg.setValue("ok");
                            }else{
                                prefManager.clearUserData();
                                String errorMsg = response.getString("messages");
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

}
