package com.example.healthsense.db.dao.old;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.healthsense.db.entity.old.Workout;

import java.util.List;

@Dao
public interface WorkoutDAO {

    @Insert(onConflict = OnConflictStrategy. REPLACE)
    void insert(Workout workout);

    @Insert(onConflict = OnConflictStrategy. REPLACE)
    void insertAll(List<Workout> workouts);

    @Update
    void update(Workout workout);

    @Delete
    void delete(Workout workout);

    @Query("DELETE FROM workout")
    void deleteAll();

    @Query("SELECT * FROM workout WHERE id_wk =  :id")
    Workout getWorkout(int id);

}
