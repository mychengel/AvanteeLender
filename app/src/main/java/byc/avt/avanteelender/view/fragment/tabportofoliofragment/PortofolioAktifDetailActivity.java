package byc.avt.avanteelender.view.fragment.tabportofoliofragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.adapter.PortofolioAktifAdapter;
import byc.avt.avanteelender.adapter.PortofolioAktifDetailAdapter;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.model.PortofolioAktif;
import byc.avt.avanteelender.model.PortofolioAktifDetail;
import byc.avt.avanteelender.view.features.pendanaan.PendanaanDetailActivity;
import byc.avt.avanteelender.viewmodel.PendanaanViewModel;
import byc.avt.avanteelender.viewmodel.tabportofolioviewmodel.AktifPortofolioViewModel;

public class PortofolioAktifDetailActivity extends AppCompatActivity {

    private AktifPortofolioViewModel viewModel;
    private PrefManager prefManager;
    private Dialog dialog;
    private Toolbar toolbar;
    private Fungsi f;
    private ImageView img_kontrak;
    private PortofolioAktif portAktif;
    private RecyclerView rv;
    TextView txt_loan_type, txt_loan_rating, txt_loan_no, txt_sisa_tenor, txt_tenor, txt_interest, txt_angs_paid, txt_angs_next, txt_is_ontime;
    ImageView img_mark;
    CardView cv_pb, cv_nom, cv_download_surat_kuasa_pemberi_dana, cv_download_agreement_penerima_dana;
    private String loan_no = "", funding_id = "";
    Boolean storageAccess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portofolio_aktif_detail);
        f = new Fungsi(PortofolioAktifDetailActivity.this);
        viewModel = new ViewModelProvider(PortofolioAktifDetailActivity.this).get(AktifPortofolioViewModel.class);
        prefManager = PrefManager.getInstance(PortofolioAktifDetailActivity.this);
        toolbar = findViewById(R.id.toolbar_port_aktif_det);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_back_24px);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        dialog = GlobalVariables.loadingDialog(PortofolioAktifDetailActivity.this);

        txt_loan_type = findViewById(R.id.txt_loan_type_port_aktif_det);
        txt_loan_rating = findViewById(R.id.txt_loan_rating_port_aktif_det);
        txt_loan_no = findViewById(R.id.txt_loan_code_port_aktif_det);
        txt_sisa_tenor = findViewById(R.id.txt_sisatenor_port_aktif_det);
        txt_tenor = findViewById(R.id.txt_tenor_port_aktif_det);
        txt_interest = findViewById(R.id.txt_bunga_port_aktif_det);
        txt_angs_paid = findViewById(R.id.txt_tot_angs_sudah_port_aktif_det);
        txt_angs_next = findViewById(R.id.txt_tot_angs_selanjutnya_port_aktif_det);
        img_mark = findViewById(R.id.img_mark_port_aktif_det);
        cv_pb = findViewById(R.id.cv_pb_received_port_aktif_det);
        cv_nom = findViewById(R.id.cv_nom_received_port_aktif_det);
        cv_download_surat_kuasa_pemberi_dana = findViewById(R.id.cv_download_surat_kuasa_pemberi_dana_port_aktif_det);
        cv_download_agreement_penerima_dana = findViewById(R.id.cv_download_agreement_penerima_dana_port_aktif_det);
        rv = findViewById(R.id.rv_port_aktif_det);

        Intent intent = getIntent();
        portAktif = intent.getParcelableExtra("port_data");
        loan_no = portAktif.getLoan_no();
        funding_id = portAktif.getFunding_id();
        //f.showMessage("CEK: "+loan_no+" - "+funding_id);

        txt_loan_type.setText(portAktif.getLoan_type());
        txt_loan_no.setText(portAktif.getLoan_no());
        txt_loan_rating.setText(portAktif.getLoan_rating());
        txt_tenor.setText(portAktif.getTenor()+" hari");
        txt_sisa_tenor.setText(portAktif.getSisa_tenor()+" hari");
        String interest="";
        if(portAktif.getInterest() == null){
            interest = "-";
        }else{
            interest = ""+ (int)Float.parseFloat(portAktif.getInterest());
        }
        txt_interest.setText(interest+"%");
        txt_angs_paid.setText(f.toNumb(portAktif.getAngs_paid()));
        txt_angs_next.setText(f.toNumb(portAktif.getAngs_next()));

        if(portAktif.getLoan_rating().charAt(0) == 'A'){
            img_mark.setImageDrawable(ContextCompat.getDrawable(PortofolioAktifDetailActivity.this, R.drawable.ic_loan_rating_a));
            cv_pb.setCardBackgroundColor(ContextCompat.getColor(PortofolioAktifDetailActivity.this, R.color.colorPrimaryA));
            cv_nom.setCardBackgroundColor(ContextCompat.getColor(PortofolioAktifDetailActivity.this, R.color.colorPrimaryA));
        }else if(portAktif.getLoan_rating().charAt(0) == 'B'){
            img_mark.setImageDrawable(ContextCompat.getDrawable(PortofolioAktifDetailActivity.this, R.drawable.ic_loan_rating_b));
            cv_pb.setCardBackgroundColor(ContextCompat.getColor(PortofolioAktifDetailActivity.this, R.color.colorPrimaryB));
            cv_nom.setCardBackgroundColor(ContextCompat.getColor(PortofolioAktifDetailActivity.this, R.color.colorPrimaryB));
        }else if(portAktif.getLoan_rating().charAt(0) == 'C'){
            img_mark.setImageDrawable(ContextCompat.getDrawable(PortofolioAktifDetailActivity.this, R.drawable.ic_loan_rating_c));
            cv_pb.setCardBackgroundColor(ContextCompat.getColor(PortofolioAktifDetailActivity.this, R.color.colorPrimaryC));
            cv_nom.setCardBackgroundColor(ContextCompat.getColor(PortofolioAktifDetailActivity.this, R.color.colorPrimaryC));
        }else if(portAktif.getLoan_rating().charAt(0) == 'D'){
            img_mark.setImageDrawable(ContextCompat.getDrawable(PortofolioAktifDetailActivity.this, R.drawable.ic_loan_rating_d));
            cv_pb.setCardBackgroundColor(ContextCompat.getColor(PortofolioAktifDetailActivity.this, R.color.colorPrimaryD));
            cv_nom.setCardBackgroundColor(ContextCompat.getColor(PortofolioAktifDetailActivity.this, R.color.colorPrimaryD));
        }else if(portAktif.getLoan_rating().charAt(0) == 'E'){
            img_mark.setImageDrawable(ContextCompat.getDrawable(PortofolioAktifDetailActivity.this, R.drawable.ic_loan_rating_e));
            cv_pb.setCardBackgroundColor(ContextCompat.getColor(PortofolioAktifDetailActivity.this, R.color.colorPrimaryE));
            cv_nom.setCardBackgroundColor(ContextCompat.getColor(PortofolioAktifDetailActivity.this, R.color.colorPrimaryE));
        }else{
        }

        cv_download_agreement_penerima_dana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if(storageAccess){
                        dialog.show();
                        viewModel.downloadSuratKuasaLoan(prefManager.getUid(), prefManager.getToken(), loan_no);
                        viewModel.getResultDownloadSuratKuasaLoan().observe(PortofolioAktifDetailActivity.this, showResultDownloadSuratKuasaLoan);
                    }else{
                        checkPermission();
                    }
                }else{
                    dialog.show();
                    viewModel.downloadSuratKuasaLoan(prefManager.getUid(), prefManager.getToken(), loan_no);
                    viewModel.getResultDownloadSuratKuasaLoan().observe(PortofolioAktifDetailActivity.this, showResultDownloadSuratKuasaLoan);
                }

            }
        });

        cv_download_surat_kuasa_pemberi_dana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if(storageAccess){
                        dialog.show();
                        viewModel.downloadAgreementFunding(prefManager.getUid(), prefManager.getToken(), funding_id);
                        viewModel.getResultDownloadAgreementFunding().observe(PortofolioAktifDetailActivity.this, showResultDownloadAgreementFunding);
                    }else{
                        checkPermission();
                    }
                }else{
                    dialog.show();
                    viewModel.downloadAgreementFunding(prefManager.getUid(), prefManager.getToken(), funding_id);
                    viewModel.getResultDownloadAgreementFunding().observe(PortofolioAktifDetailActivity.this, showResultDownloadAgreementFunding);
                }

            }
        });

        loadData();

    }

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    private void checkPermission(){
        checkStorageAccess();
        final int permission = ActivityCompat.checkSelfPermission(PortofolioAktifDetailActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PortofolioAktifDetailActivity.this, PERMISSIONS_STORAGE, 1);
        }else{

        }
    }

    private void checkStorageAccess(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if(Environment.isExternalStorageManager()) {
                storageAccess = true;
            } else {
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse(String.format("package:%s", getPackageName())));
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
                    Toast.makeText(PortofolioAktifDetailActivity.this, "Avantee Lender Apps membutuhkan ijin akses penyimpanan HP!", Toast.LENGTH_SHORT).show();
                    checkStorageAccess();
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private Observer<String> showResultDownloadSuratKuasaLoan = new Observer<String>() {
        @Override
        public void onChanged(String result) {
            dialog.cancel();
            new AlertDialog.Builder(PortofolioAktifDetailActivity.this)
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

    private Observer<String> showResultDownloadAgreementFunding = new Observer<String>() {
        @Override
        public void onChanged(String result) {
            dialog.cancel();
            new AlertDialog.Builder(PortofolioAktifDetailActivity.this)
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

    private void loadData() {
        dialog.show();
        viewModel.portofolioAktifDetailList(prefManager.getUid(), prefManager.getToken(), loan_no, funding_id);
        viewModel.getResultListDetail().observe(PortofolioAktifDetailActivity.this, showDataListDetail);
    }

    private Observer<ArrayList<PortofolioAktifDetail>> showDataListDetail = new Observer<ArrayList<PortofolioAktifDetail>>() {
        @Override
        public void onChanged(ArrayList<PortofolioAktifDetail> result) {
            rv.setLayoutManager(new LinearLayoutManager(PortofolioAktifDetailActivity.this));
            PortofolioAktifDetailAdapter portofolioAktifDetailAdapter = new PortofolioAktifDetailAdapter(PortofolioAktifDetailActivity.this);
            portofolioAktifDetailAdapter.setListPortofolioAktifDetail(result);
            rv.setAdapter(portofolioAktifDetailAdapter);
            dialog.cancel();
        }
    };

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