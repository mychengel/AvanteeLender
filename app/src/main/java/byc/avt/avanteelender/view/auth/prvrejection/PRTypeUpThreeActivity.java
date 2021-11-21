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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.helper.Routes;
import byc.avt.avanteelender.model.DataPart;
import byc.avt.avanteelender.view.auth.InVerificationProcessActivity;
import byc.avt.avanteelender.viewmodel.AuthenticationViewModel;

public class PRTypeUpThreeActivity extends AppCompatActivity {

    private AuthenticationViewModel viewModel;
    private PrefManager prefManager;
    private Dialog dialog;
    private Toolbar toolbar;
    GlobalVariables gv;
    Fungsi f = new Fungsi(PRTypeUpThreeActivity.this);

    String msg = "", handler0 = "", handler1 = "", code = "", status = "", category0 = "", category1 = "", ctype = "";
    JSONObject result;
    JSONArray handlers;
    TextView txtInfo;
    Button btnSimpan;
    TextView txt_filesupport, txt_filesupport2;
    CardView cv_filesupport, cv_filesupport2;
    ImageView img_filesupport, img_cancelfilesupport, img_filesupport2, img_cancelfilesupport2;

    byte[] filesupport_byte = null, filesupport2_byte = null;

    Bitmap bitmap, decoded_filesupport,decoded_filesupport2;
    String str_filesupport = "", str_filesupport2 = "";
    int PICK_PASSPORT = 1, PICK_PASSPORT_CAM = 2, PICK_PASSPORT2 = 3, PICK_PASSPORT2_CAM = 4;
    String PICK_TYPE_PASSPORT = "filesupport", PICK_TYPE_PASSPORT2 = "filesupport2";
    int BITMAP_SIZE = 60, MAX_SIZE = 640, CROP_PASSPORT = 101, CROP_PASSPORT2 = 102;

    Button btnr_filesupport, btnr_filesupport2;
    ImageView imgr_filesupport, imgr_filesupport2;
    Bitmap bitmap_filesupport, bitmap_filesupport2;
    String csc = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prtype_up_three);
        GlobalVariables.perReregData.clear();
        GlobalVariables.perReregDataFile.clear();
        GlobalVariables.perReregDataFileArray.clear();
        GlobalVariables.insReregData.clear();
        GlobalVariables.insReregDataFile.clear();
        GlobalVariables.insReregDataFileArray.clear();
        viewModel = new ViewModelProvider(PRTypeUpThreeActivity.this).get(AuthenticationViewModel.class);
        prefManager = PrefManager.getInstance(PRTypeUpThreeActivity.this);
        toolbar = findViewById(R.id.toolbar_prtypeupthree);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_back_24px);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dialog = GlobalVariables.loadingDialog(PRTypeUpThreeActivity.this);
        txtInfo = findViewById(R.id.txt_info_prtypeupthree);
        btnSimpan = findViewById(R.id.btn_simpan_prtypeupthree);
        ///1
        cv_filesupport = findViewById(R.id.cv_upload_filesupport_prtypeupthree);
        txt_filesupport = findViewById(R.id.txt_upload_filesupport_prtypeupthree);
        img_filesupport = findViewById(R.id.img_upload_filesupport_prtypeupthree);
        img_cancelfilesupport = findViewById(R.id.img_cancel_upload_filesupport_prtypeupthree);
        ///2
        cv_filesupport2 = findViewById(R.id.cv_upload_filesupport2_prtypeupthree);
        txt_filesupport2 = findViewById(R.id.txt_upload_filesupport2_prtypeupthree);
        img_filesupport2 = findViewById(R.id.img_upload_filesupport2_prtypeupthree);
        img_cancelfilesupport2 = findViewById(R.id.img_cancel_upload_filesupport2_prtypeupthree);

        Intent i = getIntent();
        try {
            result = new JSONObject(i.getStringExtra("rJob"));
            ctype = i.getStringExtra("cType");
            status = result.getString("msg").toLowerCase();
            msg = result.getJSONObject("reason").getString("reason");
            txtInfo.setText(msg);
            code = result.getJSONObject("reason").getString("code");
            handlers = result.getJSONObject("reason").getJSONArray("handlers");
            category0 = handlers.getJSONObject(0).getString("category");
            handler0 = handlers.getJSONObject(0).getString("handler");
            txt_filesupport.setText("Unggah "+handler0);
            category1 = handlers.getJSONObject(1).getString("category");
            handler1 = handlers.getJSONObject(1).getString("handler");
            txt_filesupport2.setText("Unggah "+handler1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cv_filesupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(str_filesupport.equalsIgnoreCase("")){
                    csc = handler0;
                    chooseFileConfirmation(PICK_TYPE_PASSPORT);
                }
            }
        });

        cv_filesupport2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(str_filesupport2.equalsIgnoreCase("")){
                    csc = handler1;
                    chooseFileConfirmation(PICK_TYPE_PASSPORT2);
                }
            }
        });

        ///1
        imgr_filesupport = findViewById(R.id.img_rotate_filesupport_prtypeupthree);
        btnr_filesupport = findViewById(R.id.btn_rotate_filesupport_prtypeupthree);
        btnr_filesupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    bitmap_filesupport = f.rotateImage(bitmap_filesupport);
                    ByteArrayOutputStream byt = new ByteArrayOutputStream();
                    imgr_filesupport.setImageBitmap(bitmap_filesupport);
                    bitmap_filesupport.compress(Bitmap.CompressFormat.JPEG, 100, byt);
                    filesupport_byte = byt.toByteArray();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        img_cancelfilesupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_filesupport = "";
                filesupport_byte = null;
                imgr_filesupport.setImageResource(R.drawable.ic_baseline_no_photography_24);
                btnr_filesupport.setVisibility(View.INVISIBLE);
                cekView();
                cekDone();
            }
        });

        ///2
        imgr_filesupport2 = findViewById(R.id.img_rotate_filesupport2_prtypeupthree);
        btnr_filesupport2 = findViewById(R.id.btn_rotate_filesupport2_prtypeupthree);
        btnr_filesupport2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    bitmap_filesupport2 = f.rotateImage(bitmap_filesupport2);
                    ByteArrayOutputStream byt = new ByteArrayOutputStream();
                    imgr_filesupport2.setImageBitmap(bitmap_filesupport2);
                    bitmap_filesupport2.compress(Bitmap.CompressFormat.JPEG, 100, byt);
                    filesupport2_byte = byt.toByteArray();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        img_cancelfilesupport2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_filesupport2 = "";
                filesupport2_byte = null;
                imgr_filesupport2.setImageResource(R.drawable.ic_baseline_no_photography_24);
                btnr_filesupport2.setVisibility(View.INVISIBLE);
                cekView();
                cekDone();
            }
        });

        btnSimpan.setEnabled(false);
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ctype.equalsIgnoreCase("perorangan")) {
                    GlobalVariables.perReregData.put("privy_status", status);
                    GlobalVariables.perReregData.put("code", code);
                    GlobalVariables.perReregData.put("category[0]", category0);
                    GlobalVariables.perReregData.put("category[1]", category1);
                    DataPart[] datas = new DataPart[]{
                            new DataPart("supportFile.jpg", filesupport_byte, "image/jpeg"),
                            new DataPart("supportFile2.jpg", filesupport2_byte, "image/jpeg")
                    };
                    GlobalVariables.perReregDataFileArray.put("supportFile[]", datas);
//                    GlobalVariables.perReregDataFile.put("supportFile[]", new DataPart("supportFile.jpg", filesupport_byte, "image/jpeg"));
//                    GlobalVariables.perReregDataFile.put("supportFile[]", new DataPart("supportFile2.jpg", filesupport2_byte, "image/jpeg"));
                }else{
                    GlobalVariables.insReregData.put("privy_status", status);
                    GlobalVariables.insReregData.put("code", code);
                    GlobalVariables.insReregData.put("category[0]", category0);
                    GlobalVariables.insReregData.put("category[1]", category1);
                    DataPart[] datas = new DataPart[]{
                            new DataPart("supportFile.jpg", filesupport_byte, "image/jpeg"),
                            new DataPart("supportFile2.jpg", filesupport2_byte, "image/jpeg")
                    };
                    GlobalVariables.insReregDataFileArray.put("supportFile[]", datas);
//                    GlobalVariables.insReregDataFile.put("supportFile[]", new DataPart("supportFile.jpg", filesupport_byte, "image/jpeg"));
//                    GlobalVariables.insReregDataFile.put("supportFile[]", new DataPart("supportFile2.jpg", filesupport2_byte, "image/jpeg"));
                }
                reregistDocument();
            }
        });

        checkStorageAccess();
        cekButtonRotate();
    }

    private void reregistDocument(){
        new AlertDialog.Builder(PRTypeUpThreeActivity.this)
                .setTitle("Konfirmasi")
                .setIcon(R.drawable.logo)
                .setMessage(getString(R.string.create_doc_confirmation))
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        dialog.show();
                        viewModel.reregistType3(prefManager.getUid(), prefManager.getToken(), ctype);
                        viewModel.getResultReregistType3().observe(PRTypeUpThreeActivity.this, showResult);
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
                    GlobalVariables.insReregData.clear();
                    GlobalVariables.insReregDataFile.clear();
                    GlobalVariables.insReregDataFileArray.clear();
                    new Fungsi(PRTypeUpThreeActivity.this).showMessage(msg);
                    dialog.cancel();
                    Intent intent = new Intent(PRTypeUpThreeActivity.this, InVerificationProcessActivity.class);
                    new Routes(PRTypeUpThreeActivity.this).moveInFinish(intent);
                }else if(result.getInt("code") == 400 || result.getBoolean("status") == false){
                    String msg = getString(R.string.reupload_failed);
                    dialog.cancel();
                    new AlertDialog.Builder(PRTypeUpThreeActivity.this)
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
                    new AlertDialog.Builder(PRTypeUpThreeActivity.this)
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
                new AlertDialog.Builder(PRTypeUpThreeActivity.this)
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
//                new Routes(PRTypeUpThreeActivity.this).moveOut();
            }
        }
    };

    private void cekButtonRotate(){
        if(bitmap_filesupport != null){btnr_filesupport.setVisibility(View.VISIBLE);}else{btnr_filesupport.setVisibility(View.INVISIBLE);}
        if(bitmap_filesupport2 != null){btnr_filesupport2.setVisibility(View.VISIBLE);}else{btnr_filesupport2.setVisibility(View.INVISIBLE);}
    }

    boolean allisfilled = false;
    private void cekDone(){
        if(!str_filesupport.isEmpty() && !str_filesupport2.isEmpty()){
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
        int permission = ActivityCompat.checkSelfPermission(PRTypeUpThreeActivity.this, Manifest.permission.CAMERA);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PRTypeUpThreeActivity.this, PERMISSIONS_STORAGE, 1);
        }else{
            new AlertDialog.Builder(PRTypeUpThreeActivity.this)
                    .setTitle("Konfirmasi")
                    .setIcon(R.drawable.ic_document_photo_circle)
                    .setMessage("Metode pengambilan foto apa yang ingin digunakan?")
                    .setCancelable(true)
                    .setPositiveButton("KAMERA", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            final Dialog mdialog = new Dialog(PRTypeUpThreeActivity.this);
                            LayoutInflater inflater = LayoutInflater.from(PRTypeUpThreeActivity.this);
//                            View dialogView = null;
//                            Button btnNext = null;

                            if(PICK_IMAGE_TYPE == PICK_TYPE_PASSPORT){
                                PICK_IMAGE_REQUEST = PICK_PASSPORT_CAM;
//                                dialogView = inflater.inflate(R.layout.dialog_pra_foto_ktp, null);
//                                btnNext = dialogView.findViewById(R.id.btn_next_dial_pfk);
                            }else if(PICK_IMAGE_TYPE == PICK_TYPE_PASSPORT2){
                                PICK_IMAGE_REQUEST = PICK_PASSPORT2_CAM;
//                                dialogView = inflater.inflate(R.layout.dialog_pra_foto_ktp, null);
//                                btnNext = dialogView.findViewById(R.id.btn_next_dial_pfk);
                            }
//                            else if(PICK_IMAGE_TYPE == PICK_TYPE_NPWP){
//                                PICK_IMAGE_REQUEST = PICK_NPWP_CAM;
//                                dialogView = inflater.inflate(R.layout.dialog_pra_foto_npwp, null);
//                                btnNext = dialogView.findViewById(R.id.btn_next_dial_pfn);
//                            }
                            dialogInterface.cancel();
                            showCameraCapture(PICK_IMAGE_REQUEST, PICK_IMAGE_TYPE);

//                            mdialog.setContentView(dialogView);
//                            mdialog.setCancelable(true);
//                            mdialog.show();

//                            btnNext.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    showCameraCapture(PICK_IMAGE_REQUEST, PICK_IMAGE_TYPE);
//                                    mdialog.cancel();
//                                }
//                            });

                        }
                    })
                    .setNegativeButton("GALERI", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int PICK_IMAGE_REQUEST = 0;
                            if(PICK_IMAGE_TYPE == PICK_TYPE_PASSPORT){
                                PICK_IMAGE_REQUEST = PICK_PASSPORT;
                            }else if(PICK_IMAGE_TYPE == PICK_TYPE_PASSPORT2){
                                PICK_IMAGE_REQUEST = PICK_PASSPORT2;
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

        Uri imgUri = FileProvider.getUriForFile(PRTypeUpThreeActivity.this, getApplicationContext().getPackageName()+".fileprovider", file);
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
                    if (requestCode == PICK_PASSPORT) {
                        bitmap_filesupport = bitmap;
                        imgr_filesupport.setImageBitmap(bitmap_filesupport);
                        decoded_filesupport = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
                        filesupport_byte = bytes.toByteArray();
                        str_filesupport = f.getStringImage(decoded_filesupport);
                        txt_filesupport.setText(filePath.getLastPathSegment() + ".jpg");
                    }else if (requestCode == PICK_PASSPORT2) {
                        bitmap_filesupport2 = bitmap;
                        imgr_filesupport2.setImageBitmap(bitmap_filesupport2);
                        decoded_filesupport2 = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
                        filesupport2_byte = bytes.toByteArray();
                        str_filesupport2 = f.getStringImage(decoded_filesupport2);
                        txt_filesupport2.setText(filePath.getLastPathSegment() + ".jpg");
                    }

                    ////new
                    if (requestCode == PICK_PASSPORT_CAM) {
                        performCrop(filePath, CROP_PASSPORT);
                    }

                    else if (requestCode == CROP_PASSPORT) {
                        bitmap = f.getResizedBitmap(bitmap, MAX_SIZE);
                        //ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, BITMAP_SIZE, bytes);
                        bitmap_filesupport = bitmap;
                        decoded_filesupport = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
                        imgr_filesupport.setImageBitmap(bitmap_filesupport);
                        filesupport_byte = bytes.toByteArray();
                        str_filesupport = f.getStringImage(decoded_filesupport);
                        txt_filesupport.setText(filePath.getLastPathSegment());
                    }

                    if (requestCode == PICK_PASSPORT2_CAM) {
                        performCrop(filePath, CROP_PASSPORT2);
                    }

                    else if (requestCode == CROP_PASSPORT2) {
                        bitmap = f.getResizedBitmap(bitmap, MAX_SIZE);
                        //ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, BITMAP_SIZE, bytes);
                        bitmap_filesupport2 = bitmap;
                        decoded_filesupport2 = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
                        imgr_filesupport2.setImageBitmap(bitmap_filesupport2);
                        filesupport2_byte = bytes.toByteArray();
                        str_filesupport2 = f.getStringImage(decoded_filesupport2);
                        txt_filesupport2.setText(filePath.getLastPathSegment());
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

                Uri filePath = FileProvider.getUriForFile(PRTypeUpThreeActivity.this, getApplicationContext().getPackageName() + ".fileprovider", file);
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    if (bitmap == null) {
                        f.showMessage(getString(R.string.must_portrait));
                    } else {
                        ////new
                        if (requestCode == PICK_PASSPORT_CAM) {
                            performCrop(filePath, CROP_PASSPORT);
                        }

                        else if (requestCode == CROP_PASSPORT) {
                            bitmap = f.getResizedBitmap(bitmap, MAX_SIZE);
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, BITMAP_SIZE, bytes);
                            bitmap_filesupport = bitmap;
                            decoded_filesupport = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
                            imgr_filesupport.setImageBitmap(bitmap_filesupport);
                            filesupport_byte = bytes.toByteArray();
                            str_filesupport = f.getStringImage(decoded_filesupport);
                            txt_filesupport.setText(filePath.getLastPathSegment());
                        }

                        if (requestCode == PICK_PASSPORT2_CAM) {
                            performCrop(filePath, CROP_PASSPORT2);
                        }

                        else if (requestCode == CROP_PASSPORT2) {
                            bitmap = f.getResizedBitmap(bitmap, MAX_SIZE);
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, BITMAP_SIZE, bytes);
                            bitmap_filesupport2 = bitmap;
                            decoded_filesupport2 = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
                            imgr_filesupport2.setImageBitmap(bitmap_filesupport2);
                            filesupport2_byte = bytes.toByteArray();
                            str_filesupport2 = f.getStringImage(decoded_filesupport2);
                            txt_filesupport2.setText(filePath.getLastPathSegment());
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
                    Toast.makeText(PRTypeUpThreeActivity.this, "Avantee Lender Apps membutuhkan ijin akses penyimpanan HP!", Toast.LENGTH_SHORT).show();
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
            if(csc.toLowerCase().contains("selfie")){
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
            Toast toast = Toast.makeText(PRTypeUpThreeActivity.this, errorMessage, Toast.LENGTH_SHORT);
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
        if(!str_filesupport.equalsIgnoreCase("")){
            cv_filesupport.setCardBackgroundColor(ContextCompat.getColor(PRTypeUpThreeActivity.this, R.color.white));
            img_filesupport.setImageDrawable(ContextCompat.getDrawable(PRTypeUpThreeActivity.this, R.drawable.ic_picture_taken));
            img_cancelfilesupport.setVisibility(View.VISIBLE);
        }else {
            cv_filesupport.setCardBackgroundColor(ContextCompat.getColor(PRTypeUpThreeActivity.this, R.color.lightest_neutral));
            img_filesupport.setImageDrawable(ContextCompat.getDrawable(PRTypeUpThreeActivity.this, R.drawable.ic_take_picture));
            txt_filesupport.setText("Unggah " +handler0);
            img_cancelfilesupport.setVisibility(View.GONE);
        }

        if(!str_filesupport2.equalsIgnoreCase("")){
            cv_filesupport2.setCardBackgroundColor(ContextCompat.getColor(PRTypeUpThreeActivity.this, R.color.white));
            img_filesupport2.setImageDrawable(ContextCompat.getDrawable(PRTypeUpThreeActivity.this, R.drawable.ic_picture_taken));
            img_cancelfilesupport2.setVisibility(View.VISIBLE);
        }else {
            cv_filesupport2.setCardBackgroundColor(ContextCompat.getColor(PRTypeUpThreeActivity.this, R.color.lightest_neutral));
            img_filesupport2.setImageDrawable(ContextCompat.getDrawable(PRTypeUpThreeActivity.this, R.drawable.ic_take_picture));
            txt_filesupport2.setText("Unggah " +handler1);
            img_cancelfilesupport2.setVisibility(View.GONE);
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            new Routes(PRTypeUpThreeActivity.this).moveOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new Routes(PRTypeUpThreeActivity.this).moveOut();
    }

}