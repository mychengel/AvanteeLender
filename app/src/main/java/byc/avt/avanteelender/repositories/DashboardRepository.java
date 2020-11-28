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

import java.util.ArrayList;
import java.util.Map;

import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.model.HistoryTrx;

public class DashboardRepository {

    private static DashboardRepository repository;
    private String url = GlobalVariables.BASE_URL;
    RequestQueue requestQueue;
    Dialog dialog;
    private PrefManager prefManager;

    private DashboardRepository() {
    }

    public static DashboardRepository getInstance() {
        if (repository == null) {
            repository = new DashboardRepository();
        }
        return repository;
    }

    public MutableLiveData<ArrayList<HistoryTrx>> getHistoryTrx(final String uid, final String token, final Context context) {
        prefManager = new PrefManager(context);
        dialog = GlobalVariables.loadingDialog(context);
        dialog.show();
        final MutableLiveData<ArrayList<HistoryTrx>> result = new MutableLiveData<>();
        final ArrayList<HistoryTrx> list = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url+"internal/lender/wallet", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.cancel();
                        int code = 0; //jika kembaliannya dalam string
                        boolean status = false;
                        JSONArray rows = null;
                        try {
                            code = response.getInt("code");
                            status = response.getBoolean("status");;
                            if(code == 200 && status == true){
                                rows = response.getJSONArray("rows");
                                if(rows.length()==0){

                                }else{
                                    for(int i = 0; i < rows.length(); i++){
                                        JSONObject obj = rows.getJSONObject(i);
                                        String nom = "0";
                                        if(obj.getString("nominal_in").equals("0")){
                                            nom = "- "+ new Fungsi(context).toNumb(obj.getString("nominal_out")) ;
                                        }else{
                                            nom = "+ "+ new Fungsi(context).toNumb(obj.getString("nominal_in")) ;
                                        }
                                        HistoryTrx historyTrx = new HistoryTrx(obj.getString("description"), obj.getString("trx_date")
                                            , nom, obj.getString("approved_status"));
                                        list.add(historyTrx);
                                    }
                                    result.setValue(list);
                                }
                            }else{
                                //String errorMsg = response.getString("messages");
                                new Fungsi(context).showMessage("Gagal memuat data histori transaksi.");
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

        return result;
    }

}