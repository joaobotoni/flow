package com.botoni.flow.data.source.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.botoni.flow.data.source.local.entities.Empresa;

import java.util.List;

@Dao
public interface EmpresaDao {

    @Query("SELECT * FROM empresa")
    List<Empresa> getAll();

    @Query("SELECT * FROM empresa WHERE id_empresa = :id")
    Empresa findById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Empresa empresa);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Empresa> empresas);

    @Update
    int update(Empresa empresa);

    @Delete
    int delete(Empresa empresa);

    @Query("DELETE FROM empresa")
    void deleteAll();
}