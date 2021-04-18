package byc.avt.avanteelender.view.misc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mukesh.OtpView;

import java.util.Objects;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.helper.receiver.OTPReceiver;
import byc.avt.avanteelender.intro.WalkthroughActivity;
import byc.avt.avanteelender.model.User;
import byc.avt.avanteelender.view.MainActivity;
import byc.avt.avanteelender.view.auth.RegistrationFormActivity;
import byc.avt.avanteelender.view.sheet.ConfirmationSheetFragment;
import byc.avt.avanteelender.viewmodel.AuthenticationViewModel;

public class OTPDocActivity extends AppCompatActivity {

    private TextView tvCountdown, tvSendTo;
    public static Button btnVerify;
    private EditText edtOTPCode;
    public static final String NEW_USER = "new_user";
    private User user;
    private OtpView otpView;
    private CountDownTimer timer;
    private AuthenticationViewModel viewModel;
    private Dialog dialog;
    private PrefManager prefManager;
    Toolbar bar;
    Fungsi f = new Fungsi(OTPDocActivity.this);

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_doc);
        bar = findViewById(R.id.toolbar_otp_doc);
        setSupportActionBar(bar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_back_24px);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvSendTo = findViewById(R.id.lbl_desc_sent_to_otp_doc);
        tvCountdown = findViewById(R.id.txt_countdown_otp_doc);
        otpView = findViewById(R.id.edit_code_otp_doc);
        viewModel = new ViewModelProvider(this).get(AuthenticationViewModel.class);
        dialog = GlobalVariables.loadingDialog(OTPDocActivity.this);
        prefManager = PrefManager.getInstance(OTPDocActivity.this);
        btnVerify = findViewById(R.id.btn_verify_otp_doc);

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyOTP();
            }
        });

    }

    public void verifyOTP() {
        // POST to server through endpoint
        dialog.show();
        viewModel.verifyOTPDoc(prefManager.getUid(), prefManager.getToken(), otpView.getText().toString());
        viewModel.getVerifyOTPDocResult().observe(OTPDocActivity.this, verifyOTPDocSuccess);
    }

    private Observer<String> verifyOTPDocSuccess = new Observer<String>() {
        @Override
        public void onChanged(String result) {
            String cek = result.split(": ")[0];
            String msg = result.split(": ")[1];
            if(cek.equalsIgnoreCase("success")){
                f.showMessage(msg);
                new ConfirmationSheetFragment(R.raw.registration_done_once, getString(R.string.register_complete), getString(R.string.registed_complete_message));
                ConfirmationSheetFragment sheetFragment = ConfirmationSheetFragment.getInstance();
                sheetFragment.show(getSupportFragmentManager(), sheetFragment.getTag());
                confirmLogin();
            }else{
                f.showMessage(msg);
            }

        }
    };

    public void confirmLogin() {
        // POST to server through endpoint
        viewModel.login(prefManager.getEmail(), prefManager.getPassword());
        viewModel.getLoginResult().observe(OTPDocActivity.this, checkSuccessLogin);
    }

    private Observer<String> checkSuccessLogin = new Observer<String>() {
        @Override
        public void onChanged(String result) {
            if(result.equals("success")) {
                if(prefManager.getName().equalsIgnoreCase("null") || prefManager.getName() == null || prefManager.getName() == "null"){
                    ///isi di sini untuk memulai pendaftaran registrasi form
                    startActivity(new Intent(OTPDocActivity.this, RegistrationFormActivity.class));
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                    finish();
                }else{
                    startActivity(new Intent(OTPDocActivity.this, MainActivity.class));
                    f.showMessage("Selamat datang "+prefManager.getName()+".");
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                    finish();
                }
            }else if(result.equals("failed")){
                f.showMessage("Email atau password tidak sesuai, silahkan coba lagi.");
            }else if(result.equals("failed2")){
                f.showMessage("Login gagal, silahkan coba lagi");
            }else if(result.equals("not_verified")){
                startActivity(new Intent(OTPDocActivity.this, OTPDocActivity.class));
                overridePendingTransition(R.anim.enter, R.anim.exit);
                finish();
            }
            dialog.cancel();
        }
    };

    public void sendOTPVerification() {
        // POST to server through endpoint
        dialog.show();
        viewModel.sendOTPVerification(prefManager.getUid(), prefManager.getToken());
        viewModel.getOTPVerificationResult().observe(OTPDocActivity.this, sendSuccess);
    }

    private Observer<String> sendSuccess = new Observer<String>() {
        @Override
        public void onChanged(String result) {
            f.showMessage(result);
            dialog.cancel();
            new OTPReceiver().setEditText(otpView);
            setTimer();
        }
    };


    /* todo send new otp code */
    private View.OnClickListener resendListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            sendOTPVerification();
            btnVerify.setEnabled(false);
            OTPDocActivity.this.setTimer();
        }
    };

    private void setTimer() {
        timer = new CountDownTimer(30000, 1000) {
            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            @Override
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                tvCountdown.setEnabled(false);
                tvCountdown.setText(getString(R.string.resend_in)+ " " + String.format("%02d:%02d", seconds / 60, seconds % 60));
            }

            @Override
            public void onFinish() {
                tvCountdown.setEnabled(true);
                tvCountdown.setText(getString(R.string.resend));
                tvCountdown.setOnClickListener(resendListener);
            }
        }.start();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

    }

    public void logout() {
        // LOGOUT: GET method to server through endpoint
        dialog.show();
        viewModel.logout(prefManager.getUid(), prefManager.getToken());
        viewModel.getLogoutResult().observe(OTPDocActivity.this, checkLogout);
    }

    private Observer<String> checkLogout = new Observer<String>() {
        @Override
        public void onChanged(String result) {
            if(result.equals("ok")) {
                dialog.cancel();
                Intent intent = new Intent(OTPDocActivity.this, WalkthroughActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                new Fungsi(OTPDocActivity.this).showMessage("Anda keluar dari verifikasi dokumen akun.");
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                finish();
            }else{
                dialog.cancel();
                new Fungsi().showMessage(result);
            }
        }
    };

}