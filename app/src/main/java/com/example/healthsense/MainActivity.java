package com.example.healthsense;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    //nombre de archivo de preferencias
    public static final String PREFS_FILENAME = "data.prefs";
    public static String email;
    public static String TOKEN = "";

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferencesEditor = getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE);
        int layoutProfile = preferencesEditor.getInt(getIntent().getExtras().getString("user")+"profile", R.layout.fragment_profile_medical);

        setContentView((layoutProfile == R.layout.fragment_profile_medical) ? R.layout.activity_main_medical : R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_profile, R.id.nav_my_trainings,
                R.id.nav_training_history, R.id.nav_my_suscribers, R.id.nav_my_suscriptions, R.id.nav_payment_methods,
                R.id.nav_device,R.id.nav_change_pass, R.id.nav_close_session)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        email = getIntent().getExtras().getString("user");
        String user = preferencesEditor.getString(email, "");
        String msg = new StringBuilder().append(getString(R.string.welcome)).append(" ").append(user).toString();
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    @Override
    public void onBackPressed() {

        if(!isTaskRoot()){
            super.onBackPressed();
        }
    }

}
