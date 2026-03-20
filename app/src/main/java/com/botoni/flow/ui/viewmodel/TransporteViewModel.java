package com.botoni.flow.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.botoni.flow.data.repositories.TransporteRepository;
import com.botoni.flow.ui.helpers.TaskHelper;
import com.botoni.flow.ui.state.TransporteUiState;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class TransporteViewModel extends ViewModel {

    private final TaskHelper taskHelper;
    private final TransporteRepository repositorio;
    private final MutableLiveData<List<TransporteUiState>> uiState = new MutableLiveData<>(null);
    private final MutableLiveData<Boolean> visivel = new MutableLiveData<>(false);
    private final MutableLiveData<Exception> erro = new MutableLiveData<>(null);
    @Inject
    public TransporteViewModel(TransporteRepository repositorio, TaskHelper taskHelper) {
        this.repositorio = repositorio;
        this.taskHelper = taskHelper;
    }

    public void recomendar(long categoria, int quantidade) {
        taskHelper.execute(
                () -> repositorio.recomendacao(categoria, quantidade)
                        .stream()
                        .map(t -> new TransporteUiState(
                                t.getId(),
                                t.getNomeVeiculo(),
                                t.getQuantidade(),
                                t.getCapacidade(),
                                t.getOcupacao()))
                        .collect(Collectors.toList()),
                lista -> {
                    uiState.postValue(lista);
                    visivel.postValue(lista != null && !lista.isEmpty());
                },
                erro::postValue
        );
    }

    public void limpar() {
        uiState.postValue(null);
        visivel.postValue(false);
        erro.postValue(null);
    }

    public LiveData<List<TransporteUiState>> getUiState() { return uiState; }
    public LiveData<Boolean> getVisivel() { return visivel; }
    public LiveData<Exception> getErro() { return erro; }
}