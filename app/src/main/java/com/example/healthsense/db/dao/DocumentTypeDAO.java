package com.example.healthsense.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.healthsense.db.entity.DocumentType;

import java.util.List;

/**
 * Interfaz DocumentTypeDAO encargada de comunicarse con el Serverless y obtener resultados.
 */

@Dao
public interface DocumentTypeDAO {

    @Insert
    void insert(DocumentType documentType);

    @Update
    void update(DocumentType documentType);

    @Delete
    void delete(DocumentType documentType);

    @Query("SELECT * FROM Document_Types ")
    LiveData<List<DocumentType>> getAllDocumentTypes();

}
