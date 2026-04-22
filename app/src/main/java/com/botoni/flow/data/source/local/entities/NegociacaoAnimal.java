package com.botoni.flow.data.source.local.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import com.google.gson.annotations.SerializedName;

@Entity(
        tableName = "xgp_negociacao_animal",
        primaryKeys = {"id_negociacao_animal", "id_negociacao_gado"},
        foreignKeys = @ForeignKey(
                entity = NegociacaoGado.class,
                parentColumns = "id_negociacao_gado",
                childColumns = "id_negociacao_gado",
                onDelete = ForeignKey.RESTRICT
        )
)
public class NegociacaoAnimal {


    @ColumnInfo(name = "id_negociacao_animal")
    @SerializedName("ID_NEGOCIACAO_ANIMAL")
    private int idNegociacaoAnimal;

    @ColumnInfo(name = "id_negociacao_gado")
    @SerializedName("ID_NEGOCIACAO_GADO")
    private int idNegociacaoGado;

    @ColumnInfo(name = "peso")
    @SerializedName("PESO")
    private Double peso;

    @ColumnInfo(name = "valor_kg")
    @SerializedName("VALOR_KG")
    private Double valorKg;

    @ColumnInfo(name = "valor_total")
    @SerializedName("VALOR_TOTAL")
    private Double valorTotal;

    public NegociacaoAnimal() {
    }

    public NegociacaoAnimal(int idNegociacaoAnimal, int idNegociacaoGado, Double peso, Double valorKg, Double valorTotal) {
        this.idNegociacaoAnimal = idNegociacaoAnimal;
        this.idNegociacaoGado = idNegociacaoGado;
        this.peso = peso;
        this.valorKg = valorKg;
        this.valorTotal = valorTotal;
    }

    public int getIdNegociacaoAnimal() {
        return idNegociacaoAnimal;
    }

    public void setIdNegociacaoAnimal(int idNegociacaoAnimal) {
        this.idNegociacaoAnimal = idNegociacaoAnimal;
    }

    public int getIdNegociacaoGado() {
        return idNegociacaoGado;
    }

    public void setIdNegociacaoGado(int idNegociacaoGado) {
        this.idNegociacaoGado = idNegociacaoGado;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public Double getValorKg() {
        return valorKg;
    }

    public void setValorKg(Double valorKg) {
        this.valorKg = valorKg;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }
}

