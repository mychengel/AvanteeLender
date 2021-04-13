package byc.avt.avanteelender.helper;

import android.app.Dialog;
import android.content.Context;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import byc.avt.avanteelender.R;

public final class GlobalVariables {

    //public static final String BASE_URL = "https://avantee.co.id:8444/baycode_api/";
    public static final String BASE_URL = "https://avantee.co.id:8444/api/";
    public static final String BASE_URL2 = "https://avantee.co.id:8444/api/internal/";
    public static final String IMG_URL = "https://avantee.co.id:8444/assets/images/";
    public static final int BIAYA_PENARIKAN = 6000;
    public static String LENDER_CODE;
    public static final Pattern PASSWORD_PATTERN = Pattern.compile("^" +
            "(?=.*[0-9])" +         //at least has 1 number
            "(?=.*[a-z])" +         //at least has 1 lower case letter
            "(?=.*[A-Z])" +         //at least has 1 upper case letter
            //"(?=.*[a-zA-Z])" + //can be any letter (uppercase/lowercase)
            //"(?=.*[@#$%^&+=])" + //at least 1 special character
            "(?=\\S+$)." + //no white spaces
            "{8,12}" + //at least 8 character, max 12 character
            "$");

    public static final Pattern PASSWORD_PATTERN2 = Pattern.compile("^" +
            //"(?=.*[0-9])" +         //at least has 1 number
            //"(?=.*[a-z])" +         //at least has 1 lower case letter
            //"(?=.*[A-Z])" +         //at least has 1 upper case letter
            //"(?=.*[a-zA-Z])" + //can be any letter (uppercase/lowercase)
            //"(?=.*[@#$%^&+=])" + //at least 1 special character
            "(?=\\S+$)." + //no white spaces
            "{1,50}" + //at least 1 character, max 50 character
            "$");

    public static Map<String, Object> perRegData = new HashMap<>();
    public static boolean stPerPersonalData;
    public static boolean stPerWorkInfo;
    public static boolean stPerAddressData;
    public static boolean stPerBankInfo;
    public static boolean stPerDocument;

    public static Map<String, Object> insRegData = new HashMap<>();

    public static Map<String, String> API_ACCESS(){
        //final String basicAuth = "Basic " + Base64.encodeToString("Av@nTe3:p@ssw0rdyangsimpleajalahyah".getBytes(), Base64.NO_WRAP);
        final String basicAuth = "Basic " + Base64.encodeToString("B@code3:p@ssw0rdyangsimpleajalahyah".getBytes(), Base64.NO_WRAP);
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", basicAuth);
        //header.put("Content-Type", "application/json; charset=utf-8");
        //header.put("X-API-KEY", "G2HN@D4N483RS@MA");
        //header.put("userid", "baycode");
        return header;
    }

    public static Map<String, String> API_ACCESS_IN(String uid, String token){
        //final String basicAuth = "Basic " + Base64.encodeToString("Av@nTe3:p@ssw0rdyangsimpleajalahyah".getBytes(), Base64.NO_WRAP);
        final String basicAuth = "Basic " + Base64.encodeToString("B@code3:p@ssw0rdyangsimpleajalahyah".getBytes(), Base64.NO_WRAP);
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", basicAuth);
        header.put("Token", token);
        header.put("Uid", uid);
        //header.put("Content-Type", "application/json; charset=utf-8");
        return header;
    }

    public static final Dialog loadingDialog(Context context){
        final Dialog dialog = new Dialog(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_loading, null);
        dialog.setContentView(dialogView);
        dialog.setCancelable(false);
        return dialog;
    }

}