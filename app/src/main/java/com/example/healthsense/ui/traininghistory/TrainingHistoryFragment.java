package com.example.healthsense.ui.traininghistory;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthsense.R;
import com.example.healthsense.db.AppDatabase;
import com.example.healthsense.db.AppDatabaseListener;
import com.example.healthsense.db.entity.WorkoutDone;
import com.example.healthsense.ui.home.HomeFragment;

import java.util.ArrayList;
import java.util.List;

public class TrainingHistoryFragment extends Fragment implements AppDatabaseListener {

    private View root;
    private TrainingHistoryAdapter adapter;
    private RecyclerView recyclerView;


    private TextView workoutscompleted;
    private List<WorkoutDone> listReports = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_training_history, container, false);


        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new HomeFragment()).addToBackStack(null).commit();
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        workoutscompleted = root.findViewById(R.id.workouts_completed);

        adapter = new TrainingHistoryAdapter();

        recyclerView = root.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        new TaskGetWorkoutReports(this).execute();

        return root;
    }

    @Override
    public void setWorkoutReportsFromDB(List<WorkoutDone> workouts) {
        adapter.setDataset(workouts);
        workoutscompleted.setText(String.valueOf(workouts.size()));
    }


    private class TaskGetWorkoutReports extends AsyncTask<Void, Void, List<WorkoutDone>> {

        AppDatabaseListener listener;
        private static final String TAG = "TaskGetWorkoutReports";

        public TaskGetWorkoutReports(AppDatabaseListener listener) {
            this.listener = listener;
        }

        @Override
        protected List<WorkoutDone> doInBackground(Void... voids) {
            List<WorkoutDone> workoutsDone = AppDatabase.getAppDatabase(getContext()).workoutDoneDAO().getAllWorkoutsDone();
            return workoutsDone;
        }

        @Override
        protected void onPostExecute(List<WorkoutDone> workoutsDone) {
            super.onPostExecute(workoutsDone);
            listener.setWorkoutReportsFromDB(workoutsDone);
            Log.d(TAG, "onPostExecute: workouts: " + workoutsDone.size() );
        }
    }

}
