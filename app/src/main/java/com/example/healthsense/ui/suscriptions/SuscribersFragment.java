package com.example.healthsense.ui.suscriptions;


import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.example.healthsense.R;
import com.example.healthsense.data.PikerDate;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class SuscribersFragment extends Fragment {
    private ArrayList<LinearLayout> listll;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        listll = new ArrayList<>();
        View root = inflater.inflate(R.layout.fragment_suscribers, container, false);


        JSONObject json = new JSONObject();

        jsonPut(json,"name", "Juan Domingo");
        jsonPut(json,"DNI", "34564367");
        jsonPut(json,"Weight", "85 kg");
        jsonPut(json,"Height", "180 cm");
        jsonPut(json,"Patologies", "None");

        LinearLayout list1 = root.findViewById(R.id.list1);
        root.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if (list1.getVisibility() == View.GONE){
                       list1.setVisibility(View.VISIBLE);
                   }else{
                       list1.setVisibility(View.GONE);
                   }
               }
           }
        );

        newSuscriber(root, json, list1, R.drawable.background_model_training_history, createButton(root,R.string.edit),View.GONE);
        newSuscriber(root, json, list1, R.drawable.background_model_training_history, createButton(root,R.string.edit),View.GONE);
        newSuscriber(root, json, list1, R.drawable.background_model_training_history, createButton(root,R.string.edit),View.GONE);
        newSuscriber(root, json, list1, R.drawable.background_model_training_history, createButton(root,R.string.edit),View.GONE);

        ((TextView)root.findViewById(R.id.count1)).setText("4");

        LinearLayout list2 = root.findViewById(R.id.list2);
        root.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if (list2.getVisibility() == View.GONE){
                       list2.setVisibility(View.VISIBLE);
                   }else{
                       list2.setVisibility(View.GONE);
                   }
               }
           }
        );

        newSuscriber(root, json, list2, R.drawable.background_target_waiting_training, createButton(root,R.string.add_training),View.GONE);
        newSuscriber(root, json, list2, R.drawable.background_target_waiting_training, createButton(root,R.string.add_training),View.GONE);
        newSuscriber(root, json, list2, R.drawable.background_target_waiting_training, createButton(root,R.string.add_training),View.GONE);

        ((TextView)root.findViewById(R.id.count2)).setText("3");

        LinearLayout list3 = root.findViewById(R.id.list3);
        root.findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if (list3.getVisibility() == View.GONE){
                       list3.setVisibility(View.VISIBLE);
                   }else{
                       list3.setVisibility(View.GONE);
                   }
               }
           }
        );

        newSuscriber(root, json, list3, R.drawable.background_target_price, createButton(root,R.string.set_price),View.VISIBLE);
        newSuscriber(root, json, list3, R.drawable.background_target_price, createButton(root,R.string.set_price),View.VISIBLE);
        newSuscriber(root, json, list3, R.drawable.background_target_price, createButton(root,R.string.set_price),View.VISIBLE);
        newSuscriber(root, json, list3, R.drawable.background_target_price, createButton(root,R.string.set_price),View.VISIBLE);
        newSuscriber(root, json, list3, R.drawable.background_target_price, createButton(root,R.string.set_price),View.VISIBLE);
        newSuscriber(root, json, list3, R.drawable.background_target_price, createButton(root,R.string.set_price),View.VISIBLE);

        ((TextView)root.findViewById(R.id.count3)).setText("6");

        return root;
    }


    private void jsonPut(JSONObject json, String key, String value){
        try {
            json.put(key,value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String jsonGet(JSONObject json, String key){
        String value= "";
        try{
            value = json.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return value;
    }

    private void newSuscriber(View root, JSONObject json, LinearLayout list, int backgroundColor , Button bton, int viewVisibility){
        String created = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            created = LocalDate.now().toString();
        }
        //----programación de formato de la lista de trainings

        Space s0 = new Space(root.getContext());
        s0.setMinimumHeight(30);

        LinearLayout ll = new LinearLayout(root.getContext());
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setGravity(Gravity.CENTER_VERTICAL);

        TextView tv1 = new TextView(root.getContext());

        tv1.setText(Html.fromHtml("<b>"+jsonGet(json,"name")+"</b><br><br> DNI: "+
                jsonGet(json,"DNI")+
                "<br> Weight: "+jsonGet(json,"Weight")+
                "<br> Height: "+jsonGet(json,"Height")+
                "<br> Patologies: "+jsonGet(json,"Patologies")+
                "<br><br> <b>SUSCRIBED ON: </b>"+ PikerDate.Companion.toDateFormatV(created)));

        tv1.setTextColor(root.getResources().getColor(android.R.color.white));
        tv1.setPadding(30,30,0,30);

        ll.addView(tv1);

        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp1.weight = 1;
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp2.weight = 3f;

        //Linearlayout izquierdo
        LinearLayout llL = new LinearLayout(root.getContext());
        llL.setOrientation(LinearLayout.VERTICAL);
        llL.setGravity(Gravity.CENTER_VERTICAL);
        llL.setLayoutParams(lp1);

        llL.addView(ll);

        //Linear layout derecho
        EditText price = new EditText(root.getContext());
        price.setHintTextColor(getResources().getColor(R.color.White));
        price.setBackgroundTintList(ContextCompat.getColorStateList(root.getContext(), R.color.DarkerButton));

        price.setHint(R.string.price);
        price.setVisibility(viewVisibility);

        Space s1 = new Space(root.getContext());
        s1.setMinimumHeight(30);

        LinearLayout llR = new LinearLayout(root.getContext());
        llR.setOrientation(LinearLayout.VERTICAL);
        llR.setGravity(Gravity.CENTER_VERTICAL);
        llR.setLayoutParams(lp2);

        llR.addView(price);
        llR.addView(s1);
        llR.addView(bton);
        llR.setPadding(0,30,30,30);

        //LAYOUT HORIZONTAL PARA TEXTO Y BOTON
        LinearLayout llSub = new LinearLayout(root.getContext());
        llSub.setOrientation(LinearLayout.HORIZONTAL);
        llSub.setGravity(Gravity.CENTER_VERTICAL);
        llSub.setBackgroundResource(backgroundColor);

        llSub.addView(llL);
        llSub.addView(llR);

        list.addView(s0);
        list.addView(llSub);
        //------fin de programación del formato
    }

    private Button createButton(View root, int text){
        Button bt = new Button(root.getContext());
        bt.setText(text);
        bt.setBackground(getResources().getDrawable(R.drawable.background_model_training));
        bt.setTextColor(getResources().getColor(android.R.color.white));
        return bt;
    }

}
