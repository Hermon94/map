package com.example.hermon.geolocalizacion;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.security.Provider;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {
    public static final String PREFS_NAME = "MyPreferencesFile";
    LocationManager locationManager;
    String provider;
    private static final String[] INITIAL_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private boolean canAccessLocation() {
        return (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    private boolean hasPermission(String perm) {
        return (PackageManager.PERMISSION_GRANTED == checkSelfPermission(perm));
    }

    public void onLocationChanged(Location location) {
        TextView latencia = (TextView) findViewById(R.id.latText);
        Double lat = location.getLatitude();
        String latText = String.valueOf(lat);
        latencia.setText(latText);
        TextView lon = (TextView) findViewById(R.id.lonText);
        Double lo = location.getLongitude();
        String lotext = String.valueOf(lo);
        lon.setText(lotext);
        Address add = null;
        String calle = null;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lo, 1);
            add = addresses.get(0);
            calle = add.getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        TextView addressT = (TextView) findViewById(R.id.addText);
        addressT.setText(calle);
        TextView acura = (TextView) findViewById(R.id.acuText);
        float ac = location.getAccuracy();
        String acText = String.valueOf(ac);
        acura.setText(acText);
        TextView sped = (TextView) findViewById(R.id.speText);
        float sp = location.getSpeed();
        String spText = String.valueOf(sp);
        sped.setText(spText);
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    public void onProviderEnabled(String provider) {

    }

    public void onProviderDisabled(String provider) {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!canAccessLocation()) {
            requestPermissions(INITIAL_PERMS, 0);
        }
        provider = locationManager.getBestProvider(new Criteria(), true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if (location == null) {
            return;
        }
        TextView latencia = (TextView) findViewById(R.id.latText);
        Double lat = location.getLatitude();
        String latText = String.valueOf(lat);
        latencia.setText(latText);
        TextView lon = (TextView) findViewById(R.id.lonText);
        Double lo = location.getLongitude();
        String lotext = String.valueOf(lo);
        lon.setText(lotext);
        Address add = null;
        String calle = null;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lo, 1);
            add = addresses.get(0);
            calle = add.getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        TextView addressT = (TextView) findViewById(R.id.addText);
        addressT.setText(calle);
        TextView acura = (TextView) findViewById(R.id.acuText);
        float ac = location.getAccuracy();
        String acText = String.valueOf(ac);
        acura.setText(acText);
        TextView sped = (TextView) findViewById(R.id.speText);
        float sp = location.getSpeed();
        String spText = String.valueOf(sp);
        sped.setText(spText);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.removeUpdates(this);
    }

    public void showMap(View view) {
        Intent i = new Intent(this, MapsActivity.class);
        startActivity(i);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if (location == null){
            return;
        }
        final  Double lat = location.getLatitude();
        final Double lo = location.getLongitude();
        final int latInt = lat.intValue();
        final int loInt = lo.intValue();
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("latencia", latInt);
        editor.putInt("longitud", loInt);
        editor.commit();
    }
}
