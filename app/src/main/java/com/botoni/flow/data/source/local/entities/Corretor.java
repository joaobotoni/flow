package com.botoni.flow.data.source.local.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.*;

@Entity(tableName = "xgp_corretor")
public class Corretor {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("ID_CORRETOR")
    @ColumnInfo(name = "id_corretor")
    private int idCorretor;
    @SerializedName("NOME")
    @ColumnInfo(name = "name")
    private String nome;

    @SerializedName("COMISSAO")
    @ColumnInfo(name = "comissao")
    private Double comissao;
    @SerializedName("TIPO_COMISSAO")
    @ColumnInfo(name = "tipo_comissao")
    private String tipoComissao;

    public Corretor() {
    }

    public Corretor(int idCorretor, String nome, Double comissao, String tipoComissao) {
        this.idCorretor = idCorretor;
        this.nome = nome;
        this.comissao = comissao;
        this.tipoComissao = tipoComissao;
    }

    public int getIdCorretor() {
        return idCorretor;
    }

    public void setIdCorretor(int idCorretor) {
        this.idCorretor = idCorretor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getComissao() {
        return comissao;
    }

    public void setComissao(Double comissao) {
        this.comissao = comissao;
    }

    public String getTipoComissao() {
        return tipoComissao;
    }

    public void setTipoComissao(String tipoComissao) {
        this.tipoComissao = tipoComissao;
    }
}
