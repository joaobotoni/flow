package com.botoni.flow.ui.state;


import com.botoni.flow.data.source.local.entities.CategoriaFrete;

import java.util.List;

public class CategoriaUiState {
    private final long id;
    private final String descricao;
    private final boolean selecionada;
    public CategoriaUiState(long id, String descricao, boolean selecionada) {
        this.id = id;
        this.descricao = descricao;
        this.selecionada = selecionada;
    }

    public long getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public boolean isSelecionada() {
        return selecionada;
    }
}