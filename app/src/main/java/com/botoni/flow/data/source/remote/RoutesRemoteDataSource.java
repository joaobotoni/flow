package com.botoni.flow.data.source.remote;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;

import com.botoni.flow.R;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class RoutesRemoteDataSource {
    private static final String URL = "https://routes.googleapis.com/directions/v2:computeRoutes";
    private static final String MASK = "routes.distanceMeters";
    private final String apiKey;

    public RoutesRemoteDataSource(@ApplicationContext Context context) {
        this.apiKey = load(context);
    }

    public String compute(@NonNull LatLng origin, @NonNull LatLng destination) throws Exception {
        String body = build(origin, destination);
        return fetch(body);
    }

    public int parse(String json) throws Exception {
        return new JSONObject(json)
                .getJSONArray("routes")
                .getJSONObject(0)
                .getInt("distanceMeters");
    }

    private static String load(@NonNull Context context) {
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            String key = info.metaData.getString(context.getString(R.string.chave_api_google_maps));
            if (key == null || key.isEmpty())
                throw new IllegalStateException(context.getString(R.string.erro_chave_api_ausente));
            return key;
        } catch (PackageManager.NameNotFoundException e) {
            throw new IllegalStateException(context.getString(R.string.erro_chave_api_ausente));
        }
    }

    private static String build(@NonNull LatLng origin, @NonNull LatLng destination) throws Exception {
        return new JSONObject()
                .put("origin", toWaypoint(origin))
                .put("destination", toWaypoint(destination))
                .put("travelMode", "DRIVE")
                .put("routingPreference", "TRAFFIC_AWARE_OPTIMAL")
                .put("units", "METRIC")
                .toString();
    }

    private String fetch(@NonNull String body) throws Exception {
        HttpURLConnection urlConnection = (HttpURLConnection) new URL(URL).openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setRequestProperty("X-Goog-Api-Key", apiKey);
        urlConnection.setRequestProperty("X-Goog-FieldMask", MASK);
        urlConnection.setDoOutput(true);
        urlConnection.getOutputStream().write(body.getBytes());

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
            return sb.toString();
        } finally {
            urlConnection.disconnect();
        }
    }

    private static JSONObject toWaypoint(@NonNull LatLng latLng) throws Exception {
        JSONObject json = new JSONObject()
                .put("latitude", latLng.latitude)
                .put("longitude", latLng.longitude);

        JSONObject location = new JSONObject()
                .put("latLng", json);

        return new JSONObject()
                .put("location", location);
    }
}