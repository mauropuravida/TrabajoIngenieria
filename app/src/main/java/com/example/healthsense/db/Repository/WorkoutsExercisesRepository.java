package com.example.healthsense.db.Repository;

import android.app.Application;
import android.os.AsyncTask;

import com.example.healthsense.db.AppDatabase;
import com.example.healthsense.db.dao.WorkoutsExercisesDAO;
import com.example.healthsense.db.entity.WorkoutExercises;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Repositorio WorkoutsExercises encargado de comunicar DAO con fragment/activity.
 */

public class WorkoutsExercisesRepository {
    private WorkoutsExercisesDAO workoutsExercisesDAO;
    List<WorkoutExercises> workoutExercises;

    public WorkoutsExercisesRepository(Application application) {
        AppDatabase database = AppDatabase.getAppDatabase(application);
        workoutsExercisesDAO = database.workoutsExercisesDAO();
        try {
            workoutExercises =  new TaskGetAllWorkouts(workoutsExercisesDAO).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

    public List<WorkoutExercises> getAll(){ return workoutsExercisesDAO.getAll();}

    public boolean existExercisesWorkout(int id_workout, int id_exercises){ return workoutsExercisesDAO.existExerciseWorkout(id_workout,id_exercises);}

    public WorkoutExercises getWorkoutExercises(int id){return workoutsExercisesDAO.getWorkoutsExercises(id);}

    public List<WorkoutExercises> getWorkouts(int id){ return workoutsExercisesDAO.getWorkouts(id);}

    public List<WorkoutExercises> getExercises(int id){ return workoutsExercisesDAO.getExercises(id);}

    public void deleteAll(){ new DeleteAll(workoutsExercisesDAO).execute();}

    public String getTime(int workout_id, int exercise_id) { return workoutsExercisesDAO.getTime(workout_id,exercise_id);
    }

    private static class DeleteAll extends AsyncTask<Void, Void, Void> {
        private WorkoutsExercisesDAO workoutsExercisesDAO;

        private DeleteAll(WorkoutsExercisesDAO workoutsExercisesDAO) {
            this.workoutsExercisesDAO = workoutsExercisesDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            workoutsExercisesDAO.deleteAll();
            return null;
        }
    }

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

    private static class TaskGetAllWorkouts extends AsyncTask<Void, Void, List<WorkoutExercises>> {

        private WorkoutsExercisesDAO workoutsExercisesDAO;

        private TaskGetAllWorkouts(WorkoutsExercisesDAO workoutsExercisesDAO) {
            this.workoutsExercisesDAO = workoutsExercisesDAO;
        }

        @Override
        protected List<WorkoutExercises> doInBackground(Void... voids) {
            return workoutsExercisesDAO.getAll();
        }

    }


}
