package byc.avt.avanteelender.view.features.account;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.view.others.BlogDetailActivity;

public class DataPendukungShowActivity extends AppCompatActivity {

    Fungsi f = new Fungsi(DataPendukungShowActivity.this);
    private Toolbar toolbar;
    JSONObject job, job2;
    TextView txt_no_ktp, txt_no_npwp;
    ImageView img_ktp, img_npwp, img_selfie, img_spesimen_ttd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_pendukung_show);
        toolbar = findViewById(R.id.toolbar_ess_document_show);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_back_24px);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txt_no_ktp = findViewById(R.id.txt_no_ktp_ess_document_show);
        txt_no_npwp = findViewById(R.id.txt_no_npwp_ess_document_show);
        img_ktp = findViewById(R.id.img_ktp_ess_document_show);
        img_npwp = findViewById(R.id.img_npwp_ess_document_show);
        img_selfie = findViewById(R.id.img_selfie_ess_document_show);
        img_spesimen_ttd = findViewById(R.id.img_ttd_ess_document_show);

        Intent i = getIntent();
        try {
            job = new JSONObject(i.getStringExtra("jobEssDocument"));
            job2 = new JSONObject(i.getStringExtra("jobDataPribadi"));
            txt_no_ktp.setText(job.getString("ktp_no"));
            if(job.getString("npwp_no").equalsIgnoreCase("")){
                txt_no_npwp.setText("-");
            }else{
                txt_no_npwp.setText(job.getString("npwp_no"));
            }


            Glide.with(DataPendukungShowActivity.this).load(job.getString("ktp_img"))
                    .placeholder(R.drawable.ic_baseline_no_photography_24)
                    .error(R.drawable.ic_baseline_no_photography_24)
                    .dontAnimate()
                    .apply(RequestOptions.skipMemoryCacheOf(true))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }
                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            img_ktp.setScaleType(ImageView.ScaleType.FIT_XY);
                            return false;
                        }
                    })
                    .into(img_ktp);

            Glide.with(DataPendukungShowActivity.this).load(job.getString("npwp_img"))
                    .placeholder(R.drawable.ic_baseline_no_photography_24)
                    .error(R.drawable.ic_baseline_no_photography_24)
                    .dontAnimate()
                    .apply(RequestOptions.skipMemoryCacheOf(true))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }
                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            img_npwp.setScaleType(ImageView.ScaleType.FIT_XY);
                            return false;
                        }
                    })
                    .into(img_npwp);

            Glide.with(DataPendukungShowActivity.this).load(job2.getString("photo_selfie"))
                    .placeholder(R.drawable.ic_baseline_no_photography_24)
                    .error(R.drawable.ic_baseline_no_photography_24)
                    .dontAnimate()
                    .apply(RequestOptions.skipMemoryCacheOf(true))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }
                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            img_selfie.setScaleType(ImageView.ScaleType.FIT_XY);
                            return false;
                        }
                    })
                    .into(img_selfie);

            Glide.with(DataPendukungShowActivity.this).load(job.getString("spesimen_ttd_img"))
                    .placeholder(R.drawable.ic_baseline_no_photography_24)
                    .error(R.drawable.ic_baseline_no_photography_24)
                    .dontAnimate()
                    .apply(RequestOptions.skipMemoryCacheOf(true))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }
                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            img_spesimen_ttd.setScaleType(ImageView.ScaleType.FIT_XY);
                            return false;
                        }
                    })
                    .into(img_spesimen_ttd);

        } catch (JSONException e) {
            e.printStackTrace();
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
}