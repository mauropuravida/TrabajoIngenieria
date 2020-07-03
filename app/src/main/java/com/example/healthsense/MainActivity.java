package com.example.healthsense;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.healthsense.data.PikerDate;
import com.example.healthsense.db.AppDatabase;
import com.example.healthsense.db.Repository.DeviceUsersRepository;
import com.example.healthsense.db.Repository.DocumentTypeRepository;
import com.example.healthsense.db.Repository.UserRepository;
import com.example.healthsense.db.entity.DeviceUsers;
import com.example.healthsense.db.entity.DocumentType;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    //nombre de archivo de preferencias
    public static final String PREFS_FILENAME = "data.prefs";
    public static boolean FIRST_TRAINING = true;
    public static String email;
    public static String user;
    public static String TOKEN = "";
    public static String PROFILETYPE = "";
    public static String PATH = "https://healthsenseapi.herokuapp.com/";

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferencesEditor = getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE);

        setContentView((PROFILETYPE.equals("m")) ? R.layout.activity_main_medical : R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_profile, R.id.nav_my_trainings,
                R.id.nav_training_history, R.id.nav_my_suscribers, R.id.nav_my_suscriptions, R.id.nav_payment_methods,
                R.id.nav_device, R.id.nav_change_pass, R.id.nav_close_session)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        email = getIntent().getExtras().getString("user");
        user = preferencesEditor.getString(email, "");
        System.out.println(email);
        String msg = new StringBuilder().append(getString(R.string.welcome)).append(" ").append(user).toString();
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();

        //  DeviceUsersRepository deviceUsers = new DeviceUsersRepository( getActivity().getApplication());
        //  deviceUsers.deleteAll();;
        //   UserRepository userRepository = new UserRepository(getActivity().getApplication());
        //   userRepository.deleteAll();
        //-----------------------------
     //   AppDatabase appDatabase = AppDatabase.getAppDatabase(getBaseContext());
       // new ElimnarTodasLasTablasYDestroy(appDatabase).execute();
        //testing data on local db
        //  new TaskInsertWorkouts().execute();

    }

    private static class ElimnarTodasLasTablasYDestroy extends AsyncTask<Void, Void, Void> {
        private AppDatabase db;
        private ElimnarTodasLasTablasYDestroy(AppDatabase db) {
            this.db = db;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            System.out.println("VORRO");
            db.clearAllTables();
            db.destroyInstance();
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        ((TextView)findViewById(R.id.nameMenu)).setText(user);
        ((TextView)findViewById(R.id.emailMenu)).setText(email);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
/*
    //test inserts history
    private class TaskInsertWorkouts extends AsyncTask<Void, Void, Void> {

        private static final String TAG = "TaskInsertWorkouts";

        List<Workout> wk = new ArrayList<>();
        List<WorkoutReport> wkr = new ArrayList<>();

        public TaskInsertWorkouts() {
        }

        @Override
        protected Void doInBackground(Void... voids) {

            AppDatabase appDatabase = AppDatabase.getAppDatabase(getBaseContext());

            appDatabase.workoutDAO().deleteAll();
            appDatabase.workoutReportDAO().deleteAll();

            wk.add(new Workout(4, "Workout 4", "2020-04-11", 5, 0, 1));
            wk.add(new Workout(5, "Workout 5", "2020-04-11", 2, 0, 1));
            wk.add(new Workout(6, "Workout 6", "2020-04-11", 4, 0, 1));

            wkr.add(new WorkoutReport(4, 3, "2020-05-29"));
            wkr.add(new WorkoutReport(5, 5, "2020-04-28"));
            wkr.add(new WorkoutReport(6, 4, "2020-06-02"));
            wkr.add(new WorkoutReport(7, 6, "2020-06-12"));

            appDatabase.workoutDAO().insertAll(wk);
            appDatabase.workoutReportDAO().insertAll(wkr);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(TAG, "doInBackground: data added");
            //Toast.makeText(getBaseContext(), "datos cargados", Toast.LENGTH_LONG).show();
        }
    }
*/
}
