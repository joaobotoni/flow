package com.botoni.flow.ui.state;

import java.util.Collections;
import java.util.List;

public class RotaUiState {
    public final List<String> pontos;
    public final double distancia;
    public RotaUiState(List<String> pontos, double distancia) {
        this.pontos = pontos;
        this.distancia = distancia;
    }

    public List<String> getPontos() {
        return pontos;
    }

    public double getDistancia() {
        return distancia;
    }
}