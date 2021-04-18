package byc.avt.avanteelender.view.fragment.registration.individual;

//import android.app.Fragment;
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
import android.widget.EditText;
import android.widget.LinearLayout;
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
public class AddressDataFragment extends Fragment {

    public AddressDataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_address_data, container, false);
    }

    private MasterDataViewModel viewModel;
    private PrefManager prefManager;
    private Dialog dialog;
    GlobalVariables gv;
    AutoCompleteTextView auto_ktpCountry, auto_ktpProvince, auto_ktpCity, auto_ktpDistrict, auto_ktpUrban, auto_domicileCountry,
            auto_domicileProvince, auto_domicileCity, auto_domicileDistrict, auto_domicileUrban;
    TextInputLayout txtKtpAddress, txtKtpCountry, txtKtpProvince, txtKtpCity,
            txtKtpDistrict, txtKtpUrban, txtKtpRT, txtKtpRW, txtKtpPostalCode;
    TextInputLayout txtDomicileAddress, txtDomicileCountry, txtDomicileProvince, txtDomicileCity,
            txtDomicileDistrict, txtDomicileUrban, txtDomicileRT, txtDomicileRW, txtDomicilePostalCode;
    String ktpAddress="", ktpCountry="", ktpProvince="", ktpCity="", ktpDistrict="", ktpUrban="",
            ktpRT="", ktpRW="", ktpPostalCode="";
    String domicileAddress="", domicileCountry="", domicileProvince="", domicileCity="", domicileDistrict="",
            domicileUrban="", domicileRT="", domicileRW="", domicilePostalCode="";
    boolean is_domicile_same_as_ktp = false;

    List<Object> listCountryKTP = new ArrayList<>(); List<Object> listCountryIDKTP = new ArrayList<>();
    List<Object> listProvinceKTP = new ArrayList<>(); List<Object> listProvinceIDKTP = new ArrayList<>();
    List<Object> listCityKTP = new ArrayList<>(); List<Object> listCityIDKTP = new ArrayList<>();
    List<Object> listDistrictKTP = new ArrayList<>(); List<Object> listDistrictIDKTP = new ArrayList<>();
    List<Object> listUrbanKTP = new ArrayList<>(); List<Object> listUrbanIDKTP = new ArrayList<>();
    List<Object> listCountryDomicile = new ArrayList<>(); List<Object> listCountryIDDomicile = new ArrayList<>();
    List<Object> listProvinceDomicile = new ArrayList<>(); List<Object> listProvinceIDDomicile = new ArrayList<>();
    List<Object> listCityDomicile = new ArrayList<>(); List<Object> listCityIDDomicile = new ArrayList<>();
    List<Object> listDistrictDomicile = new ArrayList<>(); List<Object> listDistrictIDDomicile = new ArrayList<>();
    List<Object> listUrbanDomicile = new ArrayList<>(); List<Object> listUrbanIDDomicile = new ArrayList<>();

    Button btn_next;
    LinearLayout lin_domicile_area;
    CheckBox cb_domicile_same_as_ktp;

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MasterDataViewModel.class);
        prefManager = PrefManager.getInstance(getActivity());
        dialog = GlobalVariables.loadingDialog(requireActivity());

        gv.stPerBankInfo = false;

        auto_ktpCountry = view.findViewById(R.id.auto_ktp_country_fr_address_data);
        auto_ktpProvince = view.findViewById(R.id.auto_ktp_province_fr_address_data);
        auto_ktpCity = view.findViewById(R.id.auto_ktp_city_fr_address_data);
        auto_ktpDistrict = view.findViewById(R.id.auto_ktp_kecamatan_fr_address_data);
        auto_ktpUrban = view.findViewById(R.id.auto_ktp_kelurahan_fr_address_data);
        auto_domicileCountry = view.findViewById(R.id.auto_domicile_country_fr_address_data);
        auto_domicileProvince = view.findViewById(R.id.auto_domicile_province_fr_address_data);
        auto_domicileCity = view.findViewById(R.id.auto_domicile_city_fr_address_data);
        auto_domicileDistrict = view.findViewById(R.id.auto_domicile_kecamatan_fr_address_data);
        auto_domicileUrban = view.findViewById(R.id.auto_domicile_kelurahan_fr_address_data);
        txtKtpAddress = view.findViewById(R.id.edit_ktp_address_fr_address_data);
        txtKtpCountry = view.findViewById(R.id.edit_ktp_country_fr_address_data);
        txtKtpProvince = view.findViewById(R.id.edit_ktp_province_fr_address_data);
        txtKtpCity = view.findViewById(R.id.edit_ktp_city_fr_address_data);
        txtKtpDistrict = view.findViewById(R.id.edit_ktp_kecamatan_fr_address_data);
        txtKtpUrban = view.findViewById(R.id.edit_ktp_kelurahan_fr_address_data);
        txtKtpRT = view.findViewById(R.id.edit_ktp_rt_fr_address_data);
        txtKtpRW = view.findViewById(R.id.edit_ktp_rw_fr_address_data);
        txtKtpPostalCode = view.findViewById(R.id.edit_ktp_kodepos_fr_address_data);
        txtDomicileAddress = view.findViewById(R.id.edit_domicile_address_fr_address_data);
        txtDomicileCountry = view.findViewById(R.id.edit_domicile_country_fr_address_data);
        txtDomicileProvince = view.findViewById(R.id.edit_domicile_province_fr_address_data);
        txtDomicileCity = view.findViewById(R.id.edit_domicile_city_fr_address_data);
        txtDomicileDistrict = view.findViewById(R.id.edit_domicile_kecamatan_fr_address_data);
        txtDomicileUrban = view.findViewById(R.id.edit_domicile_kelurahan_fr_address_data);
        txtDomicileRT = view.findViewById(R.id.edit_domicile_rt_fr_address_data);
        txtDomicileRW = view.findViewById(R.id.edit_domicile_rw_fr_address_data);
        txtDomicilePostalCode = view.findViewById(R.id.edit_domicile_kodepos_fr_address_data);
        lin_domicile_area = view.findViewById(R.id.lin_domicile_fr_address_data);
        cb_domicile_same_as_ktp = view.findViewById(R.id.cb_domicile_same_as_ktp_fr_address_data);
        cb_domicile_same_as_ktp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    is_domicile_same_as_ktp = true;
                    lin_domicile_area.setVisibility(View.GONE);
                }else{
                    is_domicile_same_as_ktp = false;
                    lin_domicile_area.setVisibility(View.VISIBLE);
                }
            }
        });

        btn_next = view.findViewById(R.id.btn_next_fr_address_data);
        btn_next.setEnabled(true);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //confirmNext(v);
                Navigation.findNavController(v).navigate(R.id.action_addressDataFragment_to_bankInfoFragment);
            }
        });

        loadData();
    }

    public void clearMasterList(){
        listCountryKTP.clear();listCountryIDKTP.clear();
        listProvinceKTP.clear();listProvinceIDKTP.clear();
        listCityKTP.clear();listCityIDKTP.clear();
        listCountryDomicile.clear();listCountryIDDomicile.clear();
        listProvinceDomicile.clear();listProvinceIDDomicile.clear();
        listCityDomicile.clear();listCityIDDomicile.clear();
    }

    public void loadData(){
        clearMasterList();
        dialog.show();
        if(gv.stPerAddressData){
            ktpAddress = gv.perRegData.get("alamat_ktp").toString();
            txtKtpAddress.getEditText().setText(ktpAddress);
            ktpCountry = gv.perRegData.get("negara_ktp").toString();
            ktpProvince = gv.perRegData.get("provinsi_ktp").toString();
            ktpCity = gv.perRegData.get("kota_ktp").toString();
            ktpDistrict = gv.perRegData.get("kecamatan_ktp").toString();
            txtKtpDistrict.getEditText().setText(ktpDistrict);
            ktpUrban = gv.perRegData.get("kelurahan_ktp").toString();
            txtKtpUrban.getEditText().setText(ktpUrban);
            ktpRT = gv.perRegData.get("rt_ktp").toString();
            txtKtpRT.getEditText().setText(ktpRT);
            ktpRW = gv.perRegData.get("rw_ktp").toString();
            txtKtpRW.getEditText().setText(ktpRW);
            ktpPostalCode = gv.perRegData.get("kode_pos_ktp").toString();
            txtKtpPostalCode.getEditText().setText(ktpPostalCode);
            domicileAddress = gv.perRegData.get("alamat_tempat_tinggal").toString();
            txtDomicileAddress.getEditText().setText(domicileAddress);
            domicileCountry = gv.perRegData.get("negara_tempat_tinggal").toString();
            domicileProvince = gv.perRegData.get("provinsi_tempat_tinggal").toString();
            domicileCity = gv.perRegData.get("kota_tempat_tinggal").toString();
            domicileDistrict = gv.perRegData.get("kecamatan_tempat_tinggal").toString();
            txtDomicileDistrict.getEditText().setText(domicileDistrict);
            domicileUrban = gv.perRegData.get("kelurahan_tempat_tinggal").toString();
            txtDomicileUrban.getEditText().setText(domicileUrban);
            domicileRT = gv.perRegData.get("rt_tempat_tinggal").toString();
            txtDomicileRT.getEditText().setText(domicileRT);
            domicileRW = gv.perRegData.get("rw_tempat_tinggal").toString();
            txtDomicileRW.getEditText().setText(domicileRW);
            domicilePostalCode = gv.perRegData.get("kode_pos_tempat_tinggal").toString();
            txtDomicilePostalCode.getEditText().setText(domicilePostalCode);
        }else{}
        viewModel.getCountry(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultCountry().observe(getActivity(), showCountryKTP);
        viewModel.getResultCountry().observe(getActivity(), showCountryDom);
        viewModel.getProvince(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultProvince().observe(getActivity(), showProvinceKTP);
        viewModel.getResultProvince().observe(getActivity(), showProvinceDom);
    }

    private void confirmNext(View v){
        ktpAddress = Objects.requireNonNull(txtKtpAddress.getEditText().getText().toString().trim());
//        ktpDistrict = Objects.requireNonNull(txtKtpDistrict.getEditText().getText().toString().trim());
//        ktpUrban = Objects.requireNonNull(txtKtpUrban.getEditText().getText().toString().trim());
        ktpRT = Objects.requireNonNull(txtKtpRT.getEditText().getText().toString().trim());
        ktpRW = Objects.requireNonNull(txtKtpRW.getEditText().getText().toString().trim());
        ktpPostalCode = Objects.requireNonNull(txtKtpPostalCode.getEditText().getText().toString().trim());

        if(is_domicile_same_as_ktp){
            domicileAddress = ktpAddress;
            domicileCountry = ktpCountry;
            domicileProvince = ktpProvince;
            domicileCity = ktpCity;
            domicileDistrict = ktpDistrict;
            domicileUrban = ktpUrban;
            domicileRT = ktpRT;
            domicileRW = ktpRW;
            domicilePostalCode = ktpPostalCode;
        }else{
            domicileAddress = Objects.requireNonNull(txtDomicileAddress.getEditText().getText().toString().trim());
//            domicileDistrict = Objects.requireNonNull(txtDomicileDistrict.getEditText().getText().toString().trim());
//            domicileUrban = Objects.requireNonNull(txtDomicileUrban.getEditText().getText().toString().trim());
            domicileRT = Objects.requireNonNull(txtDomicileRT.getEditText().getText().toString().trim());
            domicileRW = Objects.requireNonNull(txtDomicileRW.getEditText().getText().toString().trim());
            domicilePostalCode = Objects.requireNonNull(txtDomicilePostalCode.getEditText().getText().toString().trim());
        }

        if(!ktpAddress.isEmpty() && !ktpCountry.isEmpty() && !ktpProvince.isEmpty() && !ktpCity.isEmpty()
                && !ktpDistrict.isEmpty() && !ktpUrban.isEmpty() && !ktpRT.isEmpty()
                && !ktpRW.isEmpty() && !ktpPostalCode.isEmpty() && !domicileAddress.isEmpty()
                && !domicileCountry.isEmpty() && !domicileProvince.isEmpty() && !domicileCity.isEmpty()
                && !domicileDistrict.isEmpty() && !domicileUrban.isEmpty() && !domicileRT.isEmpty()
                && !domicileRW.isEmpty() && !domicilePostalCode.isEmpty()){
            gv.stPerAddressData = true;
            gv.perRegData.put("alamat_ktp",ktpAddress);
            gv.perRegData.put("negara_ktp",ktpCountry);
            gv.perRegData.put("provinsi_ktp",ktpProvince);
            gv.perRegData.put("kota_ktp",ktpCity);
            gv.perRegData.put("kecamatan_ktp",ktpDistrict);
            gv.perRegData.put("kelurahan_ktp",ktpUrban);
            gv.perRegData.put("rt_ktp",ktpRT);
            gv.perRegData.put("rw_ktp",ktpRW);
            gv.perRegData.put("kode_pos_ktp",ktpPostalCode);
            gv.perRegData.put("alamat_tempat_tinggal",domicileAddress);
            gv.perRegData.put("negara_tempat_tinggal",domicileCountry);
            gv.perRegData.put("provinsi_tempat_tinggal",domicileProvince);
            gv.perRegData.put("kota_tempat_tinggal",domicileCity);
            gv.perRegData.put("kecamatan_tempat_tinggal",domicileDistrict);
            gv.perRegData.put("kelurahan_tempat_tinggal",domicileUrban);
            gv.perRegData.put("rt_tempat_tinggal",domicileRT);
            gv.perRegData.put("rw_tempat_tinggal",domicileRW);
            gv.perRegData.put("kode_pos_tempat_tinggal",domicilePostalCode);
            setNoError();
            Navigation.findNavController(v).navigate(R.id.action_addressDataFragment_to_bankInfoFragment);
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
        txtKtpRT.setError(null);
        txtKtpRW.setError(null);
        txtKtpPostalCode.setError(null);
        txtDomicileAddress.setError(null);
        txtDomicileCountry.setError(null);
        txtDomicileProvince.setError(null);
        txtDomicileCity.setError(null);
        txtDomicileDistrict.setError(null);
        txtDomicileUrban.setError(null);
        txtDomicileRT.setError(null);
        txtDomicileRW.setError(null);
        txtDomicilePostalCode.setError(null);
    }

    private void cekError(){
        if(ktpAddress.isEmpty()){txtKtpAddress.setError(getString(R.string.cannotnull));}else{txtKtpAddress.setError(null);}
        if(ktpCountry.isEmpty()){txtKtpCountry.setError(getString(R.string.cannotnull));}else{txtKtpCountry.setError(null);}
        if(ktpProvince.isEmpty()){txtKtpProvince.setError(getString(R.string.cannotnull));}else{txtKtpProvince.setError(null);}
        if(ktpCity.isEmpty()){txtKtpCity.setError(getString(R.string.cannotnull));}else{txtKtpCity.setError(null);}
        if(ktpDistrict.isEmpty()){txtKtpDistrict.setError(getString(R.string.cannotnull));}else{txtKtpDistrict.setError(null);}
        if(ktpUrban.isEmpty()){txtKtpUrban.setError(getString(R.string.cannotnull));}else{txtKtpUrban.setError(null);}
        if(ktpRT.isEmpty()){txtKtpRT.setError(getString(R.string.cannotnull));}else{txtKtpRT.setError(null);}
        if(ktpRW.isEmpty()){txtKtpRW.setError(getString(R.string.cannotnull));}else{txtKtpRW.setError(null);}
        if(ktpPostalCode.isEmpty()){txtKtpPostalCode.setError(getString(R.string.cannotnull));}else{txtKtpPostalCode.setError(null);}
        if(domicileAddress.isEmpty()){txtDomicileAddress.setError(getString(R.string.cannotnull));}else{txtDomicileAddress.setError(null);}
        if(domicileCountry.isEmpty()){txtDomicileCountry.setError(getString(R.string.cannotnull));}else{txtDomicileCountry.setError(null);}
        if(domicileProvince.isEmpty()){txtDomicileProvince.setError(getString(R.string.cannotnull));}else{txtDomicileProvince.setError(null);}
        if(domicileCity.isEmpty()){txtDomicileCity.setError(getString(R.string.cannotnull));}else{txtDomicileCity.setError(null);}
        if(domicileDistrict.isEmpty()){txtDomicileDistrict.setError(getString(R.string.cannotnull));}else{txtDomicileDistrict.setError(null);}
        if(domicileUrban.isEmpty()){txtDomicileUrban.setError(getString(R.string.cannotnull));}else{txtDomicileUrban.setError(null);}
        if(domicileRT.isEmpty()){txtDomicileRT.setError(getString(R.string.cannotnull));}else{txtDomicileRT.setError(null);}
        if(domicileRW.isEmpty()){txtDomicileRW.setError(getString(R.string.cannotnull));}else{txtDomicileRW.setError(null);}
        if(domicilePostalCode.isEmpty()){txtDomicilePostalCode.setError(getString(R.string.cannotnull));}else{txtDomicilePostalCode.setError(null);}
    }

    private Observer<JSONObject> showCountryKTP = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    JSONObject jobRes = result.getJSONObject("result");
                    JSONArray jar = jobRes.getJSONArray("negara");
                    for(int i = 0; i < jar.length(); i++){
                        listCountryKTP.add(jar.getJSONObject(i).getString("name"));
                        listCountryIDKTP.add(jar.getJSONObject(i).getString("id"));
                    }
                    ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, listCountryKTP);
                    auto_ktpCountry.setAdapter(adapter);
                    auto_ktpCountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                            ktpCountry = listCountryIDKTP.get(x).toString();
                            Log.e("ktpCountry", ktpCountry);
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

    private Observer<JSONObject> showProvinceKTP = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    JSONObject jobRes = result.getJSONObject("result");
                    JSONArray jar = jobRes.getJSONArray("province");
                    for(int i = 0; i < jar.length(); i++){
                        listProvinceKTP.add(jar.getJSONObject(i).getString("name"));
                        listProvinceIDKTP.add(jar.getJSONObject(i).getString("id"));
                    }
                    ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, listProvinceKTP);
                    auto_ktpProvince.setAdapter(adapter);
                    auto_ktpProvince.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                            ktpProvince = listProvinceIDKTP.get(x).toString();
                            Log.e("ktpProvince", ktpProvince);
                            txtKtpProvince.setError(null);
                            viewModel.getCity(prefManager.getUid(), prefManager.getToken(), ktpProvince);
                            viewModel.getResultCity().observe(getActivity(), showCityKTP);
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

    private Observer<JSONObject> showCityKTP = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            listCityKTP.clear();
            listCityIDKTP.clear();
            try {
                if(result.getInt("code") == 200){
                    JSONObject jobRes = result.getJSONObject("result");
                    JSONArray jar = jobRes.getJSONArray("city");
                    for(int i = 0; i < jar.length(); i++){
                        listCityKTP.add(jar.getJSONObject(i).getString("name"));
                        listCityIDKTP.add(jar.getJSONObject(i).getString("id"));
                    }
                    ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, listCityKTP);
                    auto_ktpCity.setAdapter(adapter);
                    auto_ktpCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                            ktpCity = listCityIDKTP.get(x).toString();
                            Log.e("ktpCity", ktpCity);
                            txtKtpCity.setError(null);
                            viewModel.getDistrict(prefManager.getUid(), prefManager.getToken(), ktpCity);
                            viewModel.getResultDistrict().observe(getActivity(), showDistrictKTP);
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

    private Observer<JSONObject> showDistrictKTP = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            listDistrictKTP.clear();
            listDistrictIDKTP.clear();
            try {
                if(result.getInt("code") == 200){
                    JSONObject jobRes = result.getJSONObject("result");
                    JSONArray jar = jobRes.getJSONArray("district");
                    for(int i = 0; i < jar.length(); i++){
                        listDistrictKTP.add(jar.getJSONObject(i).getString("name"));
                        listDistrictIDKTP.add(jar.getJSONObject(i).getString("id"));
                    }
                    ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, listDistrictKTP);
                    auto_ktpDistrict.setAdapter(adapter);
                    auto_ktpDistrict.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                            ktpDistrict = listDistrictIDKTP.get(x).toString();
                            Log.e("ktpDistrict", ktpDistrict);
                            txtKtpDistrict.setError(null);
                            viewModel.getUrban(prefManager.getUid(), prefManager.getToken(), ktpDistrict);
                            viewModel.getResultUrban().observe(getActivity(), showUrbanKTP);
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

    private Observer<JSONObject> showUrbanKTP = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            listUrbanKTP.clear();
            listUrbanIDKTP.clear();
            try {
                if(result.getInt("code") == 200){
                    JSONObject jobRes = result.getJSONObject("result");
                    JSONArray jar = jobRes.getJSONArray("villages");
                    for(int i = 0; i < jar.length(); i++){
                        listUrbanKTP.add(jar.getJSONObject(i).getString("name"));
                        listUrbanIDKTP.add(jar.getJSONObject(i).getString("id"));
                    }
                    ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, listUrbanKTP);
                    auto_ktpUrban.setAdapter(adapter);
                    auto_ktpUrban.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                            ktpUrban = listUrbanIDKTP.get(x).toString();
                            Log.e("ktpUrban", ktpUrban);
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
