package com.botoni.flow.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.botoni.flow.data.repositories.NegociacaoBezerroRepository;
import com.botoni.flow.ui.helpers.TaskHelper;
import com.botoni.flow.ui.state.NegociacaoBezerroUiState;

import java.math.BigDecimal;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class NegociacaoBezerroViewModel extends ViewModel {

    private static final BigDecimal ARROBA = new BigDecimal("310");
    private static final BigDecimal PERCENT = new BigDecimal("30");
    private final NegociacaoBezerroRepository repositorio;
    private final TaskHelper taskHelper;
    private final MutableLiveData<NegociacaoBezerroUiState> uiState = new MutableLiveData<>(null);
    private final MutableLiveData<Boolean> visivel = new MutableLiveData<>(false);
    private final MutableLiveData<Exception> erro = new MutableLiveData<>(null);
    @Inject
    public NegociacaoBezerroViewModel(NegociacaoBezerroRepository repositorio, TaskHelper taskHelper) {
        this.repositorio = repositorio;
        this.taskHelper = taskHelper;
    }
    public void calcularNegociacaoBezerro(BigDecimal peso, Integer quantidade) {
        taskHelper.execute(
                () -> repositorio.calcularNegociacaoBezerro(peso, ARROBA, PERCENT, quantidade),
                result -> {
                    NegociacaoBezerroUiState state = new NegociacaoBezerroUiState(
                            result.getValorPorKg(),
                            result.getValorPorCabeca(),
                            result.getValorTotal()
                    );
                    uiState.postValue(state);
                    visivel.postValue(true);
                },
                erro::postValue
        );
    }
    public void limpar() {
        uiState.postValue(null);
        visivel.postValue(false);
        erro.postValue(null);
    }

    public LiveData<NegociacaoBezerroUiState> getUiState() {
        return uiState;
    }

    public LiveData<Boolean> getVisivel() {
        return visivel;
    }

    public LiveData<Exception> getErro() {
        return erro;
    }
}