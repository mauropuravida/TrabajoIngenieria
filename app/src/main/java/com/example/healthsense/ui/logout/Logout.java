package com.example.healthsense.ui.logout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.healthsense.R;
import com.example.healthsense.ui.login.LoginActivity;

import static com.example.healthsense.MainActivity.PREFS_FILENAME;

public class Logout extends Fragment {

    /*
    Se borran los datos guardados de user y pass del usuario, en el caso que estuviera recordado
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        SharedPreferences preferencesEditor = root.getContext().getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE);
        preferencesEditor.edit().putString("User","").apply();
        preferencesEditor.edit().putString("Pass","").apply();

        Intent intent;
        intent = new Intent(root.getContext(), LoginActivity.class);
        startActivity(intent);

        return root;
    }

}
