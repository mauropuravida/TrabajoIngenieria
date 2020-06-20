package com.example.healthsense.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName="Workouts" , indices =
        {@Index("fk_Workouts_Device_users1_idx"), @Index(value = {"device_user_id"}),
         @Index("fk_Workouts_Medical_personnel1_idx"), @Index(value = {"medical_personnel_id"}),
         @Index("id_UNIQUE"), @Index(value = "id", unique = true)})
public class Workouts {

    @NotNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NotNull
    @ForeignKey(entity = MedicalPersonnel.class, parentColumns = "id", childColumns = "medical_personnel_id",
            onUpdate = ForeignKey.CASCADE, onDelete = ForeignKey.CASCADE)
    @ColumnInfo(name = "medical_personnel_id")
    private int medical_personnel_id;

    @NotNull
    @ForeignKey(entity = DeviceUsers.class, parentColumns = "id", childColumns = "device_user_id",
            onUpdate = ForeignKey.CASCADE, onDelete = ForeignKey.CASCADE)
    @ColumnInfo(name = "device_user_id")
    private int device_user_id;

    @NotNull
    @ColumnInfo(name = "name")
    private String name;

    @NotNull
    @ColumnInfo(name = "creation_date")
    private String creation_date;

    @NotNull
    @ColumnInfo(name = "difficulty")
    private int difficulty;

    @NotNull
    @ColumnInfo(name = "price")
    private double price = 0.0;

    @NotNull
    @ColumnInfo(name = "done")
    private int done;

    @ColumnInfo(name = "rating")
    private int rating;

    public Workouts(int medical_personnel_id, int device_user_id, @NotNull String name, @NotNull String creation_date, int difficulty, double price, int done, int rating) {
        this.medical_personnel_id = medical_personnel_id;
        this.device_user_id = device_user_id;
        this.name = name;
        this.creation_date = creation_date;
        this.difficulty = difficulty;
        this.price = price;
        this.done = done;
        this.rating = rating;
    }

    public int getMedical_personnel_id() {
        return medical_personnel_id;
    }

    public int getDevice_user_id() {
        return device_user_id;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public String getCreation_date() {
        return creation_date;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public double getPrice() {
        return price;
    }

    public int getDone() {
        return done;
    }

    public int getRating() {
        return rating;
    }
}
