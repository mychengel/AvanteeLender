package byc.avt.avanteelender.view.features.account.institution;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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
import byc.avt.avanteelender.view.features.account.individual.BankInfoShowActivity;
import byc.avt.avanteelender.viewmodel.AuthenticationViewModel;
import byc.avt.avanteelender.viewmodel.MasterDataViewModel;

public class InsBankInfoShowActivity extends AppCompatActivity {

    private Toolbar toolbar;
    JSONObject job;
    private PrefManager prefManager;
    private Dialog dialog;
    GlobalVariables gv;
    AutoCompleteTextView auto_bank, auto_avg_trans;
    TextInputLayout txtBank, txtAccountName, txtAccountNumber;
    private MasterDataViewModel viewModel;
    private AuthenticationViewModel viewModel2;
    Button btn_save, btn_edit;
    boolean editIsOn = false;

    CheckBox cb_owner_name_same_as_name;

    String bank="", accountName="", accountNumber="";
    List<Object> listBank = new ArrayList<>(); List<Object> listBankID = new ArrayList<>();
    String is_same_name = "0", name_tmp = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ins_bank_info_show);

        gv.insEditData.clear();
        gv.insEditDataFile.clear();
        viewModel = new ViewModelProvider(this).get(MasterDataViewModel.class);
        viewModel2 = new ViewModelProvider(this).get(AuthenticationViewModel.class);
        toolbar = findViewById(R.id.toolbar_ins_bank_info_show);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_back_24px);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        prefManager = PrefManager.getInstance(InsBankInfoShowActivity.this);
        dialog = GlobalVariables.loadingDialog(InsBankInfoShowActivity.this);

        cb_owner_name_same_as_name = findViewById(R.id.cb_owner_name_same_as_name_fr_ins_bank_info_show);
        auto_bank = findViewById(R.id.auto_bank_name_fr_ins_bank_info_show);
        txtBank = findViewById(R.id.edit_bank_name_fr_ins_bank_info_show);
        txtAccountName = findViewById(R.id.edit_bank_owner_name_fr_ins_bank_info_show);
        txtAccountNumber = findViewById(R.id.edit_bank_account_number_fr_ins_bank_info_show);

        Intent i = getIntent();
        try {
            job = new JSONObject(i.getStringExtra("jobBankInfo"));
            txtBank.getEditText().setText(job.getString("bank_name"));
            txtAccountName.getEditText().setText(job.getString("bank_account"));
            name_tmp = job.getString("bank_account");
            txtAccountNumber.getEditText().setText(job.getString("bank_account_no"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

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

        btn_save = findViewById(R.id.btn_save_fr_ins_bank_info_show);
        btn_save.setEnabled(true);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmNext(v);
            }
        });

        btn_edit = findViewById(R.id.btn_ubah_fr_ins_bank_info_show);
        btn_edit.setEnabled(true);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editIsOn = !editIsOn;
                editIsOn(editIsOn);
            }
        });

        editIsOn(false);
        loadData();

    }

    private void confirmNext(View v){
        accountName = Objects.requireNonNull(txtAccountName.getEditText().getText().toString().trim());
        accountNumber = Objects.requireNonNull(txtAccountNumber.getEditText().getText().toString().trim());
        if(!bank.isEmpty() && !accountName.isEmpty() && !accountNumber.isEmpty()){
            gv.insEditData.put("event_name","data_rekening");
            gv.insEditData.put("tipe_investor",prefManager.getClientType());
            gv.insEditData.put("bank", bank);
            gv.insEditData.put("bank_account", accountName);
            gv.insEditData.put("bank_account_no", accountNumber);
            gv.insEditData.put("sesuai_nama", is_same_name);
            setNoError();
            updateDocument();
        }else{
            cekError();
        }
    }

    private void updateDocument(){
        new AlertDialog.Builder(InsBankInfoShowActivity.this)
                .setTitle("Konfirmasi")
                .setIcon(R.drawable.logo)
                .setMessage(getString(R.string.update_doc_confirmation))
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        dialog.show();
                        viewModel2.updateInstitutionDoc(prefManager.getUid(), prefManager.getToken());
                        viewModel2.getResultUpdateInstitutionDoc().observe(InsBankInfoShowActivity.this, showResult);
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
                    new Fungsi(InsBankInfoShowActivity.this).showMessage(getString(R.string.success_update_data));
                    dialog.cancel();
                    new Routes(InsBankInfoShowActivity.this).moveOut();
                }else{
                    new Fungsi(InsBankInfoShowActivity.this).showMessage(getString(R.string.failed_update_data));
                    dialog.cancel();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                String msg = getString(R.string.system_in_trouble);
                Log.e("Respon per cr doc", msg);
                new Fungsi(InsBankInfoShowActivity.this).showMessage(msg);
                dialog.cancel();
            }
        }
    };

    public void editIsOn(boolean s){
        txtBank.setEnabled(s);
        txtAccountName.setEnabled(s);
        txtAccountNumber.setEnabled(s);
        cb_owner_name_same_as_name.setEnabled(s);
        btn_save.setEnabled(s);
    }


    public void loadData(){
        clearMasterList();
        dialog.show();
        viewModel.getBank(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultBank().observe(InsBankInfoShowActivity.this, showBank);
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
                    ArrayAdapter adapter = new ArrayAdapter(InsBankInfoShowActivity.this, R.layout.support_simple_spinner_dropdown_item, listBank);
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

    public void clearMasterList(){
        listBank.clear();listBankID.clear();
    }

    private void setNoError(){
        txtBank.setError(null);
        txtAccountName.setError(null);
        txtAccountNumber.setError(null);
    }

    private void cekError(){
        if(bank.isEmpty()){txtBank.setError(getString(R.string.cannotnull));}else{txtBank.setError(null);}
        if(accountName.isEmpty()){txtAccountName.setError(getString(R.string.cannotnull));}else{txtAccountName.setError(null);}
        if(accountNumber.isEmpty()){txtAccountNumber.setError(getString(R.string.cannotnull));}else{txtAccountNumber.setError(null);}
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            new Routes(InsBankInfoShowActivity.this).moveOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new Routes(InsBankInfoShowActivity.this).moveOut();
    }
}