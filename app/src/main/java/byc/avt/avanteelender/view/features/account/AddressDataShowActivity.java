package byc.avt.avanteelender.view.features.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import byc.avt.avanteelender.R;

public class AddressDataShowActivity extends AppCompatActivity {

    private Toolbar toolbar;
    JSONObject job1, job2;

    TextInputLayout txtKtpAddress, txtKtpCountry, txtKtpProvince, txtKtpCity,
            txtKtpDistrict, txtKtpUrban, txtKtpRT, txtKtpRW, txtKtpPostalCode;
    TextInputLayout txtDomicileAddress, txtDomicileCountry, txtDomicileProvince, txtDomicileCity,
            txtDomicileDistrict, txtDomicileUrban, txtDomicileRT, txtDomicileRW, txtDomicilePostalCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_data_show);
        toolbar = findViewById(R.id.toolbar_address_data_show);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_back_24px);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtKtpAddress = findViewById(R.id.edit_ktp_address_address_data_show);
        txtKtpCountry = findViewById(R.id.edit_ktp_country_address_data_show);
        txtKtpProvince = findViewById(R.id.edit_ktp_province_address_data_show);
        txtKtpCity = findViewById(R.id.edit_ktp_city_address_data_show);
        txtKtpDistrict = findViewById(R.id.edit_ktp_kecamatan_address_data_show);
        txtKtpUrban = findViewById(R.id.edit_ktp_kelurahan_address_data_show);
        txtKtpRT = findViewById(R.id.edit_ktp_rt_address_data_show);
        txtKtpRW = findViewById(R.id.edit_ktp_rw_address_data_show);
        txtKtpPostalCode = findViewById(R.id.edit_ktp_kodepos_address_data_show);
        txtDomicileAddress = findViewById(R.id.edit_domicile_address_address_data_show);
        txtDomicileCountry = findViewById(R.id.edit_domicile_country_address_data_show);
        txtDomicileProvince = findViewById(R.id.edit_domicile_province_address_data_show);
        txtDomicileCity = findViewById(R.id.edit_domicile_city_address_data_show);
        txtDomicileDistrict = findViewById(R.id.edit_domicile_kecamatan_address_data_show);
        txtDomicileUrban = findViewById(R.id.edit_domicile_kelurahan_address_data_show);
        txtDomicileRT = findViewById(R.id.edit_domicile_rt_address_data_show);
        txtDomicileRW = findViewById(R.id.edit_domicile_rw_address_data_show);
        txtDomicilePostalCode = findViewById(R.id.edit_domicile_kodepos_address_data_show);

        Intent i = getIntent();
        try {
            job1 = new JSONObject(i.getStringExtra("jobAddressDataKTP"));
            txtKtpAddress.getEditText().setText(job1.getString("identity_address"));
            txtKtpCountry.getEditText().setText(job1.getString("identity_country"));
            txtKtpProvince.getEditText().setText(job1.getString("identity_province"));
            txtKtpCity.getEditText().setText(job1.getString("identity_city"));
            txtKtpDistrict.getEditText().setText(job1.getString("identity_district"));
            txtKtpUrban.getEditText().setText(job1.getString("identity_village"));
            //txtKtpRT.getEditText().setText(job1.getString("identity_rt"));
            //txtKtpRW.getEditText().setText(job1.getString("identity_rw"));
            txtKtpPostalCode.getEditText().setText(job1.getString("identity_postal_code"));

            job2 = new JSONObject(i.getStringExtra("jobAddressDataDomicile"));
            txtDomicileAddress.getEditText().setText(job2.getString("clients_address"));
            txtDomicileCountry.getEditText().setText(job2.getString("clients_country"));
            txtDomicileProvince.getEditText().setText(job2.getString("clients_province"));
            txtDomicileCity.getEditText().setText(job2.getString("clients_city"));
            txtDomicileDistrict.getEditText().setText(job2.getString("clients_district"));
            txtDomicileUrban.getEditText().setText(job2.getString("clients_village"));
            //txtDomicileRT.getEditText().setText(job2.getString("clients_rt"));
            //txtDomicileRW.getEditText().setText(job2.getString("clients_rw"));
            txtDomicilePostalCode.getEditText().setText(job2.getString("clients_postal_code"));

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