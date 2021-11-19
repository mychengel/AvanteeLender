package byc.avt.avanteelender.view.sheet;

import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.RequiresApi;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.view.features.historitransaksi.HistoriTransaksiListActivity;

public class HisTransFilterSheetFragment extends BottomSheetDialogFragment {

    private static HisTransFilterSheetFragment instance;
    RadioGroup rg_periode, rg_status, rg_type;
    RadioButton rb_periode, rb_status, rb_type;
    RadioButton rb_90s, rb_all, rb_pof, rb_berhasil, rb_pending, rb_pembayaran, rb_penarikan;
    Button btn_terapkan, btn_reset;
    static boolean m90s = true, mAll = true, mPof = false, mBerhasil = true, mPending = false, mPembayaran = true, mPenarikan = false;
    AutoCompleteTextView date_start, date_end;
    LinearLayout lin_pof;
    static String dateStart = "", dateEnd = "", status = "";
    CalendarConstraints.Builder constraintBuilderStart, constraintBuilderEnd;
    long selNow = 0, selStart = 0, selEnd = 0;

    public HisTransFilterSheetFragment() {
    }

    public static HisTransFilterSheetFragment getInstance() {
        instance = null;
        instance = new HisTransFilterSheetFragment();
        return instance;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_sheet_filter_his_trans, container, false);
        lin_pof = view.findViewById(R.id.lin_range_pof_fr_sheet_filter_his_trans);
        rg_periode = view.findViewById(R.id.rad_group_pof_fr_sheet_filter_his_trans);
        rg_status = view.findViewById(R.id.rad_group_status_fr_sheet_filter_his_trans);
        rg_type = view.findViewById(R.id.rad_group_type_fr_sheet_filter_his_trans);
        rb_all = view.findViewById(R.id.rad_all_pof_fr_sheet_filter_his_trans);
        rb_pof = view.findViewById(R.id.rad_range_pof_fr_sheet_filter_his_trans);
        rb_berhasil = view.findViewById(R.id.rad_berhasil_fr_sheet_filter_his_trans);
        rb_pending = view.findViewById(R.id.rad_pending_fr_sheet_filter_his_trans);
        rb_pembayaran = view.findViewById(R.id.rad_pembayaran_fr_sheet_filter_his_trans);
        rb_penarikan = view.findViewById(R.id.rad_penarikan_fr_sheet_filter_his_trans);
        date_start = view.findViewById(R.id.date_start_pof_fr_sheet_filter_his_trans);
        date_end = view.findViewById(R.id.date_end_pof_fr_sheet_filter_his_trans);
        btn_reset = view.findViewById(R.id.btn_reset_fr_sheet_filter_his_trans);
        btn_terapkan = view.findViewById(R.id.btn_terapkan_fr_sheet_filter_his_trans);

        date_start.setFocusable(false);
        selNow = Calendar.getInstance().getTimeInMillis() + 25200000;
        selStart = Calendar.getInstance().getTimeInMillis() + 25200000;
        constraintBuilderStart = new CalendarConstraints.Builder();
        constraintBuilderStart.setValidator(DateValidatorPointBackward.now());

        selEnd = Calendar.getInstance().getTimeInMillis() + 25200000;
        constraintBuilderEnd = new CalendarConstraints.Builder();
        constraintBuilderEnd.setValidator(DateValidatorPointBackward.now());

        date_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                final SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");


                MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
                builder.setTitleText("Tanggal batas awal");
                builder.setSelection(selStart);
                builder.setCalendarConstraints(constraintBuilderStart.build());
                MaterialDatePicker picker = builder.build();
                picker.show(getActivity().getSupportFragmentManager(),"start");
                picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        Log.e("Hasil Date:",selection.toString());
                        dateStart = sdf.format((Long)selection + 25200000);
                        date_start.setText(sdf2.format((Long)selection + 25200000));
                        selStart = (Long) selection;
                        constraintBuilderEnd.setValidator(DateValidatorPointForward.from((Long) selection));
                        if(selStart > selNow){
                            new Fungsi(getActivity()).showMessage(getString(R.string.validate_sel_now));
                        }
                        //constraintBuilderEnd.setValidator(DateValidatorPointBackward.now());
                    }
                });
            }
        });

        date_end.setFocusable(false);
        date_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                final SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");

                MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
                builder.setTitleText("Tanggal batas akhir");
                builder.setSelection(selEnd);
                builder.setCalendarConstraints(constraintBuilderEnd.build());
                MaterialDatePicker picker = builder.build();

                picker.show(getActivity().getSupportFragmentManager(),"end");
                picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        dateEnd = sdf.format((Long)selection + 25200000);
                        date_end.setText(sdf2.format((Long)selection + 25200000));
                        selEnd = (Long) selection;
                        constraintBuilderStart.setValidator(DateValidatorPointBackward.before((Long) selection));
                        if(selEnd > selNow){
                            new Fungsi(getActivity()).showMessage(getString(R.string.validate_sel_now));
                        }
                        //constraintBuilderStart.setValidator(DateValidatorPointForward.from((Long) selection));
                    }
                });
            }
        });

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rb_all.setChecked(true);
                rb_berhasil.setChecked(false);
                rb_pending.setChecked(false);
                mAll = true;
                mPof = false;
                status = "";
                dateStart = "";
                dateEnd = "";
                HistoriTransaksiListActivity.filterRun(dateStart, dateEnd, status);
            }
        });

        rg_periode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                rb_periode = view.findViewById(selectedId);
                if(rb_periode.getText().equals("Semuanya")){
                    mAll = true; mPof = false;
                }else{
                    mAll = false; mPof = true;
                }

                if(mPof){
                    lin_pof.setVisibility(View.VISIBLE);
                }else{
                    lin_pof.setVisibility(View.GONE);
                }
            }
        });

        rg_status.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                rb_status = view.findViewById(selectedId);
                if(rb_status.getText().equals("Berhasil")){
                    status = "1";
                }else{
                    status = "0";
                }
            }
        });

        btn_terapkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mPof){
                    if(dateStart.isEmpty() || dateEnd.isEmpty()){
                        new Fungsi(getActivity()).showMessage(getString(R.string.pof_cannot_null));
                    }else{
                        if((selStart > selNow) || (selEnd > selNow)){
                            new Fungsi(getActivity()).showMessage(getString(R.string.validate_sel_now));
                        }else{
                            HistoriTransaksiListActivity.filterRun(dateStart, dateEnd, status);
                            instance.dismiss();
                        }

                    }
                }else{
                    dateStart = "";
                    dateEnd = "";
                    HistoriTransaksiListActivity.filterRun(dateStart, dateEnd, status);
                    instance.dismiss();
                }

            }
        });

        loadAwal();
        return view;
    }

     public void loadAwal(){
        if(status.isEmpty()){
            rb_berhasil.setChecked(false);
            rb_pending.setChecked(false);
        }else{
            if(status.equals("1")){
                rb_berhasil.setChecked(true);
                rb_pending.setChecked(false);
            }else{
                rb_berhasil.setChecked(false);
                rb_pending.setChecked(true);
            }
        }

        if(dateStart.isEmpty() && dateEnd.isEmpty()){
            rb_all.setChecked(true);
            rb_pof.setChecked(false);
            lin_pof.setVisibility(View.GONE);
            date_start.setText("");
            date_end.setText("");
        }else{
            rb_all.setChecked(false);
            rb_pof.setChecked(true);
            lin_pof.setVisibility(View.VISIBLE);
            date_start.setText(dateStart);
            date_end.setText(dateEnd);
        }
    }
}