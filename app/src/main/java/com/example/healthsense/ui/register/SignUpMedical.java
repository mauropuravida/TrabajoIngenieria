package com.example.healthsense.ui.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.healthsense.R;
import com.example.healthsense.data.PikerDate;
import com.example.healthsense.ui.login.LoginActivity;

public class SignUpMedical extends AppCompatActivity {

    private Context cont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_medical);

        Spinner credentialType = findViewById(R.id.credential_type);
        String[] credentialTypes = new String[]{"DNI", "OTRO"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_color, credentialTypes);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_color);
        credentialType.setAdapter(arrayAdapter);

        Spinner interarlMedicine = findViewById(R.id.interal_medicine);
        String[] interarlMedicines = new String[]{"Traumatologo", "Clínico", "Cardiaco", "Respiratorio", "Kinesiologo"};
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(this,
                R.layout.spinner_color, interarlMedicines);
        arrayAdapter2.setDropDownViewResource(R.layout.spinner_color);
        interarlMedicine.setAdapter(arrayAdapter2);

        TextView birthDate = findViewById(R.id.birth_date);
        TextView signIn = findViewById(R.id.sign_button);

        cont = this;

        birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PikerDate(cont).obtenerFecha(birthDate);
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
