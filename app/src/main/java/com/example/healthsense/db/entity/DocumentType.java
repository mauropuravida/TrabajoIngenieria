package com.example.healthsense.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Document_Types")
public class DocumentType {
    @PrimaryKey
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    public DocumentType(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
