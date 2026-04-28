package com.botoni.flow.data.source.remote.retrofit.gespec;

import retrofit2.Call;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Url;

public interface GespecTipoReferenciaApiService {

    @HTTP(method = "POST", hasBody = false)
    Call<String> sync(
            @Url String url,
            @Header("Content-Type") String contentType,
            @Header("DATASOURCE_DEFAULT") String datasource,
            @Header("USUARIO_LOGADO") String usuarioLogado
    );
}
