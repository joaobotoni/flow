package com.botoni.flow.data.repositories;

import com.botoni.flow.data.source.local.dao.TipoReferenciaDao;
import com.botoni.flow.data.source.local.entities.TipoReferencia;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

public class TipoReferenciaRepository {

    private final TipoReferenciaDao dao;

    @Inject
    public TipoReferenciaRepository(TipoReferenciaDao dao) {
        this.dao = dao;
    }

    public List<TipoReferencia> getAll() {
        return dao.getAll();
    }

    public Optional<TipoReferencia> findById(long id) {
        return Optional.ofNullable(dao.findById(id));
    }

    public long insert(TipoReferencia tipoReferencia) {
        return dao.insert(tipoReferencia);
    }

    public void insertAll(List<TipoReferencia> tipoReferencias) {
        dao.insertAll(tipoReferencias);
    }

    public int update(TipoReferencia tipoReferencia) {
        return dao.update(tipoReferencia);
    }

    public int delete(TipoReferencia tipoReferencia) {
        return dao.delete(tipoReferencia);
    }

    public void deleteAll() {
        dao.deleteAll();
    }
}