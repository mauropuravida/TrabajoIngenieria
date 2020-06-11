package com.example.healthsense.ui.traininghistory;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthsense.R;
import com.example.healthsense.db.entity.Workout;
import com.example.healthsense.db.entity.WorkoutDone;
import com.example.healthsense.ui.traininginformation.TrainingInformation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrainingHistoryAdapter extends RecyclerView.Adapter<TrainingHistoryAdapter.ViewHolder> {

    private static final String TAG = "TrainingHistoryAdapter";

    List<WorkoutDone> dataset;

    public TrainingHistoryAdapter() {
        this.dataset = new ArrayList<>();
    }

    public List<WorkoutDone> getDataset() {
        return dataset;
    }

    public void setDataset(List<WorkoutDone> dataset) {
        this.dataset = dataset;
        Collections.sort(dataset, Collections.<WorkoutDone>reverseOrder());
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_training_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: workout" + position);
        WorkoutDone workout = dataset.get(position);
        holder.name.setText(workout.getName());
        holder.description.setText(workout.getDescription());
        holder.date.setText(Html.fromHtml("<b>DATE:</b> " + String.valueOf(workout.getDate())));
        holder.difficulty.setText(Html.fromHtml("<b>DIFFICULTY:</b>"));
        for (int i = 0; i < workout.getDifficulty(); i++) {
            TextView tv = new TextView(holder.getContext());
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_star, 0, 0, 0);
            holder.linearLayout.addView(tv);
        }

        holder.workout_id = workout.getWorkoutId();
        holder.report_id = workout.getReportId();
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name;
        TextView description;
        TextView date;
        TextView difficulty;
        LinearLayout linearLayout;

        int workout_id;
        int report_id;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.name = itemView.findViewById(R.id.name_training);
            this.description = itemView.findViewById(R.id.description_training);
            this.date = itemView.findViewById(R.id.date_executed);
            this.difficulty = itemView.findViewById(R.id.difficulty_training);
            this.linearLayout = itemView.findViewById(R.id.linearLayout1);
            this.workout_id = -1;
            this.report_id = -1;
        }

        public Context getContext(){
            return itemView.getContext();
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick: " + getAdapterPosition());
            Toast.makeText(view.getContext(),
                    "Click on workout: " + String.valueOf(this.workout_id) + " - report: " + String.valueOf(this.report_id),
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.getIntExtra("workout id", this.workout_id);
            intent.getIntExtra("report id", this.report_id);
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new TrainingInformation()).addToBackStack(null).commit();

//            getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new TrainingInformation()).addToBackStack(null).commit();

        }
    }
}