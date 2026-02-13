package com.botoni.flow.data.repositories.network;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class GeoLocationRepository {
    private final Context context;
    private String userCountryCode;
    private final Geocoder geocoder;
    private final FusedLocationProviderClient locationClient;

    @Inject
    public GeoLocationRepository(@ApplicationContext Context context) {
        this.context = context;
        this.locationClient = LocationServices.getFusedLocationProviderClient(context);
        this.geocoder = new Geocoder(context, Locale.getDefault());
    }

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

    private boolean filterCountryCode(Address address) {
        return userCountryCode == null ||
                address.getCountryCode() != null && address.getCountryCode()
                        .equalsIgnoreCase(userCountryCode);
    }
}