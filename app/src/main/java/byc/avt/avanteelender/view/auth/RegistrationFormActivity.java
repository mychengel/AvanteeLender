package byc.avt.avanteelender.view.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItem;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.Objects;

import byc.avt.avanteelender.R;
import byc.avt.avanteelender.helper.Fungsi;
import byc.avt.avanteelender.helper.GlobalVariables;
import byc.avt.avanteelender.helper.PrefManager;
import byc.avt.avanteelender.intro.WalkthroughActivity;
import byc.avt.avanteelender.view.misc.OTPActivity;
import byc.avt.avanteelender.view.others.SettingActivity;
import byc.avt.avanteelender.view.sheet.TermSheetFragment;
import byc.avt.avanteelender.viewmodel.AuthenticationViewModel;

public class RegistrationFormActivity extends AppCompatActivity {

    NavController navController;
    AppBarLayout appBar;
    CoordinatorLayout coor;
    CollapsingToolbarLayout collapsing;
    Toolbar toolbar;
    AppBarConfiguration appBarConfiguration;
    NavHostFragment navHostFragment;
    Dialog dialog;
    LinearLayout linStep, step1, step2, step3, step4, step5;
    AppBarLayout.LayoutParams params;
    private AuthenticationViewModel viewModel;
    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_form);
        appBar = findViewById(R.id.appbar_regis_form);
        collapsing = findViewById(R.id.collapsing_regis_form);
        params = (AppBarLayout.LayoutParams) collapsing.getLayoutParams();
        toolbar = findViewById(R.id.tb_regis_form);
        dialog = GlobalVariables.loadingDialog(this);
        linStep = findViewById(R.id.lin_step_regis_form);
        linStep.setVisibility(View.GONE);
        step1 = findViewById(R.id.step1);
        step2 = findViewById(R.id.step2);
        step3 = findViewById(R.id.step3);
        step4 = findViewById(R.id.step4);
        step5 = findViewById(R.id.step5);
        viewModel = new ViewModelProvider(this).get(AuthenticationViewModel.class);
        prefManager = PrefManager.getInstance(RegistrationFormActivity.this);

        navController = Navigation.findNavController(this, R.id.regis_form_fragment_container);
        NavigationUI.setupWithNavController(collapsing, toolbar, navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull final NavDestination destination, @Nullable Bundle arguments) {
                switch (destination.getId()) {
                    case R.id.welcomeFragment:
                        linStep.setVisibility(View.GONE);
                        dialog.show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                collapsing.setVisibility(View.GONE);
                                params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_NO_SCROLL
                                        | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.cancel();
                                    }
                                }, 300);
                            }
                        }, 100);
                        break;

                    case R.id.lenderTypeFragment:
                        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                                | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
                        collapsing.setVisibility(View.VISIBLE);
                        linStep.setVisibility(View.VISIBLE);
                        setStep(0,0,0,0,0);
                        break;

                    case R.id.personalDataFragment:
                        TermSheetFragment.read = false;
                        linStep.setVisibility(View.VISIBLE);
                        setStep(1,0,0,0,0);
                        break;
                    case R.id.companyDataFragment:
                        TermSheetFragment.read = false;
                        linStep.setVisibility(View.VISIBLE);
                        setStep(1,0,0,0,0);
                        break;

                    case R.id.workInfoFragment:
                        linStep.setVisibility(View.VISIBLE);
                        setStep(2,1,0,0,0);
                        break;
                    case R.id.narahubungFragment:
                        linStep.setVisibility(View.VISIBLE);
                        setStep(2,1,0,0,0);
                        break;

                    case R.id.addressDataFragment:
                        linStep.setVisibility(View.VISIBLE);
                        setStep(2,2,1,0,0);
                        break;
                    case R.id.companyAddressFragment:
                        linStep.setVisibility(View.VISIBLE);
                        setStep(2,2,1,0,0);
                        break;

                    case R.id.bankInfoFragment:
                        linStep.setVisibility(View.VISIBLE);
                        setStep(2,2,2,1,0);
                        break;
                    case R.id.companyBankFragment:
                        linStep.setVisibility(View.VISIBLE);
                        setStep(2,2,2,1,0);
                        break;

                    case R.id.documentsFragment:
                        linStep.setVisibility(View.VISIBLE);
                        setStep(2,2,2,2,1);
                        break;
                    case R.id.companyDocumentsFragment:
                        linStep.setVisibility(View.VISIBLE);
                        setStep(2,2,2,2,1);
                        break;
                }

            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        return navController.navigateUp()|| super.onSupportNavigateUp();

    }

    @Override
    public void onBackPressed() {
        if(navController.getCurrentDestination().getId() == R.id.welcomeFragment){
            logoutConfirmation();
        }
    }

    private void logoutConfirmation(){
        new AlertDialog.Builder(RegistrationFormActivity.this)
                .setTitle("Konfirmasi")
                .setIcon(R.drawable.logo)
                .setMessage("Apakah anda yakin mau keluar dari pengisian data akun?")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        logout();
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogs, int which) {
                        dialogs.cancel();
                    }
                })
                .create()
                .show();
    }

    public void logout() {
        // LOGOUT: GET method to server through endpoint
        dialog.show();
        viewModel.logout(prefManager.getUid(), prefManager.getToken());
        viewModel.getLogoutResult().observe(RegistrationFormActivity.this, checkLogout);
    }

    private Observer<String> checkLogout = new Observer<String>() {
        @Override
        public void onChanged(String result) {
            if(result.equals("ok")) {
                dialog.cancel();
                Intent intent = new Intent(RegistrationFormActivity.this, WalkthroughActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                new Fungsi(RegistrationFormActivity.this).showMessage("Anda keluar dari pengisian data akun.");
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                finish();
            }else{
                dialog.cancel();
                new Fungsi().showMessage(result);
            }
        }
    };

    @SuppressLint("ResourceAsColor")
    public void setStep(int c1, int c2, int c3, int c4, int c5){
        if(c1 == 0){step1.setBackgroundColor(getResources().getColor(R.color.neutral));}
        else if(c1 == 1){step1.setBackgroundColor(getResources().getColor(R.color.pending));}
        else if(c1 == 2){step1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));}
        if(c2 == 0){step2.setBackgroundColor(getResources().getColor(R.color.neutral));}
        else if(c2 == 1){step2.setBackgroundColor(getResources().getColor(R.color.pending));}
        else if(c2 == 2){step2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));}
        if(c3 == 0){step3.setBackgroundColor(getResources().getColor(R.color.neutral));}
        else if(c3 == 1){step3.setBackgroundColor(getResources().getColor(R.color.pending));}
        else if(c3 == 2){step3.setBackgroundColor(getResources().getColor(R.color.colorPrimary));}
        if(c4 == 0){step4.setBackgroundColor(getResources().getColor(R.color.neutral));}
        else if(c4 == 1){step4.setBackgroundColor(getResources().getColor(R.color.pending));}
        else if(c4 == 2){step4.setBackgroundColor(getResources().getColor(R.color.colorPrimary));}
        if(c5 == 0){step5.setBackgroundColor(getResources().getColor(R.color.neutral));}
        else if(c5 == 1){step5.setBackgroundColor(getResources().getColor(R.color.pending));}
        else if(c5 == 2){step5.setBackgroundColor(getResources().getColor(R.color.colorPrimary));}
    }
}
