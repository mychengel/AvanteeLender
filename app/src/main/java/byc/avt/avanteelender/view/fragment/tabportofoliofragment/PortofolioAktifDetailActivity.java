package byc.avt.avanteelender.view.fragment.tabportofoliofragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.util.Objects;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.model.PortofolioAktif;
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

    private String loan_no = "", funding_id = "";

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

        Intent intent = getIntent();
        portAktif = intent.getParcelableExtra("port_data");
        loan_no = portAktif.getLoan_no();
        funding_id = portAktif.getFunding_id();
        f.showMessage("CEK: "+loan_no+" - "+funding_id);

        img_kontrak = findViewById(R.id.img_kontrak_port_aktif_det);
        img_kontrak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

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