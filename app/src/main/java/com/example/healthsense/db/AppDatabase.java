package com.example.healthsense.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.healthsense.db.dao.WorkoutDAO;
import com.example.healthsense.db.dao.WorkoutDoneDAO;
import com.example.healthsense.db.dao.WorkoutReportDAO;
import com.example.healthsense.db.entity.old.Workout;
import com.example.healthsense.db.entity.old.WorkoutReport;

//Agregar todas las tablas en entities
@Database(entities = {Workout.class, WorkoutReport.class}, version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "com.example.healthsense";

    private static AppDatabase INSTANCE;

    //Agregar todos los DAO
    public abstract WorkoutDAO workoutDAO();
    public abstract WorkoutReportDAO workoutReportDAO();
    public abstract WorkoutDoneDAO workoutDoneDAO();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context, AppDatabase.class, DB_NAME)
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
