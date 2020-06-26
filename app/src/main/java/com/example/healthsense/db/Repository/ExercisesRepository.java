package com.example.healthsense.db.Repository;

import android.app.Application;
import android.os.AsyncTask;

import com.example.healthsense.db.AppDatabase;
import com.example.healthsense.db.dao.ExercisesDAO;
import com.example.healthsense.db.entity.Exercises;

import java.util.List;

public class ExercisesRepository {

    private ExercisesDAO exercisesDAO;
    private List<Exercises> exercises;

    public ExercisesRepository(Application application) {
        AppDatabase database = AppDatabase.getAppDatabase(application);
        exercisesDAO = database.exercisesDAO();
        exercises = exercisesDAO.getAll();
    }

    public void insert(Exercises exercises) {
        new InsertUserAsyncTask(exercisesDAO).execute(exercises);
    }

    public void update(Exercises exercises) {
        new UpdateUserAsyncTask(exercisesDAO).execute(exercises);
    }

    public void delete(Exercises exercises) {
        new DeleteUsersyncTask(exercisesDAO).execute(exercises);
    }

    public List<Exercises> getAll(){return this.exercises;}

    public Exercises getExercises(int id){return exercisesDAO.getExercises(id);}

    public int getNumFiles() {
        return exercisesDAO.getCount();
    }

    private static class InsertUserAsyncTask extends AsyncTask<Exercises, Void, Void> {
        private ExercisesDAO exercisesDAO;

        private InsertUserAsyncTask(ExercisesDAO exercisesDAO) {
            this.exercisesDAO = exercisesDAO;
        }

        @Override
        protected Void doInBackground(Exercises... exercises) {
            exercisesDAO.insert(exercises[0]);
            return null;
        }
    }

    private static class UpdateUserAsyncTask extends AsyncTask<Exercises, Void, Void> {
        private ExercisesDAO exercisesDAO;

        private UpdateUserAsyncTask(ExercisesDAO exercisesDAO) {
            this.exercisesDAO = exercisesDAO;
        }

        @Override
        protected Void doInBackground(Exercises... exercises) {
            exercisesDAO.update(exercises[0]);
            return null;
        }
    }

    private static class DeleteUsersyncTask extends AsyncTask<Exercises, Void, Void> {
        private ExercisesDAO exercisesDAO;

        private DeleteUsersyncTask(ExercisesDAO exercisesDAO) {
            this.exercisesDAO = exercisesDAO;
        }

        @Override
        protected Void doInBackground(Exercises... exercises) {
            exercisesDAO.delete(exercises[0]);
            return null;
        }
    }
}
