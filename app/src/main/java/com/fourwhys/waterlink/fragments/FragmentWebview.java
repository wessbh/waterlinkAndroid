package com.fourwhys.waterlink.fragments;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fourwhys.waterlink.R;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentWebview extends Fragment {
    PDFView pdfView;
    String path;

    public FragmentWebview() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_webview, container, false);

        pdfView = view.findViewById(R.id.pdfView);
        path = getArguments().getString("path");
        Log.d("inWebviewMrigel", ""+path);
        pdfView.fromUri(Uri.fromFile(new File(path))).load();
        return view;
    }

}
