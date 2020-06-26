package com.example.healthsense.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.healthsense.db.entity.Workouts;

import java.util.List;

@Dao
public interface WorkoutsDAO {

    @Insert
    void insert(Workouts workout);

    @Update
    void update(Workouts workout);

    @Delete
    void delete(Workouts workout);

    @Query("DELETE FROM Workouts")
    void deleteAll();

    @Query("SELECT * FROM Workouts WHERE id =  :id")
    Workouts getWorkout(int id);

    @Query("SELECT * FROM Workouts")
    List<Workouts> getAll();

    @Query("SELECT * FROM Workouts WHERE device_user_id = :id")
    List<Workouts> getWorkoutsDevice(int id);

    @Query("SELECT * FROM Workouts WHERE medical_personnel_id = :id")
    List<Workouts> getWorkoutsMedical(int id);

    @Query("UPDATE Workouts SET done = :done WHERE id = :id")
    int updateDone(boolean done, int id);

}
