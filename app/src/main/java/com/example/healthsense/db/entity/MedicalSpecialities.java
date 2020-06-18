package com.example.healthsense.db.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName="Medical_Specialities" ,
        indices = {@Index("id_UNIQUE"), @Index(value = {"id"}, unique = true)})
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

}
