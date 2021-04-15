package byc.avt.avanteelender.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.model.Blog;
import byc.avt.avanteelender.view.features.account.DataPendukungShowActivity;
import byc.avt.avanteelender.view.features.account.SettingAccountActivity;
import byc.avt.avanteelender.view.fragment.tabportofoliofragment.PortofolioAktifDetailActivity;
import byc.avt.avanteelender.view.others.BlogDetailActivity;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.CardViewViewHolder>{

    AlphaAnimation klik = new AlphaAnimation(1F, 0.6F);
    Fungsi f = new Fungsi();
    private Context context;
    boolean is_expand = false;
    private ArrayList<Blog> listBlog;
    private ArrayList<Blog> getListBlog() {
        return listBlog;
    }
    public void setListBlog(ArrayList<Blog> listBlog) {
        this.listBlog = listBlog;
    }
    public BlogAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public BlogAdapter.CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_blog, parent, false);
        return new BlogAdapter.CardViewViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final BlogAdapter.CardViewViewHolder holder, int position) {
        final Blog b = getListBlog().get(position);
        holder.lbl_title.setText(b.getTitle());
        holder.lbl_by.setText("Ditulis oleh: "+b.getCreated_by());
        holder.lbl_date.setText(f.tglFull(b.getCreated_date()));
//        Glide.with(context).load(b.getImg())
//                .apply(RequestOptions.skipMemoryCacheOf(true))
//                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
//                .placeholder(R.color.colorPrimary)
//                .error(R.drawable.ic_baseline_no_photography_24)
//                .dontAnimate()
//                .into(holder.img);
        Glide.with(context).load(b.getImg())
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Drawable> transition) {
                        Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();
                        //Bitmap emptyBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
                        if (resource.getConstantState() == null) {
                            holder.img.setImageResource(R.drawable.ic_baseline_no_photography_24);
                        }else{
                            Drawable newdrawable = new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(f.getCroppedBitmapSquare(bitmap), 272, 248, true));
                            holder.img.setImageDrawable(newdrawable);
                        }
                    }
                });

//        Glide.with(context).load(b.getImg())
//                .placeholder(R.drawable.ic_baseline_no_photography_24)
//                .error(R.drawable.ic_baseline_no_photography_24)
//                .dontAnimate()
//                .apply(RequestOptions.skipMemoryCacheOf(true))
//                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
//                .listener(new RequestListener<Drawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        return false;
//                    }
//                    @Override
//                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();
//                        Drawable newdrawable = new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(f.getCroppedBitmapSquare(bitmap), 272, 248, true));
//                        holder.img.setImageDrawable(newdrawable);
//                        return false;
//                    }
//                })
//                .into(holder.img);

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, BlogDetailActivity.class);
                i.putExtra("blog", b);
                context.startActivity(i);
                ((AppCompatActivity)context).overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getListBlog().size();
    }

    class CardViewViewHolder extends RecyclerView.ViewHolder{
        TextView lbl_title, lbl_date, lbl_by;
        ImageView img;
        CardView cv;

        CardViewViewHolder(View itemView) {
            super(itemView);
            lbl_title = itemView.findViewById(R.id.txt_title_adp_blog);
            lbl_date = itemView.findViewById(R.id.txt_date_adp_blog);
            lbl_by = itemView.findViewById(R.id.txt_by_adp_blog);
            img = itemView.findViewById(R.id.img_adp_blog);
            cv = itemView.findViewById(R.id.cv_adp_blog);
        }
    }
}
