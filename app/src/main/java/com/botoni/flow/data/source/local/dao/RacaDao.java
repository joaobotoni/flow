package com.botoni.flow.data.source.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.botoni.flow.data.source.local.entities.Raca;

import java.util.List;

@Dao
public interface RacaDao {

    @Query("SELECT * FROM xgp_raca")
    List<Raca> getAll();

    @Query("SELECT * FROM xgp_raca WHERE id_raca = :id")
    Raca findById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Raca Raca);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Raca> racas);

    @Update
    int update(Raca raca);

    @Delete
    int delete(Raca raca);

    @Query("DELETE FROM xgp_raca")
    void deleteAll();
}
