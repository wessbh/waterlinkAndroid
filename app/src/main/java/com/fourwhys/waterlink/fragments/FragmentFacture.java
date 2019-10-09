package com.fourwhys.waterlink.fragments;


import android.app.ProgressDialog;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.print.pdf.PrintedPdfDocument;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.fourwhys.waterlink.Entities.Facture;
import com.fourwhys.waterlink.R;
import com.fourwhys.waterlink.activities.MainActivity;
import com.fourwhys.waterlink.utilities.Constants;
import com.fourwhys.waterlink.utilities.FactureListAdapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentFacture extends Fragment {

    Button btnCreate;
    EditText editText;
    TextView num_contrat, periode, region, adresse, nom, consommation, total_sonede, total_tva, total_onas, total;
    private RequestQueue mQueue;
    ProgressDialog progressDialog;
    Facture facture;
    String periode_str;
    int id_facture, num_contr;
    public FragmentFacture() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_facture, container, false);
        progressDialog = new ProgressDialog(getContext(), R.style.AppTheme_Dark_Dialog);
        btnCreate = (Button) view.findViewById(R.id.create);
        num_contrat = view.findViewById(R.id.num_contrat);
        periode = view.findViewById(R.id.periode);
        region = view.findViewById(R.id.region);
        adresse = view.findViewById(R.id.adresse);
        nom = view.findViewById(R.id.nom);
        consommation = view.findViewById(R.id.consommation);
        total_sonede = view.findViewById(R.id.total_sonede);
        total_tva= view.findViewById(R.id.total_tva);
        total_onas= view.findViewById(R.id.total_onas);
        total= view.findViewById(R.id.total);
        facture = new Facture();
        mQueue = Volley.newRequestQueue(getContext());
        id_facture=getArguments().getInt("id_facture");
        num_contr=getArguments().getInt("num_contrat");
        periode_str=getArguments().getString("periode");
        periode.setText(periode_str);
        getFacture(num_contr, id_facture);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pdfCreate(editText.getText().toString());
                try {
                    createPdf();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    private void pdfCreate(String sometext){
        // create a new document
        PdfDocument document = new PdfDocument();
        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);
        document.finishPage(page);

        // write the document content
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/mypdf/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path+"test-2.pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(getContext(), "Done", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e("main", "error "+e.toString());
            Toast.makeText(getContext(), "Something wrong: " + e.toString(),  Toast.LENGTH_LONG).show();
        }
        // close the document
        document.close();

    }

    public void createPdf() throws FileNotFoundException, DocumentException {
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Retrieving data...");
        progressDialog.show();
        Document doc = new Document();
// write the document content
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/waterLink/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path+"test-2.pdf";
        PdfWriter.getInstance(doc, new FileOutputStream(targetPdf));
        doc.open();
        PdfPCell cellOne = new PdfPCell(new Paragraph("Facture"));
        doc.add(new Paragraph("Facture"));
        doc.add(new Paragraph(periode_str));
        LineSeparator ls = new LineSeparator();

        doc.add(new Chunk(ls));
        doc.add(new Paragraph("Client: "+nom.getText().toString()));
        doc.add(new Paragraph("Numéro de contrat: "+num_contrat.getText().toString()));
        doc.add(new Paragraph("Région: "+region.getText().toString()));
        doc.add(new Paragraph("Addresse: "+adresse.getText().toString()));

        doc.add(new Chunk(ls));
        doc.add(new Paragraph("Consommation totale en m3: "+consommation.getText().toString()));
        doc.add(new Paragraph("Totale Sonede en Dt: "+total_sonede.getText().toString()));
        doc.add(new Paragraph("Totale Sonede + Tva en Dt: "+total_tva.getText().toString()));
        doc.add(new Paragraph("Totale Onas en Dt: "+total_onas.getText().toString()));
        doc.add(new Paragraph("Totale en Dt: "+total.getText().toString()));
        doc.close();
        progressDialog.dismiss();
        Bundle bundle=new Bundle();
        bundle.putString("path", targetPdf);
        FragmentWebview fragmentPdf=new FragmentWebview();
        fragmentPdf.setArguments(bundle);
        MainActivity act = (MainActivity) getActivity();
        act.changeFragment(fragmentPdf);

    }


    public void getFacture(final int numero_contrat, int id_facture){
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Retrieving data...");
        progressDialog.show();
        final String url =  Constants.WEBSERVICE_URL+"/get_facturedetails/"+numero_contrat+"/"+id_facture;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONArray jsonArray = response;

                            Log.d("mriGel", ""+url);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject factureJson = jsonArray.getJSONObject(i);
                                int id = factureJson.getInt("id");
                                String date_debut = factureJson.getString("date_debut");
                                String date_fin = factureJson.getString("date_fin");
                                float totale_m = (Float.valueOf(factureJson.getString("total_litres"))/1000);
                                float prix_sonede = Float.valueOf(factureJson.getString("prix_sonede"));
                                float prix_onas = Float.valueOf(factureJson.getString("prix_onas"));
                                float prix_facture = Float.valueOf(factureJson.getString("prix_facture"));
                                String status = String.valueOf(factureJson.getInt("status"));
                                String regionn = factureJson.getString("region");
                                String addr = factureJson.getString("adresse");
                                String user_fullname = factureJson.getString("nom")+" "+factureJson.getString("prenom");
                                //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-YYYY");
//                                DateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
//                                Date date_db = simpleDateFormat.parse(date_debut);
//                                Date date_fn = simpleDateFormat.parse(date_fin);
//                                String date_d = simpleDateFormat.format(date_db);
//                                String date_f = simpleDateFormat.format(date_fn);
//                                String periode = date_d+"--"+date_f;
                                String periode = date_debut;
                                //String periode = date_debut+"--"+date_fin;
                                facture.setPeriode(periode);
                                facture.setStatus(status);
                                num_contrat.setText(String.valueOf(numero_contrat));
                                nom.setText(user_fullname);
                                region.setText(regionn);
                                adresse.setText(addr);
                                consommation.setText(String.valueOf(totale_m));
                                total_sonede.setText(String.valueOf(prix_sonede));
                                total_tva.setText(String.valueOf(prix_sonede));
                                total_onas.setText(String.valueOf(prix_onas));
                                total.setText(String.valueOf(prix_facture));
                            }
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("mriGel za3ma?", url);
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
