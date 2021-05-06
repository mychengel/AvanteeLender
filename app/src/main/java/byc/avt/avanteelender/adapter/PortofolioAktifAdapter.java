package byc.avt.avanteelender.adapter;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.model.PortofolioAktif;
import byc.avt.avanteelender.view.features.pendanaan.PendanaanActivity;
import byc.avt.avanteelender.view.features.pendanaan.PendanaanDetailActivity;
import byc.avt.avanteelender.view.fragment.tabportofoliofragment.PortofolioAktifDetailActivity;

public class PortofolioAktifAdapter extends RecyclerView.Adapter<PortofolioAktifAdapter.CardViewViewHolder>{

    Fungsi f = new Fungsi();
    private Context context;
    private ArrayList<PortofolioAktif> listPortofolioAktif;
    private ArrayList<PortofolioAktif> getListPortofolioAktif() {
        return listPortofolioAktif;
    }
    public void setListPortofolioAktif(ArrayList<PortofolioAktif> listPortofolioAktif) {
        this.listPortofolioAktif = listPortofolioAktif;
    }
    public PortofolioAktifAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public PortofolioAktifAdapter.CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_portofolio_aktif, parent, false);
        return new PortofolioAktifAdapter.CardViewViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final PortofolioAktifAdapter.CardViewViewHolder holder, int position) {
        final PortofolioAktif ps = getListPortofolioAktif().get(position);
        holder.txt_status.setVisibility(View.VISIBLE);
        holder.txt_status.setText(ps.getStatus());
        if(ps.getStatus().equalsIgnoreCase("Terlambat")){
            holder.txt_status.setTextColor(ContextCompat.getColor(context, R.color.pending));
        }else{
            holder.txt_status.setTextColor(ContextCompat.getColor(context, R.color.darkO05));
        }
        holder.txt_loan_type.setText(ps.getLoan_type());
        holder.txt_loan_no.setText(ps.getLoan_no());
        holder.txt_loan_rating.setText(ps.getLoan_rating());
        holder.txt_tenor.setText(ps.getTenor()+" hari");
        holder.txt_sisa_tenor.setText(ps.getSisa_tenor()+" hari");
        String interest="";
        if(ps.getInterest() == null){
            interest = "-";
        }else{
            interest = ""+ (int)Float.parseFloat(ps.getInterest());
        }
        holder.txt_interest.setText(interest+"%");
        holder.txt_angs_paid.setText(f.toNumb(ps.getAngs_paid()));
        holder.txt_angs_next.setText(f.toNumb(ps.getAngs_next()));
        //holder.txt_payment_amount.setText(f.toNumb(ps.getPayment_amount()));
        if(ps.getLoan_rating().charAt(0) == 'A'){
            holder.img_mark.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_loan_rating_a));
        }else if(ps.getLoan_rating().charAt(0) == 'B'){
            holder.img_mark.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_loan_rating_b));
        }else if(ps.getLoan_rating().charAt(0) == 'C'){
            holder.img_mark.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_loan_rating_c));
        }else if(ps.getLoan_rating().charAt(0) == 'D'){
            holder.img_mark.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_loan_rating_d));
        }else if(ps.getLoan_rating().charAt(0) == 'E'){
            holder.img_mark.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_loan_rating_e));
        }else{
        }

        holder.btn_det.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, PortofolioAktifDetailActivity.class);
                i.putExtra("port_data", ps);
//                i.putExtra("loan_no", ps.getLoan_no());
//                i.putExtra("funding_id", ps.getFunding_id());
                context.startActivity(i);
                ((AppCompatActivity)context).overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

    }

    @Override
    public int getItemCount() {
        return getListPortofolioAktif().size();
    }

    class CardViewViewHolder extends RecyclerView.ViewHolder{
        TextView txt_loan_type, txt_loan_rating, txt_loan_no, txt_sisa_tenor, txt_tenor, txt_interest, txt_angs_paid, txt_angs_next, txt_status;
        ImageView img_mark, img_ontime;
        Button btn_det;
        ConstraintLayout cons;

        CardViewViewHolder(View itemView) {
            super(itemView);
            btn_det = itemView.findViewById(R.id.btn_detail_adp_port_aktif);
            //btn_det.setVisibility(View.GONE);
            txt_loan_type = itemView.findViewById(R.id.txt_loan_type_adp_port_aktif);
            txt_loan_rating = itemView.findViewById(R.id.txt_loan_rating_adp_port_aktif);
            txt_loan_no = itemView.findViewById(R.id.txt_loan_code_adp_port_aktif);
            txt_sisa_tenor = itemView.findViewById(R.id.txt_sisatenor_adp_port_aktif);
            txt_tenor = itemView.findViewById(R.id.txt_tenor_adp_port_aktif);
            txt_interest = itemView.findViewById(R.id.txt_bunga_adp_port_aktif);
            txt_angs_paid = itemView.findViewById(R.id.txt_angs_sudah_adp_port_aktif);
            txt_angs_next = itemView.findViewById(R.id.txt_angs_selanjutnya_adp_port_aktif);
            img_mark = itemView.findViewById(R.id.img_mark_adp_port_aktif);
            //img_ontime = itemView.findViewById(R.id.img_tepat_waktu_adp_port_aktif);
            //img_ontime.setVisibility(View.GONE);
            txt_status = itemView.findViewById(R.id.lbl_tepat_waktu_adp_port_aktif);
            //txt_is_ontime.setVisibility(View.GONE);
            cons = itemView.findViewById(R.id.cons_adp_port_aktif);
        }
    }
}