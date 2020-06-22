package com.example.healthsense.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "States", indices =
        {@Index(name ="country_region", value = {"country_id"}),
         @Index(name ="id_UNIQUE_state", value = {"id"}, unique = true)})
public class States {

    @NotNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NotNull
    @ColumnInfo(name="name")
    private String name;

    @NotNull
    @ForeignKey(entity = Countries.class, parentColumns = "id", childColumns = "country_id",
            onDelete = ForeignKey.RESTRICT, onUpdate = ForeignKey.CASCADE)
    @ColumnInfo(name="country_id")
    private int country_id;

    public States(@NotNull String name, int country_id) {
        this.name = name;
        this.country_id = country_id;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public int getCountry_id() {
        return country_id;
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

    public void setCountry_id(int country_id) {
        this.country_id = country_id;
    }
}
