package byc.avt.avanteelender.view.features.account.institution;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.helper.Routes;
import byc.avt.avanteelender.viewmodel.AuthenticationViewModel;
import byc.avt.avanteelender.viewmodel.MasterDataViewModel;

public class InsDataPendukungShowActivity extends AppCompatActivity {

    private MasterDataViewModel viewModel;
    private AuthenticationViewModel viewModel2;
    Button btn_save, btn_edit;
    boolean editIsOn = false;
    private Toolbar toolbar;
    JSONObject job, job2;
    private PrefManager prefManager;
    private Dialog dialog;
    GlobalVariables gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ins_data_pendukung_show);

        gv.insEditData.clear();
        gv.insEditDataFile.clear();
        viewModel = new ViewModelProvider(this).get(MasterDataViewModel.class);
        viewModel2 = new ViewModelProvider(this).get(AuthenticationViewModel.class);
        toolbar = findViewById(R.id.toolbar_ins_data_pendukung_show);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_back_24px);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        prefManager = PrefManager.getInstance(InsDataPendukungShowActivity.this);
        dialog = GlobalVariables.loadingDialog(InsDataPendukungShowActivity.this);

        Intent i = getIntent();
        try {
            job = new JSONObject(i.getStringExtra("jobDocument"));
            job2 = new JSONObject(i.getStringExtra("jobNarahubungData"));

        } catch (JSONException e) {
            e.printStackTrace();
        }


        btn_save = findViewById(R.id.btn_save_ins_data_pendukung_show);
        btn_save.setEnabled(true);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //confirmNext(v);
            }
        });

        btn_edit = findViewById(R.id.btn_ubah_ins_data_pendukung_show);
        btn_edit.setEnabled(true);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editIsOn = !editIsOn;
                editIsOn(editIsOn);
            }
        });

        editIsOn(false);
        //loadData();

    }

    public void editIsOn(boolean s){

        btn_save.setEnabled(s);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            new Routes(InsDataPendukungShowActivity.this).moveOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new Routes(InsDataPendukungShowActivity.this).moveOut();
    }

}