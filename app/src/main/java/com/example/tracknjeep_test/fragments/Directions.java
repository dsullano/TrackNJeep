package com.example.tracknjeep_test.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tracknjeep_test.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;


public class Directions extends Fragment implements OnMapReadyCallback {

    private String fromLocation;
    private String toLocation;
    private GoogleMap mMap;

    public Directions(String fromLocation, String toLocation) {
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_directions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng origin = getLocationFromAddress(fromLocation);
        LatLng destination = getLocationFromAddress(toLocation);
        if (origin != null && destination != null) {
            mMap.addMarker(new MarkerOptions().position(origin).title("From"));
            mMap.addMarker(new MarkerOptions().position(destination).title("To"));
            LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
            boundsBuilder.include(origin);
            boundsBuilder.include(destination);
            LatLngBounds bounds = boundsBuilder.build();
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        }
    }

    private LatLng getLocationFromAddress(String address) {
        try {
            GeocodingResult[] results = GeocodingApi.geocode(getGeoContext(), address).await();
            if (results != null && results.length > 0) {
                return new LatLng(results[0].geometry.location.lat, results[0].geometry.location.lng);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private GeoApiContext getGeoContext() {
        return new GeoApiContext.Builder()
                .apiKey("AIzaSyDDzj6tlqEkIG3sKfBB9mJ_2FHYin26vzE")
                .build();
    }
}
