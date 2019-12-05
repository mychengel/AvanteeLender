package byc.avt.avanteelender;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class Daftar extends AppCompatActivity {

    ActionBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        bar = getSupportActionBar();
        bar.setHomeAsUpIndicator(R.drawable.ic_back_grey_24dp);
        bar.setHomeButtonEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle("");
        bar.setElevation(0);


    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
