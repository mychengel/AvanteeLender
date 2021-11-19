package byc.avt.avanteelender.view.auth.prvrejection;

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
import android.provider.Settings;
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

public class PRTypeUpTwoInsActivity extends AppCompatActivity {

    private AuthenticationViewModel viewModel;
    private MasterDataViewModel viewModel2;
    private PrefManager prefManager;
    private Dialog dialog;
    private Toolbar toolbar;
    GlobalVariables gv;
    Fungsi f = new Fungsi(PRTypeUpTwoInsActivity.this);

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prtype_up_two_ins);
        GlobalVariables.insReregData.clear();
        GlobalVariables.insReregDataFile.clear();
        viewModel = new ViewModelProvider(PRTypeUpTwoInsActivity.this).get(AuthenticationViewModel.class);
        viewModel2 = new ViewModelProvider(PRTypeUpTwoInsActivity.this).get(MasterDataViewModel.class);
        prefManager = PrefManager.getInstance(PRTypeUpTwoInsActivity.this);
        toolbar = findViewById(R.id.toolbar_prtype_up_two_ins);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_back_24px);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dialog = GlobalVariables.loadingDialog(PRTypeUpTwoInsActivity.this);

        cv_ktp = findViewById(R.id.cv_take_ktp_prtype_up_two_ins);

        cv_selfie = findViewById(R.id.cv_take_selfie_prtype_up_two_ins);

        edit_ktp = findViewById(R.id.edit_ktp_number_prtype_up_two_ins);
        txt_ktp = findViewById(R.id.txt_take_ktp_prtype_up_two_ins);
        txt_selfie = findViewById(R.id.txt_take_selfie_prtype_up_two_ins);
        img_ktp = findViewById(R.id.img_take_ktp_prtype_up_two_ins);
        img_cancelktp =findViewById(R.id.img_cancel_take_ktp_prtype_up_two_ins);
        img_selfie = findViewById(R.id.img_take_selfie_prtype_up_two_ins);
        img_cancelselfie = findViewById(R.id.img_cancel_take_selfie_prtype_up_two_ins);
        txtInfo = findViewById(R.id.txt_info_prtype_up_two_ins);
        txtInfo.setText(getString(R.string.lets_get_know_eo));
        btnSimpan = findViewById(R.id.btn_simpan_prtype_up_two_ins);

        Intent i = getIntent();
        try {
            result = new JSONObject(i.getStringExtra("rJob"));
            status = result.getString("msg").toLowerCase();
            msg = result.getJSONObject("reason").getString("reason");
            code = result.getJSONObject("reason").getString("code");
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

        imgr_ktp = findViewById(R.id.img_ktp_prtype_up_two_ins);
        btnr_ktp = findViewById(R.id.btn_ktp_prtype_up_two_ins);
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

        imgr_selfie = findViewById(R.id.img_selfie_prtype_up_two_ins);
        btnr_selfie = findViewById(R.id.btn_selfie_prtype_up_two_ins);
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

        btnSimpan.setEnabled(false);
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgFile = new byte[][]{ktp_byte, selfie_byte, ttd_byte};
                GlobalVariables.insReregData.put("privy_status", status);
                GlobalVariables.insReregData.put("identity_card", no_ktp);
                GlobalVariables.insReregData.put("code", code);
                GlobalVariables.insReregDataFile.put("imgFile[]", new DataPart("ktp.jpg", ktp_byte, "image/jpeg"));
                GlobalVariables.insReregDataFile.put("imgFile[]", new DataPart("selfie.jpg",selfie_byte, "image/jpeg"));
                reregistDocument();
            }
        });

        checkStorageAccess();
        cekButtonRotate();
    }

    private void reregistDocument(){
        new AlertDialog.Builder(PRTypeUpTwoInsActivity.this)
                .setTitle("Konfirmasi")
                .setIcon(R.drawable.logo)
                .setMessage(getString(R.string.create_doc_confirmation))
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        dialog.show();
                        viewModel.reregistPrvp001(prefManager.getUid(), prefManager.getToken(), "institusi");
                        viewModel.getResultReregistPrvp001().observe(PRTypeUpTwoInsActivity.this, showResult);
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
                    GlobalVariables.insReregData.clear();
                    GlobalVariables.insReregDataFile.clear();
                    new Fungsi(PRTypeUpTwoInsActivity.this).showMessage(msg);
                    dialog.cancel();
                    Intent intent = new Intent(PRTypeUpTwoInsActivity.this, InVerificationProcessActivity.class);
                    new Routes(PRTypeUpTwoInsActivity.this).moveInFinish(intent);
                }else if(result.getInt("code") == 400 || result.getBoolean("status") == false){
                    String msg = getString(R.string.reupload_failed);
                    dialog.cancel();
                    new AlertDialog.Builder(PRTypeUpTwoInsActivity.this)
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
                    new AlertDialog.Builder(PRTypeUpTwoInsActivity.this)
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
                new AlertDialog.Builder(PRTypeUpTwoInsActivity.this)
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

    private void cekButtonRotate(){
        if(bitmap_ktp != null){btnr_ktp.setVisibility(View.VISIBLE);}else{btnr_ktp.setVisibility(View.INVISIBLE);}
        if(bitmap_selfie != null){btnr_selfie.setVisibility(View.VISIBLE);}else{btnr_selfie.setVisibility(View.INVISIBLE);}
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
        if(ktpisvalid && !str_ktp.isEmpty() && !str_selfie.isEmpty()){
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

    int PICK_IMAGE_REQUEST = 0;
    private void chooseFileConfirmation(final String PICK_IMAGE_TYPE){
        int permission = ActivityCompat.checkSelfPermission(PRTypeUpTwoInsActivity.this, Manifest.permission.CAMERA);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PRTypeUpTwoInsActivity.this, PERMISSIONS_STORAGE, 1);
        }else{
            new AlertDialog.Builder(PRTypeUpTwoInsActivity.this)
                    .setTitle("Konfirmasi")
                    .setIcon(R.drawable.ic_document_photo_circle)
                    .setMessage("Metode pengambilan foto apa yang ingin digunakan?")
                    .setCancelable(true)
                    .setPositiveButton("KAMERA", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            final Dialog mdialog = new Dialog(PRTypeUpTwoInsActivity.this);
                            LayoutInflater inflater = LayoutInflater.from(PRTypeUpTwoInsActivity.this);
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
                    .setNegativeButton("GALERI", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int PICK_IMAGE_REQUEST = 0;
                            if(PICK_IMAGE_TYPE == PICK_TYPE_KTP){
                                PICK_IMAGE_REQUEST = PICK_KTP;
                            }else if(PICK_IMAGE_TYPE == PICK_TYPE_SELFIE){
                                PICK_IMAGE_REQUEST = PICK_SELFIE;
                            }
                            dialog.cancel();
                            showGallery(PICK_IMAGE_REQUEST);
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

        Uri imgUri = FileProvider.getUriForFile(PRTypeUpTwoInsActivity.this, getApplicationContext().getPackageName()+".fileprovider", file);
        cameraIntent.putExtra("return-data", true);
        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        cameraIntent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 512);
        cameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        cameraIntent.putExtra(MediaStore.Images.Media.IS_PENDING, 1);
        startActivityForResult(cameraIntent, PICK_IMAGE_REQUEST);
    }

    private void showGallery(int PICK_IMAGE_REQUEST) {
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        Intent chooser = Intent.createChooser(galleryIntent, "Select Picture");
        startActivityForResult(chooser, PICK_IMAGE_REQUEST);
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
                    }

                    ////new
                    if (requestCode == PICK_KTP_CAM) {
                        performCrop(filePath, CROP_KTP);
                    }
                    else if (requestCode == PICK_SELFIE_CAM) {
                        performCrop(filePath, CROP_SELFIE);
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
                        imgr_selfie.setImageBitmap(decoded_selfie);
                        selfie_byte = bytes.toByteArray();
                        str_selfie = f.getStringImage(decoded_selfie);
                        txt_selfie.setText(filePath.getLastPathSegment());
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

                Uri filePath = FileProvider.getUriForFile(PRTypeUpTwoInsActivity.this, getApplicationContext().getPackageName() + ".fileprovider", file);
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
                            imgr_selfie.setImageBitmap(decoded_selfie);
                            selfie_byte = bytes.toByteArray();
                            str_selfie = f.getStringImage(decoded_selfie);
                            txt_selfie.setText(filePath.getLastPathSegment());
                        }
                        ////new

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else if (requestCode == 2296) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    storageAccess = true;
                } else {
                    storageAccess = false;
                    Toast.makeText(PRTypeUpTwoInsActivity.this, "Avantee Lender Apps membutuhkan ijin akses penyimpanan HP!", Toast.LENGTH_SHORT).show();
                    checkStorageAccess();
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
            Toast toast = Toast.makeText(PRTypeUpTwoInsActivity.this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    Boolean storageAccess = false;
    private void checkStorageAccess(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if(Environment.isExternalStorageManager()) {
                storageAccess = true;
            } else {
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse(String.format("package:%s", getPackageName())));
                    startActivityForResult(intent, 2296);
                } catch (Exception e) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    startActivityForResult(intent, 2296);
                }
            }

        }
    }

    private void cekView(){
        if(!str_ktp.equalsIgnoreCase("")){
            cv_ktp.setCardBackgroundColor(ContextCompat.getColor(PRTypeUpTwoInsActivity.this, R.color.white));
            img_ktp.setImageDrawable(ContextCompat.getDrawable(PRTypeUpTwoInsActivity.this, R.drawable.ic_picture_taken));
            img_cancelktp.setVisibility(View.VISIBLE);
        }else{
            cv_ktp.setCardBackgroundColor(ContextCompat.getColor(PRTypeUpTwoInsActivity.this, R.color.lightest_neutral));
            img_ktp.setImageDrawable(ContextCompat.getDrawable(PRTypeUpTwoInsActivity.this, R.drawable.ic_take_picture));
            txt_ktp.setText(getString(R.string.take_photo_of_ktp));
            img_cancelktp.setVisibility(View.GONE);
        }

        if(!str_selfie.equalsIgnoreCase("")){
            cv_selfie.setCardBackgroundColor(ContextCompat.getColor(PRTypeUpTwoInsActivity.this, R.color.white));
            img_selfie.setImageDrawable(ContextCompat.getDrawable(PRTypeUpTwoInsActivity.this, R.drawable.ic_picture_taken));
            img_cancelselfie.setVisibility(View.VISIBLE);
        }else{
            cv_selfie.setCardBackgroundColor(ContextCompat.getColor(PRTypeUpTwoInsActivity.this, R.color.lightest_neutral));
            img_selfie.setImageDrawable(ContextCompat.getDrawable(PRTypeUpTwoInsActivity.this, R.drawable.ic_take_picture));
            txt_selfie.setText(getString(R.string.take_photo_of_selfie));
            img_cancelselfie.setVisibility(View.GONE);
        }
    }

    public void logout() {
        // LOGOUT: GET method to server through endpoint
        dialog.show();
        viewModel.logout(prefManager.getUid(), prefManager.getToken());
        viewModel.getLogoutResult().observe(PRTypeUpTwoInsActivity.this, checkLogout);
    }

    private Observer<String> checkLogout = new Observer<String>() {
        @Override
        public void onChanged(String result) {
            if(result.equals("ok")) {
                dialog.cancel();
                Intent intent = new Intent(PRTypeUpTwoInsActivity.this, WalkthroughActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                new Routes(PRTypeUpTwoInsActivity.this).moveOutIntent(intent);
            }else{
                dialog.cancel();
                new Fungsi().showMessage(result);
            }
        }
    };


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            new Routes(PRTypeUpTwoInsActivity.this).moveOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new Routes(PRTypeUpTwoInsActivity.this).moveOut();
    }

}