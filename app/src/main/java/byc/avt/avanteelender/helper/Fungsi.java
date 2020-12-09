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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
        if(numb.length() >= 3){
            if(numb.charAt(numb.length()-3) == '.'){
                hsl = formatRupiah.format((double)Integer.parseInt(numb.substring(0, (numb.length()-3))));
            }else if(numb.charAt(numb.length()-2) == '.'){
                hsl = formatRupiah.format((double)Integer.parseInt(numb.substring(0, (numb.length()-2))));
            }else{
                hsl = formatRupiah.format((double)Integer.parseInt(numb));
            }
        }else{
            hsl = formatRupiah.format((double)Integer.parseInt(numb));
        }

        return hsl;
    }

    public int selisihHari(String date){
        Calendar cNow = Calendar.getInstance();
        Date currentTime = cNow.getTime();
        long millisNow = currentTime.getTime();

        long timeInMilliseconds = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try{
            Date mDate = sdf.parse(date);
            timeInMilliseconds = mDate.getTime();
        }
        catch (ParseException e){
            e.printStackTrace();
        }

        int selisih = (int) ((timeInMilliseconds - millisNow) / 86400000)+1;

        return selisih;
    }

    public int terkumpulPersen(String pinjaman, String funding){
        int persen = (int) ((Float.parseFloat(funding) / Float.parseFloat(pinjaman)) * 100);
        return persen;
    }

    public long millisFromDate(String date){
        long timeInMilliseconds = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try{
            Date mDate = sdf.parse(date);
            timeInMilliseconds = mDate.getTime();
        }
        catch (ParseException e){
            e.printStackTrace();
        }

        return timeInMilliseconds;
    }

    public long millisFromNow(){
        Calendar cNow = Calendar.getInstance();
        Date currentTime = cNow.getTime();
        long millisNow = currentTime.getTime();
        return millisNow;
    }

}
