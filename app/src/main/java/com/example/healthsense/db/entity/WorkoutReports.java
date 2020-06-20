package com.example.healthsense.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;


import org.jetbrains.annotations.NotNull;


@Entity(tableName="Workout_Reports" , indices =
        {@Index("fk_Workout_reports_Workouts1_idx"), @Index(value = {"workout_id"}),
         @Index("id_UNIQUE"), @Index(value = "id", unique = true)})
public class WorkoutReports {

    @NotNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NotNull
    @ForeignKey(entity = Workouts.class, parentColumns = "id", childColumns = "workout_id",
            onUpdate = ForeignKey.CASCADE, onDelete = ForeignKey.NO_ACTION)
    @ColumnInfo(name="workout_id")
    private int workout_id;

    @NotNull
    @ColumnInfo(name = "execution_date")
    private String execution_date;


    public WorkoutReports(int workout_id, @NotNull String execution_date) {
        this.workout_id = workout_id;
        this.execution_date = execution_date;
    }

    public int getWorkout_id() {
        return workout_id;
    }

    @NotNull
    public String getExecution_date() {
        return execution_date;
    }
}
