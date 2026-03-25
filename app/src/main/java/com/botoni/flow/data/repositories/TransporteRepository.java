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

    public List<CapacidadeFrete> listarCapacidadesPorCategoria(long idCategoria) {
        return capacidadeDao.findByCategoria(idCategoria);
    }

    public Optional<String> buscarDescricaoVeiculo(long idTipoVeiculo) {
        return Optional.ofNullable(tipoVeiculoDao.findById(idTipoVeiculo))
                .map(TipoVeiculoFrete::getDescricao);
    }

    public List<Transporte> recomendarTransportes(long idCategoria, int totalAnimais) {
        List<CapacidadeFrete> capacidades = listarCapacidadesPorCategoria(idCategoria);
        capacidades.sort(Comparator.comparingInt(CapacidadeFrete::getQtdeFinal).reversed());
        return distribuirAnimais(capacidades, totalAnimais);
    }

    private List<Transporte> distribuirAnimais(List<CapacidadeFrete> capacidades, int totalAnimais) {
        List<Transporte> transportes = new ArrayList<>();
        int restante = totalAnimais;

        for (CapacidadeFrete capacidade : capacidades) {
            if (restante <= 0) break;

            int antes = restante;
            int veiculos = 0;

            while (restante >= capacidade.getQtdeInicial()) {
                restante -= capacidade.getQtdeFinal();
                veiculos++;
            }

            if (veiculos > 0) {
                int animaisCarregados = antes - Math.max(restante, 0);
                int ocupacao = Math.min(100, animaisCarregados * 100 / (veiculos * capacidade.getQtdeFinal()));
                String descricao = buscarDescricaoVeiculo(capacidade.getIdTipoVeiculoFrete())
                        .orElseThrow(() -> new RuntimeException("Tipo de veículo não encontrado"));

                transportes.add(new Transporte(
                        capacidade.getIdTipoVeiculoFrete(),
                        descricao,
                        veiculos,
                        capacidade.getQtdeFinal(),
                        ocupacao
                ));
            }
        }

        return transportes;
    }
}