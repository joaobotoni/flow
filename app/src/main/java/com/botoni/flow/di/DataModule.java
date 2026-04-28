package com.botoni.flow.di;

import android.content.Context;

import com.botoni.flow.data.source.local.AppDatabase;
import com.botoni.flow.data.source.local.dao.CapacidadeFreteDao;
import com.botoni.flow.data.source.local.dao.CategoriaFreteDao;
import com.botoni.flow.data.source.local.dao.CategoriaNegociacaoDao;
import com.botoni.flow.data.source.local.dao.CorretorDao;
import com.botoni.flow.data.source.local.dao.EmpresaDao;
import com.botoni.flow.data.source.local.dao.FreteDao;
import com.botoni.flow.data.source.local.dao.NegociacaoAnimalDao;
import com.botoni.flow.data.source.local.dao.NegociacaoGadoDao;
import com.botoni.flow.data.source.local.dao.RacaDao;
import com.botoni.flow.data.source.local.dao.TipoReferenciaDao;
import com.botoni.flow.data.source.local.dao.TipoVeiculoFreteDao;
import com.botoni.flow.data.source.local.dao.ValorReferenciaDao;
import com.botoni.flow.data.source.remote.RoutesRemoteDataSource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;


@Module
@InstallIn(SingletonComponent.class)
public class DataModule {

    @Provides
    @Singleton
    public FreteDao provideFreteDao(@ApplicationContext Context context) {
        return AppDatabase.getDatabase(context).freteDao();
    }

    @Provides
    @Singleton
    public CategoriaFreteDao provideCategoriaFreteDao(@ApplicationContext Context context) {
        return AppDatabase.getDatabase(context).categoriaFreteDao();
    }

    @Provides
    @Singleton
    public CapacidadeFreteDao provideCapacidadeFreteDao(@ApplicationContext Context context) {
        return AppDatabase.getDatabase(context).capacidadeFreteDao();
    }

    @Provides
    @Singleton
    public TipoVeiculoFreteDao provideTipoVeiculoFreteDao(@ApplicationContext Context context) {
        return AppDatabase.getDatabase(context).tipoVeiculoFreteDao();
    }

    @Provides
    @Singleton
    public CategoriaNegociacaoDao provideCategoriaNegDao(@ApplicationContext Context context) {
        return AppDatabase.getDatabase(context).categoriaNegDao();
    }

    @Provides
    @Singleton
    public CorretorDao provideCorretorDao(@ApplicationContext Context context) {
        return AppDatabase.getDatabase(context).corretorDao();
    }

    @Provides
    @Singleton
    public EmpresaDao provideEmpresaDao(@ApplicationContext Context context) {
        return AppDatabase.getDatabase(context).empresaDao();
    }

    @Provides
    @Singleton
    public NegociacaoGadoDao provideNegociacaoGadoDao(@ApplicationContext Context context) {
        return AppDatabase.getDatabase(context).negociacaoGadoDao();
    }

    @Provides
    @Singleton
    public NegociacaoAnimalDao provideNegociacaoAnimalDao(@ApplicationContext Context context) {
        return AppDatabase.getDatabase(context).negociacaoAnimalDao();
    }

    @Provides
    @Singleton
    public ValorReferenciaDao provideValorReferenciaDao(@ApplicationContext Context context) {
        return AppDatabase.getDatabase(context).valorReferenciaDao();
    }

    @Provides
    @Singleton
    public TipoReferenciaDao provideTipoReferenciaDao(@ApplicationContext Context context) {
        return AppDatabase.getDatabase(context).tipoReferenciaDao();
    }


    @Provides
    @Singleton
    public RacaDao provideRacaDao(@ApplicationContext Context context) {
        return AppDatabase.getDatabase(context).racaDao();
    }

    @Provides
    @Singleton
    public RoutesRemoteDataSource provideRoutesDataSource(@ApplicationContext Context context) {
        return new RoutesRemoteDataSource(context);
    }
}
