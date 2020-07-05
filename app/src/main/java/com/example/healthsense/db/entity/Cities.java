package com.example.healthsense.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

/**
 * Tabla Cities Room
 */

@Entity(tableName = "Cities", indices =
        {@Index(name = "cities_test_ibfk_1",value = {"state_id"}),
         @Index(name = "id_UNIQUE_city",value = {"id"}, unique = true)})
public class Cities {

    @NotNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NotNull
    @ColumnInfo(name="name")
    private String name;

    @NotNull
    @ForeignKey(entity = States.class, parentColumns = "id",childColumns = "state_id",
            onUpdate = ForeignKey.CASCADE)
    @ColumnInfo(name= "state_id")
    private int state_id;

    public Cities(@NotNull String name, int state_id) {
        this.name = name;
        this.state_id = state_id;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public int getState_id() {
        return state_id;
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

    public void setState_id(int state_id) {
        this.state_id = state_id;
    }
}
