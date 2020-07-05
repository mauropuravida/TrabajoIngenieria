package com.example.healthsense.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

/**
 * Tabla DeviceUsers Room
 */


@Entity(tableName="Device_Users" , indices =
        {@Index(name ="fk_Device_users_Insurances1_idx", value = {"insurance_id"}),
         @Index(name = "fk_Device_Users_Users1_idx", value = {"user_id"}),
         @Index(name = "id_UNIQUE_device", value = "id", unique = true)})
public class DeviceUsers {

    @NotNull
    @PrimaryKey(autoGenerate = true)
    int id;

    @NotNull
    @ForeignKey(entity = Users.class, parentColumns = "id", childColumns = "user_id",
                onUpdate = ForeignKey.CASCADE, onDelete = ForeignKey.CASCADE)
    @ColumnInfo(name = "user_id")
    private int user_id;

    @ColumnInfo(name="weight")
    private float weight;

    @ColumnInfo(name="height")
    private float height;

    @ForeignKey(entity = Insurances.class, parentColumns = "id", childColumns = "insurance_id",
            onUpdate = ForeignKey.CASCADE, onDelete = ForeignKey.RESTRICT)
    @ColumnInfo(name="insurance_id")
    private Integer insurance_id;

    @ColumnInfo(name="insurance_number")
    private String insurance_number;

    @ColumnInfo(name="heart_rate_signal_threshold")
    private Integer heart_rate_signal_threshold;

    public int getUpload() {
        return upload;
    }

    public void setUpload(int upload) {
        this.upload = upload;
    }

    @ColumnInfo(name="upload")
    private int upload = 0;

    public DeviceUsers(int user_id) {
        this.user_id = user_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public float getWeight() {
        return weight;
    }

    public float getHeight() {
        return height;
    }

    public String getInsurance_number() {
        return insurance_number;
    }

    public Integer getHeart_rate_signal_threshold() {
        return heart_rate_signal_threshold;
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

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public Integer getInsurance_id() {
        return insurance_id;
    }

    public void setInsurance_id(int insurance_id) {
        this.insurance_id = insurance_id;
    }

    public void setInsurance_number(String insurance_number) {
        this.insurance_number = insurance_number;
    }

    public void setHeart_rate_signal_threshold(int heart_rate_signal_threshold) {
        this.heart_rate_signal_threshold = heart_rate_signal_threshold;
    }
}
