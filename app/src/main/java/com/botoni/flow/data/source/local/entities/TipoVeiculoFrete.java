package com.botoni.flow.data.source.local.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "xgp_tipo_veiculo_frete")
public class TipoVeiculoFrete {
    @ColumnInfo(name = "id_tipo_veiculo_frete")
    @SerializedName(value = "ID_TIPO_VEICULO_FRETE")
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "descricao")
    @SerializedName(value = "DESCRICAO")
    private String descricao;

    public TipoVeiculoFrete() {
    }

    public TipoVeiculoFrete(String descricao) {
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}