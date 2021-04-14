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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.model.Header;
import byc.avt.avanteelender.model.HistoryTrx;
import byc.avt.avanteelender.model.User;

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

    public MutableLiveData<JSONObject> getWallet(final String uid, final String token, Context context) {
        final MutableLiveData<JSONObject> result = new MutableLiveData<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url+"internal/lender/wallet", null,
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

    public MutableLiveData<ArrayList<HistoryTrx>> getHistoryTrx(final String uid, final String token, final Context context) {
        final MutableLiveData<ArrayList<HistoryTrx>> result = new MutableLiveData<>();
        final ArrayList<HistoryTrx> list = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url+"internal/lender/wallet", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("WALLET", response.toString());
                        int code = 0; //jika kembaliannya dalam string
                        boolean status = false;
                        JSONArray rows = null;
                        try {
                            code = response.getInt("code");
                            status = response.getBoolean("status");;
                            if(code == 200 && status){
                                rows = response.getJSONArray("rows");
                                Log.e("Histori", rows.toString());
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
                                        if(i < 3){
                                            HistoryTrx historyTrx = new HistoryTrx(obj.getString("description"), obj.getString("trx_date"), nom, obj.getString("approved_status"));
                                            list.add(historyTrx);
                                        }
                                    }
                                }
                            }else{
                                new Fungsi(context).showMessage("Gagal memuat data");
                            }
                            result.setValue(list);
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

    public MutableLiveData<ArrayList<HistoryTrx>> getHistoryTrxList(final String uid, final String token, final Context context) {
        final MutableLiveData<ArrayList<HistoryTrx>> result = new MutableLiveData<>();
        final ArrayList<HistoryTrx> list = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url+"internal/lender/wallet", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        int code = 0; //jika kembaliannya dalam string
                        boolean status = false;
                        JSONArray rows = null;
                        try {
                            code = response.getInt("code");
                            status = response.getBoolean("status");;
                            if(code == 200 && status){
                                rows = response.getJSONArray("rows");
                                Log.e("Histori", rows.toString());
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
                                        HistoryTrx historyTrx = new HistoryTrx(obj.getString("description"), obj.getString("trx_date"), nom, obj.getString("approved_status"));
                                        list.add(historyTrx);
                                    }
                                }
                            }else{
                                new Fungsi(context).showMessage("Gagal memuat data");
                            }
                            result.setValue(list);
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

    public MutableLiveData<ArrayList<Header>> getHeader(final String uid, final String token, final Context context) {
        final MutableLiveData<ArrayList<Header>> result = new MutableLiveData<>();
        final ArrayList<Header> list = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url+"internal/information/header/1", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        int code = 0; //jika kembaliannya dalam string
                        boolean status = false;
                        JSONObject obj = null;
                        try {
                            code = response.getInt("code");
                            status = response.getBoolean("status");;
                            if(code == 200 && status){
                                obj = response.getJSONObject("result");
                                Header header = new Header(obj.getString("user_type"),obj.getString("user_code"),obj.getString("joint_date"),obj.getString("reff_code"), obj.getString("no_handphone"),obj.getString("email"));
                                list.add(header);
                            }else{
                                new Fungsi(context).showMessage("Gagal memuat data");
                            }
                            result.setValue(list);
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

    public MutableLiveData<JSONObject> getDashboard(final String uid, final String token, final Context context) {
        final MutableLiveData<JSONObject> result = new MutableLiveData<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url+"internal/lender/dashboard", null,
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

    public MutableLiveData<String> getTotActivePort(final String uid, final String token, final Context context) {
        final MutableLiveData<String> result = new MutableLiveData<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url+"internal/portofolio/active", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            result.setValue(""+response.getString("total"));
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

    public MutableLiveData<String> getTotPendingPort(final String uid, final String token, final Context context) {
        final MutableLiveData<String> result = new MutableLiveData<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url+"internal/portofolio/pending", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            result.setValue(""+response.getString("total"));
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

    public MutableLiveData<String> getTopupInstruction(final String uid, final String token, final Context context) {
        final MutableLiveData<String> result = new MutableLiveData<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        final StringRequest jor = new StringRequest(Request.Method.GET, url+"internal/panduan_topup",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Document doc = Jsoup.parse(String.valueOf(response));
                        result.setValue(doc.toString());
                        Log.e("PanDuan", doc.toString());
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

    public MutableLiveData<JSONObject> requestWithdrawal(final String uid, final String token, Context context) {
        final MutableLiveData<JSONObject> result = new MutableLiveData<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url+"internal/lender/requestWithdrawal", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("RequestWithdrawal", response.toString());
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

    public MutableLiveData<JSONObject> submitWithdrawal(final String uid, final String token, final String vaNo, final String amount, final String vaBank, Context context) {
        final MutableLiveData<JSONObject> result = new MutableLiveData<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        Map<String, String> params = new HashMap<>();
        params.put("va_no", vaNo);
        params.put("amount", amount);
        params.put("va_bank", vaBank);
        JSONObject parameters = new JSONObject(params);
        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url+"internal/lender/submitWithdrawal", parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("WithdrawalResponse", response.toString());
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
