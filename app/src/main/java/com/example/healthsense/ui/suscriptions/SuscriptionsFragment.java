package com.example.healthsense.ui.suscriptions;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Looper;
import android.text.Html;
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

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SuscriptionsFragment extends Fragment {

    public static Fragment fg;
    private ArrayList<Integer> calls = new ArrayList<>();
    private View root;
    private ArrayList<String> valuesSpeciality = new ArrayList<>();
    private ArrayList<String> valuesCity = new ArrayList<>();
    private ProgressDialog mProgressDialog;
    private Context cont;
    private ArrayList<View> views = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_suscriptions, container, false);
        cont = root.getContext();

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

        doAsync.execute(new Runnable() {
            @Override
            public void run() {
                getValues(valuesSpeciality, "medicalspeciality/");
            }
        });


        JSONObject json = new JSONObject();

        jsonPut(json,"name", "Raul Rodriguez");
        jsonPut(json,"Gender", "Male");
        jsonPut(json,"DNI", "");
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

        //newSuscription(root, json, list1, R.drawable.background_model_training_history, createButton(root,R.string.pay, null, "0"),View.VISIBLE);
        //newSuscription(root, json, list1, R.drawable.background_model_training_history, createButton(root,R.string.pay, null, "0"),View.VISIBLE);
        //newSuscription(root, json, list1, R.drawable.background_model_training_history, createButton(root,R.string.pay, null, "0"),View.VISIBLE);
        //newSuscription(root, json, list1, R.drawable.background_model_training_history, createButton(root,R.string.pay, null, "0"),View.VISIBLE);

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

        JSONObject json2 = new JSONObject();

        jsonPut(json2,"name", "Raul Rodriguez");
        jsonPut(json2,"Gender", "Male");
        jsonPut(json,"DNI", "");
        jsonPut(json2,"Email", "rodriguezgds@gmail.com");
        jsonPut(json2,"City", "Tadelisca");
        jsonPut(json2,"Address", "Narrow street 47");
        jsonPut(json2,"Speciality", "Cardiologist");
        jsonPut(json2,"Action", "REGISTERED IN:");

        //newSuscription(root, json2, list2, R.drawable.background_target_waiting_training, createButton(root,R.string.suscribe,null, "0"),View.GONE);
        //newSuscription(root, json2, list2, R.drawable.background_target_waiting_training, createButton(root,R.string.suscribe, null, "0"),View.GONE);
        //newSuscription(root, json2, list2, R.drawable.background_target_waiting_training, createButton(root,R.string.suscribe, null, "0"),View.GONE);

        ((TextView)root.findViewById(R.id.count2)).setText("0");

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                FragmentManager fragmentManager = requireFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, fg).addToBackStack(null).commit();
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

        views.add(s0);
        views.add(llSub);
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
                bt.setEnabled(false);

                if (text == R.string.suscribe) {
                    Log.d("TESTEO", pos+"");
                    suscribe(id);
                    //((LinearLayout)root.findViewById(R.id.list2)).removeViewAt(pos);
                    //((LinearLayout)root.findViewById(R.id.list2)).removeViewAt((pos+1));
                    ((LinearLayout)root.findViewById(R.id.list2)).removeView(views.get(2*pos));
                    ((LinearLayout)root.findViewById(R.id.list2)).removeView(views.get((2*pos)+1));

                    TextView count2 = root.findViewById(R.id.count2);
                    count2.setText((Integer.parseInt(count2.getText().toString())-1)+"");
                    //Log.d("TESTEO", ((LinearLayout)root.findViewById(R.id.list2)).get),
                }
                else
                    pay(id);

                Toast.makeText(root.getContext(), "The operation was done" , Toast.LENGTH_SHORT).show();
              }
          }
        );
        return bt;
    }

    private void finishCall(int i){
        calls.set(i,0);
        for (int j = 0; j< calls.size();j++) {
            if (calls.get(j) == 1)
                return;
        }
        //loadProfile();
        //mProgressDialog.dismiss();
        if (valuesCity.size()>0) {
            getMedicalList();
            getSubscriptions();
        }
    }

    private synchronized int addCall(){
        calls.add(1);
        //Log.d("CALLS",calls.size()+"");
        return calls.size()-1;
    }

    private void getMedicalList(){
        OkHttpRequest request = new OkHttpRequest(new OkHttpClient());
        String conexion = MainActivity.PATH+"medicallist/";

        //int numCall = addCall();
        request.GET(conexion,new JSONArray(), new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                msjToast(e,4000);
                //finishCall(numCall);
            }

            @Override
            public void onResponse(Call call, Response response) {

                Log.d("TESTTT", "CONSULTÓ MEDICOS");

                try {
                    String responseData = response.body().string();
                    JSONArray jsonArray = new JSONArray(responseData);

                    LinearLayout list2 = root.findViewById(R.id.list2);

                    for (int i=0;i< jsonArray.length(); i++) {

                        JSONObject json = new JSONObject(jsonArray.get(i).toString());

                        int c = (json.getString("city_id").equals("null"))? 1: Integer.parseInt(json.getString("city_id"));
                        int s = (json.getString("medical_speciality_id").equals("null"))? 1: Integer.parseInt(json.getString("medical_speciality_id"));
                        String add = (json.getString("address").equals("null"))? "": json.getString("address");
                        String g =  (json.getString("gender").equals("M") )? "Male": ((json.getString("gender").equals("F")) ? "Female" : "");
                        int pos = i;


                        JSONObject jsonSend = new JSONObject();
                        jsonPut(jsonSend, "name", json.getString("name"));
                        jsonPut(jsonSend, "Gender", g);
                        jsonPut(jsonSend, "DNI", json.getString("document_number"));
                        jsonPut(jsonSend, "Email", json.getString("email"));
                        jsonPut(jsonSend, "City", valuesCity.get(c));
                        jsonPut(jsonSend, "Address", add);
                        jsonPut(jsonSend, "Speciality", valuesSpeciality.get(s));
                        jsonPut(jsonSend, "Action", "REGISTERED IN:");

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {

                                    newSuscription(root, jsonSend, list2, R.drawable.background_target_waiting_training, createButton(root, R.string.suscribe, null, json.getString("medical_personnel_id"), pos), View.GONE);

                                }catch (Exception e){
                                    msjToast(e,response.code());
                                }
                            }
                        });
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ((TextView)root.findViewById(R.id.count2)).setText(jsonArray.length()+"");
                            }catch (Exception e){
                                msjToast(e,response.code());
                            }
                        }
                    });


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

    private void getSubscriptions(){
        OkHttpRequest request = new OkHttpRequest(new OkHttpClient());
        String conexion = MainActivity.PATH+"subscriptions/get";

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

                        LinearLayout list1 = root.findViewById(R.id.list1);

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject json = new JSONObject(jsonArray.get(i).toString());

                            JSONObject jsonSend = new JSONObject();
                            jsonPut(jsonSend, "name", "");
                            jsonPut(jsonSend, "Gender", "");
                            jsonPut(jsonSend, "DNI", "");
                            jsonPut(jsonSend, "Email", "");
                            jsonPut(jsonSend, "City", "");
                            jsonPut(jsonSend, "Address", "");
                            jsonPut(jsonSend, "Speciality", "");
                            jsonPut(jsonSend, "Action", "REGISTERED IN:");

                            newSuscription(root, jsonSend, list1, R.drawable.background_model_training_history, createButton(root, R.string.pay, null, json.getString("medical_personnel_id"), i), View.VISIBLE);
                        }
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

    private void suscribe(String id){
        OkHttpRequest request = new OkHttpRequest(new OkHttpClient());


        Log.d("testing ", id);
        Log.d("TOKEN ", MainActivity.TOKEN);
        JSONObject js = new JSONObject();
        try {
            js.put("medical_personnel_id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JSONArray header = new JSONArray();
        JSONObject jsAux = new JSONObject();
        try {
            jsAux.put("x-access-token", MainActivity.TOKEN);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        String conexion = MainActivity.PATH+"subscriptions/subscribe";


        request.POST(conexion, header, js, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                msjToast(e,4000);
            }

            @Override
            public void onResponse(Call call, Response response) {
                msjToast(null,response.code());

            }
        });
    }

    private void msjToast(Exception e, int code){
        mProgressDialog.dismiss();
        if (e != null)
            e.printStackTrace();
        Looper.prepare();
        Toast.makeText(root.getContext(), LoginActivity.error(cont,code), Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

    private void pay(String id){

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
