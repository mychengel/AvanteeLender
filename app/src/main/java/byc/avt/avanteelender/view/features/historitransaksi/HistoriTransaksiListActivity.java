package byc.avt.avanteelender.view.features.historitransaksi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.adapter.HistoryTrxAdapter;
import byc.avt.avanteelender.helper.EndlessRecyclerOnScrollListener;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.model.HistoryTrx;
import byc.avt.avanteelender.view.auth.LoginActivity;
import byc.avt.avanteelender.view.sheet.DanaiSheetFragment;
import byc.avt.avanteelender.view.sheet.HisTransFilterSheetFragment;
import byc.avt.avanteelender.viewmodel.DashboardViewModel;

public class HistoriTransaksiListActivity extends AppCompatActivity {

    static Fungsi f;
    Toolbar bar;
    static PrefManager prefManager;
    static Dialog dialog;
    static ArrayList<HistoryTrx> results = new ArrayList<>();
    static ArrayList<HistoryTrx> results_filter = new ArrayList<>();
    static ArrayList<HistoryTrx> results_tmp = new ArrayList<>();
    static HistoryTrxAdapter historyTrxAdapter, historyTrxAdapterFilter;
    public static LinearLayoutManager linearLayoutManager, linearLayoutManagerFilter;
    public static RecyclerView rv_his_trx_list, rv_his_trx_filter;
    static DashboardViewModel viewModel;
    public static TextView lbl_no_trx, txt_filter_text;
    private Button btn_filter;
    public static ProgressBar prog_bar;
    public static boolean filter_is_active = false;
    public static String filter_text = "Semua transaksi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histori_transaksi_list);
        f = new Fungsi(HistoriTransaksiListActivity.this);
        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        bar = findViewById(R.id.toolbar_his_trx_list);
        setSupportActionBar(bar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_back_24px);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        prefManager = PrefManager.getInstance(HistoriTransaksiListActivity.this);
        dialog = GlobalVariables.loadingDialog(HistoriTransaksiListActivity.this);
        txt_filter_text = findViewById(R.id.lbl_filter_text_his_trx_list);
        rv_his_trx_list = findViewById(R.id.rv_his_trx_list);
        rv_his_trx_filter = findViewById(R.id.rv_his_trx_filter);
        lbl_no_trx = findViewById(R.id.lbl_no_trx_his_trx_list);
        prog_bar = findViewById(R.id.prog_bar_his_trx_list);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                runFirst();
                loadHistoryTrxList();
            }
        }, 200);

        btn_filter = findViewById(R.id.btn_filter_his_trx_list);
        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new HisTransFilterSheetFragment();
                HisTransFilterSheetFragment sheetFragment = HisTransFilterSheetFragment.getInstance();
                sheetFragment.show(getSupportFragmentManager(), sheetFragment.getTag());
            }
        });

        linearLayoutManagerFilter = new LinearLayoutManager(HistoriTransaksiListActivity.this);
        historyTrxAdapterFilter = new HistoryTrxAdapter(HistoriTransaksiListActivity.this);
    }

    public void runFirst(){
        results.clear();
        rv_his_trx_list.setAdapter(null);
        rv_his_trx_list.setVisibility(View.INVISIBLE);
        lbl_no_trx.setVisibility(View.INVISIBLE);
    }

    public void loadHistoryTrxList() {
        // POST to server through endpoint
        dialog.show();
        viewModel.getHistoryTrxList(prefManager.getUid(), prefManager.getToken(), "1");
        viewModel.getResultHistoryTrxList().observe(HistoriTransaksiListActivity.this, showHistoryTrxList);
    }

    private Observer<ArrayList<HistoryTrx>> showHistoryTrxList = new Observer<ArrayList<HistoryTrx>>() {
        @Override
        public void onChanged(final ArrayList<HistoryTrx> result) {
            results = result;
            results_tmp = result;
            if(results.size()==0){
                rv_his_trx_list.setVisibility(View.INVISIBLE);
                lbl_no_trx.setVisibility(View.VISIBLE);
            }else{
                rv_his_trx_list.setVisibility(View.VISIBLE);
                lbl_no_trx.setVisibility(View.INVISIBLE);
                linearLayoutManager = new LinearLayoutManager(HistoriTransaksiListActivity.this);
                rv_his_trx_list.setLayoutManager(linearLayoutManager);
                historyTrxAdapter = new HistoryTrxAdapter(HistoriTransaksiListActivity.this);
                historyTrxAdapter.setListHistoryTrx(results);
                rv_his_trx_list.setAdapter(historyTrxAdapter);
                rv_his_trx_list.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
                    @Override
                    public void onLoadMore(int current_page) {
                        loadMoreHistory(""+current_page);
                    }
                });
                rv_his_trx_list.smoothScrollToPosition(results.size());
            }
            dialog.cancel();
        }
    };

    public void loadMoreHistory(String page) {
        // POST to server through endpoint
        prog_bar.setVisibility(View.VISIBLE);
        viewModel.getHistoryTrxList(prefManager.getUid(), prefManager.getToken(), page);
        viewModel.getResultHistoryTrxList().observe(HistoriTransaksiListActivity.this, showMoreHistory);
    }

    private Observer<ArrayList<HistoryTrx>> showMoreHistory = new Observer<ArrayList<HistoryTrx>>() {
        @Override
        public void onChanged(final ArrayList<HistoryTrx> result) {
            for(int i = 0; i < result.size(); i++){
                results.add(result.get(i));
                historyTrxAdapter.notifyDataSetChanged();
            }
            results_tmp = results;
            prog_bar.setVisibility(View.GONE);
            rv_his_trx_list.smoothScrollToPosition(results.size());
            txt_filter_text.setText(filter_text+ " ("+results.size()+")");
        }
    };


    public static void filterRun(String start, String end, String status){
        results_filter.clear();
        rv_his_trx_filter.setAdapter(null);

        if(status.isEmpty()){
            if(start.isEmpty() && end.isEmpty()){
                rv_his_trx_list.setVisibility(View.VISIBLE);
                rv_his_trx_filter.setVisibility(View.GONE);
                filter_text = "Semua transaksi (" + results.size() + ")";
                txt_filter_text.setText(filter_text);
            }else{
                rv_his_trx_list.setVisibility(View.INVISIBLE);
                rv_his_trx_filter.setVisibility(View.VISIBLE);
                filter_text = "Transaksi dari " + start + " s/d " + end;
                for(int a = 0; a < results_tmp.size(); a++){
                    if((f.millisFromDate(start) <= f.millisFromDate(results_tmp.get(a).getTrx_date()))
                            && (f.millisFromDate(end) >= f.millisFromDate(results_tmp.get(a).getTrx_date()))){
                        results_filter.add(results_tmp.get(a));
                    }else{}
                }
                txt_filter_text.setText(filter_text + " (" + results_filter.size() + ")");
            }
        }else{
            if(status.equals("0")){
                filter_text = "Transaksi pending";
            }else{
                filter_text = "Transaksi berhasil";
            }

            if(start.isEmpty() && end.isEmpty()){
                for(int a = 0; a < results_tmp.size(); a++){
                    if(results_tmp.get(a).getStatus().equals(status)){
                        results_filter.add(results_tmp.get(a));
                    }else{}
                }
            }else{
                filter_text = filter_text + " dari " + start + " s/d " + end;
                for(int a = 0; a < results_tmp.size(); a++){
                    if(results_tmp.get(a).getStatus().equals(status) && (f.millisFromDate(start) <= f.millisFromDate(results_tmp.get(a).getTrx_date()))
                    && (f.millisFromDate(end) >= f.millisFromDate(results_tmp.get(a).getTrx_date()))){
                        results_filter.add(results_tmp.get(a));
                    }else{}
                }
            }
            rv_his_trx_list.setVisibility(View.INVISIBLE);
            rv_his_trx_filter.setVisibility(View.VISIBLE);
            txt_filter_text.setText(filter_text + " (" + results_filter.size() + ")");
        }


        rv_his_trx_filter.setLayoutManager(linearLayoutManagerFilter);
        historyTrxAdapterFilter.setListHistoryTrx(results_filter);
        rv_his_trx_filter.setAdapter(historyTrxAdapterFilter);

        //runFirst();
        //loadHistoryTrxListFilter();
    }

//    public void loadHistoryTrxListFilter() {
//        // POST to server through endpoint
//        //dialog.show();
//        viewModel.getHistoryTrxListFilter(prefManager.getUid(), prefManager.getToken(), "1", start, end, status);
//        viewModel.getResultHistoryTrxListFilter().observe(HistoriTransaksiListActivity.this, showHistoryTrxListFilter);
//    }
//
//    private Observer<ArrayList<HistoryTrx>> showHistoryTrxListFilter = new Observer<ArrayList<HistoryTrx>>() {
//        @Override
//        public void onChanged(final ArrayList<HistoryTrx> result) {
//            results = result;
//            if(results.size()==0){
//                rv_his_trx_list.setVisibility(View.INVISIBLE);
//                lbl_no_trx.setVisibility(View.VISIBLE);
//            }else{
//                rv_his_trx_list.setVisibility(View.VISIBLE);
//                lbl_no_trx.setVisibility(View.INVISIBLE);
//                linearLayoutManager = new LinearLayoutManager(HistoriTransaksiListActivity.this);
//                rv_his_trx_list.setLayoutManager(linearLayoutManager);
//                historyTrxAdapter = new HistoryTrxAdapter(HistoriTransaksiListActivity.this);
//                historyTrxAdapter.setListHistoryTrx(results);
//                rv_his_trx_list.setAdapter(historyTrxAdapter);
//                rv_his_trx_list.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
//                    @Override
//                    public void onLoadMore(int current_page) {
//                        loadMoreHistoryFilter(""+current_page);
//                    }
//                });
//                rv_his_trx_list.smoothScrollToPosition(results.size());
//            }
//            dialog.cancel();
//        }
//    };
//
//    public void loadMoreHistoryFilter(String page) {
//        // POST to server through endpoint
//        prog_bar.setVisibility(View.VISIBLE);
//        viewModel.getHistoryTrxListFilter(prefManager.getUid(), prefManager.getToken(), page, start, end, status);
//        viewModel.getResultHistoryTrxListFilter().observe(HistoriTransaksiListActivity.this, showMoreHistoryFilter);
//    }
//
//    private Observer<ArrayList<HistoryTrx>> showMoreHistoryFilter = new Observer<ArrayList<HistoryTrx>>() {
//        @Override
//        public void onChanged(final ArrayList<HistoryTrx> result) {
//            for(int i = 0; i < result.size(); i++){
//                results.add(result.get(i));
//                historyTrxAdapter.notifyDataSetChanged();
//            }
//            //results_tmp = results;
//            prog_bar.setVisibility(View.GONE);
//            rv_his_trx_list.smoothScrollToPosition(results.size());
//            txt_filter_text.setText(filter_text+ " ("+results.size()+")");
//        }
//    };



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }
}