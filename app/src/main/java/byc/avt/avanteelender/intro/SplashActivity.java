package byc.avt.avanteelender.intro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import java.util.Calendar;
import java.util.Date;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.view.LoginActivity;
import byc.avt.avanteelender.view.MainActivity;
import byc.avt.avanteelender.viewmodel.AuthenticationViewModel;
import byc.avt.avanteelender.viewmodel.SplashViewModel;

public class SplashActivity extends AppCompatActivity {

    private SplashViewModel viewModel;
    private PrefManager prefManager;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        prefManager = PrefManager.getInstance(SplashActivity.this);
        viewModel = new ViewModelProvider(SplashActivity.this).get(SplashViewModel.class);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkSession();
            }
        }, 2500);
    }

    public void checkSession() {
        // POST to server through endpoint
        if(prefManager.getUid().equalsIgnoreCase("-")){
            Intent onBoard = new Intent(SplashActivity.this, WalkthroughActivity.class);
            onBoard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(onBoard);
            finish();
        }else {
            Date currentTime = Calendar.getInstance().getTime();
            long mNow = currentTime.getTime();
            if(prefManager.getExpiredTime() >= mNow){
                viewModel.sessionCheck(prefManager.getUid(), prefManager.getToken());
                viewModel.getResult().observe(SplashActivity.this, checkSuccess);
            }else{
                prefManager.clearUserData();
                Intent onBoard = new Intent(SplashActivity.this, WalkthroughActivity.class);
                onBoard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(onBoard);
                finish();
            }
        }
    }

    private Observer<String> checkSuccess = new Observer<String>() {
        @Override
        public void onChanged(String result) {
            if(result.equals("ok")) {
                Intent onBoard = new Intent(SplashActivity.this, WalkthroughActivity.class);
                onBoard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(onBoard);
            }else{
                Intent onBoard = new Intent(SplashActivity.this, WalkthroughActivity.class);
                onBoard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(onBoard);
            }
            finish();
        }
    };
}
