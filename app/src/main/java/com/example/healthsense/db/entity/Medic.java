package com.example.healthsense.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

/**
 * Clase auxiliar interna Medic para almacenar la lista de medicos
 */

@Entity(tableName = "Medic")
public class Medic {

    @NotNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NotNull
    @ColumnInfo(name = "name")
    private String name;

    @NotNull
    @ColumnInfo(name = "last_name")
    private String last_name;

    @ColumnInfo(name = "gender")
    private String gender;

    @NotNull
    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "document_number")
    private String document_number;

    @ColumnInfo(name = "address")
    private String address;

    @ColumnInfo(name = "city_id")
    private int city_id;

    @ColumnInfo(name = "medical_speciality_id")
    private int medical_speciality_id;

    @ColumnInfo(name = "available")
    private boolean available;

    @NotNull
    @ColumnInfo(name = "medical_personnel_id")
    private int medical_personnel_id;

    @ColumnInfo(name = "expires")
    private String expires;

    @ColumnInfo(name = "price")
    private String price;


    public Medic(@NotNull String name, @NotNull String last_name, String gender, @NotNull String email, String document_number, String address, int city_id, int medical_speciality_id, int medical_personnel_id) {
        this.name = name;
        this.last_name = last_name;
        this.gender = gender;
        this.email = email;
        this.document_number = document_number;
        this.address = address;
        this.city_id = city_id;
        this.medical_speciality_id = medical_speciality_id;
        this.medical_personnel_id = medical_personnel_id;
        this.available = false;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }


    @NotNull
    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    @NotNull
    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(@NotNull String last_name) {
        this.last_name = last_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @NotNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NotNull String email) {
        this.email = email;
    }

    public String getDocument_number() {
        return document_number;
    }

    public void setDocument_number(String document_number) {
        this.document_number = document_number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public int getMedical_speciality_id() {
        return medical_speciality_id;
    }

    public void setMedical_speciality_id(int medical_speciality_id) {
        this.medical_speciality_id = medical_speciality_id;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getMedical_personnel_id() {
        return medical_personnel_id;
    }

    public void setMedical_personnel_id(int medical_personnel_id) {
        this.medical_personnel_id = medical_personnel_id;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFullName(){
        return this.name+" "+this.last_name;
    }
}
