package com.botoni.flow.data.repositories;

import com.botoni.flow.data.source.local.dao.CategoriaNegociacaoDao;
import com.botoni.flow.data.source.local.entities.CategoriaNegociacao;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

public class CategoriaNegRepository {

    private final CategoriaNegociacaoDao dao;

    @Inject
    public CategoriaNegRepository(CategoriaNegociacaoDao dao) {
        this.dao = dao;
    }

    public List<CategoriaNegociacao> getAll() {
        return dao.getAll();
    }

    public Optional<CategoriaNegociacao> findById(long id) {
        return Optional.ofNullable(dao.findById(id));
    }

    public long insert(CategoriaNegociacao categoriaNegociacao) {
        return dao.insert(categoriaNegociacao);
    }

    public void insertAll(List<CategoriaNegociacao> categoriaNegociacaos) {
        dao.insertAll(categoriaNegociacaos);
    }

    public int update(CategoriaNegociacao categoriaNegociacao) {
        return dao.update(categoriaNegociacao);
    }

    public int delete(CategoriaNegociacao categoriaNegociacao) {
        return dao.delete(categoriaNegociacao);
    }

    public void delete() {
        dao.deleteAll();
    }
}