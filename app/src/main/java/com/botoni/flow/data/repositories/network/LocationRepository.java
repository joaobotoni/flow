package com.botoni.flow.data.repositories.network;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import androidx.core.content.ContextCompat;

import com.botoni.flow.data.source.network.RoutesDataSource;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Tasks;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

/**
 * Repositório responsável pelo gerenciamento de geolocalização e pelo processamento de endereços.
 *
 * <p>Fornece funcionalidades para obter a localização atual do usuário e realizar buscas
 * de cidades e estados, filtrando os resultados com base no país do usuário.</p>
 */
public class LocationRepository {
    private final Context context;
    private String userCountryCode;
    private final Geocoder geocoder;
    private final FusedLocationProviderClient locationClient;
    private final RoutesDataSource routesDataSource;

    @Inject
    public LocationRepository(@ApplicationContext Context context) {
        this.context = context;
        this.locationClient = LocationServices.getFusedLocationProviderClient(context);
        this.geocoder = new Geocoder(context, Locale.getDefault());
        this.routesDataSource = new RoutesDataSource(context);
    }

    public double parseDistance(String response) {
        try {
            JSONObject route = new JSONObject(response)
                    .getJSONArray("routes")
                    .getJSONObject(0);
            int meters = route.getInt("distanceMeters");
            return meters / 1000.0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Calcula a rota entre dois endereços e retorna a resposta da API.
     *
     * @param origin      endereço de origem
     * @param destination endereço de destino
     * @return JSON com os dados da rota
     */
    public String fetchRoute(Address origin, Address destination) {
        LatLng originLatLgn = new LatLng(origin.getLatitude(), origin.getLongitude());
        LatLng destinationLatLgn = new LatLng(destination.getLatitude(), destination.getLongitude());
        try {
            return routesDataSource.compute(originLatLgn, destinationLatLgn);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Realiza a busca de cidades e estados com base em uma consulta textual,
     * filtrando os resultados pelo país do usuário quando a permissão de localização está disponível.
     *
     * <p>Caso as permissões {@link Manifest.permission#ACCESS_FINE_LOCATION} ou
     * {@link Manifest.permission#ACCESS_COARSE_LOCATION} sejam concedidas, a localização atual
     * do usuário é utilizada para determinar o país e restringir os resultados.</p>
     *
     * @param query Texto utilizado como critério de busca (nome de cidade, estado, etc.).
     * @return Lista de {@link Address} correspondentes à consulta, filtrada pelo país do usuário;
     * retorna uma lista vazia caso nenhum resultado seja encontrado.
     * @throws RuntimeException se ocorrer algum erro durante a obtenção da localização ou da geocodificação.
     */
    public List<Address> searchCityAndState(String query) {
        try {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Location location = Tasks.await(locationClient.getLastLocation());
                if (location != null && Geocoder.isPresent()) {
                    List<Address> userAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (userAddresses != null && !userAddresses.isEmpty()) {
                        userCountryCode = userAddresses.get(0).getCountryCode();
                    }
                }
            }

            List<Address> results = new ArrayList<>();
            if (Geocoder.isPresent()) {
                List<Address> addresses = geocoder.getFromLocationName(query, 10);
                if (addresses != null) {
                    results = addresses.stream()
                            .filter(this::filterCountryCode)
                            .collect(Collectors.toList());
                }
            }
            return results;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Verifica se o endereço fornecido pertence ao mesmo país do usuário.
     *
     * @param address Endereço a ser verificado.
     * @return {@code true} se o código do país do endereço for nulo ou corresponder ao código do país do usuário;
     * {@code false} caso contrário.
     */
    private boolean filterCountryCode(Address address) {
        return userCountryCode == null ||
                address.getCountryCode() != null && address.getCountryCode()
                        .equalsIgnoreCase(userCountryCode);
    }
}