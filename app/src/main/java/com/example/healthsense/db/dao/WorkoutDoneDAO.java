package com.example.healthsense.db.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.healthsense.db.entity.WorkoutDone;

import java.util.List;

/**
 * Interfaz WorkoutDoneDAO encargada de comunicarse con el Serverless y obtener resultados.
 */

@Dao
public interface WorkoutDoneDAO {

    @Query("SELECT * FROM Workouts INNER JOIN Workout_Reports ON Workouts.id_backend = Workout_Reports.workout_id WHERE Workouts.device_user_id = :id")
    List<WorkoutDone> getAllWorkoutsDone(int id);
}
