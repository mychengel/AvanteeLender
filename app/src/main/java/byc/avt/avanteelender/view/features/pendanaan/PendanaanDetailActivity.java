package byc.avt.avanteelender.view.features.pendanaan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Routes;
import byc.avt.avanteelender.model.Pendanaan;
import byc.avt.avanteelender.view.fragment.tabportofoliofragment.PortofolioAktifDetailActivity;
import byc.avt.avanteelender.view.others.FactsheetActivity;
import byc.avt.avanteelender.view.sheet.DanaiSheetFragment;
import byc.avt.avanteelender.view.sheet.HistoriPinjamanSheetFragment;
import byc.avt.avanteelender.view.sheet.RiskInfoSheetFragment;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.view.sheet.DetailPeminjamSheetFragment;
import byc.avt.avanteelender.viewmodel.PendanaanViewModel;

public class PendanaanDetailActivity extends AppCompatActivity {

    private PendanaanViewModel viewModel;
    private PrefManager prefManager;
    private Dialog dialog;
    private Toolbar toolbar;
    private Fungsi f;
    private ImageView img_factsheet, img_picture, img_mark, img_sisa_hari;
    private String loan_no = "", tenor = "", factsheet_url = "";
    private TextView txt_loan_rating, txt_loan_type, txt_loan_no, txt_rating, txt_bunga, txt_tenor, txt_nom, txt_est, txt_pemilik;
    private TextView txt_sisa_hari, txt_pub_start, txt_pub_end, txt_terkumpul, txt_percent_terkumpul, txt_penggunaan_pinjaman, txt_des_pinjaman;
    private ConstraintLayout cons, cons_det_peminjam, cons_his_pinjaman, cons_risk_info;
    private Button btn_danai;
    private ProgressBar prog, prog_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendanaan_detail);
        f = new Fungsi(PendanaanDetailActivity.this);
        viewModel = new ViewModelProvider(PendanaanDetailActivity.this).get(PendanaanViewModel.class);
        prefManager = PrefManager.getInstance(PendanaanDetailActivity.this);
        toolbar = findViewById(R.id.toolbar_pendanaan_det);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_back_24px);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        dialog = GlobalVariables.loadingDialog(PendanaanDetailActivity.this);

        loadForm();

        Intent intent = getIntent();
        loan_no = intent.getStringExtra("loan_no");
        tenor = intent.getStringExtra("tenor");

        img_factsheet = findViewById(R.id.img_factsheet_pendanaan_det);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        }, 400);

        btn_danai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDanai();
            }
        });

    }

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    private void checkPermission(){
        checkStorageAccess();
        final int permission = ActivityCompat.checkSelfPermission(PendanaanDetailActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PendanaanDetailActivity.this, PERMISSIONS_STORAGE, 1);
        }else{

        }
    }

    boolean storageAccess = false;
    private void checkStorageAccess(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if(Environment.isExternalStorageManager()) {
                storageAccess = true;
            } else {
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse(String.format("package:%s",PendanaanDetailActivity.this.getPackageName())));
                    startActivityForResult(intent, 2296);
                } catch (Exception e) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    startActivityForResult(intent, 2296);
                }
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2296) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    storageAccess = true;
                } else {
                    storageAccess = false;
                    Toast.makeText(PendanaanDetailActivity.this, "Avantee Lender Apps membutuhkan ijin akses penyimpanan HP!", Toast.LENGTH_SHORT).show();
                    checkStorageAccess();
                }
            }
        }
    }

    public void confirmDanai(){
        dialog.show();
        viewModel.getStageFunding(prefManager.getUid(), prefManager.getToken(), loan_no);
        viewModel.getStageFundingResult().observe(PendanaanDetailActivity.this, showStageFunding);
    }

    private Observer<JSONObject> showStageFunding = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    JSONObject res = result.getJSONObject("result");
                    Log.e("resultStage", res.toString());
                    DanaiSheetFragment.loan_no = loan_no;
                    new DanaiSheetFragment(res);
                    DanaiSheetFragment danaiSheetFragment = DanaiSheetFragment.getInstance();
                    danaiSheetFragment.show(getSupportFragmentManager(), danaiSheetFragment.getTag());
                }else{
                    f.showMessage(getString(R.string.failed_load_data));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dialog.cancel();
        }
    };

    private void loadForm(){
        cons = findViewById(R.id.cons_sv_pendanaan_det);
        cons.setVisibility(View.INVISIBLE);
        cons_det_peminjam = findViewById(R.id.cons_det_peminjam_pendanaan_det);
        cons_his_pinjaman = findViewById(R.id.cons_his_pinjaman_pendanaan_det);
        cons_risk_info = findViewById(R.id.cons_ir_pendanaan_det);
        txt_loan_rating = findViewById(R.id.txt_loan_rating_pendanaan_det);
        txt_rating = findViewById(R.id.txt_rating_pendanaan_det);
        txt_loan_no = findViewById(R.id.txt_loan_no_pendanaan_det);
        txt_loan_type = findViewById(R.id.txt_loan_type_pendanaan_det);
        txt_bunga = findViewById(R.id.txt_bunga_pendanaan_det);
        txt_tenor = findViewById(R.id.txt_tenor_pendanaan_det);
        img_picture = findViewById(R.id.img_pendanaan_det);
        img_mark = findViewById(R.id.img_mark_pendanaan_det);
        img_sisa_hari = findViewById(R.id.img_sisa_hari_pendanaan_det);
        txt_nom = findViewById(R.id.txt_nom_pinjaman_pendanaan_det);
        txt_est = findViewById(R.id.txt_est_pengembalian_pendanaan_det);
        txt_pemilik = findViewById(R.id.txt_nama_pemilik_proyek_pendanaan_det);
        txt_sisa_hari = findViewById(R.id.txt_sisa_hari_pendanaan_det);
        txt_pub_start = findViewById(R.id.txt_mulai_pendanaan_det);
        txt_pub_end = findViewById(R.id.txt_berakhir_pendanaan_det);
        txt_terkumpul = findViewById(R.id.txt_terkumpul_pendanaan_det);
        txt_percent_terkumpul = findViewById(R.id.txt_percent_terkumpul_pendanaan_det);
        txt_penggunaan_pinjaman = findViewById(R.id.txt_penggunaan_pinjaman_pendanaan_det);
        txt_des_pinjaman = findViewById(R.id.txt_des_pinjaman_pendanaan_det);
        prog = findViewById(R.id.progress_pendanaan_det);
        prog_img = findViewById(R.id.prog_img_pendanaan_det);
        btn_danai = findViewById(R.id.btn_danai_pendanaan_det);
    }

    private void loadData() {
        dialog.show();
        viewModel.getDetailPendanaan(prefManager.getUid(), prefManager.getToken(), loan_no);
        viewModel.getDetailPendanaanResult().observe(PendanaanDetailActivity.this, showDetailPendanaan);
    }

    private Observer<String> showResultDownloadFactsheet = new Observer<String>() {
        @Override
        public void onChanged(String result) {
            dialog.cancel();
            new AlertDialog.Builder(PendanaanDetailActivity.this)
                    .setTitle("Konfirmasi")
                    .setIcon(R.drawable.logo)
                    .setMessage(result)
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    })
                    .create()
                    .show();
        }
    };

    private Observer<JSONObject> showDetailPendanaan = new Observer<JSONObject>() {
        @Override
        public void onChanged(final JSONObject result) {
            try {
                if(result.getBoolean("status") == false){
                    f.showMessage(getString(R.string.failed_load_data));
                }else{
                    final JSONObject res = result.getJSONObject("result");
                    factsheet_url = res.getString("factsheet");
                    img_factsheet.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            Intent i = new Intent(PendanaanDetailActivity.this, FactsheetActivity.class);
//                            i.putExtra("factsheet", factsheet_url);
//                            startActivity(i);
//                            overridePendingTransition(R.anim.enter, R.anim.exit);
                            checkPermission();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                if(storageAccess){
                                    dialog.show();
                                    viewModel.downloadFactsheet(prefManager.getUid(), prefManager.getToken(), loan_no);
                                    viewModel.getResultDownloadFactsheet().observe(PendanaanDetailActivity.this, showResultDownloadFactsheet);
                                }else{
                                    checkPermission();
                                }
                            }else{
                                dialog.show();
                                viewModel.downloadFactsheet(prefManager.getUid(), prefManager.getToken(), loan_no);
                                viewModel.getResultDownloadFactsheet().observe(PendanaanDetailActivity.this, showResultDownloadFactsheet);
                            }
                        }
                    });
                    txt_loan_rating.setText(res.getString("loan_rating"));
                    txt_rating.setText(res.getString("text_rating"));
                    setMark(res.getString("loan_rating"), f.selisihHari(res.getString("publikasi_end")));
                    txt_loan_no.setText(res.getString("loan_no"));
                    txt_loan_type.setText(res.getString("loan_type"));
                    txt_bunga.setText(""+ (int) Float.parseFloat(res.getString("interest")));
                    txt_tenor.setText(tenor);
                    txt_nom.setText(f.toNumb(res.getString("nominal_loan")));
                    txt_est.setText(f.tglFull(res.getString("paid_of_date")));
                    txt_pemilik.setText(res.getJSONObject("loan_information").getString("project_owner"));
                    txt_sisa_hari.setText(f.selisihHari(res.getString("publikasi_end"))+" hari lagi");
                    txt_pub_start.setText(f.tglFull(res.getString("publikasi_start")));
                    txt_pub_end.setText(f.tglFull(res.getString("publikasi_end")));
                    txt_terkumpul.setText(f.toNumb(res.getString("funding")));
                    txt_percent_terkumpul.setText(f.terkumpulPersen(res.getString("nominal_loan"),res.getString("funding"))+"%");
                    txt_penggunaan_pinjaman.setText("-"); //datanya ambil yang mana?
                    txt_des_pinjaman.setText(res.getJSONObject("loan_information").getString("deskripsi_pinjaman"));

                    prog.setProgress(f.terkumpulPersen(res.getString("nominal_loan"),res.getString("funding")));
                    Glide.with(PendanaanDetailActivity.this).load(res.getString("picture_bg"))
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
                                    img_picture.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                    prog_img.setVisibility(View.INVISIBLE);
                                    return false;
                                }
                            })
                            .into(img_picture);

                    cons.setVisibility(View.VISIBLE);

                    cons_det_peminjam.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                new DetailPeminjamSheetFragment(res.getJSONObject("borrower_information").getString("company_type"),res.getJSONObject("borrower_information").getString("company_established_year"),res.getJSONObject("borrower_information").getString("business_type"),res.getJSONObject("borrower_information").getString("loan_description"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            DetailPeminjamSheetFragment detailPeminjamSheetFragment = DetailPeminjamSheetFragment.getInstance();
                            detailPeminjamSheetFragment.show(getSupportFragmentManager(), detailPeminjamSheetFragment.getTag());
                        }
                    });

                    cons_risk_info.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                String str_disclaimer = res.getJSONObject("risk_information").getString("risk_disclaimer");
                                String str_risk_desc = res.getJSONObject("risk_information").getString("risk_information");
                                new RiskInfoSheetFragment(str_risk_desc, str_disclaimer);
                                //String risk_info = f.htmlToStr(str_edit).toString();
//                                new RiskInfoSheetFragment(risk_info);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            RiskInfoSheetFragment riskInfoSheetFragment = RiskInfoSheetFragment.getInstance();
                            riskInfoSheetFragment.show(getSupportFragmentManager(), riskInfoSheetFragment.getTag());
                        }
                    });

                    cons_his_pinjaman.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                new HistoriPinjamanSheetFragment(res.getJSONObject("borrower_information").getString("paid_off_frequency")+" unit",res.getJSONObject("borrower_information").getString("late_frequency")+" unit",f.toNumb(res.getJSONObject("borrower_information").getString("funding_process")),f.toNumb(res.getJSONObject("borrower_information").getString("current_loan")), f.toNumb(res.getJSONObject("borrower_information").getString("loan_paid_off")));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            HistoriPinjamanSheetFragment historiPinjamanSheetFragment = HistoriPinjamanSheetFragment.getInstance();
                            historiPinjamanSheetFragment.show(getSupportFragmentManager(), historiPinjamanSheetFragment.getTag());
                        }
                    });

                    //txt_tenor.setText(""+f.selisihHari2(res.getString("publikasi_end"), res.getString("paid_of_date")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dialog.cancel();
        }
    };

    private void setMark(String rating, int selisihHari){
        if(rating.charAt(0) == 'A'){
            img_mark.setImageDrawable(ContextCompat.getDrawable(PendanaanDetailActivity.this, R.drawable.ic_loan_rating_a));
        }else if(rating.charAt(0) == 'B'){
            img_mark.setImageDrawable(ContextCompat.getDrawable(PendanaanDetailActivity.this, R.drawable.ic_loan_rating_b));
        }else if(rating.charAt(0) == 'C'){
            img_mark.setImageDrawable(ContextCompat.getDrawable(PendanaanDetailActivity.this, R.drawable.ic_loan_rating_c));
        }else if(rating.charAt(0) == 'D'){
            img_mark.setImageDrawable(ContextCompat.getDrawable(PendanaanDetailActivity.this, R.drawable.ic_loan_rating_d));
        }else if(rating.charAt(0) == 'E'){
            img_mark.setImageDrawable(ContextCompat.getDrawable(PendanaanDetailActivity.this, R.drawable.ic_loan_rating_e));
        }else{
        }

        if(selisihHari >= 5){
            img_sisa_hari.setImageDrawable(ContextCompat.getDrawable(PendanaanDetailActivity.this, R.drawable.ic_sisa_7hari));
        }else if(selisihHari >= 3 && selisihHari < 5){
            img_sisa_hari.setImageDrawable(ContextCompat.getDrawable(PendanaanDetailActivity.this, R.drawable.ic_sisa_4hari));
        }else if(selisihHari >= 0 && selisihHari < 3){
            img_sisa_hari.setImageDrawable(ContextCompat.getDrawable(PendanaanDetailActivity.this, R.drawable.ic_sisa_2hari));
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            new Routes(PendanaanDetailActivity.this).moveOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new Routes(PendanaanDetailActivity.this).moveOut();
    }
}