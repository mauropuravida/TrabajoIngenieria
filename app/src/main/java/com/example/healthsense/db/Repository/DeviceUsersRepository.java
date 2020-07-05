package com.example.healthsense.db.Repository;

import android.app.Application;
import android.os.AsyncTask;

import com.example.healthsense.db.AppDatabase;
import com.example.healthsense.db.dao.DeviceUsersDAO;
import com.example.healthsense.db.dao.UsersDAO;
import com.example.healthsense.db.entity.DeviceUsers;

import java.util.concurrent.ExecutionException;

/**
 * Repositorio DeviceUsers encargado de comunicar DAO con fragment/activity.
 */

public class DeviceUsersRepository {
    private DeviceUsersDAO deviceUsersDAO;

    public DeviceUsersRepository(Application application) {
        AppDatabase database = AppDatabase.getAppDatabase(application);
        deviceUsersDAO = database.deviceUsersDAO();
    }

    public void insert(DeviceUsers deviceUsers) {
        new InsertUserAsyncTask(deviceUsersDAO).execute(deviceUsers);
    }

    public void update(DeviceUsers deviceUsers) {
        new UpdateUserAsyncTask(deviceUsersDAO).execute(deviceUsers);
    }

    public void delete(DeviceUsers deviceUsers) {
        new DeleteUsersyncTask(deviceUsersDAO).execute(deviceUsers);
    }

    public void increaseWorks(int id){ deviceUsersDAO.increaseWorks(id);}

    public void decreaseWorks(int id){ deviceUsersDAO.decreaseWorks(id);}

    public int getWorksSaved(int id){
        try {
            return new TaskGetWorksSaved(deviceUsersDAO,id).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void deleteAll(){ deviceUsersDAO.deleteAll();}

    public int getDeviceUserId(int id){return deviceUsersDAO.getDeviceUserId(id);}

    private static class InsertUserAsyncTask extends AsyncTask<DeviceUsers, Void, Void> {
        private DeviceUsersDAO deviceUsersDAO;

        private InsertUserAsyncTask(DeviceUsersDAO deviceUsersDAO) {
            this.deviceUsersDAO = deviceUsersDAO;
        }

        @Override
        protected Void doInBackground(DeviceUsers... deviceUsers) {
            deviceUsersDAO.insert(deviceUsers[0]);
            return null;
        }
    }

    private static class UpdateUserAsyncTask extends AsyncTask<DeviceUsers, Void, Void> {
        private DeviceUsersDAO deviceUsersDAO;

        private UpdateUserAsyncTask(DeviceUsersDAO deviceUsersDAO) {
            this.deviceUsersDAO = deviceUsersDAO;
        }

        @Override
        protected Void doInBackground(DeviceUsers... deviceUsers) {
            deviceUsersDAO.update(deviceUsers[0]);
            return null;
        }
    }

    private static class DeleteUsersyncTask extends AsyncTask<DeviceUsers, Void, Void> {
        private DeviceUsersDAO deviceUsersDAO;

        private DeleteUsersyncTask(DeviceUsersDAO deviceUsersDAO) {
            this.deviceUsersDAO = deviceUsersDAO;
        }

        @Override
        protected Void doInBackground(DeviceUsers... deviceUsers) {
            deviceUsersDAO.delete(deviceUsers[0]);
            return null;
        }
    }

    private static class TaskGetWorksSaved extends AsyncTask<Void, Void, Integer> {

        private DeviceUsersDAO deviceUsersDAO;
        private  int  user_id;

        private TaskGetWorksSaved(DeviceUsersDAO deviceUsersDAO, int user_id) {
            this.deviceUsersDAO = deviceUsersDAO;
            this.user_id = user_id;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            return deviceUsersDAO.getWorksSaved(user_id);
        }

    }
}
