package byc.avt.avanteelender.view.features.account.institution;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.textfield.TextInputLayout;

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
import byc.avt.avanteelender.view.features.account.individual.DataPendukungShowActivity;
import byc.avt.avanteelender.viewmodel.AuthenticationViewModel;
import byc.avt.avanteelender.viewmodel.MasterDataViewModel;

public class InsDataPendukungShowActivity extends AppCompatActivity {

    Fungsi f = new Fungsi(InsDataPendukungShowActivity.this);
    private MasterDataViewModel viewModel;
    private AuthenticationViewModel viewModel2;
    Button btn_save, btn_edit;
    boolean editIsOn = false;
    private Toolbar toolbar;
    JSONObject job, job2;
    private PrefManager prefManager;
    private Dialog dialog;
    GlobalVariables gv;

    TextInputLayout edit_ktp, edit_npwp;
    ImageView img_ktp, img_npwp, img_selfie;
    ImageView img_ktp_indi, img_npwp_indi, img_selfie_indi;
    CardView cv_ktp, cv_npwp, cv_selfie;

    String no_ktp = "", no_npwp = "";
    String name="", birthdate="", jabatan="", phone="", email="";
    byte[] ktp_byte = null, npwp_byte = null, selfie_byte = null;

    Bitmap bitmap, decoded_ktp, decoded_npwp, decoded_selfie;
    String str_ktp = "", str_npwp = "", str_selfie = "";
    int PICK_KTP = 1, PICK_NPWP = 2, PICK_SELFIE = 3, PICK_TTD = 4, PICK_KTP_CAM = 5, PICK_NPWP_CAM = 6, PICK_SELFIE_CAM = 7, PICK_TTD_CAM = 8;
    String PICK_TYPE_KTP = "ktp", PICK_TYPE_NPWP = "npwp", PICK_TYPE_SELFIE = "selfie", PICK_TYPE_TTD = "ttd";
    int BITMAP_SIZE = 60, MAX_SIZE = 512;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ins_data_pendukung_show);

        gv.insEditData.clear();
        gv.insEditDataFile.clear();
        viewModel = new ViewModelProvider(this).get(MasterDataViewModel.class);
        viewModel2 = new ViewModelProvider(this).get(AuthenticationViewModel.class);
        toolbar = findViewById(R.id.toolbar_ins_data_pendukung_show);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_back_24px);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        prefManager = PrefManager.getInstance(InsDataPendukungShowActivity.this);
        dialog = GlobalVariables.loadingDialog(InsDataPendukungShowActivity.this);

        edit_ktp = findViewById(R.id.edit_ktp_number_ins_data_pendukung_show);
        edit_npwp = findViewById(R.id.edit_npwp_number_ins_data_pendukung_show);
        cv_ktp = findViewById(R.id.cv_img_ktp_ins_data_pendukung_show);
        cv_npwp = findViewById(R.id.cv_img_npwp_ins_data_pendukung_show);
        cv_selfie = findViewById(R.id.cv_img_selfie_ins_data_pendukung_show);
        img_ktp = findViewById(R.id.img_ktp_ins_data_pendukung_show);
        img_npwp = findViewById(R.id.img_npwp_ins_data_pendukung_show);
        img_selfie = findViewById(R.id.img_selfie_ins_data_pendukung_show);
        img_ktp_indi = findViewById(R.id.img_ktp_indi_ins_data_pendukung_show);
        img_npwp_indi = findViewById(R.id.img_npwp_indi_ins_data_pendukung_show);
        img_selfie_indi = findViewById(R.id.img_selfie_indi_ins_data_pendukung_show);
        
        Intent i = getIntent();
        try {
            job = new JSONObject(i.getStringExtra("jobDocument"));
            job2 = new JSONObject(i.getStringExtra("jobNarahubungData"));
            edit_ktp.getEditText().setText(job2.getString("no_ktp"));
            no_ktp = job2.getString("no_ktp");
            cekKTP(no_ktp);
            edit_npwp.getEditText().setText(job.getString("no_npwp"));
            no_npwp = job.getString("no_npwp");
            cekNPWP(no_npwp);

            //variable that can't edited
            name = job2.getString("nama");
            birthdate = job2.getString("tanggal_lahir");
            jabatan = job2.getString("jabatan");
            phone = job2.getString("no_telepon");
            email = job2.getString("email");

            Glide.with(InsDataPendukungShowActivity.this).load(job2.getString("ktp"))
                .placeholder(R.drawable.ic_baseline_no_photography_24)
                .error(R.drawable.ic_baseline_no_photography_24)
                .dontAnimate()
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        img_ktp.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        return false;
                    }
                })
                .into(img_ktp);

            Glide.with(InsDataPendukungShowActivity.this).load(job2.getString("selfie"))
                .placeholder(R.drawable.ic_baseline_no_photography_24)
                .error(R.drawable.ic_baseline_no_photography_24)
                .dontAnimate()
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        img_selfie.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        return false;
                    }
                })
                .into(img_selfie);

            Glide.with(InsDataPendukungShowActivity.this).load(job.getString("npwp"))
                .placeholder(R.drawable.ic_baseline_no_photography_24)
                .error(R.drawable.ic_baseline_no_photography_24)
                .dontAnimate()
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        img_npwp.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        return false;
                    }
                })
                .into(img_npwp);
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
                //cekDone();
            }
        });

        edit_npwp.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                no_npwp = edit_npwp.getEditText().getText().toString().trim();
                cekNPWP(no_npwp);
                //cekDone();
            }
        });

        cv_ktp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFileConfirmation(PICK_TYPE_KTP);
            }
        });

        cv_npwp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFileConfirmation(PICK_TYPE_NPWP);
            }
        });

        cv_selfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFileConfirmation(PICK_TYPE_SELFIE);
            }
        });

        btn_save = findViewById(R.id.btn_save_ins_data_pendukung_show);
        btn_save.setEnabled(true);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmNext(v);
            }
        });

        btn_edit = findViewById(R.id.btn_ubah_ins_data_pendukung_show);
        btn_edit.setEnabled(true);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editIsOn = !editIsOn;
                editIsOn(editIsOn);
            }
        });

        editIsOn(false);

    }


    private void confirmNext(View v){
        if(ktpisvalid && npwpisvalid){
            gv.insEditData.put("nama",name);
            gv.insEditData.put("tanggal_lahir",birthdate);
            gv.insEditData.put("jabatan",jabatan);
            gv.insEditData.put("no_telepon",phone);
            gv.insEditData.put("email",email);

            gv.insEditData.put("no_ktp", no_ktp);
            gv.insEditData.put("no_npwp", no_npwp);
            if(!str_npwp.isEmpty()){gv.insEditDataFile.put("npwp", new DataPart("npwp.jpg", npwp_byte, "image/jpeg"));}
            if(!str_ktp.isEmpty()){gv.insEditDataFile.put("ktp", new DataPart("ktp.jpg", ktp_byte, "image/jpeg"));}
            if(!str_selfie.isEmpty()){gv.insEditDataFile.put("selfie", new DataPart("selfie.jpg", selfie_byte, "image/jpeg"));}
            gv.insEditData.put("event_name","informasi_dokumen");
            gv.insEditData.put("tipe_investor",prefManager.getClientType());
            updateDocument();
        }else{
        }
    }

    private void updateDocument(){
        new AlertDialog.Builder(InsDataPendukungShowActivity.this)
            .setTitle("Konfirmasi")
            .setIcon(R.drawable.logo)
            .setMessage(getString(R.string.update_doc_confirmation))
            .setCancelable(false)
            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                    dialog.show();
                    viewModel2.updateInstitutionDoc(prefManager.getUid(), prefManager.getToken());
                    viewModel2.getResultUpdateInstitutionDoc().observe(InsDataPendukungShowActivity.this, showResult);
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
                    //new Fungsi(InsDataPendukungShowActivity.this).showMessage(getString(R.string.success_update_data));
                    //dialog.cancel();
                    //new Routes(InsDataPendukungShowActivity.this).moveOut();
                }else{
                    new Fungsi(InsDataPendukungShowActivity.this).showMessage(getString(R.string.failed_update_data));
                    //dialog.cancel();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                String msg = getString(R.string.system_in_trouble);
                Log.e("ResponInsUpDoc", msg);
                new Fungsi(InsDataPendukungShowActivity.this).showMessage(msg);
                //dialog.cancel();
            }
            gv.insEditData.put("event_name","informasi_narahubung");
            viewModel2.updateInstitutionDoc(prefManager.getUid(), prefManager.getToken());
            viewModel2.getResultUpdateInstitutionDoc().observe(InsDataPendukungShowActivity.this, showResult2);
        }
    };

    private Observer<JSONObject> showResult2 = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    new Fungsi(InsDataPendukungShowActivity.this).showMessage(getString(R.string.success_update_data));
                    dialog.cancel();
                    new Routes(InsDataPendukungShowActivity.this).moveOut();
                }else{
                    JSONObject obj = result.getJSONObject("result");
                    String err = "";
                    err = obj.toString().replaceAll("\"", "");
                    err = err.replace("{", "");
                    err = err.replace("}", "");
                    //new Fungsi(InsDataPendukungShowActivity.this).showMessage(getString(R.string.failed_update_data));
                    new Fungsi(InsDataPendukungShowActivity.this).showMessageLong(err);
                    dialog.cancel();
                    new Routes(InsDataPendukungShowActivity.this).moveOut();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                String msg = getString(R.string.system_in_trouble);
                Log.e("ResponInsUpDoc2", msg);
                new Fungsi(InsDataPendukungShowActivity.this).showMessage(msg);
                dialog.cancel();
            }
        }
    };


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

    boolean npwpisvalid = false;
    public void cekNPWP(String npwp){
        if(TextUtils.isEmpty(npwp)){
            edit_npwp.setError(getString(R.string.cannotnull));
            npwpisvalid = false;
        }else{
            if(npwp.length() < 15){
                edit_npwp.setError(getString(R.string.min_digit_npwp));
                npwpisvalid = false;
            }else if(npwp.length() == 15){
                edit_npwp.setError(null);
                npwpisvalid = true;
            }
        }
    }

    public void editIsOn(boolean s){
        edit_ktp.setEnabled(s);
        edit_npwp.setEnabled(s);
        img_ktp.setEnabled(s);
        img_npwp.setEnabled(s);
        img_selfie.setEnabled(s);
        int elev = 0;
        if(s){
            elev = 8;
            img_ktp_indi.setVisibility(View.VISIBLE);
            img_npwp_indi.setVisibility(View.VISIBLE);
            img_selfie_indi.setVisibility(View.VISIBLE);
        }else{
            elev = 0;
            img_ktp_indi.setVisibility(View.GONE);
            img_npwp_indi.setVisibility(View.GONE);
            img_selfie_indi.setVisibility(View.GONE);
        }
        cv_ktp.setCardElevation(elev);
        cv_ktp.setEnabled(s);
        cv_npwp.setCardElevation(elev);
        cv_npwp.setEnabled(s);
        cv_selfie.setCardElevation(elev);
        cv_selfie.setEnabled(s);
        btn_save.setEnabled(s);
    }

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    int PICK_IMAGE_REQUEST = 0;
    private void chooseFileConfirmation(final String PICK_IMAGE_TYPE){
        int permission = ActivityCompat.checkSelfPermission(InsDataPendukungShowActivity.this, Manifest.permission.CAMERA);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(InsDataPendukungShowActivity.this, PERMISSIONS_STORAGE, 1);
        }else{
            new AlertDialog.Builder(InsDataPendukungShowActivity.this)
                    .setTitle("Konfirmasi")
                    .setIcon(R.drawable.ic_document_photo_circle)
                    .setMessage("Metode pengambilan foto apa yang ingin digunakan?")
                    .setCancelable(true)
                    .setPositiveButton("KAMERA", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(PICK_IMAGE_TYPE == PICK_TYPE_KTP){
                                PICK_IMAGE_REQUEST = PICK_KTP_CAM;
                            }else if(PICK_IMAGE_TYPE == PICK_TYPE_NPWP){
                                PICK_IMAGE_REQUEST = PICK_NPWP_CAM;
                            }else if(PICK_IMAGE_TYPE == PICK_TYPE_SELFIE){
                                PICK_IMAGE_REQUEST = PICK_SELFIE_CAM;
                            }else if(PICK_IMAGE_TYPE == PICK_TYPE_TTD){
                                PICK_IMAGE_REQUEST = PICK_TTD_CAM;
                            }
                            dialogInterface.cancel();
                            new AlertDialog.Builder(InsDataPendukungShowActivity.this)
                                    .setTitle("Pemberitahuan")
                                    .setIcon(R.drawable.ic_document_photo_circle)
                                    .setMessage(getString(R.string.req_doc))
                                    .setCancelable(true)
                                    .setPositiveButton("OK, ambil foto", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            showCameraCapture(PICK_IMAGE_REQUEST, PICK_IMAGE_TYPE);
                                        }
                                    })
                                    .create()
                                    .show();
                        }
                    })
//                .setPositiveButtonIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_document_photo_circle))
                    .setNegativeButton("GALERI", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int PICK_IMAGE_REQUEST = 0;
                            if(PICK_IMAGE_TYPE == PICK_TYPE_KTP){
                                PICK_IMAGE_REQUEST = PICK_KTP;
                            }else if(PICK_IMAGE_TYPE == PICK_TYPE_NPWP){
                                PICK_IMAGE_REQUEST = PICK_NPWP;
                            }else if(PICK_IMAGE_TYPE == PICK_TYPE_SELFIE){
                                PICK_IMAGE_REQUEST = PICK_SELFIE;
                            }else if(PICK_IMAGE_TYPE == PICK_TYPE_TTD){
                                PICK_IMAGE_REQUEST = PICK_TTD;
                            }
                            dialog.cancel();
                            showGallery(PICK_IMAGE_REQUEST);
                        }
                    })
//                .setNegativeButtonIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_picture_taken))
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
        if(Build.VERSION.SDK_INT >= 29){
            try {
                storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                file = File.createTempFile(
                        PICK_IMAGE_TYPE,  /* prefix */
                        ".jpg",         /* suffix */
                        storageDir     /* directory */
                );
                currentPhotoPath = file.getAbsolutePath();
                Log.e("takePhoto", currentPhotoPath);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }else{
            try{
                storageDir = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/avantee/");
                if (!storageDir.exists()) {
                    storageDir.mkdirs();
                }
                file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/avantee/", imageFileName);
                if (!file.exists()) {
                    file.createNewFile();
                }
                Log.e("Path Foto", file+"");
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        Uri imgUri = FileProvider.getUriForFile(InsDataPendukungShowActivity.this, getApplicationContext().getPackageName()+".fileprovider", file);
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
                    // 512 adalah resolusi tertinggi setelah image di resize, bisa di ganti.
                    bitmap = f.getResizedBitmap(bitmap, MAX_SIZE);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, BITMAP_SIZE, bytes);
                    if (requestCode == PICK_KTP) {
                        decoded_ktp = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
                        img_ktp.setImageBitmap(decoded_ktp);
                        ktp_byte = bytes.toByteArray();
                        //ktp_byte = f.getFileDataFromBitmap(getActivity(), decoded_ktp);
                        str_ktp = f.getStringImage(decoded_ktp);
                        Log.e("str_ktp", str_ktp);
                        //txt_ktp.setText(filePath.getLastPathSegment() + ".jpg");
                    } else if (requestCode == PICK_NPWP) {
                        decoded_npwp = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
                        img_npwp.setImageBitmap(decoded_npwp);
                        npwp_byte = bytes.toByteArray();
                        //npwp_byte = f.getFileDataFromBitmap(getActivity(), decoded_npwp);
                        str_npwp = f.getStringImage(decoded_npwp);
                        //txt_npwp.setText(filePath.getLastPathSegment() + ".jpg");
                    } else if (requestCode == PICK_SELFIE) {
                        decoded_selfie = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
                        img_selfie.setImageBitmap(decoded_selfie);
                        selfie_byte = bytes.toByteArray();
                        //selfie_byte = f.getFileDataFromBitmap(getActivity(), decoded_selfie);
                        str_selfie = f.getStringImage(decoded_selfie);
                        //txt_selfie.setText(filePath.getLastPathSegment() + ".jpg");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {

                File path = null;
                File file = null;
                if(Build.VERSION.SDK_INT >=29){
                    path = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    file = new File(currentPhotoPath);
                }else{
                    path = Environment.getExternalStorageDirectory();
                    file = new File(path, "/avantee/"+imageFileName);
                }

                Uri filePath = FileProvider.getUriForFile(InsDataPendukungShowActivity.this, getApplicationContext().getPackageName() + ".fileprovider", file);
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    bitmap = f.getResizedBitmap(bitmap, MAX_SIZE);
                    if(Build.VERSION.SDK_INT >=29){
                        bitmap = f.getRotateImage(currentPhotoPath, bitmap);
                    }else{
                        bitmap = f.getRotateImage(file.getAbsolutePath(), bitmap);
                    }
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, BITMAP_SIZE, bytes);
                    if (requestCode == PICK_KTP_CAM) {
                        decoded_ktp = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
                        img_ktp.setImageBitmap(decoded_ktp);
                        ktp_byte = bytes.toByteArray();
                        //ktp_byte = f.getFileDataFromBitmap(getActivity(), decoded_ktp);
                        Log.e("KTP Byte", ktp_byte + "");
                        str_ktp = f.getStringImage(decoded_ktp);
                        //txt_ktp.setText(filePath.getLastPathSegment());
                    } else if (requestCode == PICK_NPWP_CAM) {
                        decoded_npwp = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
                        img_npwp.setImageBitmap(decoded_npwp);
                        npwp_byte = bytes.toByteArray();
                        //npwp_byte = f.getFileDataFromBitmap(getActivity(), decoded_npwp);
                        str_npwp = f.getStringImage(decoded_npwp);
                        //txt_npwp.setText(filePath.getLastPathSegment());
                    } else if (requestCode == PICK_SELFIE_CAM) {
                        decoded_selfie = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
                        img_selfie.setImageBitmap(decoded_selfie);
                        selfie_byte = bytes.toByteArray();
                        //selfie_byte = f.getFileDataFromBitmap(getActivity(), decoded_selfie);
                        str_selfie = f.getStringImage(decoded_selfie);
                        //txt_selfie.setText(filePath.getLastPathSegment());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
//        cekView();
//        cekDone();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            new Routes(InsDataPendukungShowActivity.this).moveOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new Routes(InsDataPendukungShowActivity.this).moveOut();
    }

}