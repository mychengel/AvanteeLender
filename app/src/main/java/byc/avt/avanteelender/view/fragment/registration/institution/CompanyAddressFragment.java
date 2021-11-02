package byc.avt.avanteelender.view.fragment.registration.institution;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.LinearLayout;

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

public class CompanyAddressFragment extends Fragment {

    public CompanyAddressFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_company_address, container, false);
    }

    Button btn_next;

    private MasterDataViewModel viewModel;
    private PrefManager prefManager;
    private Dialog dialog;
    GlobalVariables gv;
    AutoCompleteTextView auto_aktaCountry, auto_aktaProvince, auto_aktaCity, auto_aktaDistrict, auto_aktaUrban, auto_domicileCountry,
            auto_domicileProvince, auto_domicileCity, auto_domicileDistrict, auto_domicileUrban;
    TextInputLayout txtKtpAddress, txtKtpCountry, txtKtpProvince, txtKtpCity,
            txtKtpDistrict, txtKtpUrban, txtKtpRT, txtKtpRW, txtKtpPostalCode;
    TextInputLayout txtDomicileAddress, txtDomicileCountry, txtDomicileProvince, txtDomicileCity,
            txtDomicileDistrict, txtDomicileUrban, txtDomicileRT, txtDomicileRW, txtDomicilePostalCode;
    String aktaAddress="", aktaCountry="", aktaProvince="", aktaCity="", aktaDistrict="", aktaUrban="",
            aktaRT="", aktaRW="", aktaPostalCode="";
    String domicileAddress="", domicileCountry="", domicileProvince="", domicileCity="", domicileDistrict="",
            domicileUrban="", domicileRT="", domicileRW="", domicilePostalCode="";
    boolean is_domicile_same_as_akta = false;

    List<Object> listCountryAkta = new ArrayList<>(); List<Object> listCountryIDAkta = new ArrayList<>();
    List<Object> listProvinceAkta = new ArrayList<>(); List<Object> listProvinceIDAkta = new ArrayList<>();
    List<Object> listCityAkta = new ArrayList<>(); List<Object> listCityIDAkta = new ArrayList<>();
    List<Object> listDistrictAkta = new ArrayList<>(); List<Object> listDistrictIDAkta = new ArrayList<>();
    List<Object> listUrbanAkta = new ArrayList<>(); List<Object> listUrbanIDAkta = new ArrayList<>();
    List<Object> listCountryDomicile = new ArrayList<>(); List<Object> listCountryIDDomicile = new ArrayList<>();
    List<Object> listProvinceDomicile = new ArrayList<>(); List<Object> listProvinceIDDomicile = new ArrayList<>();
    List<Object> listCityDomicile = new ArrayList<>(); List<Object> listCityIDDomicile = new ArrayList<>();
    List<Object> listDistrictDomicile = new ArrayList<>(); List<Object> listDistrictIDDomicile = new ArrayList<>();
    List<Object> listUrbanDomicile = new ArrayList<>(); List<Object> listUrbanIDDomicile = new ArrayList<>();

    LinearLayout lin_domicile_area;
    CheckBox cb_domicile_same_as_akta;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(MasterDataViewModel.class);
        prefManager = PrefManager.getInstance(getActivity());
        dialog = GlobalVariables.loadingDialog(requireActivity());

        gv.stInsBankInfo = false;

        auto_aktaCountry = view.findViewById(R.id.auto_akta_country_fr_com_address);
        auto_aktaProvince = view.findViewById(R.id.auto_akta_province_fr_com_address);
        auto_aktaCity = view.findViewById(R.id.auto_akta_city_fr_com_address);
        auto_aktaDistrict = view.findViewById(R.id.auto_akta_kecamatan_fr_com_address);
        auto_aktaUrban = view.findViewById(R.id.auto_akta_kelurahan_fr_com_address);
        auto_domicileCountry = view.findViewById(R.id.auto_operasional_country_fr_com_address);
        auto_domicileProvince = view.findViewById(R.id.auto_operasional_province_fr_com_address);
        auto_domicileCity = view.findViewById(R.id.auto_operasional_city_fr_com_address);
        auto_domicileDistrict = view.findViewById(R.id.auto_operasional_kecamatan_fr_com_address);
        auto_domicileUrban = view.findViewById(R.id.auto_operasional_kelurahan_fr_com_address);
        txtKtpAddress = view.findViewById(R.id.edit_akta_address_fr_com_address);
        txtKtpCountry = view.findViewById(R.id.edit_akta_country_fr_com_address);
        txtKtpProvince = view.findViewById(R.id.edit_akta_province_fr_com_address);
        txtKtpCity = view.findViewById(R.id.edit_akta_city_fr_com_address);
        txtKtpDistrict = view.findViewById(R.id.edit_akta_kecamatan_fr_com_address);
        txtKtpUrban = view.findViewById(R.id.edit_akta_kelurahan_fr_com_address);
//        txtKtpRT = view.findViewById(R.id.edit_akta_rt_fr_com_address);
//        txtKtpRW = view.findViewById(R.id.edit_akta_rw_fr_com_address);
        txtKtpPostalCode = view.findViewById(R.id.edit_akta_kodepos_fr_com_address);
        txtDomicileAddress = view.findViewById(R.id.edit_operasional_address_fr_com_address);
        txtDomicileCountry = view.findViewById(R.id.edit_operasional_country_fr_com_address);
        txtDomicileProvince = view.findViewById(R.id.edit_operasional_province_fr_com_address);
        txtDomicileCity = view.findViewById(R.id.edit_operasional_city_fr_com_address);
        txtDomicileDistrict = view.findViewById(R.id.edit_operasional_kecamatan_fr_com_address);
        txtDomicileUrban = view.findViewById(R.id.edit_operasional_kelurahan_fr_com_address);
//        txtDomicileRT = view.findViewById(R.id.edit_operasional_rt_fr_com_address);
//        txtDomicileRW = view.findViewById(R.id.edit_operasional_rw_fr_com_address);
        txtDomicilePostalCode = view.findViewById(R.id.edit_operasional_kodepos_fr_com_address);
        lin_domicile_area = view.findViewById(R.id.lin_operasional_fr_com_address);
        cb_domicile_same_as_akta = view.findViewById(R.id.cb_operasional_same_as_akta_fr_com_address);
        cb_domicile_same_as_akta.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    is_domicile_same_as_akta = true;
                    lin_domicile_area.setVisibility(View.GONE);
                }else{
                    is_domicile_same_as_akta = false;
                    lin_domicile_area.setVisibility(View.VISIBLE);
                }
            }
        });


        txtKtpPostalCode.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                aktaPostalCode = charSequence.toString();
                cekPostal();
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        txtDomicilePostalCode.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                domicilePostalCode = charSequence.toString();
                cekPostal();
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        btn_next = view.findViewById(R.id.btn_next_fr_com_address);
        btn_next.setEnabled(true);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmNext(v);
                //Navigation.findNavController(v).navigate(R.id.action_companyAddressFragment_to_companyBankFragment);
            }
        });

        loadData();
    }

    public void cekPostal(){
        if(aktaPostalCode.length() > 5){txtKtpPostalCode.setError(getString(R.string.postal_code_max_char));}else{txtKtpPostalCode.setError(null);}
        if(domicilePostalCode.length() > 5){txtDomicilePostalCode.setError(getString(R.string.postal_code_max_char));}else{txtDomicilePostalCode.setError(null);}
    }


    public void clearMasterList(){
        listCountryAkta.clear();listCountryIDAkta.clear();
        listProvinceAkta.clear();listProvinceIDAkta.clear();
        listCityAkta.clear();listCityIDAkta.clear();
        listCountryDomicile.clear();listCountryIDDomicile.clear();
        listProvinceDomicile.clear();listProvinceIDDomicile.clear();
        listCityDomicile.clear();listCityIDDomicile.clear();
    }

    public void loadData(){
        clearMasterList();
        dialog.show();
        if(gv.stPerAddressData){
            aktaAddress = gv.insRegData.get("alamat_akta").toString();
            txtKtpAddress.getEditText().setText(aktaAddress);
            aktaCountry = gv.insRegData.get("negara_akta").toString();
            aktaProvince = gv.insRegData.get("propinsi_akta").toString();
            aktaCity = gv.insRegData.get("kota_akta").toString();
            aktaDistrict = gv.insRegData.get("kecamatan_akta").toString();
            txtKtpDistrict.getEditText().setText(aktaDistrict);
            aktaUrban = gv.insRegData.get("kelurahan_akta").toString();
            txtKtpUrban.getEditText().setText(aktaUrban);
//            aktaRT = gv.insRegData.get("rt_akta").toString();
//            txtKtpRT.getEditText().setText(aktaRT);
//            aktaRW = gv.insRegData.get("rw_akta").toString();
//            txtKtpRW.getEditText().setText(aktaRW);
            aktaPostalCode = gv.insRegData.get("kode_pos_akta").toString();
            txtKtpPostalCode.getEditText().setText(aktaPostalCode);
            domicileAddress = gv.insRegData.get("alamat_operasional").toString();
            txtDomicileAddress.getEditText().setText(domicileAddress);
            domicileCountry = gv.insRegData.get("negara_operasional").toString();
            domicileProvince = gv.insRegData.get("propinsi_operasional").toString();
            domicileCity = gv.insRegData.get("kota_operasional").toString();
            domicileDistrict = gv.insRegData.get("kecamatan_operasional").toString();
            txtDomicileDistrict.getEditText().setText(domicileDistrict);
            domicileUrban = gv.insRegData.get("kelurahan_operasional").toString();
            txtDomicileUrban.getEditText().setText(domicileUrban);
//            domicileRT = gv.insRegData.get("rt_operasional").toString();
//            txtDomicileRT.getEditText().setText(domicileRT);
//            domicileRW = gv.insRegData.get("rw_operasional").toString();
//            txtDomicileRW.getEditText().setText(domicileRW);
            domicilePostalCode = gv.insRegData.get("kode_pos_operasional").toString();
            txtDomicilePostalCode.getEditText().setText(domicilePostalCode);
        }else{}
        viewModel.getCountry(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultCountry().observe(getActivity(), showCountryAkta);
        viewModel.getResultCountry().observe(getActivity(), showCountryDom);
        viewModel.getProvince(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultProvince().observe(getActivity(), showProvinceAkta);
        viewModel.getResultProvince().observe(getActivity(), showProvinceDom);
    }

    private void confirmNext(View v){
        aktaAddress = Objects.requireNonNull(txtKtpAddress.getEditText().getText().toString().trim());
//        aktaRT = Objects.requireNonNull(txtKtpRT.getEditText().getText().toString().trim());
//        aktaRW = Objects.requireNonNull(txtKtpRW.getEditText().getText().toString().trim());
        aktaPostalCode = Objects.requireNonNull(txtKtpPostalCode.getEditText().getText().toString().trim());

        if(is_domicile_same_as_akta){
            domicileAddress = aktaAddress;
            domicileCountry = aktaCountry;
            domicileProvince = aktaProvince;
            domicileCity = aktaCity;
            domicileDistrict = aktaDistrict;
            domicileUrban = aktaUrban;
//            domicileRT = aktaRT;
//            domicileRW = aktaRW;
            domicilePostalCode = aktaPostalCode;
        }else{
            domicileAddress = Objects.requireNonNull(txtDomicileAddress.getEditText().getText().toString().trim());
//            domicileRT = Objects.requireNonNull(txtDomicileRT.getEditText().getText().toString().trim());
//            domicileRW = Objects.requireNonNull(txtDomicileRW.getEditText().getText().toString().trim());
            domicilePostalCode = Objects.requireNonNull(txtDomicilePostalCode.getEditText().getText().toString().trim());
        }

        if(domicilePostalCode.length() <= 5 && aktaPostalCode.length() <= 5 && !aktaAddress.isEmpty() && !aktaCountry.isEmpty() && !aktaProvince.isEmpty() && !aktaCity.isEmpty()
                && !aktaDistrict.isEmpty() && !aktaUrban.isEmpty() && !aktaPostalCode.isEmpty() && !domicileAddress.isEmpty()
                && !domicileCountry.isEmpty() && !domicileProvince.isEmpty() && !domicileCity.isEmpty()
                && !domicileDistrict.isEmpty() && !domicileUrban.isEmpty() && !domicilePostalCode.isEmpty()){
            gv.stPerAddressData = true;
            gv.insRegData.put("alamat_akta",aktaAddress);
            gv.insRegData.put("negara_akta",aktaCountry);
            gv.insRegData.put("propinsi_akta",aktaProvince);
            gv.insRegData.put("kota_akta",aktaCity);
            gv.insRegData.put("kecamatan_akta",aktaDistrict);
            gv.insRegData.put("kelurahan_akta",aktaUrban);
//            gv.insRegData.put("rt_akta",aktaRT);
//            gv.insRegData.put("rw_akta",aktaRW);
            gv.insRegData.put("kode_pos_akta",aktaPostalCode);
            gv.insRegData.put("alamat_operasional",domicileAddress);
            gv.insRegData.put("negara_operasional",domicileCountry);
            gv.insRegData.put("propinsi_operasional",domicileProvince);
            gv.insRegData.put("kota_operasional",domicileCity);
            gv.insRegData.put("kecamatan_operasional",domicileDistrict);
            gv.insRegData.put("kelurahan_operasional",domicileUrban);
//            gv.insRegData.put("rt_operasional",domicileRT);
//            gv.insRegData.put("rw_operasional",domicileRW);
            gv.insRegData.put("kode_pos_operasional",domicilePostalCode);
            setNoError();
            Navigation.findNavController(v).navigate(R.id.action_companyAddressFragment_to_companyBankFragment);
        }else{
            cekError();
        }
    }

    private void setNoError(){
        txtKtpAddress.setError(null);
        txtKtpCountry.setError(null);
        txtKtpProvince.setError(null);
        txtKtpCity.setError(null);
        txtKtpDistrict.setError(null);
        txtKtpUrban.setError(null);
//        txtKtpRT.setError(null);
//        txtKtpRW.setError(null);
        txtKtpPostalCode.setError(null);
        txtDomicileAddress.setError(null);
        txtDomicileCountry.setError(null);
        txtDomicileProvince.setError(null);
        txtDomicileCity.setError(null);
        txtDomicileDistrict.setError(null);
        txtDomicileUrban.setError(null);
//        txtDomicileRT.setError(null);
//        txtDomicileRW.setError(null);
        txtDomicilePostalCode.setError(null);
    }

    private void cekError(){
        if(aktaAddress.isEmpty()){txtKtpAddress.setError(getString(R.string.cannotnull));}else{txtKtpAddress.setError(null);}
        if(aktaCountry.isEmpty()){txtKtpCountry.setError(getString(R.string.cannotnull));}else{txtKtpCountry.setError(null);}
        if(aktaProvince.isEmpty()){txtKtpProvince.setError(getString(R.string.cannotnull));}else{txtKtpProvince.setError(null);}
        if(aktaCity.isEmpty()){txtKtpCity.setError(getString(R.string.cannotnull));}else{txtKtpCity.setError(null);}
        if(aktaDistrict.isEmpty()){txtKtpDistrict.setError(getString(R.string.cannotnull));}else{txtKtpDistrict.setError(null);}
        if(aktaUrban.isEmpty()){txtKtpUrban.setError(getString(R.string.cannotnull));}else{txtKtpUrban.setError(null);}
//        if(aktaRT.isEmpty()){txtKtpRT.setError(getString(R.string.cannotnull));}else{txtKtpRT.setError(null);}
//        if(aktaRW.isEmpty()){txtKtpRW.setError(getString(R.string.cannotnull));}else{txtKtpRW.setError(null);}
        if(aktaPostalCode.isEmpty()){txtKtpPostalCode.setError(getString(R.string.cannotnull));}else{txtKtpPostalCode.setError(null);}
        if(domicileAddress.isEmpty()){txtDomicileAddress.setError(getString(R.string.cannotnull));}else{txtDomicileAddress.setError(null);}
        if(domicileCountry.isEmpty()){txtDomicileCountry.setError(getString(R.string.cannotnull));}else{txtDomicileCountry.setError(null);}
        if(domicileProvince.isEmpty()){txtDomicileProvince.setError(getString(R.string.cannotnull));}else{txtDomicileProvince.setError(null);}
        if(domicileCity.isEmpty()){txtDomicileCity.setError(getString(R.string.cannotnull));}else{txtDomicileCity.setError(null);}
        if(domicileDistrict.isEmpty()){txtDomicileDistrict.setError(getString(R.string.cannotnull));}else{txtDomicileDistrict.setError(null);}
        if(domicileUrban.isEmpty()){txtDomicileUrban.setError(getString(R.string.cannotnull));}else{txtDomicileUrban.setError(null);}
//        if(domicileRT.isEmpty()){txtDomicileRT.setError(getString(R.string.cannotnull));}else{txtDomicileRT.setError(null);}
//        if(domicileRW.isEmpty()){txtDomicileRW.setError(getString(R.string.cannotnull));}else{txtDomicileRW.setError(null);}
        if(domicilePostalCode.isEmpty()){txtDomicilePostalCode.setError(getString(R.string.cannotnull));}else{txtDomicilePostalCode.setError(null);}
    }

    private Observer<JSONObject> showCountryAkta = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    JSONObject jobRes = result.getJSONObject("result");
                    JSONArray jar = jobRes.getJSONArray("negara");
                    for(int i = 0; i < jar.length(); i++){
                        listCountryAkta.add(jar.getJSONObject(i).getString("name"));
                        listCountryIDAkta.add(jar.getJSONObject(i).getString("id"));
                    }
                    ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, listCountryAkta);
                    auto_aktaCountry.setAdapter(adapter);
                    auto_aktaCountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                            aktaCountry = listCountryIDAkta.get(x).toString();
                            Log.e("aktaCountry", aktaCountry);
                            txtKtpCountry.setError(null);
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

    private Observer<JSONObject> showProvinceAkta = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    JSONObject jobRes = result.getJSONObject("result");
                    JSONArray jar = jobRes.getJSONArray("province");
                    for(int i = 0; i < jar.length(); i++){
                        listProvinceAkta.add(jar.getJSONObject(i).getString("name"));
                        listProvinceIDAkta.add(jar.getJSONObject(i).getString("id"));
                    }
                    ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, listProvinceAkta);
                    auto_aktaProvince.setAdapter(adapter);
                    auto_aktaProvince.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                            aktaProvince = listProvinceIDAkta.get(x).toString();
                            Log.e("aktaProvince", aktaProvince);
                            txtKtpProvince.setError(null);
                            viewModel.getCity(prefManager.getUid(), prefManager.getToken(), aktaProvince);
                            viewModel.getResultCity().observe(getActivity(), showCityAkta);
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

    private Observer<JSONObject> showCityAkta = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            listCityAkta.clear();
            listCityIDAkta.clear();
            try {
                if(result.getInt("code") == 200){
                    JSONObject jobRes = result.getJSONObject("result");
                    JSONArray jar = jobRes.getJSONArray("city");
                    for(int i = 0; i < jar.length(); i++){
                        listCityAkta.add(jar.getJSONObject(i).getString("name"));
                        listCityIDAkta.add(jar.getJSONObject(i).getString("id"));
                    }
                    ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, listCityAkta);
                    auto_aktaCity.setAdapter(adapter);
                    auto_aktaCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                            aktaCity = listCityIDAkta.get(x).toString();
                            Log.e("aktaCity", aktaCity);
                            txtKtpCity.setError(null);
                            viewModel.getDistrict(prefManager.getUid(), prefManager.getToken(), aktaCity);
                            viewModel.getResultDistrict().observe(getActivity(), showDistrictAkta);
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

    private Observer<JSONObject> showDistrictAkta = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            listDistrictAkta.clear();
            listDistrictIDAkta.clear();
            try {
                if(result.getInt("code") == 200){
                    JSONObject jobRes = result.getJSONObject("result");
                    JSONArray jar = jobRes.getJSONArray("district");
                    for(int i = 0; i < jar.length(); i++){
                        listDistrictAkta.add(jar.getJSONObject(i).getString("name"));
                        listDistrictIDAkta.add(jar.getJSONObject(i).getString("id"));
                    }
                    ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, listDistrictAkta);
                    auto_aktaDistrict.setAdapter(adapter);
                    auto_aktaDistrict.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                            aktaDistrict = listDistrictIDAkta.get(x).toString();
                            Log.e("aktaDistrict", aktaDistrict);
                            txtKtpDistrict.setError(null);
                            viewModel.getUrban(prefManager.getUid(), prefManager.getToken(), aktaDistrict);
                            viewModel.getResultUrban().observe(getActivity(), showUrbanAkta);
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

    private Observer<JSONObject> showUrbanAkta = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            listUrbanAkta.clear();
            listUrbanIDAkta.clear();
            try {
                if(result.getInt("code") == 200){
                    JSONObject jobRes = result.getJSONObject("result");
                    JSONArray jar = jobRes.getJSONArray("villages");
                    for(int i = 0; i < jar.length(); i++){
                        listUrbanAkta.add(jar.getJSONObject(i).getString("name"));
                        listUrbanIDAkta.add(jar.getJSONObject(i).getString("id"));
                    }
                    ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, listUrbanAkta);
                    auto_aktaUrban.setAdapter(adapter);
                    auto_aktaUrban.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                            aktaUrban = listUrbanIDAkta.get(x).toString();
                            Log.e("aktaUrban", aktaUrban);
                            txtKtpUrban.setError(null);
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

    private Observer<JSONObject> showCountryDom = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    JSONObject jobRes = result.getJSONObject("result");
                    JSONArray jar = jobRes.getJSONArray("negara");
                    for(int i = 0; i < jar.length(); i++){
                        listCountryDomicile.add(jar.getJSONObject(i).getString("name"));
                        listCountryIDDomicile.add(jar.getJSONObject(i).getString("id"));
                    }
                    ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, listCountryDomicile);
                    auto_domicileCountry.setAdapter(adapter);
                    auto_domicileCountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                            domicileCountry = listCountryIDDomicile.get(x).toString();
                            Log.e("domCountry", domicileCountry);
                            txtDomicileCountry.setError(null);
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

    private Observer<JSONObject> showProvinceDom = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    JSONObject jobRes = result.getJSONObject("result");
                    JSONArray jar = jobRes.getJSONArray("province");
                    for(int i = 0; i < jar.length(); i++){
                        listProvinceDomicile.add(jar.getJSONObject(i).getString("name"));
                        listProvinceIDDomicile.add(jar.getJSONObject(i).getString("id"));
                    }
                    ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, listProvinceDomicile);
                    auto_domicileProvince.setAdapter(adapter);
                    auto_domicileProvince.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                            domicileProvince = listProvinceIDDomicile.get(x).toString();
                            Log.e("domProvince", domicileProvince);
                            txtDomicileProvince.setError(null);
                            viewModel.getCity(prefManager.getUid(), prefManager.getToken(), domicileProvince);
                            viewModel.getResultCity().observe(getActivity(), showCityDom);
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

    private Observer<JSONObject> showCityDom = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            listCityDomicile.clear();
            listCityIDDomicile.clear();
            try {
                if(result.getInt("code") == 200){
                    JSONObject jobRes = result.getJSONObject("result");
                    JSONArray jar = jobRes.getJSONArray("city");
                    for(int i = 0; i < jar.length(); i++){
                        listCityDomicile.add(jar.getJSONObject(i).getString("name"));
                        listCityIDDomicile.add(jar.getJSONObject(i).getString("id"));
                    }
                    ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, listCityDomicile);
                    auto_domicileCity.setAdapter(adapter);
                    auto_domicileCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                            domicileCity = listCityIDDomicile.get(x).toString();
                            Log.e("domCity", domicileCity);
                            txtDomicileCity.setError(null);
                            viewModel.getDistrict(prefManager.getUid(), prefManager.getToken(), domicileCity);
                            viewModel.getResultDistrict().observe(getActivity(), showDistrictDom);
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

    private Observer<JSONObject> showDistrictDom = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            listDistrictDomicile.clear();
            listDistrictIDDomicile.clear();
            try {
                if(result.getInt("code") == 200){
                    JSONObject jobRes = result.getJSONObject("result");
                    JSONArray jar = jobRes.getJSONArray("district");
                    for(int i = 0; i < jar.length(); i++){
                        listDistrictDomicile.add(jar.getJSONObject(i).getString("name"));
                        listDistrictIDDomicile.add(jar.getJSONObject(i).getString("id"));
                    }
                    ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, listDistrictDomicile);
                    auto_domicileDistrict.setAdapter(adapter);
                    auto_domicileDistrict.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                            domicileDistrict = listDistrictIDDomicile.get(x).toString();
                            Log.e("domDistrict", domicileDistrict);
                            txtDomicileDistrict.setError(null);
                            viewModel.getUrban(prefManager.getUid(), prefManager.getToken(), domicileDistrict);
                            viewModel.getResultUrban().observe(getActivity(), showUrbanDom);
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

    private Observer<JSONObject> showUrbanDom = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            listUrbanDomicile.clear();
            listUrbanIDDomicile.clear();
            try {
                if(result.getInt("code") == 200){
                    JSONObject jobRes = result.getJSONObject("result");
                    JSONArray jar = jobRes.getJSONArray("villages");
                    for(int i = 0; i < jar.length(); i++){
                        listUrbanDomicile.add(jar.getJSONObject(i).getString("name"));
                        listUrbanIDDomicile.add(jar.getJSONObject(i).getString("id"));
                    }
                    ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, listUrbanDomicile);
                    auto_domicileUrban.setAdapter(adapter);
                    auto_domicileUrban.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                            domicileUrban = listUrbanIDDomicile.get(x).toString();
                            Log.e("domUrban", domicileUrban);
                            txtDomicileUrban.setError(null);
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