package com.fourwhys.waterlink.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fourwhys.waterlink.Entities.User;
import com.fourwhys.waterlink.R;
import com.fourwhys.waterlink.utilities.Constants;
import com.fourwhys.waterlink.activities.MainActivity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private View parent_view;
    private RequestQueue mQueue;
    ProgressDialog mDialog;
    ProgressDialog progressDialog;
    TextView num_contrat_input, password_input;
    Button btn_valider;
    User user;
    String login, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(checkSharedPrefs()){
            setConstants();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        parent_view = findViewById(android.R.id.content);
        num_contrat_input = findViewById(R.id.num_contrat);
        password_input = findViewById(R.id.password);
        btn_valider = (Button) findViewById(R.id.btn_valider);
        mQueue = Volley.newRequestQueue(getApplicationContext());

        progressDialog = new ProgressDialog(LoginActivity.this);
        btn_valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    login =num_contrat_input.getText().toString().trim();
                    password = password_input.getText().toString().trim();
                    loginRequest(login, password);
//
                }
            }
        });
    }

    public boolean validate() {
        boolean valid = true;

        String username = num_contrat_input.getText().toString();
        String password = password_input.getText().toString();

        if (username.length() < 3) {
            num_contrat_input.setError("at least 3 characters");
            valid = false;
        } else {
            num_contrat_input.setError(null);
        }

        if (password.length() > 10) {
            password_input.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            password_input.setError(null);
        }

        return valid;
    }

    public void loginRequest (final String num_contrat, final String password ){
        progressDialog.setMessage("Connection...");
        progressDialog.show();
        user = new User();
        final String url = Constants.WEBSERVICE_URL+"/login";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("user");
                            JSONObject userJson = jsonArray.getJSONObject(0);
                            boolean state = jsonObject.getBoolean("error");
                            Log.d("myTag", "onResponse: "+userJson);
                            if (!state){
                                user.setNum_contrat(Integer.valueOf(userJson.get("num_contrat").toString()));
                                user.setNom(userJson.get("nom").toString());
                                user.setPrenom(userJson.get("prenom").toString());
                                user.setNum_tel(Integer.valueOf(userJson.get("num_tel").toString()));
                                user.setRegion(userJson.get("region").toString());
                                user.setCode_postale(Integer.valueOf(userJson.get("code_postal").toString()));
                                user.setMail(userJson.get("email").toString());
                                setUserSharedPrefs(user);
                                progressDialog.dismiss();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplication(),"Error ", Toast.LENGTH_SHORT).show();
                        Log.d("mriGel", url);
                        Log.d("myTag", "onError: "+error);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("num_contrat", num_contrat);
                params.put("password", password);

                return params;
            }
        };
        mQueue.add(postRequest);
    }
    public void loginSharedPrefs(final String name,  String api_key){
        api_key = "connected";
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.USERNAME, name);
        editor.putString(Constants.API_KEY, api_key);
        editor.apply();
    }

    public void setUserSharedPrefs(final User user){
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString(Constants.USER_STR, json);
        editor.apply();
    }
    public Boolean checkSharedPrefs(){
        String s;
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFS, MODE_PRIVATE);
        s = sharedPreferences.getString(Constants.USER_STR, null);
        if (s != null ){
            return true;
        }
        else
            return false;
    }
    public void setConstants (){
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFS, MODE_PRIVATE);
        String myStr = sharedPreferences.getString(Constants.USER_STR, null);
        User u = gson.fromJson(myStr, User.class);
        Constants.user = u;
    }

}
