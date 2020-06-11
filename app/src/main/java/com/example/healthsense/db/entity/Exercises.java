package com.example.healthsense.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Exercises")
public class Exercises {
    @PrimaryKey
    private int id;

    @ColumnInfo(name="description")
    private String description;

    @ColumnInfo(name="path")
    private String path;

    public Exercises(int id, String description, String path) {
        this.id = id;
        this.description = description;
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public String getPath() {
        return path;
    }
}
