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

    public Optional<Frete> findByValueInRange(long id, double range) {
        return Optional.ofNullable(dao.findByVehicleAndDistance(id, range));
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

    public void deleteAll() {
        dao.deleteAll();
    }

    public PrecificacaoFrete calcularFrete(List<Transporte> transportes, double distancia, int totalAnimais) {
        BigDecimal total = calcularTotalFrete(transportes, distancia);
        BigDecimal porAnimal = calcularFretePorAnimal(total, totalAnimais);
        return new PrecificacaoFrete(total, porAnimal);
    }

    private BigDecimal calcularTotalFrete(List<Transporte> transportes, double distancia) {
        BigDecimal total = BigDecimal.ZERO;
        for (Transporte transporte : transportes) {
            BigDecimal subtotal = findByValueInRange(transporte.getId(), distancia)
                    .map(frete -> BigDecimal.valueOf(frete.getValor())
                            .multiply(BigDecimal.valueOf(transporte.getQuantidade())))
                    .orElse(BigDecimal.ZERO);
            total = total.add(subtotal);
        }
        return total;
    }

    private BigDecimal calcularFretePorAnimal(BigDecimal total, int totalAnimais) {
        if (totalAnimais <= 0 || total.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
        return total.divide(BigDecimal.valueOf(totalAnimais), 2, RoundingMode.HALF_UP);
    }
}