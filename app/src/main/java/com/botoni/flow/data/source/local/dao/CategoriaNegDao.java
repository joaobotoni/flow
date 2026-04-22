package com.botoni.flow.data.source.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.botoni.flow.data.source.local.entities.CategoriaNeg;

import java.util.List;

@Dao
public interface CategoriaNegDao {

    @Query("SELECT * FROM xgp_categoria_neg")
    List<CategoriaNeg> getAll();

    @Query("SELECT * FROM xgp_categoria_neg WHERE id_categoria_neg = :id")
    CategoriaNeg findById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(CategoriaNeg categoriaNeg);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CategoriaNeg> categoriaNegs);

    @Update
    int update(CategoriaNeg categoriaNeg);

    @Delete
    int delete(CategoriaNeg categoriaNeg);

    @Query("DELETE FROM xgp_categoria_neg")
    void deleteAll();
}