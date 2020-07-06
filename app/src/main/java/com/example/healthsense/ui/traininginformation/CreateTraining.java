package com.example.healthsense.ui.traininginformation;


import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthsense.R;
import com.example.healthsense.ui.suscriptions.SuscribersFragment;

/**
 * Inicialización de plantilla de creación de entrenamientos
 */
public class CreateTraining extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_create_training, container, false);
        Fragment me = this;

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, SuscribersFragment.fg).addToBackStack(null).commit();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        createNewForm(root);

        return root;
    }

    /*
    Creacion de plantilla que se rellena con los datos que provienen del backend de cada entrenamiento
     */
    private void createNewForm(View root){

        LinearLayout ll3 = new LinearLayout(root.getContext());

        LinearLayout.LayoutParams lp10 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lp10.setMargins(0,dpToPx(20,root.getContext()),0,0);

        LinearLayout ll4 = new LinearLayout(root.getContext());
        ll4.setOrientation(LinearLayout.HORIZONTAL);
        ll4.setLayoutParams(lp10);

        LinearLayout.LayoutParams lp0 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp0.weight = 1;

        Space s0 = new Space(root.getContext());
        s0.setLayoutParams(lp0);

        LinearLayout.LayoutParams lp11 = new LinearLayout.LayoutParams(dpToPx(30,root.getContext()), dpToPx(30,root.getContext()));

        Button bt2 = new Button(root.getContext());
        bt2.setBackground(root.getResources().getDrawable(R.drawable.button_state_red));
        bt2.setText("X");
        bt2.setTextColor(root.getResources().getColor(R.color.DarkGrayText));
        bt2.setLayoutParams(lp11);
        bt2.setTag("bt2");

        bt2.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Toast.makeText( root.getContext(), root.getResources().getString(R.string.exercise_remove_info), Toast.LENGTH_SHORT).show();
                   ((LinearLayout)root.findViewById(R.id.list)).removeView(ll3);
               }
           }
        );

        ll4.addView(s0);
        ll4.addView(bt2);

        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(40,root.getContext()));
        lp1.setMargins(0,dpToPx(10,root.getContext()),0,0);

        EditText e1 = new EditText(root.getContext());
        e1.setBackground(root.getResources().getDrawable(R.drawable.background_white_form));
        e1.setPadding(30,0,30,0);
        e1.setHint(root.getResources().getString(R.string.name));
        e1.setGravity(Gravity.CENTER_VERTICAL);
        e1.setLayoutParams(lp1);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lp2.setMargins(0,dpToPx(10,root.getContext()),0,0);

        LinearLayout ll1 = new LinearLayout(root.getContext());
        ll1.setOrientation(LinearLayout.HORIZONTAL);
        ll1.setLayoutParams(lp2);

        LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(40,root.getContext()));
        lp3.weight = 1;

        EditText e2 = new EditText(root.getContext());
        e2.setBackground(root.getResources().getDrawable(R.drawable.background_white_form));
        e2.setPadding(30,0,30,0);
        e2.setHint(root.getResources().getString(R.string.time));
        e2.setGravity(Gravity.CENTER_VERTICAL);
        e2.setLayoutParams(lp3);

        Space s1 = new Space(root.getContext());
        s1.setMinimumWidth(dpToPx(30,root.getContext()));

        LinearLayout.LayoutParams lp4 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(40,root.getContext()));
        lp4.weight = 1;

        EditText e3 = new EditText(root.getContext());
        e3.setBackground(root.getResources().getDrawable(R.drawable.background_white_form));
        e3.setPadding(30,0,30,0);
        e3.setHint(root.getResources().getString(R.string.difficulty));
        e3.setGravity(Gravity.CENTER_VERTICAL);
        e3.setLayoutParams(lp4);

        ll1.addView(e2);
        ll1.addView(s1);
        ll1.addView(e3);

        LinearLayout.LayoutParams lp5 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(40,root.getContext()));
        lp5.setMargins(0,dpToPx(10,root.getContext()),0,0);

        EditText e4 = new EditText(root.getContext());
        e4.setBackground(root.getResources().getDrawable(R.drawable.background_white_form));
        e4.setPadding(30,0,30,0);
        e4.setHint(root.getResources().getString(R.string.url));
        e4.setGravity(Gravity.CENTER_VERTICAL);
        e4.setLayoutParams(lp5);

        LinearLayout.LayoutParams lp6 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(40,root.getContext()));

        TextView t1 = new TextView(root.getContext());
        t1.setTextColor(root.getResources().getColor(R.color.DarkGrayText));
        t1.setText(root.getResources().getString(R.string.instructions));
        t1.setGravity(Gravity.CENTER_VERTICAL);
        t1.setTextSize(18);
        t1.setLayoutParams(lp6);

        LinearLayout.LayoutParams lp7 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(150,root.getContext()));
        lp7.setMargins(0,dpToPx(10,root.getContext()),0,0);

        ScrollView sc1 = new ScrollView(root.getContext());
        sc1.setLayoutParams(lp7);
        sc1.setBackground(root.getResources().getDrawable(R.drawable.background_white_form));


        EditText e5 = new EditText(root.getContext());
        e5.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        e5.setPadding(30,0,30,0);
        e5.setBackground(root.getResources().getDrawable(R.drawable.background_white_form));

        sc1.addView(e5);

        LinearLayout.LayoutParams lp8 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp8.setMargins(0,dpToPx(20,root.getContext()),0,0);

        LinearLayout ll2 = new LinearLayout(root.getContext());
        ll2.setOrientation(LinearLayout.HORIZONTAL);
        ll2.setGravity(Gravity.CENTER_VERTICAL);
        ll2.setGravity(Gravity.CENTER);
        ll2.setLayoutParams(lp8);

        Button bt1 = new Button(root.getContext());
        bt1.setBackground(root.getResources().getDrawable(R.drawable.button_state));
        bt1.setText(root.getResources().getString(R.string.save));
        bt1.setTextColor(root.getResources().getColor(R.color.DarkGrayText));

        bt1.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if ( ((Button) ll3.findViewWithTag("bt2") == null)){
                       createNewForm(root);
                       //bt1.setVisibility(View.GONE);
                       ll3.addView(ll4,0);
                   }
                   Toast.makeText( root.getContext(), root.getResources().getString(R.string.exercise_add_info), Toast.LENGTH_SHORT).show();

               }
           }
        );

        ll2.addView(bt1);

        LinearLayout.LayoutParams lp9 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        ll3.setOrientation(LinearLayout.VERTICAL);
        ll3.setGravity(Gravity.CENTER_VERTICAL);
        ll3.setLayoutParams(lp9);

        ll3.addView(e1);
        ll3.addView(ll1);
        ll3.addView(e4);
        ll3.addView(t1);
        ll3.addView(sc1);
        ll3.addView(ll2);

        ((LinearLayout) root.findViewById(R.id.list)).addView(ll3,0);
    }

    public static int dpToPx(int dp, Context c) {
        float density = c.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }

}
