package com.example.healthsense.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName="Heart_Rate_Signals" , indices =
        {@Index("fk_Heart_rate_signals_Workout_reports1_idx"), @Index(value = {"workout_report_id"})})
public class HeartRateSignals {

    @NotNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NotNull
    @ForeignKey(entity = WorkoutReports.class, parentColumns = "id", childColumns = "workout_report_id",
            onUpdate = ForeignKey.CASCADE, onDelete = ForeignKey.CASCADE)
    @ColumnInfo(name="workout_report_id")
    private int workout_report_id;

    @NotNull
    @ColumnInfo(name = "time")
    private String time;

    @NotNull
    @ColumnInfo(name = "value")
    private int value;

    public HeartRateSignals(int workout_report_id, @NotNull String time, int value) {
        this.workout_report_id = workout_report_id;
        this.time = time;
        this.value = value;
    }

    public int getWorkout_report_id() {
        return workout_report_id;
    }

    @NotNull
    public String getTime() {
        return time;
    }

    public int getValue() {
        return value;
    }
}
