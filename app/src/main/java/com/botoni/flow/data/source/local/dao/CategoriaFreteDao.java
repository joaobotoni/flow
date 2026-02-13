package com.botoni.flow.data.source.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import com.botoni.flow.data.source.local.entities.CategoriaFrete;

import java.util.List;

@Dao
public interface CategoriaFreteDao {
    @Query("SELECT * FROM xgp_categoria_frete ORDER BY descricao")
    List<CategoriaFrete> getAll();
    @Query("SELECT * FROM xgp_categoria_frete WHERE id_categoria_frete = :id")
    CategoriaFrete findById(long id);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(CategoriaFrete categoriaFrete);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CategoriaFrete> categorias);
    @Update
    int update(CategoriaFrete categoriaFrete);
    @Delete
    int delete(CategoriaFrete categoriaFrete);
    @Query("DELETE FROM xgp_categoria_frete")
    void deleteAll();
}