package com.botoni.flow.data.repositories;

import com.botoni.flow.data.source.local.dao.RacaDao;
import com.botoni.flow.data.source.local.entities.Raca;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

public class RacaRepository {
    private final RacaDao dao;

    @Inject
    public RacaRepository(RacaDao dao) {
        this.dao = dao;
    }

    public List<Raca> getAll() {
        return dao.getAll();
    }

    public Optional<Raca> findById(long id) {
        return Optional.ofNullable(dao.findById(id));
    }

    public long insert(Raca raca) {
        return dao.insert(raca);
    }

    public void insertAll(List<Raca> racas) {dao.insertAll(racas);}

    public int update(Raca raca) {return dao.update(raca);}

    public int delete(Raca raca) {
        return dao.delete(raca);
    }

    public void deleteAll() {
        dao.deleteAll();
    }
}

