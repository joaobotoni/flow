package com.botoni.flow.ui.state;

import java.math.BigDecimal;

public class CalfResultUiState {
    private BigDecimal valorPorKg;
    private BigDecimal valorPorCabeca;
    private BigDecimal valorTotal;
    private boolean isVisible;

    public CalfResultUiState() {
    }

    public CalfResultUiState(BigDecimal valorPorKg, BigDecimal valorPorCabeca, BigDecimal valorTotal, boolean isVisible) {

        this.valorPorKg = valorPorKg;
        this.valorPorCabeca = valorPorCabeca;
        this.valorTotal = valorTotal;
        this.isVisible = isVisible;
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

    public boolean isVisible() {
        return isVisible;
    }

    public void setValorPorKg(BigDecimal valorPorKg) {
        this.valorPorKg = valorPorKg;
    }

    public void setValorPorCabeca(BigDecimal valorPorCabeca) {
        this.valorPorCabeca = valorPorCabeca;
    }
    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }
    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }
}