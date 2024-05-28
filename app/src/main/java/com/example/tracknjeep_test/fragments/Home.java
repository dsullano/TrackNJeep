package com.example.tracknjeep_test.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.tracknjeep_test.R;
import com.example.tracknjeep_test.auth.Register;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class Home extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

        Button showBottomSheetButton = view.findViewById(R.id.show_bottom_sheet_button);
        showBottomSheetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet();
            }
        });

        Button showJeepButton = view.findViewById(R.id.showJeepBtn);
        showJeepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showJeepCodeInputDialog();
            }
        });

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng citu = new LatLng(10.29441995166696, 123.8811194524);
        mMap.addMarker(new MarkerOptions().position(citu).title("CIT-U"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(citu, 15));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title("New Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            }
        });
    }

    private void showBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        View bottomSheetView = getLayoutInflater().inflate(R.layout.activity_bottom_sheet_home, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        EditText fromTxt = bottomSheetView.findViewById(R.id.fromTxtEdit);
        EditText toTxt = bottomSheetView.findViewById(R.id.toTextEdit);
        Button goBtn = bottomSheetView.findViewById(R.id.goBtn);

        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fromText = fromTxt.getText().toString().trim();
                String toText = toTxt.getText().toString().trim();

                if (fromText.isEmpty() || toText.isEmpty()) {
                    showAlert();
                } else {
                    getDirections(fromText, toText);
                    bottomSheetDialog.dismiss();
                }
            }

            private void showAlert() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Alert");
                builder.setMessage("Please enter both 'From' and 'To' locations.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }

            private void getDirections(String from, String to) {
                Directions directionsBottomSheet = new Directions(from,to);
                directionsBottomSheet.show(getParentFragmentManager(), "DirectionsBottomSheet");

            }
        });

        bottomSheetDialog.show();
    }

    private void showJeepCodeInputDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        View bottomSheetView = getLayoutInflater().inflate(R.layout.jeep_bottom_sheet, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        EditText findText = bottomSheetView.findViewById(R.id.findTxtEdit);
        Button fetchJeepBtn = bottomSheetView.findViewById(R.id.goBtn);

        fetchJeepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jeepCode = findText.getText().toString().trim();
                if (!jeepCode.isEmpty()) {
                    JeepInfoBottomSheet jeepInfoBottomSheet = JeepInfoBottomSheet.newInstance(jeepCode);
                    jeepInfoBottomSheet.show(getParentFragmentManager(), "JeepInfoBottomSheet");
                    bottomSheetDialog.dismiss();
                } else {
                    Toast.makeText(getContext(), "Please enter a valid jeepney code.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bottomSheetDialog.show();
    }
}