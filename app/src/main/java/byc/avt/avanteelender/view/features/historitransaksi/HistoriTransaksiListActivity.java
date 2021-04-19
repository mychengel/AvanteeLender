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

    Fungsi f = new Fungsi(HistoriTransaksiListActivity.this);
    Toolbar bar;
    private PrefManager prefManager;
    private Dialog dialog;
    private RecyclerView rv_his_trx_list;
    private DashboardViewModel viewModel;
    private TextView lbl_no_trx, txt_filter_text;
    private Button btn_filter;
    private ProgressBar prog_bar;
    public static boolean filter_is_active = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histori_transaksi_list);
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
    }

    public void runFirst(){
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

    ArrayList<HistoryTrx> results = new ArrayList<>();
    HistoryTrxAdapter historyTrxAdapter;
    private Observer<ArrayList<HistoryTrx>> showHistoryTrxList = new Observer<ArrayList<HistoryTrx>>() {
        @Override
        public void onChanged(final ArrayList<HistoryTrx> result) {
            results = result;
            if(results.size()==0){
                rv_his_trx_list.setVisibility(View.INVISIBLE);
                lbl_no_trx.setVisibility(View.VISIBLE);
            }else{
                rv_his_trx_list.setVisibility(View.VISIBLE);
                lbl_no_trx.setVisibility(View.INVISIBLE);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HistoriTransaksiListActivity.this);
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
            prog_bar.setVisibility(View.GONE);
        }
    };

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