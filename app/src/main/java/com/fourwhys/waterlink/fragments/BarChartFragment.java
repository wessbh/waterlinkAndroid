package com.fourwhys.waterlink.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.fourwhys.waterlink.R;
import com.fourwhys.waterlink.utilities.Constants;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BarChartFragment extends Fragment {
    private BarChart barChart;
    ArrayList<BarEntry> barEntries;
    ProgressDialog progressDialog;
    private RequestQueue mQueue;
    public BarChartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bar_chart, container, false);
        progressDialog = new ProgressDialog(getContext(), R.style.AppTheme_Dark_Dialog);
        mQueue = Volley.newRequestQueue(getContext());
        barChart = (BarChart) view.findViewById(R.id.barchart);
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setMaxVisibleValueCount(50);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(true);
        getMonthConsommation(Constants.user.getNum_contrat());


        return view;
    }
    public class MyAxisValueFormatter implements IAxisValueFormatter {
        private String[] mValues;

        public MyAxisValueFormatter(String[] mValues) {
            this.mValues = mValues;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mValues[(int) value];
        }
    }

    public void getMonthConsommation(final int numero_contrat){
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Retrieving data...");
        progressDialog.show();
        barEntries = new ArrayList<>();
        final String url =  Constants.WEBSERVICE_URL+"/consommation_mensuel/"+numero_contrat;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONArray jsonArray = response;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject consommationJson = jsonArray.getJSONObject(i);
                                int value  = Integer.valueOf(consommationJson.getString("total_mois"));
                                int month_id  = Integer.valueOf(consommationJson.getString("id_mois"));
                                barEntries.add(new BarEntry(month_id, value));

                            }
                            BarDataSet barDataSet = new BarDataSet(barEntries, "data set 1");
                            barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                            BarData data = new BarData(barDataSet);
                            data.setBarWidth(0.9f);
                            barChart.animateY(500);
                            barChart.setData(data);
                            String[] months = new String[]{"Jan","Fev", "Mars", "Avril", "May", "Jun", "July", "Aug", "Sep", "Oct", "Nov"};
                            XAxis xAxis = barChart.getXAxis();
                            xAxis.setValueFormatter(new MyAxisValueFormatter(months));
                            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                            xAxis.setCenterAxisLabels(true);
                            xAxis.setAxisMinimum(1f);
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

}
