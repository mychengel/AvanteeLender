package byc.avt.avanteelender.view;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.model.User;
import byc.avt.avanteelender.viewmodel.AuthenticationViewModel;

public class DaftarActivity extends AppCompatActivity {

    Fungsi f = new Fungsi(DaftarActivity.this);
    Toolbar bar;
    private TextInputLayout editPhoneNumber, editPassword, editEmail, editRefId, editConfirmPassword;
    private String phoneNumber, password, rePassword;
    private Button btnRegister;
    private AuthenticationViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);
        bar = findViewById(R.id.toolbar_daftar);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        editEmail = findViewById(R.id.edit_email_daftar);
        editPhoneNumber = findViewById(R.id.edit_phone_daftar);
        editPassword = findViewById(R.id.edit_password_daftar);
        editConfirmPassword = findViewById(R.id.edit_re_password_daftar);
        editRefId = findViewById(R.id.edit_ref_id_daftar);
        btnRegister = findViewById(R.id.btn_daftar);
        viewModel = ViewModelProviders.of(DaftarActivity.this).get(AuthenticationViewModel.class);
        Objects.requireNonNull(editPhoneNumber.getEditText()).addTextChangedListener(registerTextWatcher);
        Objects.requireNonNull(editPassword.getEditText()).addTextChangedListener(registerTextWatcher);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmInput();
            }
        });

    }

    private TextWatcher registerTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            phoneNumber = Objects.requireNonNull(editPhoneNumber.getEditText()).getText().toString().trim();
            password = Objects.requireNonNull(editPassword.getEditText()).getText().toString().trim();
            rePassword = Objects.requireNonNull(editConfirmPassword.getEditText()).getText().toString().trim();
            btnRegister.setEnabled(!phoneNumber.isEmpty() && !password.isEmpty() && !rePassword.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private boolean checkPassword(){
        if (rePassword.equals(password)){
            editConfirmPassword.setError(null);
            return true;
        } else {
            return false;
        }
    }

    public void confirmInput() {
        if (!checkPassword()) {
            editConfirmPassword.setError("Kata sandi tidak sama");
        } else {
            String email = Objects.requireNonNull(editEmail.getEditText()).getText().toString().trim();
            String ref_id = Objects.requireNonNull(editRefId.getEditText()).getText().toString().trim();
            // POST to server through endpoint
            User user = new User();
            user.setEmail(email);
            user.setPassword(password);
            user.setPhoneNumber(phoneNumber);
            viewModel.register(user, ref_id);
            viewModel.getStatus().observe(this, checkStatus);
        }
    }

    private Observer<Boolean> checkStatus = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean aBoolean) {
            if (aBoolean) {
                f.showMessage("Success");
                //intent to sms verification

            } else {
                f.showMessage("Failed");
            }
        }
    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        finish();
    }

}
