package com.example.healthsense.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

/**
 * Tabla PhoneNumbers Room
 */


@Entity(tableName="Phone_Numbers" , indices =
        {@Index(name ="fk_Phone_Numbers_Users1_idx", value = {"user_id"}),
         @Index(name ="id_UNIQUE_phone_number" ,value = "id", unique = true)})
public class PhoneNumbers {

    @NotNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NotNull
    @ForeignKey(entity = Users.class, parentColumns = "id", childColumns = "user_id",
            onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)
    @ColumnInfo(name="user_id")
    private int user_id;

    @NotNull
    @ColumnInfo(name="number")
    private String number;

    public PhoneNumbers(int user_id, @NotNull String number) {
        this.user_id = user_id;
        this.number = number;
    }

    public int getUser_id() {
        return user_id;
    }

    @NotNull
    public String getNumber() {
        return number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setNumber(@NotNull String number) {
        this.number = number;
    }
}
