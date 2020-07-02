package com.example.healthsense.db.entity;

import android.icu.text.SimpleDateFormat;

import androidx.room.Embedded;

import java.text.ParseException;
import java.util.Date;

public class WorkoutDone {

    @Embedded
    private Workouts workout;

    @Embedded
    private WorkoutReports workoutReport;

    public String getName() {
        return workout.getName();
    }

    public String getDate() {
        return workoutReport.getExecution_date();
    }

    public int getDifficulty() {
        return workout.getDifficulty();
    }

    public int getRating(){
        return workout.getRating();
    }

    public Workouts getWorkout() {
        return workout;
    }

    public void setWorkout(Workouts workout) {
        this.workout = workout;
    }

    public WorkoutReports getWorkoutReport() {
        return workoutReport;
    }

    public void setWorkoutReport(WorkoutReports workoutReport) {
        this.workoutReport = workoutReport;
    }

    public String getDescription() {
        return "description";
    }

    public int getWorkoutId(){
        return this.workout.getId();
    }

    public int getReportId(){
        return workoutReport.getId_wr();
    }

}
