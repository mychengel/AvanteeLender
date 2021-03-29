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
import android.text.Html;
import android.view.animation.AlphaAnimation;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import java.math.BigInteger;
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

    public String htmlToStr(String msg){
        String hsl = "";
        msg = msg.replace("\n", " ");
        msg = msg.replace("\t", " ");
        hsl = Html.fromHtml(msg).toString();
        return hsl;
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

    public double hitungBunga(double bunga){
        double hsl = 0.0;

        return hsl;
    }

    public String toNumb(String numb){
        String hsl = "";
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        if(numb == null || numb == "null"){
            hsl = "Rp0";
        }else{
            if(numb.contains(".")){
                hsl = formatRupiah.format((double) Long.parseLong(numb.substring(0, (numb.indexOf('.')))));
            }else{
                hsl = formatRupiah.format((double)Long.parseLong(numb));
            }
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

    public int selisihHari2(String start, String end){
        long timeStart = 0;
        long timeEnd = 0;
        int selisih = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try{
            Date startDate = sdf.parse(start);
            timeStart = startDate.getTime();
            Date endDate = sdf.parse(end);
            timeEnd = endDate.getTime();
            selisih = (int) ((timeEnd - timeStart) / 86400000)+1;
        }
        catch (ParseException e){
            e.printStackTrace();
        }

        return selisih;
    }

    public int terkumpulPersen(String pinjaman, String funding){
        int persen = 0;
        if(funding == null || funding == "null"){
            persen = 0;
        }else{
            persen = (int) ((Float.parseFloat(funding) / Float.parseFloat(pinjaman)) * 100);
        }
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

    public String tglFull(String tanggal){
        String hasil = "";
        String b = "";
        String tg = tanggal.substring(8,10);
        String th = tanggal.substring(0,4);
        String bln = tanggal.substring(5,7);

        if(bln.equals("01")){
            b = "Januari";
        }else if(bln.equals("02")){
            b = "Februari";
        }else if(bln.equals("03")){
            b = "Maret";
        }else if(bln.equals("04")){
            b = "April";
        }else if(bln.equals("05")){
            b = "Mei";
        }else if(bln.equals("06")){
            b = "Juni";
        }else if(bln.equals("07")){
            b = "Juli";
        }else if(bln.equals("08")){
            b = "Agustus";
        }else if(bln.equals("09")){
            b = "September";
        }else if(bln.equals("10")){
            b = "Oktober";
        }else if(bln.equals("11")){
            b = "November";
        }else if(bln.equals("12")){
            b = "Desember";
        }

        hasil = tg + " " + b + " " + th;
        return hasil;
    }

    public String tglFullInit(String tanggal){
        String hasil = "";
        String b = "";
        String tg = tanggal.substring(8,10);
        String th = tanggal.substring(0,4);
        String bln = tanggal.substring(5,7);

        if(bln.equals("01")){
            b = "Jan";
        }else if(bln.equals("02")){
            b = "Feb";
        }else if(bln.equals("03")){
            b = "Mar";
        }else if(bln.equals("04")){
            b = "Apr";
        }else if(bln.equals("05")){
            b = "Mei";
        }else if(bln.equals("06")){
            b = "Jun";
        }else if(bln.equals("07")){
            b = "Jul";
        }else if(bln.equals("08")){
            b = "Agus";
        }else if(bln.equals("09")){
            b = "Sep";
        }else if(bln.equals("10")){
            b = "Okt";
        }else if(bln.equals("11")){
            b = "Nov";
        }else if(bln.equals("12")){
            b = "Des";
        }

        hasil = tg + " " + b + " " + th;
        return hasil;
    }

}
