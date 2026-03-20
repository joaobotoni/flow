package com.botoni.flow.data.repositories;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.botoni.flow.data.source.network.RoutesDataSource;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;


public class LocalizacaoRepository {
    private final Geocoder geocoder;
    private final RoutesDataSource source;
    @Inject
    public LocalizacaoRepository(@ApplicationContext Context contexto) {
        this.geocoder = new Geocoder(contexto, Locale.getDefault());
        this.source = new RoutesDataSource(contexto);
    }

    public String buscarCodigoPais(double latitude, double longitude) throws IOException {
        List<Address> enderecos = geocoder.getFromLocation(latitude, longitude, 1);
        if (enderecos == null || enderecos.isEmpty()) return null;
        return enderecos.get(0).getCountryCode();
    }

    public List<Address> buscarCidadeEstado(String consulta, String codigoPais) throws IOException {
        List<Address> enderecos = geocoder.getFromLocationName(consulta, 10);
        if (enderecos == null) return Collections.emptyList();
        return enderecos.stream()
                .filter(e -> codigoPais == null
                        || e.getCountryCode() != null
                        && e.getCountryCode().equalsIgnoreCase(codigoPais))
                .collect(Collectors.toList());
    }

    public double calcularDistanciaKm(String resposta) {
        try {
            return source.parse(resposta) / 1000.0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String buscarRota(Address origem, Address destino) {
        LatLng coordenadaOrigem = new LatLng(origem.getLatitude(), origem.getLongitude());
        LatLng coordenadaDestino = new LatLng(destino.getLatitude(), destino.getLongitude());
        try {
            return source.compute(coordenadaOrigem, coordenadaDestino);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Address buscarDestino(String query) throws IOException {
        List<Address> resultados = geocoder.getFromLocationName(query, 1);
        if (resultados == null || resultados.isEmpty()) return null;
        return resultados.get(0);
    }
}