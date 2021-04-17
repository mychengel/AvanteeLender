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

/**
 * A simple {@link Fragment} subclass.
 */
public class BankInfoFragment extends Fragment {

    public BankInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bank_info, container, false);
    }

    private MasterDataViewModel viewModel;
    private PrefManager prefManager;
    private Dialog dialog;
    GlobalVariables gv;
    AutoCompleteTextView auto_bank, auto_avg_trans;
    TextInputLayout txtBank, txtAccountName, txtAccountNumber, txtAvgTrans;
    String bank="", accountName="", accountNumber="", avgTrans="";
    String SUMBER_DANA_LAIN = "GAJI";

    List<Object> listBank = new ArrayList<>(); List<Object> listBankID = new ArrayList<>();
    List<Object> listAvgTrans = new ArrayList<>(); List<Object> listAvgTransID = new ArrayList<>();

    Button btn_next;
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MasterDataViewModel.class);
        prefManager = PrefManager.getInstance(getActivity());
        dialog = GlobalVariables.loadingDialog(requireActivity());

        gv.stPerDocument = false;

        auto_bank = view.findViewById(R.id.auto_bank_name_fr_bank_info);
        auto_avg_trans = view.findViewById(R.id.auto_avg_transaction_fr_bank_info);
        txtBank = view.findViewById(R.id.edit_bank_name_fr_bank_info);
        txtAccountName = view.findViewById(R.id.edit_bank_owner_name_fr_bank_info);
        txtAccountNumber = view.findViewById(R.id.edit_bank_account_number_fr_bank_info);
        txtAvgTrans = view.findViewById(R.id.edit_avg_transaction_fr_bank_info);

        btn_next = view.findViewById(R.id.btn_next_fr_bank_info);
        btn_next.setEnabled(true);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //confirmNext(v);
                Navigation.findNavController(v).navigate(R.id.action_bankInfoFragment_to_documentsFragment);
            }
        });

        loadData();
    }

    public void clearMasterList(){
        listBank.clear();listBankID.clear();
        listAvgTrans.clear();listAvgTransID.clear();
    }

    public void loadData(){
        clearMasterList();
        dialog.show();
        if(gv.stPerBankInfo){
            bank = gv.perRegData.get("bank").toString();
            accountName = gv.perRegData.get("nama_pemilik_rekening").toString();
            txtAccountName.getEditText().setText(accountName);
            accountNumber = gv.perRegData.get("no_rekening").toString();
            txtAccountNumber.getEditText().setText(accountNumber);
            avgTrans = gv.perRegData.get("rata_rata_transaksi").toString();
        }else{}
        viewModel.getBank(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultBank().observe(getActivity(), showBank);
        viewModel.getAvgTransaction(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultAvgTransaction().observe(getActivity(), showAvgTrans);
    }

    private void confirmNext(View v){
        accountName = Objects.requireNonNull(txtAccountName.getEditText().getText().toString().trim());
        accountNumber = Objects.requireNonNull(txtAccountNumber.getEditText().getText().toString().trim());
        if(!bank.isEmpty() && !accountName.isEmpty() && !accountNumber.isEmpty() && !avgTrans.isEmpty()){
            gv.stPerBankInfo = true;
            gv.perRegData.put("bank", bank);
            gv.perRegData.put("nama_pemilik_rekening", accountName);
            gv.perRegData.put("no_rekening", accountNumber);
            gv.perRegData.put("rata_rata_transaksi", avgTrans);
            gv.perRegData.put("sumber_dana_lain", SUMBER_DANA_LAIN);
            setNoError();
            Navigation.findNavController(v).navigate(R.id.action_bankInfoFragment_to_documentsFragment);
        }else{
            cekError();
        }
    }

    private void setNoError(){
        txtBank.setError(null);
        txtAccountName.setError(null);
        txtAccountNumber.setError(null);
        txtAvgTrans.setError(null);
    }

    private void cekError(){
        if(bank.isEmpty()){txtBank.setError(getString(R.string.cannotnull));}else{txtBank.setError(null);}
        if(accountName.isEmpty()){txtAccountName.setError(getString(R.string.cannotnull));}else{txtAccountName.setError(null);}
        if(accountNumber.isEmpty()){txtAccountNumber.setError(getString(R.string.cannotnull));}else{txtAccountNumber.setError(null);}
        if(avgTrans.isEmpty()){txtAvgTrans.setError(getString(R.string.cannotnull));}else{txtAvgTrans.setError(null);}
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

    private Observer<JSONObject> showAvgTrans = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    JSONObject jobRes = result.getJSONObject("result");
                    JSONArray jar = jobRes.getJSONArray("average");
                    for(int i = 0; i < jar.length(); i++){
                        listAvgTrans.add(jar.getJSONObject(i).getString("name"));
                        listAvgTransID.add(jar.getJSONObject(i).getString("id"));
                    }
                    ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, listAvgTrans);
                    auto_avg_trans.setAdapter(adapter);
                    auto_avg_trans.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                            avgTrans = listAvgTransID.get(x).toString();
                            Log.e("avgTrans", avgTrans);
                            txtAvgTrans.setError(null);
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
