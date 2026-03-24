package com.botoni.flow.ui.viewmodel;

import com.botoni.flow.data.repositories.CategoriaFreteRepository;
import com.botoni.flow.ui.helpers.TaskHelper;
import com.botoni.flow.ui.libs.BaseViewModel;
import com.botoni.flow.ui.state.CategoriaUiState;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CategoriaViewModel extends BaseViewModel<List<CategoriaUiState>> {
    private final CategoriaFreteRepository repositorio;
    @Inject
    public CategoriaViewModel(CategoriaFreteRepository repositorio, TaskHelper taskHelper) {
        super(taskHelper);
        this.repositorio = repositorio;
        listar();
    }

    public void selecionar(CategoriaUiState selecionada) {
        if (state.getValue() == null) return;
        List<CategoriaUiState> newList = state.getValue().stream()
                .map(item -> new CategoriaUiState(
                        item.getId(),
                        item.getDescricao(),
                        Objects.equals(item.getId(), selecionada.getId())))
                .collect(Collectors.toList());
        state.setValue(newList);
    }
    private void listar() {
        taskHelper.execute(
                () -> repositorio.getAll().stream()
                        .map(e -> new CategoriaUiState(
                                e.getId(),
                                e.getDescricao(),
                                false))
                        .collect(Collectors.toList()),
                state::postValue,
                error::postValue
        );
    }
}