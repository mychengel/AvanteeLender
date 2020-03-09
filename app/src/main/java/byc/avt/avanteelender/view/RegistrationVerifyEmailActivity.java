package byc.avt.avanteelender.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.view.sheet.ConfirmationSheetFragment;

public class RegistrationVerifyEmailActivity extends AppCompatActivity {

    Toolbar bar;
    Button btn_resent, btn_next;
    TextView lbl_des;
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
        btn_resent = findViewById(R.id.btn_kirim_ulang_reg_verify_email);
        btn_next = findViewById(R.id.btn_next_reg_verify_email);
        lbl_des = findViewById(R.id.lbl_des_reg_verify_email);
        lbl_des.setText(getString(R.string.des_email_terkirim1)+" "+email+" "+getString(R.string.des_email_terkirim2));

        btn_resent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ConfirmationSheetFragment(R.raw.email_sent_once, getString(R.string.email_terkirim), getString(R.string.des_email_resent1)+" "+email+getString(R.string.des_email_resent2));
                ConfirmationSheetFragment resentEmailFragment = ConfirmationSheetFragment.getInstance();
                resentEmailFragment.show(getSupportFragmentManager(), resentEmailFragment.getTag());
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ConfirmationSheetFragment(R.raw.verify_email_loop, getString(R.string.email_not_verified), getString(R.string.des_email_not_verified));
                ConfirmationSheetFragment resentEmailFragment = ConfirmationSheetFragment.getInstance();
                resentEmailFragment.show(getSupportFragmentManager(), resentEmailFragment.getTag());
            }
        });
    }

    // button back diklik
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
        email = "";
        finish();
    }
}
