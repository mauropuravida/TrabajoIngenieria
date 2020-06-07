package com.example.healthsense.db.entity;

import android.icu.text.SimpleDateFormat;

import androidx.room.Embedded;

import java.text.ParseException;
import java.util.Date;

public class WorkoutDone implements Comparable<WorkoutDone> {

    @Embedded
    private Workout workout;

    @Embedded
    private WorkoutReport workoutReport;

    public String getName() {
        return workout.getName();
    }

    public String getDate() {
        return workoutReport.getExecution_date();
    }

    public int getDifficulty() {
        return workout.getDifficulty();
    }

    public Workout getWorkout() {
        return workout;
    }

    public void setWorkout(Workout workout) {
        this.workout = workout;
    }

    public WorkoutReport getWorkoutReport() {
        return workoutReport;
    }

    public void setWorkoutReport(WorkoutReport workoutReport) {
        this.workoutReport = workoutReport;
    }

    public String getDescription() {
        return "description";
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
