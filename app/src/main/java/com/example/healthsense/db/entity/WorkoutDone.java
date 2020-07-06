package com.example.healthsense.db.entity;


import androidx.room.Embedded;

/**
 * Clase auxiliar interna WorkoutDone
 */


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

    public int getMedic(){
        return workout.getMedical_personnel_id();
    }

}
