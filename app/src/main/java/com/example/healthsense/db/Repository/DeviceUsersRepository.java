package com.example.healthsense.db.Repository;

import android.app.Application;
import android.os.AsyncTask;

import com.example.healthsense.db.AppDatabase;
import com.example.healthsense.db.dao.DeviceUsersDAO;
import com.example.healthsense.db.entity.DeviceUsers;

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

}
