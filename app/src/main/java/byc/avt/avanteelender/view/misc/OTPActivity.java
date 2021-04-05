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
import android.widget.Toast;

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
import byc.avt.avanteelender.view.auth.LoginActivity;
import byc.avt.avanteelender.view.others.SettingActivity;
import byc.avt.avanteelender.viewmodel.AuthenticationViewModel;

public class OTPActivity extends AppCompatActivity {

    private TextView tvCountdown;
    public static Button btnVerify;
    private EditText edtOTPCode;
    public static final String NEW_USER = "new_user";
    private User user;
    private OtpView otpView;
    private CountDownTimer timer;
    private AuthenticationViewModel viewModel;
    private Dialog dialog;
    private PrefManager prefManager;
    Fungsi f = new Fungsi(OTPActivity.this);

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        Toolbar bar = findViewById(R.id.otp_toolbar);
        setSupportActionBar(bar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_back_24px);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView tvSendTo = findViewById(R.id.tv_otp_sent_to);
        tvCountdown = findViewById(R.id.tv_countdown);
        otpView = findViewById(R.id.edt_otp_code);
        viewModel = new ViewModelProvider(this).get(AuthenticationViewModel.class);
        dialog = GlobalVariables.loadingDialog(OTPActivity.this);
        prefManager = PrefManager.getInstance(OTPActivity.this);
        btnVerify = findViewById(R.id.btn_verify_otp);

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        sendOTPVerification();

        if (getIntent().getParcelableExtra(NEW_USER) != null) {
            user = getIntent().getParcelableExtra(NEW_USER);
            tvSendTo.setText(getString(R.string.otp_desc) + user.getNo_handphone());
        }

    }


    public void sendOTPVerification() {
        // POST to server through endpoint
        dialog.show();
        viewModel.sendOTPVerification(prefManager.getUid(), prefManager.getToken());
        viewModel.getOTPVerificationResult().observe(OTPActivity.this, sendSuccess);
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
            OTPActivity.this.setTimer();
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
        viewModel.getLogoutResult().observe(OTPActivity.this, checkLogout);
    }

    private Observer<String> checkLogout = new Observer<String>() {
        @Override
        public void onChanged(String result) {
            if(result.equals("ok")) {
                dialog.cancel();
                Intent intent = new Intent(OTPActivity.this, WalkthroughActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                new Fungsi(OTPActivity.this).showMessage("Anda keluar dari verifikasi akun.");
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                finish();
            }else{
                dialog.cancel();
                new Fungsi().showMessage(result);
            }
        }
    };
}
