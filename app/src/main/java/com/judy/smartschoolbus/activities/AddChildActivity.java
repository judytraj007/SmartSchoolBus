package com.judy.smartschoolbus.activities;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
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

import java.util.HashMap;
import java.util.Map;

import app.Config;

import static app.Config.ADD_CHILD_URL;
import static app.Config.SETPARENT_URL;
import static app.Config.STUDENT_BUS;
import static app.Config.STUDENT_CLASS;
import static app.Config.STUDENT_ID;
import static app.Config.STUDENT_NAME;
import static app.Config.STUDENT_PARENT;
import static app.Config.STUDENT_STOP;

public class AddChildActivity extends AppCompatActivity {

    EditText shId,name,cls,stop,bno;
    Button upBtn;
    ImageButton searchBtn;
    ProgressBar pbD;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0358B2")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        shId=(EditText)findViewById(R.id.edSearch);
        name=(EditText)findViewById(R.id.chName);
        cls=(EditText)findViewById(R.id.chClass);
        stop=(EditText)findViewById(R.id.chStop);
        bno=(EditText)findViewById(R.id.chBno);
        pbD=(ProgressBar)findViewById(R.id.progressBarChild);
        upBtn=(Button)findViewById(R.id.chUpdateBtn);
        searchBtn=(ImageButton)findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbD.setVisibility(View.VISIBLE);
                String url=ADD_CHILD_URL+shId.getText().toString();
                StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
                            JSONObject geoData = result.getJSONObject(0);
                            name.setText(geoData.getString(STUDENT_NAME));
                            cls.setText(geoData.getString(STUDENT_STOP));
                            bno.setText(geoData.getString(STUDENT_BUS));
                            stop.setText(geoData.getString(STUDENT_CLASS));
                            pbD.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddChildActivity.this,error.getMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                });

                RequestQueue requestQueue = Volley.newRequestQueue(AddChildActivity.this);
                requestQueue.add(stringRequest);
            }
        });
        upBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, SETPARENT_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(response.trim().equalsIgnoreCase("success")) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(AddChildActivity.this, R.style.MyAlertDialogStyle);
                                    builder.setMessage("The map will now show your child's location.")
                                            .setTitle("Done!")
                                            .setPositiveButton(android.R.string.ok, null);
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }else{
                                    AlertDialog.Builder builder = new AlertDialog.Builder(AddChildActivity.this, R.style.MyAlertDialogStyle);
                                    builder.setMessage(response)
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(AddChildActivity.this, R.style.MyAlertDialogStyle);
                                builder.setMessage(error.getMessage())
                                        .setTitle("Error!")
                                        .setPositiveButton(android.R.string.ok, null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        sp = getSharedPreferences("myPref", MODE_PRIVATE);
                        String username = sp.getString("username", " ");
                        params.put(STUDENT_ID, shId.getText().toString().trim());
                        params.put(STUDENT_PARENT, username);
                        return params;
                    }

                };

                RequestQueue requestQueue = Volley.newRequestQueue(AddChildActivity.this);
                requestQueue.add(stringRequest);
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
