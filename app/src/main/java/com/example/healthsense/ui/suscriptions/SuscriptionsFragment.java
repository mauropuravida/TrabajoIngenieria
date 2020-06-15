package com.example.healthsense.ui.suscriptions;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.Toast;

import com.example.healthsense.R;
import com.example.healthsense.data.PikerDate;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;

/**
 * A simple {@link Fragment} subclass.
 */
public class SuscriptionsFragment extends Fragment {

    public static Fragment fg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_suscriptions, container, false);

        JSONObject json = new JSONObject();

        jsonPut(json,"name", "Raul Rodriguez");
        jsonPut(json,"Gender", "Male");
        jsonPut(json,"Email", "rodriguezgds@gmail.com");
        jsonPut(json,"City", "Tadelisca");
        jsonPut(json,"Address", "Narrow street 47");
        jsonPut(json,"Speciality", "Cardiologist");
        jsonPut(json,"Action", "ACCEPTED ON:");

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

        newSuscription(root, json, list1, R.drawable.background_model_training_history, createButton(root,R.string.pay, null),View.VISIBLE);
        newSuscription(root, json, list1, R.drawable.background_model_training_history, createButton(root,R.string.pay, null),View.VISIBLE);
        newSuscription(root, json, list1, R.drawable.background_model_training_history, createButton(root,R.string.pay, null),View.VISIBLE);
        newSuscription(root, json, list1, R.drawable.background_model_training_history, createButton(root,R.string.pay, null),View.VISIBLE);

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

        JSONObject json2 = new JSONObject();

        jsonPut(json2,"name", "Raul Rodriguez");
        jsonPut(json2,"Gender", "Male");
        jsonPut(json2,"Email", "rodriguezgds@gmail.com");
        jsonPut(json2,"City", "Tadelisca");
        jsonPut(json2,"Address", "Narrow street 47");
        jsonPut(json2,"Speciality", "Cardiologist");
        jsonPut(json2,"Action", "REGISTERED IN:");

        newSuscription(root, json2, list2, R.drawable.background_target_waiting_training, createButton(root,R.string.suscribe,null),View.GONE);
        newSuscription(root, json2, list2, R.drawable.background_target_waiting_training, createButton(root,R.string.suscribe, null),View.GONE);
        newSuscription(root, json2, list2, R.drawable.background_target_waiting_training, createButton(root,R.string.suscribe, null),View.GONE);

        ((TextView)root.findViewById(R.id.count2)).setText("3");

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fg).addToBackStack(null).commit();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        return root;
    }

    private void newSuscription(View root, JSONObject json, LinearLayout list, int backgroundColor , Button bton, int viewVisibility){
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
                "<br> Gender: "+jsonGet(json,"Gender")+
                "<br> Speciality: "+jsonGet(json,"Speciality")+
                "<br> Email: "+jsonGet(json,"Email")+
                "<br> City: "+jsonGet(json,"City")+
                "<br> Address: "+jsonGet(json,"Address")+
                "<br><br> <b>"+jsonGet(json,"Action")+"</b>"+ PikerDate.Companion.toDateFormatV(created)));

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
        price.setClickable(false);
        price.setEnabled(false);
        price.setText("530");
        price.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        price.setTextColor(getResources().getColor(android.R.color.black));
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

    private Button createButton(View root, int text , Fragment fragment){
        Button bt = new Button(root.getContext());
        bt.setText(text);
        bt.setBackground(getResources().getDrawable(R.drawable.background_model_training));
        bt.setTextColor(getResources().getColor(android.R.color.white));
        bt.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                bt.setEnabled(false);
                  Toast.makeText(root.getContext(), "The operation was done" , Toast.LENGTH_SHORT).show();
              }
          }
        );
        return bt;
    }

    private void setPriceWorkout(){
        //TODO , hay que ver como se implementa, se puede mostrar un mensaje cuando se ejecuta
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

}
