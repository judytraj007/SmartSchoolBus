package Fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.judy.smartschoolbus.R;
import com.judy.smartschoolbus.activities.MapsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import app.Config;

import static android.content.Context.MODE_PRIVATE;
import static app.Config.KEY_PASSWORD;
import static app.Config.KEY_USERNAME;
import static app.Config.LOGIN_URL;


public class LoginFragment extends Fragment {

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private ProgressBar spinner;
    private View mProgressView;
    private View mLoginFormView;
    SharedPreferences sharedPreferences;
    String username,password;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

            sharedPreferences=getActivity().getSharedPreferences("myPref",MODE_PRIVATE);

            mEmailView = (AutoCompleteTextView)getView().findViewById(R.id.email);
            spinner = (ProgressBar)getView().findViewById(R.id.progressBar1);
            mPasswordView = (EditText)getView().findViewById(R.id.password);


            Button mEmailSignInButton = (Button)getView().findViewById(R.id.email_sign_in_button);
            if(sharedPreferences.contains("username")) {
                startActivity(new Intent(getActivity(), MapsActivity.class));
                getActivity().finish();
            }
            mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    username = mEmailView.getText().toString();
                    password = mPasswordView.getText().toString();
                    mEmailView.setError(null);
                    mPasswordView.setError(null);


                    boolean cancel = false;
                    View focusView = null;

                    if (TextUtils.isEmpty(password)) {
                        mPasswordView.setError(getString(R.string.error_invalid_password));
                        focusView = mPasswordView;
                        cancel = true;
                    }

                    if (TextUtils.isEmpty(username)) {
                        mEmailView.setError(getString(R.string.error_field_required));
                        focusView = mEmailView;
                        cancel = true;
                    }

                    if (cancel) {
                        focusView.requestFocus();
                    } else {
                        userLogin(username,password);

                    }
                }
            });

            mLoginFormView = getView().findViewById(R.id.login_form);

        }
    private void userLogin(final String username,final String password) {
       StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals("success")){
                            SharedPreferences.Editor e = sharedPreferences.edit();
                            e.putString("username",username);
                            e.putString("password",password);
                            e.commit();
                            getUserType();

                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.MyAlertDialogStyle);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.MyAlertDialogStyle);
                        builder.setMessage(error.toString()) //temp
                                .setTitle("Error!")
                                .setPositiveButton(android.R.string.ok, null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }){
           @Override
           protected Map<String, String> getParams() throws AuthFailureError {
               Map<String,String> map = new HashMap<String,String>();
               map.put(KEY_USERNAME,username);
               map.put(KEY_PASSWORD,password);
               return map;
           }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
    public void getUserType(){
        String username=sharedPreferences.getString("username"," ");
        String url = Config.TYPE_URL+username.trim();
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
                    JSONObject geoData = result.getJSONObject(0);
                    String usertype = geoData.getString(Config.KEY_TYPE);

                    SharedPreferences.Editor e = sharedPreferences.edit();
                    e.putString("usertype",usertype);
                    e.commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),error.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        Toast.makeText(getActivity(),"LogIn Successful",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getActivity(), MapsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }
}

