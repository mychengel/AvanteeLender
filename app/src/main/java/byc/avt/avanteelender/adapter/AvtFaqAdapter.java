package byc.avt.avanteelender.adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.model.AvtFaq;
import byc.avt.avanteelender.view.others.TermsActivity;

public class AvtFaqAdapter extends RecyclerView.Adapter<AvtFaqAdapter.CardViewViewHolder>{

    AlphaAnimation klik = new AlphaAnimation(1F, 0.6F);
    Fungsi f = new Fungsi();
    private Context context;
    boolean is_expand = false;
    private ArrayList<AvtFaq> listAvtFaq;
    private ArrayList<AvtFaq> getListAvtFaq() {
        return listAvtFaq;
    }
    public void setListAvtFaq(ArrayList<AvtFaq> listAvtFaq) {
        this.listAvtFaq = listAvtFaq;
    }
    public AvtFaqAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public AvtFaqAdapter.CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_avt_faq, parent, false);
        return new AvtFaqAdapter.CardViewViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final AvtFaqAdapter.CardViewViewHolder holder, int position) {
        final AvtFaq faq = getListAvtFaq().get(position);

        holder.wv.setNestedScrollingEnabled(true);
        holder.wv.setVerticalScrollBarEnabled(true);
        holder.wv.requestFocus();
        holder.wv.getSettings().setDomStorageEnabled(true);
        holder.wv.getSettings().setDefaultTextEncodingName("utf-8");
        holder.wv.getSettings().setJavaScriptEnabled(true);
        holder.wv.setWebViewClient(new AvtFaqAdapter.MyWebViewClient());
        holder.wv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        holder.wv.loadDataWithBaseURL(null, faq.getContent(), "text/HTML", "UTF-8", null);

        holder.lbl_title.setText(faq.getTitle());
        holder.img_expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!is_expand){
                    holder.img_expand.setRotation(180);
                    holder.wv.setVisibility(View.VISIBLE);
                    is_expand = !is_expand;
                }else{
                    holder.img_expand.setRotation(0);
                    holder.wv.setVisibility(View.GONE);
                    is_expand = !is_expand;
                }
            }
        });

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!is_expand){
                    holder.img_expand.setRotation(180);
                    holder.wv.setVisibility(View.VISIBLE);
                    is_expand = !is_expand;
                }else{
                    holder.img_expand.setRotation(0);
                    holder.wv.setVisibility(View.GONE);
                    is_expand = !is_expand;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return getListAvtFaq().size();
    }

    class CardViewViewHolder extends RecyclerView.ViewHolder{
        TextView lbl_title, lbl_content;
        ImageView img_expand;
        WebView wv;
        CardView cv;

        CardViewViewHolder(View itemView) {
            super(itemView);
            lbl_title = itemView.findViewById(R.id.lbl_title_adp_faq);
            lbl_content = itemView.findViewById(R.id.lbl_content_adp_faq);
            lbl_content.setVisibility(View.GONE);
            img_expand = itemView.findViewById(R.id.img_expand_adp_faq);
            wv = itemView.findViewById(R.id.wv_adp_faq);
            wv.setVisibility(View.GONE);
            cv = itemView.findViewById(R.id.cv_adp_faq);
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            //dialog.show();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url); // load the url
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            //dialog.cancel();
        }
    }

}
