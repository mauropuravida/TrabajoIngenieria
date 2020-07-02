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
            workouts = new TaskGetAllWorkouts(workoutsDAO).execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void insert(Workouts workouts) {
        new InsertUserAsyncTask(workoutsDAO).execute(workouts);
    }

    public void update(Workouts workouts) {
        new UpdateUserAsyncTask(workoutsDAO).execute(workouts);
    }

    public void updateDone(int done, int id, int rating) {
        System.out.println("done " + done + " id  " + id + "  rating" + rating);
        new TaskUpdateDone(workoutsDAO,done,id,rating).execute();
    }

    public void delete(Workouts workouts) {
        new DeleteUsersyncTask(workoutsDAO).execute(workouts);
    }

    public Workouts getWorkout(int id) {
        return workoutsDAO.getWorkout(id);
    }

    public List<Workouts> getWorkoutsDevice(int id) {
        return workoutsDAO.getWorkoutsDevice(id);
    }

    public List<Workouts> getWorkoutsMedical(int id) {
        return workoutsDAO.getWorkoutsMedical(id);
    }

    public List<Workouts> getAll() {
        return workouts;
    }

    public int getWourkoutIdRoom(int id_backend) {
        return workoutsDAO.obtainWorkoutIdRoom(id_backend);
    }

    public void deleteAll() {
        new DeleteAll(workoutsDAO).execute();
    }

    public Workouts getWorkoutFromIdBackend(int id_backend) {
        return workoutsDAO.getWorkoutFromIdBackend(id_backend);
    }

    public boolean contains(int id) {
        return workoutsDAO.existWorkout(id);
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

    private static class TaskUpdateDone extends AsyncTask<Void, Void, Void> {

        private WorkoutsDAO workoutsDAO;
        private int done,id,rating;

        private TaskUpdateDone(WorkoutsDAO workoutsDAO, int done, int id, int rating) {
            this.workoutsDAO = workoutsDAO;
            this.done = done;
            this.id = id;
            this.rating = rating;
        }

        @Override
        protected Void doInBackground(Void... voids) {
             workoutsDAO.updateDone(done,id,rating);
             return null;
        }

    }


}

