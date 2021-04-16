package byc.avt.avanteelender.view.fragment.registration.individual;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.viewmodel.MasterDataViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class DocumentsFragment extends Fragment {

    public DocumentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_documents, container, false);
    }

    private MasterDataViewModel viewModel;
    private PrefManager prefManager;
    private Dialog dialog;
    GlobalVariables gv;
    TextInputLayout edit_ktp, edit_npwp;
    TextView txt_ktp, txt_npwp, txt_selfie, txt_ttd;
    CardView cv_ktp, cv_npwp, cv_selfie, cv_ttd;
    LinearLayout lin_npwp;
    ImageView img_ktp, img_cancelktp, img_npwp, img_cancelnpwp, img_selfie, img_cancelselfie, img_ttd, img_cancelttd;
    String bank="", accountName="", accountNumber="", avgTrans="";
    CheckBox cb_not_have_npwp;
    Button btn_next;

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MasterDataViewModel.class);
        prefManager = PrefManager.getInstance(getActivity());
        dialog = GlobalVariables.loadingDialog(requireActivity());
        cv_ktp = view.findViewById(R.id.cv_take_ktp_fr_documents);
        cv_npwp = view.findViewById(R.id.cv_take_npwp_fr_documents);
        cv_selfie = view.findViewById(R.id.cv_take_selfie_fr_documents);
        cv_ttd = view.findViewById(R.id.cv_take_ttd_fr_documents);
        edit_ktp = view.findViewById(R.id.edit_ktp_number_fr_documents);
        edit_npwp = view.findViewById(R.id.edit_npwp_number_fr_documents);
        txt_ktp = view.findViewById(R.id.txt_take_ktp_fr_documents);
        txt_npwp = view.findViewById(R.id.txt_take_npwp_fr_documents);
        txt_selfie = view.findViewById(R.id.txt_take_selfie_fr_documents);
        txt_ttd = view.findViewById(R.id.txt_take_ttd_fr_documents);
        img_ktp = view.findViewById(R.id.img_take_ktp_fr_documents);
        img_cancelktp = view.findViewById(R.id.img_cancel_take_ktp_fr_document);
        img_npwp = view.findViewById(R.id.img_take_npwp_fr_documents);
        img_cancelnpwp = view.findViewById(R.id.img_cancel_take_npwp_fr_document);
        img_selfie = view.findViewById(R.id.img_take_selfie_fr_documents);
        img_cancelselfie = view.findViewById(R.id.img_cancel_take_selfie_fr_document);
        img_ttd = view.findViewById(R.id.img_take_ttd_fr_documents);
        img_cancelttd = view.findViewById(R.id.img_cancel_take_ttd_fr_document);
        lin_npwp = view.findViewById(R.id.lin_npwp_fr_documents);
        cb_not_have_npwp = view.findViewById(R.id.cb_not_have_npwp_fr_documents);
        cb_not_have_npwp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    lin_npwp.setVisibility(View.GONE);
                }else{
                    lin_npwp.setVisibility(View.VISIBLE);
                }
            }
        });

        cv_ktp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        cv_npwp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        cv_selfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        cv_ttd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        img_cancelktp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        img_cancelnpwp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        img_cancelselfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        img_cancelttd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btn_next = view.findViewById(R.id.btn_next_fr_documents);
        btn_next.setEnabled(true);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Navigation.findNavController(view).navigate(R.id.action_ban);
            }
        });
    }
}
