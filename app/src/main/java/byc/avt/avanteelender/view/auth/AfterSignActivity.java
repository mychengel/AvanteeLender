package byc.avt.avanteelender.view.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import org.json.JSONException;
import org.json.JSONObject;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.helper.Routes;
import byc.avt.avanteelender.model.UserData;
import byc.avt.avanteelender.view.MainActivity;
import byc.avt.avanteelender.view.misc.OTPActivity;
import byc.avt.avanteelender.view.misc.OTPDocActivity;
import byc.avt.avanteelender.viewmodel.AuthenticationViewModel;

public class AfterSignActivity extends AppCompatActivity {

    Fungsi f = new Fungsi(AfterSignActivity.this);
    private AuthenticationViewModel viewModel;
    private PrefManager prefManager;
    private Dialog dialog;
    GlobalVariables gv;
    String strData = "", email = "", password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_sign);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog = GlobalVariables.loadingDialog(AfterSignActivity.this);
        prefManager = PrefManager.getInstance(AfterSignActivity.this);
        viewModel = new ViewModelProvider(this).get(AuthenticationViewModel.class);
        email = prefManager.getEmail();
        password = prefManager.getPassword();
        confirmLogin();
    }

    public void confirmLogin() {
        // POST to server through endpoint
        dialog.show();
        viewModel.login(email, password);
        viewModel.getLoginResult().observe(AfterSignActivity.this, checkSuccess);
    }

    private Observer<JSONObject> checkSuccess = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            JSONObject res;
            String msg = "";
            try {
                if (result.getInt("code") == 200 && result.getBoolean("status") == true) {
                    Intent i = null;
                    String token = result.getString("token");
                    res = result.getJSONObject("result");
                    String uid = res.getString("uid");
                    int verif = res.getInt("avantee_verif");
                    UserData ud = new UserData(prefManager.getEmail(),prefManager.getPassword(),uid,res.getInt("type"),res.getString("client_type"),res.getString("avatar"),res.getString("name"),verif,token,0);
                    prefManager.setUserData(ud);
                    if(verif == 1){
                        if(res.isNull("doc") && res.isNull("swafoto") && res.isNull("docfile")){
                            Log.e("Doc", "Aman");
                            if(res.isNull("doc_otp")){
                                if(res.isNull("privy_status")){
                                    Log.e("PrivyStatus", "Aman");
                                    if(res.isNull("suratkuasa")){
                                        Log.e("TTDSuratKuasa", "Aman");
                                        if(res.isNull("suratperjanjian")){
                                            // Masuk DASHBOARD
                                            Log.e("TTDSuratPK", "Aman");
                                            i = new Intent(AfterSignActivity.this, MainActivity.class);
                                            prefManager.setName(res.getString("name"));
                                            i.putExtra("dest","1");
                                            f.showMessage("Selamat datang "+res.getString("name"));
                                        }else{
                                            msg = res.getJSONObject("suratperjanjian").getString("msg");
                                            f.showMessage(msg);
                                            i = new Intent(AfterSignActivity.this, SignersCheckActivity.class);
                                            i.putExtra("doc_type", "Surat Perjanjian");
                                            //diarahkan untuk ttd surat perjanjian kerja sama
                                        }
                                    }else{
                                        msg = res.getJSONObject("suratkuasa").getString("msg");
                                        f.showMessage(msg);
                                        i = new Intent(AfterSignActivity.this, SignersCheckActivity.class);
                                        i.putExtra("doc_type", "Surat Kuasa");
                                        //diarahkan untuk ttd surat kuasa
                                    }
                                }else{
                                    msg = res.getJSONObject("privy_status").getString("msg");
                                    i = new Intent(AfterSignActivity.this, InVerificationProcessActivity.class);
                                    i.putExtra("info", msg);
                                }
                            }else{
                                i = new Intent(AfterSignActivity.this, OTPDocActivity.class);
                                i.putExtra("from", "login");
                            }
                        }else{
                            i = new Intent(AfterSignActivity.this, RegistrationFormActivity.class);
                        }
                    }else{
                        i = new Intent(AfterSignActivity.this, OTPActivity.class);
                    }
                    //Routing
                    new Routes(AfterSignActivity.this).moveInFinish(i);
                    dialog.cancel();
                }else{
                    res = result.getJSONObject("result");
                    msg = res.getString("message");
                    f.showMessage(msg);
                }
                dialog.cancel();
            } catch (JSONException e) {
                e.printStackTrace();
                dialog.cancel();
            }
            dialog.cancel();
        }
    };

}