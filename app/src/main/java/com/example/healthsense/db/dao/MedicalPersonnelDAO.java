package com.example.healthsense.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import com.example.healthsense.db.entity.MedicalPersonnel;

/**
 * Interfaz MedicalPersonnelDAO encargada de comunicarse con el Serverless y obtener resultados.
 */

@Dao
public interface MedicalPersonnelDAO {

    @Insert
    void insert(MedicalPersonnel medical);

    @Update
    void update(MedicalPersonnel medical);

    @Delete
    void delete(MedicalPersonnel medical);

}
