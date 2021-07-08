package byc.avt.avanteelender.intro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.helper.Routes;
import byc.avt.avanteelender.view.auth.RegistrationActivity;
import byc.avt.avanteelender.view.MainActivity;
import byc.avt.avanteelender.view.auth.LoginActivity;
import byc.avt.avanteelender.view.auth.RegistrationFormActivity;

public class WalkthroughActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnSkip, btnNext, btnDaftar, btnMasuk;
    private PrefManager prefManager;
    private SharedPreferences userPref;
    private TextView txt_link_daftar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Making notification bar transparent
//        if (Build.VERSION.SDK_INT >= 21) {
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        }

        setContentView(R.layout.activity_walkthrough);

        viewPager = findViewById(R.id.view_pager_walkthrough);
        dotsLayout = findViewById(R.id.layout_dots_walkthrough);
        btnSkip = findViewById(R.id.btn_skip_walkthrough);
        //btnNext = findViewById(R.id.btn_next_walkthrough);


        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.screen_walkthrough_1,
                R.layout.screen_walkthrough_2,
                R.layout.screen_walkthrough_3,
                R.layout.screen_walkthrough_4,
                R.layout.screen_walkthrough_5};

        // adding bottom dots
        addBottomDots(0);

        // making notification bar transparent
        //changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        // Checking for first time launch - before calling setContentView()
        //userPref = getSharedPreferences("user", MODE_PRIVATE);
        prefManager = PrefManager.getInstance(WalkthroughActivity.this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            //finish();
        }

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(layouts.length);
            }
        });

        requestMultiPermission();
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestMultiPermission();
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void launchHomeScreen() {
        if(prefManager.getUid().equalsIgnoreCase("-")){
            viewPager.setCurrentItem(layouts.length);
        }else{
            new Fungsi(WalkthroughActivity.this).showMessage("Selamat datang kembali "+prefManager.getName());
            Intent intent = new Intent(WalkthroughActivity.this, MainActivity.class);
            intent.putExtra("dest", "1");
            new Routes(WalkthroughActivity.this).moveInFinish(intent);
        }

    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                btnSkip.setVisibility(View.GONE);
            } else {
                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            if(position==4){
                txt_link_daftar = view.findViewById(R.id.txt_link_daftar);
                txt_link_daftar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.startAnimation(new Fungsi().clickAnim());
                        prefManager.setFirstTimeLaunch(false);
                        //new Fungsi(WalkthroughActivity.this).showMessage("In progress ~ byc");
                        Intent intent = new Intent(WalkthroughActivity.this, RegistrationActivity.class); //yg bener
                        new Routes(WalkthroughActivity.this).moveIn(intent);
                    }
                });

                btnMasuk = view.findViewById(R.id.btn_masuk_wt5);
                btnMasuk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        prefManager.setFirstTimeLaunch(false);
                        Intent intent = new Intent(WalkthroughActivity.this, LoginActivity.class);
                        new Routes(WalkthroughActivity.this).moveIn(intent);
                    }
                });
            }
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    private void requestMultiPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Dexter.withContext(WalkthroughActivity.this)
                    .withPermissions(
                            Manifest.permission.RECEIVE_SMS,
                            Manifest.permission.READ_SMS,
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                            Manifest.permission.MANAGE_DOCUMENTS,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                        }
                    }).check();
        }else{
            Dexter.withContext(WalkthroughActivity.this)
                    .withPermissions(
                            Manifest.permission.RECEIVE_SMS,
                            Manifest.permission.READ_SMS,
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                        }
                    }).check();
        }
    }

    private void requestNotificationPermission() {
    }

    private void requestLocationPermission() {
    }

    private void requestPhoneCallPermission() {
    }

    public boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onResume() {
        super.onResume();
        this.doubleBackToExitPressedOnce = false;
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(a);
            finishAffinity();
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(WalkthroughActivity.this, "Tekan lagi untuk keluar aplikasi", Toast.LENGTH_SHORT).show();
    }
}
