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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
        //send location to the server
        sendLocationtoServer(currentLocation);

        //new google screen
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

    private void sendLocationtoServer(Location location) {
        String serverUrl = String.valueOf((R.string.webserverurl));
        serverUrl = "http://www.basicnotify.com/api/add-location";

        RequestQueue queue = Volley.newRequestQueue(this);


        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("lat", String.valueOf(location.getLatitude()));
        jsonParams.put("lng", String.valueOf(location.getLongitude()));

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                serverUrl,
                new JSONObject(jsonParams),

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        String postresult;
                        try {

                            if (response.getBoolean("success")) {
                                Log.i("Post result", "Post succesfully made");
                            } else {
                                Log.e("Error", "Error in server post");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };


        queue.add(jsonObjectRequest);
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
