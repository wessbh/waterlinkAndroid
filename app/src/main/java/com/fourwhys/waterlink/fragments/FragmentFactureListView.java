package com.fourwhys.waterlink.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fourwhys.waterlink.Entities.Facture;
import com.fourwhys.waterlink.R;
import com.fourwhys.waterlink.activities.MainActivity;
import com.fourwhys.waterlink.utilities.Constants;
import com.fourwhys.waterlink.utilities.FactureListAdapter;
import com.fourwhys.waterlink.utilities.RecyclerTouchListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentFactureListView extends Fragment {
    FactureListAdapter mAdapter;
    private RequestQueue mQueue;
    private List<Facture> factureList = new ArrayList<>();
    private RecyclerView recyclerView;
    ProgressDialog progressDialog;

    public FragmentFactureListView() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_facture_list_view, container, false);
        progressDialog = new ProgressDialog(getContext(), R.style.AppTheme_Dark_Dialog);
        mAdapter = new FactureListAdapter(getContext(), factureList);
        recyclerView = view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mQueue = Volley.newRequestQueue(getContext());
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Facture facture = factureList.get(position);

                Log.d("mriGel", ""+facture.toString());
                Bundle bundle=new Bundle();
                bundle.putInt("id_facture", facture.getId());
                bundle.putInt("num_contrat", Constants.user.getNum_contrat());
                bundle.putString("periode", facture.getPeriode());
                //set Fragmentclass Arguments
                FragmentFacture fragmentFacture=new FragmentFacture();
                fragmentFacture.setArguments(bundle);
                MainActivity act = (MainActivity) getActivity();
                act.changeFragment(fragmentFacture);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        getFactures(Constants.user.getNum_contrat());
        return view;
    }



    public void getFactures(int num_contrat){
        final String url =  Constants.WEBSERVICE_URL+"/get_facture/"+num_contrat;
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Retrieving data...");
        progressDialog.show();

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONArray jsonArray = response;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject factureJson = jsonArray.getJSONObject(i);
                                Facture facture = new Facture();
                                Log.d("mriGel", ""+response.toString());
                                int id = factureJson.getInt("id");
                                String date_debut = factureJson.getString("date_debut");
                                String date_fin = factureJson.getString("date_fin");
                                String[] parts_debut = date_debut.split("(?=T)");
                                String periode_debut = parts_debut[0];
                                String[] parts_fin = date_fin.split("(?=T)");
                                String periode_fin = parts_fin[0];
                                String periode_debufin = "De "+periode_debut +" A "+periode_fin;

                                String status = String.valueOf(factureJson.getInt("status"));
                                facture.setId(id);
                                facture.setPeriode(periode_debufin);
                                facture.setStatus(status);
                                factureList.add(facture);
                            }
                            progressDialog.dismiss();
                            mAdapter = new FactureListAdapter(getContext(), factureList);
                            recyclerView.setAdapter(mAdapter);
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d("mriGel", url);
            }
        });
        mQueue.add(request);
    }
}
