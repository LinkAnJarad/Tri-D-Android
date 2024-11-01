package com.example.testnavdrawer2.ui.qradmin;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class QRAdminViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public QRAdminViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}