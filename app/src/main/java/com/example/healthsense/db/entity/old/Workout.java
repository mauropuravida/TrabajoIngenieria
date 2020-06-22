package com.example.healthsense.db.entity.old;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "Workout")
public class Workout {

    @PrimaryKey
    @ColumnInfo(name= "id_wk")
    private int id_wk;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "creation_date")
    private String creation_date;

    @ColumnInfo(name = "difficulty")
    private int difficulty;

    @ColumnInfo(name = "price")
    private double price;

    @ColumnInfo(name = "done")
    private int done;

    @ColumnInfo(name = "rating")
    private int rating;

//    public Workout(int id_wk, String name, String creation_date, int difficulty, double price, int done, int rating) {
//        this.id_wk = id_wk;
//        this.name = name;
//        this.creation_date = creation_date;
//        this.difficulty = difficulty;
//        this.price = price;
//        this.done = done;
//        this.rating = rating;
//    }

    public Workout(int id_wk, String name, String creation_date, int difficulty, double price, int done) {
        this.id_wk = id_wk;
        this.name = name;
        this.creation_date = creation_date;
        this.difficulty = difficulty;
        this.price = price;
        this.done = done;
    }

    public int getId_wk() {
        return id_wk;
    }

    public void setId_wk(int id_wk) {
        this.id_wk = id_wk;
    }

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
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
