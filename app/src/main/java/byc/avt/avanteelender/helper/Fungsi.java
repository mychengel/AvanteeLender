package byc.avt.avanteelender.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Build;
import android.text.Html;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AlignmentSpan;
import android.util.Base64;
import android.view.Gravity;
import android.view.animation.AlphaAnimation;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
        if(msg.equalsIgnoreCase("")){
            Toast.makeText(ctx, "", Toast.LENGTH_SHORT).show();
        }else{
            Spannable centeredText = new SpannableString(msg);
            centeredText.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                    0, msg.length() - 1,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            Toast.makeText(ctx, centeredText, Toast.LENGTH_SHORT).show();
        }
    }

    public void showMessageLong(String msg) {
        if(msg.equalsIgnoreCase("")){
            Toast.makeText(ctx, "", Toast.LENGTH_SHORT).show();
        }else{
            Spannable centeredText = new SpannableString(msg);
            centeredText.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                    0, msg.length() - 1,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            Toast.makeText(ctx, centeredText, Toast.LENGTH_LONG).show();
        }
    }

    public Spanned htmlToStr(String msg){
        Spanned hsl;
        //msg = msg.replace("<li>", "\n");
        //msg = msg.replace("<\li>", " ");
        msg = msg.replace("\n", " ");
        msg = msg.replace("\t", " ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            hsl = Html.fromHtml(msg,  Html.FROM_HTML_OPTION_USE_CSS_COLORS);
        }else{
            hsl = Html.fromHtml(msg);
        }
        return hsl;
    }

    int bitmap_size = 60;
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
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
        canvas.drawCircle(bitmap.getWidth()/2, bitmap.getHeight()/2,
                bitmap.getWidth()/2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }

    public Bitmap getCroppedBitmapSquare(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRect(rect, paint);
        //canvas.drawRoundRect(, bitmap.getWidth() / 2, bitmap.getHeight() / 2, paint);
        //canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, 10, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }
    public static Bitmap rotateImage(Bitmap bitmap) throws IOException {
        Bitmap rotatedBitmap = rotateImage(bitmap, 90);
        return rotatedBitmap;
    }

    public static Bitmap getRotateImage(String photoPath, Bitmap bitmap) throws IOException {
        Bitmap rotatedBitmap = null;
        ExifInterface ei = new ExifInterface(photoPath);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL);
        if(Build.VERSION.SDK_INT > 26){
            //rotatedBitmap = bitmap;
            if(Build.VERSION.SDK_INT >= 30){
                if(Build.MODEL.contains("SM")){
                    if(orientation == ExifInterface.ORIENTATION_ROTATE_90 || orientation == ExifInterface.ORIENTATION_ROTATE_270){
                        rotatedBitmap = rotateImage(bitmap, 90);
                    }else{
                        rotatedBitmap = bitmap;
                    }
                }else{
                    rotatedBitmap = bitmap;
                }
            }else{
                rotatedBitmap = bitmap;
            }
        }else{
            rotatedBitmap = rotateImage(bitmap, 90);
        }

        return rotatedBitmap;
    }

    public static Bitmap getRotateImage2(String photoPath, Bitmap bitmap) throws IOException {
        ExifInterface ei = new ExifInterface(photoPath);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL);
        Bitmap rotatedBitmap = null;
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = rotateImage(bitmap, 90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = rotateImage(bitmap, 180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = rotateImage(bitmap, 270);
                break;
            case ExifInterface.ORIENTATION_UNDEFINED:
                rotatedBitmap = bitmap;
                break;
            case ExifInterface.ORIENTATION_NORMAL:
                rotatedBitmap = bitmap;
                break;
        }

        return rotatedBitmap;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public String toNumb(String numb){
        String hsl = "";
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        if(numb == null || numb == "null" || numb == "" || numb.isEmpty()){
            hsl = "Rp0";
        }else{
            if(numb.contains(".")){
                hsl = formatRupiah.format((double) Long.parseLong(numb.substring(0, (numb.indexOf('.')))));
            }else{
                hsl = formatRupiah.format((double)Long.parseLong(numb));
            }
        }

        if(hsl.contains(",")){
            hsl = hsl.substring(0, hsl.indexOf(','));
        }else{}

        return hsl;
    }

    public String dateStd(String date){
        String hsl = "";
        hsl = date.substring(8,10)+"-"+date.substring(5,7)+"-"+date.substring(0,4);
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
        if(tanggal.equalsIgnoreCase(null) || tanggal.equalsIgnoreCase("null")){
            hasil = "-";
        }else{
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
        }

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

    public static byte[] getFileDataFromDrawable(Context context, int id) {
        Drawable drawable = ContextCompat.getDrawable(context, id);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * Turn drawable into byte array.
     *
     * @param drawable data
     * @return byte array
     */
    public static byte[] getFileDataFromDrawable(Context context, Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] getFileDataFromBitmap(Context context, Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

}
