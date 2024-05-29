package com.example.tracknjeep_test.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class AboutDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("About")
                .setMessage("TrackNJeep is an android application that tracks jeepneys and their " +
                        "respective routes for users to locate and know which jeepney code they want " +
                        "to board on to reach their destination\n\n" +
                        "TrackNJeep also offers displaying the route of the jeepney itself, all YOU need " +
                        "to do is input a jeepney code, and TrackNJeep will display that jeepney's route for YOU.")
                .setPositiveButton("Back", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Close the dialog
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }
}
