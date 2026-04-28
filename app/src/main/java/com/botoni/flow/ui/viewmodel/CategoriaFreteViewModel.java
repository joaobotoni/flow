package com.botoni.flow.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.botoni.flow.data.repositories.CategoriaFreteRepository;
import com.botoni.flow.ui.helpers.TaskHelper;
import com.botoni.flow.utils.mappers.domain.CategoriaMapper;
import com.botoni.flow.ui.state.ItemOpcaoUiState;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CategoriaFreteViewModel extends ViewModel {

    private final CategoriaFreteRepository repositorio;
    private final TaskHelper taskHelper;
    private final CategoriaMapper mapper;
    private final MutableLiveData<List<ItemOpcaoUiState>> state = new MutableLiveData<>(Collections.emptyList());
    private final MutableLiveData<ItemOpcaoUiState> categoriaSelecionada = new MutableLiveData<>(null);
    private final MutableLiveData<Throwable> error = new MutableLiveData<>(null);

    @Inject
    public CategoriaFreteViewModel(CategoriaFreteRepository repositorio, TaskHelper taskHelper, CategoriaMapper mapper) {
        this.repositorio = repositorio;
        this.taskHelper = taskHelper;
        this.mapper = mapper;
        listarCategorias();
    }

    public LiveData<List<ItemOpcaoUiState>> getState() {
        return state;
    }

    public LiveData<ItemOpcaoUiState> getCategoriaSelecionada() {
        return categoriaSelecionada;
    }

    public LiveData<Throwable> getError() {
        return error;
    }

    public void selecionarCategorias(ItemOpcaoUiState selecionada) {
        if (state.getValue() == null) return;

        List<ItemOpcaoUiState> newList = state.getValue().stream()
                .map(item -> new ItemOpcaoUiState(
                        item.getId(),
                        item.getDescricao(),
                        Objects.equals(item.getId(), selecionada.getId())))
                .collect(Collectors.toList());

        state.setValue(newList);
        categoriaSelecionada.setValue(selecionada);
    }

    private void listarCategorias() {
        taskHelper.execute(
                () -> repositorio.getAll().stream()
                        .map(mapper::mapFrom)
                        .collect(Collectors.toList()),
                state::postValue,
                error::postValue
        );
    }


    public void limparSelecao() {
        categoriaSelecionada.setValue(null);
        if (state.getValue() == null) return;
        List<ItemOpcaoUiState> newList = state.getValue().stream()
                .map(item -> new ItemOpcaoUiState(
                        item.getId(),
                        item.getDescricao(),
                        false))
                .collect(Collectors.toList());
        state.setValue(newList);
    }
}