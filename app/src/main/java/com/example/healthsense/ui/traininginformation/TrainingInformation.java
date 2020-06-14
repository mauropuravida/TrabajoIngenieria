package com.example.healthsense.ui.traininginformation;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
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
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.example.healthsense.R;
import com.example.healthsense.ui.traininghistory.TrainingHistoryFragment;
import com.google.android.youtube.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TrainingInformation extends Fragment {

    public static Fragment fg;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_training_information, container, false);

        root.setBackgroundResource(R.color.Background);

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {

                getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fg).addToBackStack(null).commit();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        JSONObject jsonSteps = new JSONObject();
        jsonPut(jsonSteps, "0", "dsfdsdads");
        jsonPut(jsonSteps, "1", "dsfdsdads");
        jsonPut(jsonSteps, "2", "dsfdsdads");
        jsonPut(jsonSteps, "3", "dsfdsdads");
        jsonPut(jsonSteps, "4", "dsfdsdads");
        jsonPut(jsonSteps, "5", "dsfdsdads");


        JSONObject json = new JSONObject();

        jsonPut(json,"name", "Warm up");
        jsonPut(json,"time", "10 Minutes");
        jsonPut(json,"difficulty", "2");
        jsonPut(json,"URL", "Ks-lKvKQ8f4");
        jsonPut(json,"instructions", jsonSteps.toString());


        createNewExercise(root, json);

        JSONObject json2 = new JSONObject();

        jsonPut(json2,"name", "Test 2");
        jsonPut(json2,"time", "30 Minutes");
        jsonPut(json2,"difficulty", "5");
        jsonPut(json2,"URL", "Ks-lKvKQ8f4");
        jsonPut(json2,"instructions", jsonSteps.toString());


        createNewExercise(root, json2);

        JSONObject json3 = new JSONObject();

        jsonPut(json3,"name", "Test 3");
        jsonPut(json3,"time", "20 Minutes");
        jsonPut(json3,"difficulty", "4");
        jsonPut(json3,"URL", "Ks-lKvKQ8f4");
        jsonPut(json3,"instructions", jsonSteps.toString());

        createNewExercise(root, json3);

        return root;
    }

    private void jsonPut(JSONObject json, String key, Object value){
        try {
            json.put(key,value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Object jsonGet(JSONObject json, String key){
        Object value= "";
        try{
            value = json.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return value;
    }

    private void createNewExercise(View root, JSONObject json){

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
        bt2.setBackground(root.getResources().getDrawable(R.drawable.background_target_waiting_training));
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
        e1.setPadding(30,0,30,0);
        e1.setText((String)jsonGet(json,"name"));
        e1.setTextColor(getResources().getColor(R.color.DarkGrayText));
        e1.setBackgroundTintList(ContextCompat.getColorStateList(root.getContext(), R.color.DarkerButton));
        e1.setEnabled(false);
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
        e2.setText((String)jsonGet(json,"time"));
        e2.setTextColor(getResources().getColor(R.color.DarkGrayText));
        e2.setBackgroundTintList(ContextCompat.getColorStateList(root.getContext(), R.color.DarkerButton));
        e2.setPadding(30,0,30,0);
        e2.setEnabled(false);
        e2.setGravity(Gravity.CENTER_VERTICAL);
        e2.setLayoutParams(lp3);

        Space s1 = new Space(root.getContext());
        s1.setMinimumWidth(dpToPx(30,root.getContext()));

        LinearLayout.LayoutParams lp4 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(40,root.getContext()));
        lp4.weight = 1;

        ll1.addView(e2);
        ll1.addView(s1);

        String num = (String)jsonGet(json,"difficulty");
        for (int i=0; i< Integer.parseInt(num);i++) {
            TextView tv = new TextView(root.getContext());
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_star, 0, 0, 0);
            ll1.addView(tv);
        }

        Space s2 = new Space(root.getContext());
        s2.setMinimumWidth(dpToPx(30,root.getContext()));
        ll1.addView(s2);

        LinearLayout.LayoutParams lp5 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(70,root.getContext()));
        lp5.setMargins(0,dpToPx(10,root.getContext()),0,0);

        EditText e4 = new EditText(root.getContext());
        e4.setPadding(30,0,30,0);
        e4.setTextSize(dpToPx(24,root.getContext()));
        e4.setTextColor(getResources().getColor(R.color.DarkGrayText));
        e4.setText("00:00");
        e2.setBackgroundTintList(ContextCompat.getColorStateList(root.getContext(), R.color.DarkerButton));
        e4.setGravity(Gravity.CENTER);
        e4.setLayoutParams(lp5);

        YouTubePlayerView ypv = new YouTubePlayerView(root.getContext());

        ypv.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {

            @Override
            public void onReady(@NotNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {
                String videoId = (String)jsonGet(json,"URL");
                youTubePlayer.loadVideo(videoId, 0);
            }
        });


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
        sc1.setBackgroundColor(getResources().getColor(R.color.Background));


        EditText e5 = new EditText(root.getContext());
        e5.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        e5.setPadding(30,0,30,0);
        e5.setTextColor(getResources().getColor(R.color.DarkGrayText));
        e5.setBackgroundTintList(ContextCompat.getColorStateList(root.getContext(), R.color.Background));
        e5.setSingleLine(false);

        JSONObject jo = null;
        try {
            jo = new JSONObject( ((String)jsonGet(json,"instructions")) );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        e5.setText("Step 1: "+jsonGet(jo,"0"));
        for (int i=1 ; i<jo.length();i++){
            e5.append(Html.fromHtml("<br>").toString());
            e5.append("Step "+(i+1)+": "+(String)jsonGet(jo, i+""));
        }

        sc1.addView(e5);

        LinearLayout.LayoutParams lp8 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp8.setMargins(0,dpToPx(20,root.getContext()),0,0);

        LinearLayout ll2 = new LinearLayout(root.getContext());
        ll2.setOrientation(LinearLayout.HORIZONTAL);
        ll2.setGravity(Gravity.CENTER);
        ll2.setLayoutParams(lp8);

        Button bt1 = new Button(root.getContext());
        bt1.setBackground(root.getResources().getDrawable(R.drawable.background_model_training));
        bt1.setText(root.getResources().getString(R.string.start));
        bt1.setTextColor(root.getResources().getColor(R.color.DarkGrayText));

        bt1.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   /*if ( ((Button) ll3.findViewWithTag("bt2") == null)){
                       createNewForm(root);
                       //bt1.setVisibility(View.GONE);
                       ll3.addView(ll4,0);
                   }
                   Toast.makeText( root.getContext(), root.getResources().getString(R.string.exercise_add_info), Toast.LENGTH_SHORT).show();*/

               }
           }
        );

        Button bt3 = new Button(root.getContext());
        bt3.setBackground(root.getResources().getDrawable(R.drawable.background_target_waiting_training));
        bt3.setText(root.getResources().getString(R.string.stop));
        bt3.setTextColor(root.getResources().getColor(R.color.DarkGrayText));

        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if ( ((Button) ll3.findViewWithTag("bt2") == null)){
                createNewForm(root);
                //bt1.setVisibility(View.GONE);
                ll3.addView(ll4,0);
                }
                Toast.makeText( root.getContext(), root.getResources().getString(R.string.exercise_add_info), Toast.LENGTH_SHORT).show();*/
           }
        });

        Space s4= new Space(root.getContext());
        s4.setMinimumWidth(dpToPx(30,root.getContext()));

        Button bt4 = new Button(root.getContext());
        bt4.setBackground(root.getResources().getDrawable(R.drawable.background_model_training_history));
        bt4.setText(root.getResources().getString(R.string.end_up));
        bt4.setTextColor(root.getResources().getColor(R.color.White));

        bt4.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                /*if ( ((Button) ll3.findViewWithTag("bt2") == null)){
                createNewForm(root);
                //bt1.setVisibility(View.GONE);
                ll3.addView(ll4,0);
                }
                Toast.makeText( root.getContext(), root.getResources().getString(R.string.exercise_add_info), Toast.LENGTH_SHORT).show();*/
           }
        });

        Space s3 = new Space(root.getContext());
        s3.setMinimumWidth(dpToPx(10,root.getContext()));

        Space s5 = new Space(root.getContext());
        s5.setMinimumWidth(dpToPx(30,root.getContext()));
        Space s6 = new Space(root.getContext());
        s6.setMinimumWidth(dpToPx(10,root.getContext()));

        ll2.addView(s3);
        ll2.addView(bt1);
        ll2.addView(s4);
        ll2.addView(bt3);
        ll2.addView(s5);
        ll2.addView(bt4);
        ll2.addView(s6);


        LinearLayout.LayoutParams lp9 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        ll3.setOrientation(LinearLayout.VERTICAL);
        ll3.setGravity(Gravity.CENTER_VERTICAL);
        ll3.setLayoutParams(lp9);

        ll3.addView(e1);
        ll3.addView(ll1);
        ll3.addView(ypv);
        ll3.addView(t1);
        ll3.addView(sc1);
        ll3.addView(e4);
        ll3.addView(ll2);

        Space s7 = new Space(root.getContext());
        s7.setMinimumHeight(dpToPx(30,root.getContext()));

        ll3.addView(s7);

        ((LinearLayout) root.findViewById(R.id.list)).addView(ll3,0);
    }

    public static int dpToPx(int dp, Context c) {
        float density = c.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }


}
