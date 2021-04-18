package byc.avt.avanteelender.view.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.viewmodel.AuthenticationViewModel;

public class SignersCheckActivity extends AppCompatActivity {

    Fungsi f = new Fungsi(SignersCheckActivity.this);
    private AuthenticationViewModel viewModel;
    private PrefManager prefManager;
    GlobalVariables gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signers_check);
        prefManager = PrefManager.getInstance(SignersCheckActivity.this);
        viewModel = new ViewModelProvider(this).get(AuthenticationViewModel.class);

        checkSigners();
    }

    public void checkSigners() {
        // POST to server through endpoint
        viewModel.checkSuratKuasa(prefManager.getUid(), prefManager.getToken());
        viewModel.getCheckSuratKuasaResult().observe(SignersCheckActivity.this, showResult);
    }

    private Observer<String> showResult = new Observer<String>() {
        @Override
        public void onChanged(String result) {

        }
    };

}