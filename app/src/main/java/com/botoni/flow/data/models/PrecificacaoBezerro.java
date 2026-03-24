package com.botoni.flow.data.models;

import java.math.BigDecimal;

public class PrecificacaoBezerro {
    public final BigDecimal valorPorKg;
    public final BigDecimal valorPorCabeca;
    public final BigDecimal valorTotal;
    public PrecificacaoBezerro(BigDecimal valorPorKg, BigDecimal valorPorCabeca, BigDecimal valorTotal) {
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
