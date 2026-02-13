package com.botoni.flow.data.repositories.local;

import android.content.Context;

import com.botoni.flow.data.source.local.AppDatabase;
import com.botoni.flow.data.source.local.entities.Frete;

import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.qualifiers.ApplicationContext;

public class FreteRepository {
    private final AppDatabase database;

    @Inject
    public FreteRepository(@ApplicationContext Context context) {
        this.database = AppDatabase.getDatabase(context);
    }

    public List<Frete> getAll() {
        return database.freteDao().getAll();
    }

    public Frete findById(long id) {
        return database.freteDao().findById(id);
    }

    public Frete findByValueInRange(double range) {
        return database.freteDao().findByValueInRange(range);
    }

    public long insert(Frete frete) {
        return database.freteDao().insert(frete);
    }

    public void insertAll(List<Frete> fretes) {
        database.freteDao().insertAll(fretes);
    }

    public int update(Frete frete) {
        return database.freteDao().update(frete);
    }

    public int delete(Frete frete) {
        return database.freteDao().delete(frete);
    }

    public void deleteAll() {
        database.freteDao().deleteAll();
    }
}