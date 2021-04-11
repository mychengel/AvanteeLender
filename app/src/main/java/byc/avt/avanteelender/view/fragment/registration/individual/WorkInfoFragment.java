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

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.viewmodel.MasterDataViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkInfoFragment extends Fragment {

    public WorkInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_work_info, container, false);
    }

    private MasterDataViewModel viewModel;
    private PrefManager prefManager;
    private Dialog dialog;
    GlobalVariables gv;
    Button btn_next;
    AutoCompleteTextView auto_job, auto_jobField, auto_jobPosition, auto_experience, auto_income,
            auto_fundsSource, auto_companyProvince, auto_companyCity;
    private RadioGroup radGroupIsOnlineBased;
    private RadioButton radButtonIsOnlineBased;
    TextInputLayout txtJob, txtJobField, txtJobPosition, txtExperience, txtIncome, txtCompanyName,
            txtCompanyNumber, txtFundsSource, txtCompanyAddress, txtCompanyProvince, txtCompanyCity,
            txtCompanyDistrict, txtCompanyUrban, txtCompanyRT, txtCompanyRW, txtCompanyPostalCode;
    String job="", jobField="", isOnlineBased="ya", jobPosition="", experience="", income="",
            companyName="", companyNumber="", fundsSource="", companyAddress="", companyProvince="",
            companyCity="", companyDistrict="", companyUrban="", companyRT="", companyRW="",
            companyPostalCode="";

    List<Object> listJob = new ArrayList<>(); List<Object> listJobID = new ArrayList<>();
    List<Object> listJobField = new ArrayList<>(); List<Object> listJobFieldID = new ArrayList<>();
    List<Object> listJobPosition = new ArrayList<>(); List<Object> listJobPositionID = new ArrayList<>();
    List<Object> listExperience = new ArrayList<>(); List<Object> listExperienceID = new ArrayList<>();
    List<Object> listIncome = new ArrayList<>(); List<Object> listIncomeID = new ArrayList<>();
    List<Object> listFundsSource = new ArrayList<>(); List<Object> listFundsSourceID = new ArrayList<>();
    List<Object> listProvince = new ArrayList<>(); List<Object> listProvinceID = new ArrayList<>();
    List<Object> listCity = new ArrayList<>(); List<Object> listCityID = new ArrayList<>();


    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MasterDataViewModel.class);
        prefManager = PrefManager.getInstance(getActivity());
        dialog = GlobalVariables.loadingDialog(requireActivity());
        auto_job = view.findViewById(R.id.auto_job_fr_work_info);
        auto_jobField = view.findViewById(R.id.auto_job_field_fr_work_info);
        auto_jobPosition = view.findViewById(R.id.auto_jabatan_fr_work_info);
        auto_experience = view.findViewById(R.id.auto_lama_bekerja_fr_work_info);
        auto_income = view.findViewById(R.id.auto_pendapatan_per_bulan_fr_work_info);
        auto_fundsSource = view.findViewById(R.id.auto_funds_source_fr_work_info);
        auto_companyProvince = view.findViewById(R.id.auto_company_province_fr_work_info);
        auto_companyCity = view.findViewById(R.id.auto_company_city_fr_work_info);

        txtJob = view.findViewById(R.id.edit_job_fr_work_info);
        txtJobField = view.findViewById(R.id.edit_job_field_fr_work_info);
        txtJobPosition = view.findViewById(R.id.edit_jabatan_fr_work_info);
        txtExperience = view.findViewById(R.id.edit_lama_bekerja_fr_work_info);
        txtIncome = view.findViewById(R.id.edit_pendapatan_per_bulan_fr_work_info);
        txtCompanyName = view.findViewById(R.id.edit_company_name_fr_work_info);
        txtCompanyNumber = view.findViewById(R.id.edit_company_number_fr_work_info);
        txtFundsSource = view.findViewById(R.id.edit_funds_source_fr_work_info);
        txtCompanyAddress = view.findViewById(R.id.edit_company_address_fr_work_info);
        txtCompanyProvince = view.findViewById(R.id.edit_company_province_fr_work_info);
        txtCompanyCity = view.findViewById(R.id.edit_company_city_fr_work_info);
        txtCompanyDistrict = view.findViewById(R.id.edit_company_kecamatan_fr_work_info);
        txtCompanyUrban = view.findViewById(R.id.edit_company_kelurahan_fr_work_info);
        txtCompanyRT = view.findViewById(R.id.edit_company_rt_fr_work_info);
        txtCompanyRW = view.findViewById(R.id.edit_company_rt_fr_work_info);
        txtCompanyPostalCode = view.findViewById(R.id.edit_company_kodepos_fr_work_info);

        radGroupIsOnlineBased = view.findViewById(R.id.rad_group_is_online_based_fr_work_info);
        radGroupIsOnlineBased.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radButtonIsOnlineBased = view.findViewById(selectedId);
                if(radButtonIsOnlineBased.getText().equals("Ya")){
                    isOnlineBased = "ya";
                }else{
                    isOnlineBased = "tidak";
                }
            }
        });
        btn_next = view.findViewById(R.id.btn_next_fr_work_info);
        btn_next.setEnabled(true);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_workInfoFragment_to_addressDataFragment);
            }
        });

        loadData();
    }

    public void clearMasterList(){
        listJob.clear();listJobID.clear();
        listJobField.clear();listJobFieldID.clear();
        listJobPosition.clear();listJobPositionID.clear();
        listExperience.clear();listExperienceID.clear();
        listIncome.clear();listIncomeID.clear();
        listFundsSource.clear();listFundsSourceID.clear();
        listProvince.clear();listProvinceID.clear();
        listCity.clear();listCityID.clear();
    }

    public void loadData(){
        clearMasterList();
        dialog.show();
        viewModel.getJob(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultJob().observe(getActivity(), showJob);
        viewModel.getJobField(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultJobField().observe(getActivity(), showJobField);
        viewModel.getJobPosition(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultJobPosition().observe(getActivity(), showJobPosition);
        viewModel.getWorkExperience(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultWorkExperience().observe(getActivity(), showExperience);
        viewModel.getIncome(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultIncome().observe(getActivity(), showIncome);
        viewModel.getFundsSource(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultFundsSource().observe(getActivity(), showFundsSource);
        viewModel.getProvince(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultProvince().observe(getActivity(), showProvince);
    }

    private Observer<JSONObject> showJob = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    JSONObject jobRes = result.getJSONObject("result");
                    JSONArray jar = jobRes.getJSONArray("job");
                    for(int i = 0; i < jar.length(); i++){
                        listJob.add(jar.getJSONObject(i).getString("name"));
                        listJobID.add(jar.getJSONObject(i).getString("id"));
                        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, listJob);
                        auto_job.setAdapter(adapter);
                        auto_job.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                                job = listJobID.get(x).toString();
                                Log.e("job", job);
                                txtJob.setError(null);
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

    private Observer<JSONObject> showJobField = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    JSONObject jobRes = result.getJSONObject("result");
                    JSONArray jar = jobRes.getJSONArray("job_type");
                    for(int i = 0; i < jar.length(); i++){
                        listJobField.add(jar.getJSONObject(i).getString("name"));
                        listJobFieldID.add(jar.getJSONObject(i).getString("id"));
                        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, listJobField);
                        auto_jobField.setAdapter(adapter);
                        auto_jobField.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                                jobField = listJobFieldID.get(x).toString();
                                Log.e("jobField", jobField);
                                txtJobField.setError(null);
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
                        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, listJobPosition);
                        auto_jobPosition.setAdapter(adapter);
                        auto_jobPosition.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                                jobPosition = listJobPositionID.get(x).toString();
                                Log.e("jobPosition", jobPosition);
                                txtJobPosition.setError(null);
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

    private Observer<JSONObject> showExperience = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    JSONObject jobRes = result.getJSONObject("result");
                    JSONArray jar = jobRes.getJSONArray("experience");
                    for(int i = 0; i < jar.length(); i++){
                        listExperience.add(jar.getJSONObject(i).getString("name"));
                        listExperienceID.add(jar.getJSONObject(i).getString("id"));
                        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, listExperience);
                        auto_experience.setAdapter(adapter);
                        auto_experience.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                                experience = listExperienceID.get(x).toString();
                                Log.e("experience", experience);
                                txtExperience.setError(null);
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

    private Observer<JSONObject> showIncome = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    JSONObject jobRes = result.getJSONObject("result");
                    JSONArray jar = jobRes.getJSONArray("income");
                    for(int i = 0; i < jar.length(); i++){
                        listIncome.add(jar.getJSONObject(i).getString("name"));
                        listIncomeID.add(jar.getJSONObject(i).getString("id"));
                        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, listIncome);
                        auto_income.setAdapter(adapter);
                        auto_income.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                                income = listIncomeID.get(x).toString();
                                Log.e("income", income);
                                txtIncome.setError(null);
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

    private Observer<JSONObject> showFundsSource = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    JSONObject jobRes = result.getJSONObject("result");
                    JSONArray jar = jobRes.getJSONArray("funds");
                    for(int i = 0; i < jar.length(); i++){
                        listFundsSource.add(jar.getJSONObject(i).getString("name"));
                        listFundsSourceID.add(jar.getJSONObject(i).getString("id"));
                        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, listFundsSource);
                        auto_fundsSource.setAdapter(adapter);
                        auto_fundsSource.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                                fundsSource = listFundsSourceID.get(x).toString();
                                Log.e("funds", fundsSource);
                                txtFundsSource.setError(null);
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

    private Observer<JSONObject> showProvince = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    JSONObject jobRes = result.getJSONObject("result");
                    JSONArray jar = jobRes.getJSONArray("province");
                    for(int i = 0; i < jar.length(); i++){
                        listProvince.add(jar.getJSONObject(i).getString("name"));
                        listProvinceID.add(jar.getJSONObject(i).getString("id"));
                        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, listProvince);
                        auto_companyProvince.setAdapter(adapter);
                        auto_companyProvince.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                                companyProvince = listProvinceID.get(x).toString();
                                Log.e("comProvince", companyProvince);
                                txtCompanyProvince.setError(null);
                                viewModel.getCity(prefManager.getUid(), prefManager.getToken(), companyProvince);
                                viewModel.getResultCity().observe(getActivity(), showCity);
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

    private Observer<JSONObject> showCity = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            listCity.clear();
            listCityID.clear();
            try {
                if(result.getInt("code") == 200){
                    JSONObject jobRes = result.getJSONObject("result");
                    JSONArray jar = jobRes.getJSONArray("city");
                    for(int i = 0; i < jar.length(); i++){
                        listCity.add(jar.getJSONObject(i).getString("name"));
                        listCityID.add(jar.getJSONObject(i).getString("id"));
                        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, listCity);
                        auto_companyCity.setAdapter(adapter);
                        auto_companyCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                                companyCity = listCityID.get(x).toString();
                                Log.e("comCity", companyCity);
                                txtCompanyCity.setError(null);
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
