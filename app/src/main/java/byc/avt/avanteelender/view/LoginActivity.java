package byc.avt.avanteelender.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    Fungsi f = new Fungsi(LoginActivity.this);
    Toolbar bar;
    private TextInputLayout editPassword, editEmail;
    private String email, password;
    private Button btnLogin;
    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masuk);
        bar = findViewById(R.id.toolbar_masuk);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        editEmail = findViewById(R.id.edit_email_masuk);
        editPassword = findViewById(R.id.edit_password_masuk);
        btnLogin = findViewById(R.id.btn_masuk);
        viewModel = ViewModelProviders.of(LoginActivity.this).get(LoginViewModel.class);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationFormActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
