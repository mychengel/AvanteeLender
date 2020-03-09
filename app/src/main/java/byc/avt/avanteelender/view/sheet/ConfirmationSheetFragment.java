package byc.avt.avanteelender.view.sheet;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import byc.avt.avanteelender.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmationSheetFragment extends BottomSheetDialogFragment {

    private static ConfirmationSheetFragment instance;
    public static @RawRes int lottie;
    public static String title;
    public static String desc;

    TextView lbl_title, lbl_desc;
    LottieAnimationView lottie_view;

    public ConfirmationSheetFragment() {
    }

    public ConfirmationSheetFragment(@RawRes int lottie, String title, String desc){
        this.lottie = lottie;
        this.title = title;
        this.desc = desc;
    }

    public static ConfirmationSheetFragment getInstance() {
        instance = null;
        instance = new ConfirmationSheetFragment();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_sheet_confirmation, container, false);
        lbl_title = view.findViewById(R.id.lbl_title_fr_sheet_confirmation);
        lbl_desc = view.findViewById(R.id.lbl_des_fr_sheet_confirmation);
        lottie_view = view.findViewById(R.id.lottie_fr_sheet_confirmation);
        lbl_title.setText(title);
        lbl_desc.setText(desc);
        lottie_view.setAnimation(lottie);

        return view;
    }

}
