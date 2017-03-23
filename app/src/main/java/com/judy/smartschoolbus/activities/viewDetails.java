package com.judy.smartschoolbus.activities;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.judy.smartschoolbus.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Adapters.CustomRouteAdapter;
import Model.Stop;
import app.Config;

import static app.Config.BUS_AM;
import static app.Config.BUS_ID;
import static app.Config.BUS_NAME;
import static app.Config.BUS_PM;
import static app.Config.BUS_URL;
import static app.Config.DRIVER_CON;
import static app.Config.DRIVER_NAME;
import static app.Config.DRIVER_URL;

public class viewDetails extends AppCompatActivity {
    Button fetchBtn,callBtn, routeBtn;
    ListView listView;
    ArrayList stops;
    ProgressBar pB3;
    String dNum;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details);
        fetchBtn = (Button) findViewById(R.id.driverBtn);
        callBtn=(Button)findViewById(R.id.callBtn);
        routeBtn=(Button)findViewById(R.id.timeBtn);
        textView=(TextView)findViewById(R.id.driverName);
        listView=(ListView)findViewById(R.id.routeLst);
        pB3=(ProgressBar)findViewById(R.id.progressBar3);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0358B2")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        routeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pB3.setVisibility(View.VISIBLE);
                stops = new ArrayList<Stop>();
                String url = BUS_URL + "101";
                StringRequest stringRequest = new StringRequest(url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
                                    for (int i = 0; i < result.length(); i++) {
                                        JSONObject jo = result.getJSONObject(i);
                                        Stop s = new Stop();
                                        s.setsNo(jo.getString(BUS_ID));
                                        s.setAmTime(jo.getString(BUS_AM));
                                        s.setPmTime(jo.getString(BUS_PM));
                                        s.setStopName(jo.getString(BUS_NAME));
                                        stops.add(s);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(viewDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                pB3.setVisibility(View.GONE);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                            }
                        });
                RequestQueue requestQueue = Volley.newRequestQueue(viewDetails.this);
                requestQueue.add(stringRequest);
                listView.setAdapter(new CustomRouteAdapter(viewDetails.this, stops));
            }
        });
        fetchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pB3.setVisibility(View.VISIBLE);
                String url=DRIVER_URL+"101";
                StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
                            JSONObject geoData = result.getJSONObject(0);
                            dNum = geoData.getString(DRIVER_CON);
                            textView.setText("Driver's name : " + geoData.getString(DRIVER_NAME));
                            callBtn.setVisibility(View.VISIBLE);
                            pB3.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(viewDetails.this,error.getMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                });

                RequestQueue requestQueue = Volley.newRequestQueue(viewDetails.this);
                requestQueue.add(stringRequest);
            }
        });
        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url1 = "tel:"+dNum;
                Intent in1 = new Intent(Intent.ACTION_CALL, Uri.parse(url1));
                if (ActivityCompat.checkSelfPermission(viewDetails.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                }
                startActivity(in1);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
