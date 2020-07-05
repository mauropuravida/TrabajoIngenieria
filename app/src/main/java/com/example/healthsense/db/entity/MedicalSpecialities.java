package com.example.healthsense.db.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

/**
 * Tabla MedicalSpecialities Room
 */


@Entity(tableName="Medical_Specialities" ,
        indices = {@Index(name ="id_UNIQUE_medical_specialities",value = {"id"}, unique = true)})
public class MedicalSpecialities {

    @NotNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NotNull
    @ColumnInfo(name="name")
    private String name;


    public MedicalSpecialities(@NotNull String name) {
        this.name = name;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }
}
