package com.fourwhys.waterlink.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fourwhys.waterlink.R;
import com.fourwhys.waterlink.utilities.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentProfile extends Fragment {
    TextView num_contrat, nom, telephone, mail;

    public FragmentProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile, container, false);
        num_contrat = view.findViewById(R.id.num_contratt);
        num_contrat.setText(String.valueOf(Constants.user.getNum_contrat()));

        nom = view.findViewById(R.id.nom);
        nom.setText(Constants.user.getPrenom()+" "+Constants.user.getPrenom());

        telephone = view.findViewById(R.id.telephone);
        telephone.setText(String.valueOf(Constants.user.getNum_tel()));

        mail = view.findViewById(R.id.mail);
        mail.setText(Constants.user.getMail());

        return view;
    }

}
