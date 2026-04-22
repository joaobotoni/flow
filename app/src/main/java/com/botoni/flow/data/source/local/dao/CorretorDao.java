package com.botoni.flow.data.source.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.botoni.flow.data.source.local.entities.Corretor;

import java.util.List;

@Dao
public interface CorretorDao {

    @Query("SELECT * FROM xgp_corretor")
    List<Corretor> getAll();

    @Query("SELECT * FROM xgp_corretor WHERE id_corretor = :id")
    Corretor findById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Corretor corretor);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Corretor> corretores);

    @Update
    int update(Corretor corretor);

    @Delete
    int delete(Corretor corretor);

    @Query("DELETE FROM xgp_corretor")
    void deleteAll();
}