package com.botoni.flow.ui.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.botoni.flow.data.repositories.PrecificacaoBezerroRepository;
import com.botoni.flow.ui.helpers.TaskHelper;
import com.botoni.flow.ui.state.DetalhePrecoBezerroUiState;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DetalhePrecificacaoViewModel extends ViewModel {
    private final TaskHelper taskHelper;
    private final PrecificacaoBezerroRepository repository;
    private final MutableLiveData<DetalhePrecoBezerroUiState> state = new MutableLiveData<>();
    private final MutableLiveData<Throwable> error = new MutableLiveData<>();
    @Inject
    public DetalhePrecificacaoViewModel(PrecificacaoBezerroRepository repository, TaskHelper taskHelper) {
        this.repository = repository;
        this.taskHelper = taskHelper;
    }

    public LiveData<DetalhePrecoBezerroUiState> getState() {
        return state;
    }

    public void calcular(BigDecimal peso, BigDecimal arroba, BigDecimal percent) {
        taskHelper.execute(
                () -> {
                    BigDecimal valorTotal = repository.calcularValorTotalBezerro(peso, arroba, percent);
                    BigDecimal valorPorKg = repository.calcularValorPorKg(peso, arroba, percent);
                    return new DetalhePrecoBezerroUiState(peso, valorTotal, valorPorKg);
                },
                state::postValue,
                error::postValue
        );
    }
}
