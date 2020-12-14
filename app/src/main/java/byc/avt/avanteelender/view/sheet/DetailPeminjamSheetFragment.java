package byc.avt.avanteelender.view.sheet;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import byc.avt.avanteelender.R;

public class DetailPeminjamSheetFragment extends BottomSheetDialogFragment {

    private static DetailPeminjamSheetFragment instance;
    public static String tipe_perusahaan;
    public static String tahun_pendirian;
    public static String bidang_usaha;
    public static String des_peminjam;

    TextView txt_tipe_perusahaan, txt_tahun_pendirian, txt_bidang_usaha, txt_des_peminjam;

    public DetailPeminjamSheetFragment() {
    }

    public DetailPeminjamSheetFragment(String tipe_perusahaan, String tahun_pendirian, String bidang_usaha, String des_peminjam){
        this.tipe_perusahaan = tipe_perusahaan;
        this.tahun_pendirian = tahun_pendirian;
        this.bidang_usaha = bidang_usaha;
        this.des_peminjam = des_peminjam;
    }

    public static DetailPeminjamSheetFragment getInstance() {
        instance = null;
        instance = new DetailPeminjamSheetFragment();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_sheet_detail_peminjam, container, false);
        txt_tipe_perusahaan = view.findViewById(R.id.txt_tipe_perusahaan_fr_sheet_det_peminjam);
        txt_tahun_pendirian = view.findViewById(R.id.txt_tahun_pendirian_fr_sheet_det_peminjam);
        txt_bidang_usaha = view.findViewById(R.id.txt_bidang_usaha_fr_sheet_det_peminjam);
        txt_des_peminjam = view.findViewById(R.id.txt_des_peminjam_fr_sheet_det_peminjam);
        txt_tipe_perusahaan.setText(tipe_perusahaan);
        txt_tahun_pendirian.setText(tahun_pendirian);
        txt_bidang_usaha.setText(bidang_usaha);
        txt_des_peminjam.setText(des_peminjam);

        return view;
    }
}