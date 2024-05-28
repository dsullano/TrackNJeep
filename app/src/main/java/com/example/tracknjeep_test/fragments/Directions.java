package com.example.tracknjeep_test.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.DirectionsApi;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.EncodedPolyline;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.TravelMode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Directions extends BottomSheetDialogFragment implements OnMapReadyCallback {

    private String fromLocation;
    private String toLocation;
    private GoogleMap mMap;
    private TextView transitDetailsText;
    private TextView transitAvailable;
    private TextView transitETA;
    private TextView transitCost;
    private TextView transitDiscount;

    public Directions(String fromLocation, String toLocation) {
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_directions, container, false);
        transitDetailsText = view.findViewById(R.id.transit_details_top);
        transitETA = view.findViewById(R.id.transit_duration);
        transitCost = view.findViewById(R.id.transit_cost_normal);
        transitDiscount = view.findViewById(R.id.transit_cost_discount);
        return view;
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

            // Request directions and draw the route
            fetchAndDrawRoute(origin, destination);
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

    private void fetchAndDrawRoute(LatLng origin, LatLng destination) {
        new Thread(() -> {
            try {
                DirectionsResult result = DirectionsApi.newRequest(getGeoContext())
                        .origin(new com.google.maps.model.LatLng(origin.latitude, origin.longitude))
                        .destination(new com.google.maps.model.LatLng(destination.latitude, destination.longitude))
                        .mode(TravelMode.TRANSIT) // Ensure transit mode
                        .alternatives(false) // Get the primary transit route
                        .await();

                if (result.routes != null && result.routes.length > 0) {
                    DirectionsRoute route = result.routes[0];
                    EncodedPolyline polyline = route.overviewPolyline;
                    List<com.google.maps.model.LatLng> coords = polyline.decodePath();
                    List<LatLng> path = new ArrayList<>();

                    for (com.google.maps.model.LatLng coord : coords) {
                        path.add(new LatLng(coord.lat, coord.lng));
                    }

                    // Calculate total distance and duration
                    double totalDistance = 0; // in meters
                    long totalDuration = 0; // in seconds

                    for (com.google.maps.model.DirectionsLeg leg : route.legs) {
                        totalDistance += leg.distance.inMeters;
                        totalDuration += leg.duration.inSeconds;
                    }

                    final double distanceInKm = totalDistance / 1000.0; // convert to kilometers
                    long hours = totalDuration / 3600;
                    long minutes = (totalDuration % 3600) / 60;

                    getActivity().runOnUiThread(() -> {
                        if (isAdded()) {
                            PolylineOptions opts = new PolylineOptions().addAll(path).color(getResources().getColor(R.color.teal_700)).width(5);
                            mMap.addPolyline(opts);

                            // Collect all unique jeepney routes
                            Set<String> jeepneyRoutes = new HashSet<>();
                            StringBuilder transitDetails = new StringBuilder();
                            for (com.google.maps.model.DirectionsLeg leg : route.legs) {
                                for (com.google.maps.model.DirectionsStep step : leg.steps) {
                                    if (step.transitDetails != null) {
                                        LatLng transitStop = new LatLng(
                                                step.transitDetails.departureStop.location.lat,
                                                step.transitDetails.departureStop.location.lng
                                        );
                                        mMap.addMarker(new MarkerOptions()
                                                .position(transitStop)
                                                .title(step.transitDetails.line.shortName + " - " + step.transitDetails.headsign)
                                                .snippet(step.transitDetails.line.agencies[0].name)
                                        );

                                        LatLng transitEndStop = new LatLng(
                                                step.transitDetails.arrivalStop.location.lat,
                                                step.transitDetails.arrivalStop.location.lng
                                        );
                                        mMap.addMarker(new MarkerOptions()
                                                .position(transitEndStop)
                                                .title("End: " + step.transitDetails.line.shortName)
                                                .snippet(step.transitDetails.line.agencies[0].name)
                                        );

                                        if (step.transitDetails.line.shortName != null) {
                                            jeepneyRoutes.add(step.transitDetails.line.shortName);
                                        }
                                    }
                                }
                            }

                            // fetchJeepneyRoutes(fromLocation, toLocation, jeepneyRoutes);

                            for (String routeName : jeepneyRoutes) {
                                transitDetails.append(routeName).append("\n");
                            }
                            transitDetailsText.setText(transitDetails.toString());

                            String distanceDurationText = String.format("Distance: %.2f km\n\nEstimated Travel Time: %d hours %d minutes", distanceInKm, hours, minutes);
                            transitETA.setText(distanceDurationText);

                            double cost = ((distanceInKm - 4) * 1.9) + 13;
                            if (cost < 13) {
                                cost = 13;
                            }
                            double discount = cost * .80;
                            String costText = String.format("Cost: %.2f", cost);
                            String discountText = String.format("Discounted Price: %.2f", discount);
                            transitCost.setText(costText);
                            transitDiscount.setText(discountText);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void fetchJeepneyRoutes(String fromLocation, String toLocation, Set<String> jeepneyRoutes) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://tracknjeep-f4109-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("jeepneyRoutes");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String routeName = snapshot.child("routeName").getValue(String.class);
                    String startLocation = snapshot.child("startLocation").getValue(String.class);
                    String endLocation = snapshot.child("endLocation").getValue(String.class);

                    if (routeName != null && startLocation != null && endLocation != null) {
                        if (fromLocation.contains(startLocation) && toLocation.contains(endLocation)) {
                            jeepneyRoutes.add(routeName);
                        }
                    }
                }
                // Update UI with the fetched jeepney routes
                getActivity().runOnUiThread(() -> {
                    StringBuilder transitDetails = new StringBuilder();
                    for (String routeName : jeepneyRoutes) {
                        transitDetails.append(routeName).append("\n");
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Directions", "Failed to read value.", databaseError.toException());
            }
        });
    }
}