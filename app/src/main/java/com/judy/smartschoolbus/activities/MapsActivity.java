package com.judy.smartschoolbus.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import java.util.ArrayList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.judy.smartschoolbus.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import app.Config;
import utils.NotificationUtils;

import static app.Config.DEST_LOCATION;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<LatLng> pontos;
    SharedPreferences sp;
    String latitude=" ";
    String longitude=" ";
    String spd=" ";
    String origin=" ";
    public double lat,lng;
    public float speed;
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private TextView txtMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0358B2")));
        sp=getSharedPreferences("myPref",MODE_PRIVATE);
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
        getData();
        txtMessage = (TextView) findViewById(R.id.txt_push_message);
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
             if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    txtMessage.setText(message);
                }
            }
        };


    }

    public void getData(){

        StringRequest stringRequest = new StringRequest(Config.DATA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showJSON(response);
            }
        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MapsActivity.this,error.getMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    public void showJSON(String response){

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
            JSONObject geoData = result.getJSONObject(0);
            latitude = geoData.getString(Config.KEY_LAT);
            longitude = geoData.getString(Config.KEY_LNG);
            spd = geoData.getString(Config.KEY_SPEED);
            origin = latitude+","+longitude;
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            new GetDirection().execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            lat = Double.parseDouble(latitude);
            lng = Double.parseDouble(longitude);
            speed = Float.parseFloat(spd);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        String usertype=sp.getString("usertype"," ");
        MenuInflater inflater = getMenuInflater();
        if(usertype.equalsIgnoreCase("parent"))
            inflater.inflate(R.menu.menu_parent, menu);
        else if(usertype.equalsIgnoreCase("admin"))
            inflater.inflate(R.menu.menu_admin, menu);
        else
            inflater.inflate(R.menu.menu_driver, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.call:
                String url = "tel:7025422267";
                Intent in = new Intent(Intent.ACTION_CALL, Uri.parse(url));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return false;
                }
                startActivity(in);
                break;
            case R.id.addChld:
                Intent intu=new Intent(MapsActivity.this,AddChildActivity.class);
                startActivity(intu);
                break;
            case R.id.editDetls:
                Intent inte=new Intent(MapsActivity.this,viewDetails.class);
                startActivity(inte);
                break;
            case R.id.rPword:
                Intent intent=new Intent(MapsActivity.this,SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.ctDriver:
                Intent i=new Intent(MapsActivity.this,DriverDetails.class);
                startActivity(i);
                break;
            case R.id.ctAdmin:
                String url2 = "tel:7025422267";
                Intent in2 = new Intent(Intent.ACTION_CALL, Uri.parse(url2));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return false;
                }
                startActivity(in2);

                break;
            case R.id.ctAmb:
                String url3 = "tel:108";
                Intent in3 = new Intent(Intent.ACTION_CALL, Uri.parse(url3));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return false;
                }
                startActivity(in3);

                break;
            case R.id.ctPolics:
                String url4 = "tel:100";
                Intent in4 = new Intent(Intent.ACTION_CALL, Uri.parse(url4));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return false;
                }
                startActivity(in4);
                break;
            case R.id.adSettings:
                Intent n=new Intent(MapsActivity.this,SettingsActivity.class);
                startActivity(n);
                break;
            case R.id.drSettings:
                Intent n1=new Intent(MapsActivity.this,SettingsActivity.class);
                startActivity(n1);
                break;
            case R.id.adNewDr:
                Intent t=new Intent(MapsActivity.this,AddStudentActivity.class);
                startActivity(t);
                break;
            case R.id.adBus:
                Intent intent1=new Intent(MapsActivity.this,BusDetails.class);
                startActivity(intent1);
                break;
            case R.id.adNewStudent:
                Intent intent2=new Intent(MapsActivity.this,StudentDetails.class);
                startActivity(intent2);
                break;
        }
        return false;
    }
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Speed " +speed));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        LatLng dest = new LatLng(Double.parseDouble("9.673745"),Double.parseDouble("76.826303"));
        mMap.addMarker(new MarkerOptions().position(dest).title("School."));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dest));

    }
    class GetDirection extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... args) {
            String stringUrl = "http://maps.googleapis.com/maps/api/directions/json?origin=" + origin+ "&destination=" +DEST_LOCATION+ "&sensor=false";
            StringBuilder response = new StringBuilder();
            try {
                URL url = new URL(stringUrl);
                HttpURLConnection httpconn = (HttpURLConnection) url
                        .openConnection();
                if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader input = new BufferedReader(
                            new InputStreamReader(httpconn.getInputStream()),
                            8192);
                    String strLine = null;

                    while ((strLine = input.readLine()) != null) {
                        response.append(strLine);
                    }
                    input.close();
                }

                String jsonOutput = response.toString();

                JSONObject jsonObject = new JSONObject(jsonOutput);

                // routesArray contains ALL routes
                JSONArray routesArray = jsonObject.getJSONArray("routes");
                // Grab the first route
                JSONObject route = routesArray.getJSONObject(0);

                JSONObject poly = route.getJSONObject("overview_polyline");
                String polyline = poly.getString("points");
                pontos = decodePoly(polyline);

            } catch (Exception e) {

            }

            return null;

        }

        protected void onPostExecute(String file_url) {
            for (int i = 0; i < pontos.size() - 1; i++) {
                LatLng src = pontos.get(i);
                LatLng dest = pontos.get(i + 1);
                try{
                    //here is where it will draw the polyline in your map
                    Polyline line = mMap.addPolyline(new PolylineOptions()
                            .add(new LatLng(src.latitude, src.longitude),
                                    new LatLng(dest.latitude,                dest.longitude))
                            .width(2).color(Color.RED).geodesic(true));
                }catch(NullPointerException e){
                    Log.e("Error", "NullPointerException onPostExecute: " + e.toString());
                }catch (Exception e2) {
                    Log.e("Error", "Exception onPostExecute: " + e2.toString());
                }

            }
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);

        }
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

}
