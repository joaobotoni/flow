package com.botoni.flow.ui.viewmodel;

import android.location.Address;

import com.botoni.flow.data.models.Rota;
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
                () -> calcularRotaA(origem),
                state::postValue,
                error::postValue
        );
    }

    private RotaUiState calcularRotaA(Address origem) throws Exception {
        Address destino = repositorio.enderecoPorNome(DESTINO_QUERY).orElseThrow();
        Rota resposta = repositorio.calcularRota(origem, destino);
        return new RotaUiState(
                resposta.getCidadeOrigem(),
                resposta.getEstadoOrigem(),
                resposta.getCidadeDestino(),
                resposta.getEstadoDestino(),
                resposta.getDistancia()
        );
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