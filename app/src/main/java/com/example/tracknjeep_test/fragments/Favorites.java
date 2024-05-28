package com.example.tracknjeep_test.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tracknjeep_test.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Favorites extends Fragment {

    private RecyclerView recyclerView;
    private TextView jeepCodeTXT;
    private List<String> favoriteJeepCodes = new ArrayList<>();
    private FavoritesAdapter favoritesAdapter;
    private FirebaseUser currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        jeepCodeTXT = view.findViewById(R.id.jeepCodeTXT);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            favoritesAdapter = new FavoritesAdapter(favoriteJeepCodes);
            recyclerView.setAdapter(favoritesAdapter);
            fetchFavoriteJeepCodes();
        }

        return view;
    }

    private void fetchFavoriteJeepCodes() {
        String userId = currentUser.getUid();
        DatabaseReference favoritesRef = FirebaseDatabase.getInstance("https://tracknjeep-f4109-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("favorites");
        Query query = favoritesRef.orderByChild("userId").equalTo(userId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                favoriteJeepCodes.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String jeepCode = snapshot.child("jeepCode").getValue(String.class);
                    if (jeepCode != null) {
                        favoriteJeepCodes.add(jeepCode);
                    }
                }
                Log.d("Favorites", "Number of favorites: " + favoriteJeepCodes.size());
                favoritesAdapter.notifyDataSetChanged();
                if (favoriteJeepCodes.isEmpty()) {
                    jeepCodeTXT.setText("No Favorites");
                    jeepCodeTXT.setVisibility(View.VISIBLE);
                } else {
                    jeepCodeTXT.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to fetch favorites: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
