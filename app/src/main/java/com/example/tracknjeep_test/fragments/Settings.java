package com.example.tracknjeep_test.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.example.tracknjeep_test.MainActivity;
import com.example.tracknjeep_test.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.atomic.AtomicBoolean;

public class Settings extends Fragment {
    public Settings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AtomicBoolean switchClicked = new AtomicBoolean(false);

        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        LinearLayout llDarkMode = rootView.findViewById(R.id.llDarkMode);
        Switch switchDarkMode = rootView.findViewById(R.id.switchDarkMode);
        View.OnClickListener toggleSwitchListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == switchDarkMode.getId()) {
                    // Set the flag to true if the switch was clicked
                    switchClicked.set(true);
                }

                // Toggle the switch only if it's currently off
                if (!switchDarkMode.isChecked() && !switchClicked.get()) {
                    switchDarkMode.setChecked(true);
                } else if (!switchClicked.get()) {
                    switchDarkMode.setChecked(false);
                }

                // Reset the flag after processing the click event
                switchClicked.set(false);
            }
        };

        llDarkMode.setOnClickListener(toggleSwitchListener);
        switchDarkMode.setOnClickListener(toggleSwitchListener);

        LinearLayout llAbout = rootView.findViewById(R.id.llAbout);
        llAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "About Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        LinearLayout llPrivacyPolicy = rootView.findViewById(R.id.llPrivacyPolicy);
        llPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Privacy Policy Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        LinearLayout llLogOut = rootView.findViewById(R.id.llLogOut);
        llLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
                Toast.makeText(getContext(), "Successfully Logged Out", Toast.LENGTH_SHORT).show();

            }
        });

        return rootView;
    }



}