package com.botoni.flow.ui.viewmodel;

import com.botoni.flow.data.repositories.TransporteRepository;
import com.botoni.flow.ui.helpers.TaskHelper;
import com.botoni.flow.ui.libs.BaseViewModel;
import com.botoni.flow.ui.state.TransporteUiState;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class TransporteViewModel extends BaseViewModel<List<TransporteUiState>> {
    private final TransporteRepository repositorio;

    @Inject
    public TransporteViewModel(TransporteRepository repositorio, TaskHelper taskHelper) {
        super(taskHelper);
        this.repositorio = repositorio;
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
                state::postValue,
                error::postValue
        );
    }
}