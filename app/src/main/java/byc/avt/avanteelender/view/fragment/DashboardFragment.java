package byc.avt.avanteelender.view.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.VisibilityPropagation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.Array;
import java.util.ArrayList;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.adapter.HistoryTrxAdapter;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.intro.SplashActivity;
import byc.avt.avanteelender.intro.WalkthroughActivity;
import byc.avt.avanteelender.model.HistoryTrx;
import byc.avt.avanteelender.view.others.SettingActivity;
import byc.avt.avanteelender.viewmodel.DashboardViewModel;
import byc.avt.avanteelender.viewmodel.SplashViewModel;

public class DashboardFragment extends Fragment {

    private DashboardViewModel viewModel;
    Toolbar toolbar;
    Fungsi f = new Fungsi(getActivity());
    private PrefManager prefManager;
    private NavigationView navigationView;
    private RecyclerView rvHistoryTrx;
    private TextView txt_no_trans_history;
    private Dialog dialog;
    //private ArrayList<HistoryTrx> listHistoryTrx;

    JSONObject objWallet, objDashboard;

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
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        //Recycler View
        rvHistoryTrx = view.findViewById(R.id.rv_histori_trx_fr_dashboard);
        txt_no_trans_history = view.findViewById(R.id.txt_no_transaction_history_fr_dashboar);

        final ImageView pp_icon = view.findViewById(R.id.img_pp_tb_fr_dashboard);
        TextView txt_name = view.findViewById(R.id.txt_name_tb_fr_dashboard);
        txt_name.setText(prefManager.getName());
        TextView txt_code = view.findViewById(R.id.txt_code_tb_fr_dashboard);
        txt_code.setText("CLI-");
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

        loadDashboard();
    }

    public void runFirst(){
        txt_no_trans_history.setVisibility(View.INVISIBLE);
    }

    public void loadDashboard() {
        // POST to server through endpoint
        dialog.show();
        viewModel.getHistoryTrx(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultHistoryTrx().observe(getActivity(), showHistoryTrx);
    }

    private Observer<ArrayList<HistoryTrx>> showHistoryTrx = new Observer<ArrayList<HistoryTrx>>() {
        @Override
        public void onChanged(ArrayList<HistoryTrx> result) {
            //listHistoryTrx.clear();
            //rvHistoryTrx.setAdapter(null);
            Log.e("LISTHIS", result.toString());
            if(result.isEmpty()){
                dialog.cancel();
                rvHistoryTrx.setVisibility(View.INVISIBLE);
                txt_no_trans_history.setVisibility(View.VISIBLE);
            }else{
                dialog.cancel();
                rvHistoryTrx.setVisibility(View.VISIBLE);
                txt_no_trans_history.setVisibility(View.INVISIBLE);
                rvHistoryTrx.setLayoutManager(new LinearLayoutManager(getActivity()));
                HistoryTrxAdapter historyTrxAdapter = new HistoryTrxAdapter(getActivity());
                historyTrxAdapter.setListHistoryTrx(result);
                rvHistoryTrx.setAdapter(historyTrxAdapter);
            }

        }
    };

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