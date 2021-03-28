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
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.adapter.PortofolioPendingAdapter;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.model.PortofolioPending;
import byc.avt.avanteelender.viewmodel.tabportofolioviewmodel.PendingPortofolioViewModel;

public class PendingPortofolioFragment extends Fragment {

    private PendingPortofolioViewModel viewModel;
    Fungsi f;
    private PrefManager prefManager;
    private Dialog dialog;
    private TextView txt_tot_pinjaman_pending, txt_est_bunga_diterima, txt_tot_nom_pending;
    int tot_nom_pending = 0, tot_est_bunga_diterima = 0;
    private RecyclerView rv;
    ConstraintLayout cons, cons_lottie;

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
        //f.showMessage("Portofolio PENDING");
        loadData();
    }

    private void loadData() {
        dialog.show();
        viewModel.portofolioPendingHeader(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultHeader().observe(getActivity(), showDataHeader);
        viewModel.portofolioPendingList(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultList().observe(getActivity(), showDataList);
    }

    private Observer<JSONObject> showDataHeader = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            JSONArray rows;
            tot_est_bunga_diterima = 0;
            tot_nom_pending = 0;
            try {
                txt_tot_pinjaman_pending.setText(result.getInt("total")+" pinjaman");
                Log.e("pinjaman",result.getInt("total")+"");
                rows = result.getJSONArray("rows");
                Log.e("ROWS",rows.toString());
                if(rows.length()==0){
                }else{
                    for(int i = 0; i < rows.length(); i++){
                        double bunga;
                        tot_est_bunga_diterima = tot_est_bunga_diterima + rows.getJSONObject(i).getInt("estimasi_bunga_per_loan");
                        tot_nom_pending = tot_nom_pending + rows.getJSONObject(i).getInt("nominal");
                    }
                }

                txt_est_bunga_diterima.setText(f.toNumb(""+tot_est_bunga_diterima));
                txt_tot_nom_pending.setText(f.toNumb(""+(tot_nom_pending)));

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
                cons.setVisibility(View.VISIBLE);
                cons_lottie.setVisibility(View.GONE);
                rv.setLayoutManager(new LinearLayoutManager(getActivity()));
                PortofolioPendingAdapter portofolioPendingAdapter = new PortofolioPendingAdapter(getActivity());
                portofolioPendingAdapter.setListPortofolioPending(result);
                rv.setAdapter(portofolioPendingAdapter);
            }

        }
    };

}
