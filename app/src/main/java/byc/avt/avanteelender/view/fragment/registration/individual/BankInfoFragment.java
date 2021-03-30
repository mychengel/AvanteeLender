package byc.avt.avanteelender.view.fragment.registration.individual;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class BankInfoFragment extends Fragment {

    public BankInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bank_info, container, false);
    }

    Button btn_next;
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btn_next = view.findViewById(R.id.btn_next_fr_bank_info);
        btn_next.setEnabled(true);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_bankInfoFragment_to_documentsFragment);
            }
        });
    }
}