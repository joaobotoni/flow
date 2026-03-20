package com.botoni.flow.ui.state;

import java.math.BigDecimal;

public class NegociacaoBezerroUiState {
    public final BigDecimal valorPorKg;
    public final BigDecimal valorPorCabeca;
    public final BigDecimal valorTotal;

    public NegociacaoBezerroUiState(BigDecimal valorPorKg, BigDecimal valorPorCabeca, BigDecimal valorTotal) {
        this.valorPorKg = valorPorKg;
        this.valorPorCabeca = valorPorCabeca;
        this.valorTotal = valorTotal;
    }

    public BigDecimal getValorPorKg() {
        return valorPorKg;
    }

    public BigDecimal getValorPorCabeca() {
        return valorPorCabeca;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }
}