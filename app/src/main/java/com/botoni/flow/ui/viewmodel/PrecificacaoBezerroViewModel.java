package com.botoni.flow.ui.viewmodel;

import com.botoni.flow.data.models.PrecificacaoBezerro;
import com.botoni.flow.data.repositories.PrecificacaoBezerroRepository;
import com.botoni.flow.ui.helpers.TaskHelper;
import com.botoni.flow.ui.libs.BaseViewModel;
import com.botoni.flow.ui.state.PrecificacaoBezerroUiState;

import java.math.BigDecimal;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class PrecificacaoBezerroViewModel extends BaseViewModel<PrecificacaoBezerroUiState> {

    private final PrecificacaoBezerroRepository repositorio;

    @Inject
    public PrecificacaoBezerroViewModel(PrecificacaoBezerroRepository repositorio, TaskHelper taskHelper) {
        super(taskHelper);
        this.repositorio = repositorio;
    }

    public void calcularNegociacaoBezerro(BigDecimal peso, BigDecimal arroba, BigDecimal percent, Integer quantidade) {
        taskHelper.execute(
                () -> {
                    PrecificacaoBezerro result = repositorio.calcularNegociacaoBezerro(peso, arroba, percent, quantidade);
                    return new PrecificacaoBezerroUiState(result.getValorPorKg(), result.getValorPorCabeca(), result.getValorTotal());
                },
                state::postValue,
                error::postValue
        );
    }
}