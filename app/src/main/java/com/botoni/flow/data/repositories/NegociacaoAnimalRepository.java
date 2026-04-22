package com.botoni.flow.data.repositories;

import com.botoni.flow.data.source.local.dao.NegociacaoAnimalDao;
import com.botoni.flow.data.source.local.entities.NegociacaoAnimal;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

public class NegociacaoAnimalRepository {
    private final NegociacaoAnimalDao dao;
    @Inject
    public NegociacaoAnimalRepository(NegociacaoAnimalDao dao) {
        this.dao = dao;
    }

    public List<NegociacaoAnimal> getAll() {
        return dao.getAll();
    }

    public Optional<NegociacaoAnimal> findById(long id1, long id2) {
        return Optional.ofNullable(dao.findById(id1, id2));
    }

    public long insert(NegociacaoAnimal negociacaoAnimal) {
        return dao.insert(negociacaoAnimal);
    }

    public void insertAll(List<NegociacaoAnimal> negociacaoAnimals) {
        dao.insertAll(negociacaoAnimals);
    }

    public int update(NegociacaoAnimal negociacaoAnimal) {
        return dao.update(negociacaoAnimal);
    }

    public int delete(NegociacaoAnimal negociacaoAnimal) {
        return dao.delete(negociacaoAnimal);
    }

    public void deleteAll() {
        dao.deleteAll();
    }
}