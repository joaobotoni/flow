package com.botoni.flow.ui.state;

import androidx.annotation.NonNull;

import java.math.BigDecimal;

public class CorretorUiState {
    private final int id;
    private final String nome;
    private final BigDecimal comissao;
    private final String tipoComissao;
    private final boolean selecionado;

    public CorretorUiState(int id, String nome, BigDecimal comissao, String tipoComissao, boolean selecionado) {
        this.id = id;
        this.nome = nome;
        this.comissao = comissao;
        this.tipoComissao = tipoComissao;
        this.selecionado = selecionado;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public BigDecimal getComissao() {
        return comissao;
    }

    public String getTipoComissao() {
        return tipoComissao;
    }

    public boolean isSelecionado() {
        return selecionado;
    }

    @NonNull
    @Override
    public String toString() {
        return nome;
    }
}
