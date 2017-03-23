package com.judy.smartschoolbus.activities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.judy.smartschoolbus.R;

import java.util.HashMap;
import java.util.Map;

import static app.Config.ADDSTUDENT_URL;
import static app.Config.STUDENT_BUS;
import static app.Config.STUDENT_CLASS;
import static app.Config.STUDENT_ID;
import static app.Config.STUDENT_NAME;
import static app.Config.STUDENT_STOP;

public class AddStudentActivity extends AppCompatActivity {
    Button addStudent;
    EditText id,stClass,bus,stop,name;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        addStudent=(Button)findViewById(R.id.stAddBtn);
        id=(EditText)findViewById(R.id.studentId);
        name=(EditText) findViewById(R.id.studentName);
        stClass=(EditText)findViewById(R.id.studentClass);
        bus=(EditText)findViewById(R.id.studentBus);
        stop=(EditText)findViewById(R.id.studentStop);
        pb=(ProgressBar)findViewById(R.id.progressBarS);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0358B2")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String sName = name.getText().toString().trim();
                final String sClass = stClass.getText().toString().trim();
                final String sId = id.getText().toString().trim();
                final String sBus = bus.getText().toString().trim();
                final String sStop = stop.getText().toString().trim();
                if(TextUtils.isEmpty(sName))
                    name.setError("Required field!");
                else if(TextUtils.isEmpty(sClass))
                    stClass.setError("Required field!");
                else if(TextUtils.isEmpty(sId))
                    id.setError("Required field!");
                else if(TextUtils.isEmpty(sBus))
                    bus.setError("Required field!");
                else if(TextUtils.isEmpty(sStop))
                    stop.setError("Required field!");
                else {
                    pb.setVisibility(View.VISIBLE);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, ADDSTUDENT_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    pb.setVisibility(View.GONE);
                                    if(response.trim().equalsIgnoreCase("success")) {
                                        name.setText(" ");
                                        stClass.setText(" ");
                                        id.setText(" ");
                                        bus.setText(" ");
                                        stop.setText(" ");
                                        AlertDialog.Builder builder = new AlertDialog.Builder(AddStudentActivity.this, R.style.MyAlertDialogStyle);
                                        builder.setMessage("New entry added successfully.")
                                                .setTitle("Done!")
                                                .setPositiveButton(android.R.string.ok, null);
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                    }else{
                                        AlertDialog.Builder builder = new AlertDialog.Builder(AddStudentActivity.this, R.style.MyAlertDialogStyle);
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
                                    AlertDialog.Builder builder = new AlertDialog.Builder(AddStudentActivity.this, R.style.MyAlertDialogStyle);
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
                            params.put(STUDENT_ID, sId);
                            params.put(STUDENT_NAME, sName);
                            params.put(STUDENT_CLASS, sClass);
                            params.put(STUDENT_BUS, sBus);
                            params.put(STUDENT_STOP, sStop);
                            return params;
                        }

                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(AddStudentActivity.this);
                    requestQueue.add(stringRequest);
                }
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
