package byc.avt.avanteelender.repositories.tabportofoliorepositories;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
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

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Map;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.InputStreamVolleyRequest;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.model.PortofolioAktif;
import byc.avt.avanteelender.model.PortofolioAktifDetail;

public class AktifPortofolioRepository {

    private static AktifPortofolioRepository repository;
    private String url = GlobalVariables.BASE_URL;
    RequestQueue requestQueue;
    private PrefManager prefManager;

    private AktifPortofolioRepository() {
    }

    public static AktifPortofolioRepository getInstance() {
        if (repository == null) {
            repository = new AktifPortofolioRepository();
        }
        return repository;
    }

    public MutableLiveData<String> downloadSuratKuasa(final String uid, final String token, final Context context) {
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
                                Log.e("ResponSuratKuasa", response.toString());
                                String filename = "SuratKuasa.pdf";
                                File folder = null;
                                File file = null;
                                try{
                                    folder = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/avantee/");
                                    if (!folder.exists()) {
                                        folder.mkdirs();
                                    }
                                    file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/avantee/", filename);
                                    if (!file.exists()) {
                                        file.createNewFile();
                                    }
                                    Log.e("PathSuratKuasa", file+"");
                                }catch (Exception e) {
                                    e.printStackTrace();
                                }
                                FileOutputStream outputStream;
                                outputStream = new FileOutputStream(file, true);
                                outputStream.write(response);
                                outputStream.close();

                                Toast.makeText(context, context.getString(R.string.surat_kuasa_downloaded), Toast.LENGTH_LONG).show();
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

    public MutableLiveData<String> downloadSuratPerjanjian(final String uid, final String token, final Context context) {
        final MutableLiveData<String> result = new MutableLiveData<>();
        String myurl = url+"internal/portofolio/download_suratperjanjian";
        InputStreamVolleyRequest request = new InputStreamVolleyRequest(Request.Method.GET, myurl,
                new Response.Listener<byte[]>() {
                    @Override
                    public void onResponse(byte[] response) {
                        // TODO handle the response
                        try {
                            if (response!=null) {
                                result.setValue(response.toString());
                                Log.e("ResponSuratPerjanjian", response.toString());
                                String filename = "SuratPerjanjian.pdf";
                                File folder = null;
                                File file = null;
                                try{
                                    folder = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/avantee/");
                                    if (!folder.exists()) {
                                        folder.mkdirs();
                                    }
                                    file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/avantee/", filename);
                                    if (!file.exists()) {
                                        file.createNewFile();
                                    }
                                    Log.e("PathSuratPerjanjian", file+"");
                                }catch (Exception e) {
                                    e.printStackTrace();
                                }
                                FileOutputStream outputStream;
                                outputStream = new FileOutputStream(file, true);
                                outputStream.write(response);
                                outputStream.close();

                                Toast.makeText(context, context.getString(R.string.surat_perjanjian_downloaded), Toast.LENGTH_LONG).show();
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


    public MutableLiveData<JSONObject> portofolioAktifHeader(final String uid, final String token, Context context) {
        final MutableLiveData<JSONObject> result = new MutableLiveData<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url+"internal/portofolio/active", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        result.setValue(response);
                        Log.e("Aktif PORT", response.toString());
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

    public MutableLiveData<ArrayList<PortofolioAktif>> portofolioAktifList(final String uid, final String token, final Context context) {
        final MutableLiveData<ArrayList<PortofolioAktif>> result = new MutableLiveData<>();
        final ArrayList<PortofolioAktif> list = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url+"internal/portofolio/active", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        final JSONArray rows;
                        try {
                            rows = response.getJSONArray("rows");
                            final int maxRows = rows.length()-1;
                            if(rows.length()==0){
                                result.setValue(list);
                            }else{
                                for(int i = 0; i < rows.length(); i++){
                                    String loan_no="", funding_id="";
                                    loan_no = rows.getJSONObject(i).getString("loan_no");
                                    funding_id = rows.getJSONObject(i).getString("funding_id");
                                    final int finalI = i;
                                    final JsonObjectRequest jors = new JsonObjectRequest(Request.Method.GET, url+"internal/portofolio/active/detail?loan_no="+loan_no+"&funding="+funding_id, null,
                                            new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    // Log.e("Aktif PORT DETAILS", response.toString());
                                                    JSONArray rows2;
                                                    int tot = 0;
                                                    try {
                                                        rows2 = response.getJSONArray("rows");
                                                        if(rows2.length()==0){
                                                            //result.setValue(list);
                                                        }else{
                                                            for(int j = 0; j < rows2.length(); j++){
                                                                String delay_details="", status="";
                                                                delay_details = rows2.getJSONObject(j).getString("delay_details");
                                                                status = rows2.getJSONObject(j).getString("status");
                                                                if(status.equalsIgnoreCase(null) || status.equalsIgnoreCase("null") ){
                                                                    tot = tot + 0;
                                                                }else{
                                                                    tot = tot + 1;
                                                                }
                                                            }

                                                            String is_on_time = "0";
                                                            if(tot > 0){
                                                                is_on_time = "1";
                                                            }else{
                                                                is_on_time = "0";
                                                            }
                                                            String loan_rating = rows.getJSONObject(finalI).getString("loan_rating");
                                                            String loan_type = rows.getJSONObject(finalI).getString("loan_type");
                                                            PortofolioAktif pa = new PortofolioAktif(loan_type, loan_rating, rows.getJSONObject(finalI).getString("loan_no"), rows.getJSONObject(finalI).getString("funding_id"),
                                                                    rows.getJSONObject(finalI).getString("dokumen_kontrak"), rows.getJSONObject(finalI).getString("bunga_pinjaman_pa"), rows.getJSONObject(finalI).getString("jumlah_hari_pinjam"),
                                                                    rows.getJSONObject(finalI).getString("remaining_period"), is_on_time,
                                                                    rows.getJSONObject(finalI).getString("total_angsuran_terbayar_per_loan"), rows.getJSONObject(finalI).getString("total_angsuran_selanjutnya_per_loan"));
                                                            list.add(pa);
                                                            result.setValue(list);
//                                                            if(finalI == maxRows){
//                                                                result.setValue(list);
//                                                            }else{}

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
                                    requestQueue.add(jors).setRetryPolicy(new RetryPolicy() {
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

                                }
                                //result.setValue(list);
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


    public MutableLiveData<ArrayList<PortofolioAktifDetail>> getPortAktifDetList(final String uid, final String token, final Context context, final String loan_no, final String funding_id) {
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        final MutableLiveData<ArrayList<PortofolioAktifDetail>> result = new MutableLiveData<>();
        final ArrayList<PortofolioAktifDetail> list = new ArrayList<>();
        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url+"internal/portofolio/active/detail?loan_no="+loan_no+"&funding="+funding_id, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Aktif PORT DETAILS", response.toString());
                        JSONArray rows;
                        int tot = 0;
                        try {
                            rows = response.getJSONArray("rows");
                            if(rows.length()==0){
                                //result.setValue(list);
                            }else{
                                for(int i = 0; i < rows.length(); i++){
                                    String periode = rows.getJSONObject(i).getString("schedule_period");
                                    String date_payment = rows.getJSONObject(i).getString("next_payment");
                                    String date_actualtrans = rows.getJSONObject(i).getString("actual_transaction_date");
                                    String principal_payment = rows.getJSONObject(i).getString("principal_payment");
                                    String interest_amount = rows.getJSONObject(i).getString("interest_amount");
                                    String payment_amount = rows.getJSONObject(i).getString("payment_amount");
                                    String actual_payment = rows.getJSONObject(i).getString("actual_payment");
                                    String tax = rows.getJSONObject(i).getString("tax");
                                    String status = rows.getJSONObject(i).getString("status");
                                    String delay_details = rows.getJSONObject(i).getString("delay_details");
                                    PortofolioAktifDetail pad = new PortofolioAktifDetail(periode,date_payment,date_actualtrans,
                                            principal_payment,interest_amount,payment_amount,actual_payment,tax,status,delay_details);
                                    list.add(pad);
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
