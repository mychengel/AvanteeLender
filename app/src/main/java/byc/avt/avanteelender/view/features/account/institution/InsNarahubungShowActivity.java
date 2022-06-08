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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.helper.Routes;
import byc.avt.avanteelender.viewmodel.AuthenticationViewModel;
import byc.avt.avanteelender.viewmodel.MasterDataViewModel;

public class InsNarahubungShowActivity extends AppCompatActivity {

    Button btn_save, btn_edit;
    boolean editIsOn = false;

    Fungsi f = new Fungsi(InsNarahubungShowActivity.this);
    private MasterDataViewModel viewModel;
    private AuthenticationViewModel viewModel2;
    private PrefManager prefManager;
    private Dialog dialog;
    GlobalVariables gv;
    private Toolbar toolbar;
    JSONObject job;

    TextInputLayout txtName, txtBirthDate, txtJabatan, txtPhone, txtEmail, txtNoKtp;
    EditText editBirthDate;
    String name="", birthdate="", jabatan="", phone="", email="", noKtp="", noKtpx;
    boolean emailIsValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ins_narahubung_show);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        gv.insEditData.clear();
        gv.insEditDataFile.clear();

        viewModel = new ViewModelProvider(this).get(MasterDataViewModel.class);
        viewModel2 = new ViewModelProvider(this).get(AuthenticationViewModel.class);
        prefManager = PrefManager.getInstance(InsNarahubungShowActivity.this);
        dialog = GlobalVariables.loadingDialog(InsNarahubungShowActivity.this);
        toolbar = findViewById(R.id.toolbar_ins_narahubung_show);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_back_24px);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        txtName = findViewById(R.id.edit_name_ins_narahubung_show);
        txtJabatan = findViewById(R.id.edit_position_ins_narahubung_show);
        txtPhone = findViewById(R.id.edit_phone_ins_narahubung_show);
        txtEmail = findViewById(R.id.edit_email_ins_narahubung_show);
        txtNoKtp = findViewById(R.id.edit_ktp_ins_narahubung_show);
        txtBirthDate = findViewById(R.id.edit_birth_date_ins_narahubung_show);
        editBirthDate = findViewById(R.id.editText_birthdate_ins_narahubung_show);

        txtNoKtp.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                noKtp = txtNoKtp.getEditText().getText().toString().trim();
                cekKTP(noKtp);
            }
        });

        editBirthDate.setFocusable(false);
        editBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
                builder.setTitleText("Pilih tanggal lahir");
                builder.setSelection(Calendar.getInstance().getTimeInMillis());
                MaterialDatePicker picker = builder.build();
                picker.show(InsNarahubungShowActivity.this.getSupportFragmentManager(),"BIRTHDATE");
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

        btn_save = findViewById(R.id.btn_save_ins_narahubung_show);
        btn_save.setEnabled(true);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmNext(v);
            }
        });

        Intent i = getIntent();
        try {
            job = new JSONObject(i.getStringExtra("jobNarahubungData"));
            Log.e("job", i.getStringExtra("jobNarahubungData"));
            txtName.getEditText().setText(job.getString("nama"));
            txtBirthDate.getEditText().setText(job.getString("tanggal_lahir"));
            birthdate = job.getString("tanggal_lahir");
            txtJabatan.getEditText().setText(job.getString("jabatan"));
            txtPhone.getEditText().setText(job.getString("no_telepon"));
            txtEmail.getEditText().setText(job.getString("email"));
            txtNoKtp.getEditText().setText(job.getString("no_ktp"));
            noKtp = job.getString("no_ktp");
            cekKTP(noKtp);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        btn_edit = findViewById(R.id.btn_ubah_ins_narahubung_show);
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
    }

    public void editIsOn(boolean s){
        txtName.setEnabled(s);
        txtJabatan.setEnabled(s);
        txtPhone.setEnabled(s);
        txtEmail.setEnabled(s);
        txtNoKtp.setEnabled(s);
        txtBirthDate.setEnabled(s);
        btn_save.setEnabled(s);
    }

    boolean ktpisvalid = false;
    public void cekKTP(String ktp){
        if(TextUtils.isEmpty(ktp)){
            txtNoKtp.setError(getString(R.string.cannotnull));
            ktpisvalid = false;
        }else{
            if(ktp.length() < 16){
                txtNoKtp.setError(getString(R.string.min_digit_ktp));
                ktpisvalid = false;
            }else if(ktp.length() == 16) {
                txtNoKtp.setError(null);
                ktpisvalid = true;
            }
        }
    }

    private void confirmNext(View v){
        name = Objects.requireNonNull(txtName.getEditText().getText().toString().trim());
        jabatan = Objects.requireNonNull(txtJabatan.getEditText().getText().toString().trim());
        phone = Objects.requireNonNull(txtPhone.getEditText().getText().toString().trim());
        email = Objects.requireNonNull(txtEmail.getEditText().getText().toString().trim());
        noKtp = Objects.requireNonNull(txtNoKtp.getEditText().getText().toString().trim());

        emailIsValid = Patterns.EMAIL_ADDRESS.matcher(email).matches();

        if(!name.isEmpty() && !birthdate.isEmpty() && !jabatan.isEmpty() && !phone.isEmpty() && ktpisvalid && !noKtp.isEmpty()
                && !email.isEmpty() && emailIsValid){
            gv.insEditData.put("event_name","informasi_narahubung");
            gv.insEditData.put("tipe_investor",prefManager.getClientType());
            gv.insEditData.put("nama",name);
            gv.insEditData.put("tanggal_lahir",birthdate);
            gv.insEditData.put("jabatan",jabatan);
            gv.insEditData.put("no_telepon",phone);
            gv.insEditData.put("email",email);
            gv.insEditData.put("no_ktp",noKtp);
            setNoError();
            updateDocument();
        }else{
            cekError();
        }
    }

    private void updateDocument(){
        new AlertDialog.Builder(InsNarahubungShowActivity.this)
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
                        viewModel2.getResultUpdateInstitutionDoc().observe(InsNarahubungShowActivity.this, showResult);
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
                    new Fungsi(InsNarahubungShowActivity.this).showMessage(getString(R.string.success_update_data));
                    dialog.cancel();
                    new Routes(InsNarahubungShowActivity.this).moveOut();
                }else{
                    JSONObject obj = result.getJSONObject("result");
                    String err = "";
                    err = obj.toString().replaceAll("\"", "");
                    err = err.replace("{", "");
                    err = err.replace("}", "");
                    new Fungsi(InsNarahubungShowActivity.this).showMessageLong(err);
                    dialog.cancel();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                String msg = getString(R.string.system_in_trouble);
                Log.e("Respon per cr doc", msg);
                new Fungsi(InsNarahubungShowActivity.this).showMessage(msg);
                dialog.cancel();
            }
        }
    };

    private void setNoError(){
        txtName.setError(null);
        txtBirthDate.setError(null);
        txtJabatan.setError(null);
        txtPhone.setError(null);
        txtEmail.setError(null);
        txtNoKtp.setError(null);
    }

    private void cekError(){
        if(name.isEmpty()){txtName.setError(getString(R.string.cannotnull));}else{txtName.setError(null);}
        if(birthdate.isEmpty()){txtBirthDate.setError(getString(R.string.cannotnull));}else{txtBirthDate.setError(null);}
        if(jabatan.isEmpty()){txtJabatan.setError(getString(R.string.cannotnull));}else{txtJabatan.setError(null);}
        if(phone.isEmpty()){txtPhone.setError(getString(R.string.cannotnull));}else{txtPhone.setError(null);}
        if(email.isEmpty()){
            txtEmail.setError(getString(R.string.cannotnull));
        }else if(!emailIsValid){
            txtEmail.setError(getString(R.string.email_not_valid));
        }else{txtEmail.setError(null);}
        if(noKtp.isEmpty()){
            txtNoKtp.setError(getString(R.string.cannotnull));
        }else if(noKtp.length() < 16){
            txtNoKtp.setError(getString(R.string.min_digit_ktp));
        }else{txtNoKtp.setError(null);}
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            new Routes(InsNarahubungShowActivity.this).moveOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new Routes(InsNarahubungShowActivity.this).moveOut();
    }
}