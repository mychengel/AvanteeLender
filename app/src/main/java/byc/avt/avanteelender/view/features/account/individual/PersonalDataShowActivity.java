package byc.avt.avanteelender.view.features.account.individual;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.helper.Routes;
import byc.avt.avanteelender.viewmodel.AuthenticationViewModel;
import byc.avt.avanteelender.viewmodel.MasterDataViewModel;

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

    private MasterDataViewModel viewModel;
    private AuthenticationViewModel viewModel2;
    Button btn_save, btn_edit;
    boolean editIsOn = false;
    AutoCompleteTextView auto_kewarganegaraan, auto_status, auto_religion, auto_education;
    String name="", gender="male", birthplace="", birthdate="", civil="", status="", religion="", education="", telprumah="", mothername="";
    List<Object> listCivil = new ArrayList<>();
    List<Object> listCivilID = new ArrayList<>();
    List<Object> listReligion = new ArrayList<>();
    List<Object> listReligionID = new ArrayList<>();
    List<Object> listEdu = new ArrayList<>();
    List<Object> listEduID = new ArrayList<>();
    List<Object> listStatus = new ArrayList<>();
    List<Object> listStatusID = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data_show);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        gv.perEditData.clear();
        gv.perEditDataFile.clear();
        viewModel = new ViewModelProvider(this).get(MasterDataViewModel.class);
        viewModel2 = new ViewModelProvider(this).get(AuthenticationViewModel.class);
        prefManager = PrefManager.getInstance(PersonalDataShowActivity.this);
        toolbar = findViewById(R.id.toolbar_personal_data_show);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_back_24px);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dialog = GlobalVariables.loadingDialog(PersonalDataShowActivity.this);

        auto_kewarganegaraan = findViewById(R.id.auto_kewarganegaraan_fr_personal_data_show);
        auto_status = findViewById(R.id.auto_status_fr_personal_data_show);
        auto_religion = findViewById(R.id.auto_religion_fr_personal_data_show);
        auto_education = findViewById(R.id.auto_education_fr_personal_data_show);
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
                gender = "female";
            }else{
                radButtonGender = findViewById(R.id.rad_male_fr_personal_data_show);
                gender = "male";
            }
            radButtonGender.setChecked(true);
            txtName.getEditText().setText(job.getString("name"));
            txtBirthPlace.getEditText().setText(job.getString("birth_place"));
            txtBirthDate.getEditText().setText(f.dateStd(job.getString("birth_date")));
            birthdate = f.dateStd(job.getString("birth_date"));
            txtCivil.getEditText().setText(job.getString("citizenship"));
            txtStatus.getEditText().setText(job.getString("marital_status"));
            txtReligion.getEditText().setText(job.getString("religion"));
            txtEdu.getEditText().setText(job.getString("education"));
            txtMotherName.getEditText().setText(job.getString("mother_maiden_name"));
            txtHomePhone.getEditText().setText(job.getString("home_phone"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        editBirthDate = findViewById(R.id.editText_birthdate_fr_personal_data_show);
        editBirthDate.setFocusable(false);
        editBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
                builder.setTitleText("Pilih tanggal lahir");
                builder.setSelection(Calendar.getInstance().getTimeInMillis());
                MaterialDatePicker picker = builder.build();
                picker.show(getSupportFragmentManager(),"BIRTHDATE");
                picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        birthdate = sdf.format(selection);
                        editBirthDate.setText(birthdate);
                    }
                });
            }
        });

        radGroupGender = findViewById(R.id.rad_group_gender_fr_personal_data_show);
        radGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radButtonGender = findViewById(selectedId);
                if(radButtonGender.getText().equals("Laki-laki")){
                    gender = "male";
                }else{
                    gender = "female";
                }
            }
        });

        btn_save = findViewById(R.id.btn_save_fr_personal_data_show);
        btn_save.setEnabled(true);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmNext(v);
            }
        });

        btn_edit = findViewById(R.id.btn_ubah_fr_personal_data_show);
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

    private void confirmNext(View v){
        name = Objects.requireNonNull(txtName.getEditText().getText().toString().trim());
        birthplace = Objects.requireNonNull(txtBirthPlace.getEditText().getText().toString().trim());
        mothername = Objects.requireNonNull(txtMotherName.getEditText().getText().toString().trim());
        telprumah = Objects.requireNonNull(txtHomePhone.getEditText().getText().toString().trim());

        if(!name.isEmpty() && !gender.isEmpty() && !birthplace.isEmpty() && !birthdate.isEmpty()
                && !civil.isEmpty() && !status.isEmpty() && !religion.isEmpty()
                && !education.isEmpty() && !mothername.isEmpty()){
            gv.perEditData.put("event_name","data_pribadi");
            gv.perEditData.put("tipe_investor",prefManager.getClientType());
            gv.perEditData.put("nama",name);
            gv.perEditData.put("jenis_kelamin",gender);
            gv.perEditData.put("tempat_lahir",birthplace);
            gv.perEditData.put("tanggal_lahir",birthdate);
            gv.perEditData.put("kewarganegaraan",civil);
            gv.perEditData.put("status_pernikahan",status);
            gv.perEditData.put("agama",religion);
            gv.perEditData.put("pendidikan",education);
            gv.perEditData.put("no_telepon_rumah",telprumah);
            gv.perEditData.put("nama_ibu_kandung",mothername);
            setNoError();
            updateDocument();
        }else{
            cekError();
        }
    }

    private void updateDocument(){
        new AlertDialog.Builder(PersonalDataShowActivity.this)
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
                        viewModel2.getResultUpdatePersonalDoc().observe(PersonalDataShowActivity.this, showResult);
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
                    new Fungsi(PersonalDataShowActivity.this).showMessage(getString(R.string.success_update_data));
                    dialog.cancel();
                    new Routes(PersonalDataShowActivity.this).moveOut();
                }else{
                    //new Fungsi(PersonalDataShowActivity.this).showMessage(getString(R.string.failed_update_data));
                    dialog.cancel();
                    JSONObject jobRes = result.getJSONObject("result");
                    String msg = f.docErr400(jobRes.toString());
                    new AlertDialog.Builder(PersonalDataShowActivity.this)
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
                }
            } catch (JSONException e) {
                e.printStackTrace();
                String msg = getString(R.string.system_in_trouble);
                Log.e("Respon per cr doc", msg);
                new Fungsi(PersonalDataShowActivity.this).showMessage(msg);
                dialog.cancel();
            }
        }
    };

    public void editIsOn(boolean s){
        txtName.setEnabled(s);
        txtBirthPlace.setEnabled(s);
        txtBirthDate.setEnabled(s);
        txtCivil.setEnabled(s);
        txtStatus.setEnabled(s);
        txtReligion.setEnabled(s);
        txtEdu.setEnabled(s);
        txtMotherName.setEnabled(s);
        txtHomePhone.setEnabled(s);
        for(int i = 0; i < radGroupGender.getChildCount(); i++){
            ((RadioButton)radGroupGender.getChildAt(i)).setEnabled(s);
        }
        btn_save.setEnabled(s);
    }

    public void loadData(){
        clearMasterList();
        dialog.show();
        viewModel.getCivil(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultCivil().observe(PersonalDataShowActivity.this, showCivil);
        viewModel.getStatus(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultStatus().observe(PersonalDataShowActivity.this, showStatus);
        viewModel.getReligion(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultReligion().observe(PersonalDataShowActivity.this, showReligion);
        viewModel.getEducation(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultEducation().observe(PersonalDataShowActivity.this, showEducation);
    }


    private Observer<JSONObject> showCivil = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    JSONObject jobRes = result.getJSONObject("result");
                    JSONArray jar = jobRes.getJSONArray("negara");
                    for(int i = 0; i < jar.length(); i++){
                        listCivil.add(jar.getJSONObject(i).getString("name"));
                        listCivilID.add(jar.getJSONObject(i).getString("id"));
                        if(jar.getJSONObject(i).getString("name").equalsIgnoreCase(job.getString("citizenship"))){
                            civil = jar.getJSONObject(i).getString("id");
                        }
                        Log.e("Data civil", civil);
                    }
                    ArrayAdapter adapter = new ArrayAdapter(PersonalDataShowActivity.this, R.layout.support_simple_spinner_dropdown_item, listCivil);
                    auto_kewarganegaraan.setAdapter(adapter);
                    auto_kewarganegaraan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                            civil = listCivilID.get(x).toString();
                            Log.e("civil", civil);
                            txtCivil.setError(null);
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

    private Observer<JSONObject> showStatus = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    JSONObject jobRes = result.getJSONObject("result");
                    JSONArray jar = jobRes.getJSONArray("marital");
                    for(int i = 0; i < jar.length(); i++){
                        listStatus.add(jar.getJSONObject(i).getString("name"));
                        listStatusID.add(jar.getJSONObject(i).getString("id"));
                        if(jar.getJSONObject(i).getString("name").equalsIgnoreCase(job.getString("marital_status"))){
                            status = jar.getJSONObject(i).getString("id");
                        }
                        Log.e("Data status", status);
                    }
                    ArrayAdapter adapter = new ArrayAdapter(PersonalDataShowActivity.this, R.layout.support_simple_spinner_dropdown_item, listStatus);
                    auto_status.setAdapter(adapter);
                    auto_status.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                            status = listStatusID.get(x).toString();
                            Log.e("status", status);
                            txtStatus.setError(null);
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

    private Observer<JSONObject> showReligion = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    JSONObject jobRes = result.getJSONObject("result");
                    JSONArray jar = jobRes.getJSONArray("religion");
                    for(int i = 0; i < jar.length(); i++){
                        listReligion.add(jar.getJSONObject(i).getString("name"));
                        listReligionID.add(jar.getJSONObject(i).getString("id"));
                        if(jar.getJSONObject(i).getString("name").equalsIgnoreCase(job.getString("religion"))){
                            religion = jar.getJSONObject(i).getString("id");
                        }
                        Log.e("Data religion", religion);
                    }
                    ArrayAdapter adapter = new ArrayAdapter(PersonalDataShowActivity.this, R.layout.support_simple_spinner_dropdown_item, listReligion);
                    auto_religion.setAdapter(adapter);
                    auto_religion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                            religion = listReligionID.get(x).toString();
                            Log.e("religion", religion);
                            txtReligion.setError(null);
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

    private Observer<JSONObject> showEducation = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    JSONObject jobRes = result.getJSONObject("result");
                    JSONArray jar = jobRes.getJSONArray("education");
                    for(int i = 0; i < jar.length(); i++){
                        listEdu.add(jar.getJSONObject(i).getString("name"));
                        listEduID.add(jar.getJSONObject(i).getString("id"));
                        if(jar.getJSONObject(i).getString("name").equalsIgnoreCase(job.getString("education"))){
                            education = jar.getJSONObject(i).getString("id");
                        }
                        Log.e("Data education", education);
                    }
                    ArrayAdapter adapter = new ArrayAdapter(PersonalDataShowActivity.this, R.layout.support_simple_spinner_dropdown_item, listEdu);
                    auto_education.setAdapter(adapter);
                    auto_education.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                            education = listEduID.get(x).toString();
                            Log.e("education", education);
                            txtEdu.setError(null);
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
        listCivil.clear();listCivilID.clear();
        listStatus.clear();listStatusID.clear();
        listReligion.clear();listReligionID.clear();
        listEdu.clear();listEduID.clear();
    }

    private void setNoError(){
        txtName.setError(null);
        txtBirthPlace.setError(null);
        txtBirthDate.setError(null);
        txtCivil.setError(null);
        txtStatus.setError(null);
        txtReligion.setError(null);
        txtEdu.setError(null);
        txtMotherName.setError(null);
    }

    private void cekError(){
        if(name.isEmpty()){txtName.setError(getString(R.string.cannotnull));}else{txtName.setError(null);}
        if(birthplace.isEmpty()){txtBirthPlace.setError(getString(R.string.cannotnull));}else{txtBirthPlace.setError(null);}
        if(birthdate.isEmpty()){txtBirthDate.setError(getString(R.string.cannotnull));}else{txtBirthDate.setError(null);}
        if(civil.isEmpty()){txtCivil.setError(getString(R.string.cannotnull));}else{txtCivil.setError(null);}
        if(status.isEmpty()){txtStatus.setError(getString(R.string.cannotnull));}else{txtStatus.setError(null);}
        if(religion.isEmpty()){txtReligion.setError(getString(R.string.cannotnull));}else{txtReligion.setError(null);}
        if(education.isEmpty()){txtEdu.setError(getString(R.string.cannotnull));}else{txtEdu.setError(null);}
        if(mothername.isEmpty()){txtMotherName.setError(getString(R.string.cannotnull));}else{txtMotherName.setError(null);}
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            new Routes(PersonalDataShowActivity.this).moveOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new Routes(PersonalDataShowActivity.this).moveOut();
    }
}