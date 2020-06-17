package com.example.servicetest;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SecondViewModel extends ViewModel {
    private MutableLiveData<String> name;

    public SecondViewModel() {
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
