package byc.avt.avanteelender.view.sheet;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import byc.avt.avanteelender.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmationSheetFragment extends BottomSheetDialogFragment {

    private static ConfirmationSheetFragment instance;

    public ConfirmationSheetFragment() {

    }

    public static ConfirmationSheetFragment getInstance() {
        if (instance == null) {
            instance = new ConfirmationSheetFragment();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sheet_confirmation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


}
