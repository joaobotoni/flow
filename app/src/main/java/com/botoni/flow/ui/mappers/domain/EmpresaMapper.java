package com.botoni.flow.ui.mappers.domain;

import com.botoni.flow.data.source.local.entities.Empresa;
import com.botoni.flow.ui.mappers.BiMapper;
import com.botoni.flow.ui.state.EmpresaUiState;

import javax.inject.Inject;

public class EmpresaMapper implements BiMapper<EmpresaUiState, Empresa> {

    @Inject
    public EmpresaMapper() {}
    @Override
    public Empresa mapTo(EmpresaUiState empresaUiState) {
        return new Empresa(empresaUiState.getId(), empresaUiState.getNome());
    }

    @Override
    public EmpresaUiState mapFrom(Empresa empresa) {
        return new EmpresaUiState(empresa.getIdEmpresa(), empresa.getNome(), false);
    }
}
