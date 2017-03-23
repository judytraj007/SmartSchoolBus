package com.judy.smartschoolbus.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.judy.smartschoolbus.R;

import java.util.HashMap;
import java.util.Map;

import static app.Config.DELETE_URL;
import static app.Config.KEY_NEW_PASSWORD;
import static app.Config.KEY_NEW_USERNAME;
import static app.Config.KEY_PASSWORD;
import static app.Config.KEY_USERNAME;
import static app.Config.UPDID_URL;
import static app.Config.UPDPASS_URL;

public class SettingsActivity extends AppCompatActivity {
    EditText newPass;
    EditText newId;
    String id, passW;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        final ProgressBar spinner = (ProgressBar) findViewById(R.id.spinner);
        newId = (EditText) findViewById(R.id.newId);
        newPass = (EditText) findViewById(R.id.newPass);
        Button email = (Button) findViewById(R.id.doneEm);
        Button pass = (Button) findViewById(R.id.donePass);
        Button delete = (Button) findViewById(R.id.doneDelete);
        Button logOut = (Button) findViewById(R.id.logOut);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0358B2")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = newId.getText().toString().trim();
                if (TextUtils.isEmpty(id)) {
                    newId.setError(getString(R.string.error_field_required));
                } else {
                    spinner.setVisibility(View.VISIBLE);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDID_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    spinner.setVisibility(View.GONE);
                                    if (response.trim().equalsIgnoreCase("success")) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this, R.style.MyAlertDialogStyle);
                                        builder.setMessage("Updated successfully.")
                                                .setTitle("Done!")
                                                .setPositiveButton(android.R.string.ok, null);
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                        SharedPreferences.Editor e = sp.edit();
                                        e.clear();
                                        e.commit();
                                        Intent i = new Intent(SettingsActivity.this, LoginActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(i);
                                        finish();
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this, R.style.MyAlertDialogStyle);
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
                                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this, R.style.MyAlertDialogStyle);
                                    builder.setMessage(error.getMessage()) //temp
                                            .setTitle("Error!")
                                            .setPositiveButton(android.R.string.ok, null);
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() {
                            sp = getSharedPreferences("myPref", MODE_PRIVATE);
                            String username = sp.getString("username", " ");
                            Map<String, String> params = new HashMap<String, String>();
                            params.put(KEY_USERNAME, username);
                            params.put(KEY_NEW_USERNAME, id);
                            return params;
                        }

                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(SettingsActivity.this);
                    requestQueue.add(stringRequest);
                }
            }

        });
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passW = newPass.getText().toString().trim();
                if (TextUtils.isEmpty(passW)) {
                    newPass.setError("Invalid!");
                } else {
                    spinner.setVisibility(View.VISIBLE);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDPASS_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    spinner.setVisibility(View.GONE);
                                    if (response.trim().equalsIgnoreCase("success")) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this, R.style.MyAlertDialogStyle);
                                        builder.setMessage("Updated successfully.")
                                                .setTitle("Done!")
                                                .setPositiveButton(android.R.string.ok, null);
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                        SharedPreferences.Editor e = sp.edit();
                                        e.clear();
                                        e.commit();
                                        Intent i = new Intent(SettingsActivity.this, LoginActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(i);
                                        finish();
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this, R.style.MyAlertDialogStyle);
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
                                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this, R.style.MyAlertDialogStyle);
                                    builder.setMessage(error.getMessage()) //temp
                                            .setTitle("Error!")
                                            .setPositiveButton(android.R.string.ok, null);
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() {
                            sp = getSharedPreferences("myPref", MODE_PRIVATE);
                            String password = sp.getString("password", " ");
                            String username = sp.getString("username", " ");
                            Map<String, String> params = new HashMap<String, String>();
                            params.put(KEY_USERNAME, username);
                            params.put(KEY_PASSWORD, password);
                            params.put(KEY_NEW_PASSWORD, passW);
                            return params;
                        }

                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(SettingsActivity.this);
                    requestQueue.add(stringRequest);
                }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder b = new AlertDialog.Builder(SettingsActivity.this, R.style.MyAlertDialogStyle)
                        .setTitle("Delete Account")
                        .setMessage("Are you sure you want to delete your account?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                spinner.setVisibility(View.VISIBLE);
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, DELETE_URL,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                spinner.setVisibility(View.GONE);
                                                if (response.trim().equalsIgnoreCase("success")) {
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this, R.style.MyAlertDialogStyle);
                                                    builder.setMessage("Account Deleted!.")
                                                            .setTitle("Done!")
                                                            .setPositiveButton(android.R.string.ok, null);
                                                    AlertDialog dialog = builder.create();
                                                    dialog.show();
                                                    SharedPreferences.Editor e = sp.edit();
                                                    e.clear();
                                                    e.commit();
                                                    Intent i = new Intent(SettingsActivity.this, LoginActivity.class);
                                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(i);
                                                    finish();
                                                } else {
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this, R.style.MyAlertDialogStyle);
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
                                                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this, R.style.MyAlertDialogStyle);
                                                builder.setMessage(error.getMessage()) //temp
                                                        .setTitle("Error!")
                                                        .setPositiveButton(android.R.string.ok, null);
                                                AlertDialog dialog = builder.create();
                                                dialog.show();
                                            }
                                        }) {
                                    @Override
                                    protected Map<String, String> getParams() {
                                        sp = getSharedPreferences("myPref", MODE_PRIVATE);
                                        String username = sp.getString("username", " ");
                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put(KEY_USERNAME, username);
                                        return params;
                                    }

                                };

                                RequestQueue requestQueue = Volley.newRequestQueue(SettingsActivity.this);
                                requestQueue.add(stringRequest);
                                }

                        });
                b.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                        .setIcon(android.R.drawable.ic_dialog_alert);
                AlertDialog d = b.create();
                d.show();

            }
        });
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("myPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor e = sp.edit();
                e.clear();
                e.commit();
                Intent i = new Intent(SettingsActivity.this, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
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
