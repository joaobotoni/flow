package com.botoni.flow.ui.state;

import java.util.Collections;
import java.util.List;

public class RotaUiState {
    public final String cidadeOrigem;
    public final String estadoOrigem;
    public final String cidadeDestino;
    public final String estadoDestino;
    public final double distancia;

    public RotaUiState(String cidadeOrigem, String estadoOrigem,
                       String cidadeDestino, String estadoDestino,
                       double distancia) {
        this.cidadeOrigem = cidadeOrigem;
        this.estadoOrigem = estadoOrigem;
        this.cidadeDestino = cidadeDestino;
        this.estadoDestino = estadoDestino;
        this.distancia = distancia;
    }
}