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
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.model.Pendanaan;

public class PendanaanAdapter extends RecyclerView.Adapter<PendanaanAdapter.CardViewViewHolder>{

    AlphaAnimation klik = new AlphaAnimation(1F, 0.6F);
    Fungsi f = new Fungsi();
    private Context context;
    private ArrayList<Pendanaan> listPendanaan;
    private ArrayList<Pendanaan> getListPendanaan() {
        return listPendanaan;
    }
    public void setListPendanaan(ArrayList<Pendanaan> listPendanaan) {
        this.listPendanaan = listPendanaan;
    }
    public PendanaanAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public PendanaanAdapter.CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pendanaan, parent, false);
        return new PendanaanAdapter.CardViewViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final PendanaanAdapter.CardViewViewHolder holder, int position) {
        final Pendanaan p = getListPendanaan().get(position);
        char rating = p.getRating_pinjaman().charAt(0);
        int selisihHari = f.selisihHari(p.getPublikasi_end());
        int terkumpulPersen = f.terkumpulPersen(p.getNominal_pinjaman(), p.getFunding());

        holder.txt_loan_type.setText(p.getLoan_type());
        holder.txt_loan_no.setText(p.getLoan_no());
        holder.txt_loan_rating.setText(p.getRating_pinjaman());
        holder.txt_sisa_hari_publikasi.setText(selisihHari+" hari lagi");
        holder.txt_tenor.setText(p.getJumlah_hari_pinjam());
        holder.txt_interest.setText(""+ (int) Float.parseFloat(p.getInvest_bunga()));
        holder.txt_nominal.setText(f.toNumb(p.getNominal_pinjaman()));
        holder.txt_funding_percent.setText(terkumpulPersen+"%");
        holder.progress.setProgress(terkumpulPersen);
        if(rating == 'A'){
            holder.img_mark.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_loan_rating_a));
        }else if(rating == 'B'){
            holder.img_mark.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_loan_rating_b));
        }else if(rating == 'C'){
            holder.img_mark.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_loan_rating_c));
        }else if(rating == 'D'){
            holder.img_mark.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_loan_rating_d));
        }else if(rating == 'E'){
            holder.img_mark.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_loan_rating_e));
        }else{
        }

        if(selisihHari >= 5){
            holder.img_sisa_hari.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_sisa_7hari));
        }else if(selisihHari >= 3 && selisihHari < 5){
            holder.img_sisa_hari.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_sisa_4hari));
        }else if(selisihHari >= 0 && selisihHari < 3){
            holder.img_sisa_hari.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_sisa_2hari));
        }

    }

    @Override
    public int getItemCount() {
        return getListPendanaan().size();
    }

    class CardViewViewHolder extends RecyclerView.ViewHolder{
        TextView txt_loan_type, txt_loan_rating, txt_loan_no, txt_tenor, txt_interest, txt_nominal, txt_funding_percent, txt_sisa_hari_publikasi;
        ImageView img_mark, img_sisa_hari;
        ProgressBar progress;
        ConstraintLayout cons;

        CardViewViewHolder(View itemView) {
            super(itemView);
            progress = itemView.findViewById(R.id.progress_adp_pendanaan);
            txt_loan_type = itemView.findViewById(R.id.txt_loan_type_adp_pendanaan);
            txt_loan_rating = itemView.findViewById(R.id.txt_loan_rating_adp_pendanaan);
            txt_loan_no = itemView.findViewById(R.id.txt_loan_no_adp_pendanaan);
            txt_sisa_hari_publikasi = itemView.findViewById(R.id.txt_sisa_hari_adp_pendanaan);
            txt_tenor = itemView.findViewById(R.id.txt_tenor_adp_pendanaan);
            txt_interest = itemView.findViewById(R.id.txt_bunga_adp_pendanaan);
            txt_nominal = itemView.findViewById(R.id.txt_pinjaman_adp_pendanaan);
            txt_funding_percent = itemView.findViewById(R.id.txt_terkumpul_percent_adp_pendanaan);
            img_mark = itemView.findViewById(R.id.img_mark_adp_pendanaan);
            img_sisa_hari = itemView.findViewById(R.id.img_sisa_hari_adp_pendanaan);
            cons = itemView.findViewById(R.id.cons_adp_pendanaan);
        }
    }
}
