package byc.avt.avanteelender.view.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.Routes;
import byc.avt.avanteelender.intro.WalkthroughActivity;
import byc.avt.avanteelender.view.others.TermsActivity;
import byc.avt.avanteelender.view.sheet.TermSheetFragment;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.model.User;
import byc.avt.avanteelender.viewmodel.AuthenticationViewModel;

public class RegistrationActivity extends AppCompatActivity {

    public RegistrationActivity(){}

    Fungsi f = new Fungsi(RegistrationActivity.this);
    Toolbar toolbar;
    private TextInputLayout editPhoneNumber, editPassword, editEmail, editRefId, editConfirmPassword;
    private Button btnRegister;
    public CheckBox checkAgree;
    private AuthenticationViewModel viewModel;
    private String phoneNumber = "", password = "", rePassword = "", email = "", refId = "";
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        toolbar = findViewById(R.id.toolbar_daftar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_back_24px);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        dialog = GlobalVariables.loadingDialog(RegistrationActivity.this);

        editEmail = findViewById(R.id.edit_email_daftar);
        editPhoneNumber = findViewById(R.id.edit_phone_daftar);
        editPassword = findViewById(R.id.edit_password_daftar);
        editConfirmPassword = findViewById(R.id.edit_re_password_daftar);
        editRefId = findViewById(R.id.edit_ref_id_daftar);
        btnRegister = findViewById(R.id.btn_daftar);
        viewModel = new ViewModelProvider(RegistrationActivity.this).get(AuthenticationViewModel.class);
        Objects.requireNonNull(editPassword.getEditText()).addTextChangedListener(cekPassTextWatcher);
        Objects.requireNonNull(editConfirmPassword.getEditText()).addTextChangedListener(cekPassTextWatcher);

        loadTermsAndCondition();

        editEmail.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                email = editEmail.getEditText().getText().toString().trim();
                cekEmail(email);
                cekDone();
            }
        });

        editPhoneNumber.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                phoneNumber = Objects.requireNonNull(editPhoneNumber.getEditText()).getText().toString().trim();
                cekPhone(phoneNumber);
                cekDone();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmRegister();
            }
        });
    }

    boolean emailisvalid = false;
    public void cekEmail(String mail){
        if(TextUtils.isEmpty(mail)){
            editEmail.setError(getString(R.string.cannotnull));
        }else{
            emailisvalid = Patterns.EMAIL_ADDRESS.matcher(mail).matches();
            if(emailisvalid){
                editEmail.setError(null);
            }else{
                editEmail.setError(getString(R.string.email_not_valid));
            }
        }
    }

    boolean phoneisvalid = false;
    public void cekPhone(String phone){
        if(TextUtils.isEmpty(phone)){
            editPhoneNumber.setError(getString(R.string.cannotnull));
        }else if(phone.length() < 6 || phone.length() > 14){
            editPhoneNumber.setError(getString(R.string.phone_not_valid));
            phoneisvalid = false;
        }else{
            phoneisvalid = Patterns.PHONE.matcher(phone).matches();
            if(phoneisvalid){
                editPhoneNumber.setError(null);
            }else{
                editPhoneNumber.setError(getString(R.string.phone_not_valid));
            }
        }
    }

    boolean allisfilled = false;
    private void cekDone(){
        if(passisvalid && emailisvalid && phoneisvalid && !email.isEmpty() && !phoneNumber.isEmpty() && !password.isEmpty() && !rePassword.isEmpty() && password.equals(rePassword)){
            allisfilled = true;
        }else{
            allisfilled = false;
        }
        btnRegister.setEnabled(allisfilled);
    }

    boolean passisvalid = false;
    private TextWatcher cekPassTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        @Override
        public void afterTextChanged(Editable editable) {
            password = Objects.requireNonNull(editPassword.getEditText()).getText().toString().trim();
            passisvalid = GlobalVariables.PASSWORD_PATTERN.matcher(password).matches();
            rePassword = Objects.requireNonNull(editConfirmPassword.getEditText()).getText().toString().trim();
            if(passisvalid){
                editPassword.setError(null);
                if (rePassword.equals(password)){
                    editConfirmPassword.setError(null);
                } else {
                    editConfirmPassword.setError(getString(R.string.pass_must_same));
                }
            }else{
                editPassword.setError(getString(R.string.pass_terms));
            }
            cekDone();
        }
    };

    public void confirmRegister() {
        refId = Objects.requireNonNull(editRefId.getEditText()).getText().toString().trim();
        // POST to server through endpoint
        dialog.show();
        User user = new User(email, phoneNumber, password, refId);
        viewModel.register(user);
        viewModel.getResult().observe(RegistrationActivity.this, checkSuccess);
    }

    private Observer<JSONObject> checkSuccess = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject response) {
            int code = 0;
            boolean status = false;
            JSONObject res;
            String msg = "";
            try {
                code = response.getInt("code");
                status = response.getBoolean("status");
                res = response.getJSONObject("result");
                if(code == 200 & status == true){
                    msg = res.getString("message");
                    f.showMessage(msg);
                    dialog.cancel();
                    Log.e("Result: ", "register success");
                    RegistrationVerifyEmailActivity.email = email;
                    TermSheetFragment.read = false;
                    Intent intent = new Intent(RegistrationActivity.this, RegistrationVerifyEmailActivity.class);
                    new Routes(RegistrationActivity.this).moveInFinish(intent);
                }else{
                    msg = res.getString("email")+res.getString("phone")+res.getString("password")+res.getString("repeatPassword")+res.getString("reffCode")+res.getString("term");
                    dialog.cancel();
                    Log.e("Result: ", msg);
                    f.showMessage(msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private void loadTermsAndCondition() {
        dialog.show();
        viewModel.getSettingDataNoAuth();
        viewModel.getResultSettingDataNoAuth().observe(RegistrationActivity.this, showSettingData);
    }

    private Observer<JSONObject> showSettingData = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    JSONObject job = result.getJSONObject("result");
                    JSONObject terms_job = job.getJSONObject("syaratketentuan");
                    String terms = terms_job.getString("content_text");
                    Spanned terms_final = f.htmlToStr(terms);
                    TermSheetFragment.text = terms_final.toString();
                    TermSheetFragment termFragment = TermSheetFragment.getInstance();
                    termFragment.setCancelable(false);
                    termFragment.show(getSupportFragmentManager(), termFragment.getTag());
                }else{
                    f.showMessage(getString(R.string.failed_load_info));
                }
                dialog.cancel();
            } catch (JSONException e) {
                e.printStackTrace();
                dialog.cancel();
            }
            dialog.cancel();
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            if(TermSheetFragment.getInstance().isVisible()){
                TermSheetFragment.getInstance().dismiss();
            }else {
                TermSheetFragment.read = false;
                new Routes(RegistrationActivity.this).moveOut();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        TermSheetFragment.read = false;
        new Routes(RegistrationActivity.this).moveOut();
    }

}