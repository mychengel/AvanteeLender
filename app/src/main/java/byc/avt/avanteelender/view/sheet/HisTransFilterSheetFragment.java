package byc.avt.avanteelender.view.sheet;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.view.features.historitransaksi.HistoriTransaksiListActivity;

public class HisTransFilterSheetFragment extends BottomSheetDialogFragment {

    private static HisTransFilterSheetFragment instance;
    RadioGroup rg_periode, rg_status, rg_type;
    RadioButton rb_periode, rb_status, rb_type;
    RadioButton rb_90s, rb_all, rb_pof, rb_berhasil, rb_pending, rb_pembayaran, rb_penarikan;
    Button btn_terapkan, btn_reset;
    boolean m90s = true, mAll = true, mPof = false, mBerhasil = true, mPending = false, mPembayaran = true, mPenarikan = false;
    AutoCompleteTextView date_start, date_end;
    LinearLayout lin_pof;

    public HisTransFilterSheetFragment() {
    }

    public static HisTransFilterSheetFragment getInstance() {
        instance = null;
        instance = new HisTransFilterSheetFragment();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_sheet_filter_his_trans, container, false);
        lin_pof = view.findViewById(R.id.lin_range_pof_fr_sheet_filter_his_trans);
        rg_periode = view.findViewById(R.id.rad_group_pof_fr_sheet_filter_his_trans);
        rg_status = view.findViewById(R.id.rad_group_status_fr_sheet_filter_his_trans);
        rg_type = view.findViewById(R.id.rad_group_type_fr_sheet_filter_his_trans);
        rb_all = view.findViewById(R.id.rad_all_pof_fr_sheet_filter_his_trans);
        rb_pof = view.findViewById(R.id.rad_range_pof_fr_sheet_filter_his_trans);
        rb_berhasil = view.findViewById(R.id.rad_berhasil_fr_sheet_filter_his_trans);
        rb_pending = view.findViewById(R.id.rad_pending_fr_sheet_filter_his_trans);
        rb_pembayaran = view.findViewById(R.id.rad_pembayaran_fr_sheet_filter_his_trans);
        rb_penarikan = view.findViewById(R.id.rad_penarikan_fr_sheet_filter_his_trans);
        date_start = view.findViewById(R.id.date_start_pof_fr_sheet_filter_his_trans);
        date_end = view.findViewById(R.id.date_end_pof_fr_sheet_filter_his_trans);
        btn_reset = view.findViewById(R.id.btn_reset_fr_sheet_filter_his_trans);
        btn_terapkan = view.findViewById(R.id.btn_terapkan_fr_sheet_filter_his_trans);

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rb_all.setChecked(true);
                rb_berhasil.setChecked(false);
                rb_pending.setChecked(false);
            }
        });

        rg_periode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                rb_periode = view.findViewById(selectedId);
                if(rb_periode.getText().equals("Semuanya")){
                    mAll = true; mPof = false;
                }else{
                    mAll = false; mPof = true;
                }

                if(mPof){
                    lin_pof.setVisibility(View.VISIBLE);
                }else{
                    lin_pof.setVisibility(View.GONE);
                }
            }
        });

        rg_status.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                rb_status = view.findViewById(selectedId);
                if(rb_status.getText().equals("Berhasil")){

                }else{

                }

            }
        });

        rg_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                rb_type = view.findViewById(selectedId);
                if(rb_type.getText().equals("Pembayaran")){

                }else{

                }

            }
        });


        btn_terapkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                HistoriTransaksiListActivity.start = 0;
//                HistoriTransaksiListActivity.end = 0;
//                HistoriTransaksiListActivity.status = "1";
//                new HistoriTransaksiListActivity().filterRun();
            }
        });
        return view;
    }
}