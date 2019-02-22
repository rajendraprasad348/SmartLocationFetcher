package com.rajendra_prasad.smartlocationfetcherlibary;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class LocationUpdater {

    Context mContext;
    ProgressDialog pdLoading;
    LocationUpdateCallbacks locationUpdateCallbacks;

    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    public static final int REQUEST_CHECK_SETTINGS_DEFAULT = 100;
    public static final int REQUEST_CHECK_SETTINGS_CURRENT_LOCATION = 101;
    private static final String MESSAGE_LOADING_LOCATION = "Please wait...Retrieving location";

    // bunch of location related apis
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;

    public LocationUpdater(Context context, LocationUpdateCallbacks locationUpdateCallbacks) {
        mContext = context;
        this.locationUpdateCallbacks = locationUpdateCallbacks;
        initLoader();
        initLocationHandlers();
    }

    private void initLoader() {
        pdLoading = new ProgressDialog(mContext);
        pdLoading.setMessage(MESSAGE_LOADING_LOCATION);
        pdLoading.setCancelable(false);
    }

    private void initLocationHandlers() {

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
        mSettingsClient = LocationServices.getSettingsClient(mContext);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                hideLoadingIndicator();
                mCurrentLocation = locationResult.getLastLocation();
                locationUpdateCallbacks.onLocationRetrieved(mCurrentLocation);
                stopLocationUpdates();
            }
        };

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setNumUpdates(1);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    void showLoadingIndicator(String message) {
        if (!((Activity) mContext).isFinishing())
            if (pdLoading != null && !pdLoading.isShowing()) {
                pdLoading.setMessage(message);
                pdLoading.show();
            }
    }

    void hideLoadingIndicator() {
        if (!((Activity) mContext).isFinishing())
            if (pdLoading != null && pdLoading.isShowing()) {
                pdLoading.dismiss();
            }
    }

    public void startLocationUpdates(final boolean isRetrieveCurrentLocation) {

        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener((Activity) mContext, new OnSuccessListener<LocationSettingsResponse>() {

                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {

                      /*  if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }*/

                        checkLocationPermission();
                    }
                })
                .addOnFailureListener((Activity) mContext, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        //Toast.makeText(getApplicationContext(), "failed location updates!", Toast.LENGTH_SHORT).show();
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    if (isRetrieveCurrentLocation) {
                                        rae.startResolutionForResult((Activity) mContext, REQUEST_CHECK_SETTINGS_CURRENT_LOCATION);

                                    } else {
                                        rae.startResolutionForResult((Activity) mContext, REQUEST_CHECK_SETTINGS_DEFAULT);
                                    }

                                } catch (IntentSender.SendIntentException sie) {
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings and Try again.";
                                Toast.makeText(mContext, errorMessage, Toast.LENGTH_LONG).show();
                                break;
                        }

                    }
                });
    }

    public void stopLocationUpdates() {
        // Removing location updates
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    private void checkLocationPermission() {
        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyHaveLocationPermission()) {
                requestForLocationPermission();

            } else {
                showLoadingIndicator(MESSAGE_LOADING_LOCATION);
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.getMainLooper());
            }
        } else {
            showLoadingIndicator(MESSAGE_LOADING_LOCATION);
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.getMainLooper());
        }
    }

    private boolean checkIfAlreadyHaveLocationPermission() {
        int result = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestForLocationPermission() {
        ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1002);
    }

}
