package com.example.healthsense.ui.traininghistory;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TrainingHistoryViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TrainingHistoryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is tools fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}