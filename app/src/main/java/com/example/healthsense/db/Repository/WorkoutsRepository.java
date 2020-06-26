package com.example.healthsense.db.Repository;

import android.app.Application;
import android.os.AsyncTask;

import com.example.healthsense.db.AppDatabase;
import com.example.healthsense.db.dao.WorkoutsDAO;
import com.example.healthsense.db.entity.Workouts;

import java.util.List;

public class WorkoutsRepository {
    private WorkoutsDAO workoutsDAO;
    private List<Workouts> workouts;

    public WorkoutsRepository(Application application) {
        AppDatabase database = AppDatabase.getAppDatabase(application);
        workoutsDAO = database.workoutsDAO();
        workouts = workoutsDAO.getAll();
    }

    public void insert(Workouts workouts) {
        new InsertUserAsyncTask(workoutsDAO).execute(workouts);
    }

    public void update(Workouts workouts) {
        new UpdateUserAsyncTask(workoutsDAO).execute(workouts);
    }

    public void update(boolean done, int id){
        workoutsDAO.updateDone(done,id);
    }

    public void delete(Workouts workouts) {
        new DeleteUsersyncTask(workoutsDAO).execute(workouts);
    }

    public Workouts getWorkout(int id){return workoutsDAO.getWorkout(id);}

    public List<Workouts> getWorkoutsDevice(int id){ return workoutsDAO.getWorkoutsDevice(id);}

    public List<Workouts> getWorkoutsMedical(int id){ return workoutsDAO.getWorkoutsMedical(id);}

    public List<Workouts> getAll(){ return workouts;}

    private static class InsertUserAsyncTask extends AsyncTask<Workouts, Void, Void> {
        private WorkoutsDAO workoutsDAO;

        private InsertUserAsyncTask(WorkoutsDAO workoutsDAO) {
            this.workoutsDAO = workoutsDAO;
        }

        @Override
        protected Void doInBackground(Workouts... workouts) {
            workoutsDAO.insert(workouts[0]);
            return null;
        }
    }

    private static class UpdateUserAsyncTask extends AsyncTask<Workouts, Void, Void> {
        private WorkoutsDAO workoutsDAO;

        private UpdateUserAsyncTask(WorkoutsDAO workoutsDAO) {
            this.workoutsDAO = workoutsDAO;
        }

        @Override
        protected Void doInBackground(Workouts... workouts) {
            workoutsDAO.update(workouts[0]);
            return null;
        }
    }

    private static class DeleteUsersyncTask extends AsyncTask<Workouts, Void, Void> {
        private WorkoutsDAO workoutsDAO;

        private DeleteUsersyncTask(WorkoutsDAO workoutsDAO) {
            this.workoutsDAO = workoutsDAO;
        }

        @Override
        protected Void doInBackground(Workouts... workouts) {
            workoutsDAO.delete(workouts[0]);
            return null;
        }
    }

}

