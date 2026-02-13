package com.botoni.flow.data.repositories.local;

import android.content.Context;

import com.botoni.flow.data.source.local.AppDatabase;
import com.botoni.flow.data.source.local.entities.CategoriaFrete;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import dagger.hilt.android.qualifiers.ApplicationContext;

public class CategoriaFreteRepository {
    private final AppDatabase database;

    @Inject
    public CategoriaFreteRepository(@ApplicationContext Context context) {
        this.database = AppDatabase.getDatabase(context);
    }

    public List<CategoriaFrete> getAll() {
        return database.categoriaFreteDao().getAll();
    }

    public CategoriaFrete findById(long id) {
        return database.categoriaFreteDao().findById(id);
    }

    public long insert(CategoriaFrete categoriaFrete) {
        return database.categoriaFreteDao().insert(categoriaFrete);
    }

    public void insertAll(List<CategoriaFrete> categorias) {
        database.categoriaFreteDao().insertAll(categorias);
    }

    public int update(CategoriaFrete categoriaFrete) {
        return database.categoriaFreteDao().update(categoriaFrete);
    }

    public int delete(CategoriaFrete categoriaFrete) {
        return database.categoriaFreteDao().delete(categoriaFrete);
    }

    public void deleteAll() {
        database.categoriaFreteDao().deleteAll();
    }

    public List<String> getDescricoes(){
        return database.categoriaFreteDao().getAll().stream()
                .map(CategoriaFrete::getDescricao)
                .collect(Collectors.toList());
    }

}