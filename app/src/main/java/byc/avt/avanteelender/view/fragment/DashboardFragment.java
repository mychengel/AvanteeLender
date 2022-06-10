package byc.avt.avanteelender.view.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.adapter.HistoryTrxAdapter;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.helper.Routes;
import byc.avt.avanteelender.model.Header;
import byc.avt.avanteelender.model.HistoryTrx;
import byc.avt.avanteelender.view.MainActivity;
import byc.avt.avanteelender.view.features.account.individual.UpdateAvaActivity;
import byc.avt.avanteelender.view.features.account.institution.SettingInsAccountActivity;
import byc.avt.avanteelender.view.features.historitransaksi.HistoriTransaksiListActivity;
import byc.avt.avanteelender.view.features.penarikan.PenarikanDanaActivity;
import byc.avt.avanteelender.view.features.pendanaan.PendanaanActivity;
import byc.avt.avanteelender.view.features.topup.TopupInstructionActivity;
import byc.avt.avanteelender.view.others.SettingActivity;
import byc.avt.avanteelender.viewmodel.DashboardViewModel;

public class DashboardFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private DashboardViewModel viewModel;
    Toolbar toolbar;
    Fungsi f = new Fungsi(getActivity());
    private PrefManager prefManager;
    private RecyclerView rvHistoryTrx;
    private TextView txt_no_trans_history, lbl_rek_va, lbl_saldo_va, lbl_dana_pending, txtCopy;
    private TextView txt_code, txt_ewallet, txt_nom_active_port, txt_estimate_received_interest, txt_tot_loan, txt_late, txt_nom_pending_port, txt_tot_pending;
    private Dialog dialog;
    private Button btn_pendanaan, btn_histori_trx, btn_iap, btn_topup, btn_withdraw;
    private boolean headerdone, trxdone, dashboarddone, activedone, pendingdone = false;
    private ConstraintLayout cons_det_wallet;
    private ImageView img_expand_wallet;
    private SwipeRefreshLayout refreshLayout;
    boolean is_expand = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        prefManager = PrefManager.getInstance(getActivity());
        toolbar = view.findViewById(R.id.toolbar_fr_dashboard);
        refreshLayout = view.findViewById(R.id.swipeRefreshDashboard);
        dialog = GlobalVariables.loadingDialog(requireActivity());
        btn_pendanaan = view.findViewById(R.id.btn_start_invest_fr_dashboard);
        btn_histori_trx = view.findViewById(R.id.btn_histori_trx_fr_dashboard);
        btn_topup = view.findViewById(R.id.btn_topup_fr_dashboard);
        btn_withdraw = view.findViewById(R.id.btn_withdraw_fr_dashboard);
        cons_det_wallet = view.findViewById(R.id.cons_det_wallet_fr_dashboard);
        cons_det_wallet.setVisibility(View.GONE);
        img_expand_wallet = view.findViewById(R.id.img_expand_wallet_fr_dashboard);
        lbl_rek_va = view.findViewById(R.id.lbl_no_rek_va_fr_dashboard);
        lbl_saldo_va = view.findViewById(R.id.lbl_saldo_va_fr_dashboard);
        lbl_dana_pending = view.findViewById(R.id.lbl_dana_status_pending_fr_dashboard);
        txtCopy = view.findViewById(R.id.txt_copy);
        txtCopy.setVisibility(View.GONE);
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
        final TextView txt_initial = view.findViewById(R.id.txt_initial_fr_dashboard);
        txt_initial.setText(letter);
        Glide.with(this).load(prefManager.getAvatar())
            .into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Drawable> transition) {
                    Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();
                    if (resource.getConstantState() == null) {
                        pp_icon.setImageResource(R.drawable.ic_iconuser);
                        txt_initial.setVisibility(View.VISIBLE);
                    }else{
                        Drawable newdrawable = new BitmapDrawable(getResources(), bitmap);
                        pp_icon.setImageDrawable(newdrawable);
                        txt_initial.setVisibility(View.GONE);
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

        btn_withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PenarikanDanaActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        btn_topup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TopupInstructionActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        img_expand_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!is_expand){
                    img_expand_wallet.setRotation(180);
                    cons_det_wallet.setVisibility(View.VISIBLE);
                    is_expand = !is_expand;
                }else{
                    img_expand_wallet.setRotation(0);
                    cons_det_wallet.setVisibility(View.GONE);
                    is_expand = !is_expand;
                }
            }
        });

        btn_pendanaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PendanaanActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        btn_histori_trx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HistoriTransaksiListActivity.filter_text = "Semua transaksi";
                Intent intent = new Intent(getActivity(), HistoriTransaksiListActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        btn_iap = view.findViewById(R.id.btn_iap_fr_dashboard);
        btn_iap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity ma = new MainActivity();
                ma.navView.setSelectedItemId(R.id.navigation_notifikasi);
                //NotificationsFragment.index = 1;
            }
        });

        pp_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UpdateAvaActivity.class);
                new Routes(getActivity()).moveIn(intent);
            }
        });

        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(R.color.colorPrimary, R.color.colorAccent);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onResume() {
        super.onResume();
        runFirst();
        loadDashboard();
    }

    public void runFirst(){
        rvHistoryTrx.setVisibility(View.INVISIBLE);
        txt_no_trans_history.setVisibility(View.INVISIBLE);
    }

    public void loadDashboard() {
        // POST to server through endpoint
        dialog.show();
        viewModel.getWallet(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultWallet().observe(getActivity(), showWallet);
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

        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }

    private Observer<JSONObject> showWallet = new Observer<JSONObject>() {
        @Override
        public void onChanged(final JSONObject result) {
            try {
                if(result.getInt("code") == 200){
                    long mywallet = result.getLong("total_dana");
                    long saldova = result.getLong("total_dana_va");
                    //long saldordl = result.getLong("total_dana_rdl");
                    if(mywallet <= 0){
                        btn_withdraw.setEnabled(false);
                    }else{
                        btn_withdraw.setEnabled(true);
                    }
                    txt_ewallet.setText(f.toNumb(""+mywallet));
                    lbl_rek_va.setText("No rek. "+result.getString("no_rekening_va"));
                    lbl_saldo_va.setText(f.toNumb(""+saldova));
                    enableCopyButton(result.getString("no_rekening_va"));
//                    lbl_rek_rdl.setText("No rek. "+result.getString("no_rekening_rdl"));
//                    lbl_saldo_rdl.setText(f.toNumb(""+result.getString("total_dana_rdl")));
                    lbl_dana_pending.setText(f.toNumb(""+result.getLong("dana_pending")));
                }else{
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            cekDone();
        }
    };

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
                GlobalVariables.LENDER_CODE = result.get(0).getUser_code();
                GlobalVariables.NO_HP = result.get(0).getNo_handphone();
                headerdone = true;
            }
            cekDone();
        }
    };

    private Observer<ArrayList<HistoryTrx>> showHistoryTrx = new Observer<ArrayList<HistoryTrx>>() {
        @Override
        public void onChanged(ArrayList<HistoryTrx> result) {
            if(result.size()==0){
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
                }else{
                    dashboarddone = true;
                    showDashboardData(result);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            cekDone();
        }
    };

    private  void enableCopyButton(final String va) {
        txtCopy.setVisibility(View.VISIBLE);
        txtCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager manager = (ClipboardManager) requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData data = ClipData.newPlainText("VA", va);
                manager.setPrimaryClip(data);

                Toast.makeText(requireActivity(),R.string.copied_to_clipboard,Toast.LENGTH_SHORT).show();
            }
        });
    }


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
            long totInterest = 0;
            for(int i = 0; i < interestArr.length(); i++){
                totInterest = totInterest + interestArr.getJSONObject(i).getLong("value");
            }
            //txt_ewallet.setText(f.toNumb(""+res.getInt("totalEwallet")));
            txt_nom_active_port.setText(f.toNumb(""+res.getLong("totalInves")));
            txt_nom_pending_port.setText(f.toNumb(""+res.getLong("totalPending")));
            txt_estimate_received_interest.setText(f.toNumb(""+totInterest));
            txt_late.setText(""+res.getLong("pinjamanTerlambat"));
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

    @Override
    public void onRefresh() {
        this.loadDashboard();
    }
}