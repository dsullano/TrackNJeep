//package com.example.tracknjeep_test.fragments;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//import com.example.tracknjeep_test.R;
//import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
//
//public class bottomSheetHome extends BottomSheetDialogFragment {
//
//    private BottomSheetListener mListener;
//
//    public interface BottomSheetListener {
//        void onGoClicked();
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.bottom_sheet_layout, container, false);
//
//        Button goButton = view.findViewById(R.id.bottomSheetGoButton);
//        goButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mListener != null) {
//                    mListener.onGoClicked();
//                }
//                dismiss();
//            }
//        });
//
//        return view;
//    }
//
//    public void setBottomSheetListener(BottomSheetListener listener) {
//        mListener = listener;
//    }
//}