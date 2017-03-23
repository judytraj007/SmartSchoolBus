package Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.judy.smartschoolbus.R;
import com.judy.smartschoolbus.activities.MapsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import app.Config;

import static android.content.Context.MODE_PRIVATE;
import static app.Config.REGISTER_URL;


public class SignupFragment extends Fragment {

    EditText uName;
    EditText pWord;
    EditText emailId;
    Button signBtn;
    RadioGroup radioGroup;
    RadioButton selectBtn;
    ProgressBar mProgressBar;
    String userId,passWord,userType,email;
    Context con;
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        con=getActivity();
        return  inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        uName=(EditText)getView().findViewById(R.id.uName);
        pWord=(EditText)getView().findViewById(R.id.pWord);
        emailId=(EditText)getView().findViewById(R.id.emailId);
        radioGroup=(RadioGroup) getView().findViewById(R.id.rGroup);
        signBtn=(Button) getView().findViewById(R.id.signUpBtn);
        mProgressBar = (ProgressBar) getView().findViewById(R.id.progressBar2);
        sharedPreferences=getActivity().getSharedPreferences("myPref",MODE_PRIVATE);
        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userId=uName.getText().toString().trim();
                passWord=pWord.getText().toString().trim();
                email=emailId.getText().toString().trim();
                selectBtn=(RadioButton)getView().findViewById(radioGroup.getCheckedRadioButtonId());
                if (!(radioGroup.getCheckedRadioButtonId() == -1))
                    userType = selectBtn.getText().toString().toLowerCase().trim();
                if(TextUtils.isEmpty(userId))
                    uName.setError(getString(R.string.error_field_required));
                else   if(TextUtils.isEmpty(email))
                    emailId.setError(getString(R.string.error_field_required));
                else   if(TextUtils.isEmpty(passWord))
                    pWord.setError(getString(R.string.error_field_required));
                else {

                    mProgressBar.setVisibility(View.VISIBLE);
                    register(userId, email, passWord, userType);
                }
            }

        });
    }
    private void register(String name, String username,final String password, String usertype) {
        SharedPreferences pref = getActivity().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        String urlSuffix = "?regId="+regId+"&name="+name+"&username="+username+"&password="+password+"&usertype="+usertype;
        class RegisterUser extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                mProgressBar.setVisibility(View.GONE);
                if(s.equalsIgnoreCase("success")){
                    SharedPreferences.Editor e = sharedPreferences.edit();
                    e.putString("username",email);
                    e.putString("password",password);
                    e.commit();
                    getUserType();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.MyAlertDialogStyle);
                    //builder.setMessage(task.getException().getMessage())
                    builder.setMessage(s) //temp
                            .setTitle("Error!")
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

            }

            @Override
            protected String doInBackground(String... params) {
                String s = params[0];
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(REGISTER_URL+s);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    return "success";
                }catch(Exception e){
                    return null;
                }
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(urlSuffix);
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
        Intent intent = new Intent(getActivity(), MapsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }
}
