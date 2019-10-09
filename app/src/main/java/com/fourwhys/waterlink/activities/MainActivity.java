package com.fourwhys.waterlink.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fourwhys.waterlink.R;
import com.fourwhys.waterlink.fragments.BarChartFragment;
import com.fourwhys.waterlink.fragments.FragmentCharts;
import com.fourwhys.waterlink.fragments.FragmentFacture;
import com.fourwhys.waterlink.fragments.FragmentFactureListView;
import com.fourwhys.waterlink.fragments.FragmentHome;
import com.fourwhys.waterlink.fragments.FragmentProfile;
import com.fourwhys.waterlink.fragments.RealTimeChart;
import com.fourwhys.waterlink.utilities.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private AccountHeader headerResult = null;
    private Drawer result = null;
    private FragmentManager fm;
    private RequestQueue mQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentHome fragmentHome = new FragmentHome();
        fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.frame_container, fragmentHome).commit();
        mQueue = Volley.newRequestQueue(getApplicationContext());
        FirebaseApp.initializeApp(this);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        sendRegistrationToServer(token);
                        Log.d("firebaseToken", msg);
                        //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Accueil");
        setSupportActionBar(toolbar);
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHasStableIds(true)
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Accueil").withIcon(FontAwesome.Icon.faw_tachometer_alt).withIdentifier(1).withSelectable(true),
                        new SectionDrawerItem().withName("").withDivider(true),
                        new PrimaryDrawerItem().withName("Mon Profil").withIcon(FontAwesome.Icon.faw_user).withIdentifier(2).withSelectable(true),
                        new PrimaryDrawerItem().withName("Consommation").withIcon(FontAwesome.Icon.faw_chart_line).withIdentifier(3).withSelectable(true),
                        new PrimaryDrawerItem().withName("Mes Factures").withIcon(FontAwesome.Icon.faw_clipboard_list).withIdentifier(4).withSelectable(true),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Se déconnecter").withIcon(FontAwesome.Icon.faw_sign_out_alt).withIdentifier(21).withSelectable(false)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Fragment f2 = null;
                        if (drawerItem != null) {
                            if (drawerItem.getIdentifier() == 1) {
                                f2 = new FragmentHome();
                            }
                            if (drawerItem.getIdentifier() == 2) {
                                f2 = new FragmentProfile();
                            }
                            if (drawerItem.getIdentifier() == 3) {
                                f2 = new FragmentCharts();
                            }
                            if (drawerItem.getIdentifier() == 4) {
                                f2 = new FragmentFactureListView();
                            }
                            if (drawerItem.getIdentifier() == 21) {
                                disconnect();
                                Toast.makeText(MainActivity.this, "Disconnected !", Toast.LENGTH_SHORT).show();
                            }
                            if (((f2 != null) && isOnline())) {
                                changeFragment(f2);
                            }

                        }

                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
                .withOnDrawerNavigationListener(new Drawer.OnDrawerNavigationListener() {
                    @Override
                    public boolean onNavigationClickListener(View clickedView) {
                        onBackPressed();
                        //}
                        return true;
                    }
                })
                .build();
    }
    public Boolean checkSharedPrefs(){
        String s;
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFS, MODE_PRIVATE);
        s = sharedPreferences.getString(Constants.USER_STR, null);
        if (s != null ){
            Log.d("myShared", s.toString());
            return true;
        }
        else
            return false;
    }
    public void setSharedPrefs(final String name){
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.USER_STR, name);
        editor.apply();
    }
    public void disconnect(){
        setSharedPrefs(null);
        Intent intent = new Intent( MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    public void changeIntent (Intent intent){
        startActivity(intent);
        this.finish();
        this.overridePendingTransition(0, 0);
    }
    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
    public void changeFragment(Fragment fragment){

        FragmentTransaction transaction =fm.beginTransaction();
        transaction.add(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void refresh_chart(Fragment fragment){
        FragmentTransaction transaction =fm.beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }

    public void sendRegistrationToServer(final String token) {

        final String url = Constants.WEBSERVICE_URL + "/add_device_id";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("tokenState", "Done");
                        Log.d("tokenValue", token);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplication(), "Error ", Toast.LENGTH_SHORT).show();
                        Log.d("URL:", url);
                        Log.d("myTag", "onError: " + error);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("num_contrat", Constants.user.getNum_contrat() + "");
                params.put("token", token);

                return params;
            }
        };
        mQueue.add(postRequest);
    }
}
