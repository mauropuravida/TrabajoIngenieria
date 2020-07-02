package com.example.healthsense.db.Repository;

import android.app.Application;
import android.os.AsyncTask;

import com.example.healthsense.db.AppDatabase;
import com.example.healthsense.db.dao.UsersDAO;
import com.example.healthsense.db.entity.Users;

import java.util.concurrent.ExecutionException;

public class UserRepository {
    private UsersDAO userDao;

    public UserRepository(Application application) {
        AppDatabase database = AppDatabase.getAppDatabase(application);
        userDao = database.usersDAO();
    }

    public void insert(Users user) {
        new InsertUserAsyncTask(userDao).execute(user);
    }

    public void update(Users user) {
        new UpdateUserAsyncTask(userDao).execute(user);
    }

    public void delete(Users user) {
        new DeleteUsersyncTask(userDao).execute(user);
    }

    public int getId(String email){ return userDao.getID(email);}

    public int getIdTask(String email){
        try {
            return new TaskGetId(userDao,email).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean exist(String email){ return userDao.exist(email);}

    public void deleteAll(){ userDao.deleteAll();}

    private static class InsertUserAsyncTask extends AsyncTask<Users, Void, Void> {
        private UsersDAO userDAO;

        private InsertUserAsyncTask(UsersDAO userDAO) {
            this.userDAO = userDAO;
        }

        @Override
        protected Void doInBackground(Users... users) {
            userDAO.insert(users[0]);
            return null;
        }
    }

    private static class UpdateUserAsyncTask extends AsyncTask<Users, Void, Void> {
        private UsersDAO userDAO;

        private UpdateUserAsyncTask(UsersDAO userDAO) {
            this.userDAO = userDAO;
        }

        @Override
        protected Void doInBackground(Users... users) {
            userDAO.update(users[0]);
            return null;
        }
    }

    private static class DeleteUsersyncTask extends AsyncTask<Users, Void, Void> {
        private UsersDAO userDAO;

        private DeleteUsersyncTask(UsersDAO userDAO) {
            this.userDAO = userDAO;
        }

        @Override
        protected Void doInBackground(Users... users) {
            userDAO.delete(users[0]);
            return null;
        }
    }

    private static class TaskGetId extends AsyncTask<Void, Void, Integer> {

        private UsersDAO usersDAO;
        private  String email;

        private TaskGetId(UsersDAO usersDAO, String email) {
            this.usersDAO = usersDAO;
            this.email = email;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            return usersDAO.getID(email);
        }

    }
}
