package com.example.healthsense.ui.payment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.example.healthsense.R;

public class PaymentFragment extends Fragment {

    /*
    TODO en esta clase se espera la implementación para que el medico pueda colocar los datos bancarios para recibir su pago mensual
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_payment, container, false);

        return root;
    }
}
