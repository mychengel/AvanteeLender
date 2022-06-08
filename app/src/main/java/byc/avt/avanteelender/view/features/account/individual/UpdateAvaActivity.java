package byc.avt.avanteelender.view.features.account.individual;

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
import android.graphics.Camera;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.Policy;
import java.util.Objects;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.helper.Routes;
import byc.avt.avanteelender.model.DataPart;
import byc.avt.avanteelender.model.UserData;
import byc.avt.avanteelender.view.MainActivity;
import byc.avt.avanteelender.view.auth.InVerificationProcessActivity;
import byc.avt.avanteelender.view.auth.RegistrationFormActivity;
import byc.avt.avanteelender.view.auth.SignersCheckActivity;
import byc.avt.avanteelender.view.misc.OTPActivity;
import byc.avt.avanteelender.view.misc.OTPSettingsActivity;
import byc.avt.avanteelender.viewmodel.AuthenticationViewModel;

public class UpdateAvaActivity extends AppCompatActivity {

    private PrefManager prefManager;
    Fungsi f = new Fungsi(UpdateAvaActivity.this);
    private AuthenticationViewModel viewModel;
    Toolbar toolbar;
    GlobalVariables gv;
    private Dialog dialog;
    private Button btn_simpan, btn_rotate_ava;
    private TextInputLayout editNewPhone;
    private CheckBox cb;
    private LinearLayout lin;
    String noHp = "";
    boolean isChangeNoHp = false;
    TextView txt_ava;
    ImageView img_ava, img_ava_indi, img_cancel_ava;

    CardView cv_ava;
    byte[] ava_byte = null;
    Bitmap bitmap, bitmap_ava, decoded_ava;
    String str_ava = "";
    int PICK_AVA = 1, PICK_AVA_CAM = 2;
    String PICK_TYPE_AVA = "ava";
    int BITMAP_SIZE = 60, MAX_SIZE = 640, CROP_AVA = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_ava);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        gv.profileDataFile.clear();
        prefManager = PrefManager.getInstance(UpdateAvaActivity.this);
        toolbar = findViewById(R.id.tb_update_ava);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_back_24px);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        dialog = GlobalVariables.loadingDialog(UpdateAvaActivity.this);
        viewModel = new ViewModelProvider(UpdateAvaActivity.this).get(AuthenticationViewModel.class);
        cv_ava = findViewById(R.id.cv_take_ava_update_ava);
        editNewPhone = findViewById(R.id.edit_phone_number_update_ava);
        cb = findViewById(R.id.cb_update_no_hp_update_ava);
        lin = findViewById(R.id.lin_update_no_hp_update_ava);
        txt_ava = findViewById(R.id.txt_take_ava_update_ava);
        img_ava = findViewById(R.id.img_take_ava_update_ava);
        img_ava_indi = findViewById(R.id.img_ava_show_update_ava);
        img_cancel_ava = findViewById(R.id.img_cancel_take_ava_update_ava);

        btn_rotate_ava = findViewById(R.id.btn_rotate_ava_update_ava);
        btn_rotate_ava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    bitmap_ava = f.rotateImage(bitmap_ava);
                    ByteArrayOutputStream byt = new ByteArrayOutputStream();
                    img_ava_indi.setImageBitmap(bitmap_ava);
                    bitmap_ava.compress(Bitmap.CompressFormat.JPEG, 100, byt);
                    ava_byte = byt.toByteArray();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    lin.setVisibility(View.VISIBLE);
                    isChangeNoHp = true;
                }else{
                    lin.setVisibility(View.GONE);
                    isChangeNoHp = false;
                }
            }
        });

        cv_ava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(str_ava.equalsIgnoreCase("")){
                    chooseFileConfirmation(PICK_TYPE_AVA);
                }
            }
        });

        img_cancel_ava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_ava = "";
                ava_byte = null;
                cekView();
                cekDone();
            }
        });

        btn_simpan = findViewById(R.id.btn_save_update_ava);
        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isChangeNoHp){
                    noHp = editNewPhone.getEditText().getText().toString().trim();
                    if(noHp.isEmpty()){
                        editNewPhone.setError(getString(R.string.cannotnull));
                    }else{
                        editNewPhone.setError(null);
                        confirmSave();
                    }
                }else{
                    noHp = gv.NO_HP;
                    editNewPhone.setError(null);
                    confirmSave();
                }
            }
        });
        cekButtonRotate();
    }

    private void cekButtonRotate(){
        if(bitmap_ava != null){btn_rotate_ava.setVisibility(View.VISIBLE);}else{btn_rotate_ava.setVisibility(View.INVISIBLE);}
    }

    private void confirmSave(){
        gv.profileData.put("handphone", noHp);
        if(str_ava.isEmpty()){
        }else{
            gv.profileDataFile.put("pic_user", new DataPart("ava.jpg", ava_byte, "image/jpeg"));
        }

        dialog.show();
        viewModel.updateProfile(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultUpdateProfile().observe(UpdateAvaActivity.this, showResult);
    }

    private Observer<JSONObject> showResult = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getBoolean("status") == true){
                    dialog.cancel();
                    if(result.isNull("result")){
                        new Fungsi(UpdateAvaActivity.this).showMessage(result.getString("msg"));
                        confirmLogin();
                    }else{
                        Intent intent = new Intent(UpdateAvaActivity.this, OTPSettingsActivity.class);
                        intent.putExtra("type", "profile");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        new Fungsi(UpdateAvaActivity.this).showMessage(result.getJSONObject("result").getString("messages"));
                        new Routes(UpdateAvaActivity.this).moveInFinish(intent);
                    }
                }else{
                    new Fungsi(UpdateAvaActivity.this).showMessage(result.getString("msg"));
                    dialog.cancel();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                new Fungsi(UpdateAvaActivity.this).showMessage(getString(R.string.update_profile_failed));
                dialog.cancel();
            }
            dialog.cancel();
        }
    };

    boolean allisfilled = true;
    private void cekDone(){
        btn_simpan.setEnabled(allisfilled);
    }

    int PICK_IMAGE_REQUEST = 0;
    private void chooseFileConfirmation(final String PICK_IMAGE_TYPE){
        int permission = ActivityCompat.checkSelfPermission(UpdateAvaActivity.this, Manifest.permission.CAMERA);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(UpdateAvaActivity.this, PERMISSIONS_STORAGE, 1);
        }else{
            new AlertDialog.Builder(UpdateAvaActivity.this)
                    .setTitle("Konfirmasi")
                    .setIcon(R.drawable.ic_document_photo_circle)
                    .setMessage("Metode pengambilan foto apa yang ingin digunakan?")
                    .setCancelable(true)
                    .setPositiveButton("KAMERA", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            if(PICK_IMAGE_TYPE == PICK_TYPE_AVA){
                                PICK_IMAGE_REQUEST = PICK_AVA_CAM;
                            }
                            dialogInterface.cancel();

                            final Dialog dialog = new Dialog(UpdateAvaActivity.this);
                            LayoutInflater inflater = LayoutInflater.from(UpdateAvaActivity.this);
                            View dialogView = inflater.inflate(R.layout.dialog_pra_foto_wajah, null);
                            dialog.setContentView(dialogView);
                            dialog.setCancelable(true);
                            dialog.show();
                            final Button btnNext = dialogView.findViewById(R.id.btn_next_dial_pfw);
                            btnNext.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showCameraCapture(PICK_IMAGE_REQUEST, PICK_IMAGE_TYPE);
                                    dialog.cancel();
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

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    private void cekView(){
        if(!str_ava.equalsIgnoreCase("")){
            cv_ava.setCardBackgroundColor(ContextCompat.getColor(UpdateAvaActivity.this, R.color.white));
            img_ava.setImageDrawable(ContextCompat.getDrawable(UpdateAvaActivity.this, R.drawable.ic_picture_taken));
            img_cancel_ava.setVisibility(View.VISIBLE);
        }else{
            cv_ava.setCardBackgroundColor(ContextCompat.getColor(UpdateAvaActivity.this, R.color.lightest_neutral));
            img_ava.setImageDrawable(ContextCompat.getDrawable(UpdateAvaActivity.this, R.drawable.ic_take_picture));
            txt_ava.setText(getString(R.string.take_ava_photo));
            img_cancel_ava.setVisibility(View.GONE);
        }
    }

    String imageFileName = "";
    private void showCameraCapture(int PICK_IMAGE_REQUEST, String PICK_IMAGE_TYPE) {
        imageFileName = PICK_IMAGE_TYPE+".jpg";
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File storageDir = null;
        File file = null;

        try{
            storageDir = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),"/avantee/");
            if (!storageDir.exists()) {
                storageDir.mkdirs();
            }
            file = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/avantee/", imageFileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            Log.e("Path Foto", file+"");
        }catch (Exception e) {
            e.printStackTrace();
        }

        Uri imgUri = FileProvider.getUriForFile(UpdateAvaActivity.this, getApplicationContext().getPackageName()+".fileprovider", file);
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
                    if (requestCode == PICK_AVA) {
                        bitmap_ava = bitmap;
                        img_ava_indi.setImageBitmap(bitmap_ava);
                        decoded_ava = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
                        ava_byte = bytes.toByteArray();
                        str_ava = f.getStringImage(decoded_ava);
                        Log.e("str_ava", str_ava);
                        txt_ava.setText(filePath.getLastPathSegment() + ".jpg");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                File path = null;
                File file = null;

                path = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                file = new File(path, "/avantee/"+imageFileName);

                Uri filePath = FileProvider.getUriForFile(UpdateAvaActivity.this, getApplicationContext().getPackageName() + ".fileprovider", file);
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    if(bitmap == null){
                        f.showMessage(getString(R.string.must_portrait));
                    }else {
                        ///new
                        if (requestCode == PICK_AVA_CAM) {
                            bitmap = f.getResizedBitmap(bitmap, MAX_SIZE);
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, BITMAP_SIZE, bytes);
                            bitmap_ava = bitmap;
                            img_ava_indi.setImageBitmap(bitmap_ava);
                            decoded_ava = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
                            ava_byte = bytes.toByteArray();
                            str_ava = f.getStringImage(decoded_ava);
                            txt_ava.setText(filePath.getLastPathSegment());
                        }
                        ///new

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else if (requestCode == 2296) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (!Environment.isExternalStorageManager()) {
                    Toast.makeText(UpdateAvaActivity.this, "Avantee Lender Apps membutuhkan ijin akses penyimpanan HP!", Toast.LENGTH_SHORT).show();
                }
            }
        }
        cekButtonRotate();
        cekView();
        cekDone();
    }

    private void performCrop(Uri picUri, int PIC_CROP){
        try {
            UpdateAvaActivity.this.grantUriPermission("com.android.camera",picUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(picUri, "image/*");
            cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            cropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 300);
            cropIntent.putExtra("outputY", 300);
            cropIntent.putExtra("return-data", true);
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
            startActivityForResult(cropIntent, PIC_CROP);
        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Device tidak support untuk memotong gambar.";
            Toast toast = Toast.makeText( UpdateAvaActivity.this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void confirmLogin() {
        // POST to server through endpoint
        dialog.show();
        viewModel.login(prefManager.getEmail(), prefManager.getPassword());
        viewModel.getLoginResult().observe(UpdateAvaActivity.this, checkSuccess);
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
                            if(res.isNull("privy_status")){
                                Log.e("PrivyStatus", "Aman");
                                if(res.isNull("suratkuasa")){
                                    Log.e("TTDSuratKuasa", "Aman");
                                    if(res.isNull("suratperjanjian")){
                                        // Masuk DASHBOARD
                                        Log.e("TTDSuratPK", "Aman");
                                        i = new Intent(UpdateAvaActivity.this, MainActivity.class);
                                        i.putExtra("dest","1");
                                    }else{
                                        msg = res.getJSONObject("suratperjanjian").getString("msg");
                                        f.showMessage(msg);
                                        i = new Intent(UpdateAvaActivity.this, SignersCheckActivity.class);
                                        i.putExtra("doc_type", "Surat Perjanjian");
                                        //diarahkan untuk ttd surat perjanjian kerja sama
                                    }
                                }else{
                                    msg = res.getJSONObject("suratkuasa").getString("msg");
                                    f.showMessage(msg);
                                    i = new Intent(UpdateAvaActivity.this, SignersCheckActivity.class);
                                    i.putExtra("doc_type", "Surat Kuasa");
                                    //diarahkan untuk ttd surat kuasa
                                }
                            }else{
                                msg = res.getJSONObject("privy_status").getString("msg");
                                i = new Intent(UpdateAvaActivity.this, InVerificationProcessActivity.class);
                                //f.showMessage(msg);
                            }
                        }else{
                            i = new Intent(UpdateAvaActivity.this, RegistrationFormActivity.class);
                        }
                    }else{
                        i = new Intent(UpdateAvaActivity.this, OTPActivity.class);
                    }
                    //Routing
                    new Routes(UpdateAvaActivity.this).moveInFinish(i);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            new Routes(UpdateAvaActivity.this).moveOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new Routes(UpdateAvaActivity.this).moveOut();
    }
}