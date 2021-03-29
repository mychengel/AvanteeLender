package byc.avt.avanteelender.view.others;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.intro.WalkthroughActivity;
import byc.avt.avanteelender.viewmodel.AuthenticationViewModel;

public class SettingActivity extends AppCompatActivity {

    CardView cvAccountSetting;
    CardView cvFAQ;
    CardView cvTerms;
    CardView cvRisk;
    CardView cvLogout;// logout button
    ImageView ivInitial;
    TextView initial;

    private AuthenticationViewModel viewModel;
    private PrefManager prefManager;
    private Dialog dialog;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        viewModel = new ViewModelProvider(SettingActivity.this).get(AuthenticationViewModel.class);
        prefManager = PrefManager.getInstance(SettingActivity.this);
        toolbar = findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_back_24px);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dialog = GlobalVariables.loadingDialog(SettingActivity.this);

        cvAccountSetting = findViewById(R.id.cv_account_setting);
        cvFAQ = findViewById(R.id.cv_faq);
        cvTerms = findViewById(R.id.cv_terms);
        cvRisk = findViewById(R.id.cv_risk_info);
        cvLogout = findViewById(R.id.cv_logout);
        ivInitial = findViewById(R.id.imageProfile);
        initial = findViewById(R.id.tv_initial);

        String letter = String.valueOf(prefManager.getName().charAt(0));
        initial.setText(letter);

        cvAccountSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(SettingActivity.this, RiskInfoActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        cvFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, FaqActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        cvTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, TermsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        cvRisk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, RiskInfoActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        cvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutConfirmation();
            }
        });
    }

    private void logoutConfirmation(){
        new AlertDialog.Builder(SettingActivity.this)
                .setTitle("Konfirmasi")
                .setIcon(R.drawable.logo)
                .setMessage("Apakah anda yakin mau keluar dari aplikasi Avantee?")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        logout();
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create()
                .show();
    }

    public void logout() {
        // LOGOUT: GET method to server through endpoint
        dialog.show();
        viewModel.logout(prefManager.getUid(), prefManager.getToken());
        viewModel.getLogoutResult().observe(SettingActivity.this, checkLogout);
    }

    private Observer<String> checkLogout = new Observer<String>() {
        @Override
        public void onChanged(String result) {
            if(result.equals("ok")) {
                dialog.cancel();
                Intent intent = new Intent(SettingActivity.this, WalkthroughActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                new Fungsi(SettingActivity.this).showMessage("Berhasil keluar aplikasi.");
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                finish();
            }else{
                dialog.cancel();
                new Fungsi().showMessage(result);
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
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