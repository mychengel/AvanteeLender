package byc.avt.avanteelender.view.fragment.tabportofoliofragment;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.adapter.PortofolioAktifAdapter;
import byc.avt.avanteelender.adapter.PortofolioPendingAdapter;
import byc.avt.avanteelender.helper.EndlessRecyclerOnScrollListener;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.model.PortofolioAktif;
import byc.avt.avanteelender.model.PortofolioPending;
import byc.avt.avanteelender.view.fragment.PortofolioFragment;
import byc.avt.avanteelender.viewmodel.tabportofolioviewmodel.AktifPortofolioViewModel;
import byc.avt.avanteelender.viewmodel.tabportofolioviewmodel.AktifPortofolioViewModel;
import byc.avt.avanteelender.viewmodel.tabportofolioviewmodel.SelesaiPortofolioViewModel;

public class AktifPortofolioFragment extends Fragment {

    private AktifPortofolioViewModel viewModel;
    Fungsi f;
    private PrefManager prefManager;
    private Dialog dialog;
    private TextView txt_tot_pinjaman_aktif, txt_tot_pinjaman_terlambat, txt_tot_angs_bunga_selanjutnya, txt_tot_angs_bunga_dibayar;
    long tot_angs_bunga_selanjutnya=0, tot_angs_bunga_dibayar=0, tot_pinjaman_aktif=0, tot_pinjaman_terlambat=0;
    private RecyclerView rv;
    private CardView cv_download_surat_kuasa, cv_download_perjanjian_kerja_sama;
    ConstraintLayout cons, cons_lottie;
    ProgressBar prog_bar;

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
        PortofolioFragment.index = 0;
        viewModel = new ViewModelProvider(this).get(AktifPortofolioViewModel.class);
        f = new Fungsi(requireActivity());
        prefManager = PrefManager.getInstance(getActivity());
        dialog = GlobalVariables.loadingDialog(getActivity());
        txt_tot_pinjaman_aktif = v.findViewById(R.id.txt_tot_pinjaman_port_aktif);
        txt_tot_pinjaman_terlambat = v.findViewById(R.id.txt_late_port_aktif);
        txt_tot_angs_bunga_selanjutnya = v.findViewById(R.id.txt_tot_angs_selanjutnya_port_aktif);
        txt_tot_angs_bunga_dibayar = v.findViewById(R.id.txt_tot_angs_sudah_port_aktif);
        prog_bar = v.findViewById(R.id.progressBar_port_aktif);
        rv = v.findViewById(R.id.rv_port_aktif);
        cons_lottie = v.findViewById(R.id.cons_lottie_port_aktif);
        cons = v.findViewById(R.id.cons_port_aktif);
        cons.setVisibility(View.INVISIBLE);

        cv_download_surat_kuasa = v.findViewById(R.id.cv_download_surat_kuasa_port_aktif);
        cv_download_surat_kuasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
                dialog.show();
                viewModel.downloadSuratKuasa(prefManager.getUid(), prefManager.getToken());
                viewModel.getResultDownloadSuratKuasa().observe(getActivity(), showResultDownloadSuratKuasa);


            }
        });

        cv_download_perjanjian_kerja_sama = v.findViewById(R.id.cv_download_surat_kerja_sama_port_aktif);
        cv_download_perjanjian_kerja_sama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    checkPermission();
                    dialog.show();
                    viewModel.downloadSuratPerjanjian(prefManager.getUid(), prefManager.getToken());
                    viewModel.getResultDownloadSuratPerjanjian().observe(getActivity(), showResultDownloadSuratPerjanjian);


            }
        });

        loadData();
    }

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    private void checkPermission(){
        final int permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_STORAGE, 1);
        }else{
        }
    }

    private void loadData() {
        dialog.show();
        viewModel.portofolioAktifHeader(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultHeader().observe(getActivity(), showDataHeader);
        viewModel.portofolioAktifList(prefManager.getUid(), prefManager.getToken(), "1");
        viewModel.getResultList().observe(getActivity(), showDataList);
    }

    private Observer<String> showResultDownloadSuratKuasa = new Observer<String>() {
        @Override
        public void onChanged(String result) {
            dialog.cancel();
            new AlertDialog.Builder(getActivity())
                    .setTitle("Konfirmasi")
                    .setIcon(R.drawable.logo)
                    .setMessage(result)
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
    };

    private Observer<String> showResultDownloadSuratPerjanjian = new Observer<String>() {
        @Override
        public void onChanged(String result) {
            dialog.cancel();
            new AlertDialog.Builder(getActivity())
                    .setTitle("Konfirmasi")
                    .setIcon(R.drawable.logo)
                    .setMessage(result)
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
    };

    private Observer<JSONObject> showDataHeader = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            JSONArray rows;
            try {
                txt_tot_pinjaman_aktif.setText(result.getLong("total")+" pinjaman");
                Log.e("pinjaman",result.getLong("total")+"");
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

    ArrayList<PortofolioAktif> results = new ArrayList<>();
    PortofolioAktifAdapter portofolioAktifAdapter;
    LinearLayoutManager linearLayoutManager;
    private Observer<ArrayList<PortofolioAktif>> showDataList = new Observer<ArrayList<PortofolioAktif>>() {
        @Override
        public void onChanged(ArrayList<PortofolioAktif> result) {
            results = result;
            tot_pinjaman_terlambat = 0;
            if(result.isEmpty()){
                cons.setVisibility(View.VISIBLE);
                cons_lottie.setVisibility(View.VISIBLE);
            }else{
                cons.setVisibility(View.VISIBLE);
                cons_lottie.setVisibility(View.GONE);
                linearLayoutManager = new LinearLayoutManager(getActivity());
                rv.setLayoutManager(linearLayoutManager);
                portofolioAktifAdapter = new PortofolioAktifAdapter(getActivity());
                portofolioAktifAdapter.setListPortofolioAktif(results);
                rv.setAdapter(portofolioAktifAdapter);
                rv.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
                    @Override
                    public void onLoadMore(int current_page) {
                        loadMorePortAktif(""+current_page);
                    }
                });
                //rv.smoothScrollToPosition(results.size());
            }
            dialog.cancel();
        }
    };

    public void loadMorePortAktif(String page) {
        // POST to server through endpoint
        prog_bar.setVisibility(View.VISIBLE);
        viewModel.portofolioAktifList(prefManager.getUid(), prefManager.getToken(), page);
        viewModel.getResultList().observe(getActivity(), showMorePortAktif);
    }

    private Observer<ArrayList<PortofolioAktif>> showMorePortAktif = new Observer<ArrayList<PortofolioAktif>>() {
        @Override
        public void onChanged(final ArrayList<PortofolioAktif> result) {
            for(int i = 0; i < result.size(); i++){
                results.add(result.get(i));
                portofolioAktifAdapter.notifyDataSetChanged();
            }
            prog_bar.setVisibility(View.GONE);
            //rv.smoothScrollToPosition(results.size());
        }
    };

}
