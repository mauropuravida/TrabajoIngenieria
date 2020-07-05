package com.example.healthsense.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.healthsense.db.entity.Users;

/**
 * Interfaz UsersDAO encargada de comunicarse con el Serverless y obtener resultados.
 */

@Dao
public interface UsersDAO {

    @Insert
    void insert(Users user);

    @Update
    void update(Users user);

    @Delete
    void delete(Users user);

    @Query("DELETE FROM Users")
    void deleteAll();

    @Query("SELECT id from Users where email = :email")
    int getID(String email);

    @Query("SELECT CASE WHEN EXISTS (\n" +
            "    SELECT *\n" +
            "    FROM Users\n" +
            "    WHERE email = :email\n" +
            ")\n" +
            "THEN CAST(1 AS BIT)\n" +
            "ELSE CAST(0 AS BIT) END")
    boolean exist(String email);
}
