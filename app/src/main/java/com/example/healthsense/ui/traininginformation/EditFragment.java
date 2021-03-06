package com.example.healthsense.ui.traininginformation;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
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
import androidx.fragment.app.Fragment;

import com.example.healthsense.MainActivity;
import com.example.healthsense.R;
import com.example.healthsense.Resquest.OkHttpRequest;
import com.example.healthsense.Resquest.doAsync;
import com.example.healthsense.db.Repository.ExercisesRepository;
import com.example.healthsense.db.Repository.WorkoutsRepository;
import com.example.healthsense.db.entity.Exercises;
import com.example.healthsense.db.entity.Workouts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;


public class EditFragment extends Fragment{
    public static Fragment fg;
    private View root;
    private Button done;
    private ProgressDialog mProgressDialog;
    private  static  String TAG = "EditFragment";
    private JSONObject json = new JSONObject();
    private ArrayList<Integer> calls = new ArrayList<>();
    private String workout_name;
    private  WorkoutsRepository workoutsRepository;
    private ExercisesRepository exercisesRepository;




    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_create_training, container, false);

        root.setBackgroundResource(R.color.Background);
        workoutsRepository = new WorkoutsRepository(getActivity().getApplication());
        exercisesRepository = new ExercisesRepository(getActivity().getApplication());
        fg = this;
        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {

                getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fg).addToBackStack(null).commit();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        done = root.findViewById(R.id.button_done);
        done.setEnabled(false);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //actualizar en base local y back
                showDialogDone();
            }
        });


        mProgressDialog = new ProgressDialog(root.getContext(), R.style.AppCompatAlertDialogStyle);
        mProgressDialog.setTitle(R.string.loading);
        mProgressDialog.setMessage(getResources().getString(R.string.please_wait));
        mProgressDialog.setCancelable(false);

        createNewForm(root);

        return root;
    }

    public static int dpToPx(int dp, Context c) {
        float density = c.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
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


        ll4.addView(s0);

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
        Bundle getInformation = getArguments();
        workout_name = getInformation.getString("name");
        e1.setText(workout_name);
        e3.setText(getInformation.getString("difficulty"));
        e4.setText(getInformation.getString("url"));
        e5.setText(getInformation.getString("instructions"));
        try {
            json.put("exercises_id",getInformation.getInt("exercises_id"));
            json.put("workout_id",getInformation.getInt("workout_id"));
            json.put("device_users_id",getInformation.getInt("device_user_id"));
            json.put("price",getInformation.getDouble("price"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Button bt1 = new Button(root.getContext());
        bt1.setBackground(root.getResources().getDrawable(R.drawable.button_state));
        bt1.setText(root.getResources().getString(R.string.save));
        bt1.setTextColor(root.getResources().getColor(R.color.White));

        bt1.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       if (((Button) ll3.findViewWithTag("bt2") == null)) {
                                           //bt1.setVisibility(View.GONE);

                                           String name = e1.getText().toString();
                                           //TODO si el tiempo no se ingresa con formato "00:00:00" se rompe todo; arreglar eso
                                           String time = e2.getText().toString();
                                           int difficulty = Integer.parseInt(e3.getText().toString());
                                           String url = e4.getText().toString();
                                           String description = e5.getText().toString();
                                           addExercise(name, time, difficulty, url, description);
                                       }
                                       Toast.makeText(root.getContext(), root.getResources().getString(R.string.exercise_add_info), Toast.LENGTH_SHORT).show();

                                   }
                               }
        );

        ll2.addView(bt1);
        ll2.addView(id);

        LinearLayout.LayoutParams lp9 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        ll3.addView(ll4, 0);

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
                        postData();
                        mProgressDialog.show();
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

    public void addExercise(String name, String time, int difficulty, String url, String description) {
        try {
            json.put("name", name);
            json.put("time", "00:00:00");
            json.put("difficulty", difficulty);
            json.put("path", url);
            json.put("description", description);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        done.setEnabled(true);
    }

    void postData(){
        doAsync.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    putExercises(json.getInt("exercises_id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        doAsync.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    putWorkout(json.getInt("workout_id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        doAsync.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("UPDATE WORKOUT!");
                updateWorkoutRoom();
            }
        });

        doAsync.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("UPDATE EXERCISE!");
                updateExerciseRoom();
            }
        });
    }

    /**
     * Metodo PUT del ejercicio ingresado.
     * Luego de enviar el primero se ejecuta al metodo que almacena al entrenamiento en el backend
     */
    public void putExercises(int exercise_id) {
        OkHttpRequest request = new OkHttpRequest(new OkHttpClient());
        String conexion_exercise = MainActivity.PATH + "exercise/" + exercise_id;
        int numCall = addCall();
            /*
            exercise:
            “description”:
            “path”:
            */

            JSONObject json_ex = new JSONObject();
            try {
                json_ex.put("description", json.getString("description"));
                json_ex.put("path", json.getString("path"));
                System.out.println("DESC " + json.getString("description") + " Path " + json.getString("path"));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            request.PUT(conexion_exercise, new JSONArray(), json_ex, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(TAG, "onFailure: exercise post");
                    e.printStackTrace();
                    finishCall(numCall);
                    mProgressDialog.dismiss();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                        Log.d(TAG, "onResponse: " + response.code());
                        finishCall(numCall);
                }
            });
    }

    /**
     * Envio del workout a la nube mediante metodo PUT
     * @param workout_id
     */
    public void putWorkout(int workout_id) {
        String conexion_workout = MainActivity.PATH + "workout/" + workout_id;
        OkHttpRequest request = new OkHttpRequest(new OkHttpClient());
        int numCall = addCall();

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
            json_wk.put("device_user_id", json.getInt("device_users_id"));
            json_wk.put("name", workout_name);
            json_wk.put("creation_date", date);
            json_wk.put("price",json.getDouble("price"));
            json_wk.put("difficulty", Integer.valueOf(json.getString("difficulty")));
            System.out.println("Device user " +  json.getInt("device_users_id") + " Workout id: " + workout_id + " Workout_Name: " + workout_name + " CREATION: "+
                    date + " Price " + json.getDouble("price") + " Difficulty " + Integer.valueOf(json.getString("difficulty")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        request.PUT(conexion_workout, token, json_wk, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: workout post");
                e.printStackTrace();
                finishCall(numCall);
                mProgressDialog.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "onResponse: workout posted" +  response.code());
                finishCall(numCall);
            }
        });

    }

    /**
     * Aumento la cantidad de llamadas al backend.
     */
    private synchronized int addCall() {
        calls.add(1);
        Log.d("CALLS", calls.size() + "");
        return calls.size() - 1;
    }

    /**
     * Disminuyo la cantidad de llamas al backend.
     */
    private void finishCall(int i) {
        calls.set(i, 0);
        for (int j = 0; j < calls.size(); j++) {
            System.out.println("CALS");
            if (calls.get(j) == 1)
                return;
        }
        mProgressDialog.dismiss();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fg).addToBackStack(null).commit();
            }
        });
    }

    private void updateWorkoutRoom(){
        int numCall = addCall();
        System.out.println("UPDATE WORKOUT");

        int workout_id = 0;
        int difficulty = 0;
        double price = 0.0;
        Workouts to_update = null;
        System.out.println("VOY AL TRY");
        try {
            workout_id = workoutsRepository.getWourkoutIdRoom(json.getInt("workout_id"));
            System.out.println("ROKOTUT " + workout_id);
            difficulty = Integer.valueOf(json.getString("difficulty"));
            price = json.getDouble("price");
            while (workout_id == 0) {
                System.out.println("WORKOUT ID " + workout_id);
                workout_id = workoutsRepository.getWourkoutIdRoom(json.getInt("workout_id"));
            }
            to_update = workoutsRepository.getWorkout(workout_id);
            System.out.println("ME DEVOLVIO EL WORK " + to_update.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("Workout ID " + workout_id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(Calendar.getInstance().getTime()).toString();
        to_update.setCreation_date(date);
        to_update.setDifficulty(difficulty);
        to_update.setName(workout_name);
        to_update.setPrice(price);
        System.out.println("VOY A ACTUALIZAR");
        workoutsRepository.update(to_update);
        System.out.println("ACTUALIZO CON EXITO ndeaa");
        finishCall(numCall);
    }

    private  void updateExerciseRoom(){
        int numCall = addCall();
        System.out.println("UPDATE EXERCISE");

        Exercises to_update = null;
        int exercises_id = 0;
        String description = " ";
        String path = " ";
        System.out.println("SI LOPEO");

        try {
            while (exercises_id == 0)
                exercises_id = exercisesRepository.getExercisesIdBackend(json.getInt("exercises_id"));
            description = json.getString("description");
            path = json.getString("path");
            System.out.println("No lupeo");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        to_update = exercisesRepository.getExercises(exercises_id);
        to_update.setDescription(description);
        to_update.setPath(path);
        exercisesRepository.update(to_update);
        System.out.println("ACTUALIZO CON EXITO exerc");

        finishCall(numCall);
    }

}
