package byc.avt.avanteelender.view.misc;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.mukesh.OtpView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.helper.Routes;
import byc.avt.avanteelender.intro.WalkthroughActivity;
import byc.avt.avanteelender.model.User;
import byc.avt.avanteelender.model.UserData;
import byc.avt.avanteelender.view.auth.RegistrationFormActivity;
import byc.avt.avanteelender.viewmodel.AuthenticationViewModel;

public class OTPActivity extends AppCompatActivity {

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
    Fungsi f = new Fungsi(OTPActivity.this);

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        bar = findViewById(R.id.toolbar_otp);
        setSupportActionBar(bar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_back_24px);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvSendTo = findViewById(R.id.lbl_desc_sent_to_otp);
        tvCountdown = findViewById(R.id.txt_countdown_otp);
        otpView = findViewById(R.id.edit_code_otp);
        viewModel = new ViewModelProvider(this).get(AuthenticationViewModel.class);
        dialog = GlobalVariables.loadingDialog(OTPActivity.this);
        prefManager = PrefManager.getInstance(OTPActivity.this);

        btnVerify = findViewById(R.id.btn_verify_otp);
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(otpView.getText().toString().isEmpty()){
                    f.showMessage(getString(R.string.otp_cannot_null));
                }else if(otpView.getText().toString().length() < 6){
                    f.showMessage(getString(R.string.otp_min_six_char));
                }else{
                    verifyOTP();
                }

            }
        });

        checkPermission();
        sendOTPVerification();

        if (getIntent().getParcelableExtra(NEW_USER) != null) {
            user = getIntent().getParcelableExtra(NEW_USER);
            tvSendTo.setText(getString(R.string.otp_desc) + user.getNo_handphone());
        }
    }

    private static String[] PERMISSIONS_SMS = {
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_PHONE_STATE,
    };

    private void checkPermission(){
        final int permission = ActivityCompat.checkSelfPermission(OTPActivity.this, Manifest.permission.READ_SMS);
        final int permission2 = ActivityCompat.checkSelfPermission(OTPActivity.this, Manifest.permission.READ_PHONE_STATE);
        if (permission != PackageManager.PERMISSION_GRANTED ||permission2 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(OTPActivity.this, PERMISSIONS_SMS, 1);
        }else{
        }
    }

    public void verifyOTP() {
        // POST to server through endpoint
        dialog.show();
        viewModel.verifyOTP(prefManager.getUid(), prefManager.getToken(), otpView.getText().toString());
        viewModel.getVerifyOTPResult().observe(OTPActivity.this, verifyOTPSuccess);
    }

    private Observer<String> verifyOTPSuccess = new Observer<String>() {
        @Override
        public void onChanged(String result) {
            String cek = result.split(": ")[0];
            String msg = result.split(": ")[1];
            if(cek.equalsIgnoreCase("success")){
                dialog.cancel();
                f.showMessage(msg);
                confirmLogin();
            }else{
                f.showMessage(msg);
                dialog.cancel();
            }

        }
    };


    public void confirmLogin() {
        // POST to server through endpoint
        viewModel.login(prefManager.getEmail(), prefManager.getPassword());
        viewModel.getLoginResult().observe(OTPActivity.this, checkSuccessLogin);
    }


    private Observer<JSONObject> checkSuccessLogin = new Observer<JSONObject>() {
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
                    UserData ud = new UserData(prefManager.getEmail(),prefManager.getPassword(),uid,res.getInt("type"),res.getString("client_type"),res.getString("avatar"),res.getString("name"),verif,token,0);
                    prefManager.setUserData(ud);
                    if(verif == 1){
                        if(res.isNull("doc") && res.isNull("swafoto") && res.isNull("docfile")){
                            i = new Intent(OTPActivity.this, RegistrationFormActivity.class);
                        }else{
                            i = new Intent(OTPActivity.this, RegistrationFormActivity.class);
                        }
                    }else{
                        i = new Intent(OTPActivity.this, OTPActivity.class);
                    }
                    //Routing
                    new Routes(OTPActivity.this).moveInFinish(i);
                    dialog.cancel();
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


    public void sendOTPVerification() {
        // POST to server through endpoint
        dialog.show();
        viewModel.sendOTPVerification(prefManager.getUid(), prefManager.getToken(), "verification");
        viewModel.getOTPVerificationResult().observe(OTPActivity.this, sendSuccess);
    }

    private Observer<String> sendSuccess = new Observer<String>() {
        @Override
        public void onChanged(String result) {
            f.showMessage(result);
            dialog.cancel();
            setTimer();
        }
    };


    /* todo send new otp code */
    private View.OnClickListener resendListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            sendOTPVerification();
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
                new Fungsi(OTPActivity.this).showMessage("Anda keluar dari verifikasi akun.");
                Intent intent = new Intent(OTPActivity.this, WalkthroughActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                new Routes(OTPActivity.this).moveOutIntent(intent);
            }else{
                dialog.cancel();
                new Fungsi().showMessage(result);
            }
        }
    };
}
