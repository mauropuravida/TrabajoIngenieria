package com.example.healthsense.ui.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.healthsense.R;

/*
inicializa la pantalla donde se determina el tipo de perfil que se va a registrar
 */
public class ChoiseProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_profile);

        final Button userButton = findViewById(R.id.userButton);
        final Button medicalButton = findViewById(R.id.medicalButton);

        userButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent intent;
                  intent = new Intent(getApplicationContext(), SignUpUser.class);
                  startActivity(intent);
              }
          }
        );

        medicalButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent intent;
                  intent = new Intent(getApplicationContext(), SignUpMedical.class);
                  startActivity(intent);
              }
          }
        );
    }
}
