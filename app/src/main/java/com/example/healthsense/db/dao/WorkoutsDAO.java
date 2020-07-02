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

    @Query("UPDATE Workouts SET done = :done, rating = :rating WHERE id = :id")
    void updateDone(int done, int id, int rating);

    @Query("Select id FROM Workouts where id_backend = :id_backend")
    int obtainWorkoutIdRoom(int id_backend);

    @Query("SELECT * FROM Workouts where id_backend = :id_backend")
    Workouts getWorkoutFromIdBackend(int id_backend);

    @Query("SELECT COUNT(*) FROM workouts")
    int getSize();

    @Query("SELECT CASE WHEN EXISTS (SELECT * FROM Workouts WHERE id_backend = :id_backend)" +
            "THEN CAST(1 AS BIT)ELSE CAST(0 AS BIT) END")
    boolean existWorkout(int id_backend);
}
