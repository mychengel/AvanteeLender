package byc.avt.avanteelender.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.adapter.ViewPagerAdapter;
import byc.avt.avanteelender.view.fragment.tabnotificationfragment.InfoNotificationsFragment;
import byc.avt.avanteelender.view.fragment.tabnotificationfragment.PesanNotificationsFragment;
import byc.avt.avanteelender.view.fragment.tabportofoliofragment.AktifPortofolioFragment;
import byc.avt.avanteelender.view.fragment.tabportofoliofragment.PendingPortofolioFragment;
import byc.avt.avanteelender.view.fragment.tabportofoliofragment.SelesaiPortofolioFragment;
import byc.avt.avanteelender.viewmodel.NotificationsViewModel;

public class NotificationsFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TabLayout tabLayout = view.findViewById(R.id.tab_lay_fr_notifications);
        ViewPager viewPager = view.findViewById(R.id.vp_fr_notifications);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new PesanNotificationsFragment(), getString(R.string.text_pesan));
        adapter.addFragment(new InfoNotificationsFragment(), getString(R.string.info_dan_penawaran));
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }
}