package byc.avt.avanteelender.adapter;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.model.HistoryTrx;

public class HistoryTrxAdapter extends RecyclerView.Adapter<HistoryTrxAdapter.CardViewViewHolder>{

    AlphaAnimation klik = new AlphaAnimation(1F, 0.6F);
    Fungsi f = new Fungsi();
    private Context context;
    private ArrayList<HistoryTrx> listHistoryTrx;
    private ArrayList<HistoryTrx> getListHistoryTrx() {
        return listHistoryTrx;
    }
    public void setListHistoryTrx(ArrayList<HistoryTrx> listHistoryTrx) {
        this.listHistoryTrx = listHistoryTrx;
    }
    public HistoryTrxAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public HistoryTrxAdapter.CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_trx_adapter, parent, false);
        return new HistoryTrxAdapter.CardViewViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final HistoryTrxAdapter.CardViewViewHolder holder, int position) {
        final HistoryTrx ht = getListHistoryTrx().get(position);

        holder.lbl_title.setText(ht.getDescription());
        holder.lbl_datetime.setText(ht.getTrx_date().substring(0,16));
        holder.lbl_nominal.setText(ht.getNominal());
        if(ht.getNominal().charAt(0) == '-'){
            if (ht.getDescription().matches("(?i).*investasi.*") || ht.getDescription().matches("(?i).*pendanaan.*")) {
                holder.img.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_portfolio_add));
            }else if (ht.getDescription().matches("(?i).*penarikan.*")) {
                holder.img.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_withdraw_white));
            }
            holder.img.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_red));
        }else{
            if (ht.getDescription().matches("(?i).*terima.*") || ht.getDescription().matches("(?i).*pengembalian.*") ||
                ht.getDescription().matches("(?i).*penolakan.*")) {
                holder.img.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_receive_white));
            }else if (ht.getDescription().matches("(?i).*top up.*")) {
                holder.img.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_top_up_white));
            }
            holder.img.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_green));
        }
        holder.img.setPadding(16,16,16,16);
        String stat = "";
        if(ht.getStatus().equals("1")){
            stat = "";
            holder.cons.setBackgroundResource(R.color.white);
        }else {
            stat = "PENDING";
            holder.lbl_status.setTextColor(ContextCompat.getColor(context, R.color.pending));
            holder.cons.setBackgroundResource(R.color.pendingBg);
        }
        holder.lbl_status.setText(stat);
    }

    @Override
    public int getItemCount() {
        return getListHistoryTrx().size();
    }

    class CardViewViewHolder extends RecyclerView.ViewHolder{
        TextView lbl_title, lbl_datetime, lbl_nominal, lbl_status;
        ImageView img;
        ConstraintLayout cons;

        CardViewViewHolder(View itemView) {
            super(itemView);
            lbl_title = itemView.findViewById(R.id.lbl_title_adp_histori_trx);
            lbl_datetime = itemView.findViewById(R.id.lbl_datetime_adp_histori_trx);
            lbl_nominal = itemView.findViewById(R.id.lbl_nominal_adp_histori_trx);
            lbl_status = itemView.findViewById(R.id.lbl_status_adp_histori_trx);
            img = itemView.findViewById(R.id.img_adp_histori_trx);
            cons = itemView.findViewById(R.id.cons_adp_histori_trx);
        }
    }
}
