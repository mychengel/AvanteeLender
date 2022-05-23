package byc.avt.avanteelender.repositories.tabportofoliorepositories;

import static android.content.Context.DOWNLOAD_SERVICE;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
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

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.DocumentType;
import byc.avt.avanteelender.helper.GlobalVariables;
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

    public MutableLiveData<String> downloadDocument(final String uid, final String token, final Context context, final DocumentType documentType, final String loanNumber) {
        final MutableLiveData<java.lang.String> result = new MutableLiveData<>();
        final String filename = getFileName(documentType, loanNumber);
        final String myUrl = getUrl(documentType, loanNumber);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(myUrl));
        request.addRequestHeader("Authorization", GlobalVariables.basicAuth);
        request.addRequestHeader("Token", token);
        request.addRequestHeader("Uid", uid);
        request.setTitle(filename);
        request.setMimeType("application/pdf");
        request.allowScanningByMediaScanner();
        request.setAllowedOverMetered(true);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename + ".pdf");
        DownloadManager dm = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        dm.enqueue(request);
        result.setValue(context.getString(getResultMessage(documentType)));
        return result;
    }

    private Integer getResultMessage(DocumentType documentType) {
        int result = 0;
        switch (documentType) {
            case AGREEMENT:
            case PROCURATION_LOAN:
                result = R.string.surat_perjanjian_downloaded;
                break;
            case PROCURATION:
            case AGREEMENT_LOAN:
                result = R.string.surat_kuasa_downloaded;
                break;
            case FACTSHEET_LOAN:
                result = R.string.factsheet_downloaded;
                break;
        }
        return result;
    }

    private String getUrl(DocumentType documentType, String loanNumber) {
        String documentUrl = "";

        switch (documentType) {
            case AGREEMENT:
            case PROCURATION:
                documentUrl = url + documentType.getEndpoint();
                break;
            case AGREEMENT_LOAN:
            case PROCURATION_LOAN:
            case FACTSHEET_LOAN:
                documentUrl = url + documentType.getEndpoint() + loanNumber;
                break;
        }
        Log.d("GETURLENUM", "getUrl: " + documentUrl);
        return documentUrl;
    }

    private String getFileName(DocumentType documentType, String loanNumber) {
        String fileName = "";

        switch (documentType) {
            case AGREEMENT:
                fileName = GlobalVariables.LENDER_CODE + "_SuratPerjanjian";
                break;
            case PROCURATION:
                fileName = GlobalVariables.LENDER_CODE + "_SuratKuasa";
                break;
            case AGREEMENT_LOAN:
                fileName = loanNumber + "_SuratKuasa";
                break;
            case PROCURATION_LOAN:
                fileName = loanNumber + "_Agreement";
                break;
            case FACTSHEET_LOAN:
                fileName = loanNumber + "_Factsheet";
                break;
        }
        Log.d("GETFILENAMEENUM", "getFileName: " + fileName);
        return fileName;
    }

    public MutableLiveData<JSONObject> portofolioAktifHeader(final String uid, final String token, Context context) {
        final MutableLiveData<JSONObject> result = new MutableLiveData<>();
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url + "internal/portofolio/active", null,
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
        ) {
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

    public MutableLiveData<ArrayList<PortofolioAktif>> portofolioAktifList(final String page, final String uid, final String token, final Context context) {
        final MutableLiveData<ArrayList<PortofolioAktif>> result = new MutableLiveData<>();
        final MutableLiveData<String> isOnTime = new MutableLiveData<>();
        final ArrayList<PortofolioAktif> list = new ArrayList<>();
        String pg = "";
        if (page.equalsIgnoreCase("1")) {
            pg = "";
        } else {
            pg = "?page=" + page;
        }


        final PortofolioAktif[] pa = new PortofolioAktif[1];
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url + "internal/portofolio/active" + pg, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Aktif PORT LOAD", response.toString());
                        final JSONArray rows;
                        final String[] is_on_time = new String[1];
                        try {
                            rows = response.getJSONArray("rows");
                            final int maxRows = rows.length() - 1;
                            if (rows.length() != 0) {
                                for (int i = 0; i < rows.length(); i++) {
                                    String loan_rating = rows.getJSONObject(i).getString("loan_rating");
                                    String loan_type = rows.getJSONObject(i).getString("loan_type");
                                    PortofolioAktif pa = new PortofolioAktif(
                                            loan_type,
                                            loan_rating,
                                            rows.getJSONObject(i).getString("loan_no"),
                                            rows.getJSONObject(i).getString("funding_id"),
                                            rows.getJSONObject(i).getString("bunga_pinjaman_pa"),
                                            rows.getJSONObject(i).getString("jumlah_hari_pinjam"),
                                            rows.getJSONObject(i).getString("remaining_period"),
                                            rows.getJSONObject(i).getString("status_pembayaran"),
                                            rows.getJSONObject(i).getString("total_angsuran_terbayar_per_loan"),
                                            rows.getJSONObject(i).getString("total_angsuran_selanjutnya_per_loan"),
                                            rows.getJSONObject(i).getString("surat_kuasa"),
                                            rows.getJSONObject(i).getString("agreement_penerima_dana"));
                                    list.add(pa);
                                }
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
        ) {
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
        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url + "internal/portofolio/active/detail?loan_no=" + loan_no + "&funding=" + funding_id, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Aktif PORT DETAILS", response.toString());
                        JSONArray rows;
                        int tot = 0;
                        try {
                            rows = response.getJSONArray("rows");
                            if (rows.length() != 0) {
                                for (int i = 0; i < rows.length(); i++) {
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
                                    PortofolioAktifDetail pad = new PortofolioAktifDetail(
                                            periode,
                                            date_payment,
                                            date_actualtrans,
                                            principal_payment,
                                            interest_amount,
                                            payment_amount,
                                            actual_payment,
                                            tax,
                                            status,
                                            delay_details);

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
        ) {
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
