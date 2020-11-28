package byc.avt.avanteelender.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.view.animation.AlphaAnimation;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import java.text.NumberFormat;
import java.util.Locale;

import byc.avt.avanteelender.intro.SplashActivity;
import byc.avt.avanteelender.intro.WalkthroughActivity;
import byc.avt.avanteelender.viewmodel.SplashViewModel;

public class Fungsi {

    Context ctx;

    public Fungsi(){}

    public Fungsi(Context ctx){
        this.ctx = ctx;
    }

    public AlphaAnimation clickAnim(){
        return new AlphaAnimation(0.0f,0.6f);
    }

    public void showMessage(String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }

    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }

    public String toNumb(String numb){
        String hsl = "";
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        if(numb.charAt(numb.length()-3) == '.'){
            hsl = formatRupiah.format((double)Integer.parseInt(numb.substring(0, (numb.length()-4))));
        }else{
            hsl = formatRupiah.format((double)Integer.parseInt(numb));
        }
        return hsl;
    }

//    public void checkSession(SplashViewModel viewModel) {
//        // POST to server through endpoint
//        viewModel.sessionCheck(prefManager.getUid(), prefManager.getToken(), ctx);
//        viewModel.getResult().observe((LifecycleOwner) ctx, checkSuccess);
//    }
//
//    private Observer<String> checkSuccess = new Observer<String>() {
//        @Override
//        public void onChanged(String result) {
//            if(result.equals("ok")) {
//                Intent onBoard = new Intent(ctx, WalkthroughActivity.class);
//                onBoard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                ((AppCompatActivity)ctx).startActivity(onBoard);
//                ((AppCompatActivity)ctx).finish();
//            }else{
//                Intent onBoard = new Intent(ctx, WalkthroughActivity.class);
//                onBoard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                showMessage(result);
//                ((AppCompatActivity)ctx).startActivity(onBoard);
//                ((AppCompatActivity)ctx).finish();
//            }
//        }
//    };

}
