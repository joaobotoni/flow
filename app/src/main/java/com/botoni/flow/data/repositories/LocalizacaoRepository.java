package com.botoni.flow.data.repositories;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.botoni.flow.data.models.Rota;
import com.botoni.flow.data.source.network.RoutesDataSource;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
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

    public Optional<String> buscarCodigoPais(double latitude, double longitude) throws IOException {
        return Optional.ofNullable(geocoder.getFromLocation(latitude, longitude, 1))
                .orElseGet(Collections::emptyList)
                .stream()
                .findFirst()
                .map(Address::getCountryCode);
    }

    public List<Address> buscarCidadeEstado(String consulta, String codigoPais) throws IOException {
        return Optional.ofNullable(geocoder.getFromLocationName(consulta, 10))
                .orElseGet(Collections::emptyList)
                .stream()
                .filter(e -> codigoPais == null || (e.getCountryCode() != null && e.getCountryCode().equalsIgnoreCase(codigoPais)))
                .collect(Collectors.toList());
    }

    public Optional<Address> selecionarCidadeEstado(String nomeCidade) throws IOException {
        return Optional.ofNullable(geocoder.getFromLocationName(nomeCidade, 1))
                .orElseGet(Collections::emptyList)
                .stream()
                .findFirst();
    }

    public Rota calcularRota(Address origem, Address destino) {
        return new Rota(
                cidade(origem),
                estado(origem),
                cidade(destino),
                estado(destino),
                calcularDistanciaKm(obterRota(origem, destino))
        );
    }

    public double calcularDistanciaKm(String resposta) {
        try {
            return source.parse(resposta) / 1000.0;
        } catch (Exception e) {
            throw new IllegalArgumentException("Falha ao calcular distância a partir da resposta: " + resposta, e);
        }
    }

    public String obterRota(Address origem, Address destino) {
        try {
            return source.compute(
                    new LatLng(origem.getLatitude(), origem.getLongitude()),
                    new LatLng(destino.getLatitude(), destino.getLongitude())
            );
        } catch (Exception e) {
            throw new IllegalStateException("Falha ao buscar rota de " + origem.getLocality() + " para " + destino.getLocality(), e);
        }
    }

    private String cidade(Address endereco) {
        return endereco.getLocality() != null ? endereco.getLocality()
                : endereco.getSubAdminArea() != null ? endereco.getSubAdminArea()
                : endereco.getAddressLine(0);
    }

    private String estado(Address endereco) {
        return endereco.getAdminArea() != null ? endereco.getAdminArea() : "";
    }
}