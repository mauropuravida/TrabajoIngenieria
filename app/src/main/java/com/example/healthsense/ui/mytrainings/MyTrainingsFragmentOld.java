package com.example.healthsense.ui.mytrainings;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.fragment.app.Fragment;

import com.example.healthsense.MainActivity;
import com.example.healthsense.R;
import com.example.healthsense.Resquest.OkHttpRequest;
import com.example.healthsense.Resquest.doAsync;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

import static android.os.AsyncTask.execute;
import static java.util.concurrent.TimeUnit.*;

//ESTO NO SE HACE MAS.
// Obtener lista Device_User  /deviceUser/:fk&:0  -> fk es el user_id  ES UN USUARIO
// Obtener lista Medical_Personnel /medicalPersonnel/:fk&:0 -> fk es el user_id ES UN MEDICO

//AHORA
// Obtener lista Workouts  /workout/:fk&:0 -> fk es el device_user_id o medical_personnel_id USAR UNA DE LAS 2 OPCIONES DE ARRIBA (POR AHORA USUARIO)
// Obtener lista Workouts_exercises /workoutExercise/:workout_id&:id ->id es el fk de work_out
// (cada uno de los ids de la lista aterior) lista con todos los workouts_exercises
// Obtener lista Exercises /exercise/:id -> id es cada uno de los ids de Workouts_exercises.
//ESTA VERSION ASQUEROSAMENTE COMO ESTA FUNCIONA.
public class MyTrainingsFragmentOld extends Fragment{
    private  ArrayList<LinearLayout> listll;
    private ArrayList<JSONObject> workouts, exercises;
    private HashMap<Integer,ArrayList<Integer>> workoutExercises;//La base de datos permite que un workoutid tenga varios exercises.
    private final String URL_BASE = "https://healthsenseapi.herokuapp.com/";
    private JSONArray token;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_trainings, container, false);
        listll = new ArrayList<>();
        workouts = new ArrayList<>();
        exercises = new ArrayList<>();
        workoutExercises = new HashMap<>();

        View.OnClickListener mListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),
                        "Click ListItem Number " + v.getTag() , Toast.LENGTH_LONG)
                        .show();
                // nuevo intent con la info del layout seleccionado.
            }
        };

        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            // Si hay conexión a Internet en este momento OkHttp
            // getExercises();
            doAsync.execute(new Runnable() {
                @Override
                public void run() {
                   /*String path = "exercise/"; // obtengo por cada par work_id|exercice_id, la info del exercise
                    // Podria optimizarse obteniendo todos los exercices para realizar muchos menos llamados.
                    // Pero si la cantidad de exercises es muy grande no conviene.
                    String finalPath = path;
                    getBackendResponse(finalPath,1,exercises,null);
                    System.out.println("TAMAÑO DE EXERCISE : " +exercises.size());*/
                    getExercises(root,mListener);
                }
            });
            System.out.println("TAMAÑO DE EXERCISE : " +exercises.size());
        } else {
            // No hay conexión a Internet en este momento Room
        }

        return root;
    }

    private void createExercises(View root, View.OnClickListener mListener){
        //Logica para obtener datos necesarios
        System.out.println("TIENE DATOS EL WORKOUT? " + workouts.size());
        for ( int i = 0; i<workouts.size() ; i++) { // desde i hasta cant workouts
            newTraining(root,i, mListener); // ver que datos enviar
        }
    }

    private void getExercisesWorkout(View root, View.OnClickListener mListener){
        String path = "workoutExercise/workout_id&"; // obtengo por cada workout_id el workout_exercice asociado
        token = new JSONArray();
        int i = 0;
        for (JSONObject o : workouts){
            try {
                i++;
                getBackendResponse(path,o.getInt("id"),null,workoutExercises,root,mListener,i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void getExercisesfinal(View root, View.OnClickListener mListener, HashMap<Integer, ArrayList<Integer>> hashObj, int cant){
        String path = "exercise/"; // obtengo por cada par work_id|exercice_id, la info del exercise
        // Podria optimizarse obteniendo todos los exercices para realizar muchos menos llamados.
        // Pero si la cantidad de exercises es muy grande no conviene.
        String finalPath = path;
        Iterator it = hashObj.entrySet().iterator();
        System.out.println( "TENGO VALOR + " + it.hasNext());
        while (it.hasNext()){
            System.out.println( "TENGO VALOR + " + it.hasNext());
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println( "TENGO VALOR + " + it.hasNext() + "La Clave es " + pair.getKey());
            ArrayList<Integer> values = (ArrayList<Integer>) pair.getValue();
            System.out.println( "TENGO VALUES + " + values.size());
            for(int i = 0; i< values.size();i++){
                if (!it.hasNext() && (i == values.size()-1) && cant == workouts.size()) {
                    System.out.println("DEBERIA ENTRAR UNA SOLA VEZ");
                    getLastResponse(finalPath, values.get(i).intValue(), exercises, null, root, mListener);
                }
                else
                    getBackendResponse(finalPath,values.get(i).intValue(),exercises,null,root,mListener,0);
            }
        }
    }

    private void getExercises(View root, View.OnClickListener mListener){
        String path = "workout/device_user_id&"; // obtengo workouts del user_id.
        JSONObject ob = new JSONObject();
        try {
            ob.put("x-access-token","eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6InBydWViYTNAZ21haWwuY29tIiwiaWF0IjoxNTkxOTcyODMzfQ.PARzs0fB4Iz2l2H5RTWoRdrPBCGZR6dcB-y2YoC77XE");
            token = new JSONArray();
            token.put(ob);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getBackendResponse(path, 0,workouts,null,root,mListener,0);
   /*     workoutExercises.forEach((k, v) -> {
            for(Integer values: v)
                getBackendResponse(finalPath,values.intValue(),exercises,null);
        });*/

        System.out.println("TAMAÑO DE EXERCISE GET : " +exercises.size());
        System.out.println("TAMAÑO DE WORKOUTS : " + workouts.size());
        System.out.println("TAMAÑO DE WORKOUTSEXERCISE : " + workoutExercises.size());
    }


    private void newTraining(View root, int i, View.OnClickListener mListener){
        //----programación de formato de la lista de trainings

        Space s0 = new Space(root.getContext());
        s0.setMinimumHeight(30);

        LinearLayout ll = new LinearLayout(root.getContext());
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setGravity(Gravity.CENTER_VERTICAL);

        TextView tv1 = new TextView(root.getContext());
        tv1.setText(Html.fromHtml("<b>Christian Bale Training</b><br><br> " +
                "Squats, Calisthenics, Weights and Cardio. Cardio: Complete 30-45 minutes of cardio from the selection below: High Incline Walk."));
        //tv1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_star, 0, 0, 30);
        tv1.setTextColor(root.getResources().getColor(R.color.GrayText));
        tv1.setPadding(30,30,30,30);

        ll.addView(tv1);

        LinearLayout ll1 = new LinearLayout(root.getContext());
        ll1.setOrientation(LinearLayout.HORIZONTAL);
        ll1.setGravity(Gravity.CENTER_VERTICAL);

        TextView tv3 = new TextView(root.getContext());
        tv3.setText(Html.fromHtml("<b>CREATED:</b> 10/1/2025&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>DIFFICULTY:</b> "));
        tv3.setTextColor(root.getResources().getColor(R.color.GrayText));
        tv3.setPadding(30,30,30,30);

        TextView tv4 = new TextView(root.getContext());
        tv4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_star, 0, 0, 0);
        tv4.setPadding(0,20,0,20);

        TextView tv5 = new TextView(root.getContext());
        tv5.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_star, 0, 0, 0);
        tv5.setPadding(0,20,0,20);

        ll1.addView(tv3);
        ll1.addView(tv4);
        ll1.addView(tv5);


        LinearLayout ll3 = new LinearLayout(root.getContext());
        ll3.setOrientation(LinearLayout.VERTICAL);
        ll3.setGravity(Gravity.CENTER_VERTICAL);

        ll3.setBackgroundResource(R.drawable.background_model_training);

        ll3.addView(ll);
        ll3.addView(ll1);

        LinearLayout lista = root.findViewById(R.id.list_trainings);

        lista.addView(s0);
        lista.addView(ll3);
        // set an id and listener for each layout.
        listll.add(ll3);
        listll.get(listll.size() -1).setTag(i);
        listll.get(listll.size() -1).setOnClickListener(mListener);
        //------fin de programación del formato
    }

    private Call getBackendResponse(String path, int id, ArrayList<JSONObject> listObj, HashMap<Integer,ArrayList<Integer>> hashObj, View root, View.OnClickListener mListener,int cant){
        OkHttpRequest request = new OkHttpRequest(new OkHttpClient());
        String url = URL_BASE + path + id;

        return request.GET(url, token ,new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getActivity(),
                        "Failed to obtain information from " + url , Toast.LENGTH_LONG)
                        .show();            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = null;
                try {
                    ArrayList<Integer> values = new ArrayList<>();
                    responseData = response.body().string();
                    Object jsonType = new JSONTokener(responseData).nextValue();
                    //    System.out.println("ENTRO AL RESPONSE");
                    if (jsonType instanceof JSONObject){
                        JSONObject json = new JSONObject((responseData));
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                exercises.add(json);
                            }
                        });
                        //     System.out.println("ENTRO AL JSONOBJECT");

                    } else if (jsonType instanceof JSONArray){
                        //    System.out.println("ENTRO AL JSONARRAY");
                        JSONArray json = new JSONArray(responseData);
                        for (int i = 0; i < json.length(); i++) {
                            if (hashObj == null) {
                                System.out.println("El WorkId es: " + json.getJSONObject(i).getInt("id"));
                                listObj.add(json.getJSONObject(i));
                            }
                            else {
                                System.out.println("El Exercise_id es: " + json.getJSONObject(i).getInt("exercise_id"));
                                values.add(json.getJSONObject(i).getInt("exercise_id"));
                            }
                        }
                        if (hashObj != null) {
                            hashObj.put(id, values);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    doAsync.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            getExercisesfinal(root,mListener,new HashMap<>(hashObj),cant);
                                        }
                                    });
                                }
                            });
                        }
                        else
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    workouts = listObj;
                                    doAsync.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            getExercisesWorkout(root,mListener);
                                        }
                                    });
                                }
                            });
                    }
                    if(workouts != null){
                        //      System.out.println("VALOR WORKOUT RESPONSE " + workouts.size());
                        //        System.out.println("TAMAÑO DE EXERCISE RESPONSE : " +exercises.size());
                        //       System.out.println("TAMAÑO DE WORKOUTSEXERCISE RESPONSE : " + workoutExercises.size());
                    }

                } catch (IOException | JSONException e) {
                    System.out.println("FALLA EL RESPONSE ");
                    e.printStackTrace();
                }
            }
        });
    }

    private Call getLastResponse(String path, int id, ArrayList<JSONObject> listObj, HashMap<Integer,ArrayList<Integer>> hashObj, View root, View.OnClickListener mListener){
        OkHttpRequest request = new OkHttpRequest(new OkHttpClient());
        String url = URL_BASE + path + id;
        System.out.println("ENTRO AL ULTIMO RESPONSE");
        return request.GET(url, token ,new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getActivity(),
                        "Failed to obtain information from " + url , Toast.LENGTH_LONG)
                        .show();            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = null;
                try {
                    //  System.out.println("ENTROENRTO ENTRO ENTRO ENTRO");
                    ArrayList<Integer> values = new ArrayList<>();
                    responseData = response.body().string();
                    Object jsonType = new JSONTokener(responseData).nextValue();
                    //    System.out.println("ENTRO AL RESPONSE");
                    if (jsonType instanceof JSONObject){
                        JSONObject json = new JSONObject((responseData));
                        exercises.add(json);
                    } else if (jsonType instanceof JSONArray){
                        JSONArray json = new JSONArray(responseData);
                        for (int i = 0; i < json.length(); i++) {
                            if (hashObj == null) {
                                listObj.add(json.getJSONObject(i));
                            }
                            else {
                                values.add(json.getJSONObject(i).getInt("exercise_id"));
                            }
                        }
                        if (hashObj != null)
                            hashObj.put(id, values);
                    }
                    if(workouts != null) {
                        System.out.println("VALOR WORKOUT RESPONSE " + workouts.size());
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                createExercises(root, mListener);
                            }
                        });
                    }
                } catch (IOException | JSONException e) {
                    System.out.println("FALLA EL RESPONSE ");
                    e.printStackTrace();
                }
            }
        });
    }


/*
    private class innerClass extends AsyncTask<Void, Void,ArrayList<JSONObject>> {

        View root;
        View.OnClickListener mListener;
        public innerClass(View root, View.OnClickListener mListener) {
            this.root = root;
            this.mListener = mListener;

        }

        @Override
        protected ArrayList<JSONObject> doInBackground(Void... voids) {
            getExercises(root,mListener);
            ArrayList<JSONObject> workOuts = new ArrayList<>(workouts);
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<JSONObject> workOuts) {
            super.onPostExecute(workOuts);
            createExercises(root,mListener);
            if (workOuts != null)
            Log.d("TTRABAJOS WORKOUTS", "onPostExecute: workouts: " + workOuts.size() );
        }
    }
*/

    // IMPLEMENTAR LOGICA EN OBJETOS.



/*

    private Call obtainUserId(){
        OkHttpRequest request = new OkHttpRequest(new OkHttpClient());
        String url = URL_BASE + "deviceUser/" + "user_id&0"  ;

        return request.GET(url, new JSONArray(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getActivity(),
                        "Failed to obtain user_id " , Toast.LENGTH_LONG)
                        .show();            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = null;
                try {
                    responseData = response.body().string();
                    JSONObject json = new JSONObject(responseData);
                    int device_id = json.getInt("id");
                    doAsync.execute(
                            new Runnable() {
                                @Override
                                public void run() {
                                    obtainWorkOuts(device_id);
                                }
                            }
                    );
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private Call obtainWorkOuts(int id){
        OkHttpRequest request = new OkHttpRequest(new OkHttpClient());
        String url = URL_BASE + "workout/" + "device_user_id&" + id;
        return  request.GET(url,new JSONArray(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getActivity(),
                        "Failed to obtain workouts " , Toast.LENGTH_LONG)
                        .show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = null;
                try {
                    responseData = response.body().string();
                    JSONArray json = new JSONArray(responseData); // Ojo que es una lista de workouts JSONArray
                    for (int i = 0; i < json.length(); i++) {
                        workouts.add(json.getJSONObject(i));
                    }
                    // Guardar lista workouts para tratar y manipular como objetos
                    //for con cada uno de los workouts_id llamando a obtainWorkoutExercises.
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Call obtainWorkoutsExercises(int id){
        OkHttpRequest request = new OkHttpRequest(new OkHttpClient());
        String url = URL_BASE + "workoutExercise/" + "workout_id&" + id;
        return  request.GET(url,new JSONArray(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getActivity(),
                        "Failed to obtain workoutsExercises " , Toast.LENGTH_LONG)
                        .show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = null;
                try {
                    responseData = response.body().string();
                    JSONObject json = new JSONObject(responseData); // Obtengo el WorkoutExercises correspondiente.
                    workoutExercises.put(id,json.getInt("exercise_id"));
                    // Guardar excersise con el workout al que pertenece
                    // pedir informacion del excersise ese.
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Call obtainExcersise(int id){
        OkHttpRequest request = new OkHttpRequest(new OkHttpClient());
        String url = URL_BASE + "exercise/" + id;
        return request.GET(url,new JSONArray(), new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getActivity(),
                        "Failed to obtain Exercise " , Toast.LENGTH_LONG)
                        .show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = null;
                try {
                    responseData = response.body().string();
                    JSONObject json = new JSONObject(responseData); // Obtengo el exercises correspondiente.
                    exercises.add(json);
                    // Guardar informacion del excersise.
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
*/
}