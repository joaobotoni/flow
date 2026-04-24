package com.botoni.flow.data.models;

import java.math.BigDecimal;

public class ParametrosBezerro {
    public final BigDecimal precoPorArroba;
    public final BigDecimal percentualAgio;
    public final BigDecimal pesoBaseKg;

    public ParametrosBezerro(BigDecimal precoPorArroba, BigDecimal percentualAgio, BigDecimal pesoBaseKg) {
        this.precoPorArroba = precoPorArroba;
        this.percentualAgio = percentualAgio;
        this.pesoBaseKg = pesoBaseKg;
    }
}
