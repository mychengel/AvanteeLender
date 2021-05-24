package byc.avt.avanteelender.view.sheet;

import android.graphics.Bitmap;
import android.os.Bundle;

import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.view.others.RiskInfoActivity;

public class RiskInfoSheetFragment extends BottomSheetDialogFragment {

    private static RiskInfoSheetFragment instance;
    public static String risk_info;
    public static String risk_disclaimer;
    public String result;

    TextView txt_risk_info;

    public RiskInfoSheetFragment() {
    }

    public RiskInfoSheetFragment(String risk_info, String risk_disclaimer){
        this.risk_info = risk_info;
        this.risk_disclaimer = risk_disclaimer;
    }

    public static RiskInfoSheetFragment getInstance() {
        instance = null;
        instance = new RiskInfoSheetFragment();
        return instance;
    }

    private WebView simpleWebView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_sheet_risk_info, container, false);
        txt_risk_info = view.findViewById(R.id.txt_risk_info_fr_sheet_risk_info);
        //String risk_info_final = new Fungsi().htmlToStr(risk_info).toString();
        //txt_risk_info.setText(risk_info_final);

        simpleWebView = view.findViewById(R.id.wv_fr_sheet_risk_info);
        simpleWebView.setNestedScrollingEnabled(true);
        simpleWebView.setVerticalScrollBarEnabled(true);
        simpleWebView.requestFocus();
        simpleWebView.getSettings().setDomStorageEnabled(true);
        simpleWebView.getSettings().setDefaultTextEncodingName("utf-8");
        simpleWebView.getSettings().setJavaScriptEnabled(true);
        simpleWebView.setWebViewClient(new MyWebViewClient());
        simpleWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        result = "<br>" +
                "<h3>Deskripsi Risiko</h3>" +
                risk_info + "<br><br>" +
                "<h3><i>Disclaimer</i> Risiko</h3>" +
                risk_disclaimer + "<br>";

        simpleWebView.loadDataWithBaseURL(null, result, "text/HTML", "UTF-8", null);

        return view;
    }

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
            //dialog.cancel();
        }
    }
}