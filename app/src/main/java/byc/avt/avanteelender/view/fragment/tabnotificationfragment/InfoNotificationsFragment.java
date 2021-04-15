package byc.avt.avanteelender.view.fragment.tabnotificationfragment;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.adapter.BlogAdapter;
import byc.avt.avanteelender.adapter.PortofolioPendingAdapter;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.model.Blog;
import byc.avt.avanteelender.model.PortofolioPending;
import byc.avt.avanteelender.view.others.RiskInfoActivity;
import byc.avt.avanteelender.viewmodel.AuthenticationViewModel;
import byc.avt.avanteelender.viewmodel.DashboardViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InfoNotificationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoNotificationsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InfoNotificationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InfoNotificationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoNotificationsFragment newInstance(String param1, String param2) {
        InfoNotificationsFragment fragment = new InfoNotificationsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private Dialog dialog;
    Fungsi f = new Fungsi(getActivity());
    private PrefManager prefManager;
    private TextView txt_no_data;
    private DashboardViewModel viewModel;
    private RecyclerView rv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifications_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(getActivity()).get(DashboardViewModel.class);
        dialog = GlobalVariables.loadingDialog(requireActivity());
        prefManager = PrefManager.getInstance(getActivity());
        txt_no_data = view.findViewById(R.id.txt_no_data_fr_notif_info);
        txt_no_data.setVisibility(View.GONE);
        rv = view.findViewById(R.id.rv_fr_notif_info);
        loadData();
    }

    private void loadData(){
        dialog.show();
        viewModel.getBlog(prefManager.getUid(), prefManager.getToken());
        viewModel.getResultBlog().observe(getActivity(), showData);
    }

    private Observer<ArrayList<Blog>> showData = new Observer<ArrayList<Blog>>() {
        @Override
        public void onChanged(ArrayList<Blog> result) {
            if(result.isEmpty()){
                txt_no_data.setVisibility(View.VISIBLE);
            }else{
                txt_no_data.setVisibility(View.GONE);
                rv.setLayoutManager(new LinearLayoutManager(getActivity()));
                BlogAdapter blogAdapter = new BlogAdapter(getActivity());
                blogAdapter.setListBlog(result);
                rv.setAdapter(blogAdapter);
            }
            dialog.cancel();
        }
    };

}