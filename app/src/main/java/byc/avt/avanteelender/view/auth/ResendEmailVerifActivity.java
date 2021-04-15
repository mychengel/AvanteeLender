package byc.avt.avanteelender.view.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.view.sheet.ConfirmationSheetFragment;
import byc.avt.avanteelender.viewmodel.AuthenticationViewModel;

public class ResendEmailVerifActivity extends AppCompatActivity {

    private PrefManager prefManager;
    Fungsi f = new Fungsi(ResendEmailVerifActivity.this);
    private AuthenticationViewModel viewModel;
    Toolbar toolbar;
    private Dialog dialog;
    private Button btn_kirim_ulang;
    private TextInputLayout editEmail;
    String email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resend_email_verif);
        prefManager = PrefManager.getInstance(ResendEmailVerifActivity.this);
        toolbar = findViewById(R.id.tb_resend_email);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_back_24px);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        dialog = GlobalVariables.loadingDialog(ResendEmailVerifActivity.this);
        viewModel = new ViewModelProvider(ResendEmailVerifActivity.this).get(AuthenticationViewModel.class);

        editEmail = findViewById(R.id.edit_email_resend_email);
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
            }
        });

        btn_kirim_ulang = findViewById(R.id.btn_kirim_ulang_resend_email);
        btn_kirim_ulang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendEmail();
            }
        });
    }

    public void resendEmail() {
        // POST to server through endpoint
        dialog.show();
        viewModel.setResendEmail(email);
        viewModel.getResultResendEmail().observe(ResendEmailVerifActivity.this, showResult);
    }

    private Observer<JSONObject> showResult = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    JSONObject job = result.getJSONObject("result");
                    String msg = job.getString("messages");
                    new ConfirmationSheetFragment(R.raw.email_sent_once, getString(R.string.email_terkirim), msg);
                    ConfirmationSheetFragment sheetFragment = ConfirmationSheetFragment.getInstance();
                    sheetFragment.show(getSupportFragmentManager(), sheetFragment.getTag());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onBackPressed();
                            onBackPressed();
                        }
                    }, 3000);
                }else{
                    f.showMessage(getString(R.string.resend_email_failed));
                    dialog.cancel();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                dialog.cancel();
            }
            dialog.cancel();
        }
    };

    boolean emailisvalid = false;
    public void cekEmail(String mail){
        if(TextUtils.isEmpty(mail)){
            editEmail.setError("Field email harus diisi!");
            btn_kirim_ulang.setEnabled(false);
        }else{
            emailisvalid = Patterns.EMAIL_ADDRESS.matcher(mail).matches();
            if(emailisvalid){
                editEmail.setError(null);
                btn_kirim_ulang.setEnabled(true);
            }else{
                editEmail.setError("Email tidak valid!");
                btn_kirim_ulang.setEnabled(false);
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
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