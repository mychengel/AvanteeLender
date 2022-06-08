package byc.avt.avanteelender.view.auth.prvrejection;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.helper.Routes;
import byc.avt.avanteelender.intro.WalkthroughActivity;
import byc.avt.avanteelender.model.DataPart;
import byc.avt.avanteelender.view.auth.InVerificationProcessActivity;
import byc.avt.avanteelender.viewmodel.AuthenticationViewModel;
import byc.avt.avanteelender.viewmodel.MasterDataViewModel;

public class PRTypeUpTwoActivity extends AppCompatActivity {

    private AuthenticationViewModel viewModel;
    private MasterDataViewModel viewModel2;
    private PrefManager prefManager;
    private Dialog dialog;
    private Toolbar toolbar;
    GlobalVariables gv;
    Fungsi f = new Fungsi(PRTypeUpTwoActivity.this);

    String msg = "", handler = "", code = "", status = "", category = "";
    JSONObject result;
    JSONArray handlers;
    TextView txtInfo;
    Button btnSimpan;

    AutoCompleteTextView auto_job_position, auto_avg_transaction;
    TextInputLayout txt_job_position, txt_avg_transaction, edit_ktp, edit_mothername;
    List<Object> listJobPosition = new ArrayList<>(); List<Object> listJobPositionID = new ArrayList<>();
    List<Object> listAvgTrans = new ArrayList<>(); List<Object> listAvgTransID = new ArrayList<>();
    String jobPosition="", motherName="", no_ktp="", avgTrans="";

    TextView txt_ktp, txt_selfie, txt_ttd;
    byte[][] imgFile = null;
    byte[] ktp_byte = null, selfie_byte = null, ttd_byte = null;
    CardView cv_ktp, cv_selfie, cv_ttd;
    ImageView img_ktp, img_cancelktp, img_selfie, img_cancelselfie, img_ttd, img_cancelttd;
    Bitmap bitmap, decoded_ktp, decoded_npwp, decoded_selfie, decoded_ttd;
    String str_ktp = "", str_npwp = "", str_selfie = "", str_ttd = "";
    int PICK_KTP = 1, PICK_SELFIE = 3, PICK_TTD = 4, PICK_KTP_CAM = 5, PICK_SELFIE_CAM = 7, PICK_TTD_CAM = 8;
    String PICK_TYPE_KTP = "ktp", PICK_TYPE_SELFIE = "selfie", PICK_TYPE_TTD = "ttd";
    int BITMAP_SIZE = 60, MAX_SIZE = 640, CROP_KTP = 101, CROP_SELFIE = 103, CROP_TTD = 104;

    Button btnr_ktp, btnr_selfie, btnr_ttd;
    ImageView imgr_ktp, imgr_selfie, imgr_ttd;
    Bitmap bitmap_ktp, bitmap_selfie, bitmap_ttd;
    String csc = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prtype_up_two);
        GlobalVariables.perReregData.clear();
        GlobalVariables.perReregDataFile.clear();
        GlobalVariables.perReregDataFileArray.clear();
        viewModel = new ViewModelProvider(PRTypeUpTwoActivity.this).get(AuthenticationViewModel.class);
        viewModel2 = new ViewModelProvider(PRTypeUpTwoActivity.this).get(MasterDataViewModel.class);
        prefManager = PrefManager.getInstance(PRTypeUpTwoActivity.this);
        toolbar = findViewById(R.id.toolbar_prtype_up_two);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_back_24px);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dialog = GlobalVariables.loadingDialog(PRTypeUpTwoActivity.this);

        cv_ktp = findViewById(R.id.cv_take_ktp_prtype_up_two);
        
        cv_selfie = findViewById(R.id.cv_take_selfie_prtype_up_two);
        cv_ttd = findViewById(R.id.cv_take_ttd_prtype_up_two);
        edit_ktp = findViewById(R.id.edit_ktp_number_prtype_up_two);
        edit_mothername = findViewById(R.id.edit_mothername_prtype_up_two);
        txt_ktp = findViewById(R.id.txt_take_ktp_prtype_up_two);
        txt_selfie = findViewById(R.id.txt_take_selfie_prtype_up_two);
        txt_ttd = findViewById(R.id.txt_take_ttd_prtype_up_two);
        img_ktp = findViewById(R.id.img_take_ktp_prtype_up_two);
        img_cancelktp =findViewById(R.id.img_cancel_take_ktp_prtype_up_two);
        img_selfie = findViewById(R.id.img_take_selfie_prtype_up_two);
        img_cancelselfie = findViewById(R.id.img_cancel_take_selfie_prtype_up_two);
        img_ttd = findViewById(R.id.img_take_ttd_prtype_up_two);
        img_cancelttd = findViewById(R.id.img_cancel_take_ttd_prtype_up_two);
        txtInfo = findViewById(R.id.txt_info_prtype_up_two);
        txtInfo.setText(getString(R.string.lets_get_know_eo));
        btnSimpan = findViewById(R.id.btn_simpan_prtype_up_two);

        auto_job_position = findViewById(R.id.auto_jabatan_prtype_up_two);
        auto_avg_transaction = findViewById(R.id.auto_avg_transaction_prtype_up_two);
        txt_job_position = findViewById(R.id.edit_jabatan_prtype_up_two);
        txt_avg_transaction = findViewById(R.id.edit_avg_transaction_prtype_up_two);

        Intent i = getIntent();
        try {
            result = new JSONObject(i.getStringExtra("rJob"));
            status = result.getString("msg").toLowerCase();
            msg = result.getJSONObject("reason").getString("reason");
            code = result.getJSONObject("reason").getString("code");
            //handlers = result.getJSONObject("reason").getJSONArray("handlers");
            //category = handlers.getJSONObject(0).getString("category");
            //handler = handlers.getJSONObject(0).getString("handler");
            txtInfo.setText(msg);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        edit_ktp.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                no_ktp = edit_ktp.getEditText().getText().toString().trim();
                cekKTP(no_ktp);
                cekDone();
            }
        });

        edit_mothername.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                motherName = edit_mothername.getEditText().getText().toString().trim();
                cekMotherName(motherName);
                cekDone();
            }
        });

        cv_ktp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(str_ktp.equalsIgnoreCase("")){
                    chooseFileConfirmation(PICK_TYPE_KTP);
                }
            }
        });

        cv_selfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(str_selfie.equalsIgnoreCase("")){
                    chooseFileConfirmation(PICK_TYPE_SELFIE);
                }
            }
        });

        cv_ttd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(str_ttd.equalsIgnoreCase("")){
                    chooseFileConfirmation(PICK_TYPE_TTD);
                }
            }
        });

        img_cancelktp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_ktp = "";
                ktp_byte = null;
                imgr_ktp.setImageResource(R.drawable.ic_baseline_no_photography_24);
                btnr_ktp.setVisibility(View.INVISIBLE);
                cekView();
                cekDone();
            }
        });

        img_cancelselfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_selfie = "";
                selfie_byte = null;
                imgr_selfie.setImageResource(R.drawable.ic_baseline_no_photography_24);
                btnr_selfie.setVisibility(View.INVISIBLE);
                cekView();
                cekDone();
            }
        });

        img_cancelttd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_ttd = "";
                ttd_byte = null;
                imgr_ttd.setImageResource(R.drawable.ic_baseline_no_photography_24);
                btnr_ttd.setVisibility(View.INVISIBLE);
                cekView();
                cekDone();
            }
        });

        clearMasterList();

        viewModel2.getJobPosition(prefManager.getUid(), prefManager.getToken());
        viewModel2.getResultJobPosition().observe(PRTypeUpTwoActivity.this, showJobPosition);
        viewModel2.getAvgTransaction(prefManager.getUid(), prefManager.getToken());
        viewModel2.getResultAvgTransaction().observe(PRTypeUpTwoActivity.this, showAvgTrans);

        imgr_ktp = findViewById(R.id.img_ktp_prtype_up_two);
        btnr_ktp = findViewById(R.id.btn_ktp_prtype_up_two);
        btnr_ktp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    bitmap_ktp = f.rotateImage(bitmap_ktp);
                    ByteArrayOutputStream byt = new ByteArrayOutputStream();
                    imgr_ktp.setImageBitmap(bitmap_ktp);
                    bitmap_ktp.compress(Bitmap.CompressFormat.JPEG, 100, byt);
                    ktp_byte = byt.toByteArray();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        imgr_selfie = findViewById(R.id.img_selfie_prtype_up_two);
        btnr_selfie = findViewById(R.id.btn_selfie_prtype_up_two);
        btnr_selfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    bitmap_selfie = f.rotateImage(bitmap_selfie);
                    ByteArrayOutputStream byt = new ByteArrayOutputStream();
                    imgr_selfie.setImageBitmap(bitmap_selfie);
                    bitmap_selfie.compress(Bitmap.CompressFormat.JPEG, 100, byt);
                    selfie_byte = byt.toByteArray();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        imgr_ttd = findViewById(R.id.img_ttd_prtype_up_two);
        btnr_ttd = findViewById(R.id.btn_ttd_prtype_up_two);
        btnr_ttd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    bitmap_ttd = f.rotateImage(bitmap_ttd);
                    ByteArrayOutputStream byt = new ByteArrayOutputStream();
                    imgr_ttd.setImageBitmap(bitmap_ttd);
                    bitmap_ttd.compress(Bitmap.CompressFormat.JPEG, 100, byt);
                    ttd_byte = byt.toByteArray();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        btnSimpan.setEnabled(false);
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgFile = new byte[][]{ktp_byte, selfie_byte, ttd_byte};
                GlobalVariables.perReregData.put("privy_status", status);
                GlobalVariables.perReregData.put("identity_card", no_ktp);
                GlobalVariables.perReregData.put("code", code);
                GlobalVariables.perReregData.put("mother_maiden_name", motherName);
                GlobalVariables.perReregData.put("clients_job_position", jobPosition);
                GlobalVariables.perReregData.put("average_transaction_id", avgTrans);
                DataPart[] datas = new DataPart[]{
                        new DataPart("ktp.jpg", ktp_byte, "image/jpeg"),
                        new DataPart("selfie.jpg",selfie_byte, "image/jpeg"),
                        new DataPart("spesimen.jpg", ttd_byte, "image/jpeg")
                };
                GlobalVariables.perReregDataFileArray.put("imgFile[]", datas);

//                GlobalVariables.perReregDataFile.put("imgFile[]", new DataPart("ktp.jpg", ktp_byte, "image/jpeg"));
//                GlobalVariables.perReregDataFile.put("imgFile[]", new DataPart("selfie.jpg",selfie_byte, "image/jpeg"));
//                GlobalVariables.perReregDataFile.put("imgFile[]", new DataPart("spesimen.jpg", ttd_byte, "image/jpeg"));
                reregistDocument();
            }
        });

        cekButtonRotate();
    }

    private void reregistDocument(){
        new AlertDialog.Builder(PRTypeUpTwoActivity.this)
                .setTitle("Konfirmasi")
                .setIcon(R.drawable.logo)
                .setMessage(getString(R.string.create_doc_confirmation))
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        dialog.show();
                        viewModel.reregistType2(prefManager.getUid(), prefManager.getToken(), "perorangan");
                        viewModel.getResultReregistType2().observe(PRTypeUpTwoActivity.this, showResult);
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

    private Observer<JSONObject> showResult = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    //String msg = result.getString("message");
                    String msg = getString(R.string.reupload_success);
                    //new
                    GlobalVariables.perReregData.clear();
                    GlobalVariables.perReregDataFile.clear();
                    GlobalVariables.perReregDataFileArray.clear();
                    new Fungsi(PRTypeUpTwoActivity.this).showMessage(msg);
                    dialog.cancel();
                    Intent intent = new Intent(PRTypeUpTwoActivity.this, InVerificationProcessActivity.class);
                    new Routes(PRTypeUpTwoActivity.this).moveInFinish(intent);
                }else if(result.getInt("code") == 400 || result.getBoolean("status") == false){
                    String msg = getString(R.string.reupload_failed);
                    dialog.cancel();
                    new AlertDialog.Builder(PRTypeUpTwoActivity.this)
                            .setTitle("Pemberitahuan")
                            .setIcon(R.drawable.logo)
                            .setMessage("• " + msg)
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            }).create().show();
                }else{
                    String msg = getString(R.string.reupload_failed);
                    dialog.cancel();
                    new AlertDialog.Builder(PRTypeUpTwoActivity.this)
                            .setTitle("Pemberitahuan")
                            .setIcon(R.drawable.logo)
                            .setMessage("• " + msg)
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            }).create().show();
                }
                dialog.cancel();
            } catch (JSONException e) {
                e.printStackTrace();
                String msg = getString(R.string.reupload_failed);
                dialog.cancel();
                new AlertDialog.Builder(PRTypeUpTwoActivity.this)
                        .setTitle("Pemberitahuan")
                        .setIcon(R.drawable.logo)
                        .setMessage("• " + msg)
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        }).create().show();
//                new Routes(PRTypeUpOneActivity.this).moveOut();
            }
        }
    };

    public void clearMasterList(){
        listJobPosition.clear();listJobPositionID.clear();
        listAvgTrans.clear();listAvgTransID.clear();
    }

    private void cekButtonRotate(){
        if(bitmap_ktp != null){btnr_ktp.setVisibility(View.VISIBLE);}else{btnr_ktp.setVisibility(View.INVISIBLE);}
        if(bitmap_selfie != null){btnr_selfie.setVisibility(View.VISIBLE);}else{btnr_selfie.setVisibility(View.INVISIBLE);}
        if(bitmap_ttd != null){btnr_ttd.setVisibility(View.VISIBLE);}else{btnr_ttd.setVisibility(View.INVISIBLE);}
    }

    boolean mothernameisvalid = false;
    public void cekMotherName(String text){
        if(TextUtils.isEmpty(text)){
            edit_mothername.setError(getString(R.string.cannotnull));
            mothernameisvalid = false;
        }else{
            edit_mothername.setError(null);
            mothernameisvalid = true;
        }
    }

    boolean ktpisvalid = false;
    public void cekKTP(String ktp){
        if(TextUtils.isEmpty(ktp)){
            edit_ktp.setError(getString(R.string.cannotnull));
            ktpisvalid = false;
        }else{
            if(ktp.length() < 16){
                edit_ktp.setError(getString(R.string.min_digit_ktp));
                ktpisvalid = false;
            }else if(ktp.length() == 16){
                edit_ktp.setError(null);
                ktpisvalid = true;
            }
        }
    }

    boolean allisfilled = false;
    private void cekDone(){
        if(mothernameisvalid && !motherName.isEmpty() && ktpisvalid && !str_ktp.isEmpty() && !str_selfie.isEmpty() && !str_ttd.isEmpty()){
            allisfilled = true;
        }else{
            allisfilled = false;
        }

        btnSimpan.setEnabled(allisfilled);
    }

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };


    private Observer<JSONObject> showJobPosition = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    JSONObject jobRes = result.getJSONObject("result");
                    JSONArray jar = jobRes.getJSONArray("position");
                    for(int i = 0; i < jar.length(); i++){
                        listJobPosition.add(jar.getJSONObject(i).getString("name"));
                        listJobPositionID.add(jar.getJSONObject(i).getString("id"));
                        if(jar.getJSONObject(i).getString("id").equalsIgnoreCase(jobPosition)){
                            txt_job_position.getEditText().setText(jar.getJSONObject(i).getString("name"));
                        }
                    }
                    ArrayAdapter adapter = new ArrayAdapter(PRTypeUpTwoActivity.this, R.layout.support_simple_spinner_dropdown_item, listJobPosition);
                    auto_job_position.setAdapter(adapter);
                    auto_job_position.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                            jobPosition = listJobPositionID.get(x).toString();
                            Log.e("jobPosition", jobPosition);
                            txt_job_position.setError(null);
                        }
                    });
                }else{
                }
                dialog.cancel();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private Observer<JSONObject> showAvgTrans = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    JSONObject jobRes = result.getJSONObject("result");
                    JSONArray jar = jobRes.getJSONArray("average");
                    for(int i = 0; i < jar.length(); i++){
                        listAvgTrans.add(jar.getJSONObject(i).getString("name"));
                        listAvgTransID.add(jar.getJSONObject(i).getString("id"));
                        if(jar.getJSONObject(i).getString("id").equalsIgnoreCase(avgTrans)){
                            txt_avg_transaction.getEditText().setText(jar.getJSONObject(i).getString("name"));
                        }
                    }
                    ArrayAdapter adapter = new ArrayAdapter(PRTypeUpTwoActivity.this, R.layout.support_simple_spinner_dropdown_item, listAvgTrans);
                    auto_avg_transaction.setAdapter(adapter);
                    auto_avg_transaction.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                            avgTrans = listAvgTransID.get(x).toString();
                            Log.e("avgTrans", avgTrans);
                            txt_avg_transaction.setError(null);
                        }
                    });
                }else{
                }
                dialog.cancel();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    int PICK_IMAGE_REQUEST = 0;
    private void chooseFileConfirmation(final String PICK_IMAGE_TYPE){
        int permission = ActivityCompat.checkSelfPermission(PRTypeUpTwoActivity.this, Manifest.permission.CAMERA);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PRTypeUpTwoActivity.this, PERMISSIONS_STORAGE, 1);
        }else{
            new AlertDialog.Builder(PRTypeUpTwoActivity.this)
                    .setTitle("Konfirmasi")
                    .setIcon(R.drawable.ic_document_photo_circle)
                    .setMessage("Metode pengambilan foto apa yang ingin digunakan?")
                    .setCancelable(true)
                    .setPositiveButton("KAMERA", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            final Dialog mdialog = new Dialog(PRTypeUpTwoActivity.this);
                            LayoutInflater inflater = LayoutInflater.from(PRTypeUpTwoActivity.this);
                            View dialogView = null;
                            Button btnNext = null;

                            if(PICK_IMAGE_TYPE == PICK_TYPE_KTP){
                                PICK_IMAGE_REQUEST = PICK_KTP_CAM;
                                dialogView = inflater.inflate(R.layout.dialog_pra_foto_ktp, null);
                                btnNext = dialogView.findViewById(R.id.btn_next_dial_pfk);
                            }else if(PICK_IMAGE_TYPE == PICK_TYPE_SELFIE){
                                PICK_IMAGE_REQUEST = PICK_SELFIE_CAM;
                                dialogView = inflater.inflate(R.layout.dialog_pra_foto_wajah, null);
                                btnNext = dialogView.findViewById(R.id.btn_next_dial_pfw);
                            }else if(PICK_IMAGE_TYPE == PICK_TYPE_TTD){
                                PICK_IMAGE_REQUEST = PICK_TTD_CAM;
                                dialogView = inflater.inflate(R.layout.dialog_pra_foto_ttd, null);
                                btnNext = dialogView.findViewById(R.id.btn_next_dial_pft);
                            }
                            dialogInterface.cancel();

                            mdialog.setContentView(dialogView);
                            mdialog.setCancelable(true);
                            mdialog.show();

                            btnNext.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showCameraCapture(PICK_IMAGE_REQUEST, PICK_IMAGE_TYPE);
                                    mdialog.cancel();
                                }
                            });

                        }
                    })
                    .setNeutralButton("BATAL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogs, int which) {
                            dialogs.cancel();
                        }
                    })
                    .create()
                    .show();
        }

    }

    String currentPhotoPath = "";
    String imageFileName = "";
    private void showCameraCapture(int PICK_IMAGE_REQUEST, String PICK_IMAGE_TYPE) {
        imageFileName = PICK_IMAGE_TYPE+".jpg";
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File storageDir = null;
        File file = null;

        try{
            storageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),"/avantee/");
            if (!storageDir.exists()) {
                storageDir.mkdirs();
            }
            file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/avantee/", imageFileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            Log.e("Path Foto", file+"");
        }catch (Exception e) {
            e.printStackTrace();
        }

        Uri imgUri = FileProvider.getUriForFile(PRTypeUpTwoActivity.this, getApplicationContext().getPackageName()+".fileprovider", file);
        cameraIntent.putExtra("return-data", true);
        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        cameraIntent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 512);
        cameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        cameraIntent.putExtra(MediaStore.Images.Media.IS_PENDING, 1);
        startActivityForResult(cameraIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri filePath = data.getData();
                Log.e("UriPath", filePath.toString());
                try {
                    //mengambil gambar dari Gallery
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    // 640 adalah resolusi tertinggi setelah image di resize, bisa di ganti.
                    bitmap = f.getResizedBitmap(bitmap, MAX_SIZE);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, BITMAP_SIZE, bytes);
                    if (requestCode == PICK_KTP) {
                        bitmap_ktp = bitmap;
                        imgr_ktp.setImageBitmap(bitmap_ktp);
                        decoded_ktp = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
                        ktp_byte = bytes.toByteArray();
                        str_ktp = f.getStringImage(decoded_ktp);
                        Log.e("str_ktp", str_ktp);
                        txt_ktp.setText(filePath.getLastPathSegment() + ".jpg");
                    } else if (requestCode == PICK_SELFIE) {
                        bitmap_selfie = bitmap;
                        imgr_selfie.setImageBitmap(bitmap_selfie);
                        decoded_selfie = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
                        selfie_byte = bytes.toByteArray();
                        str_selfie = f.getStringImage(decoded_selfie);
                        txt_selfie.setText(filePath.getLastPathSegment() + ".jpg");
                    } else if (requestCode == PICK_TTD) {
                        bitmap_ttd = bitmap;
                        imgr_ttd.setImageBitmap(bitmap_ttd);
                        decoded_ttd = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
                        ttd_byte = bytes.toByteArray();
                        str_ttd = f.getStringImage(decoded_ttd);
                        txt_ttd.setText(filePath.getLastPathSegment() + ".jpg");
                    }


                    ////new
                    if (requestCode == PICK_KTP_CAM) {
                        performCrop(filePath, CROP_KTP);
                    }
                    else if (requestCode == PICK_SELFIE_CAM) {
                        performCrop(filePath, CROP_SELFIE);
                    }
                    else if (requestCode == PICK_TTD_CAM) {
                        performCrop(filePath, CROP_TTD);
                    }

                    else if (requestCode == CROP_KTP) {
                        bitmap = f.getResizedBitmap(bitmap, MAX_SIZE);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, BITMAP_SIZE, bytes);
                        bitmap_ktp = bitmap;
                        decoded_ktp = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
                        imgr_ktp.setImageBitmap(bitmap_ktp);
                        ktp_byte = bytes.toByteArray();
                        str_ktp = f.getStringImage(decoded_ktp);
                        txt_ktp.setText(filePath.getLastPathSegment());
                    } else if (requestCode == CROP_SELFIE) {
                        bitmap = f.getResizedBitmap(bitmap, MAX_SIZE);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, BITMAP_SIZE, bytes);
                        bitmap_selfie = bitmap;
                        decoded_selfie = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
                        imgr_selfie.setImageBitmap(bitmap_selfie);
                        selfie_byte = bytes.toByteArray();
                        str_selfie = f.getStringImage(decoded_selfie);
                        txt_selfie.setText(filePath.getLastPathSegment());
                    } else if (requestCode == CROP_TTD) {
                        bitmap = f.getResizedBitmap(bitmap, MAX_SIZE);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, BITMAP_SIZE, bytes);
                        bitmap_ttd = bitmap;
                        decoded_ttd = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
                        imgr_ttd.setImageBitmap(bitmap_ttd);
                        ttd_byte = bytes.toByteArray();
                        str_ttd = f.getStringImage(decoded_ttd);
                        txt_ttd.setText(filePath.getLastPathSegment());
                    }
                    ////new


                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                File path = null;
                File file = null;
                path = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                file = new File(path, "/avantee/" + imageFileName);

                Uri filePath = FileProvider.getUriForFile(PRTypeUpTwoActivity.this, getApplicationContext().getPackageName() + ".fileprovider", file);
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    if (bitmap == null) {
                        f.showMessage(getString(R.string.must_portrait));
                    } else {
                        ////new
                        if (requestCode == PICK_KTP_CAM) {
                            performCrop(filePath, CROP_KTP);
                        } else if (requestCode == PICK_SELFIE_CAM) {
                            performCrop(filePath, CROP_SELFIE);
                        } else if (requestCode == PICK_TTD_CAM) {
                            performCrop(filePath, CROP_TTD);
                        }

                        else if (requestCode == CROP_KTP) {
                            bitmap = f.getResizedBitmap(bitmap, MAX_SIZE);
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, BITMAP_SIZE, bytes);
                            bitmap_ktp = bitmap;
                            decoded_ktp = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
                            imgr_ktp.setImageBitmap(bitmap_ktp);
                            ktp_byte = bytes.toByteArray();
                            str_ktp = f.getStringImage(decoded_ktp);
                            txt_ktp.setText(filePath.getLastPathSegment());
                        } else if (requestCode == CROP_SELFIE) {
                            bitmap = f.getResizedBitmap(bitmap, MAX_SIZE);
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, BITMAP_SIZE, bytes);
                            bitmap_selfie = bitmap;
                            decoded_selfie = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
                            imgr_selfie.setImageBitmap(bitmap_selfie);
                            selfie_byte = bytes.toByteArray();
                            str_selfie = f.getStringImage(decoded_selfie);
                            txt_selfie.setText(filePath.getLastPathSegment());
                        } else if (requestCode == CROP_TTD) {
                            bitmap = f.getResizedBitmap(bitmap, MAX_SIZE);
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, BITMAP_SIZE, bytes);
                            bitmap_ttd = bitmap;
                            decoded_ttd = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
                            imgr_ttd.setImageBitmap(bitmap_ttd);
                            ttd_byte = bytes.toByteArray();
                            str_ttd = f.getStringImage(decoded_ttd);
                            txt_ttd.setText(filePath.getLastPathSegment());
                        }
                        ////new

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else if (requestCode == 2296) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (!Environment.isExternalStorageManager()) {
                    Toast.makeText(PRTypeUpTwoActivity.this, "Avantee Lender Apps membutuhkan ijin akses penyimpanan HP!", Toast.LENGTH_SHORT).show();
                }
            }
        }
        cekButtonRotate();
        cekView();
        cekDone();
    }

    private void performCrop(Uri picUri, int PIC_CROP){
        try {
            grantUriPermission("com.android.camera",picUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(picUri, "image/*");
            cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            cropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            cropIntent.putExtra("crop", "true");
            if(PIC_CROP == CROP_SELFIE){
                cropIntent.putExtra("aspectX", 3);
                cropIntent.putExtra("aspectY", 4);
                cropIntent.putExtra("outputX", 300);
                cropIntent.putExtra("outputY", 400);
            }else{
                cropIntent.putExtra("aspectX", 4);
                cropIntent.putExtra("aspectY", 3);
                cropIntent.putExtra("outputX", 400);
                cropIntent.putExtra("outputY", 300);
            }
            cropIntent.putExtra("return-data", true);
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
            startActivityForResult(cropIntent, PIC_CROP);
        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Device tidak support untuk memotong gambar.";
            Toast toast = Toast.makeText(PRTypeUpTwoActivity.this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void cekView(){
        if(!str_ktp.equalsIgnoreCase("")){
            cv_ktp.setCardBackgroundColor(ContextCompat.getColor(PRTypeUpTwoActivity.this, R.color.white));
            img_ktp.setImageDrawable(ContextCompat.getDrawable(PRTypeUpTwoActivity.this, R.drawable.ic_picture_taken));
            img_cancelktp.setVisibility(View.VISIBLE);
        }else{
            cv_ktp.setCardBackgroundColor(ContextCompat.getColor(PRTypeUpTwoActivity.this, R.color.lightest_neutral));
            img_ktp.setImageDrawable(ContextCompat.getDrawable(PRTypeUpTwoActivity.this, R.drawable.ic_take_picture));
            txt_ktp.setText(getString(R.string.take_photo_of_ktp));
            img_cancelktp.setVisibility(View.GONE);
        }

        if(!str_selfie.equalsIgnoreCase("")){
            cv_selfie.setCardBackgroundColor(ContextCompat.getColor(PRTypeUpTwoActivity.this, R.color.white));
            img_selfie.setImageDrawable(ContextCompat.getDrawable(PRTypeUpTwoActivity.this, R.drawable.ic_picture_taken));
            img_cancelselfie.setVisibility(View.VISIBLE);
        }else{
            cv_selfie.setCardBackgroundColor(ContextCompat.getColor(PRTypeUpTwoActivity.this, R.color.lightest_neutral));
            img_selfie.setImageDrawable(ContextCompat.getDrawable(PRTypeUpTwoActivity.this, R.drawable.ic_take_picture));
            txt_selfie.setText(getString(R.string.take_photo_of_selfie));
            img_cancelselfie.setVisibility(View.GONE);
        }

        if(!str_ttd.equalsIgnoreCase("")){
            cv_ttd.setCardBackgroundColor(ContextCompat.getColor(PRTypeUpTwoActivity.this, R.color.white));
            img_ttd.setImageDrawable(ContextCompat.getDrawable(PRTypeUpTwoActivity.this, R.drawable.ic_picture_taken));
            img_cancelttd.setVisibility(View.VISIBLE);
        }else{
            cv_ttd.setCardBackgroundColor(ContextCompat.getColor(PRTypeUpTwoActivity.this, R.color.lightest_neutral));
            img_ttd.setImageDrawable(ContextCompat.getDrawable(PRTypeUpTwoActivity.this, R.drawable.ic_take_picture));
            txt_ttd.setText(getString(R.string.take_photo_of_spesimenttd));
            img_cancelttd.setVisibility(View.GONE);
        }
    }

    public void logout() {
        // LOGOUT: GET method to server through endpoint
        dialog.show();
        viewModel.logout(prefManager.getUid(), prefManager.getToken());
        viewModel.getLogoutResult().observe(PRTypeUpTwoActivity.this, checkLogout);
    }

    private Observer<String> checkLogout = new Observer<String>() {
        @Override
        public void onChanged(String result) {
            if(result.equals("ok")) {
                dialog.cancel();
                Intent intent = new Intent(PRTypeUpTwoActivity.this, WalkthroughActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                new Routes(PRTypeUpTwoActivity.this).moveOutIntent(intent);
            }else{
                dialog.cancel();
                new Fungsi().showMessage(result);
            }
        }
    };


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            new Routes(PRTypeUpTwoActivity.this).moveOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new Routes(PRTypeUpTwoActivity.this).moveOut();
    }

}