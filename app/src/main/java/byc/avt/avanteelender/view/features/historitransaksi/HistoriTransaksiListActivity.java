package byc.avt.avanteelender.view.features.historitransaksi;

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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.adapter.HistoryTrxAdapter;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.model.HistoryTrx;
import byc.avt.avanteelender.view.auth.LoginActivity;
import byc.avt.avanteelender.viewmodel.DashboardViewModel;

public class HistoriTransaksiListActivity extends AppCompatActivity {

    Fungsi f = new Fungsi(HistoriTransaksiListActivity.this);
    Toolbar bar;
    private PrefManager prefManager;
    private Dialog dialog;
    private RecyclerView rv_his_trx_list;
    private DashboardViewModel viewModel;
    private TextView lbl_no_trx;

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
        rv_his_trx_list = findViewById(R.id.rv_his_trx_list);
        lbl_no_trx = findViewById(R.id.lbl_no_trx_his_trx_list);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                runFirst();
                loadHistoryTrxList();
            }
        }, 200);
    }

    public void runFirst(){
        rv_his_trx_list.setVisibility(View.INVISIBLE);
        lbl_no_trx.setVisibility(View.INVISIBLE);
    }

    public void loadHistoryTrxList() {
        // POST to server through endpoint
        dialog.show();
        viewModel.getHistoryTrxList(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultHistoryTrxList().observe(HistoriTransaksiListActivity.this, showHistoryTrxList);
    }

    private Observer<ArrayList<HistoryTrx>> showHistoryTrxList = new Observer<ArrayList<HistoryTrx>>() {
        @Override
        public void onChanged(ArrayList<HistoryTrx> result) {
            if(result.size()==0){
                rv_his_trx_list.setVisibility(View.INVISIBLE);
                lbl_no_trx.setVisibility(View.VISIBLE);
            }else{
                rv_his_trx_list.setVisibility(View.VISIBLE);
                lbl_no_trx.setVisibility(View.INVISIBLE);
                rv_his_trx_list.setLayoutManager(new LinearLayoutManager(HistoriTransaksiListActivity.this));
                HistoryTrxAdapter historyTrxAdapter = new HistoryTrxAdapter(HistoriTransaksiListActivity.this);
                historyTrxAdapter.setListHistoryTrx(result);
                rv_his_trx_list.setAdapter(historyTrxAdapter);
            }
            dialog.cancel();
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