package byc.avt.avanteelender.view.features.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RadioButton;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.viewmodel.AuthenticationViewModel;

public class WorkInfoShowActivity extends AppCompatActivity {

    Fungsi f = new Fungsi(WorkInfoShowActivity.this);
    private PrefManager prefManager;
    private Dialog dialog;
    private Toolbar toolbar;
    GlobalVariables gv;
    JSONObject job;

    private RadioButton radButtonIsOnlineBased;
    TextInputLayout txtJob, txtJobField, txtJobPosition, txtExperience, txtIncome, txtCompanyName,
            txtCompanyNumber, txtFundsSource, txtCompanyAddress, txtCompanyProvince, txtCompanyCity,
            txtCompanyDistrict, txtCompanyUrban, txtCompanyRT, txtCompanyRW, txtCompanyPostalCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_info_show);
        prefManager = PrefManager.getInstance(WorkInfoShowActivity.this);
        toolbar = findViewById(R.id.toolbar_work_info_show);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_back_24px);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dialog = GlobalVariables.loadingDialog(WorkInfoShowActivity.this);

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
            if(job.getString("online_based").equalsIgnoreCase("Ya")){
                radButtonIsOnlineBased = findViewById(R.id.rad_yes_fr_work_info_show);
            }else{
                radButtonIsOnlineBased = findViewById(R.id.rad_no_fr_work_info_show);
            }
            radButtonIsOnlineBased.setChecked(true);
            txtJob.getEditText().setText(job.getString("client_job"));
            txtJobField.getEditText().setText(job.getString("client_job_type"));
            txtJobPosition.getEditText().setText(job.getString("job_position"));
            txtExperience.getEditText().setText(job.getString("job_experience").replaceAll("[lgt;&]", ""));
            txtIncome.getEditText().setText(job.getString("income"));
            txtCompanyName.getEditText().setText(job.getString("company_name"));
            txtCompanyNumber.getEditText().setText(job.getString("office_phone"));
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