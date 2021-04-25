package byc.avt.avanteelender.view.sheet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.view.features.penarikan.PenarikanDanaActivity;
import byc.avt.avanteelender.view.others.SettingActivity;
import byc.avt.avanteelender.viewmodel.DashboardViewModel;

public class WithdrawalConfirmationSheetFragment extends BottomSheetDialogFragment {

    private static WithdrawalConfirmationSheetFragment instance;
    public static long nominal_penarikan;
    public static int biaya_penarikan;
    public static long total_diterima;
    public static String nama_bank;
    public static String no_rek_bank;
    public static String no_va;
    public static String nama_pemilik_bank;
    public static String va_bank = "va";
    GlobalVariables gv;
    Fungsi f = new Fungsi(getActivity());
    private Dialog dialog;
    private DashboardViewModel viewModel;
    private PrefManager prefManager;

    TextView txt_nominal_penarikan, txt_biaya_penarikan, txt_total_diterima, txt_nama_bank,
            txt_no_rek_bank, txt_nama_pemilik_bank;
    Button btn_tarik;

    public WithdrawalConfirmationSheetFragment() {
    }

    public WithdrawalConfirmationSheetFragment(long nominal_penarikan, int biaya_penarikan, String nama_bank, String no_rek_bank, String nama_pemilik_bank, String no_va){
        this.nominal_penarikan = nominal_penarikan;
        this.biaya_penarikan = biaya_penarikan;
        //this.total_diterima = nominal_penarikan - biaya_penarikan;
        this.nama_bank = nama_bank;
        this.no_rek_bank = no_rek_bank;
        this.nama_pemilik_bank = nama_pemilik_bank;
        this.no_va = no_va;
    }

    public static WithdrawalConfirmationSheetFragment getInstance() {
        instance = null;
        instance = new WithdrawalConfirmationSheetFragment();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_sheet_withdrawal_confirmation, container, false);
        txt_nominal_penarikan = view.findViewById(R.id.txt_nom_penarikan_fr_sheet_withdrawal_confirmation);
        txt_biaya_penarikan = view.findViewById(R.id.lbl_biaya_penarikan_fr_sheet_withdrawal_confirmation);

        txt_nama_bank = view.findViewById(R.id.txt_nama_bank_fr_sheet_withdrawal_confirmation);
        txt_no_rek_bank = view.findViewById(R.id.txt_no_bank_fr_sheet_withdrawal_confirmation);
        txt_nama_pemilik_bank = view.findViewById(R.id.txt_nama_pemilik_bank_fr_sheet_withdrawal_confirmation);
        txt_nominal_penarikan.setText(f.toNumb(""+nominal_penarikan));
        //txt_biaya_penarikan.setText(getString(R.string.biaya_admin_penarikan)+" -"+f.toNumb(""+biaya_penarikan));
        txt_biaya_penarikan.setText(getString(R.string.withdrawal_admin_fee));
        txt_nama_bank.setText(nama_bank);
        txt_no_rek_bank.setText(no_rek_bank);
        txt_nama_pemilik_bank.setText(nama_pemilik_bank);
        dialog = GlobalVariables.loadingDialog(getActivity());
        viewModel = new ViewModelProvider(getActivity()).get(DashboardViewModel.class);
        prefManager = PrefManager.getInstance(getActivity());

        btn_tarik = view.findViewById(R.id.btn_tarik_fr_sheet_withdrawal_confirmation);
        btn_tarik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Konfirmasi")
                        .setIcon(R.drawable.logo)
                        .setMessage("Apakah anda yakin mau menarik dana sebesar "+f.toNumb(""+nominal_penarikan)+" dari Avantee?")
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                submitWithdrawal(dialogInterface);
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
        });

        return view;
    }

    private void submitWithdrawal(DialogInterface dialogs){
        dialogs.cancel();
        dialog.show();
        viewModel.getSubmitWithdrawal(prefManager.getUid(), prefManager.getToken(), no_va, ""+nominal_penarikan, va_bank);
        viewModel.getResultSubmitWithdrawal().observe(getActivity(), showResult);
    }

    private Observer<JSONObject> showResult = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    new ConfirmationSheetFragment(R.raw.withdrawal_processed_loop, getString(R.string.withdrawal_in_process), getString(R.string.withdrawal_in_process_desc));
                    ConfirmationSheetFragment sheetFragment = ConfirmationSheetFragment.getInstance();
                    sheetFragment.show(getActivity().getSupportFragmentManager(), sheetFragment.getTag());
                    //instance.dismiss();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getActivity().finish();
                        }
                    }, 3000);

                }else{
                    JSONObject jobRes = result.getJSONObject("result");
                    String msg_va_no = jobRes.getString("va_no");
                    String msg_amount = jobRes.getString("amount");
                    String msg_va_bank = jobRes.getString("va_bank");
                    f.showMessage(msg_va_no+msg_amount+msg_va_bank);
                }
                dialog.cancel();
            } catch (JSONException e) {
                e.printStackTrace();
                dialog.cancel();
            }
            dialog.cancel();
        }
    };
}