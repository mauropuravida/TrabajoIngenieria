package com.example.healthsense.db.Repository;

import android.app.Application;
import android.os.AsyncTask;

import com.example.healthsense.db.AppDatabase;
import com.example.healthsense.db.dao.WorkoutsReportDAO;
import com.example.healthsense.db.entity.WorkoutReports;

import java.util.List;

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


    public List<WorkoutReports> getWorkoutsReportsId(int id){ return workoutsReportDAO.getWorkoutsId(id);}

    public List<WorkoutReports> getWorkoutsReports(){ return workoutsReportDAO.getWorkoutsReports();}

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
