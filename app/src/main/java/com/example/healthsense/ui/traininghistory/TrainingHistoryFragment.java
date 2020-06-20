package com.example.healthsense.ui.traininghistory;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthsense.MainActivity;
import com.example.healthsense.R;
import com.example.healthsense.Resquest.OkHttpRequest;
import com.example.healthsense.Resquest.doAsync;
import com.example.healthsense.db.AppDatabase;
import com.example.healthsense.db.AppDatabaseListener;
import com.example.healthsense.db.entity.old.Workout;
import com.example.healthsense.db.entity.WorkoutDone;
import com.example.healthsense.db.entity.old.WorkoutReport;
import com.example.healthsense.ui.home.HomeFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class TrainingHistoryFragment extends Fragment implements AppDatabaseListener {

    private static final String TAG = "TrainingHistoryFragment";

    private static final String URL_BASE = "https://healthsenseapi.herokuapp.com/";
    private JSONArray token;

    private List<JSONObject> workouts;
    private List<JSONObject> reports = new ArrayList<>();

    private TrainingHistoryAdapter adapter;

    private TextView workoutsCompleted;
    private RecyclerView recyclerView;
    public static Fragment fg;
    private TextView emptyView;


    private ProgressDialog mProgressDialog;
    private boolean first = true;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_training_history, container, false);


        fg = this;
        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new HomeFragment()).addToBackStack(null).commit();
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        mProgressDialog = new ProgressDialog(root.getContext(),R.style.AppCompatAlertDialogStyle);
        mProgressDialog.setTitle(R.string.loading);
        mProgressDialog.setMessage(getResources().getString(R.string.please_wait));

        workoutsCompleted = root.findViewById(R.id.workouts_completed);
        emptyView = root.findViewById(R.id.empty_view);

        adapter = new TrainingHistoryAdapter();

        recyclerView = root.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        FloatingActionButton reloadButton = root.findViewById(R.id.reload_button);
        reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (networkInfo != null && networkInfo.isConnected() && !first) {
                    Log.d(TAG, "onCreateView: Hay conexion");
                    mProgressDialog.show();
                    getData();
                }
            }
        });

        //todo guardar dataset para cuando se vuelve al fragmento asi no se hacen gets cada vez que se ingresa

        if (first)
            if (networkInfo != null && networkInfo.isConnected()) {
                Log.d(TAG, "onCreateView: Hay conexion");
                mProgressDialog.show();
                getData();
            } else {
                Log.d(TAG, "onCreateView: No hay conexion a internet");
            }

        return root;
    }

    @Override
    public void setWorkoutReportsFromDB(List<WorkoutDone> workouts) {
        mProgressDialog.dismiss();
        first = false;
        if (workouts.isEmpty()) {
            workoutsCompleted.setText("0");
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            adapter.setDataset(workouts);
            workoutsCompleted.setText(String.valueOf(workouts.size()));
        }
    }


    private Runnable getWorkouts() {
        return new Runnable() {
            @Override
            public void run() {
                String path = "workoutReport/";
                getBackendResponse(path, reports, getReports());

                for (JSONObject workout : workouts) {
                    try {

                        Workout w = new Workout(workout.getInt("id"), workout.getString("name"), workout.getString("creation_date").substring(0, 9),
                                workout.getInt("difficulty"), workout.getDouble("price"), workout.getInt("done"));
                        AppDatabase.getAppDatabase(getContext()).workoutDAO().insert(w);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

    }

    private Runnable getReports() {
        return new Runnable() {
            @Override
            public void run() {
                for (JSONObject report : reports) {
                    try {
                        WorkoutReport wr = new WorkoutReport(report.getInt("id"), report.getInt("workout_id"),
                                report.getString("execution_date").substring(0, 9));
                        AppDatabase.getAppDatabase(getContext()).workoutReportDAO().insert(wr);

                        Log.d(TAG, "run: added report " + wr.getId_wkr() + " - " + wr.getWorkout_id());

                        Workout w = AppDatabase.getAppDatabase(getContext()).workoutDAO().getWorkout(report.getInt("workout_id"));
                        w.setDone(1);
                        //w.setRating(report.getInt("rating"));
                        AppDatabase.getAppDatabase(getContext()).workoutDAO().update(w);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    new TaskGetWorkoutReports(TrainingHistoryFragment.this).execute();
                }
            }
        };
    }


    private void getData() {
        doAsync.execute(
                new Runnable() {
                    @Override
                    public void run() {

                        MainActivity.TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6InRlc3R1c2VyMTBAZ21haWwuY29tIiwiaWF0IjoxNTkyMDIwNjAyfQ.thkUtcbXqN__A327UA-NL8gwpoI5IDYaiT1SjRsesUc";

                        JSONObject ob = new JSONObject();
                        try {
                            ob.put("x-access-token", MainActivity.TOKEN);
                            token = new JSONArray();
                            token.put(ob);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        workouts = new ArrayList<>();
                        reports = new ArrayList<>();

                        String path = "workout/device_user_id&0"; // obtengo workouts del usuario ingresado
                        getBackendResponse(path, workouts, getWorkouts());

                    }
                }
        );

    }


    private void getBackendResponse(String path, List<JSONObject> listObj, Runnable func) {
        OkHttpRequest request = new OkHttpRequest(new OkHttpClient());
        String url = URL_BASE + path;

        request.GET(url, token, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mProgressDialog.dismiss();
                Toast.makeText(getActivity(),
                        "Failed to obtain information from " + url, Toast.LENGTH_LONG)
                        .show();
            }

            @Override
            public void onResponse(Call call, Response response) {
                String responseData = null;
                try {
                    if (response.body() != null) {
                        responseData = response.body().string();
                        Object jsonType = new JSONTokener(responseData).nextValue();

                        if (jsonType instanceof JSONArray) {
                            JSONArray json = new JSONArray(responseData);
                            for (int i = 0; i < json.length(); i++) {
                                listObj.add(json.getJSONObject(i));
                            }
                            func.run();
                        }
                    }

                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @SuppressLint("StaticFieldLeak")
    private class TaskGetWorkoutReports extends AsyncTask<Void, Void, List<WorkoutDone>> {

        AppDatabaseListener listener;
        private static final String TAG = "TaskGetWorkoutReports";

        public TaskGetWorkoutReports(AppDatabaseListener listener) {
            this.listener = listener;
        }

        @Override
        protected List<WorkoutDone> doInBackground(Void... voids) {
            return AppDatabase.getAppDatabase(getContext()).workoutDoneDAO().getAllWorkoutsDone();
        }

        @Override
        protected void onPostExecute(List<WorkoutDone> workoutsDone) {
            super.onPostExecute(workoutsDone);
            listener.setWorkoutReportsFromDB(workoutsDone);
            Log.d(TAG, "onPostExecute: workouts: " + workoutsDone.size());
        }
    }

}
