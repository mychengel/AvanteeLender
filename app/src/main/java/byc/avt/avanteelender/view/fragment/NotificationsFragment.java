package byc.avt.avanteelender.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.adapter.ViewPagerAdapter;
import byc.avt.avanteelender.view.fragment.tabnotificationfragment.InfoNotificationsFragment;
import byc.avt.avanteelender.view.fragment.tabnotificationfragment.PesanNotificationsFragment;

public class NotificationsFragment extends Fragment {

    public static int index = 0;

//    public NotificationsFragment(int index){
//        this.index = index;
//    }

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static ViewPagerAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabLayout = view.findViewById(R.id.tab_lay_fr_notifications);
        viewPager = view.findViewById(R.id.vp_fr_notifications);
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new PesanNotificationsFragment(), getString(R.string.text_pesan));
        adapter.addFragment(new InfoNotificationsFragment(), getString(R.string.info_dan_penawaran));
        setNotifTab(index);
    }

    public static void setNotifTab(int i){
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.selectTab(tabLayout.getTabAt(i));
    }
}