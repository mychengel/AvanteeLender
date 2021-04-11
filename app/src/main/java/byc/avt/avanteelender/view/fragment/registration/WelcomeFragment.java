package byc.avt.avanteelender.view.fragment.registration;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.view.misc.OTPActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class WelcomeFragment extends Fragment {

    public WelcomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }

    Button btn_begin;
    NavController navController;
    ConstraintLayout layout;
    Fungsi f;
    GlobalVariables gv;

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        f = new Fungsi(getActivity());
        gv.perRegData.clear();
        btn_begin = view.findViewById(R.id.btn_welcome_form_begin);
        btn_begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new Fungsi(getActivity()).showMessage("In progress ~ Baycode");
                Navigation.findNavController(requireActivity(), R.id.regis_form_fragment_container).navigate(R.id.action_nav_to_lendertype);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
//       ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
//        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
}
