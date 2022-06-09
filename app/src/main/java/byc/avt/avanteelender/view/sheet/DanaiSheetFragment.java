package byc.avt.avanteelender.view.sheet;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RawRes;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.helper.Routes;
import byc.avt.avanteelender.intro.WalkthroughActivity;
import byc.avt.avanteelender.view.auth.SetNewPasswordActivity;
import byc.avt.avanteelender.view.features.penarikan.PenarikanDanaActivity;
import byc.avt.avanteelender.view.features.pendanaan.PendanaanDetailActivity;
import byc.avt.avanteelender.view.features.pendanaan.SignerPendanaanActivity;
import byc.avt.avanteelender.view.features.pendanaan.ViewKontrakPendanaanActivity;
import byc.avt.avanteelender.view.others.SettingActivity;
import byc.avt.avanteelender.viewmodel.DashboardViewModel;
import byc.avt.avanteelender.viewmodel.PendanaanViewModel;

public class DanaiSheetFragment extends BottomSheetDialogFragment {

    public static String loan_no;
    private Dialog dialog;
    private PendanaanViewModel viewModel;
    private PrefManager prefManager;
    private static DanaiSheetFragment instance;
    RadioGroup rg_metode;
    RadioButton radSaldo, radTfBank;
    Button btn_next;
    boolean mSaldo = true, mTfBank = false;
    ImageView img_plus, img_minus;
    TextView txt_saldo_va, txt_total_saldo, txt_min_invest, txt_max_invest, txt_kelipatan;
    EditText edit_nominal;
    public static JSONObject job;
    CheckBox cb_agree;
    boolean isAgree = false, isNomValid = false;
    long nominal = 0;
    String nominal_show = "", current = "";
    long saldoVa=0, saldoRdl=0, totalSaldo=0, pending=0, multiplesFunding=0, minInvest=0, maxInvest=0;
    String investType="";
    Context ctx;
    Fungsi f;
    int lastCursorPosition;

    public DanaiSheetFragment() {
    }

    public DanaiSheetFragment(JSONObject job){
        this.job = job;
    }

    public static DanaiSheetFragment getInstance() {
        instance = null;
        instance = new DanaiSheetFragment();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_sheet_danai, container, false);
        f = new Fungsi(ctx);
        Log.e("JOBDanai", job.toString());
        try {
            saldoVa = job.getLong("saldo_va");
            //saldoRdl = job.getLong("saldo_rdl");
            totalSaldo = job.getLong("total_saldo");
            pending = job.getLong("pending_balance");
            multiplesFunding = job.getLong("multiples_funding_nominal");
            minInvest = job.getLong("min_invest");
            nominal = job.getLong("min_invest");
            maxInvest = job.getLong("max_invest");
            investType = job.getString("invest_type");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        prefManager = PrefManager.getInstance(getActivity());
        dialog = GlobalVariables.loadingDialog(getActivity());
        viewModel = new ViewModelProvider(getActivity()).get(PendanaanViewModel.class);
        txt_kelipatan = view.findViewById(R.id.lbl_kelipatan_satu_juta_fr_sheet_danai);
        txt_kelipatan.setText("Kelipatan "+f.toNumb(""+multiplesFunding));
        txt_total_saldo = view.findViewById(R.id.txt_saldo_fr_sheet_danai);
        txt_total_saldo.setText(f.toNumb(""+totalSaldo));
        txt_saldo_va = view.findViewById(R.id.txt_saldo_va_fr_sheet_danai);
        txt_saldo_va.setText(f.toNumb(""+saldoVa));
        txt_min_invest = view.findViewById(R.id.txt_min_invest_fr_sheet_danai);
        txt_min_invest.setText(getString(R.string.min_invest)+" "+f.toNumb(""+minInvest));
        txt_max_invest = view.findViewById(R.id.txt_max_invest_fr_sheet_danai);
        txt_max_invest.setText(getString(R.string.max_invest)+" "+f.toNumb(""+maxInvest));
        edit_nominal = view.findViewById(R.id.edit_nom_pendanaan_fr_sheet_danai);
        nominal_show = f.toNumb(""+nominal);
        edit_nominal.setText(nominal_show.substring(2, nominal_show.length()));
        cb_agree = view.findViewById(R.id.cb_setuju_mendanai_fr_sheet_danai);
        cb_agree.setText(getString(R.string.saya_setuju_mendanai)+" "+f.toNumb(""+nominal));
        rg_metode = view.findViewById(R.id.rg_metode_fr_sheet_danai);
        radSaldo = view.findViewById(R.id.rad_metode_saldo_fr_sheet_danai);
        radTfBank = view.findViewById(R.id.rad_metode_tf_bank_fr_sheet_danai);
        btn_next = view.findViewById(R.id.btn_selanjutnya_fr_sheet_danai);
        img_plus = view.findViewById(R.id.img_plus_fr_sheet_danai);
        img_minus = view.findViewById(R.id.img_minus_fr_sheet_danai);
        radSaldo.setChecked(mSaldo);
        radTfBank.setChecked(mTfBank);
        Log.e("LOAN_NO", loan_no);

        if(totalSaldo < minInvest){
            new Fungsi(getActivity()).showMessage(getString(R.string.saldo_not_enough));
            instance.dismiss();
        }else{
            confirmNominal("va");
        }

        cb_agree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    isAgree = true;
                }else{
                    isAgree = false;
                }
                cekDone();
            }
        });

        edit_nominal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                current = nominal_show;
                lastCursorPosition = edit_nominal.getSelectionStart();
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(!s.toString().equals(current)){
                    edit_nominal.removeTextChangedListener(this);
                    String cleanString = s.toString().replaceAll("[$,.]", "");
                    if(cleanString.isEmpty() || cleanString.equalsIgnoreCase("")){
                        nominal = 0;
                        nominal_show = f.toNumb(""+nominal);
                        nominal_show = nominal_show.substring(2, nominal_show.length());
                    }else{
                        try {
                            nominal = Long.parseLong(cleanString);
                            if(totalSaldo >= maxInvest){
                                if(nominal >= maxInvest){
                                    nominal = maxInvest;
                                }else{
                                }
                            }
                            nominal_show = f.toNumb(""+nominal);
                            nominal_show = nominal_show.substring(2, nominal_show.length());
                        }catch (Exception e){
                            if(totalSaldo >= maxInvest){
                                nominal = maxInvest;
                            }else{
                                nominal = totalSaldo - (totalSaldo % multiplesFunding);
                            }

                            nominal_show = f.toNumb(""+nominal);
                            nominal_show = nominal_show.substring(2, nominal_show.length());
                        }
                    }

                    edit_nominal.setText(nominal_show);
                    edit_nominal.setSelection(nominal_show.length());
                    edit_nominal.addTextChangedListener(this);

                    if (lastCursorPosition != current.length() && lastCursorPosition != -1) {
                        int lengthDelta = nominal_show.length() - current.length();
                        int newCursorOffset = Math.max(0, Math.min(nominal_show.length(), lastCursorPosition + lengthDelta));
                        edit_nominal.setSelection(newCursorOffset);
                    }

                    cekDone();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        img_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mSaldo){
                    if(totalSaldo >= maxInvest){
                        if(nominal > maxInvest){
                            nominal = maxInvest;
                            nominal_show = f.toNumb(""+nominal);
                            nominal_show = nominal_show.substring(2, nominal_show.length());
                            edit_nominal.setText(nominal_show);
                        }else{
                            if((nominal + multiplesFunding) <= maxInvest){
                                nominal = nominal - (nominal % multiplesFunding) + multiplesFunding;
                                nominal_show = f.toNumb(""+nominal);
                                nominal_show = nominal_show.substring(2, nominal_show.length());
                                edit_nominal.setText(nominal_show);
                                edit_nominal.setSelection(nominal_show.length());
                            }else{
                                nominal = maxInvest;
                                nominal_show = f.toNumb(""+nominal);
                                nominal_show = nominal_show.substring(2, nominal_show.length());
                                edit_nominal.setText(nominal_show);
                            }
                        }
                    }else{
                        if((nominal + multiplesFunding) <= totalSaldo){
                            if((nominal + multiplesFunding) <= maxInvest){
                                nominal = nominal - (nominal % multiplesFunding) + multiplesFunding;
                                nominal_show = f.toNumb(""+nominal);
                                nominal_show = nominal_show.substring(2, nominal_show.length());
                                edit_nominal.setText(nominal_show);
                                edit_nominal.setSelection(nominal_show.length());
                            }else{
                                nominal = maxInvest;
                                nominal_show = f.toNumb(""+nominal);
                                nominal_show = nominal_show.substring(2, nominal_show.length());
                                edit_nominal.setText(nominal_show);
                            }
                        }else{
                        }
                    }
                }else{

                }

                cekDone();
            }
        });

        img_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mSaldo){
                    if(totalSaldo >= maxInvest){
                        if(nominal > maxInvest){
                            nominal = maxInvest;
                            nominal_show = f.toNumb(""+nominal);
                            nominal_show = nominal_show.substring(2, nominal_show.length());
                            edit_nominal.setText(nominal_show);
                        }else{
                            if((nominal - multiplesFunding) >= minInvest){
                                nominal = nominal - (nominal % multiplesFunding) - multiplesFunding;
                                nominal_show = f.toNumb(""+nominal);
                                nominal_show = nominal_show.substring(2, nominal_show.length());
                                edit_nominal.setText(nominal_show);
                                edit_nominal.setSelection(nominal_show.length());
                            }else{
                                nominal = nominal - (nominal % multiplesFunding);
                                nominal_show = f.toNumb(""+nominal);
                                nominal_show = nominal_show.substring(2, nominal_show.length());
                                edit_nominal.setText(nominal_show);
                                edit_nominal.setSelection(nominal_show.length());
                            }
                        }
                    }else{
                        if((nominal - multiplesFunding) >= minInvest){
                            nominal = nominal - (nominal % multiplesFunding) - multiplesFunding;
                            nominal_show = f.toNumb(""+nominal);
                            nominal_show = nominal_show.substring(2, nominal_show.length());
                            edit_nominal.setText(nominal_show);
                            edit_nominal.setSelection(nominal_show.length());
                        }else{
                            nominal = nominal - (nominal % multiplesFunding);
                            nominal_show = f.toNumb(""+nominal);
                            nominal_show = nominal_show.substring(2, nominal_show.length());
                            edit_nominal.setText(nominal_show);
                            edit_nominal.setSelection(nominal_show.length());
                        }
                    }
                }else{
                }
                cekDone();
            }
        });

        radSaldo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSaldo = true; mTfBank = false;
                radSaldo.setChecked(mSaldo);
                radTfBank.setChecked(mTfBank);
                confirmNominal("va");
            }
        });

        radTfBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSaldo = false; mTfBank = true;
                radSaldo.setChecked(mSaldo);
                radTfBank.setChecked(mTfBank);
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(maxInvest == 0){
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Peringatan")
                            .setIcon(R.drawable.logo)
                            .setMessage(getString(R.string.failed_on_maxinvest) + " " + f.toNumb(maxInvest+""))
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            })
                            .create()
                            .show();
                }else{
                    if(nominal % multiplesFunding == 0){
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Konfirmasi")
                                .setIcon(R.drawable.logo)
                                .setMessage(getString(R.string.pendanaan_confirmation))
                                .setCancelable(false)
                                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                        confirmDanai();
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
                    }else{
                        if(maxInvest < multiplesFunding){
                            if(minInvest == nominal || maxInvest == nominal){
                                new AlertDialog.Builder(getActivity())
                                        .setTitle("Konfirmasi")
                                        .setIcon(R.drawable.logo)
                                        .setMessage(getString(R.string.pendanaan_confirmation))
                                        .setCancelable(false)
                                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                                confirmDanai();
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
                            }else{
                                new AlertDialog.Builder(getActivity())
                                        .setTitle("Peringatan")
                                        .setIcon(R.drawable.logo)
                                        .setMessage(getString(R.string.failed_on_maxinvest) + " " + f.toNumb(maxInvest+""))
                                        .setCancelable(false)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                            }
                                        })
                                        .create()
                                        .show();
                            }
                        }else{
                            if(minInvest == nominal){
                                new AlertDialog.Builder(getActivity())
                                        .setTitle("Konfirmasi")
                                        .setIcon(R.drawable.logo)
                                        .setMessage(getString(R.string.pendanaan_confirmation))
                                        .setCancelable(false)
                                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                                confirmDanai();
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
                            }else{
                                new AlertDialog.Builder(getActivity())
                                        .setTitle("Peringatan")
                                        .setIcon(R.drawable.logo)
                                        .setMessage(getString(R.string.failed_on_multiples) + " " + f.toNumb(multiplesFunding+""))
                                        .setCancelable(false)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                            }
                                        })
                                        .create()
                                        .show();
                            }

                        }
                    }
                }


            }
        });

        return view;
    }

    public void confirmDanai(){
        dialog.show();
        viewModel.getAgreementFunding(prefManager.getUid(), prefManager.getToken(), loan_no, nominal+"");
        viewModel.getAgreementFundingResult().observe(getActivity(), showAgreementFunding);
    }

    private Observer<JSONObject> showAgreementFunding = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    dialog.cancel();
                    Log.e("resultAgreement", result.toString());
                    JSONObject res = result.getJSONObject("result");
                    Intent intent = new Intent(getActivity(), ViewKontrakPendanaanActivity.class);
                    intent.putExtra("agreement_code", res.getString("agreement_code"));
                    intent.putExtra("loan_no", res.getString("loan_no"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    new Routes(getActivity()).moveInFinish(intent);
               }else{
                    f.showMessage(getString(R.string.failed_load_data));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dialog.cancel();
        }
    };

    public void confirmNominal(String type){
        dialog.show();
        viewModel.getNominalFunding(prefManager.getUid(), prefManager.getToken(), loan_no, type);
        viewModel.getNominalFundingResult().observe(getActivity(), showNominalFunding);
    }

    private Observer<JSONObject> showNominalFunding = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    Log.e("resultNominal", result.toString());
                }else{
                    f.showMessage(getString(R.string.failed_load_data));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dialog.cancel();
        }
    };

    public void cekDone(){
        cb_agree.setText(getString(R.string.saya_setuju_mendanai)+" "+f.toNumb(""+nominal));
        if((nominal <= totalSaldo && nominal <= maxInvest) && (totalSaldo >= minInvest) && (nominal >= minInvest)){
            isNomValid = true;
        }else{
            isNomValid = false;
        }
        btn_next.setEnabled(isAgree && isNomValid);
    }
}