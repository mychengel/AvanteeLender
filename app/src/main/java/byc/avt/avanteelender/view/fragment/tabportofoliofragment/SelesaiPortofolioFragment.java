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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.adapter.HistoryTrxAdapter;
import byc.avt.avanteelender.adapter.PortofolioSelesaiAdapter;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.model.PortofolioSelesai;
import byc.avt.avanteelender.viewmodel.tabportofolioviewmodel.SelesaiPortofolioViewModel;

public class SelesaiPortofolioFragment extends Fragment {

    private SelesaiPortofolioViewModel viewModel;
    Fungsi f;
    private PrefManager prefManager;
    private Dialog dialog;
    private TextView txt_tot_pinjaman_selesai, txt_tot_pb_selesai, txt_tot_nom_selesai;
    int tot_pb_selesai, tot_nom_selesai;
    private RecyclerView rv;
    ConstraintLayout cons, cons_lottie;

    public static SelesaiPortofolioFragment newInstance() {
        return new SelesaiPortofolioFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_portofolio_selesai, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        f = new Fungsi(requireActivity());
        viewModel = new ViewModelProvider(this).get(SelesaiPortofolioViewModel.class);
        prefManager = PrefManager.getInstance(getActivity());
        dialog = GlobalVariables.loadingDialog(getActivity());
        txt_tot_pinjaman_selesai = v.findViewById(R.id.txt_tot_pinjaman_port_selesai);
        txt_tot_pb_selesai = v.findViewById(R.id.txt_pb_received_port_selesai);
        txt_tot_nom_selesai = v.findViewById(R.id.txt_nom_received_port_selesai);
        rv = v.findViewById(R.id.rv_port_selesai);
        cons_lottie = v.findViewById(R.id.cons_lottie_port_selesai);
        cons = v.findViewById(R.id.cons_port_selesai);
        cons.setVisibility(View.INVISIBLE);
        //f.showMessage("Portofolio SELESAI");
        loadData();
    }

    private void loadData() {
        dialog.show();
        viewModel.portofolioCloseHeader(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultHeader().observe(getActivity(), showDataHeader);
        viewModel.portofolioCloseList(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultList().observe(getActivity(), showDataList);
    }

    private Observer<JSONObject> showDataHeader = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            Log.e("PortSelesai",result.toString());
            JSONArray rows;
            tot_pb_selesai = 0;
            tot_nom_selesai = 0;
            try {
                txt_tot_pinjaman_selesai.setText(result.getInt("total")+" pinjaman");
                Log.e("pinjaman",result.getInt("total")+"");
                rows = result.getJSONArray("rows");
                Log.e("ROWS",rows.toString());
                if(rows.length()==0){
                }else{
                    for(int i = 0; i < rows.length(); i++){
                        tot_pb_selesai = tot_pb_selesai + (int) Double.parseDouble(rows.getJSONObject(i).getString("payment_amount"));
                        tot_nom_selesai = tot_nom_selesai + (int) Double.parseDouble(rows.getJSONObject(i).getString("nominal"));
                    }
                }
                txt_tot_pb_selesai.setText(f.toNumb(""+tot_pb_selesai));
                txt_tot_nom_selesai.setText(f.toNumb(""+tot_nom_selesai));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            dialog.cancel();
        }
    };

    private Observer<ArrayList<PortofolioSelesai>> showDataList = new Observer<ArrayList<PortofolioSelesai>>() {
        @Override
        public void onChanged(ArrayList<PortofolioSelesai> result) {
            if(result.isEmpty()){
                //f.showMessage("Portofolio selesai belum ada.");
                cons.setVisibility(View.VISIBLE);
                cons_lottie.setVisibility(View.VISIBLE);
            }else{
                cons.setVisibility(View.VISIBLE);
                cons_lottie.setVisibility(View.GONE);
                rv.setLayoutManager(new LinearLayoutManager(getActivity()));
                PortofolioSelesaiAdapter portofolioSelesaiAdapter = new PortofolioSelesaiAdapter(getActivity());
                portofolioSelesaiAdapter.setListPortofolioSelesai(result);
                rv.setAdapter(portofolioSelesaiAdapter);
            }

        }
    };

}