package com.example.healthsense.ui.traininginformation;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Html;
import android.text.InputType;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.healthsense.MainActivity;
import com.example.healthsense.R;
import com.example.healthsense.Resquest.doAsync;
import com.example.healthsense.data.PikerDate;
import com.example.healthsense.db.AppDatabase;
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
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import static com.example.healthsense.MainActivity.PREFS_FILENAME;
import static com.example.healthsense.MainActivity.user;

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
    private int workout_id;
    private View root;
    private boolean running = false;
    private long pauseOffset;
    private Map<String,Long> timers;
    private Map<String,Long> offsets;
    private Map<String,Boolean> first_time;


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


        Bundle datosRecuperados = getArguments(); // Si es null quiere decir que se esta llamando desde HistoryFragment
        if ( datosRecuperados != null){          // Por lo que no interesa que entre aca.
            workout_id = datosRecuperados.getInt("Work_ID");
            System.out.println("WORK ID LOAD INFORMATION " + workout_id);
            doAsync.execute(new Runnable() {
                @Override
                public void run() {
                    loadInformation();
                }
            });

        }


        JSONObject jsonSteps = new JSONObject();
        jsonPut(jsonSteps, "0", "dsfdsdads");
        jsonPut(jsonSteps, "1", "dsfdsdads");
        jsonPut(jsonSteps, "2", "dsfdsdads");
        jsonPut(jsonSteps, "3", "dsfdsdads");
        jsonPut(jsonSteps, "4", "dsfdsdads");
        jsonPut(jsonSteps, "5", "dsfdsdads");


        JSONObject json = new JSONObject();

        jsonPut(json, "name", "Warm up");
        jsonPut(json, "time", "10 Minutes");
        jsonPut(json, "difficulty", "2");
        jsonPut(json, "URL", "Ks-lKvKQ8f4");
        jsonPut(json, "instructions", jsonSteps.toString());


        // createNewExercise(root, json);

        JSONObject json2 = new JSONObject();

        jsonPut(json2, "name", "Test 2");
        jsonPut(json2, "time", "30 Minutes");
        jsonPut(json2, "difficulty", "5");
        jsonPut(json2, "URL", "Ks-lKvKQ8f4");
        jsonPut(json2, "instructions", jsonSteps.toString());


        //  createNewExercise(root, json2);

        JSONObject json3 = new JSONObject();

        jsonPut(json3, "name", "Test 3");
        jsonPut(json3, "time", "20 Minutes");
        jsonPut(json3, "difficulty", "4");
        jsonPut(json3, "URL", "Ks-lKvKQ8f4");
        jsonPut(json3, "instructions", jsonSteps.toString());

        createNewExercise(root, json3,"History");
        System.out.println("EXERCISEES TAM " + exercisesList.size());
        System.out.println("WORKOUTSEXERCISE TAM " + workoutsExercises.size());
        mProgressDialog.dismiss();

        return root;
    }

    private void jsonPut(JSONObject json, String key, Object value) {
        try {
            json.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Object jsonGet(JSONObject json, String key) {
        Object value = "";
        try {
            value = json.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return value;
    }

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
        //bt2.setTag("bt2");

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
        lp1.setMargins(0, dpToPx(10, root.getContext()), 0, 0);

        EditText e1 = new EditText(root.getContext());
        e1.setPadding(30, 0, 30, 0);
        e1.setText((String) jsonGet(json, "name")); // NOMBRE DEL EJERCICIO
        e1.setTextColor(getResources().getColor(R.color.DarkGrayText));
        e1.setBackgroundTintList(ContextCompat.getColorStateList(root.getContext(), R.color.DarkerButton));
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
        e2.setBackgroundTintList(ContextCompat.getColorStateList(root.getContext(), R.color.DarkerButton));
        e2.setPadding(30, 0, 30, 0);
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

       /* EditText e4 = new EditText(root.getContext()); // Temporizador.
        e4.setPadding(30, 0, 30, 0);
        e4.setTextSize(dpToPx(24, root.getContext()));
        e4.setTextColor(getResources().getColor(R.color.DarkGrayText));
        e4.setText("00:00");
        e4.setFocusable(false);
        e2.setBackgroundTintList(ContextCompat.getColorStateList(root.getContext(), R.color.DarkerButton));
        e4.setGravity(Gravity.CENTER);
        e4.setLayoutParams(lp5);*/

        Chronometer chronometer = new Chronometer(root.getContext());
        chronometer.setPadding(30, 0, 30, 0);
        chronometer.setTextSize(dpToPx(24, root.getContext()));
        chronometer.setTextColor(getResources().getColor(R.color.DarkGrayText));
        chronometer.setText("00:00");
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.setFormat("%s");
        chronometer.setFocusable(false);
        e2.setBackgroundTintList(ContextCompat.getColorStateList(root.getContext(), R.color.DarkerButton));
        chronometer.setGravity(Gravity.CENTER);
        chronometer.setLayoutParams(lp5);


        YouTubePlayerView ypv = new YouTubePlayerView(root.getContext());
        ypv.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {

            @Override
            public void onReady(@NotNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {
                String videoId = (String) jsonGet(json, "URL"); // PATH DEL EJERCICIO
                youTubePlayer.cueVideo(videoId, 0);
                //youTubePlayer.loadVideo(videoId, 0);
            }
        });

        you.add(ypv);


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
        sc1.setBackgroundColor(getResources().getColor(R.color.Background));


        EditText e5 = new EditText(root.getContext());
        e5.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        e5.setPadding(30, 0, 30, 0);
        e5.setTextColor(getResources().getColor(R.color.DarkGrayText));
        e5.setBackgroundTintList(ContextCompat.getColorStateList(root.getContext(), R.color.Background));
        e5.setSingleLine(false);

        JSONObject jo = null;
        try {
            jo = new JSONObject(((String) jsonGet(json, "instructions")));//INSTRUCCIONES -> DESCRIPCION.
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // e5.setText("Step 1: "+jsonGet(jo,"0"));
       /* for (int i=1 ; i<jo.length();i++){
            e5.append(Html.fromHtml("<br>").toString());
           // e5.append("Step "+(i+1)+": "+(String)jsonGet(jo, i+""));
            e5.append((String)jsonGet(jo, i+""));
        }*/
        e5.setText(Html.fromHtml("<br>").toString());
        e5.append((String) jsonGet(json, "instructions"));
        sc1.addView(e5);

        LinearLayout.LayoutParams lp8 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp8.setMargins(0, dpToPx(20, root.getContext()), 0, 0);

        LinearLayout ll2 = new LinearLayout(root.getContext());
        ll2.setOrientation(LinearLayout.HORIZONTAL);
        ll2.setGravity(Gravity.CENTER);
        ll2.setLayoutParams(lp8);

        Button bt1 = new Button(root.getContext()); //Start
        Button bt3 = new Button(root.getContext()); //Stop
        Button bt4 = new Button(root.getContext()); //EndUp

        bt1.setTag(tag);
        bt3.setTag(tag);
        bt4.setTag(tag);
        chronometer.setTag(tag);
        timers.put(tag,new Long(0));
        offsets.put(tag,new Long(0));
        first_time.put(tag,true);

        bt3.setEnabled(false);
        bt4.setEnabled(false);

        bt1.setBackground(root.getResources().getDrawable(R.drawable.background_model_training));
        bt1.setText(root.getResources().getString(R.string.start));
        bt1.setTextColor(root.getResources().getColor(R.color.DarkGrayText));

        bt1.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       if (!trainingWithoutConnection()) {
                                           Toast.makeText(root.getContext(), "IMPOSIBLE GUARDAR MAS ENTRENAMIENTOS", Toast.LENGTH_LONG).show();
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

        bt3.setBackground(root.getResources().getDrawable(R.drawable.background_target_waiting_training));
        bt3.setText(root.getResources().getString(R.string.stop));
        bt3.setTextColor(root.getResources().getColor(R.color.DarkGrayText));

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

        bt4.setBackground(root.getResources().getDrawable(R.drawable.background_model_training_history));
        bt4.setText(root.getResources().getString(R.string.end_up));
        bt4.setTextColor(root.getResources().getColor(R.color.White));

        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt1.setEnabled(false);
                bt2.setEnabled(false);
                addToDatabase();
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


        LinearLayout.LayoutParams lp9 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        ll3.setOrientation(LinearLayout.VERTICAL);
        ll3.setGravity(Gravity.CENTER_VERTICAL);
        ll3.setLayoutParams(lp9);

        ll3.addView(e1);
        ll3.addView(ll1);
        ll3.addView(ypv);
        ll3.addView(t1);
        ll3.addView(sc1);
        //ll3.addView(e4);
        ll3.addView(chronometer);
        ll3.addView(ll2);

        Space s7 = new Space(root.getContext());
        s7.setMinimumHeight(dpToPx(30, root.getContext()));

        ll3.addView(s7);

        ((LinearLayout) root.findViewById(R.id.list)).addView(ll3, 0);
    }

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

    private void loadInformation() {
        workout_id = workoutsRepository.getWourkoutIdRoom(workout_id);
        System.out.println("WORK ID ROOM LOAD INFORMATION " + workout_id);
        workout = workoutsRepository.getWorkout(workout_id);
        workoutsExercises = workoutsExercisesRepository.getWorkouts(workout_id);
        for (int i = 0; i < workoutsExercises.size(); i++) {
            exercisesList.add(exercisesRepository.getExercises(workoutsExercises.get(i).getExercise_id()));
        }
        System.out.println("TAMAÑO " + exercisesList.size());
        for (int i = 0; i < exercisesList.size(); i++) {
            System.out.println(exercisesList.get(i));
            JSONObject json = new JSONObject();
            jsonPut(json, "name", workout.getName() + ":  " + "exercise " + i);
            int exercise_id = exercisesList.get(i).getId();
           // String time = workoutsExercisesRepository.getTime(workout_id, exercise_id ); // obtener de workoutsExercises
            jsonPut(json, "time", "Video");
            jsonPut(json, "difficulty", workout.getDifficulty());
            jsonPut(json,"URL", exercisesList.get(i).getPath());
           // jsonPut(json, "URL", "Ks-lKvKQ8f4"); // todo CAMBIAR ACA POR PATH.
            jsonPut(json, "instructions", exercisesList.get(i).getDescription());
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    createNewExercise(root, json,""+ exercise_id);
                }
            });
        }
        System.out.println("EXERCISEES TAM " + exercisesList.size());
        System.out.println("WORKOUTSEXERCISE TAM " + workoutsExercises.size());
        //cargar datos de room, ya esta todo guardado ahi.
    }

    private boolean trainingWithoutConnection() {
        //Consulta con la base de datos local si puede realizar el training antes de que de play. Esto si es que no hay internet.
        //Agregue un campo en device_users que es upload -> entrenamientos por subir.
        if (internetConnection())
            return true;
        UserRepository userRepository = new UserRepository(getActivity().getApplication());
        int user_id = userRepository.getId(MainActivity.email);
        DeviceUsersRepository deviceUsersRepository = new DeviceUsersRepository((getActivity().getApplication()));
        int works_saved = deviceUsersRepository.getWorksSaved(user_id);
        System.out.println("WORK SAVED " + works_saved);
        return works_saved < 5;
    }

    private boolean internetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private void addToDatabase() {
        //todo agregar trainings al dar end up a la base de datos.
        //se agrega el training a la base local (para el serverless), al dar end up si no habia internet se aumentaria en uno la cantidad de entrenamientos almacenados.

        showDialogRating();

        if (internetConnection()) {
            // Si hay conexión a Internet en este momento OkHttp
            //post y punto
        } else {
            // No hay conexión a Internet en este momento Room
            System.out.println("ENTRO A AGREGAR trainiings ");
            addFinishTrainingNoInternet();
        }
    }

    private void addFinishTrainingNoInternet() {
        //Es esta funcion agrego los datos del entrenamiento finalizado.

        System.out.println("WORK ID " + workout_id);
        //Actualizo el workout como done.
        WorkoutsRepository workoutsRepository = new WorkoutsRepository(getActivity().getApplication());
        workoutsRepository.update(true, workout_id, rating); // actualizo campo done en workouts

        //Actualizo en +1 los trabajos guardados sin conexion. Cuando vuelva la conexion pregunto por este valor y si es > 0 hago push. VER DONDE HACER ESTO.
        UserRepository userRepository = new UserRepository(getActivity().getApplication());
        int user_id = userRepository.getId(MainActivity.email);
        DeviceUsersRepository deviceUsersRepository = new DeviceUsersRepository((getActivity().getApplication()));
        deviceUsersRepository.increaseWorks(user_id);

        //Agregar reportes.
        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        System.out.println(sdf.format(currentDate.getTime()));
        String execution_date = PikerDate.Companion.toDateFormat(sdf.format(currentDate.getTime()));
        WorkoutReports report_to_insert = new WorkoutReports(workout_id, execution_date);
        WorkoutsReportsRepository workoutsReportsRepository = new WorkoutsReportsRepository(getActivity().getApplication());
        workoutsReportsRepository.insert(report_to_insert);

        //Agregar HeartRateSignals valores que obtenemos de conexion de dispositivos cada x tiempo.
        //todo
    }

    public void showDialogRating()
    {
        final AlertDialog.Builder popDialog = new AlertDialog.Builder(getContext(), R.style.AppCompatAlertDialogStyle);

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
                        setStars((int) Math.ceil(rating.getProgress())/10);
                        dialog.dismiss();
                    }

                })

                // Button Cancel
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        popDialog.create();
        popDialog.show();

    }

    public void setStars(int n){
        this.rating = n;
    }

    private void startChronometer(Chronometer chronometer){
        if (!running){
            if (first_time.get(chronometer.getTag())){
                chronometer.setBase(SystemClock.elapsedRealtime());
                first_time.replace((String) chronometer.getTag(),false);
            }
            else{
                chronometer.setBase(SystemClock.elapsedRealtime()  - offsets.get(chronometer.getTag()));
            }

            chronometer.start();
            running = true;
        }
    }

    private void stopChronometer(Chronometer chronometer){
        if (running){
            chronometer.stop();
           // long base = SystemClock.elapsedRealtime() - timers.get(chronometer.getTag());
           // timers.replace((String) chronometer.getTag(), timers.get(chronometer.getTag()) + base);
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            offsets.replace((String)chronometer.getTag(),pauseOffset);
            running = false;
        }
    }


}
