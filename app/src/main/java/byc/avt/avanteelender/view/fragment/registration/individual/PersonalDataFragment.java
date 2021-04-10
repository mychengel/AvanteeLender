package byc.avt.avanteelender.view.fragment.registration.individual;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.viewmodel.DashboardViewModel;
import byc.avt.avanteelender.viewmodel.MasterDataViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalDataFragment extends Fragment {

    public PersonalDataFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_personal_data, container, false);
    }

    private MasterDataViewModel viewModel;
    private PrefManager prefManager;
    private Dialog dialog;
    Button btn_next;
    AutoCompleteTextView auto_kewarganegaraan, auto_status, auto_religion, auto_education;
    GlobalVariables gv;

    private RadioGroup radGroupGender;
    private RadioButton radButtonGender;
    TextInputLayout txtName, txtBirthPlace, txtBirthDate, txtMotherName, txtHomePhone;
    EditText editBirthDate;

    String name="", gender="male", birthplace="", birtdate="", civil="", status="", religion="", education="", telprumah="", mothername="";

    List<Object> listCivil = new ArrayList<>();
    List<Object> listCivilID = new ArrayList<>();
    List<Object> listReligion = new ArrayList<>();
    List<Object> listReligionID = new ArrayList<>();
    List<Object> listEdu = new ArrayList<>();
    List<Object> listEduID = new ArrayList<>();
    List<Object> listStatus = new ArrayList<>();
    List<Object> listStatusID = new ArrayList<>();

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MasterDataViewModel.class);
        prefManager = PrefManager.getInstance(getActivity());
        dialog = GlobalVariables.loadingDialog(requireActivity());
        auto_kewarganegaraan = view.findViewById(R.id.auto_kewarganegaraan_fr_personal_data);
        auto_status = view.findViewById(R.id.auto_status_fr_personal_data);
        auto_religion = view.findViewById(R.id.auto_religion_fr_personal_data);
        auto_education = view.findViewById(R.id.auto_education_fr_personal_data);
        txtBirthDate = view.findViewById(R.id.edit_birth_date_fr_personal_data);
        txtName = view.findViewById(R.id.edit_name_fr_personal_data);
        txtMotherName = view.findViewById(R.id.edit_mothername_fr_personal_data);
        txtHomePhone = view.findViewById(R.id.edit_homenumber_fr_personal_data);
        txtBirthPlace = view.findViewById(R.id.edit_birth_place_fr_personal_data);
        editBirthDate = view.findViewById(R.id.editText_birthdate_fr_personal_data);
        radGroupGender = view.findViewById(R.id.rad_group_gender_fr_personal_data);
        radGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radButtonGender = view.findViewById(selectedId);
                if(radButtonGender.getText().equals("Laki-laki")){
                    gender = "male";
                }else{
                    gender = "female";
                }
            }
        });

        btn_next = view.findViewById(R.id.btn_next_fr_personal_data);
        btn_next.setEnabled(true);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("gender", gender);
                name = txtName.getEditText().getText().toString().trim();
                birthplace = txtBirthPlace.getEditText().getText().toString().trim();
                mothername = txtMotherName.getEditText().getText().toString().trim();
                telprumah = txtHomePhone.getEditText().getText().toString().trim();
                Navigation.findNavController(v).navigate(R.id.action_personalDataFragment_to_workInfoFragment);
            }
        });
        Log.e("tipeinvestor", ""+gv.perRegData.get("tipe_investor"));

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
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        birtdate = sdf.format(selection);
                        editBirthDate.setText(birtdate);
                    }
                });
            }
        });

        loadData();
    }

    public void loadData(){
        clearMasterList();
        dialog.show();
        viewModel.getCivil(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultCivil().observe(getActivity(), showCivil);
        viewModel.getStatus(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultStatus().observe(getActivity(), showStatus);
        viewModel.getReligion(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultReligion().observe(getActivity(), showReligion);
        viewModel.getEducation(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultEducation().observe(getActivity(), showEducation);
    }

    public void clearMasterList(){
        listCivil.clear();listCivilID.clear();
        listStatus.clear();listStatusID.clear();
        listReligion.clear();listReligionID.clear();
        listEdu.clear();listEduID.clear();
    }

    private Observer<JSONObject> showCivil = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    JSONObject jobRes = result.getJSONObject("result");
                    JSONArray jar = jobRes.getJSONArray("negara");
                    for(int i = 0; i < jar.length(); i++){
                        listCivil.add(jar.getJSONObject(i).getString("name"));
                        listCivilID.add(jar.getJSONObject(i).getString("id"));
                        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, listCivil);
                        auto_kewarganegaraan.setAdapter(adapter);
                        auto_kewarganegaraan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                                civil = listCivilID.get(x).toString();
                                Log.e("civil", civil);
                            }
                        });
                    }
                }else{
                }
                dialog.cancel();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private Observer<JSONObject> showStatus = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    JSONObject jobRes = result.getJSONObject("result");
                    JSONArray jar = jobRes.getJSONArray("marital");
                    for(int i = 0; i < jar.length(); i++){
                        listStatus.add(jar.getJSONObject(i).getString("name"));
                        listStatusID.add(jar.getJSONObject(i).getString("id"));
                        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, listStatus);
                        auto_status.setAdapter(adapter);
                        auto_status.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                                status = listStatusID.get(x).toString();
                                Log.e("status", status);
                            }
                        });
                    }
                }else{
                }
                dialog.cancel();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private Observer<JSONObject> showReligion = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    JSONObject jobRes = result.getJSONObject("result");
                    JSONArray jar = jobRes.getJSONArray("religion");
                    for(int i = 0; i < jar.length(); i++){
                        listReligion.add(jar.getJSONObject(i).getString("name"));
                        listReligionID.add(jar.getJSONObject(i).getString("id"));
                        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, listReligion);
                        auto_religion.setAdapter(adapter);
                        auto_religion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                                religion = listReligionID.get(x).toString();
                                Log.e("religion", religion);
                            }
                        });
                    }
                }else{
                }
                dialog.cancel();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private Observer<JSONObject> showEducation = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    JSONObject jobRes = result.getJSONObject("result");
                    JSONArray jar = jobRes.getJSONArray("education");
                    for(int i = 0; i < jar.length(); i++){
                        listEdu.add(jar.getJSONObject(i).getString("name"));
                        listEduID.add(jar.getJSONObject(i).getString("id"));
                        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, listEdu);
                        auto_education.setAdapter(adapter);
                        auto_education.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                                education = listEduID.get(x).toString();
                                Log.e("education", education);
                            }
                        });
                    }
                }else{
                }
                dialog.cancel();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

}
