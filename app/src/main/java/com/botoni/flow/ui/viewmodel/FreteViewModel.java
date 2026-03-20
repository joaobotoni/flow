package com.botoni.flow.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.botoni.flow.data.models.NegociacaoFrete;
import com.botoni.flow.data.models.Transporte;
import com.botoni.flow.data.repositories.FreteRepository;
import com.botoni.flow.ui.helpers.TaskHelper;
import com.botoni.flow.ui.state.NegociacaoFreteUiState;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class FreteViewModel extends ViewModel {
    private final TaskHelper taskHelper;
    private final FreteRepository repository;
    private final MutableLiveData<NegociacaoFreteUiState> uiState = new MutableLiveData<>(null);
    private final MutableLiveData<Boolean> visivel = new MutableLiveData<>(false);
    private final MutableLiveData<Exception> erro = new MutableLiveData<>(null);
    @Inject
    public FreteViewModel(TaskHelper taskHelper, FreteRepository repository) {
        this.repository = repository;
        this.taskHelper = taskHelper;
    }
    public void calcularFrete(List<Transporte> transportes, double distancia, int totalAnimais) {
        taskHelper.execute(
                () -> {
                    NegociacaoFrete result = repository.calcularFrete(transportes, distancia, totalAnimais);
                    return new NegociacaoFreteUiState(result.getValorTotal(), result.getValorPorAnimal());
                },
                state -> {
                    uiState.postValue(state);
                    visivel.postValue(state != null);
                },
                erro::postValue
        );
    }
    public void limpar() {
        uiState.postValue(null);
        visivel.postValue(false);
        erro.postValue(null);
    }

    public LiveData<NegociacaoFreteUiState> getUiState() {
        return uiState;
    }

    public LiveData<Boolean> getVisivel() {
        return visivel;
    }

    public LiveData<Exception> getErro() {
        return erro;
    }
}