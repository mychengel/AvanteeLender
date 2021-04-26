package byc.avt.avanteelender.view.fragment.registration.institution;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import byc.avt.avanteelender.R;

public class NarahubungFragment extends Fragment {

    public NarahubungFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_narahubung, container, false);
    }

    Button btn_next;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_next = view.findViewById(R.id.btn_next_fr_narahubung_data);
        btn_next.setEnabled(true);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //confirmNext(v);
                Navigation.findNavController(v).navigate(R.id.action_narahubungFragment_to_companyAddressFragment);
            }
        });
    }
}