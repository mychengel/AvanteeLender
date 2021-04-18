package byc.avt.avanteelender.helper;

import android.content.Context;
import android.content.SharedPreferences;

import byc.avt.avanteelender.model.UserData;

public class PrefManager {

    private SharedPreferences pref, userPref, keyPref, docPref;
    private SharedPreferences.Editor editor, userEditor, keyEditor, docEditor;
    private static PrefManager instance;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "welcome";
    private static final String PREF_USER = "user";
    private static final String PREF_KEYS = "keys";
    private static final String PREF_DOC = "doc";
    private static final String PREF_PERSONAL_REG = "personalreg";
    private static final String PREF_INSTITUTION_REG = "institutionreg";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    public PrefManager(){}

    private PrefManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

        userPref = context.getSharedPreferences(PREF_USER, PRIVATE_MODE);
        userEditor = userPref.edit();

        keyPref = context.getSharedPreferences(PREF_KEYS, PRIVATE_MODE);
        keyEditor = keyPref.edit();

        docPref = context.getSharedPreferences(PREF_DOC, PRIVATE_MODE);
        docEditor = docPref.edit();

//        perRegPref = context.getSharedPreferences(PREF_PERSONAL_REG, PRIVATE_MODE);
//        perRegEditor = perRegPref.edit();
//
//        insRegPref = context.getSharedPreferences(PREF_INSTITUTION_REG, PRIVATE_MODE);
//        insRegEditor = insRegPref.edit();
    }

    public static PrefManager getInstance(Context context) {
        if (instance == null) {
            instance = new PrefManager(context);
        }
        return instance;
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public String getEmail(){
        String email = userPref.getString("email","-");
        return email;
    }

    public String getPassword(){
        String password = userPref.getString("password","-");
        return password;
    }

    public String getUid(){
        String uid = userPref.getString("uid","-");
        return uid;
    }

    public String getName(){
        String name = userPref.getString("name","-");
        return name;
    }

    public String getToken(){
        String token = userPref.getString("token","-");
        return token;
    }

    public String getAvatar(){
        String avatar = userPref.getString("avatar","-");
        return avatar;
    }

    public long getExpiredTime(){
        long exp = userPref.getLong("expired_time",0);
        return exp;
    }

    public void setExpiredTime(long millisNow){
        userEditor.putLong("expired_time", millisNow);
        userEditor.commit();
    }

    public void setUserData(UserData ud){
        userEditor.putString("email", ud.getEmail());
        userEditor.putString("password", ud.getPassword());
        userEditor.putString("uid", ud.getUid());
        userEditor.putInt("type", ud.getType());
        userEditor.putString("client_type", ud.getClient_type());
        userEditor.putString("avatar", ud.getAvatar());
        userEditor.putString("name", ud.getName());
        userEditor.putInt("avantee_verif", ud.getAvantee_verif());
        userEditor.putString("token", ud.getToken());
        userEditor.putLong("expired_time", ud.getExpired_time());
        userEditor.commit();
    }

    public void clearUserData(){
        userEditor.clear().commit();
    }

    public void setResetPasswordKey(String key){
        keyEditor.putString("reset_pass_key", key);
        keyEditor.commit();
    }

    public String getResetPasswordKey(){
        String key = keyPref.getString("reset_pass_key","-");
        return key;
    }

    public void clearResetPasswordKeyData(){
        keyEditor.clear().commit();
    }

    //documents verification check
    public void setDocVerification(String ver){
        docEditor.putString("is_doc_verif", ver);
        docEditor.commit();
    }

    public String getDocVerification(){
        String ver = docPref.getString("is_doc_verif","0");
        return ver;
    }

    public void clearDocVerification(){
        docEditor.clear().commit();
    }

    //function to manage user pref



}
