package byc.avt.avanteelender.view.misc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Routes;
import byc.avt.avanteelender.view.features.pendanaan.PendanaanDetailActivity;

public class PDFViewerActivity extends AppCompatActivity {

    private static final String TAG = "PDFViewerActivity";
    public static final String PDF_URL = "pdf_url";
    public static final String ACTIVITY_TITLE = "activity_title";
    private PDFView pdfView;
    private CircularProgressIndicator progressIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfviewer);

        Toolbar toolbar = findViewById(R.id.pdfView_toolbar);
        pdfView = findViewById(R.id.pdfView);
        progressIndicator = findViewById(R.id.progress_circular);
        progressIndicator.setVisibility(View.VISIBLE);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_back_24px);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().getStringExtra(ACTIVITY_TITLE) != null) {
            String title = getIntent().getStringExtra(ACTIVITY_TITLE);
            if (!title.isEmpty()) {
                getSupportActionBar().setTitle(title);
            }
        } else {
            getSupportActionBar().setTitle("");
        }

        if (getIntent().getStringExtra(PDF_URL) != null) {
            String pdfURL = getIntent().getStringExtra(PDF_URL);
            if (!pdfURL.isEmpty()) {
                new LoadPdfFromUrl(this).execute(pdfURL);
            }
        } else {
            Toast.makeText(PDFViewerActivity.this,"PDF URL Not Found",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            new Routes(PDFViewerActivity.this).moveOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new Routes(PDFViewerActivity.this).moveOut();
    }

    private static class LoadPdfFromUrl extends AsyncTask<String, Void, InputStream> implements OnLoadCompleteListener, OnErrorListener {

        private final WeakReference<PDFViewerActivity> activityReference;

        public LoadPdfFromUrl(PDFViewerActivity context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;

            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (IOException e) {
                e.printStackTrace();
                return  null;
            }

            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            PDFViewerActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) return;

            activity.pdfView.fromStream(inputStream).onLoad(this).onError(this).load();
        }

        @Override
        public void loadComplete(int nbPages) {
            PDFViewerActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) return;

            activity.progressIndicator.setVisibility(View.GONE);
        }

        @Override
        public void onError(Throwable t) {
            PDFViewerActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) return;

            activity.progressIndicator.setVisibility(View.GONE);
            Toast.makeText(activity,"Error:" + t.getMessage(),Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onError-AsyncTaskLoadPDF: " + t.getMessage());
        }
    }
}