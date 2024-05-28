package com.example.tracknjeep_test.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tracknjeep_test.R;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {

    private List<String> favoriteJeepCodes;

    public FavoritesAdapter(List<String> favoriteJeepCodes) {
        this.favoriteJeepCodes = favoriteJeepCodes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_favorites, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String jeepCode = favoriteJeepCodes.get(position);
        holder.bind(jeepCode);
    }

    @Override
    public int getItemCount() {
        return favoriteJeepCodes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView jeepCodeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            jeepCodeTextView = itemView.findViewById(R.id.jeepCodeTXT);
        }

        public void bind(String jeepCode) {
            jeepCodeTextView.setText(jeepCode);
        }
    }
}
