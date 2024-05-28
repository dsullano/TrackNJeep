package com.example.tracknjeep_test.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tracknjeep_test.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class JeepInfoBottomSheet extends BottomSheetDialogFragment {

    private static final String ARG_JEEP_CODE = "jeep_code";

    private String jeepCode;
    private TextView destinationTextView;
    private TextView routesTextView;

    public static JeepInfoBottomSheet newInstance(String jeepCode) {
        JeepInfoBottomSheet fragment = new JeepInfoBottomSheet();
        Bundle args = new Bundle();
        args.putString(ARG_JEEP_CODE, jeepCode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            jeepCode = getArguments().getString(ARG_JEEP_CODE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jeep_info_bottom_sheet, container, false);

        destinationTextView = view.findViewById(R.id.destinationTextView);
        routesTextView = view.findViewById(R.id.routesTextView);

        fetchJeepInfo();

        return view;
    }

    private void fetchJeepInfo() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://tracknjeep-f4109-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("tblJeepney").child(jeepCode);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String destination = dataSnapshot.child("destination").getValue(String.class);
                    String routes = dataSnapshot.child("routes").getValue(String.class);

                    if (destination != null && routes != null) {
                        destinationTextView.setText(destination);
                        routesTextView.setText(routes);
                    } else {
                        Toast.makeText(getContext(), "Destination or routes information not available", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Jeepney code not found", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to fetch data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
