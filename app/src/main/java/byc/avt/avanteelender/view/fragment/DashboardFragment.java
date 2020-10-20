package byc.avt.avanteelender.view.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.navigation.NavigationView;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.viewmodel.DashboardViewModel;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    Toolbar toolbar;
    Fungsi f = new Fungsi(getActivity());
    private PrefManager prefManager;
    private NavigationView navigationView;
    String x = "https://www.tasteofhome.com/wp-content/uploads/2018/01/Scrum-Delicious-Burgers_EXPS_CHMZ19_824_B10_30_2b-1-696x696.jpg";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        prefManager = new PrefManager(getActivity());
        toolbar = view.findViewById(R.id.toolbar_fr_dashboard);
        toolbar.setTitle(prefManager.getName());
        toolbar.setSubtitle("CLI-USER_ID");
        toolbar.setNavigationIcon(R.drawable.ic_iconuser);
        Glide.with(this).load(prefManager.getAvatar())
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Drawable> transition) {
                        Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();
                        //Bitmap emptyBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
                        if (resource.getConstantState() == null) {
                            toolbar.setNavigationIcon(R.drawable.ic_iconuser);
                        }else{
                            Drawable newdrawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(f.getCroppedBitmap(bitmap), 136, 136, true));
                            toolbar.setNavigationIcon(newdrawable);
                        }

                    }
                });


        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);


        dashboardViewModel.getText().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.dashboard_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.account_setting){
//            Intent intent = new Intent(getActivity(), History.class);
//            startActivity(intent);
//            getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}