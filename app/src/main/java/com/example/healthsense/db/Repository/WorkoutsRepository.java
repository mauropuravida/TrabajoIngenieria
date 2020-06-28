package com.example.healthsense.db.Repository;

import android.app.Application;
import android.os.AsyncTask;

import com.example.healthsense.db.AppDatabase;
import com.example.healthsense.db.dao.WorkoutsDAO;
import com.example.healthsense.db.entity.Workouts;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class WorkoutsRepository {
    private WorkoutsDAO workoutsDAO;
    private List<Workouts> workouts;

    public WorkoutsRepository(Application application) {
        AppDatabase database = AppDatabase.getAppDatabase(application);
        workoutsDAO = database.workoutsDAO();
        try {
            workouts =  new TaskGetAllWorkouts(workoutsDAO).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

    public int getWourkoutIdRoom(int id_backend){ return workoutsDAO.obtainWorkoutIdRoom(id_backend);}

    public void deleteAll(){ new DeleteAll(workoutsDAO).execute();}

    public Workouts getWorkoutFromIdBackend(int id_backend) { return workoutsDAO.getWorkoutFromIdBackend(id_backend);
    }

    private static class DeleteAll extends AsyncTask<Void, Void, Void> {
        private WorkoutsDAO workoutsDAO;

        private DeleteAll(WorkoutsDAO workoutsDAO) {
            this.workoutsDAO = workoutsDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            workoutsDAO.deleteAll();
            return null;
        }
    }

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

    private static class TaskGetAllWorkouts extends AsyncTask<Void, Void, List<Workouts>> {

        private WorkoutsDAO workoutsDAO;

        private TaskGetAllWorkouts(WorkoutsDAO workoutsDAO) {
            this.workoutsDAO = workoutsDAO;
        }

        @Override
        protected List<Workouts> doInBackground(Void... voids) {
            return workoutsDAO.getAll();
        }

    }


}

