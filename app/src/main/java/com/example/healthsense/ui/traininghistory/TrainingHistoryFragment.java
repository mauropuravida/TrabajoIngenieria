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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthsense.MainActivity;
import com.example.healthsense.R;
import com.example.healthsense.Resquest.OkHttpRequest;
import com.example.healthsense.Resquest.doAsync;
import com.example.healthsense.data.PikerDate;
import com.example.healthsense.db.AppDatabase;
import com.example.healthsense.db.AppDatabaseListener;
import com.example.healthsense.db.Repository.DeviceUsersRepository;
import com.example.healthsense.db.Repository.UserRepository;
import com.example.healthsense.db.Repository.WorkoutsReportsRepository;
import com.example.healthsense.db.Repository.WorkoutsRepository;
import com.example.healthsense.db.entity.Workouts;
import com.example.healthsense.db.entity.WorkoutDone;
import com.example.healthsense.db.entity.WorkoutReports;
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

    private JSONArray token;

    private List<JSONObject> workouts;
    private List<JSONObject> reports;

    private TrainingHistoryAdapter adapter;

    private TextView workoutsCompleted;
    private RecyclerView recyclerView;
    public static Fragment fg;
    private TextView emptyView;

    private boolean first = true;

    private ProgressDialog mProgressDialog;


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

        mProgressDialog = new ProgressDialog(root.getContext(), R.style.AppCompatAlertDialogStyle);
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
        reloadButton.setScaleType(ImageView.ScaleType.CENTER);
        reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (networkInfo != null && networkInfo.isConnected()) {
                    mProgressDialog.show();
                    getData();
                }
            }
        });

        if (first) {
            if (networkInfo != null && networkInfo.isConnected()) {
                Log.d(TAG, "onCreateView: Hay conexion");
                mProgressDialog.show();
                getData();
            } else {
                Log.d(TAG, "onCreateView: No hay conexion a internet");
            }
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

                DeviceUsersRepository deviceUsersRepository = new DeviceUsersRepository(getActivity().getApplication());
                UserRepository userRepository = new UserRepository(getActivity().getApplication());
                int device_user_id_room = deviceUsersRepository.getDeviceUserId(userRepository.getId(MainActivity.email));

                WorkoutsRepository workoutsRepository = new WorkoutsRepository(getActivity().getApplication());

                for (JSONObject workout : workouts) {
                    try {

                        int workout_id = workout.getInt("id");
                        if (!workoutsRepository.contains(workout_id)) {
                            int rating = 0;
                            try {
                                rating = workout.getInt("rating");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Workouts workout_to_insert = new Workouts(null, device_user_id_room, workout.getString("name"),
                                    workout.getString("creation_date"), workout.getInt("difficulty"), workout.getDouble("price"),
                                    workout.getInt("done"), rating);
                            workout_to_insert.setId_backend(workout_id);
                            workoutsRepository.insert(workout_to_insert);

                            Log.d(TAG, "run: workout inserted! " + workout_id);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                getBackendResponse(path, reports, getReports(), true);
            }
        };

    }

    private Runnable getReports() {
        return new Runnable() {
            @Override
            public void run() {
                WorkoutsRepository workoutsRepository = new WorkoutsRepository(getActivity().getApplication());
                WorkoutsReportsRepository workoutsReportsRepository = new WorkoutsReportsRepository(getActivity().getApplication());

                for (JSONObject report : reports) {
                    try {
                        int workout_id_backend = report.getInt("workout_id");

                        Workouts workout = workoutsRepository.getWorkoutFromIdBackend(workout_id_backend);
                        if (workout != null) {
                            int workout_id = workout.getId();

                            Log.d(TAG, "run: obtengo report");
                            WorkoutReports wr = new WorkoutReports(workout_id,
                                    PikerDate.Companion.toDateFormatView(report.getString("execution_date")));

                            //si no existe en la base de datos local, lo agrego

                            if (!workoutsReportsRepository.contains(wr.getWorkout_id(), wr.getExecution_date())) {
                                workoutsReportsRepository.insert(wr);
                                Log.d(TAG, "run: report inserted! " + wr.getId_wr());
                            }


                            //y actualizo el workout
                            if (workout.getDone() == 0)
                                workoutsRepository.update(true, report.getInt("workout_id"), report.optInt("rating", 0));
                        }

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
                        getBackendResponse(path, workouts, getWorkouts(), false);

                    }
                }
        );

    }


    private void getBackendResponse(String path, List<JSONObject> listObj, Runnable func, boolean report) {
        OkHttpRequest request = new OkHttpRequest(new OkHttpClient());
        String url = MainActivity.PATH + path;

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
                Log.d(TAG, "onResponse: got response");
                try {
                    if (response.body() != null) {
                        responseData = response.body().string();
                        Object jsonType = new JSONTokener(responseData).nextValue();

                        if (jsonType instanceof JSONArray) {
                            JSONArray json = new JSONArray(responseData);

                            if (readData(report, json)) {
                                Log.d(TAG, "onResponse: no data on phone");
                                for (int i = 0; i < json.length(); i++) {
                                    listObj.add(json.getJSONObject(i));
                                }
                                func.run();
                            }
                        }
                    }

                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }

            private boolean readData(boolean report, JSONArray json) {
                if (report) {
                    if (json.length() == AppDatabase.getAppDatabase(getContext()).workoutsReportDAO().getSize()) {
                        new TaskGetWorkoutReports(TrainingHistoryFragment.this).execute();
                        return false;
                    }
                } else if (json.length() == AppDatabase.getAppDatabase(getContext()).workoutsDAO().getSize()) {
                    String new_path = "workoutReport/";
                    getBackendResponse(new_path, reports, getReports(), true);
                    return false;
                }
                return true;
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
