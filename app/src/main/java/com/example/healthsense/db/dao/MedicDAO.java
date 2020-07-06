package com.example.healthsense.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.healthsense.db.entity.Medic;

import java.util.List;

/**
 * Interfaz MedicDAO encargada de comunicarse con el Serverless y obtener resultados.
 */

@Dao
public interface MedicDAO {

    @Insert
    void insert(Medic medic);

    @Delete
    void delete(Medic medic);

    @Update
    void update(Medic medic);

    @Query("DELETE FROM Medic")
    void deleteAll();

    @Query("SELECT * FROM Medic WHERE medical_personnel_id =  :id")
    Medic getMedic(int id);

    @Query("SELECT * FROM Medic")
    List<Medic> getAll();

    @Query("SELECT COUNT(*) FROM Medic")
    int size();

    @Query("SELECT CASE WHEN EXISTS (SELECT * FROM Medic WHERE medical_personnel_id = :id)" +
            "THEN CAST(1 AS BIT)ELSE CAST(0 AS BIT) END")
    boolean exists(int id);

    @Query("UPDATE Medic SET price = :price AND expires = :expires WHERE medical_personnel_id = :id")
    void updatePrice(int id, String price, String expires);


}
