package com.example.healthsense;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.healthsense.db.AppDatabase;
import com.example.healthsense.db.entity.Workout;
import com.example.healthsense.db.entity.WorkoutDone;
import com.example.healthsense.db.entity.WorkoutReport;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    //nombre de archivo de preferencias
    public static final String PREFS_FILENAME = "data.prefs";
    public static String TOKEN = "";

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_profile, R.id.nav_my_trainings,
                R.id.nav_training_history, R.id.nav_device, R.id.nav_close_session)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);



        //-----------------------------

        //testing data on local db
        new TaskInsertWorkouts().execute();

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


    //test inserts history
    private class TaskInsertWorkouts extends AsyncTask<Void, Void, Void> {

        List<Workout> wk = new ArrayList<>();
        List<WorkoutReport> wkr = new ArrayList<>();

        public TaskInsertWorkouts(){
        }

        @Override
        protected Void doInBackground(Void... voids) {

            wk.add(new Workout(1, "Workout 1", "2020-05-11", 2, 0, 1, 0));
            wk.add(new Workout(2, "Workout 2", "2020-05-11", 1, 0, 1, 0));
            wk.add(new Workout(3, "Workout 3", "2020-05-11", 4, 0, 1, 0));
            wk.add(new Workout(4, "Workout 4", "2020-05-11", 2, 0, 0, 0));

            wkr.add(new WorkoutReport(1, 1, "2020-05-11"));
            wkr.add(new WorkoutReport(2, 2, "2020-05-15"));
            wkr.add(new WorkoutReport(3, 1, "2020-05-21"));
            wkr.add(new WorkoutReport(4, 3, "2020-05-29"));

            AppDatabase appDatabase = AppDatabase.getAppDatabase(getBaseContext());

            Log.d("asynctask", "doInBackground: ");

            appDatabase.workoutDAO().insertAll(wk);
            appDatabase.workoutReportDAO().insertAll(wkr);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getBaseContext(), "datos cargados", Toast.LENGTH_LONG);
        }
    }

}
