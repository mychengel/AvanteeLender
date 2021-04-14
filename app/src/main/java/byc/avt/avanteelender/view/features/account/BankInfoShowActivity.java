package byc.avt.avanteelender.view.features.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;

public class BankInfoShowActivity extends AppCompatActivity {

    private Toolbar toolbar;
    JSONObject job;

    TextInputLayout txtBank, txtAccountName, txtAccountNumber, txtAvgTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_info_show);
        toolbar = findViewById(R.id.toolbar_bank_info_show);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_back_24px);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtBank = findViewById(R.id.edit_bank_name_fr_bank_info_show);
        txtAccountName = findViewById(R.id.edit_bank_owner_name_fr_bank_info_show);
        txtAccountNumber = findViewById(R.id.edit_bank_account_number_fr_bank_info_show);
        txtAvgTrans = findViewById(R.id.edit_avg_transaction_fr_bank_info_show);

        Intent i = getIntent();
        try {
            job = new JSONObject(i.getStringExtra("jobBankInfo"));
            txtBank.getEditText().setText(job.getString("bank_name"));
            txtAccountName.getEditText().setText(job.getString("bank_account"));
            txtAccountNumber.getEditText().setText(job.getString("bank_account_no"));
            txtAvgTrans.getEditText().setText(job.getString("average_transaction").replaceAll("[lgt;&]", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

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