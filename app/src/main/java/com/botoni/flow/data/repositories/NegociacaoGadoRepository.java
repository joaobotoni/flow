package com.botoni.flow.data.repositories;

import com.botoni.flow.data.source.local.dao.NegociacaoGadoDao;
import com.botoni.flow.data.source.local.entities.NegociacaoGado;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

public class NegociacaoGadoRepository {

    private final NegociacaoGadoDao dao;

    @Inject
    public NegociacaoGadoRepository(NegociacaoGadoDao dao) {
        this.dao = dao;
    }

    public List<NegociacaoGado> getAll() {
        return dao.getAll();
    }

    public Optional<NegociacaoGado> findById(long id) {
        return Optional.ofNullable(dao.findById(id));
    }

    public long insert(NegociacaoGado negociacaoGado) {
        return dao.insert(negociacaoGado);
    }

    public void insertAll(List<NegociacaoGado> negociacaoGados) {
        dao.insertAll(negociacaoGados);
    }

    public int update(NegociacaoGado negociacaoGado) {
        return dao.update(negociacaoGado);
    }

    public int delete(NegociacaoGado negociacaoGado) {
        return dao.delete(negociacaoGado);
    }

    public void deleteAll() {
        dao.deleteAll();
    }
}