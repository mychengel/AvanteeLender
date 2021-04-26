package byc.avt.avanteelender.view.fragment.registration.institution;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.viewmodel.MasterDataViewModel;

public class CompanyDataFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_company_data, container, false);
    }

    Button btn_next;
    private RadioGroup radGroupIsOnlineBased;
    private RadioButton radButtonIsOnlineBased;

    private MasterDataViewModel viewModel;
    private PrefManager prefManager;
    private Dialog dialog;
    GlobalVariables gv;

    AutoCompleteTextView auto_company_type, auto_business_field, auto_income,
            auto_funds_source;
    TextInputLayout txtCompanyName, txtCompanyType, txtBusinessField, txtYearEst, txtIncome,
            txtFundsSource, txtCompanyPhone, txtCompanyFax, txtCompanyDesc;
    String companyName="", companyType="", businessField="", yearEst="", isOnlineBased="ya", income="",
            fundsSource="", companyPhone="", companyFax="", companyDesc="";
    List<Object> listComType = new ArrayList<>(); List<Object> listComTypeID = new ArrayList<>();
    List<Object> listBusinessField = new ArrayList<>(); List<Object> listBusinessFieldID = new ArrayList<>();
    List<Object> listIncome = new ArrayList<>(); List<Object> listIncomeID = new ArrayList<>();
    List<Object> listFunds = new ArrayList<>(); List<Object> listFundsID = new ArrayList<>();

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MasterDataViewModel.class);
        prefManager = PrefManager.getInstance(getActivity());
        dialog = GlobalVariables.loadingDialog(requireActivity());

        gv.stInsNarahubung = false;

        auto_company_type = view.findViewById(R.id.auto_company_type_fr_com_data);
        auto_business_field = view.findViewById(R.id.auto_business_field_fr_com_data);
        auto_income = view.findViewById(R.id.auto_company_income_fr_com_data);
        auto_funds_source = view.findViewById(R.id.auto_funds_source_fr_com_data);

        txtCompanyName = view.findViewById(R.id.edit_company_name_fr_com_data);
        txtCompanyType = view.findViewById(R.id.edit_company_type_fr_com_data);
        txtBusinessField = view.findViewById(R.id.edit_business_field_fr_com_data);
        txtYearEst = view.findViewById(R.id.edit_year_of_establishment_fr_com_data);
        txtIncome = view.findViewById(R.id.edit_company_income_fr_com_data);
        txtFundsSource = view.findViewById(R.id.edit_funds_source_fr_com_data);
        txtCompanyPhone = view.findViewById(R.id.edit_company_phone_fr_com_data);
        txtCompanyFax = view.findViewById(R.id.edit_company_fax_fr_com_data);
        txtCompanyDesc = view.findViewById(R.id.edit_company_desc_fr_com_data);

        radGroupIsOnlineBased = view.findViewById(R.id.rad_group_is_online_based_fr_com_data);
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

        btn_next = view.findViewById(R.id.btn_next_fr_com_data);
        btn_next.setEnabled(true);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmNext(v);
                //Navigation.findNavController(v).navigate(R.id.action_companyDataFragment_to_narahubungFragment);
            }
        });
        Log.e("tipeinvestorins", ""+ GlobalVariables.insRegData.get("tipe_investor"));

        loadData();

    }

    private void confirmNext(View v){
        companyName = Objects.requireNonNull(txtCompanyName.getEditText().getText().toString().trim());
        yearEst = Objects.requireNonNull(txtYearEst.getEditText().getText().toString().trim());
        companyPhone = Objects.requireNonNull(txtCompanyPhone.getEditText().getText().toString().trim());
        companyFax = Objects.requireNonNull(txtCompanyFax.getEditText().getText().toString().trim());
        companyDesc = Objects.requireNonNull(txtCompanyDesc.getEditText().getText().toString().trim());

        if(!companyName.isEmpty() && !companyType.isEmpty() && !businessField.isEmpty() && !yearEst.isEmpty()
                && !income.isEmpty() && !fundsSource.isEmpty() && !companyPhone.isEmpty() && !companyDesc.isEmpty()){
            gv.stInsCompanyData = true;
            gv.insRegData.put("nama_perusahaan",companyName);
            gv.insRegData.put("tipe_perusahaan",companyType);
            gv.insRegData.put("bidang_usaha",businessField);
            gv.insRegData.put("tahun_pendirian",yearEst);
            gv.insRegData.put("online_based",isOnlineBased);
            gv.insRegData.put("pendapatan",income);
            gv.insRegData.put("sumber_dana",fundsSource);
            gv.insRegData.put("no_tlp_kantor",companyPhone);
            gv.insRegData.put("no_fax_kantor",companyFax);
            gv.insRegData.put("deskripsi_usaha",companyDesc);
            setNoError();
            Navigation.findNavController(v).navigate(R.id.action_companyDataFragment_to_narahubungFragment);
        }else{
            cekError();
        }
    }

    public void clearMasterList(){
        listComType.clear();listComTypeID.clear();
        listIncome.clear();listIncomeID.clear();
        listBusinessField.clear();listBusinessFieldID.clear();
        listFunds.clear();listFundsID.clear();
    }

    public void loadData() {
        clearMasterList();
        dialog.show();
        if (gv.stInsCompanyData) {
            companyName = gv.insRegData.get("nama_perusahaan").toString();
            companyType = gv.insRegData.get("tipe_perusahaan").toString();
            businessField = gv.insRegData.get("bidang_usaha").toString();
            yearEst = gv.insRegData.get("tahun_pendirian").toString();
            isOnlineBased = gv.insRegData.get("online_based").toString();
            income = gv.insRegData.get("pendapatan").toString();
            fundsSource = gv.insRegData.get("sumber_dana").toString();
            companyPhone = gv.insRegData.get("no_tlp_kantor").toString();
            companyFax = gv.insRegData.get("no_fax_kantor").toString();
            companyDesc = gv.insRegData.get("deskripsi_usaha").toString();
            txtCompanyName.getEditText().setText(companyName);
            txtCompanyType.getEditText().setText(companyType);
            txtBusinessField.getEditText().setText(businessField);
            txtYearEst.getEditText().setText(yearEst);
            txtIncome.getEditText().setText(income);
            txtFundsSource.getEditText().setText(fundsSource);
            txtCompanyPhone.getEditText().setText(companyPhone);
            txtCompanyFax.getEditText().setText(companyFax);
            txtCompanyDesc.getEditText().setText(companyDesc);
        } else {
        }
        viewModel.getCompanyType(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultCompanyType().observe(getActivity(), showCompanyType);
        viewModel.getBusinessType(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultBusinessType().observe(getActivity(), showBusinessField);
        viewModel.getIncome(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultIncome().observe(getActivity(), showIncome);
        viewModel.getFundsSource(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultFundsSource().observe(getActivity(), showFundsSource);
    }

    private void setNoError(){
        txtCompanyName.setError(null);
        txtCompanyType.setError(null);
        txtBusinessField.setError(null);
        txtYearEst.setError(null);
        txtIncome.setError(null);
        txtFundsSource.setError(null);
        txtCompanyPhone.setError(null);
        txtCompanyFax.setError(null);
        txtCompanyDesc.setError(null);
    }

    private void cekError(){
        if(companyName.isEmpty()){txtCompanyName.setError(getString(R.string.cannotnull));}else{txtCompanyName.setError(null);}
        if(companyType.isEmpty()){txtCompanyType.setError(getString(R.string.cannotnull));}else{txtCompanyType.setError(null);}
        if(businessField.isEmpty()){txtBusinessField.setError(getString(R.string.cannotnull));}else{txtBusinessField.setError(null);}
        if(yearEst.isEmpty()){txtYearEst.setError(getString(R.string.cannotnull));}else{txtYearEst.setError(null);}
        if(income.isEmpty()){txtIncome.setError(getString(R.string.cannotnull));}else{txtIncome.setError(null);}
        if(fundsSource.isEmpty()){txtFundsSource.setError(getString(R.string.cannotnull));}else{txtFundsSource.setError(null);}
        if(companyPhone.isEmpty()){txtCompanyPhone.setError(getString(R.string.cannotnull));}else{txtCompanyPhone.setError(null);}
        //if(companyFax.isEmpty()){txtCompanyFax.setError(getString(R.string.cannotnull));}else{txtCompanyFax.setError(null);}
        if(companyDesc.isEmpty()){txtCompanyDesc.setError(getString(R.string.cannotnull));}else{txtCompanyDesc.setError(null);}
    }

    private Observer<JSONObject> showCompanyType = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    JSONObject jobRes = result.getJSONObject("result");
                    JSONArray jar = jobRes.getJSONArray("company");
                    for(int i = 0; i < jar.length(); i++){
                        listComType.add(jar.getJSONObject(i).getString("name"));
                        listComTypeID.add(jar.getJSONObject(i).getString("id"));
                    }
                    ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, listComType);
                    auto_company_type.setAdapter(adapter);
                    auto_company_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                            companyType = listComTypeID.get(x).toString();
                            Log.e("companyType", companyType);
                            txtCompanyType.setError(null);
                        }
                    });
                }else{
                }
                dialog.cancel();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private Observer<JSONObject> showBusinessField = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    JSONObject jobRes = result.getJSONObject("result");
                    JSONArray jar = jobRes.getJSONArray("businesstype");
                    for(int i = 0; i < jar.length(); i++){
                        listBusinessField.add(jar.getJSONObject(i).getString("name"));
                        listBusinessFieldID.add(jar.getJSONObject(i).getString("id"));
                    }
                    ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, listBusinessField);
                    auto_business_field.setAdapter(adapter);
                    auto_business_field.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                            businessField= listBusinessFieldID.get(x).toString();
                            Log.e("businessField", businessField);
                            txtBusinessField.setError(null);
                        }
                    });
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
                    }
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
                        listFunds.add(jar.getJSONObject(i).getString("name"));
                        listFundsID.add(jar.getJSONObject(i).getString("id"));
                    }
                    ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, listFunds);
                    auto_funds_source.setAdapter(adapter);
                    auto_funds_source.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                            fundsSource = listFundsID.get(x).toString();
                            Log.e("funds", fundsSource);
                            txtFundsSource.setError(null);
                        }
                    });
                }else{
                }
                dialog.cancel();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

}