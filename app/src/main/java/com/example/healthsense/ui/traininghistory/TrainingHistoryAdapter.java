package com.example.healthsense.ui.traininghistory;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthsense.R;
import com.example.healthsense.db.entity.Workout;
import com.example.healthsense.db.entity.WorkoutDone;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrainingHistoryAdapter extends RecyclerView.Adapter<TrainingHistoryAdapter.ViewHolder> {

    private static final String TAG = "TrainingHistoryAdapter";

    List<WorkoutDone> dataset;

    public TrainingHistoryAdapter(){
        this.dataset = new ArrayList<>();
    }

    public List<WorkoutDone> getDataset() {
        return dataset;
    }

    public void setDataset(List<WorkoutDone> dataset) {
        this.dataset = dataset;
        //Collections.sort(dataset, Collections.<WorkoutDone>reverseOrder());
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
        holder.name.setText(Html.fromHtml("<b>" + workout.getName() + "</b>"));
        holder.description.setText(workout.getDescription());
        holder.date.setText(Html.fromHtml("<b>DATE:</b> " + String.valueOf(workout.getDate())));
        holder.difficulty.setText(Html.fromHtml("<b>DIFFICULTY:</b>"));
        for(int i = 0; i<workout.getDifficulty(); i++) {
            //TODO arreglar cantidad de estrellas
            holder.difficulty.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_star, 0);
        }
        //TODO agregar onclick listener para cada tarjeta
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView description;
        TextView date;
        TextView difficulty;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_training);
            description = itemView.findViewById(R.id.description_training);
            date = itemView.findViewById(R.id.date_executed);
            difficulty = itemView.findViewById(R.id.difficulty_training);
        }
    }
}
