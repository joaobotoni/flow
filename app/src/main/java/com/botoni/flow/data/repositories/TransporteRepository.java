package com.botoni.flow.data.repositories;

import com.botoni.flow.data.models.Transporte;
import com.botoni.flow.data.source.local.dao.CapacidadeFreteDao;
import com.botoni.flow.data.source.local.dao.TipoVeiculoFreteDao;
import com.botoni.flow.data.source.local.entities.CapacidadeFrete;
import com.botoni.flow.data.source.local.entities.TipoVeiculoFrete;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

public class TransporteRepository {
    private final CapacidadeFreteDao capacidadeDao;
    private final TipoVeiculoFreteDao tipoVeiculoDao;

    @Inject
    public TransporteRepository(CapacidadeFreteDao capacidadeDao, TipoVeiculoFreteDao tipoVeiculoDao) {
        this.capacidadeDao = capacidadeDao;
        this.tipoVeiculoDao = tipoVeiculoDao;
    }

    public List<CapacidadeFrete> getCapacidades(long category) {
        return capacidadeDao.findByCategoria(category);
    }

    public Optional<String> getDescricaoTipoVeiculo(long capacidade) {
        return Optional.ofNullable(tipoVeiculoDao.findById(capacidade))
                .map(TipoVeiculoFrete::getDescricao);
    }

    public List<Transporte> recomendacao(long categoria, int quantidade) {
        List<CapacidadeFrete> capacidades = getCapacidades(categoria);
        capacidades.sort(Comparator.comparingInt(CapacidadeFrete::getQtdeFinal).reversed());
        return distribuicao(capacidades, quantidade);
    }

    private List<Transporte> distribuicao(List<CapacidadeFrete> capacidades, int total) {
        List<Transporte> resultado = new ArrayList<>();
        int restante = total;
        for (CapacidadeFrete c : capacidades) {
            if (restante <= 0) break;
            int anterior = restante;
            int quantidade = 0;
            while (restante >= c.getQtdeInicial()) {
                restante -= c.getQtdeFinal();
                quantidade++;
            }
            if (quantidade > 0) {
                int carregado = anterior - Math.max(restante, 0);
                int ocupacao = Math.min(100, carregado * 100 / (quantidade * c.getQtdeFinal()));
                String descricao = getDescricaoTipoVeiculo(c.getIdTipoVeiculoFrete())
                        .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));
                resultado.add(new Transporte(
                        c.getIdTipoVeiculoFrete(),
                        descricao,
                        quantidade, c.getQtdeFinal(), ocupacao));
            }
        }
        return resultado;
    }
}