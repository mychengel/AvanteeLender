package byc.avt.avanteelender.view.features.account.individual;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.helper.Routes;
import byc.avt.avanteelender.viewmodel.AuthenticationViewModel;
import byc.avt.avanteelender.viewmodel.MasterDataViewModel;

public class BankInfoShowActivity extends AppCompatActivity {

    private Toolbar toolbar;
    JSONObject job;
    private PrefManager prefManager;
    private Dialog dialog;
    AutoCompleteTextView auto_bank, auto_avg_trans;
    TextInputLayout txtBank, txtAccountName, txtAccountNumber, txtAvgTrans;
    GlobalVariables gv;
    private MasterDataViewModel viewModel;
    private AuthenticationViewModel viewModel2;
    Button btn_save;

    CheckBox cb_owner_name_same_as_name;

    String bank="", accountName="", accountNumber="", avgTrans="";
    List<Object> listBank = new ArrayList<>(); List<Object> listBankID = new ArrayList<>();
    List<Object> listAvgTrans = new ArrayList<>(); List<Object> listAvgTransID = new ArrayList<>();
    String is_same_name = "0", name_tmp = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_info_show);
        gv.perEditData.clear();
        gv.perEditDataFile.clear();
        viewModel = new ViewModelProvider(this).get(MasterDataViewModel.class);
        viewModel2 = new ViewModelProvider(this).get(AuthenticationViewModel.class);
        toolbar = findViewById(R.id.toolbar_bank_info_show);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_back_24px);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        prefManager = PrefManager.getInstance(BankInfoShowActivity.this);
        dialog = GlobalVariables.loadingDialog(BankInfoShowActivity.this);

        cb_owner_name_same_as_name = findViewById(R.id.cb_owner_name_same_as_name_fr_bank_info_show);
        auto_bank = findViewById(R.id.auto_bank_name_fr_bank_info_show);
        auto_avg_trans = findViewById(R.id.auto_avg_transaction_fr_bank_info_show);
        txtBank = findViewById(R.id.edit_bank_name_fr_bank_info_show);
        txtAccountName = findViewById(R.id.edit_bank_owner_name_fr_bank_info_show);
        txtAccountNumber = findViewById(R.id.edit_bank_account_number_fr_bank_info_show);
        txtAvgTrans = findViewById(R.id.edit_avg_transaction_fr_bank_info_show);

        Intent i = getIntent();
        try {
            job = new JSONObject(i.getStringExtra("jobBankInfo"));
            txtBank.getEditText().setText(job.getString("bank_name"));
            txtAccountName.getEditText().setText(job.getString("bank_account"));
            name_tmp = job.getString("bank_account");
            txtAccountNumber.getEditText().setText(job.getString("bank_account_no"));
            txtAvgTrans.getEditText().setText(job.getString("average_transaction").replaceAll("[lgt;&]", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        btn_save = findViewById(R.id.btn_save_fr_bank_info_show);
        btn_save.setEnabled(true);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmNext(v);
            }
        });

        cb_owner_name_same_as_name.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    txtAccountName.getEditText().setText(prefManager.getName());
                    is_same_name = "1";
                }else{
                    txtAccountName.getEditText().setText(name_tmp);
                    is_same_name = "0";
                }
            }
        });

        loadData();

    }

    private void confirmNext(View v){
        accountName = Objects.requireNonNull(txtAccountName.getEditText().getText().toString().trim());
        accountNumber = Objects.requireNonNull(txtAccountNumber.getEditText().getText().toString().trim());
        if(!bank.isEmpty() && !accountName.isEmpty() && !accountNumber.isEmpty() && !avgTrans.isEmpty()){
            gv.perEditData.put("event_name","data_rekening");
            gv.perEditData.put("tipe_investor",prefManager.getClientType());
            gv.perEditData.put("bank", bank);
            gv.perEditData.put("nama_pemilik_rekening", accountName);
            gv.perEditData.put("no_rekening", accountNumber);
            gv.perEditData.put("rata_rata_transaksi", avgTrans);
            gv.perEditData.put("sesuai_nama", is_same_name);
            setNoError();
            updateDocument();
        }else{
            cekError();
        }
    }

    private void updateDocument(){
        new AlertDialog.Builder(BankInfoShowActivity.this)
                .setTitle("Konfirmasi")
                .setIcon(R.drawable.logo)
                .setMessage(getString(R.string.update_doc_confirmation))
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        dialog.show();
                        viewModel2.updatePersonalDoc(prefManager.getUid(), prefManager.getToken());
                        viewModel2.getResultUpdatePersonalDoc().observe(BankInfoShowActivity.this, showResult);
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create()
                .show();
    }

    private Observer<JSONObject> showResult = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    new Fungsi(BankInfoShowActivity.this).showMessage(getString(R.string.success_update_data));
                    dialog.cancel();
                    new Routes(BankInfoShowActivity.this).moveOut();
                }else{
                    new Fungsi(BankInfoShowActivity.this).showMessage(getString(R.string.failed_update_data));
                    dialog.cancel();
                    //new Routes(PersonalDataShowActivity.this).moveOut();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                String msg = getString(R.string.system_in_trouble);
                Log.e("Respon per cr doc", msg);
                new Fungsi(BankInfoShowActivity.this).showMessage(msg);
                dialog.cancel();
                //new Routes(PersonalDataShowActivity.this).moveOut();
            }
        }
    };

    public void loadData(){
        clearMasterList();
        dialog.show();
        viewModel.getBank(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultBank().observe(BankInfoShowActivity.this, showBank);
        viewModel.getAvgTransaction(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultAvgTransaction().observe(BankInfoShowActivity.this, showAvgTrans);
    }

    private Observer<JSONObject> showBank = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    JSONObject jobRes = result.getJSONObject("result");
                    JSONArray jar = jobRes.getJSONArray("bank");
                    for(int i = 0; i < jar.length(); i++){
                        listBank.add(jar.getJSONObject(i).getString("name"));
                        listBankID.add(jar.getJSONObject(i).getString("id"));
                        if(jar.getJSONObject(i).getString("name").equalsIgnoreCase(job.getString("bank_name"))){
                            bank = jar.getJSONObject(i).getString("id");
                        }
                        Log.e("Data bank", bank);
                    }
                    ArrayAdapter adapter = new ArrayAdapter(BankInfoShowActivity.this, R.layout.support_simple_spinner_dropdown_item, listBank);
                    auto_bank.setAdapter(adapter);
                    auto_bank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                            bank = listBankID.get(x).toString();
                            Log.e("bank", bank);
                            txtBank.setError(null);
                        }
                    });
                }else{
                }
                dialog.cancel();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private Observer<JSONObject> showAvgTrans = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    JSONObject jobRes = result.getJSONObject("result");
                    JSONArray jar = jobRes.getJSONArray("average");
                    for(int i = 0; i < jar.length(); i++){
                        listAvgTrans.add(jar.getJSONObject(i).getString("name"));
                        listAvgTransID.add(jar.getJSONObject(i).getString("id"));
                        if(jar.getJSONObject(i).getString("name").equalsIgnoreCase(job.getString("average_transaction"))){
                            avgTrans = jar.getJSONObject(i).getString("id");
                        }
                        Log.e("Data avg", avgTrans);
                    }
                    ArrayAdapter adapter = new ArrayAdapter(BankInfoShowActivity.this, R.layout.support_simple_spinner_dropdown_item, listAvgTrans);
                    auto_avg_trans.setAdapter(adapter);
                    auto_avg_trans.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                            avgTrans = listAvgTransID.get(x).toString();
                            Log.e("avgTrans", avgTrans);
                            txtAvgTrans.setError(null);
                        }
                    });
                }else{
                }
                dialog.cancel();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    public void clearMasterList(){
        listBank.clear();listBankID.clear();
        listAvgTrans.clear();listAvgTransID.clear();
    }

    private void setNoError(){
        txtBank.setError(null);
        txtAccountName.setError(null);
        txtAccountNumber.setError(null);
        txtAvgTrans.setError(null);
    }

    private void cekError(){
        if(bank.isEmpty()){txtBank.setError(getString(R.string.cannotnull));}else{txtBank.setError(null);}
        if(accountName.isEmpty()){txtAccountName.setError(getString(R.string.cannotnull));}else{txtAccountName.setError(null);}
        if(accountNumber.isEmpty()){txtAccountNumber.setError(getString(R.string.cannotnull));}else{txtAccountNumber.setError(null);}
        if(avgTrans.isEmpty()){txtAvgTrans.setError(getString(R.string.cannotnull));}else{txtAvgTrans.setError(null);}
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