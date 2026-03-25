package com.botoni.flow.data.repositories;

import android.location.Address;
import android.location.Geocoder;

import com.botoni.flow.data.models.Rota;
import com.botoni.flow.data.source.network.RoutesDataSource;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

public class LocalizacaoRepository {

    private static final int BUSCA_MAXIMA = 10;
    private static final int BUSCA_UNICA = 1;
    private static final double METROS_POR_KM = 1000.0;
    private final Geocoder geocoder;
    private final RoutesDataSource rotasDataSource;

    @Inject
    public LocalizacaoRepository(Geocoder geocoder, RoutesDataSource rotasDataSource) {
        this.geocoder = geocoder;
        this.rotasDataSource = rotasDataSource;
    }

    public Optional<String> paisDeCoordenadas(double lat, double lng) throws IOException {
        return geocodificarCoordenadas(lat, lng, BUSCA_UNICA)
                .stream()
                .findFirst()
                .map(Address::getCountryCode);
    }

    public List<Address> enderecosPorTexto(String consulta, String pais) throws IOException {
        return geocodificarNome(consulta, BUSCA_MAXIMA)
                .stream()
                .filter(e -> mesmoPais(e, pais))
                .collect(Collectors.toList());
    }

    public Optional<Address> enderecoPorNome(String nome) throws IOException {
        return geocodificarNome(nome, BUSCA_UNICA)
                .stream()
                .findFirst();
    }

    public Rota calcularRota(Address origem, Address destino) {
        String respostaRota = buscarRota(origem, destino);
        double distanciaKm = converterDistanciaKm(respostaRota);

        return new Rota(
                cidade(origem), estado(origem),
                cidade(destino), estado(destino),
                distanciaKm
        );
    }


    private String buscarRota(Address origem, Address destino) {
        try {
            return rotasDataSource.compute(
                    new LatLng(origem.getLatitude(), origem.getLongitude()),
                    new LatLng(destino.getLatitude(), destino.getLongitude())
            );
        } catch (Exception e) {
            throw new IllegalStateException(
                    String.format("Falha ao buscar rota de '%s' para '%s'.", cidade(origem), cidade(destino)), e
            );
        }
    }

    private double converterDistanciaKm(String resposta) {
        try {
            return rotasDataSource.parse(resposta) / METROS_POR_KM;
        } catch (Exception e) {
            throw new IllegalArgumentException("Falha ao converter distância: " + resposta, e);
        }
    }

    private List<Address> geocodificarCoordenadas(double lat, double lng, int max) throws IOException {
        return Optional.ofNullable(geocoder.getFromLocation(lat, lng, max))
                .orElseGet(Collections::emptyList);
    }

    private List<Address> geocodificarNome(String nome, int max) throws IOException {
        return Optional.ofNullable(geocoder.getFromLocationName(nome, max))
                .orElseGet(Collections::emptyList);
    }

    private boolean mesmoPais(Address endereco, String pais) {
        return pais == null || (endereco.getCountryCode() != null && endereco.getCountryCode().equalsIgnoreCase(pais));
    }

    private String cidade(Address endereco) {
        if (endereco.getLocality() != null)     return endereco.getLocality();
        if (endereco.getSubAdminArea() != null) return endereco.getSubAdminArea();
        return endereco.getAddressLine(0);
    }

    private String estado(Address endereco) {
        return endereco.getAdminArea() != null ? endereco.getAdminArea() : "";
    }
}