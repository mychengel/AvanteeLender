package byc.avt.avanteelender.view.auth;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputLayout;
import com.here.oksse.OkSse;
import com.here.oksse.ServerSentEvent;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.security.Signer;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.helper.Routes;
import byc.avt.avanteelender.helper.SignerTemplate;
import byc.avt.avanteelender.intro.WalkthroughActivity;
import byc.avt.avanteelender.model.UserData;
import byc.avt.avanteelender.view.MainActivity;
import byc.avt.avanteelender.view.features.pendanaan.SignerPendanaanActivity;
import byc.avt.avanteelender.view.fragment.PortofolioFragment;
import byc.avt.avanteelender.view.misc.OTPActivity;
import byc.avt.avanteelender.viewmodel.AuthenticationViewModel;
import okhttp3.Request;
import okhttp3.Response;

public class SignersCheckActivity extends AppCompatActivity {

    Fungsi f = new Fungsi(SignersCheckActivity.this);
    private AuthenticationViewModel viewModel;
    private PrefManager prefManager;
    private Dialog dialog;
    GlobalVariables gv;
    String doc_type = "", doc_token = "", privy_id = "", title="", pg="";
    private WebView simpleWebView;
    ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signers_check);
        dialog = GlobalVariables.loadingDialog(SignersCheckActivity.this);
        prefManager = PrefManager.getInstance(SignersCheckActivity.this);
        viewModel = new ViewModelProvider(this).get(AuthenticationViewModel.class);

        img_back = findViewById(R.id.img_close_signers_check);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Intent i = getIntent();
        doc_type = i.getStringExtra("doc_type");

        simpleWebView = findViewById(R.id.wv_signers_check);
        simpleWebView.setNestedScrollingEnabled(true);
        simpleWebView.setVerticalScrollBarEnabled(true);
        //simpleWebView.setHorizontalScrollBarEnabled(true);
        simpleWebView.requestFocus();
        simpleWebView.getSettings().setDomStorageEnabled(true);
        simpleWebView.getSettings().setDefaultTextEncodingName("utf-8");
        simpleWebView.getSettings().setJavaScriptEnabled(true);
        simpleWebView.setWebViewClient(new SignersCheckActivity.MyWebViewClient());

        simpleWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        getDocToken();
    }

    public void getDocToken() {
        // POST to server through endpoint
        dialog.show();
        viewModel.getDocToken(prefManager.getUid(), prefManager.getToken(), doc_type);
        viewModel.getDocTokenResult().observe(SignersCheckActivity.this, showDocTokenResult);
    }

    private Observer<JSONObject> showDocTokenResult = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    String docToken = result.getJSONObject("data").getString("doc_token");
                    viewModel.getSigners(prefManager.getUid(), prefManager.getToken(), docToken);
                    viewModel.getSignerResult().observe(SignersCheckActivity.this, showSignerResult);
                }else{
                    f.showMessageLong(result.getString("message"));
                    dialog.cancel();
                    new AlertDialog.Builder(SignersCheckActivity.this)
                            .setTitle("Informasi")
                            .setIcon(R.drawable.logo)
                            .setMessage(getString(R.string.privy_doc_undefined))
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                    logout();
                                }
                            })
                            .create()
                            .show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private Observer<JSONObject> showSignerResult = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.isNull("docToken")){
                    dialog.cancel();
                    f.showMessage(result.getString("messages"));
                    Intent i = new Intent(SignersCheckActivity.this, WalkthroughActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    new Routes(SignersCheckActivity.this).moveOutIntent(i);
                }else{
                    doc_token = result.getString("docToken");
                    privy_id = result.getString("privyId");
                    pg = ""+result.getInt("page");
                    title = result.getString("title");
                    String page = SignerTemplate.inFramePrivyId(privy_id, doc_token, title, pg);
                    Document doc = Jsoup.parse(String.valueOf(page));
                    String inFrameView = doc.toString();
                    simpleWebView.loadDataWithBaseURL(null, page, "text/HTML", "UTF-8", null);

                    String url = GlobalVariables.BASE_URL+"internal/privy/sign_status/"+doc_token;
                    Request request = new Request.Builder().url(url).build();
                    OkSse okSse = new OkSse();
                    ServerSentEvent sse = okSse.newServerSentEvent(request, new ServerSentEvent.Listener() {
                        @Override
                        public void onOpen(ServerSentEvent sse, Response response) {
                        }
                        @Override
                        public void onMessage(ServerSentEvent sse, String id, String event, String message) {
                            Log.e("SSE", message);
                            try {
                                JSONObject job = new JSONObject(message);
                                String status = job.getString("sign");
                                if(status.equalsIgnoreCase("Completed")){
                                    confirmLogin();
                                    sse.close();
                                }else{}
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onComment(ServerSentEvent sse, String comment) {
                        }
                        @Override
                        public boolean onRetryTime(ServerSentEvent sse, long milliseconds) {
                            return false;
                        }
                        @Override
                        public boolean onRetryError(ServerSentEvent sse, Throwable throwable, Response response) {
                            return false;
                        }
                        @Override
                        public void onClosed(ServerSentEvent sse) {
                        }
                        @Override
                        public Request onPreRetry(ServerSentEvent sse, Request originalRequest) {
                            return null;
                        }
                    });

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            //dialog.show();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url); // load the url
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            dialog.cancel();
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(SignersCheckActivity.this)
                .setTitle("Konfirmasi")
                .setIcon(R.drawable.logo)
                .setMessage(getString(R.string.signers_confirmation))
                .setCancelable(false)
                .setPositiveButton("Berikutnya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        confirmLogin();
                    }
                })
                .setNegativeButton("Kembali", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        logout();
                    }
                })
                .setNeutralButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogs, int which) {
                        dialogs.cancel();
                    }
                })
                .create()
                .show();
    }

    public void confirmLogin() {
        // POST to server through endpoint
        dialog.show();
        viewModel.login(prefManager.getEmail(), prefManager.getPassword());
        viewModel.getLoginResult().observe(SignersCheckActivity.this, checkSuccess);
    }

    private Observer<JSONObject> checkSuccess = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            JSONObject res;
            String msg = "";
            try {
                if (result.getInt("code") == 200 && result.getBoolean("status") == true) {
                    Intent i = null;
                    String token = result.getString("token");
                    res = result.getJSONObject("result");
                    String uid = res.getString("uid");
                    int verif = res.getInt("avantee_verif");
                    UserData ud = new UserData(prefManager.getEmail(),prefManager.getPassword(),uid,res.getInt("type"),res.getString("client_type"),res.getString("avatar"),res.getString("name"),verif,token,0);
                    prefManager.setUserData(ud);
                    if(verif == 1){
                        if(res.isNull("doc") && res.isNull("swafoto") && res.isNull("docfile")){
                            Log.e("Doc", "Aman");
                            if(res.isNull("privy_status")){
                                Log.e("PrivyStatus", "Aman");
                                if(res.isNull("suratkuasa")){
                                    Log.e("TTDSuratKuasa", "Aman");
                                    if(res.isNull("suratperjanjian")){
                                        // Masuk DASHBOARD
                                        Log.e("TTDSuratPK", "Aman");
                                        i = new Intent(SignersCheckActivity.this, MainActivity.class);
                                        i.putExtra("dest","1");
                                        f.showMessage("Selamat datang "+res.getString("name"));
                                    }else{
                                        msg = res.getJSONObject("suratperjanjian").getString("msg");
                                        f.showMessage(msg);
                                        i = new Intent(SignersCheckActivity.this, SignersCheckActivity.class);
                                        i.putExtra("doc_type", "Surat Perjanjian");
                                        //diarahkan untuk ttd surat perjanjian kerja sama
                                    }
                                }else{
                                    msg = res.getJSONObject("suratkuasa").getString("msg");
                                    f.showMessage(msg);
                                    i = new Intent(SignersCheckActivity.this, SignersCheckActivity.class);
                                    i.putExtra("doc_type", "Surat Kuasa");
                                    //diarahkan untuk ttd surat kuasa
                                }
                            }else{
                                msg = res.getJSONObject("privy_status").getString("msg");
                                i = new Intent(SignersCheckActivity.this, InVerificationProcessActivity.class);
                                i.putExtra("info", msg);
                                //f.showMessage(msg);
                            }
                        }else{
                            i = new Intent(SignersCheckActivity.this, RegistrationFormActivity.class);
                        }
                    }else{
                        i = new Intent(SignersCheckActivity.this, OTPActivity.class);
                    }

                    //Routing
                    new Routes(SignersCheckActivity.this).moveInFinish(i);
                    dialog.cancel();
                }else{
                    res = result.getJSONObject("result");
                    msg = res.getString("message");
                    f.showMessage(msg);
                }
                dialog.cancel();
            } catch (JSONException e) {
                e.printStackTrace();
                dialog.cancel();
            }
            dialog.cancel();
        }
    };


    public void logout() {
        // LOGOUT: GET method to server through endpoint
        dialog.show();
        viewModel.logout(prefManager.getUid(), prefManager.getToken());
        viewModel.getLogoutResult().observe(SignersCheckActivity.this, checkLogout);
    }

    private Observer<String> checkLogout = new Observer<String>() {
        @Override
        public void onChanged(String result) {
            if(result.equals("ok")) {
                dialog.cancel();
                Intent intent = new Intent(SignersCheckActivity.this, WalkthroughActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                new Routes(SignersCheckActivity.this).moveOutIntent(intent);
            }else{
                dialog.cancel();
                new Fungsi().showMessage(result);
            }
        }
    };

}