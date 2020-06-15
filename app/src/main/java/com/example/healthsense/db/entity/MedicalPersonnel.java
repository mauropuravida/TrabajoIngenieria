package com.example.healthsense.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName="Medical_Personnel")
public class MedicalPersonnel {

    @PrimaryKey
    private int id;

    @ForeignKey(entity = Users.class, parentColumns = "id", childColumns = "user_id",
            onDelete = ForeignKey.RESTRICT, onUpdate = ForeignKey.CASCADE)
    @ColumnInfo(name="user_id")
    private int user_id;

//    @ForeignKey(entity = MedicalSpecialites.class, parentColumns = "id", childColumns = "medical_speciality_id",
//            onUpdate = ForeignKey.CASCADE)
//    @ColumnInfo(name="medical_speciality_id")
//    private int medical_speciality_id;

    @ColumnInfo(name="available") // ver esto porque es BIT.
    private boolean available;

    @ColumnInfo(name="mp_public_key")
    private String mp_public_key;

    @ColumnInfo(name="mp_access_token")
    private String mp_access_token;

    public MedicalPersonnel(int id, int user_id, boolean available, String mp_public_key, String mp_access_token) {
        this.id = id;
        this.user_id = user_id;
        this.available = available;
        this.mp_public_key = mp_public_key;
        this.mp_access_token = mp_access_token;
    }

    public int getUser_id() {
        return user_id;
    }

    public boolean isAvailable() {
        return available;
    }

    public String getMp_public_key() {
        return mp_public_key;
    }

    public String getMp_access_token() {
        return mp_access_token;
    }
}
