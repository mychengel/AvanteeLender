package byc.avt.avanteelender.view.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import byc.avt.avanteelender.intro.SplashActivity;
import byc.avt.avanteelender.intro.WalkthroughActivity;
import byc.avt.avanteelender.view.sheet.ConfirmationSheetFragment;
import byc.avt.avanteelender.viewmodel.AuthenticationViewModel;

public class SetNewPasswordActivity extends AppCompatActivity {

    private PrefManager prefManager;
    Fungsi f = new Fungsi(SetNewPasswordActivity.this);
    private AuthenticationViewModel viewModel;
    private Dialog dialog;
    private TextInputLayout edit_pass;
    private Button btn_simpan;
    boolean passIsValid = false;
    String password = "", authkey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        prefManager = PrefManager.getInstance(SetNewPasswordActivity.this);
        dialog = GlobalVariables.loadingDialog(SetNewPasswordActivity.this);
        viewModel = new ViewModelProvider(SetNewPasswordActivity.this).get(AuthenticationViewModel.class);
        edit_pass = findViewById(R.id.edit_newpass_set_new_pass);
        btn_simpan = findViewById(R.id.btn_atur_ulang_set_new_pass);
        authkey = prefManager.getResetPasswordKey();

        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();
        Log.e("IntentURIParams", data.getLastPathSegment());

        edit_pass.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                password = Objects.requireNonNull(edit_pass.getEditText()).getText().toString().trim();
                passIsValid = GlobalVariables.PASSWORD_PATTERN.matcher(password).matches();
                if(passIsValid){
                    edit_pass.setError(null);
                    btn_simpan.setEnabled(true);
                }else{
                    edit_pass.setError(getString(R.string.pass_terms));
                    btn_simpan.setEnabled(false);
                }
            }
        });

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                viewModel.setNewPassword(password, authkey);
                viewModel.getResultSetNewPassword().observe(SetNewPasswordActivity.this, showResult);
            }
        });

    }

    private Observer<JSONObject> showResult = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getBoolean("status") == true){
                    prefManager.clearResetPasswordKeyData();
                    new ConfirmationSheetFragment(R.raw.new_password_done_once, getString(R.string.new_pass_success), getString(R.string.new_pass_success_desc));
                    ConfirmationSheetFragment sheetFragment = ConfirmationSheetFragment.getInstance();
                    sheetFragment.show(getSupportFragmentManager(), sheetFragment.getTag());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(SetNewPasswordActivity.this, WalkthroughActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            new Routes(SetNewPasswordActivity.this).moveInFinish(intent);
                        }
                    }, 3000);
                }else{
                    f.showMessage(getString(R.string.set_new_pass_failed));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                getString(R.string.set_new_pass_failed);
                dialog.cancel();
            }
            dialog.cancel();
        }
    };

    @Override
    public void onBackPressed() {

    }
}