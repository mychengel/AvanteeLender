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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.model.Pendanaan;
import byc.avt.avanteelender.model.PortofolioSelesai;
import byc.avt.avanteelender.repositories.tabportofoliorepositories.SelesaiPortofolioRepository;

public class PendanaanRepository {

    private static PendanaanRepository repository;
    private String url = GlobalVariables.BASE_URL;
    RequestQueue requestQueue;
    private PrefManager prefManager;

    private PendanaanRepository() {
    }

    public static PendanaanRepository getInstance() {
        if (repository == null) {
            repository = new PendanaanRepository();
        }
        return repository;
    }

    public MutableLiveData<ArrayList<Pendanaan>> getListPendanaan(final String uid, final String token, final Context context) {
        final MutableLiveData<ArrayList<Pendanaan>> result = new MutableLiveData<>();
        final ArrayList<Pendanaan> list = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url+"internal/pendanaan", null,
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
                                    Pendanaan p = new Pendanaan(rows.getJSONObject(i).getString("loan_type"), rows.getJSONObject(i).getString("rating_pinjaman"), rows.getJSONObject(i).getString("loan_no"),
                                            rows.getJSONObject(i).getString("jumlah_hari_pinjam"), rows.getJSONObject(i).getString("invest_bunga"),
                                            rows.getJSONObject(i).getString("nominal_pinjaman"), rows.getJSONObject(i).getString("funding"),
                                            rows.getJSONObject(i).getString("jaminan_status"), rows.getJSONObject(i).getString("tipe_jaminan"),rows.getJSONObject(i).getString("city_name"),
                                            rows.getJSONObject(i).getString("publikasi_end"), rows.getJSONObject(i).getString("borrower_code"), rows.getJSONObject(i).getString("picture_bg"));
                                    list.add(p);
                                    result.setValue(list);
                                }
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

    public MutableLiveData<JSONObject> getDetailPendanaan(final String loan_no, final String uid, final String token, final Context context) {
        final MutableLiveData<JSONObject> result = new MutableLiveData<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url+"internal/pendanaan/detail/"+loan_no, null,
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
