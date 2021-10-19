package byc.avt.avanteelender.view.features.account.institution;

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
import android.text.Spanned;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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
import byc.avt.avanteelender.view.sheet.TermSheetFragment;
import byc.avt.avanteelender.viewmodel.AuthenticationViewModel;
import byc.avt.avanteelender.viewmodel.MasterDataViewModel;

public class InsCompanyShowActivity extends AppCompatActivity {

    private MasterDataViewModel viewModel;
    private AuthenticationViewModel viewModel2;
    Button btn_save, btn_edit;
    boolean editIsOn = false;
    private Toolbar toolbar;
    JSONObject job;
    private PrefManager prefManager;
    private Dialog dialog;
    GlobalVariables gv;

    private RadioButton radYa, radNo;
    private RadioGroup radGroupIsOnlineBased;
    private RadioButton radButtonIsOnlineBased;
    AutoCompleteTextView auto_company_type, auto_business_field, auto_income,
            auto_funds_source;
    TextInputLayout txtCompanyName, txtCompanyType, txtBusinessField, txtYearEst, txtIncome,
            txtFundsSource, txtCompanyPhone, txtCompanyFax, txtCompanyDesc;
    String companyName="", companyType="", businessField="", yearEst="", isOnlineBased="ya", income="",
            fundsSource="", companyPhone="", companyFax="", companyDesc="";
    List<Object> listComType = new ArrayList<>(); List<Object> listComTypeID = new ArrayList<>();
    List<Object> listBusinessField = new ArrayList<>(); List<Object> listBusinessFieldID = new ArrayList<>();
    List<Object> listIncome = new ArrayList<>(); List<Object> listIncomeID = new ArrayList<>();
    List<Object> listFunds = new ArrayList<>(); List<Object> listFundsID = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ins_company_show);

        gv.insEditData.clear();
        gv.insEditDataFile.clear();
        viewModel = new ViewModelProvider(this).get(MasterDataViewModel.class);
        viewModel2 = new ViewModelProvider(this).get(AuthenticationViewModel.class);
        toolbar = findViewById(R.id.toolbar_ins_company_show);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_back_24px);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        prefManager = PrefManager.getInstance(InsCompanyShowActivity.this);
        dialog = GlobalVariables.loadingDialog(InsCompanyShowActivity.this);

        auto_company_type = findViewById(R.id.auto_company_type_ins_company_show);
        auto_business_field = findViewById(R.id.auto_business_field_ins_company_show);
        auto_income = findViewById(R.id.auto_company_income_ins_company_show);
        auto_funds_source = findViewById(R.id.auto_funds_source_ins_company_show);

        txtCompanyName = findViewById(R.id.edit_company_name_ins_company_show);
        txtCompanyType = findViewById(R.id.edit_company_type_ins_company_show);
        txtBusinessField = findViewById(R.id.edit_business_field_ins_company_show);
        txtYearEst = findViewById(R.id.edit_year_of_establishment_ins_company_show);
        txtIncome = findViewById(R.id.edit_company_income_ins_company_show);
        txtFundsSource = findViewById(R.id.edit_funds_source_ins_company_show);
        txtCompanyPhone = findViewById(R.id.edit_company_phone_ins_company_show);
        txtCompanyFax = findViewById(R.id.edit_company_fax_ins_company_show);
        txtCompanyDesc = findViewById(R.id.edit_company_desc_ins_company_show);
        radYa = findViewById(R.id.rad_yes_ins_company_show);
        radNo = findViewById(R.id.rad_no_ins_company_show);

        radGroupIsOnlineBased = findViewById(R.id.rad_group_is_online_based_ins_company_show);
        radGroupIsOnlineBased.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radButtonIsOnlineBased = findViewById(selectedId);
                if(radButtonIsOnlineBased.getText().equals("Ya")){
                    isOnlineBased = "ya";
                }else{
                    isOnlineBased = "Tidak";
                }
            }
        });

        Intent i = getIntent();
        try {
            job = new JSONObject(i.getStringExtra("jobCompanyInfo"));
            txtCompanyName.getEditText().setText(job.getString("nama_perusahaan"));
            companyName = job.getString("nama_perusahaan");
            txtCompanyType.getEditText().setText(job.getString("tipe_perusahaan"));
            txtBusinessField.getEditText().setText(job.getString("bidang_usaha"));
            txtYearEst.getEditText().setText(job.getString("tahun_pendirian"));
            yearEst = job.getString("tahun_pendirian");
            txtIncome.getEditText().setText(job.getString("pendapatan").replace("&gt;", ">"));
            txtFundsSource.getEditText().setText(job.getString("sumber_dana"));
            txtCompanyPhone.getEditText().setText(job.getString("no_tlp_kantor"));
            companyPhone = job.getString("no_tlp_kantor");
            txtCompanyFax.getEditText().setText(job.getString("no_fax_kantor"));
            companyFax = job.getString("no_fax_kantor");
            txtCompanyDesc.getEditText().setText(job.getString("deskripsi_usaha"));
            companyDesc = job.getString("deskripsi_usaha");
            if(job.getString("online_based").equalsIgnoreCase("Tidak")){
                isOnlineBased = "Tidak";
                radNo.setChecked(true);
            }else{
                isOnlineBased = "ya";
                radYa.setChecked(true);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        btn_save = findViewById(R.id.btn_save_ins_company_show);
        btn_save.setEnabled(true);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmNext(v);
            }
        });

        btn_edit = findViewById(R.id.btn_ubah_ins_company_show);
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
        companyName = Objects.requireNonNull(txtCompanyName.getEditText().getText().toString().trim());
        yearEst = Objects.requireNonNull(txtYearEst.getEditText().getText().toString().trim());
        companyPhone = Objects.requireNonNull(txtCompanyPhone.getEditText().getText().toString().trim());
        companyFax = Objects.requireNonNull(txtCompanyFax.getEditText().getText().toString().trim());
        companyDesc = Objects.requireNonNull(txtCompanyDesc.getEditText().getText().toString().trim());

        if(!companyName.isEmpty() && !companyType.isEmpty() && !businessField.isEmpty() && !yearEst.isEmpty()
                && !income.isEmpty() && !fundsSource.isEmpty() && !companyPhone.isEmpty() && !companyDesc.isEmpty()){
            gv.insEditData.put("event_name","informasi_perusahaan");
            gv.insEditData.put("tipe_investor",prefManager.getClientType());
            gv.insEditData.put("nama_perusahaan",companyName);
            gv.insEditData.put("tipe_perusahaan",companyType);
            gv.insEditData.put("bidang_usaha",businessField);
            gv.insEditData.put("tahun_pendirian",yearEst);
            gv.insEditData.put("online_based",isOnlineBased);
            gv.insEditData.put("pendapatan",income);
            gv.insEditData.put("sumber_dana",fundsSource);
            gv.insEditData.put("no_tlp_kantor",companyPhone);
            gv.insEditData.put("no_fax_kantor",companyFax);
            gv.insEditData.put("deskripsi_usaha",companyDesc);
            setNoError();
            updateDocument();
        }else{
            cekError();
        }
    }

    private void updateDocument(){
        new AlertDialog.Builder(InsCompanyShowActivity.this)
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
                        viewModel2.getResultUpdateInstitutionDoc().observe(InsCompanyShowActivity.this, showResult);
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
                    new Fungsi(InsCompanyShowActivity.this).showMessage(getString(R.string.success_update_data));
                    dialog.cancel();
                    new Routes(InsCompanyShowActivity.this).moveOut();
                }else{
                    new Fungsi(InsCompanyShowActivity.this).showMessage(getString(R.string.failed_update_data));
                    dialog.cancel();
                    //new Routes(PersonalDataShowActivity.this).moveOut();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                String msg = getString(R.string.system_in_trouble);
                Log.e("Respon per cr doc", msg);
                new Fungsi(InsCompanyShowActivity.this).showMessage(msg);
                dialog.cancel();
            }
        }
    };

    public void editIsOn(boolean s){
        txtCompanyName.setEnabled(s);
        txtCompanyType.setEnabled(s);
        txtBusinessField.setEnabled(s);
        txtYearEst.setEnabled(s);
        txtIncome.setEnabled(s);
        txtFundsSource.setEnabled(s);
        txtCompanyPhone.setEnabled(s);
        txtCompanyFax.setEnabled(s);
        txtCompanyDesc.setEnabled(s);
        radYa.setEnabled(s);
        radNo.setEnabled(s);
        btn_save.setEnabled(s);
    }

    public void clearMasterList(){
        listComType.clear();listComTypeID.clear();
        listIncome.clear();listIncomeID.clear();
        listBusinessField.clear();listBusinessFieldID.clear();
        listFunds.clear();listFundsID.clear();
    }

    public void loadData() {
        clearMasterList();
        dialog.show();
        viewModel.getCompanyType(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultCompanyType().observe(InsCompanyShowActivity.this, showCompanyType);
        viewModel.getBusinessType(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultBusinessType().observe(InsCompanyShowActivity.this, showBusinessField);
        viewModel.getIncome(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultIncome().observe(InsCompanyShowActivity.this, showIncome);
        viewModel.getFundsSource(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultFundsSource().observe(InsCompanyShowActivity.this, showFundsSource);
    }

    private void setNoError(){
        txtCompanyName.setError(null);
        txtCompanyType.setError(null);
        txtBusinessField.setError(null);
        txtYearEst.setError(null);
        txtIncome.setError(null);
        txtFundsSource.setError(null);
        txtCompanyPhone.setError(null);
        txtCompanyFax.setError(null);
        txtCompanyDesc.setError(null);
    }

    private void cekError(){
        if(companyName.isEmpty()){txtCompanyName.setError(getString(R.string.cannotnull));}else{txtCompanyName.setError(null);}
        if(companyType.isEmpty()){txtCompanyType.setError(getString(R.string.cannotnull));}else{txtCompanyType.setError(null);}
        if(businessField.isEmpty()){txtBusinessField.setError(getString(R.string.cannotnull));}else{txtBusinessField.setError(null);}
        if(yearEst.isEmpty()){txtYearEst.setError(getString(R.string.cannotnull));}else{txtYearEst.setError(null);}
        if(income.isEmpty()){txtIncome.setError(getString(R.string.cannotnull));}else{txtIncome.setError(null);}
        if(fundsSource.isEmpty()){txtFundsSource.setError(getString(R.string.cannotnull));}else{txtFundsSource.setError(null);}
        if(companyPhone.isEmpty()){txtCompanyPhone.setError(getString(R.string.cannotnull));}else{txtCompanyPhone.setError(null);}
        if(companyDesc.isEmpty()){txtCompanyDesc.setError(getString(R.string.cannotnull));}else{txtCompanyDesc.setError(null);}
    }

    private Observer<JSONObject> showCompanyType = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    JSONObject jobRes = result.getJSONObject("result");
                    JSONArray jar = jobRes.getJSONArray("company");
                    for(int i = 0; i < jar.length(); i++){
                        listComType.add(jar.getJSONObject(i).getString("name"));
                        listComTypeID.add(jar.getJSONObject(i).getString("id"));
                        if(jar.getJSONObject(i).getString("name").equalsIgnoreCase(job.getString("tipe_perusahaan"))){
                            companyType = jar.getJSONObject(i).getString("id");
                        }
                    }
                    ArrayAdapter adapter = new ArrayAdapter(InsCompanyShowActivity.this, R.layout.support_simple_spinner_dropdown_item, listComType);
                    auto_company_type.setAdapter(adapter);
                    auto_company_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                            companyType = listComTypeID.get(x).toString();
                            Log.e("companyType", companyType);
                            txtCompanyType.setError(null);
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

    private Observer<JSONObject> showBusinessField = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    JSONObject jobRes = result.getJSONObject("result");
                    JSONArray jar = jobRes.getJSONArray("businesstype");
                    for(int i = 0; i < jar.length(); i++){
                        listBusinessField.add(jar.getJSONObject(i).getString("name"));
                        listBusinessFieldID.add(jar.getJSONObject(i).getString("id"));
                        if(jar.getJSONObject(i).getString("name").equalsIgnoreCase(job.getString("bidang_usaha"))){
                            businessField = jar.getJSONObject(i).getString("id");
                        }
                    }
                    ArrayAdapter adapter = new ArrayAdapter(InsCompanyShowActivity.this, R.layout.support_simple_spinner_dropdown_item, listBusinessField);
                    auto_business_field.setAdapter(adapter);
                    auto_business_field.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                            businessField= listBusinessFieldID.get(x).toString();
                            Log.e("businessField", businessField);
                            txtBusinessField.setError(null);
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

    private Observer<JSONObject> showIncome = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    JSONObject jobRes = result.getJSONObject("result");
                    JSONArray jar = jobRes.getJSONArray("income");
                    for(int i = 0; i < jar.length(); i++){
                        listIncome.add(jar.getJSONObject(i).getString("name"));
                        listIncomeID.add(jar.getJSONObject(i).getString("id"));
                        if(jar.getJSONObject(i).getString("name").contains(job.getString("pendapatan").replace("&gt;", ">"))){
                            income = jar.getJSONObject(i).getString("id");
                        }
                    }
                    ArrayAdapter adapter = new ArrayAdapter(InsCompanyShowActivity.this, R.layout.support_simple_spinner_dropdown_item, listIncome);
                    auto_income.setAdapter(adapter);
                    auto_income.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                            income = listIncomeID.get(x).toString();
                            Log.e("income", income);
                            txtIncome.setError(null);
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

    private Observer<JSONObject> showFundsSource = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    JSONObject jobRes = result.getJSONObject("result");
                    JSONArray jar = jobRes.getJSONArray("funds");
                    for(int i = 0; i < jar.length(); i++){
                        listFunds.add(jar.getJSONObject(i).getString("name"));
                        listFundsID.add(jar.getJSONObject(i).getString("id"));
                        if(jar.getJSONObject(i).getString("name").equalsIgnoreCase(job.getString("sumber_dana"))){
                            fundsSource = jar.getJSONObject(i).getString("id");
                        }
                    }
                    ArrayAdapter adapter = new ArrayAdapter(InsCompanyShowActivity.this, R.layout.support_simple_spinner_dropdown_item, listFunds);
                    auto_funds_source.setAdapter(adapter);
                    auto_funds_source.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                            fundsSource = listFundsID.get(x).toString();
                            Log.e("funds", fundsSource);
                            txtFundsSource.setError(null);
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


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            new Routes(InsCompanyShowActivity.this).moveOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new Routes(InsCompanyShowActivity.this).moveOut();
    }
}