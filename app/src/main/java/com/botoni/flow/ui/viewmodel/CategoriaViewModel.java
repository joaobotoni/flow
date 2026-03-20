package com.botoni.flow.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.botoni.flow.data.repositories.CategoriaFreteRepository;
import com.botoni.flow.ui.helpers.TaskHelper;
import com.botoni.flow.ui.state.CategoriaUiState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CategoriaViewModel extends ViewModel {
    private final TaskHelper taskHelper;
    private final CategoriaFreteRepository repositorio;
    private final MutableLiveData<List<CategoriaUiState>> uiState = new MutableLiveData<>(Collections.emptyList());
    private final MutableLiveData<Boolean> visivel = new MutableLiveData<>(false);
    private final MutableLiveData<Exception> erro = new MutableLiveData<>(null);
    @Inject
    public CategoriaViewModel(TaskHelper taskHelper, CategoriaFreteRepository repositorio) {
        this.repositorio = repositorio;
        this.taskHelper = taskHelper;
        listar();
    }

    public void selecionar(CategoriaUiState item) {
        taskHelper.execute(
                () -> selecionar(uiState.getValue(), item),
                lista -> {
                    uiState.postValue(lista);
                    visivel.postValue(lista != null && !lista.isEmpty());
                },
                erro::postValue
        );
    }

    public void limpar() {
        uiState.postValue(Collections.emptyList());
        visivel.postValue(false);
        erro.postValue(null);
    }

    public LiveData<List<CategoriaUiState>> getUiState() {
        return uiState;
    }

    public LiveData<Boolean> getVisivel() {
        return visivel;
    }

    public LiveData<Exception> getErro() {
        return erro;
    }

    public void listar() {
        taskHelper.execute(
                () -> repositorio.getAll().stream()
                        .map(e -> new CategoriaUiState(e.getId(), e.getDescricao(), false))
                        .collect(Collectors.toList()),
                uiState::postValue,
                erro::postValue
        );
    }

    public List<CategoriaUiState> selecionar(List<CategoriaUiState> atual, CategoriaUiState selecionada) {
        if (atual == null || atual.isEmpty()) return Collections.emptyList();
        List<CategoriaUiState> lista = new ArrayList<>();
        for (CategoriaUiState item : atual) {
            boolean estaSelecionada = selecionada != null && item.getId() == selecionada.getId();
            lista.add(new CategoriaUiState(item.getId(), item.getDescricao(), estaSelecionada));
        }
        return lista;
    }
}