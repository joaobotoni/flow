package com.botoni.flow.data.source.local.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "xgp_categoria_frete")
public class CategoriaFrete {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_categoria_frete")
    @SerializedName(value = "ID_CATEGORIA_FRETE")
    private int id;
    @ColumnInfo(name = "descricao")
    @SerializedName(value = "DESCRICAO")
    private String descricao;
    public CategoriaFrete() {}

    public CategoriaFrete(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }
    public CategoriaFrete(String descricao) {
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}

