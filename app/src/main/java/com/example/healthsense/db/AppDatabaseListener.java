package com.example.healthsense.db;

import com.example.healthsense.db.entity.WorkoutDone;

import java.util.List;

public interface AppDatabaseListener {

    void setWorkoutReportsFromDB(List<WorkoutDone> workouts);
}
