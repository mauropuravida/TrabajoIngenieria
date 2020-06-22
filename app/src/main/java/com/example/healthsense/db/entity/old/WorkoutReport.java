package com.example.healthsense.db.entity.old;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;


import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "WorkoutReport")
public class WorkoutReport {

    @PrimaryKey
    private int id_wkr;

    @ForeignKey(entity = Workout.class, parentColumns = "id_wk", childColumns = "workout_id", onDelete = CASCADE)
    private int workout_id;

    @ColumnInfo(name = "execution_date")
    private String execution_date;

    public WorkoutReport(int id_wkr, int workout_id, String execution_date) {
        this.id_wkr = id_wkr;
        this.workout_id = workout_id;
        this.execution_date = execution_date;
    }

    public int getId_wkr() {
        return id_wkr;
    }

    public void setId_wkr(int id_wkr) {
        this.id_wkr = id_wkr;
    }

    public int getWorkout_id() {
        return workout_id;
    }

    public void setWorkout_id(int workout_id) {
        this.workout_id = workout_id;
    }

    public String getExecution_date() {
        return execution_date;
    }

    public void setExecution_date(String execution_date) {
        this.execution_date = execution_date;
    }
}
