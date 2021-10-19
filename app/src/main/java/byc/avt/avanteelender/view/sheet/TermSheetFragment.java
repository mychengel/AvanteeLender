package byc.avt.avanteelender.view.sheet;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import android.text.Spanned;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import byc.avt.avanteelender.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TermSheetFragment extends BottomSheetDialogFragment {

    private static TermSheetFragment instance;

    public TermSheetFragment() {
        // Required empty public constructor
    }

    public static TermSheetFragment getInstance() {
        if (instance == null) {
            instance = new TermSheetFragment();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sheet_term, container, false);
    }

    Toolbar toolbar;
    Button btnCancel, btnNext;
    CheckBox cbAgree;
    TextView txt_content;
    NestedScrollView nestedSv;
    //public static Spanned text;
    public static String text;
    public static Boolean read = false; //variable untuk get status checked

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = view.findViewById(R.id.toolbar_fr_term);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        nestedSv = view.findViewById(R.id.nested_sv_fr_term);
        txt_content = view.findViewById(R.id.txt_toc_fr_term);
        txt_content.setText(text);
        btnCancel = view.findViewById(R.id.btn_batalkan_fr_term);
        btnCancel.setEnabled(false);
        btnNext = view.findViewById(R.id.btn_lanjutkan_fr_term);
        btnNext.setEnabled(false);
        cbAgree = view.findViewById(R.id.cb_setuju_fr_term);
        cbAgree.setEnabled(false);
        if(read == false){
            btnCancel.setEnabled(false);
            btnNext.setEnabled(false);
            cbAgree.setEnabled(false);
            cbAgree.setChecked(false);
            nestedSv.setScrollY(View.SCROLL_INDICATOR_TOP);
        }

        nestedSv.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == (v.getChildAt(0).getMeasuredHeight()) - v.getMeasuredHeight()) {
                    cbAgree.setEnabled(true);
                }
            }
        });

        // checkbox listener untuk get sudah dicentang atau belum
        cbAgree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    btnCancel.setEnabled(true);
                    btnNext.setEnabled(true);
                }else{
                    read = false;
                    btnCancel.setEnabled(false);
                    btnNext.setEnabled(false);
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                read = false;
                cbAgree.setChecked(false);
                instance.dismiss();
                getActivity().finish();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbAgree.setChecked(false);
                read = true;
                instance.dismiss();
            }
        });
    }

    @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override public void onShow(DialogInterface dialogInterface) {
                BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
                setupFullHeight(bottomSheetDialog);
            }
        });
        return  dialog;
    }


    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
       getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

}
