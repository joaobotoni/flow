package com.botoni.flow.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.botoni.flow.ui.state.RouteState;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class RouteViewModel extends ViewModel {
    private final MutableLiveData<RouteState> _uiState = new MutableLiveData<>();
    @Inject
    public RouteViewModel() {
    }
    public LiveData<RouteState> getState(){
        return _uiState;
    }
    public void setRoute(List<String> points, double distance){
        _uiState.setValue(new RouteState(points, distance, true));
    }
    public void clear() {
        _uiState.setValue(new RouteState());
    }
}
