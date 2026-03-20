package com.botoni.flow.ui.state;

import android.location.Address;

import java.util.Collections;
import java.util.List;

public class BuscaLocalizacaoUiState {
    public final List<Address> localizacoes;
    public final boolean carregando;
    public BuscaLocalizacaoUiState() {
        this.localizacoes = Collections.emptyList();
        this.carregando = false;
    }
    public BuscaLocalizacaoUiState(List<Address> localizacoes, boolean carregando) {
        this.localizacoes = localizacoes;
        this.carregando = carregando;
    }
    public List<Address> getLocalizacoes() {
        return localizacoes;
    }

    public boolean isCarregando() {
        return carregando;
    }
}