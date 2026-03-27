package com.botoni.flow.ui.state;

import java.util.Collections;
import java.util.List;

public class RotaUiState {
    private final String cidadeOrigem;
    private final String estadoOrigem;
    private final String cidadeDestino;
    private final String estadoDestino;
    private final double distancia;

    public RotaUiState(String cidadeOrigem, String estadoOrigem,
                       String cidadeDestino, String estadoDestino,
                       double distancia) {
        this.cidadeOrigem = cidadeOrigem;
        this.estadoOrigem = estadoOrigem;
        this.cidadeDestino = cidadeDestino;
        this.estadoDestino = estadoDestino;
        this.distancia = distancia;
    }

    public String getCidadeOrigem() {
        return cidadeOrigem;
    }

    public String getEstadoOrigem() {
        return estadoOrigem;
    }

    public String getCidadeDestino() {
        return cidadeDestino;
    }

    public String getEstadoDestino() {
        return estadoDestino;
    }

    public double getDistancia() {
        return distancia;
    }
}