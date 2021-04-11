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
    AutoCompleteTextView auto_ktpCountry, auto_ktpProvince, auto_ktpCity, auto_domicileCountry,
            auto_domicileProvince, auto_domicileCity;
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
    List<Object> listCountryDomicile = new ArrayList<>(); List<Object> listCountryIDDomicile = new ArrayList<>();
    List<Object> listProvinceDomicile = new ArrayList<>(); List<Object> listProvinceIDDomicile = new ArrayList<>();
    List<Object> listCityDomicile = new ArrayList<>(); List<Object> listCityIDDomicile = new ArrayList<>();

    Button btn_next;
    LinearLayout lin_domicile_area;
    CheckBox cb_domicile_same_as_ktp;

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MasterDataViewModel.class);
        prefManager = PrefManager.getInstance(getActivity());
        dialog = GlobalVariables.loadingDialog(requireActivity());
        auto_ktpCountry = view.findViewById(R.id.auto_ktp_country_fr_address_data);
        auto_ktpProvince = view.findViewById(R.id.auto_ktp_province_fr_address_data);
        auto_ktpCity = view.findViewById(R.id.auto_ktp_city_fr_address_data);
        auto_domicileCountry = view.findViewById(R.id.auto_domicile_country_fr_address_data);
        auto_domicileProvince = view.findViewById(R.id.auto_domicile_province_fr_address_data);
        auto_domicileCity = view.findViewById(R.id.auto_domicile_city_fr_address_data);
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
                    lin_domicile_area.setVisibility(View.GONE);
                }else{
                    lin_domicile_area.setVisibility(View.VISIBLE);
                }
            }
        });

        btn_next = view.findViewById(R.id.btn_next_fr_address_data);
        btn_next.setEnabled(true);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_addressDataFragment_to_bankInfoFragment);
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
        viewModel.getCountry(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultCountry().observe(getActivity(), showCountryKTP);
        viewModel.getResultCountry().observe(getActivity(), showCountryDom);
        viewModel.getProvince(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultProvince().observe(getActivity(), showProvinceKTP);
        viewModel.getResultProvince().observe(getActivity(), showProvinceDom);
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
                    }
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
                    }
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
                        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, listCityKTP);
                        auto_ktpCity.setAdapter(adapter);
                        auto_ktpCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                                ktpCity = listCityIDKTP.get(x).toString();
                                Log.e("ktpCity", ktpCity);
                                txtKtpCity.setError(null);
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
                    }
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
                    }
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
                        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, listCityDomicile);
                        auto_domicileCity.setAdapter(adapter);
                        auto_domicileCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                                domicileCity = listCityIDDomicile.get(x).toString();
                                Log.e("domCity", domicileCity);
                                txtDomicileCity.setError(null);
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
