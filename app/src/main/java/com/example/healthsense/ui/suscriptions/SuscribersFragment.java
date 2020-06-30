package com.example.healthsense.ui.suscriptions;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
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

import com.example.healthsense.MainActivity;
import com.example.healthsense.R;
import com.example.healthsense.Resquest.OkHttpRequest;
import com.example.healthsense.Resquest.doAsync;
import com.example.healthsense.data.PikerDate;
import com.example.healthsense.ui.login.LoginActivity;
import com.example.healthsense.ui.traininginformation.CreateTraining;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class SuscribersFragment extends Fragment {

    public static Fragment fg;
    private ProgressDialog mProgressDialog;
    private ArrayList<LinearLayout> listll;
    private View root;
    private ArrayList<String> valuesCity = new ArrayList<>();
    private ArrayList<Integer> calls = new ArrayList<>();
    private ArrayList<View> views = new ArrayList<>();
    private ArrayList<EditText> amountViews = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        listll = new ArrayList<>();
        root = inflater.inflate(R.layout.fragment_suscribers, container, false);


        mProgressDialog = new ProgressDialog(root.getContext(),R.style.AppCompatAlertDialogStyle);
        mProgressDialog.setTitle(R.string.loading);
        mProgressDialog.setMessage(getResources().getString(R.string.please_wait));
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        doAsync.execute(new Runnable() {
            @Override
            public void run() {
                getValues(valuesCity, "city/");
            }
        });



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

        //newSuscriber(root, json, list1, R.drawable.background_model_training_history, createButton(root,R.string.edit,new CreateTraining()),View.GONE);
        //newSuscriber(root, json, list1, R.drawable.background_model_training_history, createButton(root,R.string.edit,new CreateTraining()),View.GONE);
        //newSuscriber(root, json, list1, R.drawable.background_model_training_history, createButton(root,R.string.edit,new CreateTraining()),View.GONE);
        //newSuscriber(root, json, list1, R.drawable.background_model_training_history, createButton(root,R.string.edit,new CreateTraining()),View.GONE);

        ((TextView)root.findViewById(R.id.count1)).setText("0");

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

        //newSuscriber(root, json, list2, R.drawable.background_target_waiting_training, createButton(root,R.string.add_training,new CreateTraining()),View.GONE);
        //newSuscriber(root, json, list2, R.drawable.background_target_waiting_training, createButton(root,R.string.add_training,new CreateTraining()),View.GONE);
        //newSuscriber(root, json, list2, R.drawable.background_target_waiting_training, createButton(root,R.string.add_training,new CreateTraining()),View.GONE);

        ((TextView)root.findViewById(R.id.count2)).setText("0");

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
        fg = this;

        /*newSuscriber(root, json, list3, R.drawable.background_target_price, createButton(root,R.string.set_price, null),View.VISIBLE);
        newSuscriber(root, json, list3, R.drawable.background_target_price, createButton(root,R.string.set_price, null),View.VISIBLE);
        newSuscriber(root, json, list3, R.drawable.background_target_price, createButton(root,R.string.set_price, null),View.VISIBLE);
        newSuscriber(root, json, list3, R.drawable.background_target_price, createButton(root,R.string.set_price, null),View.VISIBLE);
        newSuscriber(root, json, list3, R.drawable.background_target_price, createButton(root,R.string.set_price, null),View.VISIBLE);
        newSuscriber(root, json, list3, R.drawable.background_target_price, createButton(root,R.string.set_price, null),View.VISIBLE);*/

        ((TextView)root.findViewById(R.id.count3)).setText("0");

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fg).addToBackStack(null).commit();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

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

    private void finishCall(int i){
        Log.d("TESTING ","CHECK CALLS");
        calls.set(i,0);
        for (int j = 0; j< calls.size();j++) {
            if (calls.get(j) == 1)
                return;
        }
        Log.d("TESTING ","FINISH CALLLS");
        //loadProfile();
        //mProgressDialog.dismiss();
        if (valuesCity.size()>0) {
            getSubscribers();
        }
    }

    private synchronized int addCall(){
        calls.add(1);
        //Log.d("CALLS",calls.size()+"");
        return calls.size()-1;
    }

    private void getValues(ArrayList arr, String path){
        OkHttpRequest request = new OkHttpRequest(new OkHttpClient());
        String conexion = MainActivity.PATH+path;

        int numCall = addCall();

        request.GET(conexion,new JSONArray(), new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                msjToast(e,4000);
                finishCall(numCall);
            }

            @Override
            public void onResponse(Call call, Response response) {

                try {
                    String responseData = response.body().string();
                    JSONArray jsonArray = new JSONArray(responseData);

                    for (int i=0;i< jsonArray.length(); i++) {
                        JSONObject json = new JSONObject(jsonArray.get(i).toString());
                        arr.add(json.getString("name"));
                    }

                } catch (IOException e) {
                    msjToast(e,response.code());
                } catch (JSONException e) {
                    msjToast(e,response.code());
                }finally {
                    finishCall(numCall);
                }
            }
        });
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
                "<br> Gender: "+jsonGet(json,"gender")+
                "<br> Weight: "+jsonGet(json,"Weight")+
                "<br> Height: "+jsonGet(json,"Height")+
                "<br> City: "+jsonGet(json,"city")+
                "<br> Address: "+jsonGet(json,"address")+
                "<br> Email: "+jsonGet(json,"email")+
                "<br> Patologies: "+jsonGet(json,"Patologies")+
                "<br><br> <b>EXPIRED: </b>"+ jsonGet(json,"subs")));

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
        price.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);

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

        views.add(s0);
        views.add(llSub);

        if (viewVisibility == View.VISIBLE)
            amountViews.add(price);
        //------fin de programación del formato
    }

    private Button createButton(View root, int text , Fragment fragment, String id, int pos){
        Button bt = new Button(root.getContext());
        bt.setText(text);
        bt.setBackground(getResources().getDrawable(R.drawable.background_model_training));
        bt.setTextColor(getResources().getColor(android.R.color.white));
        bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fragment == null){
                        setPriceWorkout(id, pos);
                    }else {
                        Intent intent = new Intent();
                        AppCompatActivity activity = (AppCompatActivity) root.getContext();
                        //intent.putExtra("complexObject", fg);
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment).addToBackStack(null).commit();
                    }
                }
            }
        );
        return bt;
    }

    private void getSubscribers(){
        OkHttpRequest request = new OkHttpRequest(new OkHttpClient());
        String conexion = MainActivity.PATH+"subscriptions/getSubscribed";

        //int numCall = addCall();

        JSONArray jarr = new JSONArray();
        try {
            JSONObject jobj = new JSONObject();
            jobj.put("x-access-token", MainActivity.TOKEN);
            jarr.put(jobj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        request.GET(conexion,jarr, new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                msjToast(e,4000);
                //finishCall(numCall);
            }

            @Override
            public void onResponse(Call call, Response response) {

                try {
                    String responseData = response.body().string();

                    if (response.code() == 200) {
                        JSONArray jsonArray = new JSONArray(responseData);

                        LinearLayout list2 = root.findViewById(R.id.list2);
                        LinearLayout list3 = root.findViewById(R.id.list3);

                        int cantSubs3 = 0;
                        int cantSubs2 = 0;

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject json = new JSONObject(jsonArray.get(i).toString());

                            int c = (json.getString("city_id").equals("null"))? 1: Integer.parseInt(json.getString("city_id"));
                            String add = (json.getString("address").equals("null"))? "": json.getString("address");
                            String g =  (json.getString("gender").equals("M") )? "Male": ((json.getString("gender").equals("F")) ? "Female" : "");
                            int pos = i;

                            JSONObject jsonSend = new JSONObject();
                            jsonPut(jsonSend, "name", json.get("name").toString());
                            jsonPut(jsonSend, "gender", g);
                            jsonPut(jsonSend, "DNI", json.get("document_number").toString());
                            jsonPut(jsonSend, "email", json.get("email").toString());
                            jsonPut(jsonSend, "city", valuesCity.get(c));
                            jsonPut(jsonSend, "address", add);
                            jsonPut(jsonSend,"Weight", json.get("weight").toString());
                            jsonPut(jsonSend,"Height", json.get("height").toString());
                            jsonPut(jsonSend,"Patologies", "None");


                            String expires = (json.getString("expires").equals("null"))? "--/--/----" : PikerDate.Companion.toDateFormatView(json.getString("expires"));

                            jsonPut(jsonSend,"subs",expires);


                            float price = Float.parseFloat(json.get("amount").toString());
                            /*textEmail = json.get("email").toString();
                            textName = json.get("name").toString();

                            medicalSubscriptions.add(json.getString("Medical_Personnel_id"));*/

                            if (price == 0) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            newSuscriber(root, jsonSend, list3, R.drawable.background_target_price, createButton(root,R.string.set_price, null, json.getString("Device_Users_id"), pos),View.VISIBLE);
                                        } catch (Exception e) {
                                            msjToast(e, response.code());
                                        }
                                    }
                                });
                                cantSubs3++;
                            }else{
                                if(!(json.getString("expires").equals("null"))){
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                newSuscriber(root, jsonSend, list2, R.drawable.background_target_waiting_training, createButton(root,R.string.add_training,new CreateTraining(), json.getString("Device_Users_id"), pos),View.GONE);
                                            } catch (Exception e) {
                                                msjToast(e, response.code());
                                            }
                                        }
                                    });
                                    cantSubs2++;
                                }
                            }
                        }

                        String cantSubsString3 = cantSubs3+"";
                        String cantSubsString2 = cantSubs2+"";
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    ((TextView)root.findViewById(R.id.count2)).setText(cantSubsString2);
                                    ((TextView)root.findViewById(R.id.count3)).setText(cantSubsString3);
                                }catch (Exception e){
                                    msjToast(e,response.code());
                                }
                            }
                        });

                        //getMedicalList();
                    }

                } catch (IOException e) {
                    msjToast(e,response.code());
                } catch (JSONException e) {
                    msjToast(e,response.code());
                }finally {
                    mProgressDialog.dismiss();
                    //finishCall(numCall);
                }
            }
        });
    }

    private void msjToast(Exception e, int code){
        mProgressDialog.dismiss();
        if (e != null)
            e.printStackTrace();
        Looper.prepare();
        Toast.makeText(root.getContext(), LoginActivity.error(root.getContext(),code), Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

    private void setPriceWorkout(String id, int p){

        String textedit = amountViews.get(p).getText().toString();

        Log.d("TESTING ",textedit);
        JSONObject js = new JSONObject();

        try {
            js.put("amount", Float.parseFloat(textedit));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray jarr = new JSONArray();
        try {
            JSONObject jobj = new JSONObject();
            jobj.put("x-access-token", MainActivity.TOKEN);
            jarr.put(jobj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkHttpRequest request = new OkHttpRequest(new OkHttpClient());
        String conexion = MainActivity.PATH +"subscriptions/approve/"+id;

        request.PUT(conexion, jarr, js, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                msjToast(e, 4000);
            }

            @Override
            public void onResponse(Call call, Response response) {
                msjToast(null,response.code());
            }
        });
    }

}
