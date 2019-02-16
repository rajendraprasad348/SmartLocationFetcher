package com.rajendra_prasad.smartlocationfetcherlibary;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SmartLocationFetcher {
    private GPSTracker gps;
    private static final String TAG = "TAG";
    public static ArrayList<AppDetailsModel> AppDetailsList = new ArrayList<>();
    private Activity activity;
    private OnLocationGetListener mListener;

    public interface OnLocationGetListener {
        void onLocationReady(Location location);

        void onError(String error);
    }

    public SmartLocationFetcher(Activity activity, @NonNull OnLocationGetListener listener) {
        this.activity = activity;
        this.mListener = listener;
    }

    // checking any GPS spoofing apps present or not
    public Location fetchSmartLocation(Context context) {
        Location UserLocation = null;
        try {
            if (isMockSettingsON(context)) {
                if (areThereMockPermissionApps(context)) {
                    showFragment(context);
                }
            } else if (areThereMockPermissionApps(context)) {
                showFragment(context);
            } else {
                UserLocation = currentLocation(context);
                mListener.onLocationReady(UserLocation);
            }
        } catch (Exception e) {
            Log.d(TAG, "fetchUserSmartLocation: " + e);
            mListener.onError("" + e);
        }
        return UserLocation;
    }


    private void showFragment(Context context) {
        FragmentActivity fragmentActivity = (FragmentActivity) context;
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        AppDetailsFragment tv = new AppDetailsFragment();
        tv.show(fragmentManager, "AppDetailsList");
    }

    // get Current Location
    private Location currentLocation(Context context) {
        Double lattitude = null, longitude = null;
        Location location = null;

        try {
            gps = new GPSTracker(context);
            if (gps.canGetLocation()) {

                lattitude = gps.getLatitude();
                location = gps.getLocation();
                longitude = gps.getLongitude();

            } else {
                gps.showSettingsAlert();
            }
        } catch (Exception e) {
            Log.d(TAG, "currentLocation: " + e);
        }
        return location;
    }

    private boolean isMockSettingsON(Context context) {
        // returns true if mock location enabled, false if not enabled.
        if (Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ALLOW_MOCK_LOCATION).equals("0"))
            return false;
        else
            return true;
    }

    private boolean areThereMockPermissionApps(Context context) {
        int count = 0;

        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> packages =
                pm.getInstalledApplications(PackageManager.GET_META_DATA);

        AppDetailsList.clear();
        for (ApplicationInfo applicationInfo : packages) {
            try {
                PackageInfo packageInfo = pm.getPackageInfo(applicationInfo.packageName,
                        PackageManager.GET_PERMISSIONS);

                // Get Permissions
                String[] requestedPermissions = packageInfo.requestedPermissions;

                if (requestedPermissions != null) {
                    for (int i = 0; i < requestedPermissions.length; i++) {
                        if (requestedPermissions[i].equals("android.permission.ACCESS_MOCK_LOCATION")
                                && !applicationInfo.packageName.equals(context.getPackageName())) {

                            count++;
                            // to get the pkage name and app name
                            try {
                                applicationInfo = pm.getApplicationInfo(applicationInfo.packageName, 0);
                            } catch (final PackageManager.NameNotFoundException e) {
                            }
                            final String title = (String) ((applicationInfo != null) ? pm.getApplicationLabel(applicationInfo) : "???");

                            // to get app icon
                            Drawable icon = null;
                            try {
                                icon = context.getPackageManager().getApplicationIcon(applicationInfo.packageName);
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }

                            // passing to adapter
                            AppDetailsModel appDetailsModelList = new AppDetailsModel();
                            appDetailsModelList.setIcon(icon);
                            appDetailsModelList.setAppname(title);
                            appDetailsModelList.setPackagename(applicationInfo.packageName);
                            AppDetailsList.add(appDetailsModelList);
                        }
                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
                Log.e("Got exception ", e.getMessage());
            }
        }
        if (count > 0)
            return true;
        return false;
    }
}
