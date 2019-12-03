package byc.avt.avanteelender.ui.portofolio.tabportofolio;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import byc.avt.avanteelender.R;

public class AktifPortofolioFragment extends Fragment {

    private AktifPortofolioViewModel mViewModel;

    public static AktifPortofolioFragment newInstance() {
        return new AktifPortofolioFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_portofolio_aktif, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AktifPortofolioViewModel.class);
        // TODO: Use the ViewModel
    }

}
