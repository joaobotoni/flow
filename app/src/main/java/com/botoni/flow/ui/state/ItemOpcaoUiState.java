package com.botoni.flow.ui.state;


public class ItemOpcaoUiState {
    private final int id;
    private final String descricao;
    private final boolean selecionada;

    public ItemOpcaoUiState(int id, String descricao, boolean selecionada) {
        this.id = id;
        this.descricao = descricao;
        this.selecionada = selecionada;
    }

    public int getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public boolean isSelecionada() {
        return selecionada;
    }
}