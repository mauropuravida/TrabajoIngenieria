package com.example.healthsense.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

/**
 * Tabla Exercises Room
 */


@Entity(tableName="Exercises" ,
        indices = {@Index( name ="id_UNIQUE_exercice", value = {"id"}, unique = true)})
public class Exercises{

    @NotNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NotNull
    @ColumnInfo(name="description")
    private String description;

    @ColumnInfo(name="path")
    private String path;

    @ColumnInfo(name="id_backend")
    private int id_backend;


    public Exercises(@NotNull String description, String path) {
        this.description = description;
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public String getPath() {
        return path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescription(@NotNull String description) {
        this.description = description;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getId_backend() {
        return id_backend;
    }

    public void setId_backend(int id_backend) {
        this.id_backend = id_backend;
    }

}

