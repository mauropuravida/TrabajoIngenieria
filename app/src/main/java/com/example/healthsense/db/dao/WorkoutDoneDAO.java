package com.example.healthsense.db.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.healthsense.db.entity.WorkoutDone;

import java.util.List;

@Dao
public interface WorkoutDoneDAO {

    @Query("SELECT * FROM Workouts INNER JOIN Workout_Reports ON Workouts.id = Workout_Reports.workout_id WHERE Workouts.done = 1")
    List<WorkoutDone> getAllWorkoutsDone();
}
