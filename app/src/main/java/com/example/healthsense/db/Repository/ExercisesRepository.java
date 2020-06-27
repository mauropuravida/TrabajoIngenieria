package com.example.healthsense.db.Repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.healthsense.db.AppDatabase;
import com.example.healthsense.db.dao.ExercisesDAO;
import com.example.healthsense.db.entity.Exercises;

import java.util.List;
import java.util.concurrent.ExecutionException;


public class ExercisesRepository {

    private ExercisesDAO exercisesDAO;
    private List<Exercises> exercises;

    public ExercisesRepository(Application application) {
        AppDatabase database = AppDatabase.getAppDatabase(application);
        exercisesDAO = database.exercisesDAO();
        try {
            exercises =  new TaskGetAllExercises(exercisesDAO).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

    public List<Exercises> getAll(){ return exercises;}

    public int getExercisesIdBackend(int id_backend){ return exercisesDAO.obtainExercisesIdRoom(id_backend);}


    public Exercises getExercises(int id){return exercisesDAO.getExercises(id);}

    public int getNumFiles() {
        return exercisesDAO.getCount();
    }

    public void deleteAll() { new DeleteAll(exercisesDAO).execute();
    }

    private static class TaskGetAllExercises extends AsyncTask<Void, Void, List<Exercises>> {

        private ExercisesDAO exercisesDAO;

        private TaskGetAllExercises(ExercisesDAO exercisesDAO) {
            this.exercisesDAO = exercisesDAO;
        }

        @Override
        protected List<Exercises> doInBackground(Void... voids) {
            return exercisesDAO.getAll();
        }

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

    private static class DeleteAll extends AsyncTask<Void, Void, Void> {
        private ExercisesDAO exercisesDAO;

        private DeleteAll(ExercisesDAO exercisesDAO) {
            this.exercisesDAO = exercisesDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            exercisesDAO.deleteAll();
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
