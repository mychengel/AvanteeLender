package byc.avt.avanteelender.view.features.pendanaan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.ImageView;

import com.here.oksse.OkSse;
import com.here.oksse.ServerSentEvent;

import org.json.JSONException;
import org.json.JSONObject;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.helper.Routes;
import byc.avt.avanteelender.view.MainActivity;
import byc.avt.avanteelender.view.fragment.NotificationsFragment;
import byc.avt.avanteelender.view.fragment.PortofolioFragment;
import byc.avt.avanteelender.viewmodel.PendanaanViewModel;
import okhttp3.Request;
import okhttp3.Response;

public class SignerPendanaanActivity extends AppCompatActivity {

    Fungsi f = new Fungsi(SignerPendanaanActivity.this);
    GlobalVariables gv;
    private Dialog dialog;
    private PendanaanViewModel viewModel;
    private PrefManager prefManager;
    String doc_token = "";
    private WebView simpleWebView;
    ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signer_pendanaan);
        prefManager = PrefManager.getInstance(SignerPendanaanActivity.this);
        dialog = GlobalVariables.loadingDialog(SignerPendanaanActivity.this);
        viewModel = new ViewModelProvider(SignerPendanaanActivity.this).get(PendanaanViewModel.class);
        img_back = findViewById(R.id.img_close_signer_pendanaan);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Intent i = getIntent();
        doc_token = i.getStringExtra("doc_token");

        simpleWebView = findViewById(R.id.wv_signer_pendanaan);
        simpleWebView.setNestedScrollingEnabled(true);
        simpleWebView.setVerticalScrollBarEnabled(true);
        //simpleWebView.setHorizontalScrollBarEnabled(true);
        simpleWebView.requestFocus();
        simpleWebView.getSettings().setDomStorageEnabled(true);
        simpleWebView.getSettings().setDefaultTextEncodingName("utf-8");
        simpleWebView.getSettings().setJavaScriptEnabled(true);
        simpleWebView.setWebViewClient(new SignerPendanaanActivity.MyWebViewClient());

        simpleWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

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
                        Intent intent = new Intent(SignerPendanaanActivity.this, MainActivity.class);
                        intent.putExtra("dest", "2:1");
                        new Routes(SignerPendanaanActivity.this).moveOutIntent(intent);
                        sse.close();
//                        MainActivity ma = new MainActivity();
//                        ma.navView.setSelectedItemId(R.id.navigation_portofolio);
//                        PortofolioFragment.index = 1;
//                        new Routes(SignerPendanaanActivity.this).moveOut();
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

        loadData();

    }

    private void loadData(){
        dialog.show();
        viewModel.getSignerFunding(prefManager.getUid(), prefManager.getToken(), doc_token);
        viewModel.getSignerFundingResult().observe(SignerPendanaanActivity.this, showResult);
//        viewModel.getSignStatus(prefManager.getUid(), prefManager.getToken(), doc_token);
//        viewModel.getSignStatusResult().observe(SignerPendanaanActivity.this, signStatusResult);
    }

    private Observer<String> showResult = new Observer<String>() {
        @Override
        public void onChanged(String result) {
            if(result.isEmpty()){
                dialog.cancel();
            }else{
                simpleWebView.loadDataWithBaseURL(null, result, "text/HTML", "UTF-8", null);
                //dialog.cancel();
            }
        }
    };

    private Observer<String> signStatusResult = new Observer<String>() {
        @Override
        public void onChanged(String result) {
            if(result.isEmpty()){
                dialog.cancel();
            }else{
                dialog.cancel();
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
        new AlertDialog.Builder(SignerPendanaanActivity.this)
                .setTitle("Konfirmasi")
                .setIcon(R.drawable.logo)
                .setMessage(getString(R.string.finish_funding_first))
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        f.showMessage(getString(R.string.funding_done));
                        Intent intent = new Intent(SignerPendanaanActivity.this, MainActivity.class);
                        new Routes(SignerPendanaanActivity.this).moveOutIntent(intent);
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create()
                .show();
    }

}