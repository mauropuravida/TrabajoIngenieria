package com.example.healthsense.db.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

/**
 * Tabla MedicalLanguages Room
 */


@Entity(tableName="Medical_Languages" , indices =
        {@Index(name ="fk_Languages_has_Medical_Personnel_Medical_Personnel1_idx",value = {"medical_personnel_id"}),
         @Index(name ="fk_Languages_has_Medical_Personnel_Languages1_idx", value = {"language_id"}),
         @Index(name ="id_UNIQUE_medical_language", value = "id", unique = true)})
public class MedicalLanguages {

    @NotNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NotNull
    @ForeignKey(entity = MedicalPersonnel.class, parentColumns = "id", childColumns = "medical_personnel_id",
            onDelete = ForeignKey.NO_ACTION, onUpdate = ForeignKey.NO_ACTION)
    @ColumnInfo(name="medical_personnel_id")
    private int medical_personnel_id;

    @NotNull
    @ForeignKey(entity = Languages.class, parentColumns = "id", childColumns = "language_id",
            onDelete = ForeignKey.NO_ACTION, onUpdate = ForeignKey.NO_ACTION)
    @ColumnInfo(name="language_id")
    private int language_id;

    public MedicalLanguages(int medical_personnel_id, int language_id) {
        this.medical_personnel_id = medical_personnel_id;
        this.language_id = language_id;
    }

    public int getMedical_personnel_id() {
        return medical_personnel_id;
    }

    public int getLanguage_id() {
        return language_id;
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

    public void setLanguage_id(int language_id) {
        this.language_id = language_id;
    }
}
