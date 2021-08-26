package byc.avt.avanteelender.view.fragment.registration.individual;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.MultiPermissionRequest;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.helper.Routes;
import byc.avt.avanteelender.helper.VolleyMultipartRequest;
import byc.avt.avanteelender.helper.receiver.OTPReceiver;
import byc.avt.avanteelender.intro.WalkthroughActivity;
import byc.avt.avanteelender.model.DataPart;
import byc.avt.avanteelender.view.auth.LoginActivity;
import byc.avt.avanteelender.view.auth.RegistrationFormActivity;
import byc.avt.avanteelender.view.auth.SignersCheckActivity;
import byc.avt.avanteelender.view.features.account.individual.DataPendukungShowActivity;
import byc.avt.avanteelender.view.misc.OTPActivity;
import byc.avt.avanteelender.view.misc.OTPDocActivity;
import byc.avt.avanteelender.view.others.SettingActivity;
import byc.avt.avanteelender.viewmodel.AuthenticationViewModel;
import byc.avt.avanteelender.viewmodel.MasterDataViewModel;

import static androidx.core.graphics.TypefaceCompatUtil.getTempFile;

/**
 * A simple {@link Fragment} subclass.
 */
public class DocumentsFragment extends Fragment {

    public DocumentsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_documents, container, false);
    }

    private MasterDataViewModel viewModel;
    private AuthenticationViewModel viewModel2;
    private PrefManager prefManager;
    private Dialog dialog;
    GlobalVariables gv;
    Fungsi f = new Fungsi(getActivity());
    TextInputLayout edit_ktp, edit_npwp, edit_tgl_npwp;
    EditText edittext_tgl_npwp;
    TextView txt_ktp, txt_npwp, txt_selfie, txt_ttd;
    CardView cv_ktp, cv_npwp, cv_selfie, cv_ttd;
    LinearLayout lin_npwp;
    ImageView img_ktp, img_cancelktp, img_npwp, img_cancelnpwp, img_selfie, img_cancelselfie, img_ttd, img_cancelttd;
    boolean is_not_have_npwp = false;
    CheckBox cb_not_have_npwp;
    Button btn_next;
    String no_ktp = "", no_npwp = "", tgl_npwp = "";
    byte[] ktp_byte = null, npwp_byte = null, selfie_byte = null, ttd_byte = null;

    Bitmap bitmap, decoded_ktp, decoded_npwp, decoded_selfie, decoded_ttd;
    String str_ktp = "", str_npwp = "", str_selfie = "", str_ttd = "";
    int PICK_KTP = 1, PICK_NPWP = 2, PICK_SELFIE = 3, PICK_TTD = 4, PICK_KTP_CAM = 5, PICK_NPWP_CAM = 6, PICK_SELFIE_CAM = 7, PICK_TTD_CAM = 8;
    String PICK_TYPE_KTP = "ktp", PICK_TYPE_NPWP = "npwp", PICK_TYPE_SELFIE = "selfie", PICK_TYPE_TTD = "ttd";
    int BITMAP_SIZE = 60, MAX_SIZE = 640, CROP_KTP = 101, CROP_NPWP = 102, CROP_SELFIE = 103, CROP_TTD = 104;;

    Button btnr_ktp, btnr_npwp, btnr_selfie, btnr_ttd;
    ImageView imgr_ktp, imgr_npwp, imgr_selfie, imgr_ttd;
    Bitmap bitmap_ktp, bitmap_npwp, bitmap_selfie, bitmap_ttd;

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MasterDataViewModel.class);
        viewModel2 = new ViewModelProvider(this).get(AuthenticationViewModel.class);
        prefManager = PrefManager.getInstance(getActivity());
        dialog = GlobalVariables.loadingDialog(requireActivity());
        edit_tgl_npwp = view.findViewById(R.id.edit_npwp_date_fr_documents);
        edittext_tgl_npwp = view.findViewById(R.id.editText_npwp_date_fr_documents);
        cv_ktp = view.findViewById(R.id.cv_take_ktp_fr_documents);
        cv_npwp = view.findViewById(R.id.cv_take_npwp_fr_documents);
        cv_selfie = view.findViewById(R.id.cv_take_selfie_fr_documents);
        cv_ttd = view.findViewById(R.id.cv_take_ttd_fr_documents);
        edit_ktp = view.findViewById(R.id.edit_ktp_number_fr_documents);
        edit_npwp = view.findViewById(R.id.edit_npwp_number_fr_documents);
        txt_ktp = view.findViewById(R.id.txt_take_ktp_fr_documents);
        txt_npwp = view.findViewById(R.id.txt_take_npwp_fr_documents);
        txt_selfie = view.findViewById(R.id.txt_take_selfie_fr_documents);
        txt_ttd = view.findViewById(R.id.txt_take_ttd_fr_documents);
        img_ktp = view.findViewById(R.id.img_take_ktp_fr_documents);
        img_cancelktp = view.findViewById(R.id.img_cancel_take_ktp_fr_document);
        img_npwp = view.findViewById(R.id.img_take_npwp_fr_documents);
        img_cancelnpwp = view.findViewById(R.id.img_cancel_take_npwp_fr_document);
        img_selfie = view.findViewById(R.id.img_take_selfie_fr_documents);
        img_cancelselfie = view.findViewById(R.id.img_cancel_take_selfie_fr_document);
        img_ttd = view.findViewById(R.id.img_take_ttd_fr_documents);
        img_cancelttd = view.findViewById(R.id.img_cancel_take_ttd_fr_document);
        lin_npwp = view.findViewById(R.id.lin_npwp_fr_documents);
        cb_not_have_npwp = view.findViewById(R.id.cb_not_have_npwp_fr_documents);
        cb_not_have_npwp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    lin_npwp.setVisibility(View.GONE);
                    is_not_have_npwp = true;
//                    gv.perRegDataFile.remove("npwp_img");
//                    gv.perRegData.remove("no_npwp");
//                    gv.perRegData.remove("tanggal_pendaftaran_npwp");
                }else{
                    lin_npwp.setVisibility(View.VISIBLE);
                    is_not_have_npwp = false;
                }
                cekDone();
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
                cekDone();
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
                cekDone();
            }
        });

        edittext_tgl_npwp.setFocusable(false);
        edittext_tgl_npwp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
                builder.setTitleText("Pilih tanggal NPWP");
                builder.setSelection(Calendar.getInstance().getTimeInMillis());
                MaterialDatePicker picker = builder.build();
                picker.show(getActivity().getSupportFragmentManager(),"NPWP");
                picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        tgl_npwp = sdf.format(selection);
                        edittext_tgl_npwp.setText(tgl_npwp);
                    }
                });
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

        cv_npwp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(str_npwp.equalsIgnoreCase("")){
                    chooseFileConfirmation(PICK_TYPE_NPWP);
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
                cekView();
                cekDone();
            }
        });

        img_cancelnpwp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_npwp = "";
                npwp_byte = null;
                cekView();
                cekDone();
            }
        });

        img_cancelselfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_selfie = "";
                selfie_byte = null;
                cekView();
                cekDone();
            }
        });

        img_cancelttd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_ttd = "";
                ttd_byte = null;
                cekView();
                cekDone();
            }
        });

        btn_next = view.findViewById(R.id.btn_next_fr_documents);
        btn_next.setEnabled(false);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gv.stPerDocument = true;
                gv.perRegData.put("no_ktp", no_ktp);
                if(is_not_have_npwp){
                    gv.perRegData.put("miliki_npwp", "0");
//                    gv.perRegData.put("tanggal_pendaftaran_npwp", "");
//                    gv.perRegData.put("no_npwp", "");
//                    gv.perRegData.put("npwp_img", null);
                }else{
                    gv.perRegData.put("miliki_npwp", "1");
                    gv.perRegData.put("tanggal_pendaftaran_npwp", tgl_npwp);
                    gv.perRegData.put("no_npwp", no_npwp);
                    gv.perRegDataFile.put("npwp_img", new DataPart("npwp.jpg", npwp_byte, "image/jpeg"));
                }
                gv.perRegDataFile.put("ktp_img", new DataPart("ktp.jpg", ktp_byte, "image/jpeg"));
                gv.perRegDataFile.put("selfie", new DataPart("selfie.jpg", selfie_byte, "image/jpeg"));
                gv.perRegDataFile.put("spesimen_ttd", new DataPart("ttd.jpg", ttd_byte, "image/jpeg"));
                createDocument();
                Log.e("Data-Object", gv.perRegData.toString());
                Log.e("Data-File", gv.perRegDataFile.toString());
            }
        });


        imgr_ktp = view.findViewById(R.id.img_ktp_fr_documents);
        btnr_ktp = view.findViewById(R.id.btn_ktp_fr_documents);
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

        imgr_npwp = view.findViewById(R.id.img_npwp_fr_documents);
        btnr_npwp = view.findViewById(R.id.btn_npwp_fr_documents);
        btnr_npwp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    bitmap_npwp = f.rotateImage(bitmap_npwp);
                    ByteArrayOutputStream byt = new ByteArrayOutputStream();
                    imgr_npwp.setImageBitmap(bitmap_npwp);
                    bitmap_npwp.compress(Bitmap.CompressFormat.JPEG, 100, byt);
                    npwp_byte = byt.toByteArray();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        imgr_selfie = view.findViewById(R.id.img_selfie_fr_documents);
        btnr_selfie = view.findViewById(R.id.btn_selfie_fr_documents);
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

        imgr_ttd = view.findViewById(R.id.img_ttd_fr_documents);
        btnr_ttd = view.findViewById(R.id.btn_ttd_fr_documents);
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

        checkStorageAccess();
        cekButtonRotate();
    }

    private void cekButtonRotate(){
        if(bitmap_ktp != null){btnr_ktp.setVisibility(View.VISIBLE);}else{btnr_ktp.setVisibility(View.INVISIBLE);}
        if(bitmap_npwp != null){btnr_npwp.setVisibility(View.VISIBLE);}else{btnr_npwp.setVisibility(View.INVISIBLE);}
        if(bitmap_selfie != null){btnr_selfie.setVisibility(View.VISIBLE);}else{btnr_selfie.setVisibility(View.INVISIBLE);}
        if(bitmap_ttd != null){btnr_ttd.setVisibility(View.VISIBLE);}else{btnr_ttd.setVisibility(View.INVISIBLE);}
    }

    private void createDocument(){
        new AlertDialog.Builder(getActivity())
                .setTitle("Konfirmasi")
                .setIcon(R.drawable.logo)
                .setMessage(getString(R.string.create_doc_confirmation))
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        dialog.show();
                        viewModel2.createPersonalDoc(prefManager.getUid(), prefManager.getToken());
                        viewModel2.getResultCreatePersonalDoc().observe(getActivity(), showResult);
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
                    OTPReceiver.isReady = true;
                    JSONObject jobRes = result.getJSONObject("result");
                    String msg = jobRes.getString("messages");
                    Log.e("Respon per cr doc", jobRes.toString());
                    //new
                    GlobalVariables.perRegData.clear();
                    GlobalVariables.insRegData.clear();
                    new Fungsi(getActivity()).showMessage(msg);
                    dialog.cancel();
                    Intent intent = new Intent(getActivity(), OTPDocActivity.class);
                    intent.putExtra("from", "doc");
                    new Routes(getActivity()).moveInFinish(intent);
                }else if(result.getInt("code") == 400){
                    OTPReceiver.isReady = false;
                    JSONObject jobRes = result.getJSONObject("result");
                    String msg = jobRes.toString();
                    msg = msg.replaceAll("\",","\n• ");
                    msg = msg.replaceAll("\"","");
                    msg = msg.replaceAll("_"," ");
                    msg = msg.replaceAll("\\{","");
                    msg = msg.replaceAll("\\}","");
                    dialog.cancel();
                    new AlertDialog.Builder(getActivity())
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
                    OTPReceiver.isReady = false;
                    String msg = result.getString("msg");
                    Log.e("Respon per cr doc", msg);
                    //f.showMessage(msg);
                    dialog.cancel();
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Pemberitahuan")
                            .setIcon(R.drawable.logo)
                            .setMessage(msg)
                            .setCancelable(false)
                            .setPositiveButton("Baik, saya mengerti", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
//                                    Intent intent = new Intent(getActivity(), RegistrationFormActivity.class);
//                                    new Routes(getActivity()).moveOutIntent(intent);
                                }
                            }).create().show();
                }
                dialog.cancel();
            } catch (JSONException e) {
                e.printStackTrace();
                String msg = getString(R.string.system_in_trouble);
                Log.e("Respon per cr doc", msg);
                new Fungsi(getActivity()).showMessage(msg);
                dialog.cancel();
                logout();
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

    boolean allisfilled = false;
    private void cekDone(){
        if(is_not_have_npwp){
            if(ktpisvalid && !str_ktp.isEmpty() && !str_selfie.isEmpty() && !str_ttd.isEmpty()){
                allisfilled = true;
            }else{
                allisfilled = false;
            }
        }else{
            if(ktpisvalid && npwpisvalid && !str_ktp.isEmpty() && !str_npwp.isEmpty() && !str_selfie.isEmpty() && !str_ttd.isEmpty()){
                allisfilled = true;
            }else{
                allisfilled = false;
            }
        }

        btn_next.setEnabled(allisfilled);
    }

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    int PICK_IMAGE_REQUEST = 0;
    private void chooseFileConfirmation(final String PICK_IMAGE_TYPE){
        int permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_STORAGE, 1);
        }else{
            new AlertDialog.Builder(getActivity())
                    .setTitle("Konfirmasi")
                    .setIcon(R.drawable.ic_document_photo_circle)
                    .setMessage("Metode pengambilan foto apa yang ingin digunakan?")
                    .setCancelable(true)
                    .setPositiveButton("KAMERA", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            final Dialog mdialog = new Dialog(getActivity());
                            LayoutInflater inflater = LayoutInflater.from(getActivity());
                            View dialogView = null;
                            Button btnNext = null;

                            if(PICK_IMAGE_TYPE == PICK_TYPE_KTP){
                                PICK_IMAGE_REQUEST = PICK_KTP_CAM;
                                dialogView = inflater.inflate(R.layout.dialog_pra_foto_ktp, null);
                                btnNext = dialogView.findViewById(R.id.btn_next_dial_pfk);
                            }else if(PICK_IMAGE_TYPE == PICK_TYPE_NPWP){
                                PICK_IMAGE_REQUEST = PICK_NPWP_CAM;
                                dialogView = inflater.inflate(R.layout.dialog_pra_foto_npwp, null);
                                btnNext = dialogView.findViewById(R.id.btn_next_dial_pfn);
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
            storageDir = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),"/avantee/");
            if (!storageDir.exists()) {
                storageDir.mkdirs();
            }
            file = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/avantee/", imageFileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            Log.e("Path Foto", file+"");
        }catch (Exception e) {
            e.printStackTrace();
        }

        Uri imgUri = FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName()+".fileprovider", file);
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
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
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
                    } else if (requestCode == PICK_NPWP) {
                        bitmap_npwp = bitmap;
                        imgr_npwp.setImageBitmap(bitmap_npwp);
                        decoded_npwp = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
                        npwp_byte = bytes.toByteArray();
                        str_npwp = f.getStringImage(decoded_npwp);
                        txt_npwp.setText(filePath.getLastPathSegment() + ".jpg");
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

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                File path = null;
                File file = null;
                path = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                file = new File(path, "/avantee/" + imageFileName);

                Uri filePath = FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".fileprovider", file);
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                    if (bitmap == null) {
                        f.showMessage(getString(R.string.must_portrait));
                    } else {
                        ////new
                        if (requestCode == PICK_KTP_CAM) {
                            performCrop(filePath, CROP_KTP);
                        } else if (requestCode == PICK_NPWP_CAM) {
                            performCrop(filePath, CROP_NPWP);
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
                        } else if (requestCode == CROP_NPWP) {
                            bitmap = f.getResizedBitmap(bitmap, MAX_SIZE);
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, BITMAP_SIZE, bytes);
                            bitmap_npwp = bitmap;
                            decoded_npwp = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
                            imgr_npwp.setImageBitmap(decoded_npwp);
                            npwp_byte = bytes.toByteArray();
                            str_npwp = f.getStringImage(decoded_npwp);
                            txt_npwp.setText(filePath.getLastPathSegment());
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
                        } else if (requestCode == CROP_TTD) {
                            bitmap = f.getResizedBitmap(bitmap, MAX_SIZE);
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, BITMAP_SIZE, bytes);
                            bitmap_ttd = bitmap;
                            decoded_ttd = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
                            imgr_ttd.setImageBitmap(decoded_ttd);
                            ttd_byte = bytes.toByteArray();
                            str_ttd = f.getStringImage(decoded_ttd);
                            txt_ttd.setText(filePath.getLastPathSegment());
                        }
                        ////new


//                        bitmap = f.getResizedBitmap(bitmap, MAX_SIZE);
//                        bitmap = f.getRotateImage2(file.getPath(), bitmap);
//                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, BITMAP_SIZE, bytes);
//                        if (requestCode == PICK_KTP_CAM) {
//                            bitmap_ktp = bitmap;
//                            imgr_ktp.setImageBitmap(bitmap_ktp);
//                            decoded_ktp = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
//                            ktp_byte = bytes.toByteArray();
//                            str_ktp = f.getStringImage(decoded_ktp);
//                            txt_ktp.setText(filePath.getLastPathSegment());
//                        } else if (requestCode == PICK_NPWP_CAM) {
//                            bitmap_npwp = bitmap;
//                            imgr_npwp.setImageBitmap(bitmap_npwp);
//                            decoded_npwp = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
//                            npwp_byte = bytes.toByteArray();
//                            str_npwp = f.getStringImage(decoded_npwp);
//                            txt_npwp.setText(filePath.getLastPathSegment());
//                        } else if (requestCode == PICK_SELFIE_CAM) {
//                            bitmap_selfie = bitmap;
//                            imgr_selfie.setImageBitmap(bitmap_selfie);
//                            decoded_selfie = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
//                            selfie_byte = bytes.toByteArray();
//                            str_selfie = f.getStringImage(decoded_selfie);
//                            txt_selfie.setText(filePath.getLastPathSegment());
//                        } else if (requestCode == PICK_TTD_CAM) {
//                            bitmap_ttd = bitmap;
//                            imgr_ttd.setImageBitmap(bitmap_ttd);
//                            decoded_ttd = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
//                            ttd_byte = bytes.toByteArray();
//                            str_ttd = f.getStringImage(decoded_ttd);
//                            txt_ttd.setText(filePath.getLastPathSegment());
//                        }
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
                    Toast.makeText(getActivity(), "Avantee Lender Apps membutuhkan ijin akses penyimpanan HP!", Toast.LENGTH_SHORT).show();
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
            getActivity().grantUriPermission("com.android.camera",picUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(picUri, "image/*");
            cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            cropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 4);
            cropIntent.putExtra("aspectY", 3);
            cropIntent.putExtra("outputX", 400);
            cropIntent.putExtra("outputY", 300);
            cropIntent.putExtra("return-data", true);
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
            startActivityForResult(cropIntent, PIC_CROP);
        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Device tidak support untuk memotong gambar.";
            Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
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
                    intent.setData(Uri.parse(String.format("package:%s", getActivity().getPackageName())));
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
            cv_ktp.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
            img_ktp.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_picture_taken));
            img_cancelktp.setVisibility(View.VISIBLE);
        }else{
            cv_ktp.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.lightest_neutral));
            img_ktp.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_take_picture));
            txt_ktp.setText(getString(R.string.take_photo_of_ktp));
            img_cancelktp.setVisibility(View.GONE);
        }

        if(!str_npwp.equalsIgnoreCase("")){
            cv_npwp.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
            img_npwp.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_picture_taken));
            img_cancelnpwp.setVisibility(View.VISIBLE);
        }else{
            cv_npwp.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.lightest_neutral));
            img_npwp.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_take_picture));
            txt_npwp.setText(getString(R.string.take_photo_of_npwp));
            img_cancelnpwp.setVisibility(View.GONE);
        }

        if(!str_selfie.equalsIgnoreCase("")){
            cv_selfie.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
            img_selfie.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_picture_taken));
            img_cancelselfie.setVisibility(View.VISIBLE);
        }else{
            cv_selfie.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.lightest_neutral));
            img_selfie.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_take_picture));
            txt_selfie.setText(getString(R.string.take_photo_of_selfie));
            img_cancelselfie.setVisibility(View.GONE);
        }

        if(!str_ttd.equalsIgnoreCase("")){
            cv_ttd.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
            img_ttd.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_picture_taken));
            img_cancelttd.setVisibility(View.VISIBLE);
        }else{
            cv_ttd.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.lightest_neutral));
            img_ttd.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_take_picture));
            txt_ttd.setText(getString(R.string.take_photo_of_spesimenttd));
            img_cancelttd.setVisibility(View.GONE);
        }
    }

    public void logout() {
        // LOGOUT: GET method to server through endpoint
        dialog.show();
        viewModel2.logout(prefManager.getUid(), prefManager.getToken());
        viewModel2.getLogoutResult().observe(getActivity(), checkLogout);
    }

    private Observer<String> checkLogout = new Observer<String>() {
        @Override
        public void onChanged(String result) {
            if(result.equals("ok")) {
                dialog.cancel();
                Intent intent = new Intent(getActivity(), WalkthroughActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                new Routes(getActivity()).moveOutIntent(intent);
            }else{
                dialog.cancel();
                new Fungsi().showMessage(result);
            }
        }
    };

}
