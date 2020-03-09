package byc.avt.avanteelender.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.view.sheet.ConfirmationSheetFragment;

public class RegistrationVerifyEmailActivity extends AppCompatActivity {

    Toolbar bar;
    Button btn_resent, btn_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_verify_email);
        bar = findViewById(R.id.tb_reg_verify_email);
        bar.setTitle("");
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        btn_resent = findViewById(R.id.btn_kirim_ulang_reg_verify_email);
        btn_next = findViewById(R.id.btn_next_reg_verify_email);

        btn_resent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmationSheetFragment resentEmailFragment = ConfirmationSheetFragment.getInstance();
                resentEmailFragment.show(getSupportFragmentManager(), resentEmailFragment.getTag());
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    // button back diklik
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
