package byc.avt.avanteelender.view.features.pendanaan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.view.features.topup.TopupInstructionActivity;
import byc.avt.avanteelender.viewmodel.PendanaanViewModel;

public class ViewKontrakPendanaanActivity extends AppCompatActivity {

    Fungsi f = new Fungsi(ViewKontrakPendanaanActivity.this);
    GlobalVariables gv;
    private Dialog dialog;
    private PendanaanViewModel viewModel;
    private PrefManager prefManager;
    String loan_no = "", agreement_code = "";
    private WebView simpleWebView;
    private Button btn_setuju;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_kontrak_pendanaan);
        prefManager = PrefManager.getInstance(ViewKontrakPendanaanActivity.this);
        dialog = GlobalVariables.loadingDialog(ViewKontrakPendanaanActivity.this);
        viewModel = new ViewModelProvider(ViewKontrakPendanaanActivity.this).get(PendanaanViewModel.class);
        btn_setuju = findViewById(R.id.btn_setuju_view_kontrak_pendanaan);
        btn_setuju.setEnabled(false);

        Intent i = getIntent();
        loan_no = i.getStringExtra("loan_no");
        agreement_code = i.getStringExtra("agreement_code");

        simpleWebView = findViewById(R.id.wv_view_kontrak_pendanaan);
        simpleWebView.setNestedScrollingEnabled(true);
        simpleWebView.setVerticalScrollBarEnabled(true);
        //simpleWebView.setHorizontalScrollBarEnabled(true);
        simpleWebView.requestFocus();
        simpleWebView.getSettings().setDomStorageEnabled(true);
        simpleWebView.getSettings().setDefaultTextEncodingName("utf-8");
        simpleWebView.getSettings().setJavaScriptEnabled(true);
        simpleWebView.setWebViewClient(new ViewKontrakPendanaanActivity.MyWebViewClient());

        simpleWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        btn_setuju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmSetuju();
            }
        });

        loadData();

    }

    private void confirmSetuju(){
        dialog.show();
        viewModel.getSetujuFunding(prefManager.getUid(), prefManager.getToken(), agreement_code);
        viewModel.getSetujuFundingResult().observe(ViewKontrakPendanaanActivity.this, showSetujuResult);
    }

    private Observer<JSONObject> showSetujuResult = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    JSONObject job = result.getJSONObject("result");
                    String doc_token = job.getString("token");
                    Intent intent = new Intent(ViewKontrakPendanaanActivity.this, SignerPendanaanActivity.class);
                    intent.putExtra("doc_token", doc_token);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                    finish();
                    dialog.cancel();
                }else{
                    dialog.cancel();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private void loadData(){
        dialog.show();
        viewModel.getViewKontrakFunding(prefManager.getUid(), prefManager.getToken(), agreement_code);
        viewModel.getViewKontrakFundingResult().observe(ViewKontrakPendanaanActivity.this, showResult);
    }

    private Observer<String> showResult = new Observer<String>() {
        @Override
        public void onChanged(String result) {
            if(result.isEmpty()){
                dialog.cancel();
            }else{
                simpleWebView.loadDataWithBaseURL(null, result, "text/HTML", "UTF-8", null);
                btn_setuju.setEnabled(true);
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
            btn_setuju.setEnabled(true);
        }
    }

    @Override
    public void onBackPressed() {

    }
}