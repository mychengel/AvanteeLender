package byc.avt.avanteelender.view.features.account.institution;

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
import android.view.WindowManager;
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
import byc.avt.avanteelender.helper.Routes;
import byc.avt.avanteelender.view.features.account.individual.ChangePasswordActivity;
import byc.avt.avanteelender.view.features.account.individual.SettingAccountActivity;
import byc.avt.avanteelender.view.features.account.individual.UpdateAvaActivity;
import byc.avt.avanteelender.viewmodel.AuthenticationViewModel;

public class SettingInsAccountActivity extends AppCompatActivity {

    Fungsi f = new Fungsi(SettingInsAccountActivity.this);
    private AuthenticationViewModel viewModel;
    private PrefManager prefManager;
    private Dialog dialog;
    private Toolbar toolbar;
    GlobalVariables gv;
    JSONObject jobDataNarahubung;
    JSONObject jobDataPerusahaan;
    JSONObject jobDataAlamatAkta;
    JSONObject jobDataAlamatOperasional;
    JSONObject jobDataRekBank;
    JSONObject jobDataDokumenPendukung;

    private TextView txt_inisial, txt_name, txt_code, txt_email;
    private CardView cv_change_password, cv_narahubung_data, cv_company_info, cv_address_data, cv_bank_info, cv_essential_doc;
    private ImageView img_pp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_ins_account);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        viewModel = new ViewModelProvider(SettingInsAccountActivity.this).get(AuthenticationViewModel.class);
        prefManager = PrefManager.getInstance(SettingInsAccountActivity.this);
        toolbar = findViewById(R.id.toolbar_ins_account);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_back_24px);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dialog = GlobalVariables.loadingDialog(SettingInsAccountActivity.this);

        txt_name = findViewById(R.id.txt_lender_name_ins_account);
        txt_inisial = findViewById(R.id.txt_inisial_ins_account);
        txt_code = findViewById(R.id.txt_lender_code_ins_account);
        txt_email = findViewById(R.id.txt_lender_email_ins_account);
        cv_change_password = findViewById(R.id.cv_change_password_ins_account);
        cv_narahubung_data = findViewById(R.id.cv_data_narahubung_ins_account);
        cv_company_info = findViewById(R.id.cv_data_perusahaan_ins_account);
        cv_address_data = findViewById(R.id.cv_data_alamat_ins_account);
        cv_bank_info = findViewById(R.id.cv_data_bank_ins_account);
        cv_essential_doc = findViewById(R.id.cv_data_dokumen_pendukung_ins_account);
        img_pp = findViewById(R.id.img_pp_ins_account);

        Glide.with(SettingInsAccountActivity.this).load(prefManager.getAvatar())
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Drawable> transition) {
                        final Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();
                        if (resource.getConstantState() == null) {
                            img_pp.setImageResource(R.drawable.ic_iconuser);
                            txt_inisial.setVisibility(View.VISIBLE);
                        }else{
                            //Drawable newdrawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(f.getCroppedBitmap(bitmap), 136, 136, true));
                            Drawable newdrawable = new BitmapDrawable(getResources(), bitmap);
                            img_pp.setImageDrawable(newdrawable);
                            txt_inisial.setVisibility(View.GONE);
                        }
                    }
                });

        txt_code.setText(gv.LENDER_CODE);
        txt_email.setText(prefManager.getEmail());
        img_pp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //view.startAnimation(f.clickAnim());
                Intent intent = new Intent(SettingInsAccountActivity.this, UpdateAvaActivity.class);
                new Routes(SettingInsAccountActivity.this).moveIn(intent);
            }
        });

        cv_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingInsAccountActivity.this, ChangePasswordActivity.class);
                new Routes(SettingInsAccountActivity.this).moveIn(intent);
            }
        });

        cv_narahubung_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingInsAccountActivity.this, InsNarahubungShowActivity.class);
                intent.putExtra("jobNarahubungData", jobDataNarahubung.toString());
                new Routes(SettingInsAccountActivity.this).moveIn(intent);
            }
        });

        cv_company_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingInsAccountActivity.this, InsCompanyShowActivity.class);
                intent.putExtra("jobCompanyInfo", jobDataPerusahaan.toString());
                new Routes(SettingInsAccountActivity.this).moveIn(intent);
            }
        });

        cv_address_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingInsAccountActivity.this, InsAddressDataActivity.class);
                intent.putExtra("jobAddressDataAkta", jobDataAlamatAkta.toString());
                intent.putExtra("jobAddressDataOperasional", jobDataAlamatOperasional.toString());
                new Routes(SettingInsAccountActivity.this).moveIn(intent);
            }
        });

        cv_bank_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingInsAccountActivity.this, InsBankInfoShowActivity.class);
                intent.putExtra("jobBankInfo", jobDataRekBank.toString());
                intent.putExtra("jobCompanyInfo", jobDataPerusahaan.toString());
                new Routes(SettingInsAccountActivity.this).moveIn(intent);
            }
        });

        cv_essential_doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingInsAccountActivity.this, InsDataPendukungShowActivity.class);
                intent.putExtra("jobDocument", jobDataDokumenPendukung.toString());
                intent.putExtra("jobNarahubungData", jobDataNarahubung.toString());
                new Routes(SettingInsAccountActivity.this).moveIn(intent);
            }
        });

        loadData();

    }

    private void loadData() {
        dialog.show();
        viewModel.getAccountData(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultAccountData().observe(SettingInsAccountActivity.this, showAccountData);
    }

    private Observer<JSONObject> showAccountData = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    setEnabled();
                    JSONObject job = result.getJSONObject("result");
                    Log.e("HSL", job.toString());
                    jobDataNarahubung = job.getJSONObject("informasi_narahubung");
                    jobDataPerusahaan = job.getJSONObject("informasi_perusahaan");
                    txt_name.setText(jobDataPerusahaan.getString("nama_perusahaan"));
                    txt_inisial.setText(jobDataPerusahaan.getString("nama_perusahaan").substring(0,1));
                    prefManager.setName(jobDataPerusahaan.getString("nama_perusahaan"));
                    jobDataAlamatAkta = job.getJSONObject("alamat_akta");
                    jobDataAlamatOperasional = job.getJSONObject("alamat_operasional");
                    jobDataRekBank = job.getJSONObject("informasi_rekening");
                    jobDataDokumenPendukung = job.getJSONObject("informasi_dokumen");
                }else{
                    f.showMessage(getString(R.string.failed_load_info));
                }
                dialog.cancel();
            } catch (JSONException e) {
                e.printStackTrace();
                setDisabled();
                dialog.cancel();
                new Fungsi(SettingInsAccountActivity.this).showMessageLong(getString(R.string.failed_exc_data));
            }
        }
    };


    private void setEnabled(){
        cv_narahubung_data.setEnabled(true);
        cv_company_info.setEnabled(true);
        cv_address_data.setEnabled(true);
        cv_bank_info.setEnabled(true);
        cv_essential_doc.setEnabled(true);
        cv_change_password.setEnabled(true);
        img_pp.setEnabled(true);
    }

    private void setDisabled(){
        cv_narahubung_data.setEnabled(false);
        cv_company_info.setEnabled(false);
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

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
}