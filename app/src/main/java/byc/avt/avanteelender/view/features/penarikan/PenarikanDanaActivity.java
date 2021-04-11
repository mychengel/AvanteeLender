package byc.avt.avanteelender.view.features.penarikan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.view.others.RiskInfoActivity;
import byc.avt.avanteelender.viewmodel.AuthenticationViewModel;
import byc.avt.avanteelender.viewmodel.DashboardViewModel;

public class PenarikanDanaActivity extends AppCompatActivity {

    Fungsi f = new Fungsi(PenarikanDanaActivity.this);
    Toolbar toolbar;
    private PrefManager prefManager;
    private Dialog dialog;
    private DashboardViewModel viewModel;
    private Button btn_next;
    private TextView txt_saldo_tersedia, txt_info_max_tarik;
    ImageView imgPlus, imgMinus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penarikan_dana);
        dialog = GlobalVariables.loadingDialog(PenarikanDanaActivity.this);
        viewModel = new ViewModelProvider(PenarikanDanaActivity.this).get(DashboardViewModel.class);
        toolbar = findViewById(R.id.toolbar_penarikan_dana);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_back_24px);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        prefManager = PrefManager.getInstance(PenarikanDanaActivity.this);
        txt_saldo_tersedia = findViewById(R.id.txt_total_saldo_penarikan_dana);
        txt_info_max_tarik = findViewById(R.id.lbl_info_max_tarik_penarikan_dana);
        imgPlus = findViewById(R.id.img_plus_penarikan_dana);
        imgMinus = findViewById(R.id.img_minus_penarikan_dana);

        imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        imgMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btn_next = findViewById(R.id.btn_next_penarikan_dana);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        loadData();
    }

    private void loadData() {
        dialog.show();
        viewModel.getRequestWithdrawal(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultRequestWithdrawal().observe(PenarikanDanaActivity.this, showData);
    }

    String vaNo="";
    private Observer<JSONObject> showData = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                int maxTarik = 0;
                int ewallet = result.getInt("ewallet");
                txt_saldo_tersedia.setText(f.toNumb(""+ewallet));
                if(ewallet < 1000000){
                    maxTarik = 0;
                }else{
                    maxTarik = ewallet - 6000;
                }
                txt_info_max_tarik.setText(getString(R.string.info_penarikan_dana_maks_1)+" "+f.toNumb(""+maxTarik)+" "+getString(R.string.info_penarikan_dana_maks_2));
                JSONObject job = result.getJSONObject("info_ewallet");
                vaNo = job.getString("va_no");
                Log.e("va_no", vaNo);
                dialog.cancel();
            } catch (JSONException e) {
                e.printStackTrace();
            }
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