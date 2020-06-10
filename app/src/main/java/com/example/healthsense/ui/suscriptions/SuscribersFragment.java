package com.example.healthsense.ui.suscriptions;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.healthsense.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SuscribersFragment extends Fragment {


    public SuscribersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_suscribers, container, false);
    }

}
