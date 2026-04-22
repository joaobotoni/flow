package com.botoni.flow.data.source.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.botoni.flow.data.source.local.entities.NegociacaoAnimal;

import java.util.List;

@Dao
public interface NegociacaoAnimalDao {

    @Query("SELECT * FROM xgp_negociacao_animal")
    List<NegociacaoAnimal> getAll();

    @Query("SELECT * FROM xgp_negociacao_animal WHERE id_negociacao_animal = :id1 AND id_negociacao_gado = :id2")
    NegociacaoAnimal findById(long id1, long id2);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(NegociacaoAnimal negociacaoAnimal);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<NegociacaoAnimal> negociacaoAnimals);

    @Update
    int update(NegociacaoAnimal negociacaoAnimal);

    @Delete
    int delete(NegociacaoAnimal negociacaoAnimal);

    @Query("DELETE FROM xgp_negociacao_animal")
    void deleteAll();
}