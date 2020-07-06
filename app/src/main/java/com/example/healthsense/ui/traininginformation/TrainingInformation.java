package com.example.healthsense.ui.traininginformation;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.healthsense.MainActivity;
import com.example.healthsense.R;
import com.example.healthsense.Resquest.OkHttpRequest;
import com.example.healthsense.Resquest.doAsync;
import com.example.healthsense.data.PikerDate;
import com.example.healthsense.db.Repository.DeviceUsersRepository;
import com.example.healthsense.db.Repository.ExercisesRepository;
import com.example.healthsense.db.Repository.UserRepository;
import com.example.healthsense.db.Repository.WorkoutsExercisesRepository;
import com.example.healthsense.db.Repository.WorkoutsReportsRepository;
import com.example.healthsense.db.Repository.WorkoutsRepository;
import com.example.healthsense.db.entity.Exercises;
import com.example.healthsense.db.entity.WorkoutExercises;
import com.example.healthsense.db.entity.WorkoutReports;
import com.example.healthsense.db.entity.Workouts;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.jetbrains.annotations.NotNull;
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

/*
Clase que se encarga de manejar la imformación que posee la plantilla que contiene cada entrenamiento junto con su funcionalidad
 */
public class TrainingInformation extends Fragment {

    public static Fragment fg;
    private ProgressDialog mProgressDialog;
    private ArrayList<YouTubePlayerView> you = new ArrayList<>();
    private Workouts workout;
    private List<Exercises> exercisesList;
    private List<WorkoutExercises> workoutsExercises;
    private ExercisesRepository exercisesRepository;
    private WorkoutsRepository workoutsRepository;
    private WorkoutsExercisesRepository workoutsExercisesRepository;
    private WorkoutsReportsRepository workoutsReportsRepository;
    private int workout_id, work_id, works_saved, report_id;
    private View root;
    private boolean running = false;
    private long pauseOffset;
    private Map<String, Long> timers;
    private Map<String, Long> offsets;
    private Map<String, Boolean> first_time;
    private String fragment;


    int rating = 0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_training_information, container, false);

        root.setBackgroundResource(R.color.Background);
        exercisesList = new ArrayList<>();
        workoutsExercises = new ArrayList<>();
        workoutsRepository = new WorkoutsRepository(getActivity().getApplication());
        exercisesRepository = new ExercisesRepository(getActivity().getApplication());
        workoutsExercisesRepository = new WorkoutsExercisesRepository(getActivity().getApplication());
        workoutsReportsRepository = new WorkoutsReportsRepository(getActivity().getApplication());

        timers = new HashMap<>();
        first_time = new HashMap<>();
        offsets = new HashMap<>();
        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {

                getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fg).addToBackStack(null).commit();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        mProgressDialog = new ProgressDialog(root.getContext(), R.style.AppCompatAlertDialogStyle);
        mProgressDialog.setTitle(R.string.loading);
        mProgressDialog.setMessage(getResources().getString(R.string.please_wait));
        mProgressDialog.show();


        //Obtengo datos enviados desde otro fragment. M = MyTrainingsFragment - H = HistoryTrainingFragment. - S = SubscribersFragment
        Bundle datosRecuperados = getArguments();
        if (datosRecuperados != null) {
            fragment = datosRecuperados.getString("Fragment");
            if (fragment.equals("M") || fragment.equals("S")) {
                workout_id = datosRecuperados.getInt("Work_ID");
                work_id = datosRecuperados.getInt("Work_ID");
                doAsync.execute(new Runnable() {
                    @Override
                    public void run() {
                        loadInformation();
                    }
                });
            } else {
                workout_id = datosRecuperados.getInt("Work_ID");
                report_id = datosRecuperados.getInt("Report_ID");
                doAsync.execute(new Runnable() {
                    @Override
                    public void run() {
                        loadInformationHistory();
                    }
                });
            }
        }

        mProgressDialog.dismiss();

        return root;
    }

    /**
     * Agrego datos a un objeto Json.
     *
     * @param json
     * @param key
     * @param value
     */
    private void jsonPut(JSONObject json, String key, Object value) {
        try {
            json.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtengo datos de un objeto Json.
     *
     * @param json
     * @param key
     * @return
     */
    private Object jsonGet(JSONObject json, String key) {
        Object value = "";
        try {
            value = json.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * Creacion vista de entrenamientos/historicos.
     *
     * @param root
     * @param json
     * @param tag
     */

    /*
    Plantilla de datos para la creación de un ejercicio
     */
    private void createNewExercise(View root, JSONObject json, String tag) {

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
        bt2.setBackground(root.getResources().getDrawable(R.drawable.background_target_waiting_training));
        bt2.setText("X");
        bt2.setTextColor(root.getResources().getColor(R.color.DarkGrayText));
        bt2.setLayoutParams(lp11);

        bt2.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       Toast.makeText(root.getContext(), root.getResources().getString(R.string.exercise_remove_info), Toast.LENGTH_SHORT).show();
                                       ((LinearLayout) root.findViewById(R.id.list)).removeView(ll3);
                                   }
                               }
        );

        ll4.addView(s0);
        ll4.addView(bt2);

        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(40, root.getContext()));
        lp1.setMargins(0, dpToPx(0, root.getContext()), 0, 0);

        EditText e1 = new EditText(root.getContext());
        e1.setPadding(30, 0, 30, 0);
        e1.setText((String) jsonGet(json, "name")); // NOMBRE DEL EJERCICIO
        e1.setTextColor(getResources().getColor(R.color.DarkGrayText));
        e1.setEnabled(false);
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
        e2.setText((String) jsonGet(json, "time")); // DURACION DEL EJERCICIO
        e2.setTextColor(getResources().getColor(R.color.DarkGrayText));
        e2.setPadding(30, 0, 30, 10);
        e2.setBackgroundTintList(ContextCompat.getColorStateList(root.getContext(), R.color.DarkerButton));
        e2.setEnabled(false);
        e2.setGravity(Gravity.CENTER_VERTICAL);
        e2.setLayoutParams(lp3);

        Space s1 = new Space(root.getContext());
        s1.setMinimumWidth(dpToPx(30, root.getContext()));

        LinearLayout.LayoutParams lp4 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(40, root.getContext()));
        lp4.weight = 1;

        ll1.addView(e2);
        ll1.addView(s1);

        String num = (String) jsonGet(json, "difficulty"); // DIFICULTAD DEL EJERCICIO
        for (int i = 0; i < Integer.parseInt(num); i++) {
            TextView tv = new TextView(root.getContext());
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_star, 0, 0, 0);
            ll1.addView(tv);
        }

        Space s2 = new Space(root.getContext());
        s2.setMinimumWidth(dpToPx(30, root.getContext()));
        ll1.addView(s2);

        LinearLayout.LayoutParams lp5 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(70, root.getContext()));
        lp5.setMargins(0, dpToPx(10, root.getContext()), 0, 0);

        Chronometer chronometer = new Chronometer(root.getContext());
        YouTubePlayerView ypv = new YouTubePlayerView(root.getContext());

        if (fragment.equals("M") || fragment.equals("S")) {
            chronometer.setPadding(30, 0, 30, 0);
            chronometer.setTextSize(dpToPx(24, root.getContext()));
            chronometer.setTextColor(getResources().getColor(R.color.DarkGrayText));
            chronometer.setText("00:00");
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.setFormat("%s");
            chronometer.setFocusable(false);
            chronometer.setGravity(Gravity.CENTER);
            chronometer.setLayoutParams(lp5);


            ypv.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {

                @Override
                public void onReady(@NotNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {
                    String videoId = (String) jsonGet(json, "URL"); // PATH DEL EJERCICIO
                    youTubePlayer.cueVideo(videoId, 0);
                    //youTubePlayer.loadVideo(videoId, 0);
                }
            });

            you.add(ypv);

        }
        LinearLayout.LayoutParams lp6 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(40, root.getContext()));

        TextView t1 = new TextView(root.getContext());
        t1.setTextColor(root.getResources().getColor(R.color.DarkGrayText));
        t1.setText(root.getResources().getString(R.string.desciptions));
        t1.setGravity(Gravity.CENTER_VERTICAL);
        t1.setTextSize(18);
        t1.setLayoutParams(lp6);

        LinearLayout.LayoutParams lp7 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(50, root.getContext()));
        lp7.setMargins(0, dpToPx(10, root.getContext()), 0, 0);

        ScrollView sc1 = new ScrollView(root.getContext());
        sc1.setLayoutParams(lp7);
        sc1.setBackgroundColor(getResources().getColor(R.color.Background));


        EditText e5 = new EditText(root.getContext());
        e5.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        e5.setPadding(30, 0, 30, 0);
        e5.setTextColor(getResources().getColor(R.color.DarkGrayText));
        e5.setBackgroundTintList(ContextCompat.getColorStateList(root.getContext(), R.color.Background));
        e5.setSingleLine(false);
        e5.setEnabled(false);

        if (fragment.equals("H")) {
            jsonPut(json, "price", workout.getPrice() + " ");
            jsonPut(json, "creation_date", workout.getCreation_date());
            e5.setText("Price:  " + (String) jsonGet(json, "price"));
            e5.append(Html.fromHtml("<br>").toString());
            String date = PikerDate.Companion.toDateFormatView((String) jsonGet(json, "creation_date"));
            e5.append("Creation Date:  " + date);
            e5.append(Html.fromHtml("<br>").toString());
            e5.append("Rating:  " + (String) jsonGet(json, "rating"));
        } else {
            e5.setText(Html.fromHtml("<br>").toString());
            e5.append((String) jsonGet(json, "instructions"));
        }
        sc1.addView(e5);

        LinearLayout.LayoutParams lp8 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp8.setMargins(0, dpToPx(20, root.getContext()), 0, 0);

        LinearLayout ll2 = new LinearLayout(root.getContext());
        ll2.setOrientation(LinearLayout.HORIZONTAL);
        ll2.setGravity(Gravity.CENTER);
        ll2.setLayoutParams(lp8);
        if (fragment.equals("M")) {

            Button bt1 = new Button(root.getContext()); //Start
            Button bt3 = new Button(root.getContext()); //Stop
            Button bt4 = new Button(root.getContext()); //EndUp

            bt1.setTag(tag);
            bt3.setTag(tag);
            bt4.setTag(tag);
            chronometer.setTag(tag);
            timers.put(tag, new Long(0));
            offsets.put(tag, new Long(0));
            first_time.put(tag, true);

            bt3.setEnabled(false);
            bt4.setEnabled(false);

            bt1.setBackground(root.getResources().getDrawable(R.drawable.button_state));
            bt1.setText(root.getResources().getString(R.string.start));
            bt1.setTextColor(root.getResources().getColor(R.color.DarkGrayText));

            // Logica boton Comenzar entrenamiento (START)
            bt1.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           if (!trainingWithoutConnection()) {
                                               bt1.setEnabled(false);
                                               Toast.makeText(root.getContext(), root.getResources().getString(R.string.cant_train), Toast.LENGTH_LONG).show();
                                           } else {
                                               startChronometer(chronometer);
                                               bt1.setEnabled(false);
                                               bt3.setEnabled(true);
                                               bt4.setEnabled(false);
                                               //comenzar cuenta regresiva.
                                           }
                   /*if ( ((Button) ll3.findViewWithTag("bt2") == null)){
                       createNewForm(root);
                       //bt1.setVisibility(View.GONE);
                       ll3.addView(ll4,0);
                   }
                   Toast.makeText( root.getContext(), root.getResources().getString(R.string.exercise_add_info), Toast.LENGTH_SHORT).show();*/

                                       }
                                   }
            );

            bt3.setBackground(root.getResources().getDrawable(R.drawable.button_state_red));
            bt3.setText(root.getResources().getString(R.string.stop));
            bt3.setTextColor(root.getResources().getColor(R.color.DarkGrayText));

            // Logica boton Detener entrenamiento (STOP)
            bt3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //pausar cuenta regresiva, reactivar...
                    if (running)
                        stopChronometer(chronometer);
                    else
                        startChronometer(chronometer);
                    bt3.setEnabled(false);
                    bt1.setEnabled(true);
                    bt4.setEnabled(true);
                /*if ( ((Button) ll3.findViewWithTag("bt2") == null)){
                createNewForm(root);
                //bt1.setVisibility(View.GONE);
                ll3.addView(ll4,0);
                }
                Toast.makeText( root.getContext(), root.getResources().getString(R.string.exercise_add_info), Toast.LENGTH_SHORT).show();*/
                }
            });

            Space s4 = new Space(root.getContext());
            s4.setMinimumWidth(dpToPx(30, root.getContext()));

            bt4.setBackground(root.getResources().getDrawable(R.drawable.button_state_green));
            bt4.setText(root.getResources().getString(R.string.end_up));
            bt4.setTextColor(root.getResources().getColor(R.color.White));

            // Logica boton Finalizar entrenamiento (ENDUP)
            bt4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bt1.setEnabled(false);
                    bt4.setEnabled(false);
                    bt3.setEnabled(false);
                    addToDatabase();
                    ll3.setVisibility(View.GONE);
                /*if ( ((Button) ll3.findViewWithTag("bt2") == null)){
                createNewForm(root);
                //bt1.setVisibility(View.GONE);
                ll3.addView(ll4,0);
                }
                Toast.makeText( root.getContext(), root.getResources().getString(R.string.exercise_add_info), Toast.LENGTH_SHORT).show();*/
                }
            });

            Space s3 = new Space(root.getContext());
            s3.setMinimumWidth(dpToPx(10, root.getContext()));

            Space s5 = new Space(root.getContext());
            s5.setMinimumWidth(dpToPx(30, root.getContext()));
            Space s6 = new Space(root.getContext());
            s6.setMinimumWidth(dpToPx(10, root.getContext()));

            ll2.addView(s3);
            ll2.addView(bt1);
            ll2.addView(s4);
            ll2.addView(bt3);
            ll2.addView(s5);
            ll2.addView(bt4);
            ll2.addView(s6);
        }

        if (fragment.equals("S")){
            Button bt5 = new Button(root.getContext()); //EDIT

            bt5.setBackground(root.getResources().getDrawable(R.drawable.button_state));
            bt5.setText(root.getResources().getString(R.string.edit));
            bt5.setTextColor(root.getResources().getColor(R.color.DarkGrayText));

            Space s3 = new Space(root.getContext());
            s3.setMinimumWidth(dpToPx(10, root.getContext()));

            Space s4 = new Space(root.getContext());
            s4.setMinimumWidth(dpToPx(30, root.getContext()));

            bt5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            ll2.addView(s3);
            ll2.addView(bt5);
            ll2.addView(s4);

        }

        LinearLayout.LayoutParams lp9 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        ll3.setOrientation(LinearLayout.VERTICAL);
        ll3.setGravity(Gravity.CENTER_VERTICAL);
        ll3.setLayoutParams(lp9);

        ll3.addView(e1);
        EditText es = new EditText(root.getContext());
        es.setBackgroundTintList(ContextCompat.getColorStateList(root.getContext(), R.color.DarkerButton));
        es.setPadding(0,0,0,0);
        ll3.addView(es);
        ll3.addView(ll1);
        if (fragment.equals("M"))
            ll3.addView(ypv);
        ll3.addView(t1);
        ll3.addView(sc1);
        //ll3.addView(e4);
        if (fragment.equals("M"))
            ll3.addView(chronometer);
        ll3.addView(ll2);

        Space s7 = new Space(root.getContext());
        s7.setMinimumHeight(dpToPx(30, root.getContext()));

        ll3.addView(s7);

        ((LinearLayout) root.findViewById(R.id.list)).addView(ll3, 0);
    }

    /**
     * Renderizado videos.
     *
     * @param dp
     * @param c
     * @return
     */
    public static int dpToPx(int dp, Context c) {
        float density = c.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }

    @Override
    public void onPause() {
        super.onPause();
        for (int i = 0; i < you.size(); i++)
            you.get(i).release();
    }

    /**
     * Obtengo datos del workout y exercises a realizar del Serverless.
     * Evito llamar al backend nuevamente.
     */
    private void loadInformation() {
        workout_id = workoutsRepository.getWourkoutIdRoom(workout_id);
        System.out.println("WORK ID ROOM LOAD INFORMATION " + workout_id);
        workout = workoutsRepository.getWorkout(workout_id);
        workoutsExercises = workoutsExercisesRepository.getWorkouts(workout_id);
        for (int i = 0; i < workoutsExercises.size(); i++) {
            exercisesList.add(exercisesRepository.getExercises(workoutsExercises.get(i).getExercise_id()));
        }
        for (int i = 0; i < exercisesList.size(); i++) {
            System.out.println(exercisesList.get(i));
            JSONObject json = new JSONObject();
            jsonPut(json, "name", workout.getName());
            int exercise_id = exercisesList.get(i).getId();
            // String time = workoutsExercisesRepository.getTime(workout_id, exercise_id ); // obtener de workoutsExercises
            jsonPut(json, "time", "Video");
            jsonPut(json, "difficulty", workout.getDifficulty());
            jsonPut(json, "URL", exercisesList.get(i).getPath());
            // jsonPut(json, "URL", "Ks-lKvKQ8f4"); // todo CAMBIAR ACA POR PATH.
            jsonPut(json, "instructions", exercisesList.get(i).getDescription());
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    createNewExercise(root, json, "" + exercise_id);
                }
            });
        }
    }

    /**
     * Obtengo datos del reporte generado correspondiente a un workout_exercises del Serverless.
     * Evito llamar al backend nuevamente.
     */
    private void loadInformationHistory() {
        while (workout_id == 0)
            workout_id = workoutsRepository.getWourkoutIdRoom(workout_id);
        System.out.println("WORK ID ROOM LOAD INFORMATION " + workout_id);
        workout = workoutsRepository.getWorkout(workout_id);
        WorkoutReports report = workoutsReportsRepository.getWorkoutReports(report_id);
        JSONObject json = new JSONObject();
        jsonPut(json, "name", workout.getName() + ":  " + " Report. ");
        jsonPut(json, "time", report.getExecution_date());
        jsonPut(json, "difficulty", workout.getDifficulty());
        jsonPut(json, "price", workout.getPrice() + " ");
        jsonPut(json, "rating", workout.getRating());
        jsonPut(json, "creation_date", workout.getCreation_date());
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                createNewExercise(root, json, "");
            }
        });
    }

    /**
     * Chequeo si puedo realizar entrenamiento o no.
     * Si hay internet -> true
     * Si no hay internet -> Si la cantidad de trabajos guardados es < 5 -> True
     * Si no hay internet -> Si la cantidad de trabajos guardados es > 5 -> False
     *
     * @return
     */
    private boolean trainingWithoutConnection() {
        //Consulta con la base de datos local si puede realizar el training antes de que de play. Esto si es que no hay internet.
        //Agregue un campo en device_users que es upload -> entrenamientos por subir.
        if (internetConnection())
            return true;

        UserRepository userRepository = new UserRepository(getActivity().getApplication());
        int user_id = userRepository.getIdTask(MainActivity.email);
        while (user_id == 0)
            user_id = userRepository.getIdTask(MainActivity.email);

        DeviceUsersRepository deviceUsersRepository = new DeviceUsersRepository((getActivity().getApplication()));
        works_saved = deviceUsersRepository.getWorksSaved(user_id);

        return works_saved < 5;
    }

    /**
     * Check conexion a internet.
     *
     * @return conectado? SI - NO
     */
    private boolean internetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Agrego reporte de entrenamiento realizado a la base de datos local.
     * Si hay internet envio los reportes, si no aumento la cantidad de realizados por ese usuario.
     */
    private void addToDatabase() {
        //se agrega el training a la base local (para el serverless), al dar end up si no habia internet se aumentaria en uno la cantidad de entrenamientos almacenados.

        // El usuario debe indicar el puntaje del ejercicio realizado.
        showDialogRating();

        if (internetConnection()) {
            // Si hay conexión a Internet en este momento OkHttp
            System.out.println("HAY INTERNET!");
            doAsync.execute(new Runnable() {
                @Override
                public void run() {
                    sendReports();
                }
            });
        } else {
            System.out.println("NO HAY INTERNET!");
            doAsync.execute(new Runnable() {
                @Override
                public void run() {
                    updateUpload();
                }
            });

        }
    }

    /**
     * Aumento la cantidad de trabajos realizados por el user.
     */
    private void updateUpload() {
        //Actualizo en +1 los trabajos guardados sin conexion. Cuando vuelva la conexion pregunto por este valor y si es > 0 hago push.
        UserRepository userRepository = new UserRepository(getActivity().getApplication());
        int user_id = userRepository.getId(MainActivity.email);
        System.out.println("USER " + user_id);
        DeviceUsersRepository deviceUsersRepository = new DeviceUsersRepository((getActivity().getApplication()));
        deviceUsersRepository.increaseWorks(user_id);
    }

    /**
     * Almaceno reportes en el Serverless
     */
    private void addFinishTrainingNoInternet() {
        //Es esta funcion agrego los datos del entrenamiento finalizado.

        //Actualizo el workout como done.
        System.out.println("WORK ID " + workout_id + "  RATING " + rating);
        WorkoutsRepository workoutsRepository = new WorkoutsRepository(getActivity().getApplication());
        workoutsRepository.updateDone(1, workout_id, rating); // actualizo campo done en workouts
        System.out.println("Se actualizo?? ");

        //Agregar reportes.
        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        System.out.println(sdf.format(currentDate.getTime()));
        String execution_date = PikerDate.Companion.toDateFormat(sdf.format(currentDate.getTime()));
        WorkoutReports report_to_insert = new WorkoutReports(workout_id, execution_date);
        // agregue el campo sent para saber si existe el reporte en la api, se puede hacer una consulta para conocer la cantidad de no enviados
        // y asi conocer cuales mandar cuando ya se tiene internet
        report_to_insert.setSent(false);

        WorkoutsReportsRepository workoutsReportsRepository = new WorkoutsReportsRepository(getActivity().getApplication());
        workoutsReportsRepository.insert(report_to_insert);

    }

    /**
     * Conexion con backend y envio de reportes.
     */
    private void sendReports() {
        OkHttpRequest request = new OkHttpRequest(new OkHttpClient());
        String url = MainActivity.PATH + "workoutReport/";
        JSONObject js = new JSONObject();
        try {
            js.put("workout_id", work_id);
            Calendar currentDate = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String execution_date = PikerDate.Companion.toDateFormat(sdf.format(currentDate.getTime()));
            js.put("execution_date", execution_date);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.POST(url, js, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("FAILURE", "NO ES POSIBLE ENVIAR DATOS");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("SUCCES", "DATOS ENVIADOS");
            }
        });

    }

    /**
     * Conexion con backend y update del workout realizado (DONE-RATING).
     */
    private void updateWorkout() {
        //todo actualizar campo done en el workout id del backend y rating tambien.
        OkHttpRequest request = new OkHttpRequest(new OkHttpClient());
        String url = MainActivity.PATH + "workout/setDone/" + work_id;

        System.out.println(url);
        JSONObject json = new JSONObject();
        try {
            json.put("rating", rating);
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

        System.out.println("UPDATE PARA EL WORK " + work_id + "  RATING " + rating);
        request.PUT(url, jarr, json, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("FAILURE", "NO ES POSIBLE ENVIAR DATOS");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("SUCCES", "DATOS ENVIADOS");
            }
        });
    }

    /**
     * Creacion pop-up para setear rating del ejercicio finalizado.
     */
    public void showDialogRating() {
        AlertDialog.Builder popDialog = new AlertDialog.Builder(getContext(), R.style.AppCompatAlertDialogStyle);

        LinearLayout linearLayout = new LinearLayout(getContext());
        final RatingBar rating = new RatingBar(getContext());

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        rating.setLayoutParams(lp);
        rating.setNumStars(5);
        rating.setStepSize(0.1f);

        linearLayout.addView(rating);

        popDialog.setTitle(R.string.rate_training);

        popDialog.setView(linearLayout);

        popDialog.setCancelable(false);


        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rate, boolean fromUser) {
                if (fromUser)
                    ratingBar.setRating((int) Math.ceil(rate));
            }
        });

        // Button OK
        popDialog.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        setStars((int) Math.ceil(rating.getProgress()) / 10);
                        dialog.dismiss();
                        addFinishTrainingNoInternet();
                        doAsync.execute(new Runnable() {
                            @Override
                            public void run() {
                                updateWorkout();
                            }
                        });
                    }

                });

        popDialog.create();
        popDialog.show();

    }

    /**
     * Seteo rating del ejercicio finalizado.
     *
     * @param n = rating.
     */
    public void setStars(int n) {
        this.rating = n;
        Toast.makeText(root.getContext(), root.getResources().getString(R.string.exercise_finish), Toast.LENGTH_SHORT).show();
    }

    /**
     * Comienza/continua cuenta del cronometo.
     *
     * @param chronometer
     */
    private void startChronometer(Chronometer chronometer) {
        if (!running) {
            if (first_time.get(chronometer.getTag())) {
                chronometer.setBase(SystemClock.elapsedRealtime());
                first_time.replace((String) chronometer.getTag(), false);
            } else {
                chronometer.setBase(SystemClock.elapsedRealtime() - offsets.get(chronometer.getTag()));
            }

            chronometer.start();
            running = true;
        }
    }

    /**
     * Se detiene y guarda el estado del cronometro.
     *
     * @param chronometer
     */
    private void stopChronometer(Chronometer chronometer) {
        if (running) {
            chronometer.stop();
            // long base = SystemClock.elapsedRealtime() - timers.get(chronometer.getTag());
            // timers.replace((String) chronometer.getTag(), timers.get(chronometer.getTag()) + base);
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            offsets.replace((String) chronometer.getTag(), pauseOffset);
            running = false;
        }
    }

}
