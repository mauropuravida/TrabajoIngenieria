package com.example.healthsense.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Workout_Exercises")
public class WorkoutExercises {

    @PrimaryKey
    private int id;

    @ForeignKey(entity = Workout.class, parentColumns = "id", childColumns = "workout_id",
            onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)
    @ColumnInfo(name="workout_id")
    private int workout_id;

    @ForeignKey(entity = Exercises.class, parentColumns = "id", childColumns = "exercise_id",
            onDelete = ForeignKey.RESTRICT, onUpdate = ForeignKey.CASCADE)
    @ColumnInfo(name="exercise_id")
    private int exercise_id;

    @ColumnInfo(name="time") // Ver qeue tipo almacenar aca, es TIME.
    private String time;

    public WorkoutExercises(int id, int workout_id, int exercise_id, String time) {
        this.id = id;
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
}
