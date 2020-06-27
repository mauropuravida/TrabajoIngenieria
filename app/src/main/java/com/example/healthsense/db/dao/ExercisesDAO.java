package com.example.healthsense.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.healthsense.db.entity.Exercises;

import java.util.List;

@Dao
public interface ExercisesDAO {

    @Insert
    void insert(Exercises exercises);

    @Update
    void update(Exercises exercises);

    @Delete
    void delete(Exercises exercises);

    @Query("DELETE FROM Exercises")
    void deleteAll();

    @Query("SELECT * FROM Exercises")
    List<Exercises> getAll();

    @Query("SELECT * FROM Exercises WHERE id =  :id")
    Exercises getExercises(int id);

    @Query("SELECT COUNT(id) FROM Exercises ")
    int getCount();

    @Query("Select id FROM Exercises where id_backend = :id_backend")
    int obtainExercisesIdRoom(int id_backend);
}
