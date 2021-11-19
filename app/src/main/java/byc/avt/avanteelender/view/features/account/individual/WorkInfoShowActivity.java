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
import android.text.Editable;
import android.text.TextWatcher;
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
import byc.avt.avanteelender.viewmodel.AuthenticationViewModel;
import byc.avt.avanteelender.viewmodel.MasterDataViewModel;

public class WorkInfoShowActivity extends AppCompatActivity {

    Fungsi f = new Fungsi(WorkInfoShowActivity.this);
    private PrefManager prefManager;
    private Dialog dialog;
    private Toolbar toolbar;
    GlobalVariables gv;
    JSONObject job;

    private RadioGroup radGroupIsOnlineBased;
    private RadioButton radButtonIsOnlineBased;
    TextInputLayout txtJob, txtJobField, txtJobPosition, txtExperience, txtIncome, txtCompanyName,
            txtCompanyNumber, txtFundsSource, txtCompanyAddress, txtCompanyProvince, txtCompanyCity,
            txtCompanyDistrict, txtCompanyUrban, txtCompanyRT, txtCompanyRW, txtCompanyPostalCode;

    private MasterDataViewModel viewModel;
    private AuthenticationViewModel viewModel2;
    Button btn_save, btn_edit;
    boolean editIsOn = false;
    AutoCompleteTextView auto_job, auto_jobField, auto_jobPosition, auto_experience, auto_income,
            auto_fundsSource, auto_companyProvince, auto_companyCity, auto_companyDistrict, auto_companyUrban;
    String jobs="", jobField="", isOnlineBased="ya", jobPosition="", experience="", income="",
            companyName="", companyNumber="", fundsSource="", companyAddress="", companyProvince="",
            companyCity="", companyDistrict="", companyUrban="", companyRT="", companyRW="",
            companyPostalCode="", ex="";

    List<Object> listJob = new ArrayList<>(); List<Object> listJobID = new ArrayList<>();
    List<Object> listJobField = new ArrayList<>(); List<Object> listJobFieldID = new ArrayList<>();
    List<Object> listJobPosition = new ArrayList<>(); List<Object> listJobPositionID = new ArrayList<>();
    List<Object> listExperience = new ArrayList<>(); List<Object> listExperienceID = new ArrayList<>();
    List<Object> listIncome = new ArrayList<>(); List<Object> listIncomeID = new ArrayList<>();
    List<Object> listFundsSource = new ArrayList<>(); List<Object> listFundsSourceID = new ArrayList<>();
    List<Object> listProvince = new ArrayList<>(); List<Object> listProvinceID = new ArrayList<>();
    List<Object> listCity = new ArrayList<>(); List<Object> listCityID = new ArrayList<>();
    List<Object> listDistrict = new ArrayList<>(); List<Object> listDistrictID = new ArrayList<>();
    List<Object> listUrban = new ArrayList<>(); List<Object> listUrbanID = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_info_show);
        gv.perEditData.clear();
        gv.perEditDataFile.clear();
        viewModel = new ViewModelProvider(this).get(MasterDataViewModel.class);
        viewModel2 = new ViewModelProvider(this).get(AuthenticationViewModel.class);
        prefManager = PrefManager.getInstance(WorkInfoShowActivity.this);
        toolbar = findViewById(R.id.toolbar_work_info_show);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_back_24px);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dialog = GlobalVariables.loadingDialog(WorkInfoShowActivity.this);

        auto_job = findViewById(R.id.auto_job_fr_work_info_show);
        auto_jobField = findViewById(R.id.auto_job_field_fr_work_info_show);
        auto_jobPosition = findViewById(R.id.auto_jabatan_fr_work_info_show);
        auto_experience = findViewById(R.id.auto_lama_bekerja_fr_work_info_show);
        auto_income = findViewById(R.id.auto_pendapatan_per_bulan_fr_work_info_show);
        auto_fundsSource = findViewById(R.id.auto_funds_source_fr_work_info_show);
        auto_companyProvince = findViewById(R.id.auto_company_province_fr_work_info_show);
        auto_companyCity = findViewById(R.id.auto_company_city_fr_work_info_show);
        auto_companyDistrict = findViewById(R.id.auto_company_kecamatan_fr_work_info_show);
        auto_companyUrban = findViewById(R.id.auto_company_kelurahan_fr_work_info_show);

        txtJob = findViewById(R.id.edit_job_fr_work_info_show);
        txtJobField = findViewById(R.id.edit_job_field_fr_work_info_show);
        txtJobPosition = findViewById(R.id.edit_jabatan_fr_work_info_show);
        txtExperience = findViewById(R.id.edit_lama_bekerja_fr_work_info_show);
        txtIncome = findViewById(R.id.edit_pendapatan_per_bulan_fr_work_info_show);
        txtCompanyName = findViewById(R.id.edit_company_name_fr_work_info_show);
        txtCompanyNumber = findViewById(R.id.edit_company_number_fr_work_info_show);
        txtFundsSource = findViewById(R.id.edit_funds_source_fr_work_info_show);
        txtCompanyAddress = findViewById(R.id.edit_company_address_fr_work_info_show);
        txtCompanyProvince = findViewById(R.id.edit_company_province_fr_work_info_show);
        txtCompanyCity = findViewById(R.id.edit_company_city_fr_work_info_show);
        txtCompanyDistrict = findViewById(R.id.edit_company_kecamatan_fr_work_info_show);
        txtCompanyUrban = findViewById(R.id.edit_company_kelurahan_fr_work_info_show);
        txtCompanyRT = findViewById(R.id.edit_company_rt_fr_work_info_show);
        txtCompanyRW = findViewById(R.id.edit_company_rw_fr_work_info_show);
        txtCompanyPostalCode = findViewById(R.id.edit_company_kodepos_fr_work_info_show);

        Intent i = getIntent();
        try {
            job = new JSONObject(i.getStringExtra("jobWorkInfo"));
            ex = job.getString("job_experience").replaceAll("&lt;","<");
            ex = ex.replaceAll("&gt;",">");
            if(job.getString("online_based").equalsIgnoreCase("Ya")){
                radButtonIsOnlineBased = findViewById(R.id.rad_yes_fr_work_info_show);
                isOnlineBased = "ya";
            }else{
                radButtonIsOnlineBased = findViewById(R.id.rad_no_fr_work_info_show);
                isOnlineBased = "Tidak";
            }
            radButtonIsOnlineBased.setChecked(true);
            txtJob.getEditText().setText(job.getString("client_job"));
            txtJobField.getEditText().setText(job.getString("client_job_type"));
            txtJobPosition.getEditText().setText(job.getString("job_position"));
            txtExperience.getEditText().setText(ex);
            txtIncome.getEditText().setText(job.getString("income"));
            txtCompanyName.getEditText().setText(job.getString("company_name"));
            if(job.getString("office_phone").equalsIgnoreCase("-")){
                txtCompanyNumber.getEditText().setText("");
            }else{
                txtCompanyNumber.getEditText().setText(job.getString("office_phone"));
            }

            txtFundsSource.getEditText().setText(job.getString("source_funds"));
            txtCompanyAddress.getEditText().setText(job.getString("company_address"));
            txtCompanyProvince.getEditText().setText(job.getString("company_province"));
            txtCompanyCity.getEditText().setText(job.getString("company_city"));
            txtCompanyDistrict.getEditText().setText(job.getString("company_district"));
            txtCompanyUrban.getEditText().setText(job.getString("company_village"));
            txtCompanyRT.getEditText().setText(job.getString("company_rt"));
            txtCompanyRW.getEditText().setText(job.getString("company_rw"));
            txtCompanyPostalCode.getEditText().setText(job.getString("company_postal_code"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        txtCompanyPostalCode.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                companyPostalCode = charSequence.toString();
                cekPostal();
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        radGroupIsOnlineBased = findViewById(R.id.rad_group_is_online_based_fr_work_info_show);
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
        btn_save = findViewById(R.id.btn_save_fr_work_info_show);
        btn_save.setEnabled(true);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmNext(v);
            }
        });

        btn_edit = findViewById(R.id.btn_ubah_fr_work_info_show);
        btn_edit.setEnabled(true);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editIsOn = !editIsOn;
                editIsOn(editIsOn);
                v.setEnabled(false);
            }
        });

        editIsOn(false);
        loadData();

    }

    public void cekPostal(){
        if(companyPostalCode.length() > 5){txtCompanyPostalCode.setError(getString(R.string.postal_code_max_char));}else{txtCompanyPostalCode.setError(null);}
    }

    public void editIsOn(boolean s){
        txtJob.setEnabled(s);
        txtJobField.setEnabled(s);
        txtJobPosition.setEnabled(s);
        txtExperience.setEnabled(s);
        txtIncome.setEnabled(s);
        txtCompanyName.setEnabled(s);
        txtCompanyNumber.setEnabled(s);
        txtFundsSource.setEnabled(s);
        txtCompanyAddress.setEnabled(s);
        txtCompanyProvince.setEnabled(s);
        txtCompanyCity.setEnabled(s);
        txtCompanyDistrict.setEnabled(s);
        txtCompanyUrban.setEnabled(s);
        txtCompanyRT.setEnabled(s);
        txtCompanyRW.setEnabled(s);
        txtCompanyPostalCode.setEnabled(s);
        for(int i = 0; i < radGroupIsOnlineBased.getChildCount(); i++){
            ((RadioButton)radGroupIsOnlineBased.getChildAt(i)).setEnabled(s);
        }
        btn_save.setEnabled(s);
    }

    private void confirmNext(View v){
        companyName = Objects.requireNonNull(txtCompanyName.getEditText().getText().toString().trim());
        companyNumber = Objects.requireNonNull(txtCompanyNumber.getEditText().getText().toString().trim());
        companyAddress = Objects.requireNonNull(txtCompanyAddress.getEditText().getText().toString().trim());
        companyRT = Objects.requireNonNull(txtCompanyRT.getEditText().getText().toString().trim());
        companyRW = Objects.requireNonNull(txtCompanyRW.getEditText().getText().toString().trim());
        companyPostalCode = Objects.requireNonNull(txtCompanyPostalCode.getEditText().getText().toString().trim());

        if(!jobs.isEmpty() && !jobField.isEmpty() && !jobPosition.isEmpty() && !experience.isEmpty()
                && !companyName.isEmpty() && !income.isEmpty() && !fundsSource.isEmpty()
                && !companyAddress.isEmpty() && !companyProvince.isEmpty() && !companyCity.isEmpty()
                && !companyDistrict.isEmpty() && !companyUrban.isEmpty() && !companyRT.isEmpty()
                && !companyRW.isEmpty() && !companyPostalCode.isEmpty() && companyPostalCode.length() <= 5){
            gv.perEditData.put("event_name","data_pekerjaan");
            gv.perEditData.put("tipe_investor",prefManager.getClientType());
            gv.perEditData.put("pekerjaan",jobs);
            gv.perEditData.put("bidang_pekerjaan",jobField);
            gv.perEditData.put("basis_internet",isOnlineBased);
            gv.perEditData.put("jabatan",jobPosition);
            gv.perEditData.put("lama_bekerja",experience);
            gv.perEditData.put("pendapatan_per_bulan",income);
            gv.perEditData.put("sumber_dana",fundsSource);
            gv.perEditData.put("nama_perusahaan",companyName);
            gv.perEditData.put("alamat_perusahaan",companyAddress);
            gv.perEditData.put("provinsi_perusahaan",companyProvince);
            gv.perEditData.put("kota_perusahaan",companyCity);
            gv.perEditData.put("kecamatan_perusahaan",companyDistrict);
            gv.perEditData.put("kelurahan_perusahaan",companyUrban);
            gv.perEditData.put("rt_perusahaan",companyRT);
            gv.perEditData.put("rw_perusahaan",companyRW);
            gv.perEditData.put("kode_pos_perusahaan",companyPostalCode);
            gv.perEditData.put("no_telepon_kantor",companyNumber);
            setNoError();
            updateDocument();
        }else{
            cekError();
        }
    }

    private void updateDocument(){
        new AlertDialog.Builder(WorkInfoShowActivity.this)
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
                        viewModel2.getResultUpdatePersonalDoc().observe(WorkInfoShowActivity.this, showResult);
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
                    new Fungsi(WorkInfoShowActivity.this).showMessage(getString(R.string.success_update_data));
                    dialog.cancel();
                    new Routes(WorkInfoShowActivity.this).moveOut();
                }else{
                    dialog.cancel();
                    JSONObject jobRes = result.getJSONObject("result");
                    String msg = f.docErr400(jobRes.toString());
                    new AlertDialog.Builder(WorkInfoShowActivity.this)
                            .setTitle("Peringatan")
                            .setIcon(R.drawable.logo)
                            .setMessage("• " + msg)
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            }).create().show();
                    cekError();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                String msg = getString(R.string.system_in_trouble);
                Log.e("Respon per cr doc", msg);
                new Fungsi(WorkInfoShowActivity.this).showMessage(msg);
                dialog.cancel();
                cekError();
            }
        }
    };

    public void loadData(){
        clearMasterList();
        dialog.show();
        viewModel.getJob(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultJob().observe(WorkInfoShowActivity.this, showJob);
        viewModel.getJobField(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultJobField().observe(WorkInfoShowActivity.this, showJobField);
        viewModel.getJobPosition(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultJobPosition().observe(WorkInfoShowActivity.this, showJobPosition);
        viewModel.getWorkExperience(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultWorkExperience().observe(WorkInfoShowActivity.this, showExperience);
        viewModel.getIncome(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultIncome().observe(WorkInfoShowActivity.this, showIncome);
        viewModel.getFundsSource(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultFundsSource().observe(WorkInfoShowActivity.this, showFundsSource);
        viewModel.getProvince(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultProvince().observe(WorkInfoShowActivity.this, showProvince);
    }

    private Observer<JSONObject> showJob = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    JSONObject jobRes = result.getJSONObject("result");
                    JSONArray jar = jobRes.getJSONArray("job");
                    for(int i = 0; i < jar.length(); i++){
                        listJob.add(jar.getJSONObject(i).getString("name"));
                        listJobID.add(jar.getJSONObject(i).getString("id"));
                        if(jar.getJSONObject(i).getString("name").equalsIgnoreCase(f.exrn(job.getString("client_job")))){
                            jobs = jar.getJSONObject(i).getString("id");
                        }
                        Log.e("Data jobtype", jobs);
                    }
                    ArrayAdapter adapter = new ArrayAdapter(WorkInfoShowActivity.this, R.layout.support_simple_spinner_dropdown_item, listJob);
                    auto_job.setAdapter(adapter);
                    auto_job.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                            jobs = listJobID.get(x).toString();
                            Log.e("job", jobs);
                            txtJob.setError(null);
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

    private Observer<JSONObject> showJobField = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    JSONObject jobRes = result.getJSONObject("result");
                    JSONArray jar = jobRes.getJSONArray("job_type");
                    for(int i = 0; i < jar.length(); i++){
                        listJobField.add(jar.getJSONObject(i).getString("name"));
                        listJobFieldID.add(jar.getJSONObject(i).getString("id"));
                        if(jar.getJSONObject(i).getString("name").equalsIgnoreCase(f.exrn(job.getString("client_job_type")))){
                            jobField = jar.getJSONObject(i).getString("id");
                        }
                        Log.e("Data jobfield", jobField);
                    }
                    ArrayAdapter adapter = new ArrayAdapter(WorkInfoShowActivity.this, R.layout.support_simple_spinner_dropdown_item, listJobField);
                    auto_jobField.setAdapter(adapter);
                    auto_jobField.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                            jobField = listJobFieldID.get(x).toString();
                            Log.e("jobField", jobField);
                            txtJobField.setError(null);
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

    private Observer<JSONObject> showJobPosition = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    JSONObject jobRes = result.getJSONObject("result");
                    JSONArray jar = jobRes.getJSONArray("position");
                    Log.e("List Job Position", jar.toString());
                    for(int i = 0; i < jar.length(); i++){
                        listJobPosition.add(jar.getJSONObject(i).getString("name"));
                        listJobPositionID.add(jar.getJSONObject(i).getString("id"));
                        if(jar.getJSONObject(i).getString("name").equalsIgnoreCase(f.exrn(job.getString("job_position")))){
                            jobPosition = jar.getJSONObject(i).getString("id");
                        }
                        Log.e("Data jobpos", jobPosition);
                    }
                    ArrayAdapter adapter = new ArrayAdapter(WorkInfoShowActivity.this, R.layout.support_simple_spinner_dropdown_item, listJobPosition);
                    auto_jobPosition.setAdapter(adapter);
                    auto_jobPosition.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                            jobPosition = listJobPositionID.get(x).toString();
                            Log.e("jobPosition", jobPosition);
                            txtJobPosition.setError(null);
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

    private Observer<JSONObject> showExperience = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    JSONObject jobRes = result.getJSONObject("result");
                    JSONArray jar = jobRes.getJSONArray("experience");
                    Log.e("List Experience", jar.toString());
                    for(int i = 0; i < jar.length(); i++){
                        listExperience.add(jar.getJSONObject(i).getString("name"));
                        listExperienceID.add(jar.getJSONObject(i).getString("id"));

                        if(jar.getJSONObject(i).getString("name").equalsIgnoreCase(ex)){
                            experience = jar.getJSONObject(i).getString("id");
                        }
                        Log.e("Data jobexp", experience);
                    }
                    ArrayAdapter adapter = new ArrayAdapter(WorkInfoShowActivity.this, R.layout.support_simple_spinner_dropdown_item, listExperience);
                    auto_experience.setAdapter(adapter);
                    auto_experience.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                            experience = listExperienceID.get(x).toString();
                            Log.e("experience", experience);
                            txtExperience.setError(null);
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
                        if(jar.getJSONObject(i).getString("name").equalsIgnoreCase(f.exrn(job.getString("income")))){
                            income = jar.getJSONObject(i).getString("id");
                        }
                        Log.e("Data income", income);
                    }
                    ArrayAdapter adapter = new ArrayAdapter(WorkInfoShowActivity.this, R.layout.support_simple_spinner_dropdown_item, listIncome);
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
                        listFundsSource.add(jar.getJSONObject(i).getString("name"));
                        listFundsSourceID.add(jar.getJSONObject(i).getString("id"));
                        if(jar.getJSONObject(i).getString("name").equalsIgnoreCase(f.exrn(job.getString("source_funds")))){
                            fundsSource = jar.getJSONObject(i).getString("id");
                        }
                        Log.e("Data funds", fundsSource);
                    }
                    ArrayAdapter adapter = new ArrayAdapter(WorkInfoShowActivity.this, R.layout.support_simple_spinner_dropdown_item, listFundsSource);
                    auto_fundsSource.setAdapter(adapter);
                    auto_fundsSource.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                            fundsSource = listFundsSourceID.get(x).toString();
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

    private Observer<JSONObject> showProvince = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    JSONObject jobRes = result.getJSONObject("result");
                    JSONArray jar = jobRes.getJSONArray("province");
                    for(int i = 0; i < jar.length(); i++){
                        listProvince.add(jar.getJSONObject(i).getString("name"));
                        listProvinceID.add(jar.getJSONObject(i).getString("id"));
                        if(jar.getJSONObject(i).getString("name").equalsIgnoreCase(f.exrn(job.getString("company_province")))){
                            companyProvince = jar.getJSONObject(i).getString("id");
                            viewModel.getCity(prefManager.getUid(), prefManager.getToken(), companyProvince);
                            viewModel.getResultCity().observe(WorkInfoShowActivity.this, showCity);
                        }
                        Log.e("Data comProv", companyProvince);
                    }
                    ArrayAdapter adapter = new ArrayAdapter(WorkInfoShowActivity.this, R.layout.support_simple_spinner_dropdown_item, listProvince);
                    auto_companyProvince.setAdapter(adapter);
                    auto_companyProvince.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                            companyProvince = listProvinceID.get(x).toString();
                            Log.e("comProvince", companyProvince);
                            txtCompanyProvince.setError(null);
                            viewModel.getCity(prefManager.getUid(), prefManager.getToken(), companyProvince);
                            viewModel.getResultCity().observe(WorkInfoShowActivity.this, showCity);
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

    private Observer<JSONObject> showCity = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            listCity.clear();
            listCityID.clear();
            try {
                if(result.getInt("code") == 200){
                    JSONObject jobRes = result.getJSONObject("result");
                    JSONArray jar = jobRes.getJSONArray("city");
                    for(int i = 0; i < jar.length(); i++){
                        listCity.add(jar.getJSONObject(i).getString("name"));
                        listCityID.add(jar.getJSONObject(i).getString("id"));
                        if(jar.getJSONObject(i).getString("name").equalsIgnoreCase(f.exrn(job.getString("company_city")))){
                            companyCity = jar.getJSONObject(i).getString("id");
                            viewModel.getDistrict(prefManager.getUid(), prefManager.getToken(), companyCity);
                            viewModel.getResultDistrict().observe(WorkInfoShowActivity.this, showDistrict);
                        }
                        Log.e("Data comCity", companyCity);
                    }
                    ArrayAdapter adapter = new ArrayAdapter(WorkInfoShowActivity.this, R.layout.support_simple_spinner_dropdown_item, listCity);
                    auto_companyCity.setAdapter(adapter);
                    auto_companyCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                            companyCity = listCityID.get(x).toString();
                            Log.e("comCity", companyCity);
                            txtCompanyCity.setError(null);
                            viewModel.getDistrict(prefManager.getUid(), prefManager.getToken(), companyCity);
                            viewModel.getResultDistrict().observe(WorkInfoShowActivity.this, showDistrict);
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

    private Observer<JSONObject> showDistrict = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            listDistrict.clear();
            listDistrictID.clear();
            try {
                if(result.getInt("code") == 200){
                    JSONObject jobRes = result.getJSONObject("result");
                    JSONArray jar = jobRes.getJSONArray("district");
                    for(int i = 0; i < jar.length(); i++){
                        listDistrict.add(jar.getJSONObject(i).getString("name"));
                        listDistrictID.add(jar.getJSONObject(i).getString("id"));
                        if(jar.getJSONObject(i).getString("name").equalsIgnoreCase(f.exrn(job.getString("company_district")))){
                            companyDistrict = jar.getJSONObject(i).getString("id");
                            viewModel.getUrban(prefManager.getUid(), prefManager.getToken(), companyDistrict);
                            viewModel.getResultUrban().observe(WorkInfoShowActivity.this, showUrban);
                        }
                        Log.e("Data comDistrict", companyDistrict);
                    }
                    ArrayAdapter adapter = new ArrayAdapter(WorkInfoShowActivity.this, R.layout.support_simple_spinner_dropdown_item, listDistrict);
                    auto_companyDistrict.setAdapter(adapter);
                    auto_companyDistrict.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                            companyDistrict = listDistrictID.get(x).toString();
                            Log.e("comDistrict", companyDistrict);
                            txtCompanyDistrict.setError(null);
                            viewModel.getUrban(prefManager.getUid(), prefManager.getToken(), companyDistrict);
                            viewModel.getResultUrban().observe(WorkInfoShowActivity.this, showUrban);
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

    private Observer<JSONObject> showUrban = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            listUrban.clear();
            listUrbanID.clear();
            try {
                if(result.getInt("code") == 200){
                    JSONObject jobRes = result.getJSONObject("result");
                    JSONArray jar = jobRes.getJSONArray("villages");
                    for(int i = 0; i < jar.length(); i++){
                        listUrban.add(jar.getJSONObject(i).getString("name"));
                        listUrbanID.add(jar.getJSONObject(i).getString("id"));
                        if(jar.getJSONObject(i).getString("name").equalsIgnoreCase(f.exrn(job.getString("company_village")))){
                            companyUrban = jar.getJSONObject(i).getString("id");
                        }
                        Log.e("Data comUrban", companyUrban);
                    }
                    ArrayAdapter adapter = new ArrayAdapter(WorkInfoShowActivity.this, R.layout.support_simple_spinner_dropdown_item, listUrban);
                    auto_companyUrban.setAdapter(adapter);
                    auto_companyUrban.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                            companyUrban = listUrbanID.get(x).toString();
                            Log.e("comUrban", companyUrban);
                            txtCompanyUrban.setError(null);
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
        listJob.clear();listJobID.clear();
        listJobField.clear();listJobFieldID.clear();
        listJobPosition.clear();listJobPositionID.clear();
        listExperience.clear();listExperienceID.clear();
        listIncome.clear();listIncomeID.clear();
        listFundsSource.clear();listFundsSourceID.clear();
        listProvince.clear();listProvinceID.clear();
        listCity.clear();listCityID.clear();
    }

    private void setNoError(){
        txtJob.setError(null);
        txtJobField.setError(null);
        txtJobPosition.setError(null);
        txtExperience.setError(null);
        txtIncome.setError(null);
        txtFundsSource.setError(null);
        txtCompanyName.setError(null);
        txtCompanyAddress.setError(null);
        txtCompanyProvince.setError(null);
        txtCompanyCity.setError(null);
        txtCompanyDistrict.setError(null);
        txtCompanyUrban.setError(null);
        txtCompanyRT.setError(null);
        txtCompanyRW.setError(null);
        txtCompanyPostalCode.setError(null);
    }

    private void cekError(){
        if(jobs.isEmpty()){txtJob.setError(getString(R.string.cannotnull));}else{txtJob.setError(null);}
        if(jobField.isEmpty()){txtJobField.setError(getString(R.string.cannotnull));}else{txtJobField.setError(null);}
        if(jobPosition.isEmpty()){txtJobPosition.setError(getString(R.string.cannotnull));}else{txtJobPosition.setError(null);}
        if(experience.isEmpty()){txtExperience.setError(getString(R.string.cannotnull));}else{txtExperience.setError(null);}
        if(income.isEmpty()){txtIncome.setError(getString(R.string.cannotnull));}else{txtIncome.setError(null);}
        if(fundsSource.isEmpty()){txtFundsSource.setError(getString(R.string.cannotnull));}else{txtFundsSource.setError(null);}
        if(companyName.isEmpty()){txtCompanyName.setError(getString(R.string.cannotnull));}else{txtCompanyName.setError(null);}
        if(companyAddress.isEmpty()){txtCompanyAddress.setError(getString(R.string.cannotnull));}else{txtCompanyAddress.setError(null);}
        if(companyProvince.isEmpty()){txtCompanyProvince.setError(getString(R.string.cannotnull));}else{txtCompanyProvince.setError(null);}
        if(companyCity.isEmpty()){txtCompanyCity.setError(getString(R.string.cannotnull));}else{txtCompanyCity.setError(null);}
        if(companyDistrict.isEmpty()){txtCompanyDistrict.setError(getString(R.string.cannotnull));}else{txtCompanyDistrict.setError(null);}
        if(companyUrban.isEmpty()){txtCompanyUrban.setError(getString(R.string.cannotnull));}else{txtCompanyUrban.setError(null);}
        if(companyRT.isEmpty()){txtCompanyRT.setError(getString(R.string.cannotnull));}else{txtCompanyRT.setError(null);}
        if(companyRW.isEmpty()){txtCompanyRW.setError(getString(R.string.cannotnull));}else{txtCompanyRW.setError(null);}
        if(companyPostalCode.isEmpty()){txtCompanyPostalCode.setError(getString(R.string.cannotnull));}else if(companyPostalCode.length() > 5){txtCompanyPostalCode.setError(getString(R.string.postal_code_max_char));}
        else{txtCompanyPostalCode.setError(null);}
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