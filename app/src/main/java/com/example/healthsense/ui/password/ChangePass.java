package com.example.healthsense.ui.password;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.example.healthsense.R;

public class ChangePass extends Fragment {

    /*
    Se inicializa el fragmento y la vista de layout para cambio de contraseña
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_change_pass, container, false);

        return root;
    }
}
