package com.example.healthsense.ui.urgency;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.healthsense.R;

/**
 * Este fragmento se encarga de inicializar la vista de datos de urgencia para el medico y tod-o su comportamiento
 */
public class UrgencyMedicalFragment extends Fragment {


    public UrgencyMedicalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_urgency_medical, container, false);
    }

}
