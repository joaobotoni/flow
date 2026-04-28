package com.botoni.flow.data.source.remote.retrofit.gespec;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Url;

public interface GespecEmpresaApiService {

    @GET
    Call<String> sync(
            @Url String url,
            @Header("Content-Type") String contentType
    );
}
