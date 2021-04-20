package byc.avt.avanteelender.view.auth;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.helper.Routes;
import byc.avt.avanteelender.intro.WalkthroughActivity;
import byc.avt.avanteelender.model.UserData;
import byc.avt.avanteelender.view.MainActivity;
import byc.avt.avanteelender.view.features.penarikan.PenarikanDanaActivity;
import byc.avt.avanteelender.view.misc.OTPActivity;
import byc.avt.avanteelender.viewmodel.AuthenticationViewModel;

public class LoginActivity extends AppCompatActivity {

    Fungsi f = new Fungsi(LoginActivity.this);
    Toolbar bar;
    private TextInputLayout editPassword, editEmail;
    private String email = "", password = "";
    private Button btnLogin;
    private AuthenticationViewModel viewModel;
    private PrefManager prefManager;
    private Dialog dialog;
    private TextView linkForgotPassword, notReceivedConfirmationEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masuk);
        bar = findViewById(R.id.toolbar_masuk);
        setSupportActionBar(bar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_back_24px);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        prefManager = PrefManager.getInstance(LoginActivity.this);
        linkForgotPassword = findViewById(R.id.txt_forgot_password_masuk);
        notReceivedConfirmationEmail = findViewById(R.id.txt_not_received_confirmation_email_masuk);
        dialog = GlobalVariables.loadingDialog(LoginActivity.this);
        editEmail = findViewById(R.id.edit_email_masuk);
        editPassword = findViewById(R.id.edit_password_masuk);
        btnLogin = findViewById(R.id.btn_masuk);
        viewModel = new ViewModelProvider(this).get(AuthenticationViewModel.class);
        Objects.requireNonNull(editPassword.getEditText()).addTextChangedListener(cekPassTextWatcher);

        linkForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(f.clickAnim());
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        notReceivedConfirmationEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(f.clickAnim());
                Intent intent = new Intent(LoginActivity.this, ResendEmailVerifActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

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

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this, RegistrationFormActivity.class);
//                startActivity(intent);
                confirmLogin();
            }
        });
    }

    public void confirmLogin() {
        // POST to server through endpoint
        dialog.show();
        viewModel.login(email, password);
        viewModel.getLoginResult().observe(LoginActivity.this, checkSuccess);
    }

    private Observer<JSONObject> checkSuccess = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            JSONObject res;
            String msg = "";
            try {
                if (result.getInt("code") == 200 && result.getBoolean("status") == true) {
                    Intent i = null;
                    String token = result.getString("token");
                    res = result.getJSONObject("result");
                    String uid = res.getString("uid");
                    int verif = res.getInt("avantee_verif");
                    if(verif == 1){
                        if(res.isNull("doc")){
                            Log.e("Doc", "Aman");
                            if(res.isNull("privy_status")){
                                Log.e("PrivyStatus", "Aman");
                                if(res.isNull("suratkuasa")){
                                    Log.e("TTDSuratKuasa", "Aman");
                                    if(res.isNull("suratperjanjian")){
                                        Log.e("TTDSuratPK", "Aman");
                                        UserData ud = new UserData(email,password,uid,res.getInt("type"),res.getString("client_type"),res.getString("avatar"),res.getString("name"),verif,token,0);
                                        prefManager.setUserData(ud);
                                        i = new Intent(LoginActivity.this, MainActivity.class);
                                        f.showMessage("Selamat datang "+res.getString("name"));
                                    }else{
                                        msg = res.getJSONObject("suratperjanjian").getString("msg");
                                        f.showMessage(msg);
                                        //diarahkan untuk ttd surat perjanjian kerja sama
                                    }
                                }else{
                                    msg = res.getJSONObject("suratkuasa").getString("msg");
                                    f.showMessage(msg);
                                    //diarahkan untuk ttd surat kuasa
                                }
                            }else{
                                msg = res.getJSONObject("privy_status").getString("msg");
                                f.showMessage(msg);
                            }
                        }else{
                            i = new Intent(LoginActivity.this, RegistrationFormActivity.class);
                        }
                    }else{
                        i = new Intent(LoginActivity.this, OTPActivity.class);
                    }

                    new Routes(LoginActivity.this).moveInFinish(i);
                }else{
                    res = result.getJSONObject("result");
                    msg = res.getString("message");
                    f.showMessage(msg);
                }

                dialog.cancel();
            } catch (JSONException e) {
                e.printStackTrace();
                dialog.cancel();
            }

            dialog.cancel();
        }
    };

//    private Observer<JSONObject> checkSuccess = new Observer<JSONObject>() {
//        @Override
//        public void onChanged(JSONObject result) {
//            if(result.equals("success")) {
//                dialog.cancel();
//                if(prefManager.getName().equalsIgnoreCase("null") || prefManager.getName() == null || prefManager.getName() == "null"){
//                    ///isi di sini untuk memulai pendaftaran registrasi form
//                    //startActivity(new Intent(LoginActivity.this, SignersCheckActivity.class));
//                    //startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                    startActivity(new Intent(LoginActivity.this, RegistrationFormActivity.class));
//                    overridePendingTransition(R.anim.enter, R.anim.exit);
//                    finish();
//                }else if(!prefManager.getName().equalsIgnoreCase("null")){
//                    //startActivity(new Intent(LoginActivity.this, SignersCheckActivity.class));
//                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                    f.showMessage("Selamat datang "+prefManager.getName()+".");
//                    overridePendingTransition(R.anim.enter, R.anim.exit);
//                    finish();
//                }
//            }else if(result.equals("failed")){
//                dialog.cancel();
//                f.showMessage("Email atau password tidak sesuai, silahkan coba lagi.");
//            }else if(result.equals("failed2")){
//                dialog.cancel();
//                f.showMessage("Login gagal, silahkan coba lagi");
//            }else if(result.equals("not_verified")){
//                dialog.cancel();
//                startActivity(new Intent(LoginActivity.this, OTPActivity.class));
//                overridePendingTransition(R.anim.enter, R.anim.exit);
//                finish();
//            }
//            dialog.cancel();
//        }
//    };

    boolean emailisvalid = false;
    public void cekEmail(String mail){
        if(TextUtils.isEmpty(mail)){
            editEmail.setError("Field email harus diisi!");
        }else{
            emailisvalid = Patterns.EMAIL_ADDRESS.matcher(mail).matches();
            if(emailisvalid){
                editEmail.setError(null);
            }else{
                editEmail.setError("Email tidak valid!");
            }
        }
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
            passisvalid = GlobalVariables.PASSWORD_PATTERN2.matcher(password).matches();
            if(passisvalid){
                editPassword.setError(null);
            }else{
                //editPassword.setError("Kata sandi setidaknya harus memiliki 1 huruf kapital, 1 huruf kecil & 1 angka. (min. 8 huruf & maks. 12 huruf)");
            }
            cekDone();
        }
    };

    boolean allisfilled = false;
    private void cekDone(){
        if(emailisvalid && !email.isEmpty() && passisvalid && !password.isEmpty()){
            allisfilled = true;
        }else{
            allisfilled = false;
        }
        btnLogin.setEnabled(allisfilled);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            new Routes(LoginActivity.this).moveOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new Routes(LoginActivity.this).moveOut();
    }
}
