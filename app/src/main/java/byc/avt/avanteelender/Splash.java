package byc.avt.avanteelender;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Splash extends AppCompatActivity {

    ActionBar bar;
    ImageView img_logo;
    Intent i = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        bar = getSupportActionBar();
        bar.hide();

        img_logo = findViewById(R.id.img_logo_splash);
        i = new Intent(getApplicationContext(), Walkthrough.class);
        Animation anim = AnimationUtils.loadAnimation(Splash.this,R.anim.transition);
        img_logo.startAnimation(anim);

        Thread timer = new Thread(){
            public void run(){
                try {
                    sleep(2500);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();
    }

}
