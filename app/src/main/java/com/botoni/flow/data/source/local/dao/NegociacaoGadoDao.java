package com.botoni.flow.data.source.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.botoni.flow.data.source.local.entities.NegociacaoGado;

import java.util.List;

@Dao
public interface NegociacaoGadoDao {

    @Query("SELECT * FROM xgp_negociacao_gado")
    List<NegociacaoGado> getAll();

    @Query("SELECT * FROM xgp_negociacao_gado WHERE id_negociacao_gado = :id")
    NegociacaoGado findById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(NegociacaoGado negociacaoGado);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<NegociacaoGado> negociacaoGados);

    @Update
    int update(NegociacaoGado negociacaoGado);

    @Delete
    int delete(NegociacaoGado negociacaoGado);

    @Query("DELETE FROM xgp_negociacao_gado")
    void deleteAll();
}