package com.botoni.flow.ui.state;

import java.math.BigDecimal;

public class PrecificacaoFreteUiState {
    public final BigDecimal valorTotal;
    public final BigDecimal valorPorAnimal;
    public PrecificacaoFreteUiState(BigDecimal valorTotal, BigDecimal valorPorAnimal) {
        this.valorTotal = valorTotal;
        this.valorPorAnimal = valorPorAnimal;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public BigDecimal getValorPorAnimal() {
        return valorPorAnimal;
    }
}