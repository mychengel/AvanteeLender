package byc.avt.avanteelender.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.view.misc.TermFragment;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.model.User;
import byc.avt.avanteelender.viewmodel.AuthenticationViewModel;

public class DaftarActivity extends AppCompatActivity {

    public DaftarActivity(){}

    Fungsi f = new Fungsi(DaftarActivity.this);
    Toolbar bar;
    private TextInputLayout editPhoneNumber, editPassword, editEmail, editRefId, editConfirmPassword;
    private String phoneNumber, password, rePassword;
    private Button btnRegister;
    public CheckBox checkAgree;
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
        checkAgree = findViewById(R.id.cb_setuju_syarat_ketentuan_daftar);
        viewModel = ViewModelProviders.of(DaftarActivity.this).get(AuthenticationViewModel.class);
        Objects.requireNonNull(editPhoneNumber.getEditText()).addTextChangedListener(registerTextWatcher);
        Objects.requireNonNull(editPassword.getEditText()).addTextChangedListener(registerTextWatcher);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmInput();
            }
        });

        checkAgree.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    TermFragment termFragment = TermFragment.getInstance();
                    termFragment.show(getSupportFragmentManager(), termFragment.getTag());
                    buttonView.setChecked(false);
                }else{

                }

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
            if(TermFragment.getInstance().isVisible()){
                TermFragment.getInstance().dismiss();
            }else {
                finish();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
