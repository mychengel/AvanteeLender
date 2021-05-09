package byc.avt.avanteelender.view.fragment.tabportofoliofragment;

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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.adapter.PortofolioPendingAdapter;
import byc.avt.avanteelender.helper.EndlessRecyclerOnScrollListener;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.model.HistoryTrx;
import byc.avt.avanteelender.model.PortofolioPending;
import byc.avt.avanteelender.view.features.historitransaksi.HistoriTransaksiListActivity;
import byc.avt.avanteelender.view.fragment.PortofolioFragment;
import byc.avt.avanteelender.viewmodel.tabportofolioviewmodel.PendingPortofolioViewModel;

public class PendingPortofolioFragment extends Fragment {

    private PendingPortofolioViewModel viewModel;
    Fungsi f;
    private PrefManager prefManager;
    private Dialog dialog;
    private TextView txt_tot_pinjaman_pending, txt_est_bunga_diterima, txt_tot_nom_pending;
    long tot_nom_pending = 0, tot_est_bunga_diterima = 0;
    private RecyclerView rv;
    ConstraintLayout cons, cons_lottie;
    ProgressBar prog_bar;

    public static PendingPortofolioFragment newInstance() {
        return new PendingPortofolioFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_portofolio_pending, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        PortofolioFragment.index = 0;
        f = new Fungsi(requireActivity());
        viewModel = new ViewModelProvider(this).get(PendingPortofolioViewModel.class);
        prefManager = PrefManager.getInstance(getActivity());
        dialog = GlobalVariables.loadingDialog(getActivity());
        txt_tot_pinjaman_pending = v.findViewById(R.id.txt_tot_pinjaman_port_pending);
        txt_est_bunga_diterima = v.findViewById(R.id.txt_est_bunga_port_pending);
        txt_tot_nom_pending = v.findViewById(R.id.txt_tot_nom_port_pending);
        cons_lottie = v.findViewById(R.id.cons_lottie_port_pending);
        cons = v.findViewById(R.id.cons_port_pending);
        cons.setVisibility(View.INVISIBLE);
        rv = v.findViewById(R.id.rv_port_pending);
        prog_bar = v.findViewById(R.id.progressBar_port_pending);
        loadData();
    }

    private void loadData() {
        dialog.show();
        viewModel.portofolioPendingHeader(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultHeader().observe(getActivity(), showDataHeader);
        viewModel.portofolioPendingList(prefManager.getUid(), prefManager.getToken(), "1");
        viewModel.getResultList().observe(getActivity(), showDataList);
    }

    private Observer<JSONObject> showDataHeader = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            JSONArray rows;
            tot_est_bunga_diterima = 0;
            tot_nom_pending = 0;
            try {
                txt_tot_pinjaman_pending.setText(result.getLong("total")+" pinjaman");
                txt_est_bunga_diterima.setText(f.toNumb(""+result.getLong("estimasi_total_bunga")));
                txt_tot_nom_pending.setText(f.toNumb(""+result.getLong("total_portofolio_pending")));
                Log.e("pinjaman",result.getLong("total")+"");
//                rows = result.getJSONArray("rows");
//                Log.e("ROWS",rows.toString());
//                if(rows.length()==0){
//                }else{
//                    for(int i = 0; i < rows.length(); i++){
//                        tot_est_bunga_diterima = tot_est_bunga_diterima + rows.getJSONObject(i).getLong("estimasi_bunga_per_loan");
//                        tot_nom_pending = tot_nom_pending + rows.getJSONObject(i).getLong("nominal");
//                    }
//                }

//                txt_est_bunga_diterima.setText(f.toNumb(""+tot_est_bunga_diterima));
//                txt_tot_nom_pending.setText(f.toNumb(""+(tot_nom_pending)));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            dialog.cancel();
        }
    };

    private Observer<ArrayList<PortofolioPending>> showDataList = new Observer<ArrayList<PortofolioPending>>() {
        @Override
        public void onChanged(ArrayList<PortofolioPending> result) {
            if(result.isEmpty()){
                //f.showMessage("Portofolio pending belum ada.");
                cons.setVisibility(View.VISIBLE);
                cons_lottie.setVisibility(View.VISIBLE);
            }else{
                results = result;
                cons.setVisibility(View.VISIBLE);
                cons_lottie.setVisibility(View.GONE);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                rv.setLayoutManager(linearLayoutManager);
                portofolioPendingAdapter = new PortofolioPendingAdapter(getActivity());
                portofolioPendingAdapter.setListPortofolioPending(results);
                rv.setAdapter(portofolioPendingAdapter);
                rv.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
                    @Override
                    public void onLoadMore(int current_page) {
                        loadMorePortPending(""+current_page);
                    }
                });
                //rv.smoothScrollToPosition(results.size());
            }

        }
    };

    ArrayList<PortofolioPending> results = new ArrayList<>();
    PortofolioPendingAdapter portofolioPendingAdapter;
    public void loadMorePortPending(String page) {
        // POST to server through endpoint
        prog_bar.setVisibility(View.VISIBLE);
        viewModel.portofolioPendingList(prefManager.getUid(), prefManager.getToken(), page);
        viewModel.getResultList().observe(getActivity(), showMorePortPending);
    }

    private Observer<ArrayList<PortofolioPending>> showMorePortPending = new Observer<ArrayList<PortofolioPending>>() {
        @Override
        public void onChanged(final ArrayList<PortofolioPending> result) {
            for(int i = 0; i < result.size(); i++){
                results.add(result.get(i));
                portofolioPendingAdapter.notifyDataSetChanged();
            }
            prog_bar.setVisibility(View.GONE);
            //rv.smoothScrollToPosition(results.size());
        }
    };

}
