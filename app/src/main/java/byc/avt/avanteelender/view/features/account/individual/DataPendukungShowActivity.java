package byc.avt.avanteelender.view.features.account.individual;

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
import android.content.pm.ActivityInfo;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.helper.Routes;
import byc.avt.avanteelender.model.DataPart;
import byc.avt.avanteelender.viewmodel.AuthenticationViewModel;
import byc.avt.avanteelender.viewmodel.MasterDataViewModel;

public class DataPendukungShowActivity extends AppCompatActivity {

    Fungsi f = new Fungsi(DataPendukungShowActivity.this);
    private Toolbar toolbar;
    JSONObject job, job2;
    TextInputLayout edit_ktp, edit_npwp, edit_tgl_npwp;
    ImageView img_ktp, img_npwp, img_selfie, img_spesimen_ttd;
    ImageView img_ktp_indi, img_npwp_indi, img_selfie_indi, img_spesimen_ttd_indi;
    CheckBox cb_not_have_npwp;
    Button btn_save, btn_edit;
    boolean editIsOn = false;

    private MasterDataViewModel viewModel;
    private AuthenticationViewModel viewModel2;
    private PrefManager prefManager;
    private Dialog dialog;
    GlobalVariables gv;
    EditText edittext_tgl_npwp;
    CardView cv_ktp, cv_npwp, cv_selfie, cv_ttd;
    LinearLayout lin_npwp;
    boolean is_not_have_npwp = false, punya_npwp = false;
    String no_ktp = "", no_npwp = "", tgl_npwp = "";
    byte[] ktp_byte = null, npwp_byte = null, selfie_byte = null, ttd_byte = null;

    Bitmap bitmap, decoded_ktp, decoded_npwp, decoded_selfie, decoded_ttd;
    String str_ktp = "", str_npwp = "", str_selfie = "", str_ttd = "";
    int PICK_KTP = 1, PICK_NPWP = 2, PICK_SELFIE = 3, PICK_TTD = 4, PICK_KTP_CAM = 5, PICK_NPWP_CAM = 6, PICK_SELFIE_CAM = 7, PICK_TTD_CAM = 8;
    String PICK_TYPE_KTP = "ktp", PICK_TYPE_NPWP = "npwp", PICK_TYPE_SELFIE = "selfie", PICK_TYPE_TTD = "ttd";
    int BITMAP_SIZE = 60, MAX_SIZE = 512;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_pendukung_show);
        gv.perEditData.clear();
        gv.perEditDataFile.clear();
        viewModel = new ViewModelProvider(this).get(MasterDataViewModel.class);
        viewModel2 = new ViewModelProvider(this).get(AuthenticationViewModel.class);
        prefManager = PrefManager.getInstance(DataPendukungShowActivity.this);
        dialog = GlobalVariables.loadingDialog(DataPendukungShowActivity.this);
        toolbar = findViewById(R.id.toolbar_ess_document_show);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_back_24px);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        edit_ktp = findViewById(R.id.edit_ktp_number_ess_document_show);
        edit_npwp = findViewById(R.id.edit_npwp_number_ess_document_show);
        edit_tgl_npwp = findViewById(R.id.edit_npwp_date_ess_document_show);
        cv_ktp = findViewById(R.id.cv_img_ktp_ess_document_show);
        cv_npwp = findViewById(R.id.cv_img_npwp_ess_document_show);
        cv_selfie = findViewById(R.id.cv_img_selfie_ess_document_show);
        cv_ttd = findViewById(R.id.cv_img_ttd_ess_document_show);
        cb_not_have_npwp = findViewById(R.id.cb_not_have_npwp_ess_document_show);
        img_ktp = findViewById(R.id.img_ktp_ess_document_show);
        img_npwp = findViewById(R.id.img_npwp_ess_document_show);
        img_selfie = findViewById(R.id.img_selfie_ess_document_show);
        img_spesimen_ttd = findViewById(R.id.img_ttd_ess_document_show);
        lin_npwp = findViewById(R.id.lin_npwp_ess_document_show);

        img_ktp_indi = findViewById(R.id.img_ktp_indi_ess_document_show);
        img_npwp_indi = findViewById(R.id.img_npwp_indi_ess_document_show);
        img_selfie_indi = findViewById(R.id.img_selfie_indi_ess_document_show);
        img_spesimen_ttd_indi = findViewById(R.id.img_ttd_indi_ess_document_show);

        Intent i = getIntent();
        try {
            job = new JSONObject(i.getStringExtra("jobEssDocument"));
            job2 = new JSONObject(i.getStringExtra("jobDataPribadi"));
            edit_ktp.getEditText().setText(job.getString("ktp_no"));
            no_ktp = job.getString("ktp_no");
            cekKTP(no_ktp);
            edit_npwp.getEditText().setText(job.getString("npwp_no"));
            if(job.getString("npwp_no").isEmpty() || job.getString("tanggal_daftar_npwp") == null){
                lin_npwp.setVisibility(View.GONE);
                cb_not_have_npwp.setChecked(true);
                is_not_have_npwp = true;
                punya_npwp = false;
            }else{
                punya_npwp = true;
                cb_not_have_npwp.setChecked(false);
                lin_npwp.setVisibility(View.VISIBLE);
                no_npwp = job.getString("npwp_no");
                is_not_have_npwp = false;
                cekNPWP(no_npwp);
                edit_tgl_npwp.getEditText().setText(f.dateStd(job.getString("tanggal_daftar_npwp")));
                tgl_npwp = f.dateStd(job.getString("tanggal_daftar_npwp"));
                Glide.with(DataPendukungShowActivity.this).load(job.getString("npwp_img"))
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
            }

            Glide.with(DataPendukungShowActivity.this).load(job.getString("ktp_img"))
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

            Glide.with(DataPendukungShowActivity.this).load(job2.getString("photo_selfie"))
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

            Glide.with(DataPendukungShowActivity.this).load(job.getString("spesimen_ttd_img"))
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
                            img_spesimen_ttd.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            return false;
                        }
                    })
                    .into(img_spesimen_ttd);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb_not_have_npwp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    lin_npwp.setVisibility(View.GONE);
                    is_not_have_npwp = true;
                }else{
                    lin_npwp.setVisibility(View.VISIBLE);
                    is_not_have_npwp = false;
                }
                //cekDone();
            }
        });

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

        edit_tgl_npwp.getEditText().setFocusable(false);
        edit_tgl_npwp.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
                builder.setTitleText("Pilih tanggal NPWP");
                builder.setSelection(Calendar.getInstance().getTimeInMillis());
                MaterialDatePicker picker = builder.build();
                picker.show(getSupportFragmentManager(),"NPWP");
                picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        tgl_npwp = sdf.format(selection);
                        edit_tgl_npwp.getEditText().setText(tgl_npwp);
                    }
                });
            }
        });

        cv_ktp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if(str_ktp.equalsIgnoreCase("")){
                    chooseFileConfirmation(PICK_TYPE_KTP);
                //}
            }
        });

        cv_npwp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if(str_npwp.equalsIgnoreCase("")){
                    chooseFileConfirmation(PICK_TYPE_NPWP);
                //}
            }
        });

        cv_selfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if(str_selfie.equalsIgnoreCase("")){
                    chooseFileConfirmation(PICK_TYPE_SELFIE);
                //}
            }
        });

        cv_ttd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if(str_ttd.equalsIgnoreCase("")){
                    chooseFileConfirmation(PICK_TYPE_TTD);
                //}
            }
        });


        btn_save = findViewById(R.id.btn_save_ess_document_show);
        btn_save.setEnabled(true);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmNext(v);
            }
        });

        btn_edit = findViewById(R.id.btn_ubah_ess_document_show);
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
        if(ktpisvalid){
//            try {
//                if(job.getString("ktp_no").equalsIgnoreCase(no_ktp)){
//
//                }else{
//                    gv.perEditData.put("no_ktp", no_ktp);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
            gv.perEditData.put("no_ktp", no_ktp);
            if(is_not_have_npwp){
                gv.perEditData.put("miliki_npwp", "0");
//                    gv.perRegData.put("tanggal_pendaftaran_npwp", "");
//                    gv.perRegData.put("no_npwp", "");
//                    gv.perRegData.put("npwp_img", null);
            }else{
                gv.perEditData.put("miliki_npwp", "1");
                gv.perEditData.put("tanggal_pendaftaran_npwp", tgl_npwp);
                gv.perEditData.put("no_npwp", no_npwp);
                if(!str_npwp.isEmpty()){gv.perEditDataFile.put("npwp_img", new DataPart("npwp.jpg", npwp_byte, "image/jpeg"));}
            }
            if(!str_ktp.isEmpty()){gv.perEditDataFile.put("ktp_img", new DataPart("ktp.jpg", ktp_byte, "image/jpeg"));}
            if(!str_selfie.isEmpty()){gv.perEditDataFile.put("selfie", new DataPart("selfie.jpg", selfie_byte, "image/jpeg"));}
            if(!str_ttd.isEmpty()){gv.perEditDataFile.put("spesimen_ttd", new DataPart("ttd.jpg", ttd_byte, "image/jpeg"));}

            gv.perEditData.put("event_name","data_dokumen");
            gv.perEditData.put("tipe_investor",prefManager.getClientType());

            updateDocument();
        }else{

        }
    }

    private void updateDocument(){
        new AlertDialog.Builder(DataPendukungShowActivity.this)
                .setTitle("Konfirmasi")
                .setIcon(R.drawable.logo)
                .setMessage(getString(R.string.update_doc_confirmation))
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        dialog.show();
                        viewModel2.updatePersonalDoc(prefManager.getUid(), prefManager.getToken());
                        viewModel2.getResultUpdatePersonalDoc().observe(DataPendukungShowActivity.this, showResult);
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
                    new Fungsi(DataPendukungShowActivity.this).showMessage(getString(R.string.success_update_data));
                    dialog.cancel();
                    new Routes(DataPendukungShowActivity.this).moveOut();
                }else{
                    new Fungsi(DataPendukungShowActivity.this).showMessage(getString(R.string.failed_update_data));
                    dialog.cancel();
                    //new Routes(PersonalDataShowActivity.this).moveOut();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                String msg = getString(R.string.system_in_trouble);
                Log.e("Respon per cr doc", msg);
                new Fungsi(DataPendukungShowActivity.this).showMessage(msg);
                dialog.cancel();
                //new Routes(PersonalDataShowActivity.this).moveOut();
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
        edit_tgl_npwp.setEnabled(s);
        img_ktp.setEnabled(s);
        img_npwp.setEnabled(s);
        img_selfie.setEnabled(s);
        img_spesimen_ttd.setEnabled(s);
        lin_npwp.setEnabled(s);
        if(punya_npwp){
            cb_not_have_npwp.setEnabled(false);
        }else{
            cb_not_have_npwp.setEnabled(s);
        }

        int elev = 0;
        if(s){
            elev = 8;
            img_ktp_indi.setVisibility(View.VISIBLE);
            img_npwp_indi.setVisibility(View.VISIBLE);
            img_selfie_indi.setVisibility(View.VISIBLE);
            img_spesimen_ttd_indi.setVisibility(View.VISIBLE);
        }else{
            elev = 0;
            img_ktp_indi.setVisibility(View.GONE);
            img_npwp_indi.setVisibility(View.GONE);
            img_selfie_indi.setVisibility(View.GONE);
            img_spesimen_ttd_indi.setVisibility(View.GONE);
        }
        cv_ktp.setCardElevation(elev);
        cv_ktp.setEnabled(s);
        cv_npwp.setCardElevation(elev);
        cv_npwp.setEnabled(s);
        cv_selfie.setCardElevation(elev);
        cv_selfie.setEnabled(s);
        cv_ttd.setCardElevation(elev);
        cv_ttd.setEnabled(s);
        btn_save.setEnabled(s);

    }

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    int PICK_IMAGE_REQUEST = 0;
    private void chooseFileConfirmation(final String PICK_IMAGE_TYPE){
        int permission = ActivityCompat.checkSelfPermission(DataPendukungShowActivity.this, Manifest.permission.CAMERA);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DataPendukungShowActivity.this, PERMISSIONS_STORAGE, 1);
        }else{
            new AlertDialog.Builder(DataPendukungShowActivity.this)
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
                            new AlertDialog.Builder(DataPendukungShowActivity.this)
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

        Uri imgUri = FileProvider.getUriForFile(DataPendukungShowActivity.this, getApplicationContext().getPackageName()+".fileprovider", file);
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
                        str_ktp = f.getStringImage(decoded_ktp);
                        Log.e("str_ktp", str_ktp);
                    } else if (requestCode == PICK_NPWP) {
                        decoded_npwp = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
                        img_npwp.setImageBitmap(decoded_npwp);
                        npwp_byte = bytes.toByteArray();
                        str_npwp = f.getStringImage(decoded_npwp);
                    } else if (requestCode == PICK_SELFIE) {
                        decoded_selfie = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
                        img_selfie.setImageBitmap(decoded_selfie);
                        selfie_byte = bytes.toByteArray();
                        str_selfie = f.getStringImage(decoded_selfie);
                    } else if (requestCode == PICK_TTD) {
                        decoded_ttd = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
                        img_spesimen_ttd.setImageBitmap(decoded_ttd);
                        ttd_byte = bytes.toByteArray();
                        str_ttd = f.getStringImage(decoded_ttd);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                File path = null;
                File file = null;
                path = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                file = new File(path, "/avantee/"+imageFileName);

                Uri filePath = FileProvider.getUriForFile(DataPendukungShowActivity.this, getApplicationContext().getPackageName() + ".fileprovider", file);
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    bitmap = f.getResizedBitmap(bitmap, MAX_SIZE);
                    bitmap = f.getRotateImage(bitmap);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, BITMAP_SIZE, bytes);
                    if (requestCode == PICK_KTP_CAM) {
                        decoded_ktp = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
                        img_ktp.setImageBitmap(decoded_ktp);
                        ktp_byte = bytes.toByteArray();
                        Log.e("KTP Byte", ktp_byte + "");
                        str_ktp = f.getStringImage(decoded_ktp);
                    } else if (requestCode == PICK_NPWP_CAM) {
                        decoded_npwp = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
                        img_npwp.setImageBitmap(decoded_npwp);
                        npwp_byte = bytes.toByteArray();
                        str_npwp = f.getStringImage(decoded_npwp);
                    } else if (requestCode == PICK_SELFIE_CAM) {
                        decoded_selfie = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
                        img_selfie.setImageBitmap(decoded_selfie);
                        selfie_byte = bytes.toByteArray();
                        str_selfie = f.getStringImage(decoded_selfie);
                    } else if (requestCode == PICK_TTD_CAM) {
                        decoded_ttd = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
                        img_spesimen_ttd.setImageBitmap(decoded_ttd);
                        ttd_byte = bytes.toByteArray();
                        str_ttd = f.getStringImage(decoded_ttd);
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
            finish();
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }
}