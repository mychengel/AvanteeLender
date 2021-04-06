package byc.avt.avanteelender;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import byc.avt.avanteelender.view.sheet.DanaiSheetFragment;


public class TkbSheetFragment extends BottomSheetDialogFragment {

    private static TkbSheetFragment instance;

    public static TkbSheetFragment getInstance() {
        instance = null;
        instance = new TkbSheetFragment();
        return instance;
    }

    public TkbSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sheet_tkb, container, false);
    }
}