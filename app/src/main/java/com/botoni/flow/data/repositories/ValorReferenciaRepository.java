package com.botoni.flow.data.repositories;

import com.botoni.flow.data.source.local.dao.ValorReferenciaDao;
import com.botoni.flow.data.source.local.entities.ValorReferencia;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

public class ValorReferenciaRepository {

    private final ValorReferenciaDao dao;

    @Inject
    public ValorReferenciaRepository(ValorReferenciaDao dao) {
        this.dao = dao;
    }

    public List<ValorReferencia> getAll() {
        return dao.getAll();
    }

    public Optional<ValorReferencia> findById(long id) {
        return Optional.ofNullable(dao.findById(id));
    }

    public Optional<ValorReferencia> findMaisRecente() {
        return Optional.ofNullable(dao.findMaisRecente());
    }

    public long insert(ValorReferencia valorReferencia) {
        return dao.insert(valorReferencia);
    }

    public void insertAll(List<ValorReferencia> valorReferencias) {
        dao.insertAll(valorReferencias);
    }

    public int update(ValorReferencia valorReferencia) {
        return dao.update(valorReferencia);
    }

    public int delete(ValorReferencia valorReferencia) {
        return dao.delete(valorReferencia);
    }

    public void deleteAll() {
        dao.deleteAll();
    }
}