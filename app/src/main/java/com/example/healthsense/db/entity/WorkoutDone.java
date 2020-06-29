package com.example.healthsense.db.entity;

import android.icu.text.SimpleDateFormat;

import androidx.room.Embedded;

import java.text.ParseException;
import java.util.Date;

public class WorkoutDone implements Comparable<WorkoutDone> {

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

    @Override
    public int compareTo(WorkoutDone workoutDone) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = format.parse(this.getDate());
            Date date2 = format.parse(workoutDone.getDate());

            return date1.compareTo(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
