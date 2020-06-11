package com.example.healthsense.db.entity;

import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "Workouts")
public class Workout {

    @PrimaryKey
    private int id;

    @ForeignKey(entity = DeviceUser.class, parentColumns = "id", childColumns = "device_user_id",
                onDelete = ForeignKey.RESTRICT, onUpdate = ForeignKey.CASCADE)
    private int device_user_id;

    @ForeignKey(entity = MedicalPersonnel.class, parentColumns = "id", childColumns = "medical_personnel_id",
                onDelete = ForeignKey.RESTRICT, onUpdate = ForeignKey.CASCADE)
    private int medical_personnel_id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "creation_date")
    private String creation_date;

    @ColumnInfo(name = "difficulty")
    private int difficulty;

    @ColumnInfo(name = "price")
    private float price;

    @ColumnInfo(name = "done")
    private int done;

    @ColumnInfo(name = "rating")
    private int rating;

    public Workout(int id_wk, String name, String creation_date, int difficulty, float price, int done, int rating) {
        this.id = id_wk;
        this.name = name;
        this.creation_date = creation_date;
        this.difficulty = difficulty;
        this.price = price;
        this.done = done;
        this.rating = rating;
    }

    public int getId_wk() {
        return id;
    }

    public void setId_wk(int id_wk) {
        this.id = id_wk;
    }

    //    public int getDevice_user_id() {
//        return device_user_id;
//    }
//
//    public void setDevice_user_id(int device_user_id) {
//        this.device_user_id = device_user_id;
//    }
//
//    public int getMedical_personnel_id() {
//        return medical_personnel_id;
//    }
//
//    public void setMedical_personnel_id(int medical_personnel_id) {
//        this.medical_personnel_id = medical_personnel_id;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getDone() {
        return done;
    }

    public void setDone(int done) {
        this.done = done;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
