package com.botoni.flow.data.repositories;

import com.botoni.flow.data.models.Transporte;
import com.botoni.flow.data.source.local.dao.CapacidadeFreteDao;
import com.botoni.flow.data.source.local.dao.TipoVeiculoFreteDao;
import com.botoni.flow.data.source.local.entities.CapacidadeFrete;
import com.botoni.flow.data.source.local.entities.TipoVeiculoFrete;

import java.util.ArrayList;
import java.util.List;

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

    public TipoVeiculoFrete getTipoVeiculo(long capacidade) {
        return tipoVeiculoDao.findById(capacidade);
    }

    public List<Transporte> recomendacao(long category, int quantity) {
        List<CapacidadeFrete> capacities = getCapacidades(category);
        capacities.sort((a, b) -> Integer.compare(b.getQtdeFinal(), a.getQtdeFinal()));
        return distribuicao(capacities, quantity);
    }

    private List<Transporte> distribuicao(List<CapacidadeFrete> capacities, int total) {
        List<Transporte> result = new ArrayList<>();
        int remaining = total;
        for (CapacidadeFrete c : capacities) {
            if (remaining <= 0) break;
            int before = remaining;
            int count = 0;
            while (remaining >= c.getQtdeInicial()) {
                remaining -= c.getQtdeFinal();
                count++;
            }
            if (count > 0) {
                int loaded = before - Math.max(remaining, 0);
                int ocupacao = Math.min(100, loaded * 100 / (count * c.getQtdeFinal()));
                result.add(new Transporte(
                        c.getIdTipoVeiculoFrete(),
                        getTipoVeiculo(c.getIdTipoVeiculoFrete()).getDescricao(),
                        count, c.getQtdeFinal(), ocupacao));
            }
        }
        return result;
    }
}