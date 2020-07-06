package com.example.healthsense.ui.urgency;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.healthsense.R;

/**
 * Fragmento que inicializa la vista y comportamiento de la vista de emergencias
 */
public class UrgencyFragment extends Fragment {


    public UrgencyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_urgency, container, false);
    }

}
