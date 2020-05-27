package com.example.healthsense.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.healthsense.MainActivity;
import com.example.healthsense.R;
import com.example.healthsense.ui.login.LoginActivity;

import static com.example.healthsense.MainActivity.PREFS_FILENAME;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        Button closeSession = (Button) root.findViewById(R.id.closeSession);
        closeSession.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    SharedPreferences preferencesEditor = root.getContext().getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE);
                    preferencesEditor.edit().putString("User","").apply();
                    preferencesEditor.edit().putString("Pass","").apply();

                    Intent intent;
                    intent = new Intent(root.getContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        );
        return root;
    }
}