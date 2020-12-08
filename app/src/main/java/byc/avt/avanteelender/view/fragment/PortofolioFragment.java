package byc.avt.avanteelender.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
//import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.adapter.ViewPagerAdapter;
import byc.avt.avanteelender.view.fragment.tabportofoliofragment.AktifPortofolioFragment;
import byc.avt.avanteelender.view.fragment.tabportofoliofragment.PendingPortofolioFragment;
import byc.avt.avanteelender.view.fragment.tabportofoliofragment.SelesaiPortofolioFragment;

public class PortofolioFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       return inflater.inflate(R.layout.fragment_portofolio, container, false);
    }

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabLayout = view.findViewById(R.id.tab_lay_fr_portfolio);
        viewPager = view.findViewById(R.id.vp_fr_portfolio);
        if (viewPager != null) {
            ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
            adapter.addFragment(new AktifPortofolioFragment(), getString(R.string.aktif));
            adapter.addFragment(new PendingPortofolioFragment(), getString(R.string.pending));
            adapter.addFragment(new SelesaiPortofolioFragment(), getString(R.string.selesai));
            viewPager.setAdapter(adapter);
        }
        assert viewPager != null;
        tabLayout.setupWithViewPager(viewPager);
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int i, float v, int i1) {
//            }
//            @Override
//            public void onPageSelected(int i) {
//                tabLayout.getTabAt(i).select();
//            }
//            @Override
//            public void onPageScrollStateChanged(int i) {
//            }
//        });

//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition(), true);
////                if (tab.getPosition() == 0)
////                    getChildFragmentManager().beginTransaction().replace(R.id.vp_fr_portfolio, AktifPortofolioFragment.newInstance(), "Aktif").addToBackStack(null).commit();
////                if (tab.getPosition() == 1)
////                    getChildFragmentManager().beginTransaction().replace(R.id.vp_fr_portfolio, PendingPortofolioFragment.newInstance(), "Pending").addToBackStack(null).commit();
////                if (tab.getPosition() == 2)
////                    getChildFragmentManager().beginTransaction().replace(R.id.vp_fr_portfolio, SelesaiPortofolioFragment.newInstance(), "Selesai").addToBackStack(null).commit();
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
    }

}