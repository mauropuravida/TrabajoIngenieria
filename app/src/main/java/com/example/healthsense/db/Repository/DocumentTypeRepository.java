package com.example.healthsense.db.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.healthsense.db.AppDatabase;
import com.example.healthsense.db.dao.DocumentTypeDAO;
import com.example.healthsense.db.entity.DocumentType;

import java.util.List;

public class DocumentTypeRepository {
    private DocumentTypeDAO documentTypeDAO;
    private LiveData<List<DocumentType>> allDocumentsTypes;

    public DocumentTypeRepository(Application application) {
        AppDatabase database = AppDatabase.getAppDatabase(application);
        documentTypeDAO = database.documentTypeDAO();
        allDocumentsTypes = documentTypeDAO.getAllDocumentTypes();
    }

    public void insert(DocumentType documentTypes) {
        new InsertUserAsyncTask(documentTypeDAO).execute(documentTypes);
    }

    public void update(DocumentType documentTypes) {
        new UpdateUserAsyncTask(documentTypeDAO).execute(documentTypes);
    }

    public void delete(DocumentType documentTypes) {
        new DeleteUsersyncTask(documentTypeDAO).execute(documentTypes);
    }

    public LiveData<List<DocumentType>> getAllDocumentTypes(){
        return allDocumentsTypes;
    }

    private static class InsertUserAsyncTask extends AsyncTask<DocumentType, Void, Void> {
        private DocumentTypeDAO documentTypeDAO;

        private InsertUserAsyncTask(DocumentTypeDAO documentTypeDAO) {
            this.documentTypeDAO = documentTypeDAO;
        }

        @Override
        protected Void doInBackground(DocumentType... documentTypes) {
            documentTypeDAO.insert(documentTypes[0]);
            return null;
        }
    }

    private static class UpdateUserAsyncTask extends AsyncTask<DocumentType, Void, Void> {
        private DocumentTypeDAO documentTypeDAO;

        private UpdateUserAsyncTask(DocumentTypeDAO documentTypeDAO) {
            this.documentTypeDAO = documentTypeDAO;
        }

        @Override
        protected Void doInBackground(DocumentType... documentTypes) {
            documentTypeDAO.update(documentTypes[0]);
            return null;
        }
    }

    private static class DeleteUsersyncTask extends AsyncTask<DocumentType, Void, Void> {
        private DocumentTypeDAO documentTypeDAO;

        private DeleteUsersyncTask(DocumentTypeDAO documentTypeDAO) {
            this.documentTypeDAO = documentTypeDAO;
        }

        @Override
        protected Void doInBackground(DocumentType... documentTypes) {
            documentTypeDAO.delete(documentTypes[0]);
            return null;
        }
    }
}
