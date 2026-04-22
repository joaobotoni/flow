package com.botoni.flow.data.source.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.botoni.flow.data.source.local.entities.TipoReferencia;

import java.util.List;

@Dao
public interface TipoReferenciaDao {

    @Query("SELECT * FROM xgp_tipo_referencia")
    List<TipoReferencia> getAll();

    @Query("SELECT * FROM xgp_tipo_referencia WHERE id_tipo_referencia = :id")
    TipoReferencia findById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(TipoReferencia tipoReferencia);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<TipoReferencia> tipoReferencias);

    @Update
    int update(TipoReferencia tipoReferencia);

    @Delete
    int delete(TipoReferencia tipoReferencia);

    @Query("DELETE FROM xgp_tipo_referencia")
    void deleteAll();
}