package byc.avt.avanteelender.view.fragment.registration.institution;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.GlobalVariables;

public class CompanyDataFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_company_data, container, false);
    }

    Button btn_next;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_next = view.findViewById(R.id.btn_next_fr_com_data);
        btn_next.setEnabled(true);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //confirmNext(v);
                Navigation.findNavController(v).navigate(R.id.action_companyDataFragment_to_narahubungFragment);
            }
        });
        Log.e("tipeinvestorins", ""+ GlobalVariables.insRegData.get("tipe_investor"));

    }
}