package com.botoni.flow.ui.state;

public class EmpresaUiState {
    private final Integer id;
    private final String nome;
    private final String iniciais;
    private final boolean selecionada;

    public EmpresaUiState(Integer id, String nome, boolean selecionada) {
        this.id = id;
        this.nome = nome;
        this.iniciais = init(nome);
        this.selecionada = selecionada;
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getIniciais() {
        return iniciais;
    }

    public boolean isSelecionada() {
        return selecionada;
    }

    private String init(String value){
        return value.replaceAll("[^a-zA-Z ]", "")
                .replaceAll("\\b([a-zA-Z])[a-zA-Z]*\\b", "$1")
                .replaceAll(" ", "")
                .toUpperCase();
    }
}