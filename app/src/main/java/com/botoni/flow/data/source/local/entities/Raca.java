package com.botoni.flow.data.source.local.entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "xgp_raca")
public class Raca {

    @PrimaryKey
    @ColumnInfo(name = "id_raca")
    @SerializedName(value = "ID_RACA")
    private int idRaca;

    @ColumnInfo(name = "descricao")
    @SerializedName(value = "DESCRICAO")
    private String descricao;

    @ColumnInfo(name = "sigla")
    @SerializedName(value = "SIGLA")
    private String sigla;

    public Raca() {
    }

    public Raca(int idRaca, String descricao, String sigla) {
        this.idRaca = idRaca;
        this.descricao = descricao;
        this.sigla = sigla;
    }

    public int getIdRaca() {
        return idRaca;
    }

    public void setIdRaca(int idRaca) {
        this.idRaca = idRaca;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }
}
