package byc.avt.avanteelender.view.features.penarikan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.view.others.RiskInfoActivity;
import byc.avt.avanteelender.view.sheet.ConfirmationSheetFragment;
import byc.avt.avanteelender.view.sheet.WithdrawalConfirmationSheetFragment;
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
    EditText edit_nominal;
    long nominal_tarik_int = 0;
    String nominal_tarik_show="", current="";
    int ewallet = 0, maxTarik = 0;
    String namaBank="", noRek="", vaNo="", namaPemilikBank="";

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

        edit_nominal = findViewById(R.id.edit_nom_penarikan_dana);
        edit_nominal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(!s.toString().equals(current)){
                    edit_nominal.removeTextChangedListener(this);
                    String cleanString = s.toString().replaceAll("[$,.]", "");
                    if(cleanString.isEmpty() || cleanString.equalsIgnoreCase("")){
                        nominal_tarik_int = 0;
                        nominal_tarik_show = f.toNumb(cleanString);
                        nominal_tarik_show = nominal_tarik_show.substring(2, nominal_tarik_show.length());
                    }else{
                        try {
                            nominal_tarik_int = Long.parseLong(cleanString);
                            nominal_tarik_show = f.toNumb(cleanString);
                            nominal_tarik_show = nominal_tarik_show.substring(2, nominal_tarik_show.length());
                        }catch (Exception e){
                            nominal_tarik_int = ewallet - (ewallet % 1000000);
                            nominal_tarik_show = f.toNumb(""+nominal_tarik_int);
                            nominal_tarik_show = nominal_tarik_show.substring(2, nominal_tarik_show.length());
                            f.showMessage(getString(R.string.reach_limit_number));
                        }
                    }

                    current = nominal_tarik_show;
                    edit_nominal.setText(nominal_tarik_show);
                    edit_nominal.setSelection(nominal_tarik_show.length());
                    edit_nominal.addTextChangedListener(this);
                    cekDone();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nominal_tarik_int > ewallet){
                    nominal_tarik_int = ewallet - (ewallet % 1000000);
                    nominal_tarik_show = f.toNumb(""+nominal_tarik_int);
                    nominal_tarik_show = nominal_tarik_show.substring(2, nominal_tarik_show.length());
                    edit_nominal.setText(nominal_tarik_show);
                    edit_nominal.setSelection(nominal_tarik_show.length());
                    f.showMessage(getString(R.string.reach_limit_withdrawal));
                }else{
                    if((nominal_tarik_int + 1000000) < ewallet){
                        nominal_tarik_int = nominal_tarik_int - (nominal_tarik_int % 1000000) + 1000000;
                        nominal_tarik_show = f.toNumb(""+nominal_tarik_int);
                        nominal_tarik_show = nominal_tarik_show.substring(2, nominal_tarik_show.length());
                        edit_nominal.setText(nominal_tarik_show);
                        edit_nominal.setSelection(nominal_tarik_show.length());
                    }else{
                        f.showMessage(getString(R.string.reach_limit_withdrawal));
                    }
                }
                cekDone();
            }
        });

        imgMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nominal_tarik_int > ewallet){
                    nominal_tarik_int = ewallet - (ewallet % 1000000);
                    nominal_tarik_show = f.toNumb(""+nominal_tarik_int);
                    nominal_tarik_show = nominal_tarik_show.substring(2, nominal_tarik_show.length());
                    edit_nominal.setText(nominal_tarik_show);
                    edit_nominal.setSelection(nominal_tarik_show.length());
                }else{
                    if((nominal_tarik_int - 1000000) >= 1000000){
                        long nom_mod = nominal_tarik_int % 1000000;
                        if(nom_mod == 0){
                            nominal_tarik_int = nominal_tarik_int - 1000000;
                        }else{
                            nominal_tarik_int = nominal_tarik_int - nom_mod;
                        }
                        nominal_tarik_show = f.toNumb(""+nominal_tarik_int);
                        nominal_tarik_show = nominal_tarik_show.substring(2, nominal_tarik_show.length());
                        edit_nominal.setText(nominal_tarik_show);
                        edit_nominal.setSelection(nominal_tarik_show.length());
                    }else{
                        nominal_tarik_int = 0;
                        nominal_tarik_show = f.toNumb(""+nominal_tarik_int);
                        nominal_tarik_show = nominal_tarik_show.substring(2, nominal_tarik_show.length());
                        edit_nominal.setText(nominal_tarik_show);
                        edit_nominal.setSelection(nominal_tarik_show.length());
                    }
                }
                cekDone();
            }
        });

        btn_next = findViewById(R.id.btn_next_penarikan_dana);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new WithdrawalConfirmationSheetFragment((int) nominal_tarik_int, namaBank, noRek, namaPemilikBank, vaNo);
                WithdrawalConfirmationSheetFragment withdrawalConfirmationSheetFragment = WithdrawalConfirmationSheetFragment.getInstance();
                withdrawalConfirmationSheetFragment.show(getSupportFragmentManager(), withdrawalConfirmationSheetFragment.getTag());
            }
        });

        loadData();
    }

    private void cekDone(){
        if(nominal_tarik_int >= 1000000 && (nominal_tarik_int % 1000000) == 0 && nominal_tarik_int <= ewallet){
            btn_next.setEnabled(true);
        }else {
            btn_next.setEnabled(false);
        }
    }

    private void loadData() {
        dialog.show();
        viewModel.getRequestWithdrawal(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultRequestWithdrawal().observe(PenarikanDanaActivity.this, showData);
    }

    private Observer<JSONObject> showData = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                maxTarik = 0;
                ewallet = result.getInt("ewallet");
                txt_saldo_tersedia.setText(f.toNumb(""+ewallet));
                if(ewallet < 1000000){
                    maxTarik = 0;
                }else{
                    maxTarik = ewallet - 6000;
                }
                txt_info_max_tarik.setText(getString(R.string.info_penarikan_dana_maks_1)+" "+f.toNumb(""+maxTarik)+" "+getString(R.string.info_penarikan_dana_maks_2));
                JSONObject job = result.getJSONObject("info_ewallet");
                vaNo = job.getString("va_no");
                namaBank = job.getString("bank_name");
                noRek = job.getString("bank_account_no");
                namaPemilikBank = job.getString("bank_account_holder");
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