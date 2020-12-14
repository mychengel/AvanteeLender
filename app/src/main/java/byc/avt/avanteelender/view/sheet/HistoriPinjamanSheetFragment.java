package byc.avt.avanteelender.view.sheet;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import byc.avt.avanteelender.R;

public class HistoriPinjamanSheetFragment extends BottomSheetDialogFragment {

    private static HistoriPinjamanSheetFragment instance;
    public static String frek_byr_tepat;
    public static String frek_byr_telat;
    public static String tot_pin_galang;
    public static String tot_pin_aktif;
    public static String tot_pin_selesai;

    TextView txt_frek_byr_tepat, txt_frek_byr_telat, txt_tot_pin_galang, txt_tot_pin_aktif, txt_tot_pin_selesai;

    public HistoriPinjamanSheetFragment() {
    }

    public HistoriPinjamanSheetFragment(String frek_byr_tepat, String frek_byr_telat, String tot_pin_galang, String tot_pin_aktif, String tot_pin_selesai){
        this.frek_byr_tepat = frek_byr_tepat;
        this.frek_byr_telat = frek_byr_telat;
        this.tot_pin_galang = tot_pin_galang;
        this.tot_pin_aktif = tot_pin_aktif;
        this.tot_pin_selesai = tot_pin_selesai;
    }

    public static HistoriPinjamanSheetFragment getInstance() {
        instance = null;
        instance = new HistoriPinjamanSheetFragment();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_sheet_histori_pinjaman, container, false);
        txt_frek_byr_tepat = view.findViewById(R.id.txt_frek_byr_tepat_fr_sheet_his_pinjaman);
        txt_frek_byr_telat = view.findViewById(R.id.txt_frek_bayar_telat_fr_sheet_his_pinjaman);
        txt_tot_pin_galang = view.findViewById(R.id.txt_tot_pin_galang_fr_sheet_his_pinjaman);
        txt_tot_pin_aktif = view.findViewById(R.id.txt_tot_pin_aktif_fr_sheet_his_pinjaman);
        txt_tot_pin_selesai = view.findViewById(R.id.txt_tot_pin_selesai_fr_sheet_his_pinjaman);
        txt_frek_byr_tepat.setText(frek_byr_tepat);
        txt_frek_byr_telat.setText(frek_byr_telat);
        txt_tot_pin_galang.setText(tot_pin_galang);
        txt_tot_pin_aktif.setText(tot_pin_aktif);
        txt_tot_pin_selesai.setText(tot_pin_selesai);

        return view;
    }
}