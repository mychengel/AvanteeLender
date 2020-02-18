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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TabLayout tabLayout = view.findViewById(R.id.tab_lay_fr_portofolio);
        ViewPager viewPager = view.findViewById(R.id.vp_fr_portofolio);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new AktifPortofolioFragment(), getString(R.string.aktif));
        adapter.addFragment(new PendingPortofolioFragment(), getString(R.string.pending));
        adapter.addFragment(new SelesaiPortofolioFragment(), getString(R.string.selesai));
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }
}