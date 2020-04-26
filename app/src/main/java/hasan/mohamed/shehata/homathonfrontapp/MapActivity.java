package hasan.mohamed.shehata.homathonfrontapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import hasan.mohamed.shehata.homathonfrontapp.databinding.ActivityMapBinding;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener  {


    public static final int PICK_LOCATION_REQUEST = 65; // The request code.
    public static final String KEY_RESULT_MESSAGE = "KEY_RESULT_MESSAGE";
    public static final String RESULT_MESSAGE = "Cannot play this file";



    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 545;
    private boolean mLocationPermissionGranted = false;
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;
    private static final String TAG = "MAP LOCATION";
    private float DEFAULT_ZOOM = 17f;
    private LatLng mDefaultLocation = new LatLng(21.422510,39.826168); //mecca latlng;
    private LatLng clinicLocation = null;

    public static final String INTENT_LOC_LAT_KEY = "CLINIC_LOC_LAT_KEY";
    public static final String INTENT_LOC_LNG_KEY = "CLINIC_LOC_LNG_KEY";

    private double currentLat = mDefaultLocation.latitude;
    private double currentLng = mDefaultLocation.longitude;

    @Override
    protected void onPostResume() {
        super.onPostResume();
        forceRTLIfSupported();
//        getActionBar().getCustomView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void forceRTLIfSupported()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }






    private GoogleMap mMap;
    private ActivityMapBinding mDataBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_map);


        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(INTENT_LOC_LAT_KEY) && intent.hasExtra(INTENT_LOC_LNG_KEY)){
            double lat = intent.getDoubleExtra(INTENT_LOC_LAT_KEY,mDefaultLocation.latitude);
            double lng = intent.getDoubleExtra(INTENT_LOC_LNG_KEY,mDefaultLocation.longitude);
            if(lat!=0 && lng!=0){
                clinicLocation = new LatLng(lat,lng);
            }
        }

        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_frag);
        mapFragment.getMapAsync(this);

        mDataBinding.clinicTemplateMapSetLocatiobBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocationPermission();
            }
        });

    }




//    private void updateLocationUI() {
//        if (mMap == null) {
//            return;
//        }
//        try {
//            if (mLocationPermissionGranted) {
//                mMap.setMyLocationEnabled(true);
//                mMap.getUiSettings().setMyLocationButtonEnabled(true);
//            } else {
//                mMap.setMyLocationEnabled(false);
//                mMap.getUiSettings().setMyLocationButtonEnabled(false);
//                mLastKnownLocation = null;
//                getLocationPermission();
//            }
//        } catch (SecurityException e)  {
//            Log.e("Exception: %s", e.getMessage());
//        }
//    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.map_activity_true_menu_item_id:{
                Intent intent1=new Intent();
                intent1.putExtra(INTENT_LOC_LAT_KEY,currentLat);
                intent1.putExtra(INTENT_LOC_LNG_KEY,currentLng);
                setResult(RESULT_OK,intent1);
                finish();//finishing activity
            }
            break;
            case R.id.map_activity_false_menu_item_id:{
                finish();
            }
            break;
//            finish();
        }
        return true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                currentLat = googleMap.getCameraPosition().target.latitude;
                currentLng = googleMap.getCameraPosition().target.longitude;
                Log.i("centerLat",String.valueOf(currentLat));
                Log.i("centerLong",String.valueOf(currentLng));
            }
        });
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        updateLocationUI();
        getDeviceLocation();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            turnGPSOn();
            setMyLocationOnMap();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    turnGPSOn();
                    setMyLocationOnMap();
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
//                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                if(clinicLocation != null){
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(clinicLocation, DEFAULT_ZOOM));
                }
                else {
                    Task locationResult = mFusedLocationProviderClient.getLastLocation();
                    locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                // Set the map's camera position to the current location of the device.
                                mLastKnownLocation = (Location) task.getResult();
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(mLastKnownLocation.getLatitude(),
                                                mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            } else {
                                Log.d(TAG, "Current location is null. Using defaults.");
                                Log.e(TAG, "Exception: %s", task.getException());
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
//                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                            }
                        }
                    });
                }
            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void setMyLocationOnMap() {
        updateLocationUI();
        getDeviceLocation();
    }

    private void setLocation (LatLng location){
    }

    Context context;
    Intent intent1;
    LocationManager locationManager ;
    boolean GpsStatus ;
    private void turnGPSOn(){
        locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!GpsStatus) {
//            intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//            startActivity(intent1);
            Toast.makeText(this,"يرجي تشغيل GPS",Toast.LENGTH_LONG).show();
        }
    }

}
