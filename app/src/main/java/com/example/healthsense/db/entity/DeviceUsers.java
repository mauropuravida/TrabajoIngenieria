package com.example.healthsense.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName="Device_Users" , indices =
        {@Index("fk_Device_users_Insurances1_idx"), @Index(value = {"insurance_id"}),
         @Index("fk_Device_Users_Users1_idx"), @Index(value = {"user_id"}),
         @Index("id_UNIQUE"), @Index(value = "id", unique = true)})
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
    private int insurance_id;

    @ColumnInfo(name="insurance_number")
    private String insurance_number;

    @ColumnInfo(name="heart_rate_signal_threshold")
    private int heart_rate_signal_threshold;

    public DeviceUsers(int user_id, float weight, float height, int insurance_id, String insurance_number, int heart_rate_signal_threshold) {
        this.user_id = user_id;
        this.weight = weight;
        this.height = height;
        this.insurance_id = insurance_id;
        this.insurance_number = insurance_number;
        this.heart_rate_signal_threshold = heart_rate_signal_threshold;
    }

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

    public int getHeart_rate_signal_threshold() {
        return heart_rate_signal_threshold;
    }
}
