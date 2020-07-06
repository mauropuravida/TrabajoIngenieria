package com.example.healthsense.db.Repository;


import android.app.Application;
import android.os.AsyncTask;

import com.example.healthsense.db.AppDatabase;
import com.example.healthsense.db.dao.MedicDAO;
import com.example.healthsense.db.dao.WorkoutsDAO;
import com.example.healthsense.db.entity.Medic;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Repositorio Medic encargado de comunicar DAO con fragment/activity.
 */


public class MedicRepository {

    private MedicDAO medicDAO;

    public MedicRepository(Application application){
        AppDatabase database = AppDatabase.getAppDatabase(application);
        medicDAO = database.medicDAO();
    }

    public void insert(Medic medic) {
        new InsertMedicAsyncTask(medicDAO).execute(medic);
    }

    public void update(Medic medic) {
        new UpdateMedicAsyncTask(medicDAO).execute(medic);
    }

    public void delete(Medic medic) {
        new DeleteMedicAsyncTask(medicDAO).execute(medic);
    }

    public void deleteAll() {
        new DeleteAll(medicDAO).execute();
    }


    public Medic getMedic(int id){ return medicDAO.getMedic(id);}

    public boolean contains(int id) {
        return medicDAO.exists(id);
    }

    public List<Medic> getAll(){
        try {
            return new TaskGetAllMedics(medicDAO).execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class DeleteAll extends AsyncTask<Void, Void, Void> {
        private MedicDAO medicDAO;

        private DeleteAll(MedicDAO medicDAO) {
            this.medicDAO = medicDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            medicDAO.deleteAll();
            return null;
        }
    }


    private static class InsertMedicAsyncTask extends AsyncTask<Medic, Void, Void> {
        private MedicDAO medicDAO;

        private InsertMedicAsyncTask(MedicDAO medicDAO) {
            this.medicDAO = medicDAO;
        }

        @Override
        protected Void doInBackground(Medic... medics) {
            medicDAO.insert(medics[0]);
            return null;
        }
    }

    private static class UpdateMedicAsyncTask extends AsyncTask<Medic, Void, Void> {
        private MedicDAO medicDAO;

        private UpdateMedicAsyncTask(MedicDAO medicDAO) {
            this.medicDAO = medicDAO;
        }

        @Override
        protected Void doInBackground(Medic... medics) {
            medicDAO.update(medics[0]);
            return null;
        }
    }

    private static class DeleteMedicAsyncTask extends AsyncTask<Medic, Void, Void> {
        private MedicDAO medicDAO;

        private DeleteMedicAsyncTask(MedicDAO medicDAO) {
            this.medicDAO = medicDAO;
        }

        @Override
        protected Void doInBackground(Medic... medics) {
            medicDAO.delete(medics[0]);
            return null;
        }
    }

    private static class TaskGetAllMedics extends AsyncTask<Void, Void, List<Medic>> {

        private MedicDAO medicDAO;

        private TaskGetAllMedics(MedicDAO medicDAO) {
            this.medicDAO = medicDAO;
        }

        @Override
        protected List<Medic> doInBackground(Void... voids) {
            return medicDAO.getAll();
        }

    }


}
