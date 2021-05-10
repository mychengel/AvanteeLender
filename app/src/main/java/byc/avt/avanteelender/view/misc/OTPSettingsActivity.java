package byc.avt.avanteelender.view.misc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.airbnb.lottie.LottieDrawable;
import com.mukesh.OtpView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.helper.Routes;
import byc.avt.avanteelender.helper.receiver.OTPReceiver;
import byc.avt.avanteelender.intro.WalkthroughActivity;
import byc.avt.avanteelender.model.User;
import byc.avt.avanteelender.model.UserData;
import byc.avt.avanteelender.view.MainActivity;
import byc.avt.avanteelender.view.auth.InVerificationProcessActivity;
import byc.avt.avanteelender.view.auth.RegistrationFormActivity;
import byc.avt.avanteelender.view.auth.SignersCheckActivity;
import byc.avt.avanteelender.view.sheet.ConfirmationSheetFragment;
import byc.avt.avanteelender.viewmodel.AuthenticationViewModel;

public class OTPSettingsActivity extends AppCompatActivity {

    private TextView tvCountdown, tvSendTo;
    public static Button btnVerify;
    private EditText edtOTPCode;
    private User user;
    private OtpView otpView;
    private CountDownTimer timer;
    private AuthenticationViewModel viewModel;
    private Dialog dialog;
    private PrefManager prefManager;
    Toolbar bar;
    Fungsi f = new Fungsi(OTPSettingsActivity.this);
    String type = "", title_msg = "", desc_msg = "";
    int lottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_settings);
        bar = findViewById(R.id.toolbar_otp_settings);
        setSupportActionBar(bar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_back_24px);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvSendTo = findViewById(R.id.lbl_desc_sent_to_otp_settings);
        tvCountdown = findViewById(R.id.txt_countdown_otp_settings);
        otpView = findViewById(R.id.edit_code_otp_settings);
        viewModel = new ViewModelProvider(this).get(AuthenticationViewModel.class);
        dialog = GlobalVariables.loadingDialog(OTPSettingsActivity.this);
        prefManager = PrefManager.getInstance(OTPSettingsActivity.this);
        btnVerify = findViewById(R.id.btn_verify_otp_settings);
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyOTP();
            }
        });

        Intent i = getIntent();
        type = i.getStringExtra("type");
        if(type.equalsIgnoreCase("profile")){
            lottie = R.raw.account_done_once;
            title_msg = getString(R.string.update_profile_done);
            desc_msg = "";
        }else if(type.equalsIgnoreCase("password")){
            lottie = R.raw.new_password_done_once;
            title_msg = getString(R.string.update_pass_done);
            desc_msg = getString(R.string.lets_login_again);
        }

        checkPermission();

        OTPReceiver.isReady = true;
        new OTPReceiver().setEditText(otpView, "settings");
        setTimer();
    }

    private static String[] PERMISSIONS_SMS = {
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_PHONE_STATE,
    };

    private void checkPermission(){
        final int permission = ActivityCompat.checkSelfPermission(OTPSettingsActivity.this, Manifest.permission.READ_SMS);
        final int permission2 = ActivityCompat.checkSelfPermission(OTPSettingsActivity.this, Manifest.permission.READ_PHONE_STATE);
        if (permission != PackageManager.PERMISSION_GRANTED ||permission2 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(OTPSettingsActivity.this, PERMISSIONS_SMS, 1);
        }else{
        }
    }

    public void verifyOTP() {
        // POST to server through endpoint
        dialog.show();
        viewModel.verifyOTPSettings(prefManager.getUid(), prefManager.getToken(), otpView.getText().toString(), type);
        viewModel.getVerifyOTPSettingsResult().observe(OTPSettingsActivity.this, verifyOTPSuccess);
    }

    private Observer<String> verifyOTPSuccess = new Observer<String>() {
        @Override
        public void onChanged(String result) {
            String cek = result.split(": ")[0];
            String msg = result.split(": ")[1];
            if(cek.equalsIgnoreCase("success")){
                dialog.cancel();
                new ConfirmationSheetFragment(lottie, title_msg, msg+". "+desc_msg);
                ConfirmationSheetFragment sheetFragment = ConfirmationSheetFragment.getInstance();
                sheetFragment.show(getSupportFragmentManager(), sheetFragment.getTag());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(type.equalsIgnoreCase("profile")){
                            //new Routes(OTPSettingsActivity.this).moveOut();
                            confirmLogin();
                        }else if(type.equalsIgnoreCase("password")){
                            logout();
                        }
                    }
                }, 3000);
            }else{
                f.showMessage(msg);
                dialog.cancel();
            }

        }
    };

    public void resendOTPVerification() {
        // POST to server through endpoint
        dialog.show();
        viewModel.sendOTPVerification(prefManager.getUid(), prefManager.getToken(), type);
        viewModel.getOTPVerificationResult().observe(OTPSettingsActivity.this, sendSuccess);
    }

    private Observer<String> sendSuccess = new Observer<String>() {
        @Override
        public void onChanged(String result) {
            f.showMessage(result);
            dialog.cancel();
            OTPReceiver.isReady = true;
            new OTPReceiver().setEditText(otpView, "settings");
            setTimer();
        }
    };


    /* todo send new otp code */
    private View.OnClickListener resendListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            resendOTPVerification();
            //btnVerify.setEnabled(false);
            OTPSettingsActivity.this.setTimer();
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

    public void logout() {
        // LOGOUT: GET method to server through endpoint
        dialog.show();
        viewModel.logout(prefManager.getUid(), prefManager.getToken());
        viewModel.getLogoutResult().observe(OTPSettingsActivity.this, checkLogout);
    }

    private Observer<String> checkLogout = new Observer<String>() {
        @Override
        public void onChanged(String result) {
            if(result.equals("ok")) {
                dialog.cancel();
                Intent intent = new Intent(OTPSettingsActivity.this, WalkthroughActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                new Routes(OTPSettingsActivity.this).moveOutIntent(intent);
            }else{
                dialog.cancel();
                new Fungsi(OTPSettingsActivity.this).showMessage(result);
            }
        }
    };


    ///////////
    public void confirmLogin() {
        // POST to server through endpoint
        dialog.show();
        viewModel.login(prefManager.getEmail(), prefManager.getPassword());
        viewModel.getLoginResult().observe(OTPSettingsActivity.this, checkSuccess);
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
                    UserData ud = new UserData(prefManager.getEmail(),prefManager.getPassword(),uid,res.getInt("type"),res.getString("client_type"),res.getString("avatar"),res.getString("name"),verif,token,0);
                    prefManager.setUserData(ud);
                    if(verif == 1){
                        if(res.isNull("doc") && res.isNull("swafoto") && res.isNull("docfile")){
                            Log.e("Doc", "Aman");
                            if(res.isNull("doc_otp")){
                                if(res.isNull("privy_status")){
                                    Log.e("PrivyStatus", "Aman");
                                    if(res.isNull("suratkuasa")){
                                        Log.e("TTDSuratKuasa", "Aman");
                                        if(res.isNull("suratperjanjian")){
                                            // Masuk DASHBOARD
                                            Log.e("TTDSuratPK", "Aman");
                                            i = new Intent(OTPSettingsActivity.this, MainActivity.class);
                                            i.putExtra("dest", "1");
                                        }else{
                                            msg = res.getJSONObject("suratperjanjian").getString("msg");
                                            f.showMessage(msg);
                                            i = new Intent(OTPSettingsActivity.this, SignersCheckActivity.class);
                                            i.putExtra("doc_type", "Surat Perjanjian");
                                            //diarahkan untuk ttd surat perjanjian kerja sama
                                        }
                                    }else{
                                        msg = res.getJSONObject("suratkuasa").getString("msg");
                                        f.showMessage(msg);
                                        i = new Intent(OTPSettingsActivity.this, SignersCheckActivity.class);
                                        i.putExtra("doc_type", "Surat Kuasa");
                                        //diarahkan untuk ttd surat kuasa
                                    }
                                }else{
                                    i = new Intent(OTPSettingsActivity.this, InVerificationProcessActivity.class);
                                }
                            }else{
                                i = new Intent(OTPSettingsActivity.this, OTPDocActivity.class);
                                i.putExtra("from", "doc");
                            }
                        }else{
                            i = new Intent(OTPSettingsActivity.this, RegistrationFormActivity.class);
                        }
                    }else{
                        i = new Intent(OTPSettingsActivity.this, OTPActivity.class);
                    }

                    //Routing
                    new Routes(OTPSettingsActivity.this).moveInFinish(i);
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            new Routes(OTPSettingsActivity.this).moveOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

    }
}