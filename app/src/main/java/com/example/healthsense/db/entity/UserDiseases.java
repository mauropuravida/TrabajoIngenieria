package com.example.healthsense.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName="User_Diseases" , indices =
        {@Index("fk_Device_users_has_Diseases_Diseases1_idx"), @Index(value = {"disease_id"}),
         @Index("fk_Device_users_has_Diseases_Device_users1_idx"), @Index(value = {"device_user_id"}),
         @Index("id_UNIQUE"), @Index(value = "id", unique = true)})
public class UserDiseases {

    @NotNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NotNull
    @ForeignKey(entity = DeviceUsers.class, parentColumns = "id", childColumns = "device_user_id",
            onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)
    @ColumnInfo(name="device_user_id")
    private int device_user_id;

    @NotNull
    @ForeignKey(entity = Diseases.class, parentColumns = "id", childColumns = "disease_id",
            onDelete = ForeignKey.RESTRICT, onUpdate = ForeignKey.CASCADE)
    @ColumnInfo(name="disease_id")
    private int disease_id;

    public UserDiseases(int device_user_id, int disease_id) {
        this.device_user_id = device_user_id;
        this.disease_id = disease_id;
    }

    public int getDevice_user_id() {
        return device_user_id;
    }

    public int getDisease_id() {
        return disease_id;
    }
}
