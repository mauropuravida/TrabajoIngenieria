package com.example.healthsense.ui.traininginformation;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthsense.MainActivity;
import com.example.healthsense.R;
import com.example.healthsense.Resquest.OkHttpRequest;
import com.example.healthsense.data.PikerDate;
import com.example.healthsense.ui.suscriptions.SuscribersFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Inicialización de plantilla de creación de entrenamientos
 */
public class CreateTraining extends Fragment {

    private static final String TAG = "CreateTraining";
    Map<Integer, JSONObject> map_exercises = new HashMap<>();
    private int id_ex = -1;
    private List<JSONObject> list_wk_ex  = new ArrayList<>();
    private int device_id_user = -1;
    private String workout_name;
    private boolean sent = false;
    private Button done;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Bundle datosRecuperados = getArguments(); // Si es null quiere decir que se esta llamando desde HistoryFragment
        if (datosRecuperados != null)
            device_id_user = datosRecuperados.getInt("Device_Users_id");

        View root = inflater.inflate(R.layout.fragment_create_training, container, false);
        Fragment me = this;

        done = root.findViewById(R.id.button_done);
        done.setEnabled(false);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogDone();
            }
        });

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

    /**
     *Creacion de plantilla que se rellena con los datos que provienen del backend de cada entrenamiento
     */
    private void createNewForm(View root){

        TextView id = new TextView(root.getContext());
        id.setText("-1");
        id.setVisibility(View.GONE);

        LinearLayout ll3 = new LinearLayout(root.getContext());

        LinearLayout.LayoutParams lp10 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lp10.setMargins(0, dpToPx(20, root.getContext()), 0, 0);

        LinearLayout ll4 = new LinearLayout(root.getContext());
        ll4.setOrientation(LinearLayout.HORIZONTAL);
        ll4.setLayoutParams(lp10);

        LinearLayout.LayoutParams lp0 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp0.weight = 1;

        Space s0 = new Space(root.getContext());
        s0.setLayoutParams(lp0);

        LinearLayout.LayoutParams lp11 = new LinearLayout.LayoutParams(dpToPx(30, root.getContext()), dpToPx(30, root.getContext()));

        Button bt2 = new Button(root.getContext());
        bt2.setBackground(root.getResources().getDrawable(R.drawable.button_state_red));
        bt2.setText("X");
        bt2.setTextColor(root.getResources().getColor(R.color.DarkGrayText));
        bt2.setLayoutParams(lp11);
        bt2.setTag("bt2");

        bt2.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       Toast.makeText(root.getContext(), root.getResources().getString(R.string.exercise_remove_info), Toast.LENGTH_SHORT).show();
                                       ((LinearLayout) root.findViewById(R.id.list)).removeView(ll3);

                                       deleteExercise(Integer.parseInt(id.getText().toString()));
                                   }
                               }
        );

        ll4.addView(s0);
        ll4.addView(bt2);

        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(40, root.getContext()));
        lp1.setMargins(0, dpToPx(10, root.getContext()), 0, 0);

        EditText e1 = new EditText(root.getContext());
        e1.setBackground(root.getResources().getDrawable(R.drawable.background_white_form));
        e1.setPadding(30, 0, 30, 0);
        e1.setHint(root.getResources().getString(R.string.name));
        e1.setGravity(Gravity.CENTER_VERTICAL);
        e1.setLayoutParams(lp1);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lp2.setMargins(0, dpToPx(10, root.getContext()), 0, 0);

        LinearLayout ll1 = new LinearLayout(root.getContext());
        ll1.setOrientation(LinearLayout.HORIZONTAL);
        ll1.setLayoutParams(lp2);

        LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(40, root.getContext()));
        lp3.weight = 1;

        EditText e2 = new EditText(root.getContext());
        e2.setBackground(root.getResources().getDrawable(R.drawable.background_white_form));
        e2.setPadding(30, 0, 30, 0);
        e2.setHint(root.getResources().getString(R.string.time));
        e2.setGravity(Gravity.CENTER_VERTICAL);
        e2.setLayoutParams(lp3);
        e2.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_TIME);

        Space s1 = new Space(root.getContext());
        s1.setMinimumWidth(dpToPx(30, root.getContext()));

        LinearLayout.LayoutParams lp4 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(40, root.getContext()));
        lp4.weight = 1;

        EditText e3 = new EditText(root.getContext());
        e3.setBackground(root.getResources().getDrawable(R.drawable.background_white_form));
        e3.setPadding(30, 0, 30, 0);
        e3.setHint(root.getResources().getString(R.string.difficulty));
        e3.setGravity(Gravity.CENTER_VERTICAL);
        e3.setInputType(InputType.TYPE_CLASS_NUMBER);
        e3.setLayoutParams(lp4);

        ll1.addView(e2);
        ll1.addView(s1);
        ll1.addView(e3);

        LinearLayout.LayoutParams lp5 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(40, root.getContext()));
        lp5.setMargins(0, dpToPx(10, root.getContext()), 0, 0);

        EditText e4 = new EditText(root.getContext());
        e4.setBackground(root.getResources().getDrawable(R.drawable.background_white_form));
        e4.setPadding(30, 0, 30, 0);
        e4.setHint(root.getResources().getString(R.string.url));
        e4.setGravity(Gravity.CENTER_VERTICAL);
        e4.setLayoutParams(lp5);

        LinearLayout.LayoutParams lp6 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(40, root.getContext()));

        TextView t1 = new TextView(root.getContext());
        t1.setTextColor(root.getResources().getColor(R.color.DarkGrayText));
        t1.setText(root.getResources().getString(R.string.instructions));
        t1.setGravity(Gravity.CENTER_VERTICAL);
        t1.setTextSize(18);
        t1.setLayoutParams(lp6);

        LinearLayout.LayoutParams lp7 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(150, root.getContext()));
        lp7.setMargins(0, dpToPx(10, root.getContext()), 0, 0);

        ScrollView sc1 = new ScrollView(root.getContext());
        sc1.setLayoutParams(lp7);
        sc1.setBackground(root.getResources().getDrawable(R.drawable.background_white_form));


        EditText e5 = new EditText(root.getContext());
        e5.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        e5.setPadding(30, 0, 30, 0);
        e5.setBackground(root.getResources().getDrawable(R.drawable.background_white_form));

        sc1.addView(e5);

        LinearLayout.LayoutParams lp8 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp8.setMargins(0, dpToPx(20, root.getContext()), 0, 0);

        LinearLayout ll2 = new LinearLayout(root.getContext());
        ll2.setOrientation(LinearLayout.HORIZONTAL);
        ll2.setGravity(Gravity.CENTER_VERTICAL);
        ll2.setGravity(Gravity.CENTER);
        ll2.setLayoutParams(lp8);

        Button bt1 = new Button(root.getContext());
        bt1.setBackground(root.getResources().getDrawable(R.drawable.button_state));
        bt1.setText(root.getResources().getString(R.string.save));
        bt1.setTextColor(root.getResources().getColor(R.color.White));

        bt1.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       if (((Button) ll3.findViewWithTag("bt2") == null)) {
                                           createNewForm(root);
                                           //bt1.setVisibility(View.GONE);
                                           ll3.addView(ll4, 0);

                                           String name = e1.getText().toString();
                                           //TODO si el tiempo no se ingresa con formato "00:00:00" se rompe todo; arreglar eso
                                           String time = e2.getText().toString();
                                           int difficulty = Integer.parseInt(e3.getText().toString());
                                           String url = e4.getText().toString();
                                           String description = e5.getText().toString();
                                           id.setText(addExercise(name, time, difficulty, url, description));
                                           Log.d(TAG, "onClick: id: " + id.getText());
                                       }
                                       Toast.makeText(root.getContext(), root.getResources().getString(R.string.exercise_add_info), Toast.LENGTH_SHORT).show();

                                   }
                               }
        );

        ll2.addView(bt1);
        ll2.addView(id);

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

        ((LinearLayout) root.findViewById(R.id.list)).addView(ll3, 0);
    }

    public static int dpToPx(int dp, Context c) {
        float density = c.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }

    /**
     * Almacenamiento de un nuevo ejercicio
     * @param name
     * @param time
     * @param difficulty
     * @param url
     * @param description
     * @return
     */
    public String addExercise(String name, String time, int difficulty, String url, String description) {

        id_ex++;
        Log.d(TAG, "addexercise: id: " + id_ex);

        JSONObject json = new JSONObject();
        try {
            json.put("name", name);
            json.put("time", time);
            json.put("difficulty", difficulty);
            json.put("path", url);
            json.put("description", description);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        map_exercises.put(id_ex, json);
        done.setEnabled(true);

        return String.valueOf(id_ex);
    }

    /**
     * Borrado de un ejercicio ya cargado
     * @param id
     */
    public void deleteExercise(int id) {
        map_exercises.remove(id);
        Log.d(TAG, "deleteexercise: remove: " + id);
        if (map_exercises.isEmpty())
            done.setEnabled(false);
    }


    /**
     * Dialogo en el cual se nombra al entrenamiento. De dar OK se almacena el entrenamiento y todos
     * sus ejercicios en el backend
     */
    public void showDialogDone() {
        AlertDialog.Builder popDialog = new AlertDialog.Builder(getContext(), R.style.AppCompatAlertDialogStyle);

        LinearLayout linearLayout = new LinearLayout(getContext());
        EditText name = new EditText(getContext());
        name.setInputType(InputType.TYPE_CLASS_TEXT);
        name.setHint("Workout name");
        name.setTextColor(getResources().getColor(R.color.TextGray));
        name.setBackgroundColor(getResources().getColor(R.color.White));


        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        linearLayout.addView(name);

        popDialog.setTitle("Workout");
        popDialog.setView(linearLayout);
        popDialog.setCancelable(true);


        // Button OK
        popDialog.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        workout_name = name.getText().toString();
                        dialog.dismiss();
                        postExercises();
                        getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, SuscribersFragment.fg).addToBackStack(null).commit();
                        Toast.makeText(getContext(), getResources().getString(R.string.e200), Toast.LENGTH_SHORT).show();

                    }

                });

        popDialog.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        popDialog.create();
        popDialog.show();

    }

    /**
     * Metodo POST de cada ejercicios ingresado.
     * Luego de enviar el primero se ejecuta al metodo que almacena al entrenamiento en el backend
     */
    public void postExercises() {
        OkHttpRequest request = new OkHttpRequest(new OkHttpClient());
        String conexion_exercise = MainActivity.PATH + "exercise/";


        for (JSONObject json: map_exercises.values()) {
            /*
            exercise:
            “description”:
            “path”:
            */

            JSONObject json_ex = new JSONObject();
            try {
                json_ex.put("description", json.optString("description",""));
                json_ex.put("path", json.getString("path"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            /*
            Workout_exercise:
            “workout_id”:
            “exercise_id”:
            “time”:
            */

            JSONObject json_wk_ex = new JSONObject();
            try {
                json_wk_ex.put("time", "00:00:00");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            list_wk_ex.add(json_wk_ex);

            request.POST(conexion_exercise, json_ex, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(TAG, "onFailure: exercise post");
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d(TAG, "onResponse: exercise posted");
                    if (response.code() == 200) {
                        try {
                            String responseData = response.body().string();
                            JSONObject json = new JSONObject(responseData);
                            int id = json.getInt("insertId");
                            json_wk_ex.put("exercise_id", id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (!sent){
                            sendWorkout(request);
                            sent = true;
                        }
                    }
                    else
                        Log.d(TAG, "onResponse: " + response.code());

                }
            });
        }
    }

    /**
     * Envio del workout a la nube mediante metodo POST
     * Al obtener el id con el que fue almacenado se envian los ejercicios del workout
     * @param request
     */
    public void sendWorkout(OkHttpRequest request) {
        String conexion_workout = MainActivity.PATH + "workout/";

        /*
        Workout: “x-access-token” en el header
            “device_user_id”:
            “name”:
            “creation_date”:
            “difficulty”:
            “price":
         */

        JSONArray token = new JSONArray();

        JSONObject ob = new JSONObject();
        try {
            ob.put("x-access-token", MainActivity.TOKEN);
            token.put(ob);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject json_wk = new JSONObject();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(Calendar.getInstance().getTime()).toString();

        try {
            json_wk.put("device_user_id", device_id_user);
            json_wk.put("name", workout_name);
            json_wk.put("creation_date", date);
            json_wk.put("difficulty", getDifficulty());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        boolean first = true;


        request.POST(conexion_workout, token, json_wk, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: workout post");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "onResponse: workout posted");
                int id_workout = -1;
                if (response.code() == 200) {
                    try {
                        String responseData = response.body().string();
                        JSONObject json = new JSONObject(responseData);
                        id_workout = json.getInt("insertId");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    postWorkoutExercises(first, id_workout, request);
                }
                else
                    Log.d(TAG, "onResponse: " + response.code());
            }
        });
    }

    /**
     * Envio de workout_exercise mediante metodo POST
     * @param first = variable booleana para conocer si es la primera vez que se ejecuta el metodo
     * @param id = id del workout
     * @param request
     */
    private void postWorkoutExercises(boolean first, int id, OkHttpRequest request) {
        if (first && id != -1) {
            first = false;
            String conexion_workout_exercise = MainActivity.PATH + "workoutExercise/";
            for (JSONObject json : list_wk_ex) {
                try {
                    json.put("workout_id", id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                request.POST(conexion_workout_exercise, json, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d(TAG, "onFailure: workout exercise post");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.code() == 200)
                            Log.d(TAG, "onResponse: workout_exercise posted");
                        else
                            Log.d(TAG, "onResponse: " + response.code());
                    }
                });
            }
        }
    }


    /**
     * Obtener dificultad promedio del entrenamiento basado en el conjunto de ejercicios
     * @return
     */
    private int getDifficulty() {
        int sum = 0;
        for (JSONObject json : map_exercises.values()) {
            try {
                sum += json.getInt("difficulty");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return sum / map_exercises.size();
    }

}
