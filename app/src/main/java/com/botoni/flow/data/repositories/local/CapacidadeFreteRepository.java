package com.botoni.flow.data.repositories.local;

import android.content.Context;

import com.botoni.flow.data.source.local.AppDatabase;
import com.botoni.flow.data.source.local.entities.CapacidadeFrete;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class CapacidadeFreteRepository {
    private final AppDatabase database;

    @Inject
    public CapacidadeFreteRepository(@ApplicationContext Context context) {
        this.database = AppDatabase.getDatabase(context);
    }

    public List<CapacidadeFrete> getAll() {
        return database.capacidadeFreteDao().getAll();
    }

    public CapacidadeFrete findById(long id) {
        return database.capacidadeFreteDao().findById(id);
    }

    public List<CapacidadeFrete> findByCategoria(long id) {
        return database.capacidadeFreteDao().findByCategoria(id);
    }

    public long insert(CapacidadeFrete capacidadeFrete) {
        return database.capacidadeFreteDao().insert(capacidadeFrete);
    }

    public void insertAll(List<CapacidadeFrete> capacidades) {
        database.capacidadeFreteDao().insertAll(capacidades);
    }

    public int update(CapacidadeFrete capacidadeFrete) {
        return database.capacidadeFreteDao().update(capacidadeFrete);
    }

    public int delete(CapacidadeFrete capacidadeFrete) {
        return database.capacidadeFreteDao().delete(capacidadeFrete);
    }

    public void deleteAll() {
        database.capacidadeFreteDao().deleteAll();
    }
}