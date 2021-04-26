package byc.avt.avanteelender.repositories.tabportofoliorepositories;

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

import java.util.ArrayList;
import java.util.Map;

import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.model.PortofolioSelesai;
import byc.avt.avanteelender.repositories.PortofolioRepository;

public class SelesaiPortofolioRepository {

    private static SelesaiPortofolioRepository repository;
    private String url = GlobalVariables.BASE_URL;
    RequestQueue requestQueue;
    private PrefManager prefManager;

    private SelesaiPortofolioRepository() {
    }

    public static SelesaiPortofolioRepository getInstance() {
        if (repository == null) {
            repository = new SelesaiPortofolioRepository();
        }
        return repository;
    }

    public MutableLiveData<JSONObject> portofolioCloseHeader(final String uid, final String token, Context context) {
        final MutableLiveData<JSONObject> result = new MutableLiveData<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url+"internal/portofolio/close", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        result.setValue(response);
                        Log.e("SELESAI PORT", response.toString());
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

    public MutableLiveData<ArrayList<PortofolioSelesai>> portofolioCloseList(final String page, final String uid, final String token, final Context context) {
        final MutableLiveData<ArrayList<PortofolioSelesai>> result = new MutableLiveData<>();
        final ArrayList<PortofolioSelesai> list = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        String pg = "";
        if(page.equalsIgnoreCase("1")){
            pg = "";
        }else{
            pg = "?page="+page;
        }
        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url+"internal/portofolio/close"+pg, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray rows;
                        try {
                            rows = response.getJSONArray("rows");
                            if(rows.length()==0){
                                result.setValue(list);
                            }else{
                                for(int i = 0; i < rows.length(); i++){
                                    String loan_rating="", loan_type="";
                                    loan_rating = rows.getJSONObject(i).getString("loan_rating");
                                    loan_type = rows.getJSONObject(i).getString("loan_type");
                                    PortofolioSelesai ps = new PortofolioSelesai(loan_type, loan_rating, rows.getJSONObject(i).getString("loan_no"),
                                            rows.getJSONObject(i).getString("tanggal_investasi"), rows.getJSONObject(i).getString("durasi"),
                                            rows.getJSONObject(i).getString("bunga_pinjaman_pa"), rows.getJSONObject(i).getString("nominal"),
                                            rows.getJSONObject(i).getString("payment_amount"));
                                    list.add(ps);
                                }
                                result.setValue(list);
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
        return result;
    }

}
