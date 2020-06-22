package com.example.healthsense.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Entity(tableName = "Users",  indices =
        {@Index(name ="fk_Users_Document_Types1_idx", value = {"document_type_id"}),
         @Index(name ="fk_cities_idx", value = {"city_id"}),
         @Index(name ="id_UNIQUE", value = "id", unique = true),
         @Index(name ="email_UNIQUE", value = "email", unique = true)})
public class Users {

    @NotNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NotNull
    @ColumnInfo(name = "name")
    private String name;

    @NotNull
    @ColumnInfo(name = "last_name")
    private String last_name;

    @NotNull
    @ColumnInfo(name = "birth_date")
    private String birth_date;

    @ColumnInfo(name = "gender")
    private char gender;

    @NotNull
    @ForeignKey(entity = DocumentType.class, parentColumns = "id",
                childColumns = "document_type_id", onUpdate = ForeignKey.CASCADE)
    @ColumnInfo(name = "document_type_id")
    private int document_type_id;

    @NotNull
    @ColumnInfo(name = "document_number")
    private String document_number;

    @NotNull
    @ColumnInfo(name = "email")
    private String email;

    @NotNull
    @ColumnInfo(name = "password")
    private String password;

    @ForeignKey(entity = Cities.class, parentColumns = "id", childColumns = "city_id",
                onDelete = ForeignKey.RESTRICT, onUpdate = ForeignKey.CASCADE)
    @ColumnInfo(name = "city_id")
    private int city_id;

    @ColumnInfo(name = "address")
    private String address;

    public Users (String name, String last_name, String birth_date, int document_type_id, String document_number, String email, String password) {
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public void setLast_name(@NotNull String last_name) {
        this.last_name = last_name;
    }

    public void setBirth_date(@NotNull String birth_date) {
        this.birth_date = birth_date;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public void setDocument_type_id(int document_type_id) {
        this.document_type_id = document_type_id;
    }

    public void setDocument_number(@NotNull String document_number) {
        this.document_number = document_number;
    }

    public void setEmail(@NotNull String email) {
        this.email = email;
    }

    public void setPassword(@NotNull String password) {
        this.password = password;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
