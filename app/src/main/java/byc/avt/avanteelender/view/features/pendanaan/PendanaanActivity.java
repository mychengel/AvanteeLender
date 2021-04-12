package byc.avt.avanteelender.view.features.pendanaan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Objects;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.view.sheet.TkbSheetFragment;
import byc.avt.avanteelender.adapter.PendanaanAdapter;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.ItemClickSupport;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.model.Pendanaan;
import byc.avt.avanteelender.viewmodel.PendanaanViewModel;

public class PendanaanActivity extends AppCompatActivity {

    private PendanaanViewModel viewModel;
    private PrefManager prefManager;
    private Dialog dialog;
    private Toolbar toolbar;
    private Fungsi f;
    private ImageView img_tbk;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendanaan);
        f = new Fungsi(PendanaanActivity.this);
        viewModel = new ViewModelProvider(PendanaanActivity.this).get(PendanaanViewModel.class);
        prefManager = PrefManager.getInstance(PendanaanActivity.this);
        toolbar = findViewById(R.id.toolbar_pendanaan);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_back_24px);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rv = findViewById(R.id.rv_pendanaan);
        dialog = GlobalVariables.loadingDialog(PendanaanActivity.this);
        img_tbk = findViewById(R.id.img_tbk_pendanaan);
        img_tbk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(f.clickAnim());
                TkbSheetFragment tkbFragment = TkbSheetFragment.getInstance();
                tkbFragment.show(getSupportFragmentManager(), tkbFragment.getTag());
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        }, 400);
    }

    private void loadData() {
        rv.setAdapter(null);
        dialog.show();
        viewModel.getListPendanaan(prefManager.getUid(), prefManager.getToken());
        viewModel.getListPendanaanResult().observe(PendanaanActivity.this, showListPendanaan);
    }

    private Observer<ArrayList<Pendanaan>> showListPendanaan = new Observer<ArrayList<Pendanaan>>() {
        @Override
        public void onChanged(final ArrayList<Pendanaan> result) {
            if(result.isEmpty()){
                f.showMessage("Belum ada pinjaman yang siap didanai.");
            }else{
                rv.setLayoutManager(new LinearLayoutManager(PendanaanActivity.this));
                PendanaanAdapter adapter = new PendanaanAdapter(PendanaanActivity.this);
                adapter.setListPendanaan(result);
                rv.setAdapter(adapter);
                ItemClickSupport.addTo(rv).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        v.startAnimation(f.clickAnim());
                        Pendanaan pendanaan = result.get(position);
                        Intent i = new Intent(PendanaanActivity.this, PendanaanDetailActivity.class);
                        i.putExtra("loan_no", pendanaan.getLoan_no());
                        i.putExtra("tenor", pendanaan.getJumlah_hari_pinjam());
                        startActivity(i);
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                    }
                });
            }
            dialog.cancel();
        }
    };

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
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