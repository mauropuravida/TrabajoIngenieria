package com.example.healthsense.db.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.healthsense.db.entity.WorkoutDone;

import java.util.List;

@Dao
public interface WorkoutDoneDAO {

    @Query("SELECT * FROM workouts INNER JOIN workoutreports ON workouts.id_wk = workoutreports.workout_id WHERE workouts.done = 1")
    List<WorkoutDone> getAllWorkoutsDone();
}
