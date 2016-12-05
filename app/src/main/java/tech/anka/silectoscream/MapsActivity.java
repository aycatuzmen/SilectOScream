package tech.anka.silectoscream;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private LocationManager lm;
    private LocationListener listener;
    Location currentLocation;
    Marker mMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);




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
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean isGPS = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetwork = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        //Toast.makeText(MapsActivity.this,                "GPS:" + isGPS + ", Network:" + isNetwork,Toast.LENGTH_SHORT).show();
        //getLastPosition();

        currentLocation =  (Location) getIntent().getExtras().getParcelable("Location");
//        double cloclong = getIntent().getDoubleExtra("long", 0);
//        double cloclat = getIntent().getDoubleExtra("lat", 0);
//        Log.d("Konum ", "Lat:" + cloclat +
//                " Lon:" + cloclong);
        if (currentLocation != null) {
            Toast.makeText(this, "Konum:" + currentLocation.getLatitude() + "-" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
            Log.d("Konum ", "Lat:" + currentLocation.getLatitude() +
                    " Lon:" + currentLocation.getLongitude());
            if (mMap != null) {
                LatLng position = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                 mMarker = mMap.addMarker(new MarkerOptions().position(position).title("CurrentLocation"));
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 16));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 16));
                //mMap.clear();
                mMarker.showInfoWindow();
            } else Toast.makeText(MapsActivity.this, "Harita henüz yüklenmedi", Toast.LENGTH_SHORT).show();
        } else {
            getLastPosition();
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currentLocation = location;
                Toast.makeText(MapsActivity.this, "Lat:" + location.getLatitude() +
                        " Lon:" + location.getLongitude(), Toast.LENGTH_SHORT).show();
                Log.d("Konum ", "Lat:" + location.getLatitude() +
                        " Lon:" + location.getLongitude());
                //lm.removeUpdates(listener);
                if (mMap != null) {
                    LatLng position = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                    //mMarker = mMap.addMarker(new MarkerOptions().position(position).title("CurrentLocation"));
                    mMarker.setPosition(position);
                    //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 16));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 16));
                    //mMap.clear();
                    mMarker.showInfoWindow();
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {
            }
        };
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3 * 1000, 0, listener);

        /*
        new Handler()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //gerceklestir
                        lm.removeUpdates(listener);
                    }
                }, 5*)1000;


        */

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng arg0) {
                // TODO Auto-generated method stub
                Log.d("arg0", arg0.latitude + "-" + arg0.longitude);
            }
        });

    }

    private void getLastPosition() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            //ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION );
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Toast.makeText(this, "Son konum isteniyor", Toast.LENGTH_SHORT).show();
        Location locGPS = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (locGPS != null) {
            Toast.makeText(this, "Konum:" + locGPS.getLatitude() + "-" + locGPS.getLongitude(), Toast.LENGTH_SHORT).show();
        }
    }

    public void addBLLMarker(View view) {
        if (mMap != null) {
            LatLng position = new LatLng(41.095325, 28.803271);
            Marker mMarker = mMap.addMarker(new MarkerOptions().position(position).title("BLL").snippet("Başakşehir Living Lab"));
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 16));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 16));
            //mMap.clear();
            mMarker.showInfoWindow();
        } else Toast.makeText(MapsActivity.this, "Harita henüz yüklenmedi", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Fine location yetki istegi
        if (requestCode == 1) {
            //permissions[0]
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastPosition();
            } else Toast.makeText(MapsActivity.this, "Gerekli izin verilmedi", Toast.LENGTH_SHORT).show();
            //ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION );
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("OnPuse", "Gerceklesti");
        if (listener != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            lm.removeUpdates(listener);
            Log.d("listener", "birakildi");
        }
    }

    public void showMyLocation(View view) {
        if (mMap != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);


        }

    }

    public void addLine(View view) {
        if (mMap != null) {
            mMap.addPolyline(new PolylineOptions()
                    .geodesic(true)
                    .width(8)
                    .color(Color.RED)
                    .add(new LatLng(-33.866, 151.195))
                    .add(new LatLng(-18.142, 178.431))
                    .add(new LatLng(21.291, -157.821))
                    .add(new LatLng(37.423, -122.091)));
        }
    }

    public void changeMapType(View view) {
        if (mMap != null) {
            mMap.setMapType(
                    mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL
                            ? GoogleMap.MAP_TYPE_HYBRID
                            : GoogleMap.MAP_TYPE_NORMAL );
        }
    }
}
