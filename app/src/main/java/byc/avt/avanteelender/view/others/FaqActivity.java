package byc.avt.avanteelender.view.others;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.adapter.AvtFaqAdapter;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.model.AvtFaq;
import byc.avt.avanteelender.viewmodel.AuthenticationViewModel;

public class FaqActivity extends AppCompatActivity {

    Fungsi f = new Fungsi(FaqActivity.this);
    Toolbar bar;
    private PrefManager prefManager;
    RecyclerView rv_faq;
    private Dialog dialog;
    private AuthenticationViewModel viewModel;
    ArrayList<AvtFaq> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        dialog = GlobalVariables.loadingDialog(FaqActivity.this);
        viewModel = new ViewModelProvider(FaqActivity.this).get(AuthenticationViewModel.class);
        bar = findViewById(R.id.toolbar_faq);
        setSupportActionBar(bar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_back_24px);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        prefManager = PrefManager.getInstance(FaqActivity.this);
        rv_faq = findViewById(R.id.rv_faq);
        loadData();
    }

    private void loadData() {
        dialog.show();
        viewModel.getSettingData(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultSettingData().observe(FaqActivity.this, showSettingData);
    }

    private Observer<JSONObject> showSettingData = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                list.clear();
                rv_faq.setAdapter(null);
                if(result.getInt("code") == 200){
                    JSONObject job = result.getJSONObject("result");
                    JSONArray faq_jar = job.getJSONArray("faq");
                    for(int i = 0; i < faq_jar.length(); i++){
                        AvtFaq faq = new AvtFaq(f.htmlToStr(faq_jar.getJSONObject(i).getString("faq")), f.htmlToStr(faq_jar.getJSONObject(i).getString("content_text")));
                        list.add(faq);
                    }
                    rv_faq.setLayoutManager(new LinearLayoutManager(FaqActivity.this));
                    AvtFaqAdapter avtFaqAdapter = new AvtFaqAdapter(FaqActivity.this);
                    avtFaqAdapter.setListAvtFaq(list);
                    rv_faq.setAdapter(avtFaqAdapter);
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