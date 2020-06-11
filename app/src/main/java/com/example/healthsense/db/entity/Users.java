package com.example.healthsense.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Users")
public class Users {
    @PrimaryKey
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "last_name")
    private String last_name;

    @ColumnInfo(name = "birth_date")
    private String birth_date;

    @ColumnInfo(name = "gender")
    private char gender;

    @ForeignKey(entity = DocumentType.class, parentColumns = "id",
                childColumns = "document_type_id", onUpdate = ForeignKey.CASCADE)
    @ColumnInfo(name = "document_type_id")
    private int document_type_id;

    @ColumnInfo(name = "document_number")
    private String document_number;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "password")
    private String password;

    @ForeignKey(entity = Cities.class, parentColumns = "id", childColumns = "city_id",
                onDelete = ForeignKey.RESTRICT, onUpdate = ForeignKey.CASCADE)
    @ColumnInfo(name = "city_id")
    private int city_id;

    @ColumnInfo(name = "address")
    private String address;

    public Users(int id, String name, String last_name, String birth_date, char gender, int document_type_id, String document_number, String email, String password, int city_id, String address) {
        this.id = id;
        this.name = name;
        this.last_name = last_name;
        this.birth_date = birth_date;
        this.gender = gender;
        this.document_type_id = document_type_id;
        this.document_number = document_number;
        this.email = email;
        this.password = password;
        this.city_id = city_id;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public char getGender() {
        return gender;
    }

    public int getDocument_type_id() {
        return document_type_id;
    }

    public String getDocument_number() {
        return document_number;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getCity_id() {
        return city_id;
    }

    public String getAddress() {
        return address;
    }
}
