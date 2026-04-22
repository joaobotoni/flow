package com.botoni.flow.data.source.local.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(
        tableName = "xgp_frete",
        foreignKeys = {
                @ForeignKey(
                        entity = TipoVeiculoFrete.class,
                        parentColumns = "id_tipo_veiculo_frete",
                        childColumns = "id_tipo_veiculo_frete",
                        onDelete = ForeignKey.RESTRICT
                )
        },
        indices = {
                @Index("id_tipo_veiculo_frete")
        }
)
public class Frete {
    @ColumnInfo(name = "id_frete")
    @SerializedName(value = "ID_FRETE")
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "id_tipo_veiculo_frete")
    @SerializedName(value = "ID_TIPO_VEICULO_FRETE")
    private int idTipoVeiculoFrete;
    @ColumnInfo(name = "tipo_cobranca")
    @SerializedName(value = "TIPO_COBRANCA")
    private int tipoCobranca;
    @ColumnInfo(name = "km_inicial")
    @SerializedName(value = "KM_INICIAL")
    private double kmInicial;
    @ColumnInfo(name = "km_final")
    @SerializedName(value = "KM_FINAL")
    private double kmFinal;
    @ColumnInfo(name = "valor")
    @SerializedName(value = "VALOR")
    private double valor;


    public Frete(int idTipoVeiculoFrete, int tipoCobranca, double kmInicial, double kmFinal, double valor) {
        this.idTipoVeiculoFrete = idTipoVeiculoFrete;
        this.tipoCobranca = tipoCobranca;
        this.kmInicial = kmInicial;
        this.kmFinal = kmFinal;
        this.valor = valor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdTipoVeiculoFrete() {
        return idTipoVeiculoFrete;
    }

    public void setIdTipoVeiculoFrete(int idTipoVeiculoFrete) {
        this.idTipoVeiculoFrete = idTipoVeiculoFrete;
    }

    public double getKmInicial() {
        return kmInicial;
    }

    public void setKmInicial(double kmInicial) {
        this.kmInicial = kmInicial;
    }

    public double getKmFinal() {
        return kmFinal;
    }

    public void setKmFinal(double kmFinal) {
        this.kmFinal = kmFinal;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public int getTipoCobranca() {
        return tipoCobranca;
    }

    public void setTipoCobranca(int tipoCobranca) {
        this.tipoCobranca = tipoCobranca;
    }
}