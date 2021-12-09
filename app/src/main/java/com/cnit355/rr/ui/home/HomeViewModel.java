package com.cnit355.rr.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is read fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }



    public void pdfToEpub(){


    }

}