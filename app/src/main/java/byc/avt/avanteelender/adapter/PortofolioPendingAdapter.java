package byc.avt.avanteelender.adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.model.PortofolioPending;

public class PortofolioPendingAdapter extends RecyclerView.Adapter<PortofolioPendingAdapter.CardViewViewHolder>{

    AlphaAnimation klik = new AlphaAnimation(1F, 0.6F);
    Fungsi f = new Fungsi();
    private Context context;
    private ArrayList<PortofolioPending> listPortofolioPending;
    private ArrayList<PortofolioPending> getListPortofolioPending() {
        return listPortofolioPending;
    }
    public void setListPortofolioPending(ArrayList<PortofolioPending> listPortofolioPending) {
        this.listPortofolioPending = listPortofolioPending;
    }
    public PortofolioPendingAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public PortofolioPendingAdapter.CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_portofolio_pending, parent, false);
        return new PortofolioPendingAdapter.CardViewViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final PortofolioPendingAdapter.CardViewViewHolder holder, int position) {
        final PortofolioPending ps = getListPortofolioPending().get(position);
//        if(ps.getIs_paid().equalsIgnoreCase("-")){
//            holder.txt_is_paid.setVisibility(View.VISIBLE);
//            holder.img_done.setVisibility(View.VISIBLE);
//        }else{
//            holder.txt_is_paid.setVisibility(View.VISIBLE);
//            holder.img_done.setVisibility(View.VISIBLE);
//        }
        holder.txt_loan_type.setText(ps.getLoan_type());
        holder.txt_loan_no.setText(ps.getLoan_no());
        holder.txt_loan_rating.setText(ps.getLoan_rating());
        holder.txt_est_date.setText(f.tglFull(ps.getEst_pengembalian_date().substring(0,10)));
        holder.txt_tenor.setText(ps.getTenor()+" hari");
        holder.txt_interest.setText(""+ (int)Float.parseFloat(ps.getInterest())+"%");
        holder.txt_nominal.setText(f.toNumb(ps.getNominal()));
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

    }

    @Override
    public int getItemCount() {
        return getListPortofolioPending().size();
    }

    class CardViewViewHolder extends RecyclerView.ViewHolder{
        TextView txt_loan_type, txt_loan_rating, txt_loan_no, txt_est_date, txt_tenor, txt_interest, txt_nominal, txt_is_paid;
        ImageView img_mark, img_done;
        ConstraintLayout cons;

        CardViewViewHolder(View itemView) {
            super(itemView);
            txt_loan_type = itemView.findViewById(R.id.txt_loan_type_adp_port_pending);
            txt_loan_rating = itemView.findViewById(R.id.txt_loan_rating_adp_port_pending);
            txt_loan_no = itemView.findViewById(R.id.txt_loan_no_adp_port_pending);
            txt_est_date = itemView.findViewById(R.id.txt_est_pengembalian_adp_port_pending);
            txt_tenor = itemView.findViewById(R.id.txt_tenor_adp_port_pending);
            txt_interest = itemView.findViewById(R.id.txt_interest_adp_port_pending);
            txt_nominal = itemView.findViewById(R.id.txt_nominal_pendanaan_adp_port_pending);
            txt_is_paid = itemView.findViewById(R.id.lbl_sudah_dibayar_adp_port_pending);
            img_mark = itemView.findViewById(R.id.img_mark_adp_port_pending);
            img_done = itemView.findViewById(R.id.img_done_adp_port_pending);
            cons = itemView.findViewById(R.id.cons_adp_port_pending);
        }
    }
}
