package com.example.healthsense.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;


import org.jetbrains.annotations.NotNull;


@Entity(tableName="Workout_Reports" , indices =
        {@Index(name ="fk_Workout_reports_Workouts1_idx", value = {"workout_id"}),
         @Index(name ="id_UNIQUE_workout_report",value = "id_wr", unique = true)})
public class WorkoutReports {

    @NotNull
    @PrimaryKey(autoGenerate = true)
    private int id_wr;

    @NotNull
    @ForeignKey(entity = Workouts.class, parentColumns = "id", childColumns = "workout_id",
            onUpdate = ForeignKey.CASCADE, onDelete = ForeignKey.NO_ACTION)
    @ColumnInfo(name="workout_id")
    private int workout_id;

    @NotNull
    @ColumnInfo(name = "execution_date")
    private String execution_date;

    @ColumnInfo(name = "sent")
    private boolean sent;


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

    public int getId_wr() {
        return id_wr;
    }

    public void setId_wr(int id) {
        this.id_wr = id;
    }

    public void setWorkout_id(int workout_id) {
        this.workout_id = workout_id;
    }

    public void setExecution_date(@NotNull String execution_date) {
        this.execution_date = execution_date;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }
}
