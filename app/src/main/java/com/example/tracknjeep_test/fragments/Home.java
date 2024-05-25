package com.example.tracknjeep_test.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.tracknjeep_test.R;

public class Home extends Fragment {

    private EditText fromTxt;
    private EditText toTxt;
    private Button goBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        fromTxt = rootView.findViewById(R.id.fromTxtEdit);
        toTxt = rootView.findViewById(R.id.toTextEdit);

        goBtn = rootView.findViewById(R.id.goBtn);

        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fromText = fromTxt.getText().toString().trim();
                String toText = toTxt.getText().toString().trim();

                if (fromText.isEmpty() || toText.isEmpty()) {
                    showAlert();
                } else {
                    getDirections(fromText, toText);
                }
            }
        });

        return rootView;
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
        Directions directionsFragment = new Directions(from, to);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.frame_layout_container, directionsFragment)
                .addToBackStack(null)
                .commit();
    }
}
