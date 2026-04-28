package com.botoni.flow.data.source.remote.retrofit.routes;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RoutesApiService {

    @POST("directions/v2:computeRoutes")
    Call<String> computeRoutes(
            @Header("X-Goog-Api-Key") String apiKey,
            @Header("X-Goog-FieldMask") String fieldMask,
            @Body RequestBody body
    );
}
