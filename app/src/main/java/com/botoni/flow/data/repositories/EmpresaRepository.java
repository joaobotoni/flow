package com.botoni.flow.data.repositories;

import com.botoni.flow.data.source.local.dao.EmpresaDao;
import com.botoni.flow.data.source.local.entities.Empresa;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

public class EmpresaRepository {

    private final EmpresaDao dao;

    @Inject
    public EmpresaRepository(EmpresaDao dao) {
        this.dao = dao;
    }

    public List<Empresa> getAll() {
        return dao.getAll();
    }

    public Optional<Empresa> findById(long id) {
        return Optional.ofNullable(dao.findById(id));
    }

    public long insert(Empresa empresa) {
        return dao.insert(empresa);
    }

    public void insertAll(List<Empresa> empresas) {
        dao.insertAll(empresas);
    }

    public int update(Empresa empresa) {
        return dao.update(empresa);
    }

    public int delete(Empresa empresa) {
        return dao.delete(empresa);
    }

    public void deleteAll() {
        dao.deleteAll();
    }
}