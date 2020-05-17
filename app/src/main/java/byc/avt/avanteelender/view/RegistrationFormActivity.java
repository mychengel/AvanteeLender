package byc.avt.avanteelender.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;

import byc.avt.avanteelender.R;

public class RegistrationFormActivity extends AppCompatActivity {

    private NavController navController;
    AppBarLayout appBarLayout;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_form);
        appBarLayout = findViewById(R.id.appbar_regis_form);
        toolbar = findViewById(R.id.tb_regis_form);
        setSupportActionBar(toolbar);

        navController = Navigation.findNavController(this, R.id.regis_form_fragment_container);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                Toast.makeText(RegistrationFormActivity.this, destination.getLabel(), Toast.LENGTH_SHORT).show();
                switch (destination.getLabel().toString().trim()){
                    case "fragment_welcome" :
                        getSupportActionBar().hide();
                        appBarLayout.setVisibility(View.GONE);
                        break;
                    case "fragment_lender_type" :
                        appBarLayout.setVisibility(View.VISIBLE);
                        getSupportActionBar().show();
                        getSupportActionBar().setTitle(R.string.lender_type);
                        break;
                    case "fragment_personal_data" :
                        appBarLayout.setVisibility(View.VISIBLE);
                        getSupportActionBar().show();
                        getSupportActionBar().setTitle(R.string.personal_data);
                        break;
                    case "fragment_document" :
                        appBarLayout.setVisibility(View.VISIBLE);
                        getSupportActionBar().show();
                        getSupportActionBar().setTitle(R.string.essential_document);
                        break;

                }
//                if (R.layout.fragment_personal_data.equals(destination.getLabel())) {
//                    appBarLayout.setVisibility(View.VISIBLE);
//                    toolbar.setTitle(R.string.personal_data);
//                }
            }
        });
        setSupportActionBar(toolbar);
        NavigationUI.setupWithNavController(toolbar, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, (DrawerLayout) null);
    }
}
