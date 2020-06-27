package com.example.healthsense.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.healthsense.db.entity.Exercises;
import com.example.healthsense.db.entity.WorkoutExercises;

import java.util.List;

@Dao
public interface WorkoutsExercisesDAO {

    @Insert
    void insert(WorkoutExercises workoutExercises);

    @Update
    void update(WorkoutExercises workoutExercises);

    @Delete
    void delete(WorkoutExercises workoutExercises);

    @Query("SELECT * FROM Workout_Exercises WHERE id =  :id")
    WorkoutExercises getWorkoutsExercises(int id);

    @Query("SELECT * FROM Workout_Exercises WHERE workout_id = :id")
    List<WorkoutExercises> getWorkouts(int id);

    @Query("SELECT * FROM Workout_Exercises WHERE exercise_id = :id")
    List<WorkoutExercises> getExercises(int id);

    @Query("SELECT * FROM Workout_Exercises")
    List<WorkoutExercises> getAll();

    @Query("SELECT CASE WHEN EXISTS (SELECT * FROM Workout_Exercises WHERE" +
            " workout_id = :id_workout AND exercise_id = :id_exercises)" +
            "THEN CAST(1 AS BIT)ELSE CAST(0 AS BIT) END")
    boolean existExerciseWorkout(int id_workout, int id_exercises);

    @Query("DELETE FROM Workout_Exercises")
    void deleteAll();
}
