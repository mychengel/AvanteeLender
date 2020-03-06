package byc.avt.avanteelender.intro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import byc.avt.avanteelender.R;

public class Splash extends AppCompatActivity {

    ImageView img_logo;
    Intent i = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent onBoard = new Intent(Splash.this, Walkthrough.class);
                onBoard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(onBoard);
                finish();
            }
        }, 2500);
    }
}
