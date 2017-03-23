package com.judy.smartschoolbus.activities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapters.CustomRouteAdapter;
import Model.Stop;
import app.Config;

import static app.Config.ADDSTOP_URL;
import static app.Config.BUS_AM;
import static app.Config.BUS_ID;
import static app.Config.BUS_NAME;
import static app.Config.BUS_PM;
import static app.Config.BUS_URL;
import static app.Config.DELETE_STOP_URL;
import static app.Config.UPDBUS_URL;

public class BusDetails extends AppCompatActivity {

    Button viewRoute;
    Button addNewStop;
    Spinner spinner;
    String busno;
    ProgressBar busPbar;
    ArrayList<Stop> stops;
    ListView listView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_details);
        addNewStop=(Button) findViewById(R.id.addStop);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0358B2")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        spinner = (Spinner) findViewById(R.id.busId);
        busPbar = (ProgressBar) findViewById(R.id.progressBarBus);
        listView = (ListView) findViewById(R.id.listRoute);
        List<String> list = new ArrayList<>();
        list.add("101");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                busno = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        viewRoute = (Button) findViewById(R.id.addRoute);
        viewRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                busPbar.setVisibility(View.VISIBLE);
                stops = new ArrayList<>();
                String url = BUS_URL + busno;
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
                                    Toast.makeText(BusDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                busPbar.setVisibility(View.GONE);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                            }
                        });
                RequestQueue requestQueue = Volley.newRequestQueue(BusDetails.this);
                requestQueue.add(stringRequest);
                listView.setAdapter(new CustomRouteAdapter(BusDetails.this, stops));
                addNewStop.setVisibility(View.VISIBLE);
            }
        });
        addNewStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(BusDetails.this);
                LayoutInflater inflater = getLayoutInflater();
                TextView myView = new TextView(BusDetails.this);
                myView.setText("Add");
                myView.setWidth(100);
                myView.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.edit, 0, 0, 0);
                myView.setTextSize(45);
                myView.setTextColor(Color.parseColor("#FFFFFF"));
                myView.setBackgroundColor(Color.parseColor("#021145"));
                builder.setCustomTitle(myView);
                View dialogView = inflater.inflate(R.layout.add_new_stop, null);
                builder.setView(dialogView);
                final EditText bId = (EditText) dialogView.findViewById(R.id.adSno);
                final EditText bno = (EditText) dialogView.findViewById(R.id.adBid);
                final EditText mName = (EditText) dialogView.findViewById(R.id.adSid);
                final EditText mAm = (EditText) dialogView.findViewById(R.id.adAm);
                final EditText mPm = (EditText) dialogView.findViewById(R.id.adPm);
                bno.setText(busno);
                Button addButton = (Button) dialogView.findViewById(R.id.adAdd);
                builder.setIcon(R.drawable.edit);
                final AlertDialog dialog = builder.create();
                dialog.show();
                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Stop s=new Stop();
                        s.setsNo(bId.getText().toString());
                        s.setStopName(mName.getText().toString());
                        s.setAmTime(mAm.getText().toString());
                        s.setPmTime(mPm.getText().toString());
                        if (TextUtils.isEmpty(s.getsNo()))
                            bId.setError("Required field!");
                        else if (TextUtils.isEmpty(s.getStopName()))
                            mName.setError("Required field!");
                        else if (TextUtils.isEmpty(s.getAmTime()))
                            mAm.setError("Required field!");
                        else if (TextUtils.isEmpty(s.getPmTime()))
                            mPm.setError("Required field!");
                        else {
                            add(s);
                            bId.setText(" ");
                            mName.setText(" ");
                            mAm.setText(" ");
                            mPm.setText(" ");
                        }
                    }
                });
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Stop s = (Stop) listView.getItemAtPosition(position);
                final AlertDialog.Builder builder = new AlertDialog.Builder(BusDetails.this);
                LayoutInflater inflater = getLayoutInflater();
                TextView myView = new TextView(BusDetails.this);
                myView.setText("Edit");
                myView.setWidth(100);
                myView.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.edit, 0, 0, 0);
                myView.setTextSize(45);
                myView.setTextColor(Color.parseColor("#FFFFFF"));
                myView.setBackgroundColor(Color.parseColor("#021145"));
                builder.setCustomTitle(myView);
                View dialogView = inflater.inflate(R.layout.edit_bus_route, null);
                builder.setView(dialogView);
                final EditText bId = (EditText) dialogView.findViewById(R.id.edSno);
                final EditText bno = (EditText) dialogView.findViewById(R.id.edBid);
                final EditText mName = (EditText) dialogView.findViewById(R.id.edSid);
                final EditText mAm = (EditText) dialogView.findViewById(R.id.edAm);
                final EditText mPm = (EditText) dialogView.findViewById(R.id.edPm);
                bId.setText(s.getsNo());
                bno.setText(busno);
                mName.setText(s.getStopName());
                mAm.setText(s.getAmTime());
                mPm.setText(s.getPmTime());
                Button sendButton = (Button) dialogView.findViewById(R.id.adUpd);
                Button deleteButton=(Button)dialogView.findViewById(R.id.adDel);
                builder.setIcon(R.drawable.edit);
                final AlertDialog dialog = builder.create();
                dialog.show();
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, DELETE_STOP_URL,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        if (response.trim().equalsIgnoreCase("success")) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(BusDetails.this, R.style.MyAlertDialogStyle);
                                            builder.setMessage("Stop Deleted!.")
                                                    .setTitle("Done!")
                                                    .setPositiveButton(android.R.string.ok, null);
                                            AlertDialog dialog = builder.create();
                                            dialog.show();
                                            bId.setText(" ");
                                            mName.setText(" ");
                                            mAm.setText(" ");
                                            mPm.setText(" ");
                                        } else {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(BusDetails.this, R.style.MyAlertDialogStyle);
                                            builder.setMessage(response) //temp
                                                    .setTitle("Error!")
                                                    .setPositiveButton(android.R.string.ok, null);
                                            AlertDialog dialog = builder.create();
                                            dialog.show();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(BusDetails.this, R.style.MyAlertDialogStyle);
                                        builder.setMessage(error.getMessage()) //temp
                                                .setTitle("Error!")
                                                .setPositiveButton(android.R.string.ok, null);
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                    }
                                }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put(BUS_ID, s.getsNo());
                                return params;
                            }

                        };

                        RequestQueue requestQueue = Volley.newRequestQueue(BusDetails.this);
                        requestQueue.add(stringRequest);
                    }
                });
                sendButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        s.setsNo(bId.getText().toString());
                        s.setPmTime(mPm.getText().toString());
                        s.setAmTime(mAm.getText().toString());
                        s.setStopName(mName.getText().toString());
                        dialog.cancel();
                        update(s);
                        bId.setText(" ");
                        mName.setText(" ");
                        mAm.setText(" ");
                        mPm.setText(" ");

                    }
                });
            }
        });
    }
    public void add(Stop s){
        final String sName = s.getStopName();
        final String sAm=s.getAmTime();
        final String sId = s.getsNo();
        final String sPm=s.getPmTime();


            StringRequest stringRequest = new StringRequest(Request.Method.POST, ADDSTOP_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.trim().equalsIgnoreCase("success")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(BusDetails.this, R.style.MyAlertDialogStyle);
                                builder.setMessage("New entry added successfully.")
                                        .setTitle("Done!")
                                        .setPositiveButton(android.R.string.ok, null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(BusDetails.this, R.style.MyAlertDialogStyle);
                                builder.setMessage(response) //temp
                                        .setTitle("Error!")
                                        .setPositiveButton(android.R.string.ok, null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(BusDetails.this, R.style.MyAlertDialogStyle);
                            builder.setMessage(error.getMessage()) //temp
                                    .setTitle("Error!")
                                    .setPositiveButton(android.R.string.ok, null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("busno", busno);
                    params.put(BUS_ID, sId);
                    params.put(BUS_NAME, sName);
                    params.put(BUS_AM, sAm);
                    params.put(BUS_PM, sPm);
                    return params;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(BusDetails.this);
            requestQueue.add(stringRequest);
    }
    public void update(final Stop stop){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDBUS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equalsIgnoreCase("success")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(BusDetails.this, R.style.MyAlertDialogStyle);
                            builder.setMessage("Updated successfully.")
                                    .setTitle("Done!")
                                    .setPositiveButton(android.R.string.ok, null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(BusDetails.this, R.style.MyAlertDialogStyle);
                            builder.setMessage(response) //temp
                                    .setTitle("Error!")
                                    .setPositiveButton(android.R.string.ok, null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(BusDetails.this, R.style.MyAlertDialogStyle);
                        builder.setMessage(error.getMessage()) //temp
                                .setTitle("Error!")
                                .setPositiveButton(android.R.string.ok, null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id",busno);
                params.put(BUS_ID, stop.getsNo());
                params.put(BUS_NAME, stop.getStopName());
                params.put(BUS_AM, stop.getAmTime());
                params.put(BUS_PM, stop.getPmTime());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(BusDetails.this);
        requestQueue.add(stringRequest);
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
