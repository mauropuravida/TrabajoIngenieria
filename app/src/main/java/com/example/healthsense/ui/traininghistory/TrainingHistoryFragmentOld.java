package com.example.healthsense.ui.traininghistory;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.healthsense.R;
import com.example.healthsense.db.AppDatabase;
import com.example.healthsense.db.AppDatabaseListener;
import com.example.healthsense.db.entity.WorkoutDone;
import com.example.healthsense.ui.home.HomeFragment;
import com.example.healthsense.ui.traininginformation.TrainingInformation;

import java.util.ArrayList;
import java.util.List;

public class TrainingHistoryFragmentOld extends Fragment implements AppDatabaseListener {

    private static final String BACK_STACK_ROOT_TAG = "root_fragment";

    private View root;
    private LinearLayout lista;
    private TextView workoutscompleted;


    private List<WorkoutDone> listReports = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_training_history_old, container, false);


        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new HomeFragment()).addToBackStack(null).commit();
            }
        };


        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        //TextView totalhours = root.findViewById(R.id.total_ours);
        workoutscompleted = root.findViewById(R.id.workouts_completed);

        //totalhours.setText("15");
        //workoutscompleted.setText(listReports.size());

        lista = root.findViewById(R.id.list_trainings);

        new TaskGetWorkoutReports(this).execute();

//        workoutscompleted.setText(String.valueOf(listReports.size()));
//
//        for (WorkoutDone workout : listReports) {
//            newCard(root, lista, workout.getName(), "descripcion", workout.getDate(), workout.getDifficulty());
//
//        }


        return root;
    }



    private void newCard(View root, LinearLayout lista, String name, String description, String date, int difficulty) {

        //5 estrellas ya se me van de la pantalla
        if (difficulty < 1) difficulty = 1;
        if (difficulty > 4) difficulty = 4;

        Space s0 = new Space(root.getContext());
        s0.setMinimumHeight(30);

        LinearLayout ll = new LinearLayout(root.getContext());
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setGravity(Gravity.CENTER_VERTICAL);

        TextView tv1 = new TextView(root.getContext());
        tv1.setText(Html.fromHtml("<b>" + name + "</b><br><br> " +
                description));
        //tv1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_star, 0, 0, 30);
        tv1.setTextColor(root.getResources().getColor(R.color.GrayText));
        tv1.setPadding(30, 30, 30, 30);

        ll.addView(tv1);

        LinearLayout ll1 = new LinearLayout(root.getContext());
        ll1.setOrientation(LinearLayout.HORIZONTAL);
        ll1.setGravity(Gravity.CENTER_VERTICAL);

        TextView tv3 = new TextView(root.getContext());
        tv3.setText(Html.fromHtml("<b>DATE:</b> " + date + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>DIFFICULTY:</b> "));
        tv3.setTextColor(root.getResources().getColor(R.color.GrayText));
        tv3.setPadding(30, 30, 30, 30);

        ll1.addView(tv3);

        //estrellas
        for (int i = 0; i < difficulty; i++) {
            TextView tv4 = new TextView(root.getContext());
            tv4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_star, 0, 0, 0);
            tv4.setPadding(0, 20, 0, 20);
            ll1.addView(tv4);
        }

        LinearLayout ll3 = new LinearLayout(root.getContext());
        ll3.setOrientation(LinearLayout.VERTICAL);
        ll3.setGravity(Gravity.CENTER_VERTICAL);

        ll3.setBackgroundResource(R.drawable.background_model_training_history);

        ll3.addView(ll);
        ll3.addView(ll1);

        ll3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new TrainingInformation()).addToBackStack(null).commit();
                //trainingInformation con la informacion que va
            }

        });

        lista.addView(s0);
        lista.addView(ll3);
    }



    @Override
    public void setWorkoutReportsFromDB(List<WorkoutDone> workouts) {
        workoutscompleted.setText(String.valueOf(listReports.size()));

        for (WorkoutDone workout : listReports) {
            newCard(root, lista, workout.getName(), "descripcion", workout.getDate(), workout.getDifficulty());

        }

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
            Log.d(TAG, "onPostExecute: " + workoutsDone.size() );
        }
    }
}