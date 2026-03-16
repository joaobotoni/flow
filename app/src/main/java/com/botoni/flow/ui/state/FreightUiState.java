package com.botoni.flow.ui.state;

import java.math.BigDecimal;

public class FreightUiState {
    private BigDecimal valorFretePorAnimal;
    private BigDecimal valorFreteTotal;
    private boolean isVisible;

    public FreightUiState() {
    }

    public FreightUiState(BigDecimal valorFretePorAnimal, BigDecimal valorFreteTotal, boolean isVisible) {
        this.valorFretePorAnimal = valorFretePorAnimal;
        this.valorFreteTotal = valorFreteTotal;
        this.isVisible = isVisible;
    }

    public BigDecimal getValorFretePorAnimal() {
        return valorFretePorAnimal;
    }

    public BigDecimal getValorFreteTotal() {
        return valorFreteTotal;
    }

    public boolean isVisible() {
        return isVisible;
    }
}