package com.logixhunt.shikhaaqua.ui.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.logixhunt.shikhaaqua.R;
import com.logixhunt.shikhaaqua.database.DatabaseClient;
import com.logixhunt.shikhaaqua.databinding.ActivityLocationBinding;
import com.logixhunt.shikhaaqua.model.database.AddressModel;
import com.logixhunt.shikhaaqua.ui.common.BaseActivity;
import com.logixhunt.shikhaaqua.utils.Constant;
import com.logixhunt.shikhaaqua.utils.PreferenceUtils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback, LocationListener, GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnCameraIdleListener{

    private String TAG = LocationActivity.class.getSimpleName();
    private ActivityLocationBinding binding;

    private GoogleMap googleMap;

    private LocationRequest mLocationRequest;
    private static final long INTERVAL = 1000 * 5;
    private static final long FASTEST_INTERVAL = 1000;
    private LocationManager locationManager;
    boolean gps_enabled, network_enabled;

    private GoogleApiClient mGoogleApiClient;
    private Location newLocation;

    SupportMapFragment mapFragment;

    private String currentAddress = "";
    private String otherAddress = "";
    private String currentPin = "";
    private String currentLat = "";
    private String currentLng = "";

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gps_enabled = false;
        network_enabled = false;

        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }

        if (!gps_enabled && !network_enabled) {
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            Task<LocationSettingsResponse> result =
                    LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());

            result.addOnCompleteListener(task -> {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    // All location settings are satisfied. The client can initialize location
                    // requests here.
                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the
                            // user a dialog.
                            try {
                                // Cast to a resolvable exception.
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                resolvable.startResolutionForResult(
                                        LocationActivity.this,
                                        LocationRequest.PRIORITY_HIGH_ACCURACY);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            } catch (ClassCastException e) {
                                // Ignore, should be an impossible error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.
                            break;
                    }
                }
            });
        }

        //do your work
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

        mapFragment.onCreate(savedInstanceState);
        mapFragment.onResume();

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapFragment.getMapAsync(mMap -> {
            googleMap = mMap;
            if (ActivityCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            googleMap.setMyLocationEnabled(false);
            googleMap.setBuildingsEnabled(true);
//            googleMap.setOnMapClickListener(AddressActivity.this);

            //added by mukul
            googleMap.setOnCameraMoveListener(LocationActivity.this);
            googleMap.setOnCameraMoveStartedListener(LocationActivity.this);
            googleMap.setOnCameraIdleListener(LocationActivity.this);

            googleMap.getUiSettings().setZoomControlsEnabled(true);

//            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        });

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        createLocationRequest();

        getPreferenceData();
        initialization();
    }

    private void getPreferenceData() {

    }

    private void initialization() {
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.etAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validate();
            }
        });

        binding.btnUpdateAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.etAddress.getText().toString().isEmpty()){
//                    currentAddress = binding.etAddress.getText().toString();
                    otherAddress = binding.etAddress.getText().toString();
                }else {
                    otherAddress = currentAddress;
                }
                PreferenceUtils.setString(Constant.PreferenceConstant.LAT,currentLat,LocationActivity.this);

                Log.d("TAG", "preferenceLAT3" + PreferenceUtils.getString(Constant.PreferenceConstant.LAT, LocationActivity.this));

                PreferenceUtils.setString(Constant.PreferenceConstant.LNG,currentLng,LocationActivity.this);

                Log.d("TAG", "preferenceLNG3" + PreferenceUtils.getString(Constant.PreferenceConstant.LNG, LocationActivity.this));

                PreferenceUtils.setString(Constant.PreferenceConstant.ADDRESS,currentAddress,LocationActivity.this);
                PreferenceUtils.setString(Constant.PreferenceConstant.OTHER_ADDRESS,otherAddress,LocationActivity.this);
                PreferenceUtils.setString(Constant.PreferenceConstant.PINCODE,currentPin,LocationActivity.this);

                AddressModel cart = new AddressModel(binding.etAddressTitle.getText().toString(), currentPin, otherAddress, currentLat, currentLng);
                AsyncTask.execute(() -> {
                    DatabaseClient.getInstance(LocationActivity.this).getAppDatabase().addressDao().insert(cart);
                });

                showAlert("Address Saved Successfully");

                onBackPressed();

//                Intent intent = new Intent(LocationActivity.this, ScheduleActivity.class);
//                intent.putExtra(Constant.BundleExtras.ADDRESS, currentAddress);
//                intent.putExtra(Constant.BundleExtras.PINCODE, currentPin);
//                intent.putExtra(Constant.BundleExtras.LAT, currentLat);
//                intent.putExtra(Constant.BundleExtras.LNG, currentLng);
//                startActivity(intent);
            }
        });

    }

    private void validate() {
        if (!binding.etAddress.getText().toString().isEmpty() && !binding.etPinCode.getText().toString().isEmpty()) {
            binding.btnUpdateAddress.setEnabled(true);
        } else {
            binding.btnUpdateAddress.setEnabled(false);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;


        LatLng latLng = new LatLng(newLocation.getLatitude(), newLocation.getLongitude());

        googleMap.addMarker(new MarkerOptions().position(latLng));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
        googleMap.animateCamera(cameraUpdate);


    }


    @Override
    public void onResume() {
        super.onResume();
        mapFragment.onResume();
        mGoogleApiClient.connect();

        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        stopLocationUpdates();
        mapFragment.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapFragment.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapFragment.onLowMemory();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LocationRequest.PRIORITY_HIGH_ACCURACY) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    //                    Log.e("Location On-Off", "onActivityResult: GPS Enabled by user");
                    mGoogleApiClient = new GoogleApiClient.Builder(LocationActivity.this)
                            .addApi(LocationServices.API)
                            .addConnectionCallbacks(this)
                            .addOnConnectionFailedListener(this)
                            .build();
                    createLocationRequest();
                    break;
                case Activity.RESULT_CANCELED:

                    break;
                default:
                    break;
            }
        }
    }


    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, LocationActivity.this);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, LocationActivity.this);

        //        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }


    @Override
    public void onCameraIdle() {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(LocationActivity.this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(googleMap.getCameraPosition().target.latitude, googleMap.getCameraPosition().target.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = "", pinCode = "";
            if (addresses.size() > 0) {
                address = addresses.get(0).getAddressLine(0);
                pinCode = addresses.get(0).getPostalCode();
            }




            PreferenceUtils.setString(
                    Constant.PreferenceConstant.LAT,
                    String.valueOf(googleMap.getCameraPosition().target.latitude),
                    this);

            Log.d("TAG", "preferenceLAT2" + PreferenceUtils.getString(Constant.PreferenceConstant.LAT, LocationActivity.this));


            PreferenceUtils.setString(
                    Constant.PreferenceConstant.LNG,
                    String.valueOf(googleMap.getCameraPosition().target.longitude),
                    this);

            Log.d("TAG", "preferenceLNG2" + PreferenceUtils.getString(Constant.PreferenceConstant.LNG, LocationActivity.this));

            currentAddress = address;
            currentPin = pinCode;
            currentLng = String.valueOf(googleMap.getCameraPosition().target.longitude);
            currentLat = String.valueOf(googleMap.getCameraPosition().target.latitude);



            binding.etAddress.setText(address);
            binding.etPinCode.setText(pinCode);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onCameraMove() {

    }

    @Override
    public void onCameraMoveStarted(int i) {
        startLocationUpdates();
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        newLocation = location;
        googleMap.clear();
        if (binding.etAddress.getText().toString().isEmpty()) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//        googleMap.addMarker(new MarkerOptions().position(latLng));
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
            googleMap.animateCamera(cameraUpdate);
        stopLocationUpdates();



            PreferenceUtils.setString(
                    Constant.PreferenceConstant.LAT,
                    String.valueOf(location.getLatitude()),
                    this);

            Log.d("TAG", "preferenceLAT" + PreferenceUtils.getString(Constant.PreferenceConstant.LAT, LocationActivity.this));

            PreferenceUtils.setString(
                    Constant.PreferenceConstant.LNG,
                    String.valueOf(location.getLongitude()),
                    this);

            Log.d("TAG", "preferenceLNG" + PreferenceUtils.getString(Constant.PreferenceConstant.LNG, LocationActivity.this));

            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(LocationActivity.this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                String address = addresses.get(0).getAddressLine(0);
                String pinCode = addresses.get(0).getPostalCode();

                currentAddress = address;
                currentPin = pinCode;
                currentLng = String.valueOf(location.getLongitude());
                currentLat = String.valueOf(location.getLatitude());


                binding.etAddress.setText(address);
                binding.etPinCode.setText(pinCode);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        validate();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void stopLocationUpdates() {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

}