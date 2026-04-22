package com.botoni.flow.data.source.local.entities;

import androidx.room.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "xgp_tipo_referencia")
public class TipoReferencia {
    @PrimaryKey
    @ColumnInfo(name = "id_tipo_referencia")
    @SerializedName("ID_TIPO_REFERENCIA")
    private int idTipoReferencia;

    @ColumnInfo(name = "descricao")
    @SerializedName("DESCRICAO")
    private String descricao;

    public TipoReferencia() {
    }

    public TipoReferencia(int idTipoReferencia, String descricao) {
        this.idTipoReferencia = idTipoReferencia;
        this.descricao = descricao;
    }

    public int getIdTipoReferencia() {
        return idTipoReferencia;
    }

    public void setIdTipoReferencia(int idTipoReferencia) {
        this.idTipoReferencia = idTipoReferencia;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}