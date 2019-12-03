package byc.avt.avanteelender.ui.portofolio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
//import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.ui.portofolio.tabportofolio.AktifPortofolioFragment;
import byc.avt.avanteelender.ui.portofolio.tabportofolio.PendingPortofolioFragment;
import byc.avt.avanteelender.ui.portofolio.tabportofolio.SelesaiPortofolioFragment;

public class PortofolioFragment extends Fragment {

    ActionBar bar;
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    TabLayout tabLayout;
    private PortofolioViewModel portofolioViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        portofolioViewModel =
                ViewModelProviders.of(this).get(PortofolioViewModel.class);
        View root = inflater.inflate(R.layout.fragment_portofolio, container, false);

        portofolioViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });

        bar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        bar.show();
        bar.setTitle(R.string.portofolioinvest);
        bar.setElevation(0);

        mSectionsPagerAdapter = new PortofolioFragment.SectionsPagerAdapter(getChildFragmentManager());

        mViewPager = root.findViewById(R.id.vp_fr_portofolio);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = root.findViewById(R.id.tab_lay_fr_portofolio);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        return root;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position){
                case 0:
                    AktifPortofolioFragment t1 = new AktifPortofolioFragment();
                    return t1;
                case 1:
                    PendingPortofolioFragment t2 = new PendingPortofolioFragment();
                    return t2;
                case 2:
                    SelesaiPortofolioFragment t3 = new SelesaiPortofolioFragment();
                    return t3;
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            return 3;
        }
    }


}