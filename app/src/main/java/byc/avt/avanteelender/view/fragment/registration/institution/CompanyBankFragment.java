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
import android.widget.CheckBox;
import android.widget.CompoundButton;

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

public class CompanyBankFragment extends Fragment {

    public CompanyBankFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_company_bank, container, false);
    }

    Button btn_next;

    private MasterDataViewModel viewModel;
    private PrefManager prefManager;
    private Dialog dialog;
    GlobalVariables gv;
    AutoCompleteTextView auto_bank;
    TextInputLayout txtBank, txtAccountName, txtAccountNumber;
    String bank="", accountName="", accountNumber="";
    CheckBox cb_owner_name_same_as_name_company;
    String is_same_name = "0", name_tmp = "", companyName = "";

    List<Object> listBank = new ArrayList<>(); List<Object> listBankID = new ArrayList<>();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MasterDataViewModel.class);
        prefManager = PrefManager.getInstance(getActivity());
        dialog = GlobalVariables.loadingDialog(requireActivity());

        gv.stInsDocument = false;
        cb_owner_name_same_as_name_company = view.findViewById(R.id.cb_name_same_as_name_narahubung_fr_com_bank);
        auto_bank = view.findViewById(R.id.auto_bank_name_fr_com_bank);
        txtBank = view.findViewById(R.id.edit_bank_name_fr_com_bank);
        txtAccountName = view.findViewById(R.id.edit_bank_owner_name_fr_com_bank);
        txtAccountNumber = view.findViewById(R.id.edit_bank_account_number_fr_com_bank);

        cb_owner_name_same_as_name_company.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    txtAccountName.getEditText().setText(gv.insRegData.get("nama_perusahaan"));
                    is_same_name = "1";
                }else{
//                    txtAccountName.getEditText().setText(name_tmp);
                    is_same_name = "0";
                }
            }
        });

        btn_next = view.findViewById(R.id.btn_next_fr_com_bank);
        btn_next.setEnabled(true);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmNext(v);
                //Navigation.findNavController(v).navigate(R.id.action_companyBankFragment_to_companyDocumentsFragment);
            }
        });

        loadData();
    }

    private void confirmNext(View v){
        accountName = Objects.requireNonNull(txtAccountName.getEditText().getText().toString().trim());
        accountNumber = Objects.requireNonNull(txtAccountNumber.getEditText().getText().toString().trim());
        if(!bank.isEmpty() && !accountName.isEmpty() && !accountNumber.isEmpty()){
            gv.stInsBankInfo = true;
            gv.insRegData.put("bank", bank);
            gv.insRegData.put("bank_account", accountName);
            gv.insRegData.put("bank_account_no", accountNumber);
            gv.insRegData.put("sesuai_nama", is_same_name);
            setNoError();
            Navigation.findNavController(v).navigate(R.id.action_companyBankFragment_to_companyDocumentsFragment);
        }else{
            cekError();
        }
    }

    public void clearMasterList(){
        listBank.clear();listBankID.clear();
    }

    public void loadData(){
        clearMasterList();
        dialog.show();
        if(gv.stInsBankInfo){
            bank = gv.insRegData.get("bank").toString();
            accountName = gv.insRegData.get("bank_account").toString();
            txtAccountName.getEditText().setText(accountName);
            accountNumber = gv.insRegData.get("bank_account_no").toString();
            txtAccountNumber.getEditText().setText(accountNumber);
        }else{}
        viewModel.getBank(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultBank().observe(getActivity(), showBank);
    }

    private void setNoError(){
        txtBank.setError(null);
        txtAccountName.setError(null);
        txtAccountNumber.setError(null);
    }

    private void cekError(){
        if(bank.isEmpty()){txtBank.setError(getString(R.string.cannotnull));}else{txtBank.setError(null);}
        if(accountName.isEmpty()){txtAccountName.setError(getString(R.string.cannotnull));}else{txtAccountName.setError(null);}
        if(accountNumber.isEmpty()){txtAccountNumber.setError(getString(R.string.cannotnull));}else{txtAccountNumber.setError(null);}
    }


    private Observer<JSONObject> showBank = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    JSONObject jobRes = result.getJSONObject("result");
                    JSONArray jar = jobRes.getJSONArray("bank");
                    for(int i = 0; i < jar.length(); i++){
                        listBank.add(jar.getJSONObject(i).getString("name"));
                        listBankID.add(jar.getJSONObject(i).getString("id"));
                    }
                    ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, listBank);
                    auto_bank.setAdapter(adapter);
                    auto_bank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                            bank = listBankID.get(x).toString();
                            Log.e("bank", bank);
                            txtBank.setError(null);
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