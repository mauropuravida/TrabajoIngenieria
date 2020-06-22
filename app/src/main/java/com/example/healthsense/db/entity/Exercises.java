package com.example.healthsense.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName="Exercises" ,
        indices = {@Index( name ="id_UNIQUE_exercice", value = {"id"}, unique = true)})
public class Exercises {

    @NotNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NotNull
    @ColumnInfo(name="description")
    private String description;

    @ColumnInfo(name="path")
    private String path;

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
}

