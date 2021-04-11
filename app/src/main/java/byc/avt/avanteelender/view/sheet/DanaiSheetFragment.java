package byc.avt.avanteelender.view.sheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import byc.avt.avanteelender.R;

public class DanaiSheetFragment extends BottomSheetDialogFragment {

    private static DanaiSheetFragment instance;
    RadioGroup rg_metode;
    RadioButton radSaldo, radTfBank;
    Button btn_next;
    boolean mSaldo = true, mTfBank = false;
    ImageView img_plus, img_minus;

    public DanaiSheetFragment() {
    }

    public static DanaiSheetFragment getInstance() {
        instance = null;
        instance = new DanaiSheetFragment();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_sheet_danai, container, false);
        rg_metode = view.findViewById(R.id.rg_metode_fr_sheet_danai);
        radSaldo = view.findViewById(R.id.rad_metode_saldo_fr_sheet_danai);
        radTfBank = view.findViewById(R.id.rad_metode_tf_bank_fr_sheet_danai);
        btn_next = view.findViewById(R.id.btn_selanjutnya_fr_sheet_danai);
        img_plus = view.findViewById(R.id.img_plus_fr_sheet_danai);
        img_minus = view.findViewById(R.id.img_minus_fr_sheet_danai);
        /*final int selectedId = rg_metode.getCheckedRadioButtonId();
        radioButton = view.findViewById(selectedId);*/
        radSaldo.setChecked(mSaldo);
        radTfBank.setChecked(mTfBank);

        img_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        img_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        radSaldo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSaldo = true; mTfBank = false;
                radSaldo.setChecked(mSaldo);
                radTfBank.setChecked(mTfBank);
            }
        });

        radTfBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSaldo = false; mTfBank = true;
                radSaldo.setChecked(mSaldo);
                radTfBank.setChecked(mTfBank);
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mSaldo == true){
                    Toast.makeText(getActivity(), "Saldo", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), "Transfer Bank", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
}