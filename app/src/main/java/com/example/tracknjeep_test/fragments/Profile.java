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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends Fragment {

    private TextView firstNameTextView, lastNameTextView, emailTextView, genderTextView, birthdateTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        firstNameTextView = view.findViewById(R.id.textViewFirstName);
        lastNameTextView = view.findViewById(R.id.textViewLastName);
        emailTextView = view.findViewById(R.id.textViewEmail);
        genderTextView = view.findViewById(R.id.textViewGender);
        birthdateTextView = view.findViewById(R.id.textViewBirthdate);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance("https://tracknjeep-f4109-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("users").child(userId);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d("Firebase", "DataSnapshot exists: " + dataSnapshot.exists());
                    if (dataSnapshot.exists()) {
                        String firstName = dataSnapshot.child("firstName").getValue(String.class);
                        String lastName = dataSnapshot.child("lastName").getValue(String.class);
                        String email = dataSnapshot.child("email").getValue(String.class);
                        String gender = dataSnapshot.child("gender").getValue(String.class);
                        String birthdate = dataSnapshot.child("birthdate").getValue(String.class);

                        firstNameTextView.setText(firstName);
                        lastNameTextView.setText(lastName);
                        emailTextView.setText(email);
                        genderTextView.setText(gender);
                        birthdateTextView.setText(birthdate);

                        Log.d("Firebase", "DataSnapshot value: " + dataSnapshot.getValue());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Firebase", "Error fetching data: " + databaseError.getMessage());
                }
            });
        }

        return view;
    }
}
