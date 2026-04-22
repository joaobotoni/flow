package com.botoni.flow.data.repositories;

import com.botoni.flow.data.source.local.dao.CategoriaNegDao;
import com.botoni.flow.data.source.local.entities.CategoriaNeg;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

public class CategoriaNegRepository {

    private final CategoriaNegDao dao;

    @Inject
    public CategoriaNegRepository(CategoriaNegDao dao) {
        this.dao = dao;
    }

    public List<CategoriaNeg> getAll() {
        return dao.getAll();
    }

    public Optional<CategoriaNeg> findById(long id) {
        return Optional.ofNullable(dao.findById(id));
    }

    public long insert(CategoriaNeg categoriaNeg) {
        return dao.insert(categoriaNeg);
    }

    public void insertAll(List<CategoriaNeg> categoriaNegs) {
        dao.insertAll(categoriaNegs);
    }

    public int update(CategoriaNeg categoriaNeg) {
        return dao.update(categoriaNeg);
    }

    public int delete(CategoriaNeg categoriaNeg) {
        return dao.delete(categoriaNeg);
    }

    public void deleteAll() {
        dao.deleteAll();
    }
}