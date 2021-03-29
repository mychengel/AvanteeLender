package byc.avt.avanteelender.view.others;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.viewmodel.AuthenticationViewModel;

public class TermsActivity extends AppCompatActivity {

    Fungsi f = new Fungsi(TermsActivity.this);
    Toolbar bar;
    private PrefManager prefManager;
    TextView txt_content;
    private Dialog dialog;
    private AuthenticationViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        dialog = GlobalVariables.loadingDialog(TermsActivity.this);
        viewModel = new ViewModelProvider(TermsActivity.this).get(AuthenticationViewModel.class);
        bar = findViewById(R.id.toolbar_terms);
        setSupportActionBar(bar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_back_24px);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        prefManager = PrefManager.getInstance(TermsActivity.this);
        txt_content = findViewById(R.id.content_terms);
        loadData();
    }

    private void loadData() {
        dialog.show();
        viewModel.getSettingData(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultSettingData().observe(TermsActivity.this, showSettingData);
    }

    private Observer<JSONObject> showSettingData = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    JSONObject job = result.getJSONObject("result");
                    JSONObject terms_job = job.getJSONObject("syaratketentuan");
                    String terms = terms_job.getString("content_text");
                    String terms_final = f.htmlToStr(terms);
                    txt_content.setText(terms_final);
                }else{
                    f.showMessage(getString(R.string.failed_load_info));
                }
                dialog.cancel();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

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