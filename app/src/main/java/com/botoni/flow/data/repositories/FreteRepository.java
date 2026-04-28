package com.botoni.flow.data.repositories;

import static com.botoni.flow.utils.BigDecimalUtil.ARREDONDAMENTO_FINANCEIRO;
import static com.botoni.flow.utils.BigDecimalUtil.ESCALA_CALCULO;
import static com.botoni.flow.utils.BigDecimalUtil.ESCALA_MONETARIA;

import com.botoni.flow.data.models.PrecificacaoFrete;
import com.botoni.flow.data.models.Transporte;
import com.botoni.flow.data.source.local.dao.FreteDao;
import com.botoni.flow.data.source.local.entities.Frete;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

public class FreteRepository {
    private final FreteDao dao;

    @Inject
    public FreteRepository(FreteDao dao) {
        this.dao = dao;
    }

    public List<Frete> getAll() {
        return dao.getAll();
    }

    public Optional<Frete> findById(long id) {
        return Optional.ofNullable(dao.findById(id));
    }

    public Optional<Frete> buscarPorVeiculoEDistancia(long idVeiculo, double distancia) {
        return Optional.ofNullable(dao.findByVehicleAndDistance(idVeiculo, distancia));
    }

    public long insert(Frete frete) {
        return dao.insert(frete);
    }

    public void insertAll(List<Frete> fretes) {
        dao.insertAll(fretes);
    }

    public int update(Frete frete) {
        return dao.update(frete);
    }

    public int delete(Frete frete) {
        return dao.delete(frete);
    }

    public void delete() {
        dao.deleteAll();
    }

    public PrecificacaoFrete calcularFrete(List<Transporte> transportes, double distancia, int cargaTotal) {
        BigDecimal totalFrete = calcularFreteTotal(transportes, distancia);
        BigDecimal valorParcial = calcularIncidenciaFretePorAnimal(totalFrete, cargaTotal);
        return new PrecificacaoFrete(totalFrete, valorParcial);
    }

    public BigDecimal calcularFreteTotal(List<Transporte> transportes, double distancia) {
        BigDecimal total = BigDecimal.ZERO;
        for (Transporte transporte : transportes) {
            Frete frete = buscarPorVeiculoEDistancia(transporte.getId(), distancia)
                    .orElseThrow(() -> new RuntimeException("Nenhum frete configurado para o veículo: " + transporte.getNomeVeiculo()));
            BigDecimal custoUnitario = calcularCustoUnitario(frete, distancia);
            BigDecimal quantidade = BigDecimal.valueOf(transporte.getQuantidade());
            BigDecimal subtotal = custoUnitario.multiply(quantidade);
            total = total.add(subtotal);
        }

        return total.setScale(ESCALA_MONETARIA, ARREDONDAMENTO_FINANCEIRO);
    }

    public BigDecimal calcularIncidenciaFretePorAnimal(BigDecimal valorTotalFrete, int cargaTotal) {
        BigDecimal quantidadeAnimais = BigDecimal.valueOf(cargaTotal);
        return valorTotalFrete.divide(quantidadeAnimais, ESCALA_CALCULO, ARREDONDAMENTO_FINANCEIRO)
                .setScale(ESCALA_MONETARIA, ARREDONDAMENTO_FINANCEIRO);
    }

    public BigDecimal calcularCustoUnitario(Frete frete, double distancia) {
        BigDecimal valorBase = BigDecimal.valueOf(frete.getValor());
        if (frete.getTipoCobranca() == 1) {
            BigDecimal multiplicadorDistancia = BigDecimal.valueOf(distancia);
            return valorBase.multiply(multiplicadorDistancia);
        }
        return valorBase;
    }
}