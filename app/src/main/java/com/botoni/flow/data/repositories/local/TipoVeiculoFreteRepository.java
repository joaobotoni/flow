package com.botoni.flow.data.repositories.local;

import android.content.Context;

import com.botoni.flow.data.source.local.AppDatabase;
import com.botoni.flow.data.source.local.entities.TipoVeiculoFrete;

import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.qualifiers.ApplicationContext;

public class TipoVeiculoFreteRepository {
    private final AppDatabase database;

    @Inject
    public TipoVeiculoFreteRepository(@ApplicationContext Context context) {
        this.database = AppDatabase.getDatabase(context);
    }

    public List<TipoVeiculoFrete> getAll() {
        return database.tipoVeiculoFreteDao().getAll();
    }

    public TipoVeiculoFrete findById(long id) {
        return database.tipoVeiculoFreteDao().findById(id);
    }

    public long insert(TipoVeiculoFrete tipoVeiculo) {
        return database.tipoVeiculoFreteDao().insert(tipoVeiculo);
    }

    public void insertAll(List<TipoVeiculoFrete> tiposVeiculo) {
        database.tipoVeiculoFreteDao().insertAll(tiposVeiculo);
    }

    public int update(TipoVeiculoFrete tipoVeiculo) {
        return database.tipoVeiculoFreteDao().update(tipoVeiculo);
    }

    public int delete(TipoVeiculoFrete tipoVeiculo) {
        return database.tipoVeiculoFreteDao().delete(tipoVeiculo);
    }

    public void deleteAll() {
        database.tipoVeiculoFreteDao().deleteAll();
    }
}