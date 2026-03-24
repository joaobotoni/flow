package com.botoni.flow.ui.libs;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.botoni.flow.ui.helpers.TaskHelper;

public abstract class BaseViewModel<T> extends ViewModel {

    protected final TaskHelper taskHelper;
    protected final MutableLiveData<T> state = new MutableLiveData<>();
    protected final MutableLiveData<Exception> error = new MutableLiveData<>(null);
    protected BaseViewModel(TaskHelper taskHelper) {
        this.taskHelper = taskHelper;
    }

    public LiveData<T> getState() {
        return state;
    }

    public LiveData<Exception> getError() {
        return error;
    }

    public void clear() {
        state.postValue(null);
        error.postValue(null);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        taskHelper.cancelAll();
    }
}
