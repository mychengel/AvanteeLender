package byc.avt.avanteelender.view.fragment.registration.institution;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.viewmodel.MasterDataViewModel;

public class NarahubungFragment extends Fragment {

    public NarahubungFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_narahubung, container, false);
    }

    Button btn_next;

    private MasterDataViewModel viewModel;
    private PrefManager prefManager;
    private Dialog dialog;
    GlobalVariables gv;

    TextInputLayout txtName, txtBirthDate, txtJabatan, txtPhone, txtEmail, txtNoKtp;
    EditText editBirthDate;
    String name="", birthdate="", jabatan="", phone="", email="", noKtp="";
    boolean emailIsValid = false;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MasterDataViewModel.class);
        prefManager = PrefManager.getInstance(getActivity());
        dialog = GlobalVariables.loadingDialog(requireActivity());

        gv.stInsCompanyAddress = false;

        txtName = view.findViewById(R.id.edit_name_fr_narahubung_data);
        txtJabatan = view.findViewById(R.id.edit_position_fr_narahubung_data);
        txtPhone = view.findViewById(R.id.edit_phone_fr_narahubung_data);
        txtEmail = view.findViewById(R.id.edit_email_fr_narahubung_data);
        txtNoKtp = view.findViewById(R.id.edit_ktp_fr_narahubung_data);
        txtBirthDate = view.findViewById(R.id.edit_birth_date_fr_narahubung_data);
        editBirthDate = view.findViewById(R.id.editText_birthdate_fr_narahubung_data);

        editBirthDate.setFocusable(false);
        editBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
                builder.setTitleText("Pilih tanggal lahir");
                builder.setSelection(Calendar.getInstance().getTimeInMillis());
                MaterialDatePicker picker = builder.build();
                picker.show(getActivity().getSupportFragmentManager(),"BIRTHDATE");
                picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        birthdate = sdf.format(selection);
                        editBirthDate.setText(birthdate);
                    }
                });
            }
        });

        btn_next = view.findViewById(R.id.btn_next_fr_narahubung_data);
        btn_next.setEnabled(true);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmNext(v);
//                Navigation.findNavController(v).navigate(R.id.action_narahubungFragment_to_companyAddressFragment);
            }
        });

        loadData();
    }

    public void loadData(){
        dialog.show();
        if(gv.stInsNarahubung){
            name = gv.insRegData.get("nama").toString();
            txtName.getEditText().setText(name);
            birthdate = gv.insRegData.get("tanggal_lahir").toString();
            txtBirthDate.getEditText().setText(birthdate);
            jabatan = gv.insRegData.get("jabatan").toString();
            txtJabatan.getEditText().setText(jabatan);
            phone = gv.insRegData.get("no_telepon").toString();
            txtPhone.getEditText().setText(phone);
            email = gv.insRegData.get("email").toString();
            txtEmail.getEditText().setText(email);
            noKtp = gv.insRegData.get("no_ktp").toString();
            txtNoKtp.getEditText().setText(noKtp);
        }else{dialog.cancel();}
        dialog.cancel();
    }

    private void confirmNext(View v){
        name = Objects.requireNonNull(txtName.getEditText().getText().toString().trim());
        jabatan = Objects.requireNonNull(txtJabatan.getEditText().getText().toString().trim());
        phone = Objects.requireNonNull(txtPhone.getEditText().getText().toString().trim());
        email = Objects.requireNonNull(txtEmail.getEditText().getText().toString().trim());
        noKtp = Objects.requireNonNull(txtNoKtp.getEditText().getText().toString().trim());

        emailIsValid = Patterns.EMAIL_ADDRESS.matcher(email).matches();

        if(!name.isEmpty() && !birthdate.isEmpty() && !jabatan.isEmpty() && !phone.isEmpty() && !noKtp.isEmpty()
                && !email.isEmpty() && emailIsValid){
            gv.stInsNarahubung = true;
            gv.insRegData.put("nama",name);
            gv.insRegData.put("tanggal_lahir",birthdate);
            gv.insRegData.put("jabatan",jabatan);
            gv.insRegData.put("no_telepon",phone);
            gv.insRegData.put("email",email);
            gv.insRegData.put("no_ktp",noKtp);
            setNoError();
            Navigation.findNavController(v).navigate(R.id.action_narahubungFragment_to_companyAddressFragment);
        }else{
            cekError();
        }
    }

    private void setNoError(){
        txtName.setError(null);
        txtBirthDate.setError(null);
        txtJabatan.setError(null);
        txtPhone.setError(null);
        txtEmail.setError(null);
        txtNoKtp.setError(null);
    }

    private void cekError(){
        if(name.isEmpty()){txtName.setError(getString(R.string.cannotnull));}else{txtName.setError(null);}
        if(birthdate.isEmpty()){txtBirthDate.setError(getString(R.string.cannotnull));}else{txtBirthDate.setError(null);}
        if(jabatan.isEmpty()){txtJabatan.setError(getString(R.string.cannotnull));}else{txtJabatan.setError(null);}
        if(phone.isEmpty()){txtPhone.setError(getString(R.string.cannotnull));}else{txtPhone.setError(null);}
        if(email.isEmpty()){
            txtEmail.setError(getString(R.string.cannotnull));
        }else if(!emailIsValid){
            txtEmail.setError(getString(R.string.email_not_valid));
        }else{txtEmail.setError(null);}
        if(noKtp.isEmpty()){txtNoKtp.setError(getString(R.string.cannotnull));}else{txtNoKtp.setError(null);}
    }
}