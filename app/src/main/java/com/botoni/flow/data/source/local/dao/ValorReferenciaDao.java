package com.botoni.flow.data.source.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.botoni.flow.data.source.local.entities.ValorReferencia;

import java.util.List;

@Dao
public interface ValorReferenciaDao {

    @Query("SELECT * FROM xgp_valor_referencia")
    List<ValorReferencia> getAll();

    @Query("SELECT * FROM xgp_valor_referencia WHERE id_valor_referencia = :id")
    ValorReferencia findById(long id);

    @Query("SELECT * FROM xgp_valor_referencia ORDER BY data_referencia DESC LIMIT 1")
    ValorReferencia findMaisRecente();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(ValorReferencia valorReferencia);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ValorReferencia> valorReferencias);

    @Update
    int update(ValorReferencia valorReferencia);

    @Delete
    int delete(ValorReferencia valorReferencia);

    @Query("DELETE FROM xgp_valor_referencia")
    void deleteAll();
}