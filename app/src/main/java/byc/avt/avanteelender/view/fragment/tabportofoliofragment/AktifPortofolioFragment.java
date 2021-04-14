package byc.avt.avanteelender.view.fragment.tabportofoliofragment;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.adapter.PortofolioAktifAdapter;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.model.PortofolioAktif;
import byc.avt.avanteelender.viewmodel.tabportofolioviewmodel.AktifPortofolioViewModel;
import byc.avt.avanteelender.viewmodel.tabportofolioviewmodel.AktifPortofolioViewModel;
import byc.avt.avanteelender.viewmodel.tabportofolioviewmodel.SelesaiPortofolioViewModel;

public class AktifPortofolioFragment extends Fragment {

    private AktifPortofolioViewModel viewModel;
    Fungsi f;
    private PrefManager prefManager;
    private Dialog dialog;
    private TextView txt_tot_pinjaman_aktif, txt_tot_pinjaman_terlambat, txt_tot_angs_bunga_selanjutnya, txt_tot_angs_bunga_dibayar;
    int tot_angs_bunga_selanjutnya=0, tot_angs_bunga_dibayar=0, tot_pinjaman_aktif=0, tot_pinjaman_terlambat=0;
    private RecyclerView rv;
    private CardView cv_download_surat_kuasa, cv_download_perjanjian_kerja_sama;
    ConstraintLayout cons, cons_lottie;

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
        txt_tot_pinjaman_terlambat = v.findViewById(R.id.txt_late_port_aktif);
        txt_tot_angs_bunga_selanjutnya = v.findViewById(R.id.txt_tot_angs_selanjutnya_port_aktif);
        txt_tot_angs_bunga_dibayar = v.findViewById(R.id.txt_tot_angs_sudah_port_aktif);
        rv = v.findViewById(R.id.rv_port_aktif);
        cons_lottie = v.findViewById(R.id.cons_lottie_port_aktif);
        cons = v.findViewById(R.id.cons_port_aktif);
        cons.setVisibility(View.INVISIBLE);

        cv_download_surat_kuasa = v.findViewById(R.id.cv_download_surat_kuasa_port_aktif);
        cv_download_surat_kuasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                viewModel.downloadSuratKuasa(prefManager.getUid(), prefManager.getToken());
                viewModel.getResultDownloadSuratKuasa().observe(getActivity(), showResultDownloadSuratKuasa);
            }
        });

        cv_download_perjanjian_kerja_sama = v.findViewById(R.id.cv_download_surat_kerja_sama_port_aktif);
        cv_download_perjanjian_kerja_sama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        loadData();
        //f.showMessage("Portofolio AKTIF");
    }

    private void loadData() {
        dialog.show();
        viewModel.portofolioAktifHeader(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultHeader().observe(getActivity(), showDataHeader);
        viewModel.portofolioAktifList(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultList().observe(getActivity(), showDataList);
    }

    private Observer<String> showResultDownloadSuratKuasa = new Observer<String>() {
        @Override
        public void onChanged(String result) {
            dialog.cancel();
        }
    };

    private Observer<JSONObject> showDataHeader = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            JSONArray rows;
            try {
                txt_tot_pinjaman_aktif.setText(result.getInt("total")+" pinjaman");
                Log.e("pinjaman",result.getInt("total")+"");
                rows = result.getJSONArray("rows");
                Log.e("ROWS",rows.toString());
                if(rows.length()==0){
                }else{

                }
                txt_tot_angs_bunga_dibayar.setText(f.toNumb(result.getString("total_angsuran_terbayar")));
                txt_tot_angs_bunga_selanjutnya.setText(f.toNumb(result.getString("total_angsuran_belum_terbayar")));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //dialog.cancel();
        }
    };

    private Observer<ArrayList<PortofolioAktif>> showDataList = new Observer<ArrayList<PortofolioAktif>>() {
        @Override
        public void onChanged(ArrayList<PortofolioAktif> result) {
            tot_pinjaman_terlambat = 0;
            if(result.isEmpty()){
                //f.showMessage("Portofolio Aktif belum ada.");
                cons.setVisibility(View.VISIBLE);
                cons_lottie.setVisibility(View.VISIBLE);
            }else{
                cons.setVisibility(View.VISIBLE);
                cons_lottie.setVisibility(View.GONE);
                for(int i=0; i < result.size(); i++){
                    if(result.get(i).getIs_on_time().equalsIgnoreCase("1")){
                        tot_pinjaman_terlambat = tot_pinjaman_terlambat +1;
                    }else{}
                }
                txt_tot_pinjaman_terlambat.setText(tot_pinjaman_terlambat+" pinjaman");
                rv.setLayoutManager(new LinearLayoutManager(getActivity()));
                PortofolioAktifAdapter portofolioAktifAdapter = new PortofolioAktifAdapter(getActivity());
                portofolioAktifAdapter.setListPortofolioAktif(result);
                rv.setAdapter(portofolioAktifAdapter);
            }
            dialog.cancel();
        }
    };

}
