package com.example.healthsense.db;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.healthsense.db.dao.DeviceUsersDAO;
import com.example.healthsense.db.dao.DocumentTypeDAO;
import com.example.healthsense.db.dao.ExercisesDAO;
import com.example.healthsense.db.dao.MedicDAO;
import com.example.healthsense.db.dao.MedicalPersonnelDAO;
import com.example.healthsense.db.dao.UsersDAO;
import com.example.healthsense.db.dao.WorkoutsDAO;
import com.example.healthsense.db.dao.WorkoutsExercisesDAO;
import com.example.healthsense.db.dao.WorkoutsReportDAO;
import com.example.healthsense.db.dao.WorkoutDoneDAO;

import com.example.healthsense.db.entity.Cities;
import com.example.healthsense.db.entity.Countries;
import com.example.healthsense.db.entity.DeviceUsers;
import com.example.healthsense.db.entity.Diseases;
import com.example.healthsense.db.entity.DocumentType;
import com.example.healthsense.db.entity.Exercises;
import com.example.healthsense.db.entity.HeartRateSignals;
import com.example.healthsense.db.entity.Insurances;
import com.example.healthsense.db.entity.Languages;
import com.example.healthsense.db.entity.Medic;
import com.example.healthsense.db.entity.MedicalLanguages;
import com.example.healthsense.db.entity.MedicalPersonnel;
import com.example.healthsense.db.entity.MedicalSpecialities;
import com.example.healthsense.db.entity.Patients;
import com.example.healthsense.db.entity.PhoneNumbers;
import com.example.healthsense.db.entity.States;
import com.example.healthsense.db.entity.UserDiseases;
import com.example.healthsense.db.entity.Users;
import com.example.healthsense.db.entity.WorkoutDone;
import com.example.healthsense.db.entity.WorkoutExercises;
import com.example.healthsense.db.entity.WorkoutReports;
import com.example.healthsense.db.entity.Workouts;

/**
 * Serverless programado bajo la tecnologia Room.
 */

//Agregar todas las tablas en entities
@Database(entities = {
        Cities.class, Countries.class, DeviceUsers.class, Diseases.class,
        DocumentType.class, Exercises.class, HeartRateSignals.class, Insurances.class,
        Languages.class, MedicalLanguages.class, MedicalPersonnel.class, MedicalSpecialities.class,
        Patients.class, PhoneNumbers.class, States.class, UserDiseases.class, Users.class, WorkoutExercises.class,
        WorkoutReports.class, Workouts.class, Medic.class
}, version = 12, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "com.example.healthsense";

    private static AppDatabase INSTANCE;

     //Dao
    public abstract UsersDAO usersDAO();
    public abstract DeviceUsersDAO deviceUsersDAO();
    public abstract MedicalPersonnelDAO medicalPersonnelDAO();
    public abstract DocumentTypeDAO documentTypeDAO();
    public abstract WorkoutsDAO workoutsDAO();
    public abstract WorkoutsExercisesDAO workoutsExercisesDAO();
    public abstract ExercisesDAO exercisesDAO();
    public abstract WorkoutsReportDAO workoutsReportDAO();
    public abstract WorkoutDoneDAO workoutDoneDAO();
    public abstract MedicDAO medicDAO();

    /**
     * Instanciacion del objeto base de datos.
     * @param context
     * @return
     */
    public static synchronized AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context, AppDatabase.class, DB_NAME)
                            .addCallback(roomCallback)
                            .fallbackToDestructiveMigration()// para realizar modificaciones en las tablas
                            .build();
        }
        return INSTANCE;
    }

    /**
     * Destruir instancia de la base de datos.
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }

    /**
     * Eliminar toda la informacion de las tablas.
     */
    public static void clearAll(){ INSTANCE.clearAllTables();}


    /**
     * Pre-cargar tipos de documentos.
     */
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(INSTANCE).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private DocumentTypeDAO documentTypeDAO;
        private PopulateDbAsyncTask(AppDatabase db) {
            documentTypeDAO = db.documentTypeDAO();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            documentTypeDAO.insert(new DocumentType("DNI"));
            documentTypeDAO.insert(new DocumentType("L.E"));
            documentTypeDAO.insert(new DocumentType("CARNET EXT."));
            documentTypeDAO.insert(new DocumentType("RUC"));
            documentTypeDAO.insert(new DocumentType("P. NACIONAL"));
            documentTypeDAO.insert(new DocumentType("PASAPORTE"));
            documentTypeDAO.insert(new DocumentType("OTROS"));
            return null;
        }
    }



}
