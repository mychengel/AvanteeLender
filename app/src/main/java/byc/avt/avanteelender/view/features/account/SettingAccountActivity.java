package byc.avt.avanteelender.view.features.account;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.view.others.RiskInfoActivity;
import byc.avt.avanteelender.view.others.SettingActivity;
import byc.avt.avanteelender.viewmodel.AuthenticationViewModel;

public class SettingAccountActivity extends AppCompatActivity {

    Fungsi f = new Fungsi(SettingAccountActivity.this);
    private AuthenticationViewModel viewModel;
    private PrefManager prefManager;
    private Dialog dialog;
    private Toolbar toolbar;
    GlobalVariables gv;
    JSONObject jobDataPribadi;
    JSONObject jobDataPekerjaan;
    JSONObject jobDataAlamatKTP;
    JSONObject jobDataAlamatDomisili;
    JSONObject jobDataRekBank;
    JSONObject jobDataDokumenPendukung;

    private TextView txt_inisial, txt_name, txt_code, txt_email;
    private CardView cv_change_password, cv_personal_data, cv_job_info, cv_address_data, cv_bank_info, cv_essential_doc;
    private ImageView img_pp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_account);
        viewModel = new ViewModelProvider(SettingAccountActivity.this).get(AuthenticationViewModel.class);
        prefManager = PrefManager.getInstance(SettingAccountActivity.this);
        toolbar = findViewById(R.id.toolbar_setting_account);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_back_24px);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dialog = GlobalVariables.loadingDialog(SettingAccountActivity.this);

        txt_name = findViewById(R.id.txt_lender_name_setting_account);
        txt_inisial = findViewById(R.id.txt_inisial_setting_account);
        txt_code = findViewById(R.id.txt_lender_code_setting_account);
        txt_email = findViewById(R.id.txt_lender_email_setting_account);
        cv_change_password = findViewById(R.id.cv_change_password_setting_account);
        cv_personal_data = findViewById(R.id.cv_data_pribadi_setting_account);
        cv_job_info = findViewById(R.id.cv_data_pekerjaan_setting_account);
        cv_address_data = findViewById(R.id.cv_data_alamat_setting_account);
        cv_bank_info = findViewById(R.id.cv_data_bank_setting_account);
        cv_essential_doc = findViewById(R.id.cv_data_dokumen_pendukung_setting_account);
        img_pp = findViewById(R.id.img_pp_setting_account);

        txt_name.setText(prefManager.getName());
        txt_inisial.setText(prefManager.getName().substring(0,1));
        txt_code.setText(gv.LENDER_CODE);
        txt_email.setText(prefManager.getEmail());

        img_pp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(f.clickAnim());
            }
        });

        cv_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        cv_personal_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingAccountActivity.this, PersonalDataShowActivity.class);
                intent.putExtra("jobPersonalData", jobDataPribadi.toString());
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        cv_job_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingAccountActivity.this, WorkInfoShowActivity.class);
                intent.putExtra("jobWorkInfo", jobDataPekerjaan.toString());
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        cv_address_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingAccountActivity.this, AddressDataShowActivity.class);
                intent.putExtra("jobAddressDataKTP", jobDataAlamatKTP.toString());
                intent.putExtra("jobAddressDataDomicile", jobDataAlamatDomisili.toString());
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        cv_bank_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingAccountActivity.this, BankInfoShowActivity.class);
                intent.putExtra("jobBankInfo", jobDataRekBank.toString());
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        cv_essential_doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingAccountActivity.this, DataPendukungShowActivity.class);
                intent.putExtra("jobEssDocument", jobDataDokumenPendukung.toString());
                intent.putExtra("jobDataPribadi", jobDataPribadi.toString());
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        loadData();
    }

    private void loadData() {
        dialog.show();
        viewModel.getAccountData(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultAccountData().observe(SettingAccountActivity.this, showAccountData);
    }

    private Observer<JSONObject> showAccountData = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    setEnabled();
                    JSONObject job = result.getJSONObject("result");
                    jobDataPribadi = job.getJSONObject("data_pribadi");
                    jobDataPekerjaan = job.getJSONObject("data_pekerjaan");
                    jobDataAlamatKTP = job.getJSONObject("alamat_ktp");
                    jobDataAlamatDomisili = job.getJSONObject("alamat_tempat_tinggal");
                    jobDataRekBank = job.getJSONObject("informasi_rekening");
                    jobDataDokumenPendukung = job.getJSONObject("informasi_dokumen");
                    Glide.with(SettingAccountActivity.this).load(prefManager.getAvatar())
                            .into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Drawable> transition) {
                                    Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();
                                    //Bitmap emptyBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
                                    if (resource.getConstantState() == null) {
                                        img_pp.setImageResource(R.drawable.ic_iconuser);
                                    }else{
                                        Drawable newdrawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(f.getCroppedBitmap(bitmap), 136, 136, true));
                                        img_pp.setImageDrawable(newdrawable);
                                    }
                                }
                            });
                    Log.e("jobDataPribadi", jobDataPribadi.toString());
                }else{
                    f.showMessage(getString(R.string.failed_load_info));
                }
                dialog.cancel();
            } catch (JSONException e) {
                e.printStackTrace();
                setDisabled();
                dialog.cancel();
                f.showMessage(getString(R.string.failed_exc_data));
            }
        }
    };

    private void setEnabled(){
        cv_personal_data.setEnabled(true);
        cv_job_info.setEnabled(true);
        cv_address_data.setEnabled(true);
        cv_bank_info.setEnabled(true);
        cv_essential_doc.setEnabled(true);
        cv_change_password.setEnabled(true);
        img_pp.setEnabled(true);
    }

    private void setDisabled(){
        cv_personal_data.setEnabled(false);
        cv_job_info.setEnabled(false);
        cv_address_data.setEnabled(false);
        cv_bank_info.setEnabled(false);
        cv_essential_doc.setEnabled(false);
        cv_change_password.setEnabled(false);
        img_pp.setEnabled(false);
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