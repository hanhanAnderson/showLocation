package com.hanhan.maptest;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private LocationManager locationManager;
    private String locationProvider;
    private Location location;
    Marker currMarker = null;
    LatLng currLatLng = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationProvider = LocationManager.GPS_PROVIDER;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;

        }
        ///////////////////////////LOCATION NULL????
        location = locationManager.getLastKnownLocation(locationProvider);
        Log.e("MAP", "location get!");
        if(location!=null){
            Log.e("MAP", "location is not null!");
            showLocation(location);
        }
        locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        Log.e("MAP", "MAP ready!");
        showLocation(location);
    }
    public void showLocation(Location location) {
        Log.e("MAP", "show location!");
        currLatLng = new LatLng(location.getLatitude(),location.getLongitude());
        if (mMap!= null) {
            if (currMarker != null) {
                currMarker.remove();
            }
//            MarkerOptions currMarker = new MarkerOptions().position(currLatLng).title("Curr");
            currMarker = mMap.addMarker(new MarkerOptions().position(currLatLng).title("Curr"));

            mMap.moveCamera(CameraUpdateFactory.newLatLng(currLatLng));
        }
        Log.e("MAP: LAT: ", Double.toString(location.getLatitude()));
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {
        }
        @Override
        public void onProviderEnabled(String provider) {
        }
        @Override
        public void onProviderDisabled(String provider) {
        }
        @Override
        public void onLocationChanged(Location location) {
            Log.e("MAP", "location changed!");

            currLatLng = new LatLng(location.getLatitude(),location.getLongitude());
            if (mMap!= null) {
                Log.e("MAP", "map not null!");
                if (currMarker != null) {
                    currMarker.remove();
                }
                currMarker = mMap.addMarker(new MarkerOptions().position(currLatLng).title("Curr"));
                // USE THIS LINE TO KEEP PREVIOUS MARKER
                //mMap.addMarker(new MarkerOptions().position(currLatLng).title("Curr"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(currLatLng));
            }
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(locationManager!=null){
            locationManager.removeUpdates(locationListener);
        }

    }
}
