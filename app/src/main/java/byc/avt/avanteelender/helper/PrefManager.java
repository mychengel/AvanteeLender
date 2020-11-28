package byc.avt.avanteelender.helper;

import android.content.Context;
import android.content.SharedPreferences;

import byc.avt.avanteelender.model.UserData;

public class PrefManager {

    private SharedPreferences pref, userPref;
    private SharedPreferences.Editor editor, userEditor;
    private static PrefManager instance;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "welcome";
    private static final String PREF_USER = "user";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    public PrefManager(){}

    private PrefManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

        //pref manager for user auth
        userPref = context.getSharedPreferences(PREF_USER, PRIVATE_MODE);
        userEditor = userPref.edit();
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

    public void setUserData(UserData ud){
        userEditor.putString("uid", ud.getUid());
        userEditor.putInt("type", ud.getType());
        userEditor.putString("client_type", ud.getClient_type());
        userEditor.putString("avatar", ud.getAvatar());
        userEditor.putString("name", ud.getName());
        userEditor.putInt("avantee_verif", ud.getAvantee_verif());
        userEditor.putString("token", ud.getToken());
        userEditor.putString("expired_time", ud.getToken());
        userEditor.commit();
    }

    public void clearUserData(){
        userEditor.clear().commit();
    }

    //function to manage user pref



}
