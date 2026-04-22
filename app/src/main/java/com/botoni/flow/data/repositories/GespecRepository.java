package com.botoni.flow.data.repositories;

import com.botoni.flow.data.models.Configuration;
import com.botoni.flow.data.source.network.gespec.GespecSyncAcessoService;
import com.botoni.flow.data.source.network.gespec.GespecSyncUsuarioService;

import java.io.IOException;

import javax.inject.Inject;

public class GespecRepository {

    private final GespecSyncAcessoService acessoService;
    private final GespecSyncUsuarioService usuarioService;

    @Inject
    public GespecRepository(
            GespecSyncAcessoService acessoService,
            GespecSyncUsuarioService usuarioService
    ) {
        this.acessoService = acessoService;
        this.usuarioService = usuarioService;
    }

    public String syncUsuario(Configuration configuration) throws IOException {
        return usuarioService.sync(configuration);
    }

    public String syncAcessoUsuario(Configuration configuration) throws IOException {
        return acessoService.sync(configuration);
    }
}