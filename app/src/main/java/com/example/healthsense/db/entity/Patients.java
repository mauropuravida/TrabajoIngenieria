package com.example.healthsense.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

/**
 * Tabla Patients Room
 */


@Entity(tableName="Patients" , indices =
        {@Index(name ="fk_Medical_Personnel_has_Device_Users_Device_Users1_idx",value = {"device_user_id"}),
         @Index(name ="fk_Medical_Personnel_has_Device_Users_Medical_Personnel1_idx", value = {"medical_personnel_id"}),
         @Index(name ="id_UNIQUE_patient", value = "id", unique = true)})
public class Patients {

    @NotNull
    @PrimaryKey(autoGenerate = true)
    int id;

    @NotNull
    @ForeignKey(entity = MedicalPersonnel.class, parentColumns = "id", childColumns = "medical_personnel_id",
            onUpdate = ForeignKey.NO_ACTION, onDelete = ForeignKey.NO_ACTION)
    @ColumnInfo(name = "medical_personnel_id")
    private int medical_personnel_id;

    @NotNull
    @ForeignKey(entity = DeviceUsers.class, parentColumns = "id", childColumns = "device_user_id",
            onUpdate = ForeignKey.NO_ACTION, onDelete = ForeignKey.NO_ACTION)
    @ColumnInfo(name = "device_user_id")
    private int device_user_id;

    public Patients(int medical_personnel_id, int device_user_id) {
        this.medical_personnel_id = medical_personnel_id;
        this.device_user_id = device_user_id;
    }

    public int getMedical_personnel_id() {
        return medical_personnel_id;
    }

    public int getDevice_user_id() {
        return device_user_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMedical_personnel_id(int medical_personnel_id) {
        this.medical_personnel_id = medical_personnel_id;
    }

    public void setDevice_user_id(int device_user_id) {
        this.device_user_id = device_user_id;
    }
}
