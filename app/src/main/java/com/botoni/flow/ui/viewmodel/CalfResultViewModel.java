package com.botoni.flow.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.botoni.flow.data.repositories.local.TransportRepository;
import com.botoni.flow.ui.helpers.TaskHelper;
import com.botoni.flow.ui.state.CalfResultUiState;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CalfResultViewModel extends ViewModel {
    private final TransportRepository repository;
    private final TaskHelper taskExecutor;
    private final MutableLiveData<CalfResultUiState> uiState = new MutableLiveData<>(new CalfResultUiState());
    private final MutableLiveData<Exception> errorEvent = new MutableLiveData<>();

    @Inject
    public CalfResultViewModel(TransportRepository repository, TaskHelper taskExecutor) {
        this.repository = repository;
        this.taskExecutor = taskExecutor;
    }

    public LiveData<CalfResultUiState> getUiState() {
        return uiState;
    }

    public LiveData<Exception> getErrorEvent() {
        return errorEvent;
    }

    public void reset() {
        uiState.setValue(new CalfResultUiState());
    }

}