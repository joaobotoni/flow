package com.botoni.flow.data.repositories;

import com.botoni.flow.data.models.PrecificacaoFrete;
import com.botoni.flow.data.models.Transporte;
import com.botoni.flow.data.source.local.dao.FreteDao;
import com.botoni.flow.data.source.local.entities.Frete;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

public class FreteRepository {
    private static final int CASAS_DECIMAIS = 2;
    private final FreteDao dao;
    @Inject
    public FreteRepository(FreteDao dao) {
        this.dao = dao;
    }

    public List<Frete> listar() {
        return dao.getAll();
    }

    public Optional<Frete> buscarPorId(long id) {
        return Optional.ofNullable(dao.findById(id));
    }

    public Optional<Frete> buscarPorVeiculoEDistancia(long idVeiculo, double distancia) {
        return Optional.ofNullable(dao.findByVehicleAndDistance(idVeiculo, distancia));
    }

    public long inserir(Frete frete) {
        return dao.insert(frete);
    }

    public void inserirTodos(List<Frete> fretes) {
        dao.insertAll(fretes);
    }

    public int atualizar(Frete frete) {
        return dao.update(frete);
    }

    public int remover(Frete frete) {
        return dao.delete(frete);
    }

    public void removerTodos() {
        dao.deleteAll();
    }

    public PrecificacaoFrete calcularFrete(List<Transporte> transportes, double distancia, int totalAnimais) {
        BigDecimal total = somarFretes(transportes, distancia);
        BigDecimal porAnimal = valorPorAnimal(total, totalAnimais);
        return new PrecificacaoFrete(total, porAnimal);
    }

    private BigDecimal somarFretes(List<Transporte> transportes, double distancia) {
        BigDecimal total = BigDecimal.ZERO;
        for (Transporte transporte : transportes) {
            BigDecimal subtotal = buscarPorVeiculoEDistancia(transporte.getId(), distancia)
                    .map(frete -> BigDecimal.valueOf(frete.getValor())
                            .multiply(BigDecimal.valueOf(transporte.getQuantidade())))
                    .orElse(BigDecimal.ZERO);
            total = total.add(subtotal);
        }
        return total;
    }

    private BigDecimal valorPorAnimal(BigDecimal total, int totalAnimais) {
        if (totalAnimais <= 0 || total.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
        return total.divide(BigDecimal.valueOf(totalAnimais), CASAS_DECIMAIS, RoundingMode.HALF_UP);
    }
}