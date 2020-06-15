package com.example.healthsense.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Device_Users")
public class DeviceUser {

    @PrimaryKey
    int id;

    @ForeignKey(entity = Users.class, parentColumns = "id", childColumns = "user_id",
                onUpdate = ForeignKey.CASCADE, onDelete = ForeignKey.CASCADE)
    @ColumnInfo(name = "user_id")
    private int user_id;

    @ColumnInfo(name="weight")
    private float weight;

    @ColumnInfo(name="height")
    private float height;

//    @ForeignKey(entity = Insurances.class, parentColumns = "id", childColumns = "insurance_id")
//    @ColumnInfo(name="insurance_id")
//    private int insurance_id;

    @ColumnInfo(name="insurance_number")
    private String insurance_number;

    @ColumnInfo(name="heart_rate_signal_threshold")
    private int heart_rate_signal_threshold;

    public DeviceUser(int id, int user_id, float weight, float height, String insurance_number, int heart_rate_signal_threshold) {
        this.id = id;
        this.user_id = user_id;
        this.weight = weight;
        this.height = height;
        this.insurance_number = insurance_number;
        this.heart_rate_signal_threshold = heart_rate_signal_threshold;
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

    public int getHeart_rate_signal_threshold() {
        return heart_rate_signal_threshold;
    }
}
