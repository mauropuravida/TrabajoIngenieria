package com.example.healthsense.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;


import org.jetbrains.annotations.NotNull;


@Entity(tableName="Workout_Exercises" , indices =
        {@Index(name ="fk_Workouts_has_Exercise_Exercise1_idx", value = {"exercise_id"}),
         @Index(name ="fk_Workouts_has_Exercise_Workouts_idx",value = {"workout_id"}),
         @Index(name ="id_UNIQUE_workout_exercise",value = "id", unique = true)})
public class WorkoutExercises {

    @Ignore
    int id_backend;

    @NotNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ForeignKey(entity = Workouts.class, parentColumns = "id", childColumns = "workout_id",
            onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)
    @ColumnInfo(name="workout_id")
    private int workout_id;

    @ForeignKey(entity = Exercises.class, parentColumns = "id", childColumns = "exercise_id",
            onDelete = ForeignKey.RESTRICT, onUpdate = ForeignKey.CASCADE)
    @ColumnInfo(name="exercise_id")
    private int exercise_id;

    @ColumnInfo(name="time")
    private String time;

    public WorkoutExercises(int workout_id, int exercise_id, String time) {
        this.workout_id = workout_id;
        this.exercise_id = exercise_id;
        this.time = time;
    }

    public int getWorkout_id() {
        return workout_id;
    }

    public int getExercise_id() {
        return exercise_id;
    }

    public String getTime() {
        return time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setWorkout_id(int workout_id) {
        this.workout_id = workout_id;
    }

    public void setExercise_id(int exercise_id) {
        this.exercise_id = exercise_id;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getId_backend() {
        return id_backend;
    }

    public void setId_backend(int id_backend) {
        this.id_backend = id_backend;
    }
}
