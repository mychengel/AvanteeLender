package byc.avt.avanteelender.view.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.helper.Routes;
import byc.avt.avanteelender.intro.WalkthroughActivity;
import byc.avt.avanteelender.view.sheet.ConfirmationSheetFragment;
import byc.avt.avanteelender.viewmodel.AuthenticationViewModel;

public class EmailVerificationActivity extends AppCompatActivity {

    private PrefManager prefManager;
    private AuthenticationViewModel viewModel;
    private Dialog dialog;
    private Button btn_next;
    private TextView txt_info;
    private LottieAnimationView lottie;
    String authKey = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);
        txt_info = findViewById(R.id.txt_info_email_verif);
        lottie = findViewById(R.id.lottie_email_verif);
        btn_next = findViewById(R.id.btn_lanjutkan_email_verif);
        prefManager = PrefManager.getInstance(EmailVerificationActivity.this);
        dialog = GlobalVariables.loadingDialog(EmailVerificationActivity.this);
        viewModel = new ViewModelProvider(EmailVerificationActivity.this).get(AuthenticationViewModel.class);

        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();
        authKey = data.getLastPathSegment();
        Log.e("IntentURIParams", data.getLastPathSegment());

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmailVerificationActivity.this, WalkthroughActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                new Routes(EmailVerificationActivity.this).moveInFinish(intent);
            }
        });

        checkVerification();
    }

    private void checkVerification(){
        dialog.show();
        viewModel.emailVerif(authKey);
        viewModel.getResultEmailVerif().observe(EmailVerificationActivity.this, showResult);
    }

    private Observer<JSONObject> showResult = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            Log.e("ResultVerif", result.toString());
            try {
                if(result.getInt("code") == 200){
                    dialog.cancel();
                    txt_info.setText(getString(R.string.email_verified));
                    lottie.setAnimation(R.raw.account_done_once);
                }else{
                    dialog.cancel();
                    txt_info.setText(getString(R.string.verify_expired));
                    lottie.setAnimation(R.raw.doc_warning);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                dialog.cancel();
                txt_info.setText(getString(R.string.verify_expired));
                lottie.setAnimation(R.raw.doc_warning);
            }
            dialog.cancel();
        }
    };

    @Override
    public void onBackPressed() {

    }

}