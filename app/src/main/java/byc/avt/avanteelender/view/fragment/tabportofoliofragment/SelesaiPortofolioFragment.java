package byc.avt.avanteelender.view.fragment.tabportofoliofragment;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.viewmodel.tabportofolioviewmodel.SelesaiPortofolioViewModel;

public class SelesaiPortofolioFragment extends Fragment {

    private SelesaiPortofolioViewModel mViewModel;

    public static SelesaiPortofolioFragment newInstance() {
        return new SelesaiPortofolioFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_portofolio_selesai, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SelesaiPortofolioViewModel.class);
        // TODO: Use the ViewModel
    }

}
