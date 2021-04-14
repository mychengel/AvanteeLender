package byc.avt.avanteelender.view.features.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import byc.avt.avanteelender.R;

public class DataPendukungShowActivity extends AppCompatActivity {

    private Toolbar toolbar;
    JSONObject job;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_pendukung_show);
        toolbar = findViewById(R.id.toolbar_ess_document_show);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_back_24px);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        try {
            job = new JSONObject(i.getStringExtra("jobEssDocument"));


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