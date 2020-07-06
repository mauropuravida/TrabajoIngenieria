package com.example.healthsense.ui.traininginformation;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import com.example.healthsense.R;
import com.example.healthsense.Resquest.doAsync;
import com.example.healthsense.db.Repository.ExercisesRepository;
import com.example.healthsense.db.Repository.WorkoutsExercisesRepository;
import com.example.healthsense.db.Repository.WorkoutsReportsRepository;
import com.example.healthsense.db.Repository.WorkoutsRepository;

import java.util.ArrayList;
import java.util.HashMap;

public class EditFragment extends Fragment{
    public static Fragment fg;
    private View root;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_training_information, container, false);

        root.setBackgroundResource(R.color.Background);

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {

                getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fg).addToBackStack(null).commit();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        return root;
    }

}
