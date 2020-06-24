package com.example.healthsense.db.Repository;

import android.app.Application;
import android.os.AsyncTask;

import com.example.healthsense.db.AppDatabase;
import com.example.healthsense.db.dao.WorkoutsExercisesDAO;
import com.example.healthsense.db.entity.WorkoutExercises;

import java.util.List;

public class WorkoutsExercisesRepository {
    private WorkoutsExercisesDAO workoutsExercisesDAO;

    public WorkoutsExercisesRepository(Application application) {
        AppDatabase database = AppDatabase.getAppDatabase(application);
        workoutsExercisesDAO = database.workoutsExercisesDAO();
    }

    public void insert(WorkoutExercises workouts) {
        new InsertUserAsyncTask(workoutsExercisesDAO).execute(workouts);
    }

    public void update(WorkoutExercises workouts) {
        new UpdateUserAsyncTask(workoutsExercisesDAO).execute(workouts);
    }

    public void delete(WorkoutExercises workouts) {
        new DeleteUsersyncTask(workoutsExercisesDAO).execute(workouts);
    }

    public WorkoutExercises getWorkoutExercises(int id){return workoutsExercisesDAO.getWorkoutsExercises(id);}

    public List<WorkoutExercises> getWorkouts(int id){ return workoutsExercisesDAO.getWorkouts(id);}

    public List<WorkoutExercises> getExercises(int id){ return workoutsExercisesDAO.getExercises(id);}

    private static class InsertUserAsyncTask extends AsyncTask<WorkoutExercises, Void, Void> {
        private WorkoutsExercisesDAO workoutsExercisesDAO;

        private InsertUserAsyncTask(WorkoutsExercisesDAO workoutsExercisesDAO) {
            this.workoutsExercisesDAO = workoutsExercisesDAO;
        }

        @Override
        protected Void doInBackground(WorkoutExercises... workouts) {
            workoutsExercisesDAO.insert(workouts[0]);
            return null;
        }
    }

    private static class UpdateUserAsyncTask extends AsyncTask<WorkoutExercises, Void, Void> {
        private WorkoutsExercisesDAO workoutsExercisesDAO;

        private UpdateUserAsyncTask(WorkoutsExercisesDAO workoutsExercisesDAO) {
            this.workoutsExercisesDAO = workoutsExercisesDAO;
        }

        @Override
        protected Void doInBackground(WorkoutExercises... workouts) {
            workoutsExercisesDAO.update(workouts[0]);
            return null;
        }
    }

    private static class DeleteUsersyncTask extends AsyncTask<WorkoutExercises, Void, Void> {
        private WorkoutsExercisesDAO workoutsExercisesDAO;

        private DeleteUsersyncTask(WorkoutsExercisesDAO workoutsExercisesDAO) {
            this.workoutsExercisesDAO = workoutsExercisesDAO;
        }

        @Override
        protected Void doInBackground(WorkoutExercises... workouts) {
            workoutsExercisesDAO.delete(workouts[0]);
            return null;
        }
    }


}
