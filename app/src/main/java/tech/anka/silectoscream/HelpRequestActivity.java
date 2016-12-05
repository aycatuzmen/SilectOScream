package tech.anka.silectoscream;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class HelpRequestActivity extends AppCompatActivity {

    private LocationManager lm;
    private LocationListener listener;
    Location currentLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_request);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void requestHelp(View view) {
        Log.i("SilectOScream", "Help Requested");
        //capture loc and lat
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean isGPS = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetwork = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        //Toast.makeText(MapsActivity.this,                "GPS:" + isGPS + ", Network:" + isNetwork,Toast.LENGTH_SHORT).show();
        getLastPosition();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //new google screan
        Intent intent = new Intent(HelpRequestActivity.this, MapsActivity.class);
        intent.putExtra("Location", currentLocation);
//                intent.putExtra("lat", currentLocation.getLatitude());
//                intent.putExtra("long", currentLocation.getLongitude());
        startActivity(intent);
        /*
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currentLocation = new Location (location);
                        Toast.makeText(HelpRequestActivity.this, "Lat:" + location.getLatitude() +
                        " Lon:" + location.getLongitude(), Toast.LENGTH_SHORT).show();
                Log.d("Konum ", "Lat:" + location.getLatitude() +
                        " Lon:" + location.getLongitude());

                //lm.removeUpdates(listener);
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

*/


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
        currentLocation = new Location (locGPS);
        if (currentLocation != null) {
            Toast.makeText(HelpRequestActivity.this, "Lat:" + currentLocation.getLatitude() +
                    " Lon:" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
            Log.d("Konum ", "Lat:" + currentLocation.getLatitude() +
                    " Lon:" + currentLocation.getLongitude());

        }
    }

}
