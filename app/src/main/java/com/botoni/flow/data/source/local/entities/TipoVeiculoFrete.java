package com.botoni.flow.data.source.local.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "xgp_tipo_veiculo_frete")
public class TipoVeiculoFrete {
    @ColumnInfo(name = "id_tipo_veiculo_frete")
    @PrimaryKey(autoGenerate = true)
    private Long id;
    @ColumnInfo(name = "descricao")
    private String descricao;

    public TipoVeiculoFrete() {
    }

    public TipoVeiculoFrete(String descricao) {
        this.descricao = descricao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}