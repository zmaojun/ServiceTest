package com.example.servicetest;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FirstViewModel extends ViewModel {
    private MutableLiveData<String> name;

    public FirstViewModel() {
        name = new MutableLiveData<>();
        name.setValue("");
    }

    public MutableLiveData<String> getName() {
        return name;
    }

    public void SetName(String name) {
        this.name.setValue(name);
    }
}
