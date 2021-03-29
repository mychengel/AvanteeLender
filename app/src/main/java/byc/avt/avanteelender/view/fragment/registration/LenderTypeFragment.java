package byc.avt.avanteelender.view.fragment.registration;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.view.sheet.TermSheetFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class LenderTypeFragment extends Fragment {

    public LenderTypeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lender_type, container, false);
    }

    NavController navController;
    Toolbar toolbar;
    CardView cvIndividu, cvInstitution;
    Fungsi f;
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        f = new Fungsi(getActivity());
        cvIndividu = view.findViewById(R.id.cv_personal_fr_lender_type);
        cvInstitution = view.findViewById(R.id.cv_ins_fr_lender_type);
        cvIndividu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireActivity(), R.id.regis_form_fragment_container).navigate(R.id.action_lenderTypeFragment_to_personalDataFragment);
            }
        });

        cvInstitution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
//        toolbar = view.findViewById(R.id.tb_fr_lender_type);
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);


//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //navController.navigate(R.id.action_lenderTypeFragment_to_welcomeFragment);
//                navController.navigate(R.id.action_lenderTypeFragment_to_welcomeFragment);
//            }
//        });
    }

}
