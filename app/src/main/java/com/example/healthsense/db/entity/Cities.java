package com.example.healthsense.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Cities")
public class Cities {

    @PrimaryKey
    int id;

    @ColumnInfo(name = "name")
    private String name;

  /*  @ForeignKey(entity = State.class, parentColumns = "id",
            childColumns = "state_id", onUpdate = ForeignKey.CASCADE)
    @ColumnInfo(name= "state_id")
    private int state_id;*/

    public Cities(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
