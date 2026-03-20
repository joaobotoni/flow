package com.botoni.flow.data.models;

import java.math.BigDecimal;

public class NegociacaoFrete {
    public final BigDecimal valorTotal;
    public final BigDecimal valorPorAnimal;
    public NegociacaoFrete(BigDecimal valorTotal, BigDecimal valorPorAnimal) {
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
