package byc.avt.avanteelender.view.features.account.individual;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.viewmodel.AuthenticationViewModel;
import byc.avt.avanteelender.viewmodel.MasterDataViewModel;

public class DataPendukungShowActivity extends AppCompatActivity {

    Fungsi f = new Fungsi(DataPendukungShowActivity.this);
    private Toolbar toolbar;
    JSONObject job, job2;
    TextView txt_no_ktp, txt_no_npwp;
    TextInputLayout edit_ktp, edit_npwp, edit_tgl_npwp;
    ImageView img_ktp, img_npwp, img_selfie, img_spesimen_ttd;
    LinearLayout linDoc;
    CheckBox cb_not_have_npwp;
    Button btn_save, btn_edit;
    boolean editIsOn = false;

    private MasterDataViewModel viewModel;
    private AuthenticationViewModel viewModel2;
    private PrefManager prefManager;
    private Dialog dialog;
    GlobalVariables gv;
    EditText edittext_tgl_npwp;
    TextView txt_ktp, txt_npwp, txt_selfie, txt_ttd;
    CardView cv_ktp, cv_npwp, cv_selfie, cv_ttd;
    LinearLayout lin_npwp;
    boolean is_not_have_npwp = false;
    Button btn_next;
    String no_ktp = "", no_npwp = "", tgl_npwp = "";
    byte[] ktp_byte = null, npwp_byte = null, selfie_byte = null, ttd_byte = null;

    Bitmap bitmap, decoded_ktp, decoded_npwp, decoded_selfie, decoded_ttd;
    String str_ktp = "", str_npwp = "", str_selfie = "", str_ttd = "";
    int PICK_KTP = 1, PICK_NPWP = 2, PICK_SELFIE = 3, PICK_TTD = 4, PICK_KTP_CAM = 5, PICK_NPWP_CAM = 6, PICK_SELFIE_CAM = 7, PICK_TTD_CAM = 8;
    String PICK_TYPE_KTP = "ktp", PICK_TYPE_NPWP = "npwp", PICK_TYPE_SELFIE = "selfie", PICK_TYPE_TTD = "ttd";
    int BITMAP_SIZE = 60, MAX_SIZE = 512;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_pendukung_show);
        toolbar = findViewById(R.id.toolbar_ess_document_show);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_back_24px);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        edit_ktp = findViewById(R.id.edit_ktp_number_ess_document_show);
        edit_npwp = findViewById(R.id.edit_npwp_number_ess_document_show);
        edit_tgl_npwp = findViewById(R.id.edit_npwp_date_ess_document_show);
        cv_ktp = findViewById(R.id.cv_img_ktp_ess_document_show);
        cv_npwp = findViewById(R.id.cv_img_npwp_ess_document_show);
        cv_selfie = findViewById(R.id.cv_img_ktp_ess_document_show);
        cv_ttd = findViewById(R.id.cv_img_ttd_ess_document_show);
        cb_not_have_npwp = findViewById(R.id.cb_not_have_npwp_ess_document_show);
        //txt_no_npwp = findViewById(R.id.txt_no_npwp_ess_document_show);
        img_ktp = findViewById(R.id.img_ktp_ess_document_show);
        img_npwp = findViewById(R.id.img_npwp_ess_document_show);
        img_selfie = findViewById(R.id.img_selfie_ess_document_show);
        img_spesimen_ttd = findViewById(R.id.img_ttd_ess_document_show);
        linDoc = findViewById(R.id.lin_npwp_ess_document_show);

        Intent i = getIntent();
        try {
            job = new JSONObject(i.getStringExtra("jobEssDocument"));
            job2 = new JSONObject(i.getStringExtra("jobDataPribadi"));
            edit_ktp.getEditText().setText(job.getString("ktp_no"));
            edit_npwp.getEditText().setText(job.getString("npwp_no"));
            edit_tgl_npwp.getEditText().setText(job.getString("tanggal_daftar_npwp"));
            if(job.getString("npwp_no").isEmpty() || job.getString("tanggal_daftar_npwp") == null){
                linDoc.setVisibility(View.GONE);
            }else{linDoc.setVisibility(View.VISIBLE);}

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

        btn_save = findViewById(R.id.btn_save_ess_document_show);
        btn_save.setEnabled(true);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //confirmNext(v);
            }
        });

        cb_not_have_npwp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });

        btn_edit = findViewById(R.id.btn_ubah_ess_document_show);
        btn_edit.setEnabled(true);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editIsOn = !editIsOn;
                editIsOn(editIsOn);
            }
        });

        editIsOn(false);

    }

    public void editIsOn(boolean s){
        edit_ktp.setEnabled(s);
        edit_npwp.setEnabled(s);
        edit_tgl_npwp.setEnabled(s);
        img_ktp.setEnabled(s);
        img_npwp.setEnabled(s);
        img_selfie.setEnabled(s);
        img_spesimen_ttd.setEnabled(s);
        linDoc.setEnabled(s);
        cb_not_have_npwp.setEnabled(s);
        int elev = 0;
        if(s){
            elev = 8;
        }else{
            elev = 0;
        }
        cv_ktp.setCardElevation(elev);
        cv_npwp.setCardElevation(elev);
        cv_selfie.setCardElevation(elev);
        cv_ttd.setCardElevation(elev);
        btn_save.setEnabled(s);

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