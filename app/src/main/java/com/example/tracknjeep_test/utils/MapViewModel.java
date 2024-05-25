package com.example.tracknjeep_test.utils;

import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;

public class MapViewModel extends ViewModel {
    private final SavedStateHandle savedStateHandle;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_MARKER_POSITION = "marker_position";
    private static final String KEY_MARKER_TITLE = "marker_title";
    private static final String KEY_ZOOM_LEVEL = "zoom_level";

    public MapViewModel(SavedStateHandle savedStateHandle) {
        this.savedStateHandle = savedStateHandle;
    }

    public void setCameraPosition(LatLng position) {
        savedStateHandle.set(KEY_CAMERA_POSITION, position);
    }

    public LatLng getCameraPosition() {
        return savedStateHandle.get(KEY_CAMERA_POSITION);
    }

    public void setMarkerPosition(LatLng position) {
        savedStateHandle.set(KEY_MARKER_POSITION, position);
    }

    public LatLng getMarkerPosition() {
        return savedStateHandle.get(KEY_MARKER_POSITION);
    }

    public void setMarkerTitle(String title) {
        savedStateHandle.set(KEY_MARKER_TITLE, title);
    }

    public String getMarkerTitle() {
        return savedStateHandle.get(KEY_MARKER_TITLE);
    }

    public void setZoomLevel(float zoomLevel) {
        savedStateHandle.set(KEY_ZOOM_LEVEL, zoomLevel);
    }

    public Float getZoomLevel() {
        return savedStateHandle.get(KEY_ZOOM_LEVEL);
    }
}
