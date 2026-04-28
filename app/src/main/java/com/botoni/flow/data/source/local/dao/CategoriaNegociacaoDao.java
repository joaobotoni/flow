package com.botoni.flow.data.source.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.botoni.flow.data.source.local.entities.CategoriaNegociacao;

import java.util.List;

@Dao
public interface CategoriaNegociacaoDao {

    @Query("SELECT * FROM xgp_categoria_neg")
    List<CategoriaNegociacao> getAll();

    @Query("SELECT * FROM xgp_categoria_neg WHERE id_categoria_neg = :id")
    CategoriaNegociacao findById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(CategoriaNegociacao categoriaNegociacao);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CategoriaNegociacao> categoriaNegociacaos);

    @Update
    int update(CategoriaNegociacao categoriaNegociacao);

    @Delete
    int delete(CategoriaNegociacao categoriaNegociacao);

    @Query("DELETE FROM xgp_categoria_neg")
    void deleteAll();
}