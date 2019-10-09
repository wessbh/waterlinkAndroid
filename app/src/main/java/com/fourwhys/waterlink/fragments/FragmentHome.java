package com.fourwhys.waterlink.fragments;


import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fourwhys.waterlink.R;
import com.fourwhys.waterlink.activities.MainActivity;
import com.fourwhys.waterlink.utilities.Constants;
import com.github.anastr.speedviewlib.TubeSpeedometer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import pl.pawelkleczkowski.customgauge.CustomGauge;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHome extends Fragment {

    MainActivity act;
    //private CustomGauge gauge;
    private TubeSpeedometer gauge;
    private TextView consommation_value, tranche_value, prix;
    private EditText seuil_field;
    private Button btn_reload;
    private RequestQueue mQueue;
    View view;
    float prix_value;

    public FragmentHome() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.fragment_home, container, false);
        act = (MainActivity) getActivity();
        mQueue = Volley.newRequestQueue(getContext());
        gauge = (TubeSpeedometer) view.findViewById(R.id.gauge);
        consommation_value = (TextView) view.findViewById(R.id.consommation_value);
        prix = (TextView) view.findViewById(R.id.prix);
        tranche_value = (TextView) view.findViewById(R.id.tranche_value);
        seuil_field = (EditText) view.findViewById(R.id.seuil_field);
        btn_reload = view.findViewById(R.id.btn_reload);
        getValues(Constants.user.getNum_contrat());
        btn_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!seuil_field.getText().toString().equals("")){
                    final int seuil_val = Integer.valueOf(seuil_field.getText().toString());
                    if(seuil_val < prix_value){
                        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(getContext());
                        myAlertDialog.setTitle("--- Attention ! ---");
                        myAlertDialog.setMessage("Le prix a déja dépassé: "+seuil_field.getText().toString());
                        myAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                // do something when the OK button is clicked
                                updateSeuil(seuil_val);
                                refresh_fragment();
                            }});
                        myAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                // do something when the Cancel button is clicked
                            }});
                        myAlertDialog.show();
                    }
                    else {
                        updateSeuil(seuil_val);
                        refresh_fragment();
                    }
                }
                else{

                    refresh_fragment();
                }
            }
        });

        return view;
    }

    public void getValues (int num_contrat) {
        final String url =  Constants.WEBSERVICE_URL+"/total/"+num_contrat;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("consommation");
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                JSONObject obj = jsonArray.getJSONObject(0);
                            int total = obj.getInt("total");
                            int tranche = obj.getInt("tranche");
                            float prix_val = Float.valueOf(obj.getString("prix"));
                            float prixx = prix_val/1000;
                            int max_val = obj.getInt("seuil");
                            max_val = max_val *1000;
                            String prix_str = (new DecimalFormat("##.##").format(prixx))+" Dt";
                            prix.setText(prix_str);
                            prix_value = prix_val;
                            consommation_value.setText(String.valueOf(total));
                            tranche_value.setText(String.valueOf(tranche));
                            if(max_val == 0){
                                gauge.setMaxSpeed(20);
                            }
                            else{
                                Log.d("max_val", "in else :"+max_val);
                                gauge.setMaxSpeed(max_val);
                                gauge.speedTo(prix_value);
                                gauge.setWithEffects3D(false);
                            }
                            //Toast.makeText(getContext(), ""+gauge.getStartValue()+"-"+gauge.getEndValue()+"-"+gauge.getValue(), Toast.LENGTH_SHORT).show();

                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("mriGel", url);
                            Log.d("mriGel", ""+Constants.user.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
        gauge.refreshDrawableState();
        gauge.invalidate();
    }
    public void updateSeuil(int seuil) {
        final String url =  Constants.WEBSERVICE_URL+"/update_seuil?user_id="+Constants.user.getNum_contrat()+"&seuil="+seuil;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("seuil_update", "seuil_updated");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }
    public void refresh_fragment(){
        FragmentHome fragmentHome = new FragmentHome();
        act.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragmentHome).commit();
    }
}
