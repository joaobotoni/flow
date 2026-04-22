package com.botoni.flow.data.source.local.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "xgp_categoria_neg")
public class CategoriaNeg {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_categoria_neg")
    @SerializedName(value = "ID_CATEGORIA_NEG")
    private int idCategoriaNeg;

    @ColumnInfo(name = "descricao")
    @SerializedName(value = "DESCRICAO")
    private String descricao;

    @ColumnInfo(name = "sexo")
    @SerializedName(value = "SEXO")
    private String sexo;

    @ColumnInfo(name = "ativo")
    @SerializedName(value = "ATIVO")
    private String ativo;

    public CategoriaNeg() {
    }

    public CategoriaNeg(int idCategoriaNeg, String descricao, String sexo, String ativo) {
        this.idCategoriaNeg = idCategoriaNeg;
        this.descricao = descricao;
        this.sexo = sexo;
        this.ativo = ativo;
    }

    public int getIdCategoriaNeg() {
        return idCategoriaNeg;
    }

    public void setIdCategoriaNeg(int idCategoriaNeg) {
        this.idCategoriaNeg = idCategoriaNeg;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getAtivo() {
        return ativo;
    }

    public void setAtivo(String ativo) {
        this.ativo = ativo;
    }
}
