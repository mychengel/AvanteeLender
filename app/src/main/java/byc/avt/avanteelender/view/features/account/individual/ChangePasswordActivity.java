package byc.avt.avanteelender.view.features.account.individual;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.helper.Routes;
import byc.avt.avanteelender.view.misc.OTPSettingsActivity;
import byc.avt.avanteelender.viewmodel.AuthenticationViewModel;

public class ChangePasswordActivity extends AppCompatActivity {

    private PrefManager prefManager;
    Fungsi f = new Fungsi(ChangePasswordActivity.this);
    private AuthenticationViewModel viewModel;
    Toolbar toolbar;
    private Dialog dialog;
    private Button btn_simpan;
    private TextInputLayout edit_old_pass, edit_new_pass, edit_new_pass_retype;
    String oldPass = "", newPass = "", newPassRetype = "";
    boolean passisvalid = false, oldpassisvalid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        prefManager = PrefManager.getInstance(ChangePasswordActivity.this);
        toolbar = findViewById(R.id.tb_change_pass);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_back_24px);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        dialog = GlobalVariables.loadingDialog(ChangePasswordActivity.this);
        viewModel = new ViewModelProvider(ChangePasswordActivity.this).get(AuthenticationViewModel.class);

        btn_simpan = findViewById(R.id.btn_simpan_change_pass);
        edit_old_pass = findViewById(R.id.edit_oldpass_change_pass);
        edit_new_pass = findViewById(R.id.edit_newpass_change_pass);
        edit_new_pass_retype = findViewById(R.id.edit_retype_newpass_change_pass);

        Objects.requireNonNull(edit_new_pass.getEditText()).addTextChangedListener(cekPassTextWatcher);
        Objects.requireNonNull(edit_new_pass_retype.getEditText()).addTextChangedListener(cekPassTextWatcher);
        edit_old_pass.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                oldPass = Objects.requireNonNull(edit_old_pass.getEditText()).getText().toString().trim();
                oldpassisvalid = GlobalVariables.PASSWORD_PATTERN.matcher(oldPass).matches();
                if(oldpassisvalid){
                    edit_old_pass.setError(null);
                }else{
                    edit_old_pass.setError(getString(R.string.pass_terms));
                }
                cekDone();
            }
        });

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                viewModel.setChangePassword(oldPass, newPass, prefManager.getUid(), prefManager.getToken());
                viewModel.getResultChangePassword().observe(ChangePasswordActivity.this, showResult);
            }
        });

    }

    private Observer<JSONObject> showResult = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getBoolean("status") == true){
                    dialog.cancel();
                    Intent intent = new Intent(ChangePasswordActivity.this, OTPSettingsActivity.class);
                    intent.putExtra("type", "password");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    new Fungsi(ChangePasswordActivity.this).showMessage(result.getJSONObject("result").getString("messages"));
                    new Routes(ChangePasswordActivity.this).moveInFinish(intent);
                }else{
                    new Fungsi(ChangePasswordActivity.this).showMessage(getString(R.string.change_pass_failed));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                new Fungsi(ChangePasswordActivity.this).showMessage(getString(R.string.change_pass_failed));
                dialog.cancel();
            }
            dialog.cancel();
        }
    };

    private TextWatcher cekPassTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        @Override
        public void afterTextChanged(Editable editable) {
            newPass = Objects.requireNonNull(edit_new_pass.getEditText()).getText().toString().trim();
            passisvalid = GlobalVariables.PASSWORD_PATTERN.matcher(newPass).matches();
            newPassRetype = Objects.requireNonNull(edit_new_pass_retype.getEditText()).getText().toString().trim();
            if(passisvalid){
                edit_new_pass.setError(null);
                if (newPassRetype.equals(newPass)){
                    edit_new_pass_retype.setError(null);
                    passisvalid = true;
                } else {
                    edit_new_pass_retype.setError(getString(R.string.pass_must_same));
                    passisvalid = false;
                }
            }else{
                edit_new_pass.setError(getString(R.string.pass_terms));
            }
            cekDone();
        }
    };

    private void cekDone(){
        btn_simpan.setEnabled(passisvalid && oldpassisvalid);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            new Routes(ChangePasswordActivity.this).moveOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new Routes(ChangePasswordActivity.this).moveOut();
    }
}