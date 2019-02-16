package com.rajendra_prasad.smart_location_fetcher;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.rajendra_prasad.smartlocationfetcherlibary.SmartLocationFetcher;

public class MainActivity extends AppCompatActivity implements SmartLocationFetcher.OnLocationGetListener {
    private TextView mTxv_LocationData;
    private String Lattitude, Longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTxv_LocationData = findViewById(R.id.txv_location_data);

        findViewById(R.id.location_updater).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SmartLocationFetcher smartLocationFetcher = new SmartLocationFetcher(MainActivity.this, MainActivity.this);
                smartLocationFetcher.fetchSmartLocation(MainActivity.this);
            }
        });

    }

    @Override
    public void onLocationReady(Location location) {
        Lattitude = String.valueOf(location.getLatitude());
        Longitude = String.valueOf(location.getLongitude());

        mTxv_LocationData.setText("Lattitude :" + Lattitude + "\n" + "Longitude : " + Longitude);
    }

    @Override
    public void onError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }
}


