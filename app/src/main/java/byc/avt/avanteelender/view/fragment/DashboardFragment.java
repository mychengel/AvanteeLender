package byc.avt.avanteelender.view.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.adapter.HistoryTrxAdapter;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.model.Header;
import byc.avt.avanteelender.model.HistoryTrx;
import byc.avt.avanteelender.view.features.PendanaanActivity;
import byc.avt.avanteelender.view.others.SettingActivity;
import byc.avt.avanteelender.viewmodel.DashboardViewModel;

public class DashboardFragment extends Fragment {

    private DashboardViewModel viewModel;
    Toolbar toolbar;
    Fungsi f = new Fungsi(getActivity());
    private PrefManager prefManager;
    private RecyclerView rvHistoryTrx;
    private TextView txt_no_trans_history;
    private TextView txt_code, txt_ewallet, txt_nom_active_port, txt_estimate_received_interest, txt_tot_loan, txt_late, txt_nom_pending_port, txt_tot_pending;
    private Dialog dialog;
    private Button btn_pendanaan;
    private boolean headerdone, trxdone, dashboarddone, activedone, pendingdone = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        prefManager = PrefManager.getInstance(getActivity());
        toolbar = view.findViewById(R.id.toolbar_fr_dashboard);
        dialog = GlobalVariables.loadingDialog(requireActivity());
        btn_pendanaan = view.findViewById(R.id.btn_start_invest_fr_dashboard);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        //Recycler View
        rvHistoryTrx = view.findViewById(R.id.rv_histori_trx_fr_dashboard);
        txt_no_trans_history = view.findViewById(R.id.txt_no_transaction_history_fr_dashboar);

        final ImageView pp_icon = view.findViewById(R.id.img_pp_tb_fr_dashboard);
        TextView txt_name = view.findViewById(R.id.txt_name_tb_fr_dashboard);
        txt_name.setText(prefManager.getName());
        txt_code = view.findViewById(R.id.txt_code_tb_fr_dashboard);
        txt_ewallet = view.findViewById(R.id.txt_ewallet_fr_dashboard);
        txt_nom_active_port = view.findViewById(R.id.txt_total_active_portfolio_fr_dashboard);
        txt_estimate_received_interest = view.findViewById(R.id.txt_estimation_received_interest_fr_dashboard);
        txt_tot_loan = view.findViewById(R.id.txt_tot_loan_fr_dashboard);
        txt_late = view.findViewById(R.id.txt_late_fr_dashboard);
        txt_nom_pending_port = view.findViewById(R.id.txt_nom_pending_port_fr_dashboard);
        txt_tot_pending = view.findViewById(R.id.txt_tot_pending_port_fr_dashboard);
        String letter = String.valueOf(prefManager.getName().charAt(0));
        TextView txt_initial = view.findViewById(R.id.txt_initial_fr_dashboard);
        txt_initial.setText(letter);
        Glide.with(this).load(prefManager.getAvatar())
            .into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Drawable> transition) {
                    Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();
                    //Bitmap emptyBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
                    if (resource.getConstantState() == null) {
                        pp_icon.setImageResource(R.drawable.ic_iconuser);
                    }else{
                        Drawable newdrawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(f.getCroppedBitmap(bitmap), 136, 136, true));
                        pp_icon.setImageDrawable(newdrawable);
                    }
                }
            });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                runFirst();
                loadDashboard();
            }
        }, 200);

        btn_pendanaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PendanaanActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
    }

    public void runFirst(){
        rvHistoryTrx.setVisibility(View.INVISIBLE);
        txt_no_trans_history.setVisibility(View.INVISIBLE);
    }

    public void loadDashboard() {
        // POST to server through endpoint
        dialog.show();
        viewModel.getHeader(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultHeader().observe(getActivity(), showHeader);
        viewModel.getHistoryTrx(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultHistoryTrx().observe(getActivity(), showHistoryTrx);
        viewModel.getDashboard(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultDashboard().observe(getActivity(), showDashboard);
        viewModel.getTotActivePort(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultTotActivePort().observe(getActivity(), showTotActivePort);
        viewModel.getTotPendingPort(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultTotPendingPort().observe(getActivity(), showTotPendingPort);
    }

    private Observer<String> showTotActivePort = new Observer<String>() {
        @Override
        public void onChanged(String result) {
            if(result.isEmpty()){
                activedone = true;
            }else{
                txt_tot_loan.setText(result);
                activedone = true;
            }
            cekDone();
        }
    };

    private Observer<String> showTotPendingPort = new Observer<String>() {
        @Override
        public void onChanged(String result) {
            if(result.isEmpty()){
                pendingdone = true;
            }else{
                txt_tot_pending.setText(result);
                pendingdone = true;
            }
            cekDone();
        }
    };

    private Observer<ArrayList<Header>> showHeader = new Observer<ArrayList<Header>>() {
        @Override
        public void onChanged(ArrayList<Header> result) {
            if(result.isEmpty()){
                headerdone = true;
            }else{
                txt_code.setText(result.get(0).getUser_code());
                headerdone = true;
            }
            cekDone();
        }
    };

    private Observer<ArrayList<HistoryTrx>> showHistoryTrx = new Observer<ArrayList<HistoryTrx>>() {
        @Override
        public void onChanged(ArrayList<HistoryTrx> result) {
            if(result.isEmpty()){
                rvHistoryTrx.setVisibility(View.INVISIBLE);
                txt_no_trans_history.setVisibility(View.VISIBLE);
                trxdone = true;
            }else{
                rvHistoryTrx.setVisibility(View.VISIBLE);
                txt_no_trans_history.setVisibility(View.INVISIBLE);
                rvHistoryTrx.setLayoutManager(new LinearLayoutManager(getActivity()));
                HistoryTrxAdapter historyTrxAdapter = new HistoryTrxAdapter(getActivity());
                historyTrxAdapter.setListHistoryTrx(result);
                rvHistoryTrx.setAdapter(historyTrxAdapter);
                trxdone = true;
            }
            cekDone();
        }
    };

    private Observer<JSONObject> showDashboard = new Observer<JSONObject>() {
        @Override
        public void onChanged(JSONObject result) {
            try {
                if(result.getBoolean("status")==false){
                    dashboarddone = true;
                    //new Fungsi(getActivity()).showMessage("Gagal memuat data");
                }else{
                    //new Fungsi(getActivity()).showMessage("Berhasil memuat data");
                    dashboarddone = true;
                    showDashboardData(result);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            cekDone();
        }
    };

    private void cekDone(){
        if(headerdone && trxdone && dashboarddone && activedone && pendingdone){
            dialog.cancel();
        }else{
        }
    }

    private void showDashboardData(JSONObject obj){
        try {
            JSONObject res = obj.getJSONObject("result");
            JSONArray interestArr = res.getJSONArray("interestChart");
            int totInterest = 0;
            for(int i = 0; i < interestArr.length(); i++){
                totInterest = totInterest + interestArr.getJSONObject(i).getInt("value");
            }
            txt_ewallet.setText(f.toNumb(""+res.getInt("totalEwallet")));
            txt_nom_active_port.setText(f.toNumb(""+res.getInt("totalInves")));
            txt_nom_pending_port.setText(f.toNumb(""+res.getInt("totalPending")));
            txt_estimate_received_interest.setText(f.toNumb(""+totInterest));
            //txt_late.setText(); //data diambil dari mana (?)
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.dashboard_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.account_setting){
            Intent intent = new Intent(getActivity(), SettingActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}