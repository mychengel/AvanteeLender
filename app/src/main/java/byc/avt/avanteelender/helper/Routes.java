package byc.avt.avanteelender.helper;

import android.content.Context;
import android.content.Intent;

import byc.avt.avanteelender.R;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import static androidx.core.content.ContextCompat.startActivity;

public class Routes {

    Context ctx;

    public Routes(){}

    public Routes(Context ctx){
        this.ctx = ctx;
    }

    public void moveIn(Intent intent){
        ((AppCompatActivity)ctx).startActivity(intent);
        ((AppCompatActivity)ctx).overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    public void moveInFinish(Intent intent){
        ((AppCompatActivity)ctx).startActivity(intent);
        ((AppCompatActivity)ctx).overridePendingTransition(R.anim.enter, R.anim.exit);
        ((AppCompatActivity)ctx).finish();
    }

    public void moveOut(){
        ((AppCompatActivity)ctx).finish();
        ((AppCompatActivity)ctx).overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }

    public void moveOutIntent(Intent intent){
        ((AppCompatActivity)ctx).startActivity(intent);
        ((AppCompatActivity)ctx).overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        ((AppCompatActivity)ctx).finish();

    }

}
