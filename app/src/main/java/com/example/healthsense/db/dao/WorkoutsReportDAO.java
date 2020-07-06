package com.example.healthsense.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.healthsense.db.entity.WorkoutReports;

import java.util.List;

/**
 * Interfaz WorkoutsReportDAO encargada de comunicarse con el Serverless y obtener resultados.
 */

@Dao
public interface WorkoutsReportDAO {

    @Insert
    void insert(WorkoutReports workoutReport);

    @Insert
    void insertAll(List<WorkoutReports> workoutReports);

    @Query("SELECT * FROM Workout_Reports")
    List<WorkoutReports> getWorkoutsReports();

    @Update
    void update(WorkoutReports workoutReport);

    @Delete
    void delete(WorkoutReports workoutReport);

    @Query("DELETE FROM Workout_Reports")
    void deleteAll();

    @Query("SELECT COUNT(*) FROM Workout_Reports")
    int getSize();

    @Query("SELECT * FROM Workout_Reports WHERE workout_id = :id")
    List<WorkoutReports> getWorkoutsId(int id);

    @Query("SELECT CASE WHEN EXISTS (SELECT * FROM Workout_Reports WHERE" +
            " workout_id = :id_workout AND execution_date Like :execution_date)" +
            "THEN CAST(1 AS BIT)ELSE CAST(0 AS BIT) END")
    boolean existWorkoutReport(int id_workout, String execution_date);

    @Query("SELECT sent FROM Workout_Reports WHERE workout_id = :workout_id")
    boolean isSent(int workout_id);

    @Query("SELECT * FROM Workout_Reports WHERE sent = 0")
    List<WorkoutReports> getUnsent();

    @Query("SELECT * FROM Workout_Reports WHERE id_wr = :id")
    WorkoutReports getWorkoutReport(int id);

    @Query("SELECT * FROM Workout_Reports WHERE idBackend = :id_backend")
    WorkoutReports getWorkoutReportBackend(int id_backend);
}
