package com.example.tracknjeep_test.fragments;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;

import com.example.tracknjeep_test.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class Maps extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    private androidx.appcompat.widget.SearchView searchLocation;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        searchLocation = view.findViewById(R.id.search_location);

        searchLocation.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                String location = searchLocation.getQuery().toString();
                List<Address> addressList = null;

                if(location != null){
                    Geocoder geocoder = new Geocoder(requireActivity());

                    try{
                        addressList = geocoder.getFromLocationName(location,1);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                Address address = addressList.get(0);

                LatLng latlng = new LatLng(address.getLatitude(),address.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latlng).title("TADA"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.map, mapFragment)
                    .commit();
        }
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker and move the camera
        LatLng citu = new LatLng(10.29441995166696, 123.8811194524);
        mMap.addMarker(new MarkerOptions().position(citu).title("CIT-U"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(citu, 15));
    }
}
