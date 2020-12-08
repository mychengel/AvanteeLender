package byc.avt.avanteelender.repositories;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;

public class PortofolioRepository {

    private static PortofolioRepository repository;
    private String url = GlobalVariables.BASE_URL;
    RequestQueue requestQueue;
    private PrefManager prefManager;

    private PortofolioRepository() {
    }

    public static PortofolioRepository getInstance() {
        if (repository == null) {
            repository = new PortofolioRepository();
        }
        return repository;
    }

    public MutableLiveData<JSONObject> portofolioClose(final String uid, final String token, Context context) {
        final MutableLiveData<JSONObject> result = new MutableLiveData<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url+"internal/portofolio/close", null,
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
