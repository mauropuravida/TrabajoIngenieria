package com.example.healthsense.ui.password;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

public class ChangePass extends Fragment {

    public ChangePass() {
        // Required empty public constructor
    }

    public static ChangePass newInstance(String param1, String param2) {
        ChangePass fragment = new ChangePass();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
