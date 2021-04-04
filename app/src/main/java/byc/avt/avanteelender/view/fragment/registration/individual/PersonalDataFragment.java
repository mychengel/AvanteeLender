package byc.avt.avanteelender.view.fragment.registration.individual;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import byc.avt.avanteelender.R;

import static com.google.android.gms.common.util.CollectionUtils.listOf;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalDataFragment extends Fragment {

    public PersonalDataFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_personal_data, container, false);
    }

    Button btn_next;
    AutoCompleteTextView auto_kewarganegaraan;
    //TextInputLayout
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auto_kewarganegaraan = view.findViewById(R.id.auto_kewarganegaraan_fr_personal_data);
        btn_next = view.findViewById(R.id.btn_next_fr_personal_data);
        btn_next.setEnabled(true);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_personalDataFragment_to_workInfoFragment);
            }
        });

        List<String> items = listOf("Material", "Design", "Components", "Android");
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, items);
        auto_kewarganegaraan.setAdapter(adapter);
    }
}
