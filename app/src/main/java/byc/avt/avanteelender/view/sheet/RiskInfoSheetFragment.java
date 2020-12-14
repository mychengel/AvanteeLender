package byc.avt.avanteelender.view.sheet;

import android.os.Bundle;

import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import byc.avt.avanteelender.R;

public class RiskInfoSheetFragment extends BottomSheetDialogFragment {

    private static RiskInfoSheetFragment instance;
    public static String risk_info;

    TextView txt_risk_info;

    public RiskInfoSheetFragment() {
    }

    public RiskInfoSheetFragment(String risk_info){
        this.risk_info = risk_info;
    }

    public static RiskInfoSheetFragment getInstance() {
        instance = null;
        instance = new RiskInfoSheetFragment();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_sheet_risk_info, container, false);
        txt_risk_info = view.findViewById(R.id.txt_risk_info_fr_sheet_risk_info);
        txt_risk_info.setText(risk_info);

        return view;
    }
}