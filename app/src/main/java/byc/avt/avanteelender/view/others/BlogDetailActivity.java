package byc.avt.avanteelender.view.others;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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
import java.util.Objects;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.model.Blog;

public class BlogDetailActivity extends AppCompatActivity {

    TextView txt_category, txt_author_and_date, txt_title, txt_content;
    ImageView img;
    Blog b;
    Toolbar bar;
    Fungsi f = new Fungsi(BlogDetailActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_detail);

        bar = findViewById(R.id.toolbar_blog_detail);
        setSupportActionBar(bar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_back_24px);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");

        img = findViewById(R.id.img_blog_detail);
        txt_category = findViewById(R.id.txt_category_blog_detail);
        txt_author_and_date = findViewById(R.id.txt_author_and_date_blog_detail);
        txt_title = findViewById(R.id.txt_title_blog_detail);
        txt_content = findViewById(R.id.txt_content_blog_detail);

        Intent i = getIntent();
        b = i.getParcelableExtra("blog");
        txt_category.setText(b.getCategory());
        txt_author_and_date.setText(b.getCreated_by()+" - "+f.tglFull(b.getCreated_date()));
        txt_title.setText(b.getTitle());
        txt_content.setText(f.htmlToStr(b.getText()));

//        Glide.with(BlogDetailActivity.this).load(b.getImg())
//                .apply(RequestOptions.skipMemoryCacheOf(true))
//                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
//                .placeholder(R.drawable.ic_baseline_no_photography_24)
//                .error(R.drawable.ic_baseline_no_photography_24)
//                .dontAnimate()
//                .into(img);

        Glide.with(BlogDetailActivity.this).load(b.getImg())
                .placeholder(R.drawable.ic_baseline_no_photography_24)
                .error(R.drawable.ic_baseline_no_photography_24)
                .dontAnimate()
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        img.setScaleType(ImageView.ScaleType.FIT_XY);
                        return false;
                    }
                })
                .into(img);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }
}