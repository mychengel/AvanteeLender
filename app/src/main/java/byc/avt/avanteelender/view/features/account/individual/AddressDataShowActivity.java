package byc.avt.avanteelender.view.features.account.individual;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.helper.Routes;
import byc.avt.avanteelender.viewmodel.AuthenticationViewModel;
import byc.avt.avanteelender.viewmodel.MasterDataViewModel;

public class AddressDataShowActivity extends AppCompatActivity {

    Fungsi f = new Fungsi(AddressDataShowActivity.this);
    private Toolbar toolbar;
    JSONObject job1, job2;

    private MasterDataViewModel viewModel;
    private AuthenticationViewModel viewModel2;
    Button btn_save, btn_edit;
    boolean editIsOn = false;
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

    LinearLayout lin_domicile_area;
    CheckBox cb_domicile_same_as_ktp;
    String same_as_ktp = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_data_show);
        gv.perEditData.clear();
        gv.perEditDataFile.clear();
        viewModel = new ViewModelProvider(this).get(MasterDataViewModel.class);
        viewModel2 = new ViewModelProvider(this).get(AuthenticationViewModel.class);
        prefManager = PrefManager.getInstance(AddressDataShowActivity.this);
        dialog = GlobalVariables.loadingDialog(AddressDataShowActivity.this);
        toolbar = findViewById(R.id.toolbar_address_data_show);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_back_24px);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auto_ktpCountry = findViewById(R.id.auto_ktp_country_address_data_show);
        auto_ktpProvince = findViewById(R.id.auto_ktp_province_address_data_show);
        auto_ktpCity = findViewById(R.id.auto_ktp_city_address_data_show);
        auto_ktpDistrict = findViewById(R.id.auto_ktp_kecamatan_address_data_show);
        auto_ktpUrban = findViewById(R.id.auto_ktp_kelurahan_address_data_show);
        auto_domicileCountry = findViewById(R.id.auto_domicile_country_address_data_show);
        auto_domicileProvince = findViewById(R.id.auto_domicile_province_address_data_show);
        auto_domicileCity = findViewById(R.id.auto_domicile_city_address_data_show);
        auto_domicileDistrict = findViewById(R.id.auto_domicile_kecamatan_address_data_show);
        auto_domicileUrban = findViewById(R.id.auto_domicile_kelurahan_address_data_show);
        txtKtpAddress = findViewById(R.id.edit_ktp_address_address_data_show);
        txtKtpCountry = findViewById(R.id.edit_ktp_country_address_data_show);
        txtKtpProvince = findViewById(R.id.edit_ktp_province_address_data_show);
        txtKtpCity = findViewById(R.id.edit_ktp_city_address_data_show);
        txtKtpDistrict = findViewById(R.id.edit_ktp_kecamatan_address_data_show);
        txtKtpUrban = findViewById(R.id.edit_ktp_kelurahan_address_data_show);
        txtKtpRT = findViewById(R.id.edit_ktp_rt_address_data_show);
        txtKtpRW = findViewById(R.id.edit_ktp_rw_address_data_show);
        txtKtpPostalCode = findViewById(R.id.edit_ktp_kodepos_address_data_show);
        txtDomicileAddress = findViewById(R.id.edit_domicile_address_address_data_show);
        txtDomicileCountry = findViewById(R.id.edit_domicile_country_address_data_show);
        txtDomicileProvince = findViewById(R.id.edit_domicile_province_address_data_show);
        txtDomicileCity = findViewById(R.id.edit_domicile_city_address_data_show);
        txtDomicileDistrict = findViewById(R.id.edit_domicile_kecamatan_address_data_show);
        txtDomicileUrban = findViewById(R.id.edit_domicile_kelurahan_address_data_show);
        txtDomicileRT = findViewById(R.id.edit_domicile_rt_address_data_show);
        txtDomicileRW = findViewById(R.id.edit_domicile_rw_address_data_show);
        txtDomicilePostalCode = findViewById(R.id.edit_domicile_kodepos_address_data_show);
        lin_domicile_area = findViewById(R.id.lin_domicile_address_data_show);

        Intent i = getIntent();
        try {
            job1 = new JSONObject(i.getStringExtra("jobAddressDataKTP"));
            txtKtpAddress.getEditText().setText(job1.getString("identity_address"));
            txtKtpCountry.getEditText().setText(job1.getString("identity_country"));
            txtKtpProvince.getEditText().setText(job1.getString("identity_province"));
            txtKtpCity.getEditText().setText(job1.getString("identity_city"));
            txtKtpDistrict.getEditText().setText(job1.getString("identity_district"));
            txtKtpUrban.getEditText().setText(job1.getString("identity_village"));
            txtKtpRT.getEditText().setText(job1.getString("identity_rt"));
            txtKtpRW.getEditText().setText(job1.getString("identity_rw"));
            txtKtpPostalCode.getEditText().setText(job1.getString("identity_postal_code"));

            job2 = new JSONObject(i.getStringExtra("jobAddressDataDomicile"));
            txtDomicileAddress.getEditText().setText(job2.getString("clients_address"));
            txtDomicileCountry.getEditText().setText(job2.getString("clients_country"));
            txtDomicileProvince.getEditText().setText(job2.getString("clients_province"));
            txtDomicileCity.getEditText().setText(job2.getString("clients_city"));
            txtDomicileDistrict.getEditText().setText(job2.getString("clients_district"));
            txtDomicileUrban.getEditText().setText(job2.getString("clients_village"));
            txtDomicileRT.getEditText().setText(job2.getString("rt"));
            txtDomicileRW.getEditText().setText(job2.getString("rw"));
            txtDomicilePostalCode.getEditText().setText(job2.getString("clients_postal_code"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb_domicile_same_as_ktp = findViewById(R.id.cb_domicile_same_as_ktp_address_data_show);
        cb_domicile_same_as_ktp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    is_domicile_same_as_ktp = true;
                    lin_domicile_area.setVisibility(View.GONE);
                    same_as_ktp = "1";
                }else{
                    is_domicile_same_as_ktp = false;
                    lin_domicile_area.setVisibility(View.VISIBLE);
                    same_as_ktp = "0";
                }
            }
        });

        txtKtpPostalCode.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ktpPostalCode = charSequence.toString();
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

        btn_save = findViewById(R.id.btn_save_address_data_show);
        btn_save.setEnabled(true);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmNext(v);
            }
        });

        btn_edit = findViewById(R.id.btn_ubah_address_data_show);
        btn_edit.setEnabled(true);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editIsOn = !editIsOn;
                editIsOn(editIsOn);
            }
        });

        editIsOn(false);
        loadData();

    }

    public void cekPostal(){
        if(ktpPostalCode.length() > 5){txtKtpPostalCode.setError(getString(R.string.postal_code_max_char));}else{txtKtpPostalCode.setError(null);}
        if(domicilePostalCode.length() > 5){txtDomicilePostalCode.setError(getString(R.string.postal_code_max_char));}else{txtDomicilePostalCode.setError(null);}
    }

    public void editIsOn(boolean s){
        txtKtpAddress.setEnabled(s);
        txtKtpCountry.setEnabled(s);
        txtKtpProvince.setEnabled(s);
        txtKtpCity.setEnabled(s);
        txtKtpDistrict.setEnabled(s);
        txtKtpUrban.setEnabled(s);
        txtKtpRT.setEnabled(s);
        txtKtpRW.setEnabled(s);
        txtKtpPostalCode.setEnabled(s);
        txtDomicileAddress.setEnabled(s);
        txtDomicileCountry.setEnabled(s);
        txtDomicileProvince.setEnabled(s);
        txtDomicileCity.setEnabled(s);
        txtDomicileDistrict.setEnabled(s);
        txtDomicileUrban.setEnabled(s);
        txtDomicileRT.setEnabled(s);
        txtDomicileRW.setEnabled(s);
        txtDomicilePostalCode.setEnabled(s);
        cb_domicile_same_as_ktp.setEnabled(s);
        btn_save.setEnabled(s);
    }

    private void confirmNext(View v){
        ktpAddress = Objects.requireNonNull(txtKtpAddress.getEditText().getText().toString().trim());
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
            domicileRT = Objects.requireNonNull(txtDomicileRT.getEditText().getText().toString().trim());
            domicileRW = Objects.requireNonNull(txtDomicileRW.getEditText().getText().toString().trim());
            domicilePostalCode = Objects.requireNonNull(txtDomicilePostalCode.getEditText().getText().toString().trim());
        }

        if(!ktpAddress.isEmpty() && !ktpCountry.isEmpty() && !ktpProvince.isEmpty() && !ktpCity.isEmpty()
                && !ktpDistrict.isEmpty() && !ktpUrban.isEmpty() && !ktpRT.isEmpty()
                && !ktpRW.isEmpty() && !ktpPostalCode.isEmpty() && ktpPostalCode.length() <= 5 && !domicileAddress.isEmpty()
                && !domicileCountry.isEmpty() && !domicileProvince.isEmpty() && !domicileCity.isEmpty()
                && !domicileDistrict.isEmpty() && !domicileUrban.isEmpty() && !domicileRT.isEmpty()
                && !domicileRW.isEmpty() && !domicilePostalCode.isEmpty() && domicilePostalCode.length() <= 5){
            gv.perEditData.put("event_name","data_alamat");
            gv.perEditData.put("tipe_investor",prefManager.getClientType());
            gv.perEditData.put("alamat_ktp",ktpAddress);
            gv.perEditData.put("negara_ktp",ktpCountry);
            gv.perEditData.put("provinsi_ktp",ktpProvince);
            gv.perEditData.put("kota_ktp",ktpCity);
            gv.perEditData.put("kecamatan_ktp",ktpDistrict);
            gv.perEditData.put("kelurahan_ktp",ktpUrban);
            gv.perEditData.put("rt_ktp",ktpRT);
            gv.perEditData.put("rw_ktp",ktpRW);
            gv.perEditData.put("kode_pos_ktp",ktpPostalCode);
            gv.perEditData.put("alamat_sesuai_ktp",same_as_ktp);
            gv.perEditData.put("alamat_tempat_tinggal",domicileAddress);
            gv.perEditData.put("negara_tempat_tinggal",domicileCountry);
            gv.perEditData.put("provinsi_tempat_tinggal",domicileProvince);
            gv.perEditData.put("kota_tempat_tinggal",domicileCity);
            gv.perEditData.put("kecamatan_tempat_tinggal",domicileDistrict);
            gv.perEditData.put("kelurahan_tempat_tinggal",domicileUrban);
            gv.perEditData.put("rt_tempat_tinggal",domicileRT);
            gv.perEditData.put("rw_tempat_tinggal",domicileRW);
            gv.perEditData.put("kode_pos_tempat_tinggal",domicilePostalCode);
            setNoError();
            updateDocument();
        }else{
            cekError();
        }
    }

    private void updateDocument(){
        new AlertDialog.Builder(AddressDataShowActivity.this)
                .setTitle("Konfirmasi")
                .setIcon(R.drawable.logo)
                .setMessage(getString(R.string.update_doc_confirmation))
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        dialog.show();
                        viewModel2.updatePersonalDoc(prefManager.getUid(), prefManager.getToken());
                        viewModel2.getResultUpdatePersonalDoc().observe(AddressDataShowActivity.this, showResult);
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
                    new Fungsi(AddressDataShowActivity.this).showMessage(getString(R.string.success_update_data));
                    dialog.cancel();
                    new Routes(AddressDataShowActivity.this).moveOut();
                }else{
                    //new Fungsi(AddressDataShowActivity.this).showMessage(getString(R.string.failed_update_data));
                    dialog.cancel();
                    JSONObject jobRes = result.getJSONObject("result");
                    String msg = f.docErr400(jobRes.toString());
                    new AlertDialog.Builder(AddressDataShowActivity.this)
                            .setTitle("Peringatan")
                            .setIcon(R.drawable.logo)
                            .setMessage("• " + msg)
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            }).create().show();
                    cekError();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                String msg = getString(R.string.system_in_trouble);
                Log.e("Respon per cr doc", msg);
                new Fungsi(AddressDataShowActivity.this).showMessage(msg);
                dialog.cancel();
                cekError();
            }
        }
    };

    public void loadData(){
        clearMasterList();
        dialog.show();
        viewModel.getCountry(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultCountry().observe(AddressDataShowActivity.this, showCountryKTP);
        viewModel.getResultCountry().observe(AddressDataShowActivity.this, showCountryDom);
        viewModel.getProvince(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultProvince().observe(AddressDataShowActivity.this, showProvinceKTP);
        viewModel.getResultProvince().observe(AddressDataShowActivity.this, showProvinceDom);
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
                        if(jar.getJSONObject(i).getString("name").equalsIgnoreCase(job1.getString("identity_country"))){
                            ktpCountry = jar.getJSONObject(i).getString("id");
                        }
                        Log.e("Data ktpnegara", ktpCountry);
                    }
                    ArrayAdapter adapter = new ArrayAdapter(AddressDataShowActivity.this, R.layout.support_simple_spinner_dropdown_item, listCountryKTP);
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
                        if(jar.getJSONObject(i).getString("name").equalsIgnoreCase(job1.getString("identity_province"))){
                            ktpProvince = jar.getJSONObject(i).getString("id");
                            viewModel.getCity(prefManager.getUid(), prefManager.getToken(), ktpProvince);
                            viewModel.getResultCity().observe(AddressDataShowActivity.this, showCityKTP);
                        }
                        Log.e("Data ktpprov", ktpProvince);

                    }
                    ArrayAdapter adapter = new ArrayAdapter(AddressDataShowActivity.this, R.layout.support_simple_spinner_dropdown_item, listProvinceKTP);
                    auto_ktpProvince.setAdapter(adapter);
                    auto_ktpProvince.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                            ktpProvince = listProvinceIDKTP.get(x).toString();
                            Log.e("ktpProvince", ktpProvince);
                            txtKtpProvince.setError(null);
                            viewModel.getCity(prefManager.getUid(), prefManager.getToken(), ktpProvince);
                            viewModel.getResultCity().observe(AddressDataShowActivity.this, showCityKTP);
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
                        if(jar.getJSONObject(i).getString("name").equalsIgnoreCase(job1.getString("identity_city"))){
                            ktpCity = jar.getJSONObject(i).getString("id");
                            viewModel.getDistrict(prefManager.getUid(), prefManager.getToken(), ktpCity);
                            viewModel.getResultDistrict().observe(AddressDataShowActivity.this, showDistrictKTP);
                        }
                        Log.e("Data ktpcity", ktpCity);

                    }
                    ArrayAdapter adapter = new ArrayAdapter(AddressDataShowActivity.this, R.layout.support_simple_spinner_dropdown_item, listCityKTP);
                    auto_ktpCity.setAdapter(adapter);
                    auto_ktpCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                            ktpCity = listCityIDKTP.get(x).toString();
                            Log.e("ktpCity", ktpCity);
                            txtKtpCity.setError(null);
                            viewModel.getDistrict(prefManager.getUid(), prefManager.getToken(), ktpCity);
                            viewModel.getResultDistrict().observe(AddressDataShowActivity.this, showDistrictKTP);
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
                        if(jar.getJSONObject(i).getString("name").equalsIgnoreCase(job1.getString("identity_district"))){
                            ktpDistrict = jar.getJSONObject(i).getString("id");
                            viewModel.getUrban(prefManager.getUid(), prefManager.getToken(), ktpDistrict);
                            viewModel.getResultUrban().observe(AddressDataShowActivity.this, showUrbanKTP);
                        }
                        Log.e("Data ktpdistrict", ktpDistrict);

                    }
                    ArrayAdapter adapter = new ArrayAdapter(AddressDataShowActivity.this, R.layout.support_simple_spinner_dropdown_item, listDistrictKTP);
                    auto_ktpDistrict.setAdapter(adapter);
                    auto_ktpDistrict.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                            ktpDistrict = listDistrictIDKTP.get(x).toString();
                            Log.e("ktpDistrict", ktpDistrict);
                            txtKtpDistrict.setError(null);
                            viewModel.getUrban(prefManager.getUid(), prefManager.getToken(), ktpDistrict);
                            viewModel.getResultUrban().observe(AddressDataShowActivity.this, showUrbanKTP);
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
                        if(jar.getJSONObject(i).getString("name").equalsIgnoreCase(job1.getString("identity_village"))){
                            ktpUrban = jar.getJSONObject(i).getString("id");
                        }
                        Log.e("Data ktpurban", ktpUrban);
                    }
                    ArrayAdapter adapter = new ArrayAdapter(AddressDataShowActivity.this, R.layout.support_simple_spinner_dropdown_item, listUrbanKTP);
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
                        if(jar.getJSONObject(i).getString("name").equalsIgnoreCase(job2.getString("clients_country"))){
                            domicileCountry = jar.getJSONObject(i).getString("id");
                        }
                        Log.e("Data domnegara", domicileCountry);
                    }
                    ArrayAdapter adapter = new ArrayAdapter(AddressDataShowActivity.this, R.layout.support_simple_spinner_dropdown_item, listCountryDomicile);
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
                        if(jar.getJSONObject(i).getString("name").equalsIgnoreCase(job2.getString("clients_province"))){
                            domicileProvince = jar.getJSONObject(i).getString("id");
                            viewModel.getCity(prefManager.getUid(), prefManager.getToken(), domicileProvince);
                            viewModel.getResultCity().observe(AddressDataShowActivity.this, showCityDom);
                        }
                        Log.e("Data domprov", domicileProvince);

                    }
                    ArrayAdapter adapter = new ArrayAdapter(AddressDataShowActivity.this, R.layout.support_simple_spinner_dropdown_item, listProvinceDomicile);
                    auto_domicileProvince.setAdapter(adapter);
                    auto_domicileProvince.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                            domicileProvince = listProvinceIDDomicile.get(x).toString();
                            Log.e("domProvince", domicileProvince);
                            txtDomicileProvince.setError(null);
                            viewModel.getCity(prefManager.getUid(), prefManager.getToken(), domicileProvince);
                            viewModel.getResultCity().observe(AddressDataShowActivity.this, showCityDom);
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
                        if(jar.getJSONObject(i).getString("name").equalsIgnoreCase(job2.getString("clients_city"))){
                            domicileCity = jar.getJSONObject(i).getString("id");
                            viewModel.getDistrict(prefManager.getUid(), prefManager.getToken(), domicileCity);
                            viewModel.getResultDistrict().observe(AddressDataShowActivity.this, showDistrictDom);
                        }
                        Log.e("Data domcity", domicileCity);

                    }
                    ArrayAdapter adapter = new ArrayAdapter(AddressDataShowActivity.this, R.layout.support_simple_spinner_dropdown_item, listCityDomicile);
                    auto_domicileCity.setAdapter(adapter);
                    auto_domicileCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                            domicileCity = listCityIDDomicile.get(x).toString();
                            Log.e("domCity", domicileCity);
                            txtDomicileCity.setError(null);
                            viewModel.getDistrict(prefManager.getUid(), prefManager.getToken(), domicileCity);
                            viewModel.getResultDistrict().observe(AddressDataShowActivity.this, showDistrictDom);
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
                        if(jar.getJSONObject(i).getString("name").equalsIgnoreCase(job2.getString("clients_district"))){
                            domicileDistrict = jar.getJSONObject(i).getString("id");
                            viewModel.getUrban(prefManager.getUid(), prefManager.getToken(), domicileDistrict);
                            viewModel.getResultUrban().observe(AddressDataShowActivity.this, showUrbanDom);
                        }
                        Log.e("Data domdistrict", domicileDistrict);

                    }
                    ArrayAdapter adapter = new ArrayAdapter(AddressDataShowActivity.this, R.layout.support_simple_spinner_dropdown_item, listDistrictDomicile);
                    auto_domicileDistrict.setAdapter(adapter);
                    auto_domicileDistrict.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                            domicileDistrict = listDistrictIDDomicile.get(x).toString();
                            Log.e("domDistrict", domicileDistrict);
                            txtDomicileDistrict.setError(null);
                            viewModel.getUrban(prefManager.getUid(), prefManager.getToken(), domicileDistrict);
                            viewModel.getResultUrban().observe(AddressDataShowActivity.this, showUrbanDom);
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
                        if(jar.getJSONObject(i).getString("name").equalsIgnoreCase(job2.getString("clients_village"))){
                            domicileUrban = jar.getJSONObject(i).getString("id");
                        }
                        Log.e("Data domurban", domicileUrban);
                    }
                    ArrayAdapter adapter = new ArrayAdapter(AddressDataShowActivity.this, R.layout.support_simple_spinner_dropdown_item, listUrbanDomicile);
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

    public void clearMasterList(){
        listCountryKTP.clear();listCountryIDKTP.clear();
        listProvinceKTP.clear();listProvinceIDKTP.clear();
        listCityKTP.clear();listCityIDKTP.clear();
        listCountryDomicile.clear();listCountryIDDomicile.clear();
        listProvinceDomicile.clear();listProvinceIDDomicile.clear();
        listCityDomicile.clear();listCityIDDomicile.clear();
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
        if(ktpPostalCode.isEmpty()){txtKtpPostalCode.setError(getString(R.string.cannotnull));}if(ktpPostalCode.length() > 5){txtKtpPostalCode.setError(getString(R.string.postal_code_max_char));}
        else{txtKtpPostalCode.setError(null);}
        if(domicileAddress.isEmpty()){txtDomicileAddress.setError(getString(R.string.cannotnull));}else{txtDomicileAddress.setError(null);}
        if(domicileCountry.isEmpty()){txtDomicileCountry.setError(getString(R.string.cannotnull));}else{txtDomicileCountry.setError(null);}
        if(domicileProvince.isEmpty()){txtDomicileProvince.setError(getString(R.string.cannotnull));}else{txtDomicileProvince.setError(null);}
        if(domicileCity.isEmpty()){txtDomicileCity.setError(getString(R.string.cannotnull));}else{txtDomicileCity.setError(null);}
        if(domicileDistrict.isEmpty()){txtDomicileDistrict.setError(getString(R.string.cannotnull));}else{txtDomicileDistrict.setError(null);}
        if(domicileUrban.isEmpty()){txtDomicileUrban.setError(getString(R.string.cannotnull));}else{txtDomicileUrban.setError(null);}
        if(domicileRT.isEmpty()){txtDomicileRT.setError(getString(R.string.cannotnull));}else{txtDomicileRT.setError(null);}
        if(domicileRW.isEmpty()){txtDomicileRW.setError(getString(R.string.cannotnull));}else{txtDomicileRW.setError(null);}
        if(domicilePostalCode.isEmpty()){txtDomicilePostalCode.setError(getString(R.string.cannotnull));}if(domicilePostalCode.length() > 5){txtDomicilePostalCode.setError(getString(R.string.postal_code_max_char));}
        else{txtDomicilePostalCode.setError(null);}
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            new Routes(AddressDataShowActivity.this).moveOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new Routes(AddressDataShowActivity.this).moveOut();
    }
}