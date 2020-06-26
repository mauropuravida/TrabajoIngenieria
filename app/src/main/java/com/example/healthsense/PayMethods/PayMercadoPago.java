package com.example.healthsense.PayMethods;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.healthsense.MainActivity;
import com.example.healthsense.R;
import com.mercadopago.android.px.core.MercadoPagoCheckout;
import com.mercadopago.android.px.model.Payment;
import com.mercadopago.android.px.model.exceptions.MercadoPagoError;
import com.mercadopago.callbacks.Callback;
import com.mercadopago.core.CustomServer;
import com.mercadopago.model.ApiException;
import com.mercadopago.preferences.CheckoutPreference;
import com.mercadopago.util.JsonUtil;
import com.mercadopago.util.LayoutUtil;

import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_CANCELED;

/**
 * A simple {@link Fragment} subclass.
 */
public class PayMercadoPago extends Fragment {

    private View root;
    //String pk = "TEST-ad365c37-8012-4014-84f5-6c895b3f8e0a";
    String pk = "TEST-7d36b114-d09c-4884-8b5c-d9e9575533b3";
    private static final int REQUEST_CODE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_suscriptions, container, false);

        return root;
    }

    private void startMercadoPagoCheckout(String checkoutPreferenceId) {
        new MercadoPagoCheckout.Builder(pk, checkoutPreferenceId)
                .build()
                .startPayment(root.getContext(), REQUEST_CODE);
    }

    public void submit(View view) {
// Create a map with payment’s details.
        Map<String, Object> preferenceMap = new HashMap<>();
        preferenceMap.put("id", "1");
        preferenceMap.put("title", "");
        preferenceMap.put("quantity", 10);
        preferenceMap.put("currency_id", "ARS");
        preferenceMap.put("unit_price", 11.4);
        preferenceMap.put("email", "eloy@hotmail.com");


        final Activity activity = new MainActivity();
        LayoutUtil.showProgressLayout(activity);
        CustomServer.createCheckoutPreference(activity, "https://mauropuravida.000webhostapp.com", "preference.php", preferenceMap, new Callback<CheckoutPreference>() {


            @Override
            public void success(CheckoutPreference checkoutPreference) {
                Log.d("ERROR DE SALIDA", "BIEN HECHO");
                startMercadoPagoCheckout(checkoutPreference.getId());
                LayoutUtil.showRegularLayout(activity);
            }

            @Override
            public void failure(ApiException apiException) {
                Log.d("ERROR DE SALIDA", apiException.getMessage());
                LayoutUtil.showRegularLayout(activity);
            }
        });
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == MercadoPagoCheckout.PAYMENT_RESULT_CODE) {
                final Payment payment = (Payment) data.getSerializableExtra(MercadoPagoCheckout.EXTRA_PAYMENT_RESULT);
                //((TextView) findViewById(R.id.mp_results)).setText("Resultado del pago: " + payment.getPaymentStatus());


                //Done!
            } else if (resultCode == RESULT_CANCELED) {
                if (data != null && data.getStringExtra("mercadoPagoError") != null) {
                    MercadoPagoError mercadoPagoError = JsonUtil.getInstance().fromJson(data.getStringExtra("mercadoPagoError"), MercadoPagoError.class);
                    //((TextView) findViewById(R.id.mp_results)).setText("Error: " +  mercadoPagoError.getMessage());
                    //Resolve error in checkout
                } else {
                    //Resolve canceled checkout
                }
            }
        }
    }

}
