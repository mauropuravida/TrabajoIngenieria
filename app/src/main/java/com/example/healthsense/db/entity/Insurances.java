package com.example.healthsense.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName="Insurances")
public class Insurances {

    @NotNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NotNull
    @ColumnInfo(name="name")
    private String name;

    public Insurances(@NotNull String name) {
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
