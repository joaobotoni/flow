package com.botoni.flow.ui.viewmodel;

import android.location.Address;

import com.botoni.flow.data.repositories.LocalizacaoRepository;
import com.botoni.flow.ui.helpers.TaskHelper;
import com.botoni.flow.ui.libs.BaseViewModel;
import com.botoni.flow.ui.state.RotaUiState;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class RotaViewModel extends BaseViewModel<RotaUiState> {
    private final LocalizacaoRepository repositorio;
    private static final String DESTINO_QUERY = "Cuiabá";
    @Inject
    public RotaViewModel(TaskHelper taskHelper, LocalizacaoRepository repositorio) {
        super(taskHelper);
        this.repositorio = repositorio;
    }

    public void selecionar(Address origem) {
        taskHelper.execute(
                () -> calcularRota(origem),
                state::postValue,
                error::postValue
        );
    }

    private RotaUiState calcularRota(Address origem) throws Exception {
        Address destino = repositorio.selecionarCidadeEstado(DESTINO_QUERY).orElseThrow();
        String resposta = repositorio.obterRota(origem, destino);
        double distancia = repositorio.calcularDistanciaKm(resposta);
        return new RotaUiState(cidade(origem), estado(origem), cidade(destino), estado(destino), distancia);
    }

    private String cidade(Address endereco) {
        String cidade = endereco.getLocality() != null ? endereco.getLocality() : endereco.getSubAdminArea();
        return cidade != null ? cidade : endereco.getAddressLine(0);
    }

    private String estado(Address endereco) {
        String estado = endereco.getAdminArea();
        return estado != null ? estado : "";
    }
}