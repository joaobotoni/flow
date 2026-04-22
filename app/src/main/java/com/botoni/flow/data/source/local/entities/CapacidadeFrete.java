package com.botoni.flow.data.source.local.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(
        tableName = "xgp_capacidade_frete",
        foreignKeys = {
                @ForeignKey(
                        entity = CategoriaFrete.class,
                        parentColumns = "id_categoria_frete",
                        childColumns = "id_categoria_frete",
                        onDelete = ForeignKey.RESTRICT
                ),
                @ForeignKey(
                        entity = TipoVeiculoFrete.class,
                        parentColumns = "id_tipo_veiculo_frete",
                        childColumns = "id_tipo_veiculo_frete",
                        onDelete = ForeignKey.RESTRICT
                )
        },
        indices = {
                @Index("id_categoria_frete"),
                @Index("id_tipo_veiculo_frete")
        }
)
public class CapacidadeFrete {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_capacidade_frete")
    @SerializedName(value = "ID_CAPACIDADE_FRETE")
    private int id;
    @ColumnInfo(name = "id_categoria_frete")
    @SerializedName(value = "ID_CATEGORIA_FRETE")
    private int idCategoriaFrete;
    @ColumnInfo(name = "id_tipo_veiculo_frete")
    @SerializedName(value = "ID_TIPO_VEICULO_FRETE")
    private int idTipoVeiculoFrete;
    @ColumnInfo(name = "qtde_inicial")
    @SerializedName(value = "QTDE_INICIAL")
    private Integer qtdeInicial;
    @ColumnInfo(name = "qtde_final")
    @SerializedName(value = "QTDE_FINAL")
    private Integer qtdeFinal;

    public CapacidadeFrete() {
    }

    public CapacidadeFrete(int id, int idCategoriaFrete, int idTipoVeiculoFrete, Integer qtdeInicial, Integer qtdeFinal) {
        this.id = id;
        this.idCategoriaFrete = idCategoriaFrete;
        this.idTipoVeiculoFrete = idTipoVeiculoFrete;
        this.qtdeInicial = qtdeInicial;
        this.qtdeFinal = qtdeFinal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCategoriaFrete() {
        return idCategoriaFrete;
    }

    public void setIdCategoriaFrete(int idCategoriaFrete) {
        this.idCategoriaFrete = idCategoriaFrete;
    }

    public int getIdTipoVeiculoFrete() {
        return idTipoVeiculoFrete;
    }

    public void setIdTipoVeiculoFrete(int idTipoVeiculoFrete) {
        this.idTipoVeiculoFrete = idTipoVeiculoFrete;
    }

    public Integer getQtdeInicial() {
        return qtdeInicial;
    }

    public void setQtdeInicial(Integer qtdeInicial) {
        this.qtdeInicial = qtdeInicial;
    }

    public Integer getQtdeFinal() {
        return qtdeFinal;
    }

    public void setQtdeFinal(Integer qtdeFinal) {
        this.qtdeFinal = qtdeFinal;
    }
}