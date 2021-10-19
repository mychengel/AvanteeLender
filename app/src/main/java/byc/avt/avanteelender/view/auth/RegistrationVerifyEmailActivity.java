package byc.avt.avanteelender.view.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.Routes;
import byc.avt.avanteelender.intro.WalkthroughActivity;
import byc.avt.avanteelender.view.misc.OTPActivity;
import byc.avt.avanteelender.view.sheet.ConfirmationSheetFragment;
import byc.avt.avanteelender.viewmodel.AuthenticationViewModel;

public class RegistrationVerifyEmailActivity extends AppCompatActivity {

    Toolbar bar;
    Button btn_resent, btn_next;
    TextView lbl_des;
    private AuthenticationViewModel viewModel;
    private Dialog dialog;
    public static String email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_verify_email);
        bar = findViewById(R.id.tb_reg_verify_email);
        bar.setTitle("");
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        dialog = GlobalVariables.loadingDialog(RegistrationVerifyEmailActivity.this);
        viewModel = new ViewModelProvider(RegistrationVerifyEmailActivity.this).get(AuthenticationViewModel.class);
        btn_resent = findViewById(R.id.btn_kirim_ulang_reg_verify_email);
        btn_next = findViewById(R.id.btn_next_reg_verify_email);
        lbl_des = findViewById(R.id.lbl_des_reg_verify_email);
        lbl_des.setText(getString(R.string.des_email_terkirim1)+" "+email+" "+getString(R.string.des_email_terkirim2));
        btn_resent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendEmail();
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationVerifyEmailActivity.this, WalkthroughActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                new Routes(RegistrationVerifyEmailActivity.this).moveInFinish(intent);

            }
        });
    }

    public void resendEmail() {
        // POST to server through endpoint
        dialog.show();
        viewModel.setResendEmail(email);
        viewModel.getResultResendEmail().observe(RegistrationVerifyEmailActivity.this, showResult);
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
                    dialog.cancel();
                }else{
                    new Fungsi(RegistrationVerifyEmailActivity.this).showMessage(getString(R.string.resend_email_failed));
                    dialog.cancel();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                dialog.cancel();
            }
            dialog.cancel();
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            email = "";
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
    }
}
