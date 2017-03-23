package com.judy.smartschoolbus.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

import app.Config;

import static app.Config.DRIVER_CON;
import static app.Config.DRIVER_NAME;
import static app.Config.DRIVER_URL;

public class DriverDetails extends AppCompatActivity {
    Button call,fetchBtn;
    ProgressBar pbD;
    String val,dNum;
    Spinner spinner;
    EditText textView;
    List<String> buses=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_details);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0358B2")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        spinner=(Spinner)findViewById(R.id.dbList);
        fetchBtn=(Button)findViewById(R.id.dfetch);
        pbD=(ProgressBar)findViewById(R.id.progressBarD);
        call=(Button)findViewById(R.id.dCall);
        textView=(EditText)findViewById(R.id.dName);
        buses.add("101");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,buses);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                val=parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        fetchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbD.setVisibility(View.VISIBLE);
                String url=DRIVER_URL+val;
                StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
                            JSONObject geoData = result.getJSONObject(0);
                            String dName = geoData.getString(DRIVER_NAME);
                            dNum = geoData.getString(DRIVER_CON);
                            textView.setText(dName);
                            call.setVisibility(View.VISIBLE);
                            pbD.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DriverDetails.this,error.getMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                });

                RequestQueue requestQueue = Volley.newRequestQueue(DriverDetails.this);
                requestQueue.add(stringRequest);
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url1 = "tel:"+dNum;
                Intent in1 = new Intent(Intent.ACTION_CALL, Uri.parse(url1));
                if (ActivityCompat.checkSelfPermission(DriverDetails.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                }
                startActivity(in1);
            }
        });
}
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}