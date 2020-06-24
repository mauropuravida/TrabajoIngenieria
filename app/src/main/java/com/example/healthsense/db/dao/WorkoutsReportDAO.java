package com.example.healthsense.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.healthsense.db.entity.WorkoutReports;

import java.util.List;

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
}
