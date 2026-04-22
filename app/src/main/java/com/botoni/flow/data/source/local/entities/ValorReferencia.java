package com.botoni.flow.data.source.local.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(
        tableName = "xgp_valor_referencia",
        foreignKeys = {
                @ForeignKey(
                        entity = TipoReferencia.class,
                        parentColumns = "id_tipo_referencia",
                        childColumns = "id_tipo_referencia",
                        onDelete = ForeignKey.RESTRICT
                ),
                @ForeignKey(
                        entity = Empresa.class,
                        parentColumns = "id_empresa",
                        childColumns = "id_empresa",
                        onDelete = ForeignKey.RESTRICT
                ),
        }
)
public class ValorReferencia {
    @PrimaryKey
    @ColumnInfo(name = "id_valor_referencia")
    @SerializedName("ID_VALOR_REFERENCIA")
    private int idValorReferencia;

    @ColumnInfo(name = "id_tipo_referencia")
    @SerializedName("ID_TIPO_REFERENCIA")
    private int idTipoReferencia;

    @ColumnInfo(name = "id_empresa")
    @SerializedName("ID_EMPRESA")
    private int idEmpresa;

    @ColumnInfo(name = "data_referencia")
    @SerializedName("DATA_REFERENCIA")
    private Date DataReferencia;

    @ColumnInfo(name = "valor_arroba_boi")
    @SerializedName("VALOR_ARROBA_BOI")
    private Double valorArrobaBoi;

    @ColumnInfo(name = "valor_bezerro")
    @SerializedName("VALOR_BEZERRO")
    private Double valorBezerro;

    @ColumnInfo(name = "peso_bezerro")
    @SerializedName("PESO_BEZERRO")
    private Integer pesoBezerro;

    @ColumnInfo(name = "valor_arroba_vaca")
    @SerializedName("VALOR_ARROBA_VACA")
    private Double valorArrobaVaca;

    @ColumnInfo(name = "valor_bezerra")
    @SerializedName("VALOR_BEZERRA")
    private Double valorBezerra;

    @ColumnInfo(name = "peso_bezerra")
    @SerializedName("PESO_BEZERRA")
    private Integer pesoBezerra;

    public ValorReferencia() {
    }

    public ValorReferencia(int idValorReferencia, int idTipoReferencia, int idEmpresa, Date dataReferencia, Double valorArrobaBoi, Double valorBezerro, Integer pesoBezerro, Double valorArrobaVaca, Double valorBezerra, Integer pesoBezerra) {
        this.idValorReferencia = idValorReferencia;
        this.idTipoReferencia = idTipoReferencia;
        this.idEmpresa = idEmpresa;
        DataReferencia = dataReferencia;
        this.valorArrobaBoi = valorArrobaBoi;
        this.valorBezerro = valorBezerro;
        this.pesoBezerro = pesoBezerro;
        this.valorArrobaVaca = valorArrobaVaca;
        this.valorBezerra = valorBezerra;
        this.pesoBezerra = pesoBezerra;
    }

    public int getIdValorReferencia() {
        return idValorReferencia;
    }

    public void setIdValorReferencia(int idValorReferencia) {
        this.idValorReferencia = idValorReferencia;
    }

    public int getIdTipoReferencia() {
        return idTipoReferencia;
    }

    public void setIdTipoReferencia(int idTipoReferencia) {
        this.idTipoReferencia = idTipoReferencia;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public Date getDataReferencia() {
        return DataReferencia;
    }

    public void setDataReferencia(Date dataReferencia) {
        DataReferencia = dataReferencia;
    }

    public Double getValorArrobaBoi() {
        return valorArrobaBoi;
    }

    public void setValorArrobaBoi(Double valorArrobaBoi) {
        this.valorArrobaBoi = valorArrobaBoi;
    }

    public Double getValorBezerro() {
        return valorBezerro;
    }

    public void setValorBezerro(Double valorBezerro) {
        this.valorBezerro = valorBezerro;
    }

    public Integer getPesoBezerro() {
        return pesoBezerro;
    }

    public void setPesoBezerro(Integer pesoBezerro) {
        this.pesoBezerro = pesoBezerro;
    }

    public Double getValorArrobaVaca() {
        return valorArrobaVaca;
    }

    public void setValorArrobaVaca(Double valorArrobaVaca) {
        this.valorArrobaVaca = valorArrobaVaca;
    }

    public Double getValorBezerra() {
        return valorBezerra;
    }

    public void setValorBezerra(Double valorBezerra) {
        this.valorBezerra = valorBezerra;
    }

    public Integer getPesoBezerra() {
        return pesoBezerra;
    }

    public void setPesoBezerra(Integer pesoBezerra) {
        this.pesoBezerra = pesoBezerra;
    }
}