package com.example.healthsense.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName="Medical_Personnel" , indices =
        {@Index(name ="fk_Medical_personnel_Medical_specialities1_idx", value = {"medical_speciality_id"}),
         @Index(name ="fk_Medical_Personnel_Users1_idx", value = {"user_id"})})
public class MedicalPersonnel {

    @NotNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NotNull
    @ForeignKey(entity = Users.class, parentColumns = "id", childColumns = "user_id",
            onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)
    @ColumnInfo(name="user_id")
    private int user_id;

    @ForeignKey(entity = MedicalSpecialities.class, parentColumns = "id", childColumns = "medical_speciality_id",
            onUpdate = ForeignKey.CASCADE)
    @ColumnInfo(name="medical_speciality_id")
    private int medical_speciality_id;

    @ColumnInfo(name="available") // ver esto porque es BIT.
    private boolean available;

    @ColumnInfo(name="mp_public_key")
    private String mp_public_key;

    @ColumnInfo(name="mp_access_token")
    private String mp_access_token;


    public MedicalPersonnel(int user_id, int medical_speciality_id) {
        this.medical_speciality_id = medical_speciality_id;
        this.user_id = user_id;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getMedical_speciality_id() {
        return medical_speciality_id;
    }

    public void setMedical_speciality_id(int medical_speciality_id) {
        this.medical_speciality_id = medical_speciality_id;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setMp_public_key(String mp_public_key) {
        this.mp_public_key = mp_public_key;
    }

    public void setMp_access_token(String mp_access_token) {
        this.mp_access_token = mp_access_token;
    }
}
