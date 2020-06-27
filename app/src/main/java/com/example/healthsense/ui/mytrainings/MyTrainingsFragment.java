package com.example.healthsense.ui.mytrainings;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.healthsense.MainActivity;
import com.example.healthsense.R;
import com.example.healthsense.Resquest.OkHttpRequest;
import com.example.healthsense.Resquest.doAsync;
import com.example.healthsense.db.Repository.DeviceUsersRepository;
import com.example.healthsense.db.Repository.ExercisesRepository;
import com.example.healthsense.db.Repository.UserRepository;
import com.example.healthsense.db.Repository.WorkoutsExercisesRepository;
import com.example.healthsense.db.Repository.WorkoutsRepository;
import com.example.healthsense.db.entity.Exercises;
import com.example.healthsense.db.entity.WorkoutExercises;
import com.example.healthsense.db.entity.Workouts;
import com.example.healthsense.db.entity.old.Workout;
import com.example.healthsense.ui.traininginformation.TrainingInformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

import static com.example.healthsense.MainActivity.PREFS_FILENAME;

//ESTO NO SE HACE MAS.
// Obtener lista Device_User  /deviceUser/:fk&:0  -> fk es el user_id  ES UN USUARIO
// Obtener lista Medical_Personnel /medicalPersonnel/:fk&:0 -> fk es el user_id ES UN MEDICO

//AHORA
// Obtener lista Workouts  /workout/:fk&:0 -> fk es el device_user_id o medical_personnel_id USAR UNA DE LAS 2 OPCIONES DE ARRIBA (POR AHORA USUARIO)
// Obtener lista Workouts_exercises /workoutExercise/:workout_id&:id ->id es el fk de work_out
// (cada uno de los ids de la lista aterior) lista con todos los workouts_exercises
// Obtener lista Exercises /exercise/:id -> id es cada uno de los ids de Workouts_exercises.

public class MyTrainingsFragment extends Fragment {
    private ArrayList<LinearLayout> listll;
    private ArrayList<JSONObject> workouts, exercises;
    private HashMap<Integer,String> times;
    private HashMap<Integer, ArrayList<Integer>> workoutExercises;//La base de datos permite que un workoutid tenga varios exercises.
    private final String URL_BASE = "https://healthsenseapi.herokuapp.com/";
    private JSONArray token;
    private ProgressDialog mProgressDialog;
    private View.OnClickListener listenerGeneral;
    private View rootGeneral;
    private ArrayList<Integer> calls = new ArrayList<>();
    private List<Exercises> exercises_room;
    private ExercisesRepository exercisesRepository;
    private WorkoutsRepository workoutsRepository;
    private WorkoutsExercisesRepository workoutsExercisesRepository;
    private  DeviceUsersRepository deviceUsersRepository;
    public static Fragment fg;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_trainings, container, false);
        listll = new ArrayList<>();
        workouts = new ArrayList<>();
        exercises = new ArrayList<>();
        workoutExercises = new HashMap<>();
        times = new HashMap<>();
        fg = this;

        deviceUsersRepository = new DeviceUsersRepository(getActivity().getApplication());
        workoutsRepository = new WorkoutsRepository(getActivity().getApplication());
        exercisesRepository = new ExercisesRepository(getActivity().getApplication());
        workoutsExercisesRepository = new WorkoutsExercisesRepository(getActivity().getApplication());

        //exercisesRepository.deleteAll();
        exercises_room = exercisesRepository.getAll();
        System.out.println("TAMA " + exercises_room.size());

        View.OnClickListener mListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // nuevo intent con la info del layout seleccionado.
                SharedPreferences preferencesEditor = getActivity().getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE);
                System.out.println("WORK ID: " + v.getTag());
                preferencesEditor.edit().putInt("Work_id", (int) v.getTag());
                TrainingInformation.fg = fg;
                getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new TrainingInformation()).addToBackStack(null).commit();
            }
        };

        rootGeneral = root;
        listenerGeneral = mListener;

        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        mProgressDialog = new ProgressDialog(root.getContext(), R.style.AppCompatAlertDialogStyle);
        mProgressDialog.setTitle(R.string.loading);
        mProgressDialog.setMessage(getResources().getString(R.string.please_wait));
        mProgressDialog.setCancelable(false);

        if (networkInfo != null && networkInfo.isConnected()) {
            System.out.println("ES LA PRIMERA VEZ? " + MainActivity.FIRST_TRAINING);
            // Si hay conexión a Internet en este momento OkHttp
            mProgressDialog.show();
            getData();
        } else {
            // No hay conexión a Internet en este momento levantar datos de Room (SERVERLESS)
        }
        return root;
    }

    private synchronized int addCall() {
        calls.add(1);
        Log.d("CALLS", calls.size() + "");
        return calls.size() - 1;
    }

    private void finishCall(int i) {
        calls.set(i, 0);
        for (int j = 0; j < calls.size(); j++) {
            if (calls.get(j) == 1)
                return;
        }
        depureExercises();

        if (MainActivity.FIRST_TRAINING) {
            try {
                addWorkoutsRoom();
                addExercisesRoom(exercises_room);
                addWorkoutsExercisesRoom();
                addWorkoutsExercisesRoom();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            MainActivity.FIRST_TRAINING = false;
        }

        createExercises(rootGeneral, listenerGeneral);
    }


    private void depureExercises() {
        List<Integer> values = new ArrayList<>();
        int contador = 0;
        while (contador < exercises.size()) {
            int exercises_id = 0;
            try {
                if (exercises.get(contador).has("id")) {
                    exercises_id = exercises.get(contador).getInt("id");
                    if (!values.contains(new Integer(exercises_id))) {
                        contador++;
                        values.add(new Integer(exercises_id));
                    } else {
                        exercises.remove(exercises.get(contador));
                    }
                } else {
                    exercises.remove(exercises.get(contador));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < values.size(); i++)
            System.out.println(values.get(i));
    }

    private void createExercises(View root, View.OnClickListener mListener) {
        //Logica para obtener datos necesarios
        for (int i = 0; i < workouts.size(); i++) { // desde i hasta cant workouts
            newTraining(root, i, mListener); // ver que datos enviar
        }
        mProgressDialog.dismiss();
    }

    private Runnable getWorks() {
        return new Runnable() {
            @Override
            public void run() {
                String path = "workoutExercise/workout_id&"; // obtengo por cada workout_id el workout_exercice asociado
                token = new JSONArray();
                int i = 0;
                for (JSONObject o : workouts) {
                    try {
                        i++;
                        getBackendResponse(path, o.getInt("id"), null, workoutExercises, null, null, getExercisesWorkout(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        };
    }

    private Runnable getExercisesWorkout(int cant) {
        return new Runnable() {
            @Override
            public void run() {
                String finalPath = "exercise/";
                Map<Integer, ArrayList<Integer>> map = new HashMap<Integer, ArrayList<Integer>>(workoutExercises);
                for (ArrayList<Integer> value : map.values()) {
                    for (int i = 0; i < value.size(); i++) {
                        if ((i == map.values().size() - 1) && cant == workouts.size()) {
                            getLastResponse(finalPath, value.get(i).intValue(), exercises);
                        } else {
                            getBackendResponse(finalPath, value.get(i).intValue(), exercises, null, null, null, null);
                        }
                    }
                }
            }
        };
    }


    private void getData() {
        doAsync.execute(new Runnable() {
            @Override
            public void run() {
                String path = "workout/device_user_id&"; // obtengo workouts del user_id. REEMPLAZAR TOKEN por MAINACTIVITY.TOKEN
                JSONObject ob = new JSONObject(); //
                MainActivity.TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6InBydWViYTNAZ21haWwuY29tIiwiaWF0IjoxNTkxOTcyODMzfQ.PARzs0fB4Iz2l2H5RTWoRdrPBCGZR6dcB-y2YoC77XE";
                try {
                    ob.put("x-access-token", MainActivity.TOKEN);
                    token = new JSONArray();
                    token.put(ob);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                workouts = new ArrayList<>();
                exercises = new ArrayList<>();
                workoutExercises = new HashMap<Integer, ArrayList<Integer>>();
                listll = new ArrayList<>();

                getBackendResponse(path, 0, workouts, null, null, null, getWorks());
            }
        });
    }

    private String formatDate(String date) throws ParseException {
        Date d = new SimpleDateFormat("dd-MM-yyyy").parse(date);
        String formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(d);
        return formatDate.substring(0, 9);
    }

    private void newTraining(View root, int i, View.OnClickListener mListener) {
        //----programación de formato de la lista de trainings
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject workout = workouts.get(i);
                    JSONObject exercise = new JSONObject();
                    String description = "No description";
                    String date = "";
                    int difficulty = 1;
                    try {
                        ArrayList<Integer> values = workoutExercises.get(new Integer(workout.getInt("id")));
                        if (values != null && values.size() > 0) {
                            // System.out.println("VALUES " + values.size());
                            int x = 0;
                            int k = 0;
                            boolean encontrado = false;
                            while (exercise.length() < x && !encontrado) {
                                while (k < values.size()) {
                                    if (exercises.get(i).getInt("id") != values.get(x).intValue())
                                        k++;
                                    else
                                        encontrado = true;
                                }
                                x++;
                            }
                            exercise = exercises.get(k);
                            description = exercise.getString("description");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Space s0 = new Space(root.getContext());
                    s0.setMinimumHeight(30);

                    LinearLayout ll = new LinearLayout(root.getContext());
                    ll.setOrientation(LinearLayout.HORIZONTAL);
                    ll.setGravity(Gravity.CENTER_VERTICAL);

                    TextView tv1 = new TextView(root.getContext());
                    try {
                        date = formatDate(workout.getString("creation_date"));
                        difficulty = workout.getInt("difficulty");
                        tv1.setText(Html.fromHtml("<b>" + workout.getString("name") + "</b>" + "<br><br>" + description));
                    } catch (JSONException | ParseException e) {
                        e.printStackTrace();
                    }
                    //tv1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_star, 0, 0, 30);
                    tv1.setTextColor(root.getResources().getColor(R.color.GrayText));
                    tv1.setPadding(30, 30, 30, 30);

                    ll.addView(tv1);

                    LinearLayout ll1 = new LinearLayout(root.getContext());
                    ll1.setOrientation(LinearLayout.HORIZONTAL);
                    ll1.setGravity(Gravity.CENTER_VERTICAL);

                    //   System.out.println("DATE " + date);
                    TextView tv3 = new TextView(root.getContext());
                    tv3.setText(Html.fromHtml("<b>CREATED:</b>" + date + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>DIFFICULTY:</b> "));
                    tv3.setTextColor(root.getResources().getColor(R.color.GrayText));
                    tv3.setPadding(30, 30, 30, 30);

                    ll1.addView(tv3);


                    for (int j = 0; j < difficulty; j++) {
                        TextView tv4 = new TextView(root.getContext());
                        tv4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_star, 0, 0, 0);
                        tv4.setPadding(0, 20, 0, 20);
                        ll1.addView(tv4);
                    }


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
                    try {
                        listll.get(listll.size() - 1).setTag(workout.getInt("id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    listll.get(listll.size() - 1).setOnClickListener(mListener);
                    //------fin de programación del formato
                }
            });
        }
    }

    private Call getBackendResponse(String path, int id, ArrayList<JSONObject> listObj, HashMap<Integer, ArrayList<Integer>> hashObj, View root, View.OnClickListener mListener, Runnable func) {
        OkHttpRequest request = new OkHttpRequest(new OkHttpClient());
        String url = URL_BASE + path + id;

        int numCall = addCall();
        return request.GET(url, token, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                finishCall(numCall);
                mProgressDialog.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = null;
                try {
                    ArrayList<Integer> values = new ArrayList<>();
                    responseData = response.body().string();
                    Object jsonType = new JSONTokener(responseData).nextValue();

                    if (jsonType instanceof JSONObject) { //Si es JSONOBject -> exercices
                        JSONObject json = new JSONObject((responseData));
                        if (listObj != null) {
                            listObj.add(json);

                        }
                    } else if (jsonType instanceof JSONArray) {// Si es JSONArray -> workoust/workout_exercise
                        JSONArray json = new JSONArray(responseData);
                        for (int i = 0; i < json.length(); i++) {
                            if (hashObj == null) {
                                listObj.add(json.getJSONObject(i));
                            } else {
                                String time = json.getJSONObject(i).get("time").toString();
                                int id = json.getJSONObject(i).getInt("exercise_id");
                                times.put(id,time);
                                values.add(id);
                            }
                        }
                        if (hashObj != null) {
                            if (json.length() != 0)
                                hashObj.put(id, values);
                        }
                        if (func != null)
                            func.run();
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                } finally {
                    finishCall(numCall);
                }
            }
        });
    }

    private Call getLastResponse(String path, int id, ArrayList<JSONObject> listObj) {
        OkHttpRequest request = new OkHttpRequest(new OkHttpClient());
        String url = URL_BASE + path + id;
        int numCall = addCall();

        return request.GET(url, token, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                finishCall(numCall);
                mProgressDialog.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = null;
                try {
                    responseData = response.body().string();
                    JSONObject json = new JSONObject((responseData));
                    listObj.add(json);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                } finally {
                    finishCall(numCall);
                }
            }
        });
    }


    private void addWorkoutsRoom() throws JSONException {
        // En esta funcion guardo todos los exercises y los workouts_exercises para el serverless.
        UserRepository userRepository = new UserRepository(getActivity().getApplication());
        //  workoutsRepository.deleteAll();
        List<Workouts> workouts_room = workoutsRepository.getAll();
      /*  System.out.println("TAM WORKOURS" + workouts_room.size());
        for(int k = 0 ; k<workouts_room.size();k++){
            System.out.println("ID WORK " + workouts_room.get(k).getId());
        }*/
        boolean in_room;
        for (int i = 0; i < workouts.size(); i++) {
            int workout_id = workouts.get(i).getInt("id");
            in_room = false;
            for (int j = 0; j < workouts_room.size(); j++) {
                System.out.println("ID DEL WORKOUT " + workout_id + "  ID DEL ROOM " + workouts_room.get(j).getId_backend());
                if (workout_id == workouts_room.get(j).getId_backend()) {
                    in_room = true;
                    break;
                }
            }
            System.out.println("ESTA EN ROOM? " + in_room);
            if (!in_room) {
                int device_user_id_room = deviceUsersRepository.getDeviceUserId(userRepository.getId(MainActivity.email));
                System.out.println("DEVICE USER ID " + device_user_id_room);
                int price = 0;
                try {
                    price = workouts.get(i).getInt("price");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                int rating = 0;
                try {
                    rating = workouts.get(i).getInt("rating");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Workouts workout_to_insert = new Workouts(null, device_user_id_room, workouts.get(i).getString("name"),
                        workouts.get(i).getString("creation_date"), workouts.get(i).getInt("difficulty"), price,
                        workouts.get(i).getInt("done"), rating);
                workout_to_insert.setId_backend(workout_id);
                workoutsRepository.insert(workout_to_insert);
            }
        }
    }

    private void addExercisesRoom(List<Exercises> exercises_room) throws JSONException {
        ExercisesRepository exercisesRepository = new ExercisesRepository(getActivity().getApplication());
        boolean in_room;
        for (int i = 0; i < exercises.size(); i++) {
            int exercises_id = 0;
            try {
                exercises_id = exercises.get(i).getInt("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //  System.out.println("EXERCISES ID: " + exercises_id);
            in_room = false;
            for (int j = 0; j < exercises_room.size(); j++) {
                //        System.out.println("EXERCICE ID ROOM: " + exercises_room.get(j).getId_backend() + "Y EL ID DEL BACK: " + exercises_id);
                if (exercises_room.get(j).getId_backend() == exercises_id) {
                    in_room = true;
                    break;
                }
            }
            //   System.out.println("EXERCICE EN ROOM?: " + in_room);
            if (!in_room) {
                Exercises exercises_to_insert = null;
                try {
                    exercises_to_insert = new Exercises(exercises.get(i).getString("description"), exercises.get(i).getString("path"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                exercises_to_insert.setId_backend(exercises_id);
                //        System.out.println("EXERCISES ID BACK: " + exercises_to_insert.getId_backend());
                //        System.out.println("EXERCISES DESCRIPTION: " + exercises_to_insert.getDescription());
                //         System.out.println("EXERCISES PATH: " + exercises_to_insert.getPath());
                exercisesRepository.insert(exercises_to_insert);
            }
        }

    }

    private void addWorkoutsExercisesRoom() {
        Map<Integer, ArrayList<Integer>> map = new HashMap<Integer, ArrayList<Integer>>(workoutExercises);
       // workoutsExercisesRepository.deleteAll();
        boolean in_room;
        for (Integer key : map.keySet()) {
            List<Integer> v = map.get(key);
            int workout_id_room = workoutsRepository.getWourkoutIdRoom(key);
            for (int i = 0; i < v.size(); i++) {
                int exercises_id_room = exercisesRepository.getExercisesIdBackend(v.get(i));
                in_room = workoutsExercisesRepository.existExercisesWorkout(workout_id_room,exercises_id_room);
                System.out.println("ESTA EN ROOM? : " + workout_id_room + "  " + exercises_id_room + " " + in_room);
                if (!in_room){
                    //System.out.println("ENTRO A INSERTAR EL WORKOUT: " + workout_id_room + exercises_id_room + "CON UN TIEMPO: " + times.get(key));
                    WorkoutExercises to_insert = new WorkoutExercises(workout_id_room,exercises_id_room, times.get(key));
                    workoutsExercisesRepository.insert(to_insert);
                }
            }
        }
    }
}