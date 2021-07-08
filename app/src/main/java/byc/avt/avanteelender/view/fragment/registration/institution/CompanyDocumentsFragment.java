package byc.avt.avanteelender.view.fragment.registration.institution;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.helper.Routes;
import byc.avt.avanteelender.helper.receiver.OTPReceiver;
import byc.avt.avanteelender.intro.WalkthroughActivity;
import byc.avt.avanteelender.model.DataPart;
import byc.avt.avanteelender.view.auth.RegistrationFormActivity;
import byc.avt.avanteelender.view.misc.OTPDocActivity;
import byc.avt.avanteelender.viewmodel.AuthenticationViewModel;
import byc.avt.avanteelender.viewmodel.MasterDataViewModel;

public class CompanyDocumentsFragment extends Fragment {

    public CompanyDocumentsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_company_documents, container, false);
    }

    Button btn_next;

    private AuthenticationViewModel viewModel;
    private PrefManager prefManager;
    private Dialog dialog;
    GlobalVariables gv;
    Fungsi f = new Fungsi(getActivity());
    TextInputLayout edit_npwp;
    TextView txt_ktp, txt_npwp, txt_selfie;
    CardView cv_ktp, cv_npwp, cv_selfie;
    LinearLayout lin_npwp;
    ImageView img_ktp, img_cancelktp, img_npwp, img_cancelnpwp, img_selfie, img_cancelselfie;
    boolean is_not_have_npwp = false;
    CheckBox cb_not_have_npwp;
    String no_npwp = "";
    byte[] ktp_byte = null, npwp_byte = null, selfie_byte = null;

    Bitmap bitmap, decoded_ktp, decoded_npwp, decoded_selfie;
    String str_ktp = "", str_npwp = "", str_selfie = "";
    int PICK_KTP = 1, PICK_NPWP = 2, PICK_SELFIE = 3, PICK_KTP_CAM = 4, PICK_NPWP_CAM = 5, PICK_SELFIE_CAM = 6;
    String PICK_TYPE_KTP = "insktp", PICK_TYPE_NPWP = "insnpwp", PICK_TYPE_SELFIE = "insselfie";
    int BITMAP_SIZE = 60, MAX_SIZE = 512;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(AuthenticationViewModel.class);
        prefManager = PrefManager.getInstance(getActivity());
        dialog = GlobalVariables.loadingDialog(requireActivity());
        cv_ktp = view.findViewById(R.id.cv_take_ktp_fr_com_doc);
        cv_npwp = view.findViewById(R.id.cv_take_npwp_fr_com_doc);
        cv_selfie = view.findViewById(R.id.cv_take_selfie_fr_com_doc);
        edit_npwp = view.findViewById(R.id.edit_npwp_number_fr_com_doc);
        txt_ktp = view.findViewById(R.id.txt_take_ktp_fr_com_doc);
        txt_npwp = view.findViewById(R.id.txt_take_npwp_fr_com_doc);
        txt_selfie = view.findViewById(R.id.txt_take_selfie_fr_com_doc);
        img_ktp = view.findViewById(R.id.img_take_ktp_fr_com_doc);
        img_cancelktp = view.findViewById(R.id.img_cancel_take_ktp_fr_com_doc);
        img_npwp = view.findViewById(R.id.img_take_npwp_fr_com_doc);
        img_cancelnpwp = view.findViewById(R.id.img_cancel_take_npwp_fr_com_doc);
        img_selfie = view.findViewById(R.id.img_take_selfie_fr_com_doc);
        img_cancelselfie = view.findViewById(R.id.img_cancel_take_selfie_fr_com_doc);
        lin_npwp = view.findViewById(R.id.lin_npwp_fr_com_doc);

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

        btn_next = view.findViewById(R.id.btn_next_fr_com_doc);
        btn_next.setEnabled(false);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gv.stInsDocument = true;
                gv.insRegData.put("no_npwp", no_npwp);
                gv.insRegDataFile.put("npwp", new DataPart("npwp.jpg", npwp_byte, "image/jpeg"));
                gv.insRegDataFile.put("selfie_img", new DataPart("selfie.jpg", selfie_byte, "image/jpeg"));
                createDocument();
            }
        });

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
                        viewModel.createInstitutionDoc(prefManager.getUid(), prefManager.getToken());
                        viewModel.getResultCreateInstitutionDoc().observe(getActivity(), showResult);
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
                    new Fungsi(getActivity()).showMessage(msg);
                    dialog.cancel();
                    Intent intent = new Intent(getActivity(), OTPDocActivity.class);
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
                    Log.e("Respon ins cr doc", msg);
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
                                    Intent intent = new Intent(getActivity(), RegistrationFormActivity.class);
                                    new Routes(getActivity()).moveOutIntent(intent);
                                }
                            }).create().show();
                }
                dialog.cancel();
            } catch (JSONException e) {
                e.printStackTrace();
                String msg = getString(R.string.system_in_trouble);
                Log.e("Respon ins cr doc", msg);
                new Fungsi(getActivity()).showMessage(msg);
                dialog.cancel();
                Intent intent = new Intent(getActivity(), WalkthroughActivity.class);
                new Routes(getActivity()).moveOutIntent(intent);
            }
        }
    };

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
                                dialogView = inflater.inflate(R.layout.dialog_pra_foto_swafoto, null);
                                btnNext = dialogView.findViewById(R.id.btn_next_dial_pfs);
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

//                            new AlertDialog.Builder(getActivity())
//                                .setTitle("Pemberitahuan")
//                                .setIcon(R.drawable.ic_document_photo_circle)
//                                .setMessage(getString(R.string.req_doc))
//                                .setCancelable(true)
//                                .setPositiveButton("OK, ambil foto", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        showCameraCapture(PICK_IMAGE_REQUEST, PICK_IMAGE_TYPE);
//                                    }
//                                })
//                                .create()
//                                .show();
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
                    // 512 adalah resolusi tertinggi setelah image di resize, bisa di ganti.
                    bitmap = f.getResizedBitmap(bitmap, MAX_SIZE);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, BITMAP_SIZE, bytes);
                    if (requestCode == PICK_KTP) {
                        decoded_ktp = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
                        ktp_byte = bytes.toByteArray();
                        str_ktp = f.getStringImage(decoded_ktp);
                        Log.e("str_ktp", str_ktp);
                        txt_ktp.setText(filePath.getLastPathSegment() + ".jpg");
                    } else if (requestCode == PICK_NPWP) {
                        decoded_npwp = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
                        npwp_byte = bytes.toByteArray();
                        str_npwp = f.getStringImage(decoded_npwp);
                        txt_npwp.setText(filePath.getLastPathSegment() + ".jpg");
                    } else if (requestCode == PICK_SELFIE) {
                        decoded_selfie = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
                        selfie_byte = bytes.toByteArray();
                        str_selfie = f.getStringImage(decoded_selfie);
                        txt_selfie.setText(filePath.getLastPathSegment() + ".jpg");
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
                    if(bitmap == null){
                        f.showMessage(getString(R.string.bitmap_null));
                    }else {
                        bitmap = f.getResizedBitmap(bitmap, MAX_SIZE);
                        //bitmap = f.getRotateImage(bitmap);
                        bitmap = f.getRotateImage(file.getPath(), bitmap);
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, BITMAP_SIZE, bytes);
                        if (requestCode == PICK_KTP_CAM) {
                            decoded_ktp = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
                            ktp_byte = bytes.toByteArray();
                            Log.e("KTP Byte", ktp_byte + "");
                            str_ktp = f.getStringImage(decoded_ktp);
                            txt_ktp.setText(filePath.getLastPathSegment());
                        } else if (requestCode == PICK_NPWP_CAM) {
                            decoded_npwp = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
                            npwp_byte = bytes.toByteArray();
                            str_npwp = f.getStringImage(decoded_npwp);
                            txt_npwp.setText(filePath.getLastPathSegment());
                        } else if (requestCode == PICK_SELFIE_CAM) {
                            decoded_selfie = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
                            selfie_byte = bytes.toByteArray();
                            str_selfie = f.getStringImage(decoded_selfie);
                            txt_selfie.setText(filePath.getLastPathSegment());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        cekView();
        cekDone();
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
        if(!str_ktp.isEmpty() && npwpisvalid && !str_npwp.isEmpty() && !str_selfie.isEmpty()){
            allisfilled = true;
        }else{
            allisfilled = false;
        }
        btn_next.setEnabled(allisfilled);
    }

    private void cekView(){
        if(!str_ktp.equalsIgnoreCase("")){
            cv_ktp.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
            img_ktp.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_picture_taken));
            img_cancelktp.setVisibility(View.VISIBLE);
        }else{
            cv_ktp.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.lightest_neutral));
            img_ktp.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_take_picture));
            txt_ktp.setText(getString(R.string.take_narahubung_ktp_photo));
            img_cancelktp.setVisibility(View.GONE);
        }

        if(!str_npwp.equalsIgnoreCase("")){
            cv_npwp.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
            img_npwp.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_picture_taken));
            img_cancelnpwp.setVisibility(View.VISIBLE);
        }else{
            cv_npwp.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.lightest_neutral));
            img_npwp.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_take_picture));
            txt_npwp.setText(getString(R.string.take_narahubung_npwp_photo));
            img_cancelnpwp.setVisibility(View.GONE);
        }

        if(!str_selfie.equalsIgnoreCase("")){
            cv_selfie.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
            img_selfie.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_picture_taken));
            img_cancelselfie.setVisibility(View.VISIBLE);
        }else{
            cv_selfie.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.lightest_neutral));
            img_selfie.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_take_picture));
            txt_selfie.setText(getString(R.string.take_narahubung_selfie_photo));
            img_cancelselfie.setVisibility(View.GONE);
        }
    }
}