package byc.avt.avanteelender.view.features.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.viewmodel.AuthenticationViewModel;

public class PersonalDataShowActivity extends AppCompatActivity {

    Fungsi f = new Fungsi(PersonalDataShowActivity.this);
    private PrefManager prefManager;
    private Dialog dialog;
    private Toolbar toolbar;
    GlobalVariables gv;
    JSONObject job;

    private RadioGroup radGroupGender;
    private RadioButton radButtonGender;
    TextInputLayout txtName, txtBirthPlace, txtBirthDate, txtCivil, txtStatus, txtReligion, txtEdu, txtMotherName, txtHomePhone;
    EditText editBirthDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data_show);
        prefManager = PrefManager.getInstance(PersonalDataShowActivity.this);
        toolbar = findViewById(R.id.toolbar_personal_data_show);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_back_24px);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dialog = GlobalVariables.loadingDialog(PersonalDataShowActivity.this);

        txtName = findViewById(R.id.edit_name_fr_personal_data_show);
        txtBirthPlace = findViewById(R.id.edit_birth_place_fr_personal_data_show);
        txtBirthDate = findViewById(R.id.edit_birth_date_fr_personal_data_show);
        txtCivil = findViewById(R.id.edit_kewarganegaraan_fr_personal_data_show);
        txtStatus = findViewById(R.id.edit_status_fr_personal_data_show);
        txtReligion = findViewById(R.id.edit_religion_fr_personal_data_show);
        txtEdu = findViewById(R.id.edit_education_fr_personal_data_show);
        txtMotherName = findViewById(R.id.edit_mothername_fr_personal_data_show);
        txtHomePhone = findViewById(R.id.edit_homenumber_fr_personal_data_show);

        Intent i = getIntent();
        try {
            job = new JSONObject(i.getStringExtra("jobPersonalData"));
            if(job.getString("gender").equalsIgnoreCase("Perempuan")){
                radButtonGender = findViewById(R.id.rad_female_fr_personal_data_show);
            }else{
                radButtonGender = findViewById(R.id.rad_male_fr_personal_data_show);
            }
            radButtonGender.setChecked(true);
            txtName.getEditText().setText(job.getString("name"));
            txtBirthPlace.getEditText().setText(job.getString("birth_place"));
            txtBirthPlace.getEditText().setText(job.getString("birth_date"));
            txtCivil.getEditText().setText(job.getString("citizenship"));
            txtStatus.getEditText().setText(job.getString("marital_status"));
            txtReligion.getEditText().setText(job.getString("religion"));
            txtEdu.getEditText().setText(job.getString("education"));
            txtMotherName.getEditText().setText(job.getString("mother_maiden_name"));
            txtHomePhone.getEditText().setText(job.getString("home_phone"));
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