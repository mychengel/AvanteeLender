package byc.avt.avanteelender.view.fragment.tabportofoliofragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.viewmodel.tabportofolioviewmodel.AktifPortofolioViewModel;
import byc.avt.avanteelender.viewmodel.tabportofolioviewmodel.PendingPortofolioViewModel;
import byc.avt.avanteelender.viewmodel.tabportofolioviewmodel.SelesaiPortofolioViewModel;

public class AktifPortofolioFragment extends Fragment {

    private AktifPortofolioViewModel viewModel;
    Fungsi f;
    private PrefManager prefManager;
    private Dialog dialog;
    private TextView txt_tot_pinjaman_aktif, txt_tot_pinjaman_terlambat_aktif, txt_tot_angs_bunga_selanjutnya, txt_tot_angs_bunga_dibayar;
    double tot_pb_selesai = 0, tot_nom_selesai = 0;
    private RecyclerView rv;

    public static AktifPortofolioFragment newInstance() {
        return new AktifPortofolioFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_portofolio_aktif, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(AktifPortofolioViewModel.class);
        f = new Fungsi(requireActivity());
        prefManager = PrefManager.getInstance(getActivity());
        dialog = GlobalVariables.loadingDialog(getActivity());
        txt_tot_pinjaman_aktif = v.findViewById(R.id.txt_tot_pinjaman_port_aktif);
        txt_tot_pinjaman_terlambat_aktif = v.findViewById(R.id.txt_late_port_aktif);
        txt_tot_angs_bunga_selanjutnya = v.findViewById(R.id.txt_tot_angs_selanjutnya_port_aktif);
        txt_tot_angs_bunga_dibayar = v.findViewById(R.id.txt_tot_angs_sudah_port_aktif);
        rv = v.findViewById(R.id.rv_port_aktif);

        //f.showMessage("Portofolio AKTIF");
    }

}
