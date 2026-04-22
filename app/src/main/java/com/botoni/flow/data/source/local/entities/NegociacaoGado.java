package com.botoni.flow.data.source.local.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(
        tableName = "xgp_negociacao_gado",
        foreignKeys = {
                @ForeignKey(
                        entity = Empresa.class,
                        parentColumns = "id_empresa",
                        childColumns = "id_empresa",
                        onDelete = ForeignKey.RESTRICT
                ),
                @ForeignKey(
                        entity = Corretor.class,
                        parentColumns = "id_corretor",
                        childColumns = "id_corretor",
                        onDelete = ForeignKey.RESTRICT
                ),
                @ForeignKey(
                        entity = CategoriaNeg.class,
                        parentColumns = "id_categoria_neg",
                        childColumns = "id_categoria_neg",
                        onDelete = ForeignKey.RESTRICT
                )
        },
        indices = {
                @Index("id_empresa"),
                @Index("id_corretor"),
                @Index("id_categoria_neg")
        }
)
public class NegociacaoGado {

    @PrimaryKey
    @ColumnInfo(name = "id_negociacao_gado")
    @SerializedName("ID_NEGOCIACAO_GADO")
    private int idNegociacaoGado;

    @ColumnInfo(name = "id_empresa")
    @SerializedName("ID_EMPRESA")
    private int idEmpresa;

    @ColumnInfo(name = "id_corretor")
    @SerializedName("ID_CORRETOR")
    private int idCorretor;

    @ColumnInfo(name = "id_categoria_neg")
    @SerializedName("ID_CATEGORIA_NEG")
    private int idCategoriaNeg;

    @ColumnInfo(name = "descricao")
    @SerializedName("DESCRICAO")
    private String descricao;

    @ColumnInfo(name = "data_negociacao")
    @SerializedName("DATA_NEGOCIACAO")
    private Date dataNegociacao;

    @ColumnInfo(name = "valor_kg_ref")
    @SerializedName("VALOR_KG_REF")
    private Double valorKgRef;

    @ColumnInfo(name = "valor_cab_ref")
    @SerializedName("VALOR_CAB_REF")
    private Double valorCabRef;

    @ColumnInfo(name = "perc_agio_ref")
    @SerializedName("PERC_AGIO_REF")
    private Double percAgioRef;

    @ColumnInfo(name = "peso_medio")
    @SerializedName("PESO_MEDIO")
    private Double pesoMedio;

    @ColumnInfo(name = "qtde_animais")
    @SerializedName("QTDE_ANIMAIS")
    private Integer qtdeAnimais;

    @ColumnInfo(name = "tipo_valor")
    @SerializedName("TIPO_VALOR")
    private String tipoValor;

    @ColumnInfo(name = "valor_animal")
    @SerializedName("VALOR_ANIMAL")
    private Double valorAnimal;

    @ColumnInfo(name = "valor_total")
    @SerializedName("VALOR_TOTAL")
    private Double valorTotal;

    @ColumnInfo(name = "valor_frete")
    @SerializedName("VALOR_FRETE")
    private Double valorFrete;

    @ColumnInfo(name = "valor_comissao")
    @SerializedName("VALOR_COMISSAO")
    private Double valorComissao;

    @ColumnInfo(name = "valor_cab_neg")
    @SerializedName("VALOR_CAB_NEG")
    private Double valorCabNeg;

    @ColumnInfo(name = "valor_kg_neg")
    @SerializedName("VALOR_KG_NEG")
    private Double valorKgNeg;

    @ColumnInfo(name = "perc_agio_neg")
    @SerializedName("PERC_AGIO_NEG")
    private Double percAgioNeg;

    @ColumnInfo(name = "status")
    @SerializedName("STATUS")
    private String status;

    public NegociacaoGado() {
    }

    public int getIdNegociacaoGado() {
        return idNegociacaoGado;
    }

    public void setIdNegociacaoGado(int idNegociacaoGado) {
        this.idNegociacaoGado = idNegociacaoGado;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public int getIdCorretor() {
        return idCorretor;
    }

    public void setIdCorretor(int idCorretor) {
        this.idCorretor = idCorretor;
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

    public Date getDataNegociacao() {
        return dataNegociacao;
    }

    public void setDataNegociacao(Date dataNegociacao) {
        this.dataNegociacao = dataNegociacao;
    }

    public Double getValorKgRef() {
        return valorKgRef;
    }

    public void setValorKgRef(Double valorKgRef) {
        this.valorKgRef = valorKgRef;
    }

    public Double getValorCabRef() {
        return valorCabRef;
    }

    public void setValorCabRef(Double valorCabRef) {
        this.valorCabRef = valorCabRef;
    }

    public Double getPercAgioRef() {
        return percAgioRef;
    }

    public void setPercAgioRef(Double percAgioRef) {
        this.percAgioRef = percAgioRef;
    }

    public Double getPesoMedio() {
        return pesoMedio;
    }

    public void setPesoMedio(Double pesoMedio) {
        this.pesoMedio = pesoMedio;
    }

    public Integer getQtdeAnimais() {
        return qtdeAnimais;
    }

    public void setQtdeAnimais(Integer qtdeAnimais) {
        this.qtdeAnimais = qtdeAnimais;
    }

    public String getTipoValor() {
        return tipoValor;
    }

    public void setTipoValor(String tipoValor) {
        this.tipoValor = tipoValor;
    }

    public Double getValorAnimal() {
        return valorAnimal;
    }

    public void setValorAnimal(Double valorAnimal) {
        this.valorAnimal = valorAnimal;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Double getValorFrete() {
        return valorFrete;
    }

    public void setValorFrete(Double valorFrete) {
        this.valorFrete = valorFrete;
    }

    public Double getValorComissao() {
        return valorComissao;
    }

    public void setValorComissao(Double valorComissao) {
        this.valorComissao = valorComissao;
    }

    public Double getValorCabNeg() {
        return valorCabNeg;
    }

    public void setValorCabNeg(Double valorCabNeg) {
        this.valorCabNeg = valorCabNeg;
    }

    public Double getValorKgNeg() {
        return valorKgNeg;
    }

    public void setValorKgNeg(Double valorKgNeg) {
        this.valorKgNeg = valorKgNeg;
    }

    public Double getPercAgioNeg() {
        return percAgioNeg;
    }

    public void setPercAgioNeg(Double percAgioNeg) {
        this.percAgioNeg = percAgioNeg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}