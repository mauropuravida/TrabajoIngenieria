package com.example.healthsense.db.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.healthsense.db.entity.WorkoutDone;

import java.util.List;

@Dao
public interface WorkoutDoneDAO {

    @Query("SELECT * FROM workout INNER JOIN workoutreport ON workout.id_wk = workoutreport.workout_id WHERE workout.done = 1")
    List<WorkoutDone> getAllWorkoutsDone();
}
