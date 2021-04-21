package byc.avt.avanteelender.view.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.helper.Routes;
import byc.avt.avanteelender.intro.WalkthroughActivity;
import byc.avt.avanteelender.view.others.SettingActivity;
import byc.avt.avanteelender.viewmodel.AuthenticationViewModel;

public class InVerificationProcessActivity extends AppCompatActivity {

    private AuthenticationViewModel viewModel;
    private PrefManager prefManager;
    private Dialog dialog;
    Button btn_back;
    TextView txt_info;
    String msg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_verification_process);
        viewModel = new ViewModelProvider(InVerificationProcessActivity.this).get(AuthenticationViewModel.class);
        prefManager = PrefManager.getInstance(InVerificationProcessActivity.this);
        dialog = GlobalVariables.loadingDialog(InVerificationProcessActivity.this);
        txt_info = findViewById(R.id.txt_info_in_verification_process);

        Intent i = getIntent();
        msg = i.getStringExtra("info");
        if(msg.isEmpty()){
            txt_info.setText(getString(R.string.in_process_verification));
        }else{
            //txt_info.setText(msg);
            txt_info.setText(getString(R.string.in_process_verification));
        }

        btn_back = findViewById(R.id.btn_kembali_in_verification_process);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

    }

    public void logout() {
        // LOGOUT: GET method to server through endpoint
        dialog.show();
        viewModel.logout(prefManager.getUid(), prefManager.getToken());
        viewModel.getLogoutResult().observe(InVerificationProcessActivity.this, checkLogout);
    }

    private Observer<String> checkLogout = new Observer<String>() {
        @Override
        public void onChanged(String result) {
            if(result.equals("ok")) {
                dialog.cancel();
                Intent intent = new Intent(InVerificationProcessActivity.this, WalkthroughActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                new Routes(InVerificationProcessActivity.this).moveOutIntent(intent);
            }else{
                dialog.cancel();
                new Fungsi().showMessage(result);
            }
        }
    };

    @Override
    public void onBackPressed() {

    }
}