package com.judy.smartschoolbus.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.judy.smartschoolbus.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapters.CustomListAdapter;
import Model.Student;
import app.Config;

import static app.Config.DELETE_STUDENT_URL;
import static app.Config.DELETE_URL;
import static app.Config.JSON_ARRAY;
import static app.Config.KEY_USERNAME;
import static app.Config.STUDENT_BUS;
import static app.Config.STUDENT_CLASS;
import static app.Config.STUDENT_ID;
import static app.Config.STUDENT_NAME;
import static app.Config.STUDENT_PARENT;
import static app.Config.STUDENT_STATUS;
import static app.Config.STUDENT_STOP;
import static app.Config.STUDENT_URL;
import static app.Config.UPDSTUDENT_URL;


public class  StudentDetails extends AppCompatActivity {

    ArrayList<Student> students;
    ListView listView;
    List<String> buses=new ArrayList<String>();
    ProgressBar pb;
    String val;
    Spinner busList;
    Button getData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0358B2")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pb=(ProgressBar)findViewById(R.id.progressBarUpd);
        listView=(ListView)findViewById(R.id.listView);
        getData=(Button)findViewById(R.id.getStData);
        busList=(Spinner)findViewById(R.id.busNo);
        buses.add("101");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,buses);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        busList.setAdapter(dataAdapter);
        busList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                val=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb.setVisibility(View.VISIBLE);
                students=new ArrayList<>();
                String url=STUDENT_URL+val;
                StringRequest stringRequest = new StringRequest(url ,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
                                    for (int i = 0; i < result.length(); i++) {
                                        JSONObject jo = result.getJSONObject(i);
                                        Student s=new Student();
                                        s.setId(jo.getString(STUDENT_ID));
                                        s.setName(jo.getString(STUDENT_NAME));
                                        s.setClassNo(jo.getString(STUDENT_CLASS));
                                        s.setBusno(jo.getString(STUDENT_BUS));
                                        s.setParentId(jo.getString(STUDENT_PARENT));
                                        s.setStatus(Integer.parseInt(jo.getString(STUDENT_STATUS)));
                                        s.setStopid(jo.getString(STUDENT_STOP));
                                        students.add(s);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(StudentDetails.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                                pb.setVisibility(View.GONE);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                                Toast.makeText(StudentDetails.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                RequestQueue requestQueue = Volley.newRequestQueue(StudentDetails.this);
                requestQueue.add(stringRequest);
                listView.setAdapter(new CustomListAdapter(StudentDetails.this,students));
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Student s=(Student)listView.getItemAtPosition(position);
               final AlertDialog.Builder builder = new AlertDialog.Builder(StudentDetails.this);
                LayoutInflater inflater = getLayoutInflater();
                TextView myView=new TextView(StudentDetails.this);
                myView.setText("Edit");
                myView.setWidth(100);
                myView.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.edit, 0, 0, 0);
                myView.setTextSize(45);
                myView.setTextColor(Color.parseColor("#FFFFFF"));
                myView.setBackgroundColor(Color.parseColor("#021145"));
                builder.setCustomTitle(myView);
                View dialogView= inflater.inflate(R.layout.dialogfragment, null);
                builder.setView(dialogView);
                final EditText mId =(EditText)dialogView.findViewById(R.id.edId);
                final EditText mName =(EditText)dialogView.findViewById(R.id.edName);
                final EditText mClass =(EditText)dialogView.findViewById(R.id.edCls);
                final EditText mBus =(EditText)dialogView.findViewById(R.id.edBus);
                final EditText mStop =(EditText)dialogView.findViewById(R.id.edStop);
                final EditText mParent =(EditText)dialogView.findViewById(R.id.edParent);
                mId.setText(s.getId());
                mName.setText(s.getName());
                mClass.setText(s.getClassNo());
                mBus.setText(s.getBusno());
                mStop.setText(s.getStopid());
                mParent.setText(s.getParentId());
                Button sendButton=(Button)dialogView.findViewById(R.id.adSend);
                Button callButton=(Button)dialogView.findViewById(R.id.adCall);
                Button delButton=(Button)dialogView.findViewById(R.id.adDelete);
                builder.setIcon(R.drawable.edit);
                final AlertDialog dialog=builder.create();
                dialog.show();
                callButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = "tel:"+s.getParentId();
                        Intent in = new Intent(Intent.ACTION_CALL, Uri.parse(url));
                        if (ActivityCompat.checkSelfPermission(StudentDetails.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        }
                        startActivity(in);
                    }
                });
                delButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, DELETE_STUDENT_URL,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        if (response.trim().equalsIgnoreCase("success")) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(StudentDetails.this, R.style.MyAlertDialogStyle);
                                            builder.setMessage("Student Deleted!.")
                                                    .setTitle("Done!")
                                                    .setPositiveButton(android.R.string.ok, null);
                                            AlertDialog dialog = builder.create();
                                            dialog.show();
                                            mId.setText(" ");
                                            mName.setText(" ");
                                            mClass.setText(" ");
                                            mParent.setText(" ");
                                            mBus.setText(" ");
                                            mStop.setText(" ");
                                        } else {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(StudentDetails.this, R.style.MyAlertDialogStyle);
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
                                        AlertDialog.Builder builder = new AlertDialog.Builder(StudentDetails.this, R.style.MyAlertDialogStyle);
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
                                params.put(STUDENT_ID, s.getId());
                                return params;
                            }

                        };

                        RequestQueue requestQueue = Volley.newRequestQueue(StudentDetails.this);
                        requestQueue.add(stringRequest);
                    }
                });
                sendButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        s.setId(mId.getText().toString());
                        s.setName(mName.getText().toString());
                        s.setClassNo(mClass.getText().toString());
                        s.setBusno(mBus.getText().toString());
                        s.setStopid(mStop.getText().toString());
                        dialog.cancel();
                       update(s);
                        mId.setText(" ");
                        mName.setText(" ");
                        mClass.setText(" ");
                        mParent.setText(" ");
                        mBus.setText(" ");
                        mStop.setText(" ");

                    }
                });
            }
        });

    }
    public void update(final Student student){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDSTUDENT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equalsIgnoreCase("success")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(StudentDetails.this, R.style.MyAlertDialogStyle);
                            builder.setMessage("Updated successfully.")
                                    .setTitle("Done!")
                                    .setPositiveButton(android.R.string.ok, null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(StudentDetails.this, R.style.MyAlertDialogStyle);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(StudentDetails.this, R.style.MyAlertDialogStyle);
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
                params.put(STUDENT_ID, student.getId());
                params.put(STUDENT_NAME, student.getName());
                params.put(STUDENT_CLASS, student.getClassNo());
                params.put(STUDENT_BUS, student.getBusno());
                params.put(STUDENT_STOP, student.getStopid());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(StudentDetails.this);
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
