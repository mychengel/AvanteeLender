package byc.avt.avanteelender.view.auth;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.helper.Routes;
import byc.avt.avanteelender.intro.WalkthroughActivity;
import byc.avt.avanteelender.view.auth.prvrejection.PRTypeUpOneActivity;
import byc.avt.avanteelender.view.auth.prvrejection.PRTypeUpThreeActivity;
import byc.avt.avanteelender.view.auth.prvrejection.PRTypeUpTwoActivity;
import byc.avt.avanteelender.view.others.SettingActivity;
import byc.avt.avanteelender.viewmodel.AuthenticationViewModel;

public class RejectionProcessActivity extends AppCompatActivity {

    private AuthenticationViewModel viewModel;
    private PrefManager prefManager;
    private Dialog dialog;
    JSONObject result;
    TextView txtInfo;
    Button btnReupload;
    String msg = "", code = "", ctype = "";
    String[] typeUploadOne = new String[]{ "PRVP001", "PRVP002", "PRVP003", "PRVP004", "PRVP005", "PRVP006", "PRVP010", "PRVP012", "PRVP014", "PRVP015", "PRVN004", "PRVN005", "PRVK002", "PRVK003", "PRVK004", "PRVK006", "PRVK008", "PRVK009", "PRVK011", "PRVK012", "PRVK013", "PRVK015", "PRVK018", "PRVS001", "PRVS002", "PRVS003", "PRVS004", "PRVS006" };

    ///MUST BE EDITED BY ITS TYPE
    ///-->privy_status, identity_card, code, mother_maiden_name, clients_job_position, average_transaction_id, imgFile[]::3 files
    String[] typeUploadTwo = new String[]{ "PRVP009", "PRVK001", "PRVK014", "PRVD004", "PRVN002", "PRVM001", "PRVM002", "PRVM003" };

    ///-->privy_status, supportFile[]::2 files, code, category[0], category[1]
    String[] typeUploadThree = new String[]{ "PRVD001", "PRVD002", "PRVD005", "PRVD007", "PRVD009", "PRVD011", "PRVD013", "PRVK016", "PRVK017", "PRVK019" };

    boolean checkOne = false, checkTwo = false, checkThree = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejection_process);
        viewModel = new ViewModelProvider(RejectionProcessActivity.this).get(AuthenticationViewModel.class);
        prefManager = PrefManager.getInstance(RejectionProcessActivity.this);
        dialog = GlobalVariables.loadingDialog(RejectionProcessActivity.this);
        txtInfo = findViewById(R.id.txt_info_rejection_process);
        btnReupload = findViewById(R.id.btn_unggah_kembali_rejection_process);

        Intent i = getIntent();
        try {
            result = new JSONObject(i.getStringExtra("rJob"));
            ctype = i.getStringExtra("cType");
            msg = result.getJSONObject("reason").getString("reason");
            code = result.getJSONObject("reason").getString("code");
            txtInfo.setText(msg);
            checkOne = Arrays.asList(typeUploadOne).contains(code);
            checkTwo = Arrays.asList(typeUploadTwo).contains(code);
            checkThree = Arrays.asList(typeUploadThree).contains(code);
            btnReupload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(checkOne) {
                        Intent in = new Intent(RejectionProcessActivity.this, PRTypeUpOneActivity.class);
                        in.putExtra("rJob", result.toString());
                        in.putExtra("cType", ctype);
                        new Routes(RejectionProcessActivity.this).moveIn(in);
                    }else if(checkTwo){
                        if(ctype.equalsIgnoreCase("institusi")){

                        }else{
                            Intent in = new Intent(RejectionProcessActivity.this, PRTypeUpTwoActivity.class);
                            in.putExtra("rJob", result.toString());
                            new Routes(RejectionProcessActivity.this).moveIn(in);
                            //new Fungsi(RejectionProcessActivity.this).showMessage(code);
                        }
                    }else if(checkThree){
                        Intent in = new Intent(RejectionProcessActivity.this, PRTypeUpThreeActivity.class);
                        in.putExtra("rJob", result.toString());
                        in.putExtra("cType", ctype);
                        new Routes(RejectionProcessActivity.this).moveIn(in);
//                        new Fungsi(RejectionProcessActivity.this).showMessage(code);
                    }

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void logout() {
        // LOGOUT: GET method to server through endpoint
        dialog.show();
        viewModel.logout(prefManager.getUid(), prefManager.getToken());
        viewModel.getLogoutResult().observe(RejectionProcessActivity.this, checkLogout);
    }

    private Observer<String> checkLogout = new Observer<String>() {
        @Override
        public void onChanged(String result) {
            if(result.equals("ok")) {
                dialog.cancel();
                Intent intent = new Intent(RejectionProcessActivity.this, WalkthroughActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                new Routes(RejectionProcessActivity.this).moveOutIntent(intent);
            }else{
                dialog.cancel();
                new Fungsi().showMessage(result);
            }
        }
    };

    @Override
    public void onBackPressed() {
        logoutConfirmation();
    }

    private void logoutConfirmation(){
        new AlertDialog.Builder(RejectionProcessActivity.this)
                .setTitle("Konfirmasi")
                .setIcon(R.drawable.logo)
                .setMessage("Apakah anda yakin mau kembali?")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        logout();
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create()
                .show();
    }
}