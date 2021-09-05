package byc.avt.avanteelender.view.others;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
//import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.view.fragment.tabnotificationfragment.InfoNotificationsFragment;

public class FactsheetActivity extends AppCompatActivity {

    private ProgressBar prog;
    private TextView lbl_info;
    private Toolbar toolbar;
    //private PDFView pdf;
    private LottieAnimationView lottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factsheet);
        prog = findViewById(R.id.prog_factsheet);
        toolbar = findViewById(R.id.toolbar_factsheet);
        //pdf = findViewById(R.id.pdfviewer_factsheet);
        lbl_info = findViewById(R.id.lbl_info_factsheet);
        lbl_info.setVisibility(View.GONE);
        lottie = findViewById(R.id.lottie_factsheet);
        lottie.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_back_24px);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Factsheet");
        String url = "";
        Intent intent = getIntent();
        url = intent.getStringExtra("factsheet");
        Log.e("URL PDF", url);
        try{
            new RetrievePdfStream().execute(url);
        }
        catch (Exception e){
            lbl_info.setVisibility(View.VISIBLE);
            lottie.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }

    class RetrievePdfStream extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;

            URL url = null;
            try {
                url = new URL(strings[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                lbl_info.setVisibility(View.VISIBLE);
                lottie.setVisibility(View.VISIBLE);
            }
            try{
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }
            }catch (Exception e){
                lbl_info.setVisibility(View.VISIBLE);
                lottie.setVisibility(View.VISIBLE);
            }

            return inputStream;
        }
        @Override
        protected void onPostExecute(InputStream inputStream) {
            //pdf.fromStream(inputStream).load();
        }
    }
}
