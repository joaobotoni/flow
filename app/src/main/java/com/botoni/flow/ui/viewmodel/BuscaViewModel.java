package com.botoni.flow.ui.viewmodel;

import android.Manifest;

import androidx.annotation.RequiresPermission;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.botoni.flow.data.repositories.LocalizacaoRepository;
import com.botoni.flow.ui.helpers.TaskHelper;
import com.botoni.flow.ui.state.BuscaLocalizacaoUiState;

import java.io.IOException;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;


@HiltViewModel
public class BuscaViewModel extends ViewModel {
    private final LocalizacaoRepository repositorio;
    private final TaskHelper taskHelper;
    private final MutableLiveData<BuscaLocalizacaoUiState> uiState = new MutableLiveData<>(null);
    private final MutableLiveData<Boolean> visivel = new MutableLiveData<>(false);
    private final MutableLiveData<Exception> erro = new MutableLiveData<>(null);
    @Inject
    public BuscaViewModel(LocalizacaoRepository repositorio, TaskHelper taskHelper) {
        this.repositorio = repositorio;
        this.taskHelper = taskHelper;
    }

    public void buscar(String consulta, double latitude, double longitude) {
        taskHelper.execute(
                () -> {
                    String codigoPais;
                    try {
                        codigoPais = repositorio.buscarCodigoPais(latitude, longitude);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        return new BuscaLocalizacaoUiState(
                                repositorio.buscarCidadeEstado(consulta, codigoPais), false);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
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

    public LiveData<BuscaLocalizacaoUiState> getUiState() {
        return uiState;
    }

    public LiveData<Boolean> getVisivel() {
        return visivel;
    }

    public LiveData<Exception> getErro() {
        return erro;
    }
}