package com.logixhunt.shikhaaqua.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.logixhunt.shikhaaqua.R;

import java.util.Arrays;

public class GooglePlaceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_google_place);

        // Initialize Places API
//        Places.initialize(getApplicationContext(), "AIzaSyBzLYBtmne5PfiyveMN1_IseV_j6K1D1X4");
//
//        // Create a new Places client instance
//        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
////                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment1);
//
//        // Specify the types of place data to return
//        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
//
//        // Set up a PlaceSelectionListener to handle the selected place
//        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(@NonNull Place place) {
//                // Handle the selected place
//                Log.i("PlaceAutocomplete", "Place: " + place.getName() + ", " + place.getId());
//            }
//
//            @Override
//            public void onError(@NonNull Status status) {
//                // Handle the error
//                Log.i("PlaceAutocomplete", "An error occurred: " + status);
//            }
//        });
    }
}