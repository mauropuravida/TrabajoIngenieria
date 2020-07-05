package com.example.healthsense.db.Repository;

import android.app.Application;
import android.os.AsyncTask;

import com.example.healthsense.db.AppDatabase;
import com.example.healthsense.db.dao.MedicalPersonnelDAO;
import com.example.healthsense.db.entity.MedicalPersonnel;

/**
 * Repositorio MedicalPersonnel encargado de comunicar DAO con fragment/activity.
 */

public class MedicalPersonnelRepository {
    private MedicalPersonnelDAO medicalPersonnelDAO;

    public MedicalPersonnelRepository(Application application) {
        AppDatabase database = AppDatabase.getAppDatabase(application);
        medicalPersonnelDAO = database.medicalPersonnelDAO();
    }

    public void insert(MedicalPersonnel medeicalPersonnel) {
        new InsertUserAsyncTask(medicalPersonnelDAO).execute(medeicalPersonnel);
    }

    public void update(MedicalPersonnel medeicalPersonnel) {
        new UpdateUserAsyncTask(medicalPersonnelDAO).execute(medeicalPersonnel);
    }

    public void delete(MedicalPersonnel medeicalPersonnel) {
        new DeleteUsersyncTask(medicalPersonnelDAO).execute(medeicalPersonnel);
    }

    private static class InsertUserAsyncTask extends AsyncTask<MedicalPersonnel, Void, Void> {
        private MedicalPersonnelDAO medicalPersonnelDAO;

        private InsertUserAsyncTask(MedicalPersonnelDAO medicalPersonnelDAO) {
            this.medicalPersonnelDAO = medicalPersonnelDAO;
        }

        @Override
        protected Void doInBackground(MedicalPersonnel... medicals) {
            medicalPersonnelDAO.insert(medicals[0]);
            return null;
        }
    }

    private static class UpdateUserAsyncTask extends AsyncTask<MedicalPersonnel, Void, Void> {
        private MedicalPersonnelDAO medicalPersonnelDAO;

        private UpdateUserAsyncTask(MedicalPersonnelDAO medicalPersonnelDAO) {
            this.medicalPersonnelDAO = medicalPersonnelDAO;
        }

        @Override
        protected Void doInBackground(MedicalPersonnel... medicals) {
            medicalPersonnelDAO.update(medicals[0]);
            return null;
        }
    }

    private static class DeleteUsersyncTask extends AsyncTask<MedicalPersonnel, Void, Void> {
        private MedicalPersonnelDAO medicalPersonnelDAO;

        private DeleteUsersyncTask(MedicalPersonnelDAO medicalPersonnelDAO) {
            this.medicalPersonnelDAO = medicalPersonnelDAO;
        }

        @Override
        protected Void doInBackground(MedicalPersonnel... medicals) {
            medicalPersonnelDAO.delete(medicals[0]);
            return null;
        }
    }

}
