package com.botoni.flow.ui.viewmodel;

import com.botoni.flow.data.repositories.LocalizacaoRepository;
import com.botoni.flow.ui.helpers.TaskHelper;
import com.botoni.flow.ui.libs.BaseViewModel;
import com.botoni.flow.ui.state.BuscaLocalizacaoUiState;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class BuscaViewModel extends BaseViewModel<BuscaLocalizacaoUiState> {
    private final LocalizacaoRepository repositorio;

    @Inject
    public BuscaViewModel(LocalizacaoRepository repositorio, TaskHelper taskHelper) {
        super(taskHelper);
        this.repositorio = repositorio;
    }

    public void buscar(String consulta, double latitude, double longitude) {
        taskHelper.execute(
                () -> {
                    String codigo = repositorio.paisDeCoordenadas(latitude, longitude).orElseThrow(() ->
                            new RuntimeException("Código do pais não encontrado"));
                    return new BuscaLocalizacaoUiState(repositorio.enderecosPorTexto(consulta, codigo), false);
                },
                state::postValue,
                error::postValue
        );
    }
}