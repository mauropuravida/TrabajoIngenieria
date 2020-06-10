package com.example.healthsense.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.healthsense.db.entity.WorkoutReport;

import java.util.List;

@Dao
public interface WorkoutReportDAO {

    @Insert(onConflict = OnConflictStrategy. REPLACE)
    void insert(WorkoutReport workoutReport);

    @Insert(onConflict = OnConflictStrategy. REPLACE)
    void insertAll(List<WorkoutReport> workoutReports);

    @Query("SELECT * FROM WorkoutReports")
    List<WorkoutReport> getWorkoutsReports();

    @Update
    void update(WorkoutReport workoutReport);

    @Delete
    void delete(WorkoutReport workoutReport);

    @Query("DELETE FROM WorkoutReports")
    void deleteAll();
}
