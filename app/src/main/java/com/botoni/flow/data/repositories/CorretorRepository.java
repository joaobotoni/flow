package com.botoni.flow.data.repositories;

import com.botoni.flow.data.source.local.dao.CorretorDao;
import com.botoni.flow.data.source.local.entities.Corretor;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

public class CorretorRepository {

    private final CorretorDao dao;

    @Inject
    public CorretorRepository(CorretorDao dao) {
        this.dao = dao;
    }

    public List<Corretor> getAll() {
        return dao.getAll();
    }

    public Optional<Corretor> findById(long id) {
        return Optional.ofNullable(dao.findById(id));
    }

    public long insert(Corretor corretor) {
        return dao.insert(corretor);
    }

    public void insertAll(List<Corretor> corretores) {
        dao.insertAll(corretores);
    }

    public int update(Corretor corretor) {
        return dao.update(corretor);
    }

    public int delete(Corretor corretor) {
        return dao.delete(corretor);
    }

    public void deleteAll() {
        dao.deleteAll();
    }
}