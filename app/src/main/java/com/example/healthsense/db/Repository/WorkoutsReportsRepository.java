package com.example.healthsense.db.Repository;

import android.app.Application;
import android.os.AsyncTask;

import com.example.healthsense.db.AppDatabase;
import com.example.healthsense.db.dao.WorkoutsReportDAO;
import com.example.healthsense.db.entity.WorkoutReports;

import java.util.List;

/**
 * Repositorio WorkoutsReports encargado de comunicar DAO con fragment/activity.
 */

public class WorkoutsReportsRepository {
    private WorkoutsReportDAO workoutsReportDAO;

    public WorkoutsReportsRepository(Application application) {
        AppDatabase database = AppDatabase.getAppDatabase(application);
        workoutsReportDAO = database.workoutsReportDAO();
    }

    public void insert(WorkoutReports workoutReports) {
       new InsertUserAsyncTask(workoutsReportDAO).execute(workoutReports);
    }

    public void update(WorkoutReports workoutReports) {
        new UpdateUserAsyncTask(workoutsReportDAO).execute(workoutReports);
    }

    public void delete(WorkoutReports workoutReports) {
        new DeleteUsersyncTask(workoutsReportDAO).execute(workoutReports);
    }


    public List<WorkoutReports> getWorkoutsReportsId(int id) {
        return workoutsReportDAO.getWorkoutsId(id);
    }

    public int size(){
        return workoutsReportDAO.getSize();
    }

    public WorkoutReports getWorkoutReports(int id){ return workoutsReportDAO.getWorkoutReport(id);}

    public WorkoutReports getWorkoutReportsBackend(int id_backend){ return workoutsReportDAO.getWorkoutReportBackend(id_backend);}

    public List<WorkoutReports> getWorkoutsReports() {
        return workoutsReportDAO.getWorkoutsReports();
    }

    public boolean contains(int id, String date) {
        return workoutsReportDAO.existWorkoutReport(id, date);
    }

    public List<WorkoutReports> getUnsent(){
        return workoutsReportDAO.getUnsent();
    }

    public boolean isSent(int id){
        return workoutsReportDAO.isSent(id);
    }

    private static class InsertUserAsyncTask extends AsyncTask<WorkoutReports, Void, Void> {
        private WorkoutsReportDAO workoutsReportDAODAO;

        private InsertUserAsyncTask(WorkoutsReportDAO workoutsReportDAODAO) {
            this.workoutsReportDAODAO = workoutsReportDAODAO;
        }

        @Override
        protected Void doInBackground(WorkoutReports... workoutReports) {
            workoutsReportDAODAO.insert(workoutReports[0]);
            return null;
        }
    }

    private static class UpdateUserAsyncTask extends AsyncTask<WorkoutReports, Void, Void> {
        private WorkoutsReportDAO workoutsReportDAO;

        private UpdateUserAsyncTask(WorkoutsReportDAO workoutsReportDAO) {
            this.workoutsReportDAO = workoutsReportDAO;
        }

        @Override
        protected Void doInBackground(WorkoutReports... workoutReports) {
            workoutsReportDAO.update(workoutReports[0]);
            return null;
        }
    }

    private static class DeleteUsersyncTask extends AsyncTask<WorkoutReports, Void, Void> {
        private WorkoutsReportDAO workoutsReportDAO;

        private DeleteUsersyncTask(WorkoutsReportDAO workoutsReportDAO) {
            this.workoutsReportDAO = workoutsReportDAO;
        }

        @Override
        protected Void doInBackground(WorkoutReports... workoutReports) {
            workoutsReportDAO.delete(workoutReports[0]);
            return null;
        }
    }
}
