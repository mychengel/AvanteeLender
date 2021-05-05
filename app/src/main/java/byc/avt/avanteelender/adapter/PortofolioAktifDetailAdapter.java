package byc.avt.avanteelender.adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.model.PortofolioAktifDetail;

public class PortofolioAktifDetailAdapter extends RecyclerView.Adapter<PortofolioAktifDetailAdapter.CardViewViewHolder>{

    Fungsi f = new Fungsi();
    private Context context;
    boolean is_expand = false;
    private ArrayList<PortofolioAktifDetail> listPortofolioAktifDetail;
    private ArrayList<PortofolioAktifDetail> getListPortofolioAktifDetail() {
        return listPortofolioAktifDetail;
    }
    public void setListPortofolioAktifDetail(ArrayList<PortofolioAktifDetail> listPortofolioAktifDetail) {
        this.listPortofolioAktifDetail = listPortofolioAktifDetail;
    }
    public PortofolioAktifDetailAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public PortofolioAktifDetailAdapter.CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_portofolio_aktif_detail, parent, false);
        return new PortofolioAktifDetailAdapter.CardViewViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final PortofolioAktifDetailAdapter.CardViewViewHolder holder, int position) {
        final PortofolioAktifDetail pad = getListPortofolioAktifDetail().get(position);
        holder.lbl_title_date.setText(f.tglFull(pad.getDate_payment()));
        holder.lbl_title_nom.setText(f.toNumb(pad.getPayment_amount()));
        holder.lbl_actual_date.setText(f.tglFull(pad.getDate_actualtrans()));
        holder.lbl_actual_nom.setText(f.toNumb(pad.getActual_payment()));
        String delay = "";
        if(pad.getDelay_details().equalsIgnoreCase(null) || pad.getDelay_details().equalsIgnoreCase("null")){
            delay = "-";
        }else{
            delay = pad.getDelay_details();
        }
        holder.lbl_delay_details.setText("Keterangan terlambat: "+delay);
        holder.img_expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!is_expand){
                    holder.img_expand.setRotation(180);
                    holder.cons.setVisibility(View.VISIBLE);
                    is_expand = !is_expand;
                }else{
                    holder.img_expand.setRotation(0);
                    holder.cons.setVisibility(View.GONE);
                    is_expand = !is_expand;
                }
            }
        });

        String next_payment = pad.getDate_payment();
        int selisih = new Fungsi(context).selisihHari(next_payment);

        holder.lbl_period.setText(pad.getPeriode());
        if((pad.getDate_actualtrans() != "null") && (pad.getStatus().equalsIgnoreCase("Lancar"))){
            holder.img_flag.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_schedule_num_paid));
            holder.lbl_period.setVisibility(View.GONE);
        }else if((pad.getDate_actualtrans() == "null") && (pad.getStatus() == "null") && (selisih > 0)){
            holder.img_flag.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_bg_round_white));
            holder.lbl_period.setVisibility(View.VISIBLE);
        }else if((pad.getDate_actualtrans() != "null") && (pad.getStatus() == "null" || pad.getStatus().equalsIgnoreCase("Tidak Lancar") || pad.getStatus().equalsIgnoreCase("Macet"))){
            holder.img_flag.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_schedule_num_paid_late));
            holder.lbl_period.setVisibility(View.GONE);
        }else if((pad.getDate_actualtrans() == "null") && (pad.getStatus() == "null") && pad.getActual_payment().equalsIgnoreCase("0") && (selisih <= 0)){
            holder.img_flag.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_schedule_num_unpaid));
            holder.lbl_period.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return getListPortofolioAktifDetail().size();
    }

    class CardViewViewHolder extends RecyclerView.ViewHolder{
        TextView lbl_title_date, lbl_title_nom, lbl_actual_date, lbl_actual_nom, lbl_delay_details, lbl_period;
        ImageView img_flag, img_expand;
        ConstraintLayout cons;

        CardViewViewHolder(View itemView) {
            super(itemView);
            lbl_title_date = itemView.findViewById(R.id.lbl_title_date_adp_port_aktif_det);
            lbl_title_nom = itemView.findViewById(R.id.lbl_title_nom_adp_port_aktif_det);
            lbl_actual_date = itemView.findViewById(R.id.lbl_actual_date_adp_port_aktif_det);
            lbl_actual_nom = itemView.findViewById(R.id.lbl_actual_nom_adp_port_aktif_det);
            lbl_delay_details = itemView.findViewById(R.id.lbl_delay_det_adp_port_aktif_det);
            lbl_period = itemView.findViewById(R.id.lbl_flag_ke_adp_port_aktif_det);
            img_flag = itemView.findViewById(R.id.img_flag_adp_port_aktif_det);
            img_expand = itemView.findViewById(R.id.img_expand_adp_port_aktif_det);
            cons = itemView.findViewById(R.id.cons_expand_adp_port_aktif_det);
            cons.setVisibility(View.GONE);
        }
    }
}