package com.botoni.flow.data.source.local.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "empresa")
public class Empresa {
    @PrimaryKey(autoGenerate = true)
    @SerializedName("ID_EMPRESA")
    @ColumnInfo(name = "id_empresa")
    private int idEmpresa;
    @SerializedName("NOME")
    @ColumnInfo(name = "nome")
    private String nome;

    public Empresa() {
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
