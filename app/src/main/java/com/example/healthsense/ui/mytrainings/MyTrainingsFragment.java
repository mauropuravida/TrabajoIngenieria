package com.example.healthsense.ui.mytrainings;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.example.healthsense.db.Repository.WorkoutsRepository;
import com.example.healthsense.db.entity.Exercises;
import com.example.healthsense.db.entity.WorkoutExercises;
import com.example.healthsense.db.entity.Workouts;
import com.example.healthsense.ui.traininginformation.EditFragment;
import com.example.healthsense.ui.traininginformation.TrainingInformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class MyTrainingsFragment extends Fragment {
    private ArrayList<LinearLayout> listll;
    private ArrayList<JSONObject> workouts, exercises;
    private HashMap<Integer, String> times;
    private HashMap<Integer, ArrayList<Integer>> workoutExercises;
    private JSONArray token;
    private ProgressDialog mProgressDialog;
    private View.OnClickListener listenerGeneral;
    private View rootGeneral;
    private ArrayList<Integer> calls = new ArrayList<>();
    private List<Exercises> exercises_room;
    private ExercisesRepository exercisesRepository;
    private WorkoutsRepository workoutsRepository;
    private WorkoutsExercisesRepository workoutsExercisesRepository;
    private DeviceUsersRepository deviceUsersRepository;
    public static Fragment fg;
    private Bundle datosAEnviar = new Bundle();
    private String fragment_previous = null;


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

        exercises_room = exercisesRepository.getAll();
        Bundle datosRecuperados = getArguments();


        //Listener para cada uno de los workouts.
        View.OnClickListener mListener = new View.OnClickListener() {
            @SuppressLint("CommitPrefEdits")
            @Override
            public void onClick(View v) {
                // nuevo intent con la info del layout seleccionado.
                datosAEnviar.putInt("Work_ID", (int) v.getTag());
                TrainingInformation.fg = fg;
                Fragment fragment;
                if(fragment_previous.equals("M"))
                    datosAEnviar.putString("Fragment", "M");
                else {
                    datosAEnviar.putString("Fragment", "S");
                    datosAEnviar.putDouble("price",datosRecuperados.getDouble("price"));
                    datosAEnviar.putInt("device_user_id",datosRecuperados.getInt("device_users_id"));
                }
                fragment = new TrainingInformation();
                fragment.setArguments(datosAEnviar);
                getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment).addToBackStack(null).commit();
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

        mProgressDialog.show();
        if (datosRecuperados != null && datosRecuperados.getString("Fragment").equals("S")) {
            fragment_previous = "S";
            System.out.println("DEVICE " + datosRecuperados.getInt("device_users_id"));
            getData(datosRecuperados.getInt("device_users_id"));
        } else {
            fragment_previous = "M";
            if (networkInfo != null && networkInfo.isConnected()) {
                // Si hay conexión a Internet en este momento OkHttp
                getData(0);
            } else {
                // No hay conexión a Internet en este momento levantar datos de Room (SERVERLESS)
            }
        }

        return root;
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
            if (calls.get(j) == 1)
                return;
        }
        System.out.println("FINISH CALLS");

        depureExercises();
        // Si finalizaron todas las llamadas del backend y es la primera vez que entro al fragment
        // Agrego a la base de datos local los workouts y exercises que no esten.
        if (getActivity() != null) {
            try {
                System.out.println("ROOM");

                addWorkoutsRoom();
                addExercisesRoom(exercises_room);
                addWorkoutsExercisesRoom();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        System.out.println("CREAR EXERCICES");


        createExercises(rootGeneral, listenerGeneral);
    }

    /**
     * Chequeo de mensajes recibidos del backend.
     */
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
            System.out.println(contador);
        }
    }

    /**
     * Creacion de Targets por cada workout.
     */
    //todo descomentar first training arriba, sacar de aca, dejar el toast si workoutsize == 0;
    private void createExercises(View root, View.OnClickListener mListener) {
        for (int i = 0; i < workouts.size(); i++) {
            newTraining(root, i, mListener);
        }

        if (workouts.size() == 0) {
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(root.getContext(), root.getResources().getString(R.string.no_workouts), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        mProgressDialog.dismiss();
    }

    /**
     * Consulta al backend por los trabajos asociados al usuario logueado.
     */
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

    /**
     * Consulta al backend por los ejercicios asociados a los trabajos obtenidos en getWorks.
     */
    private Runnable getExercisesWorkout(int cant) {
        return new Runnable() {
            @Override
            public void run() {
                String finalPath = "exercise/";
                Map<Integer, ArrayList<Integer>> map = new HashMap<Integer, ArrayList<Integer>>(workoutExercises);
                for (ArrayList<Integer> value : map.values()) {
                    for (int i = 0; i < value.size(); i++) {
                        System.out.println("VALUES " + value.get(i));
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

    /**
     * Funcion que lanza los llamados al backend.
     */
    private void getData(int device_user_id) {
        doAsync.execute(new Runnable() {
            @Override
            public void run() {
                String path = "workout/device_user_id&"; // obtengo workouts del user_id.
                JSONObject ob = new JSONObject(); //
                //MainActivity.TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6InBydWViYTNAZ21haWwuY29tIiwiaWF0IjoxNTkxOTcyODMzfQ.PARzs0fB4Iz2l2H5RTWoRdrPBCGZR6dcB-y2YoC77XE"; //User de prueba.
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

                getBackendResponse(path, device_user_id, workouts, null, null, null, getWorks());
            }
        });
    }

    /**
     * Crea las vistas de los Workouts.
     *
     * @param root      = This
     * @param i         = Workout
     * @param mListener = Target
     */
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
                            int x = 0;
                            int k = 0;
                            boolean encontrado = false;
                            while (exercises.size() > x && !encontrado) {
                                while (k < values.size()) {
                                    if (exercises.get(x).getInt("id") != values.get(k).intValue()) {
                                        k++;
                                    } else {
                                        encontrado = true;
                                        break;
                                    }
                                }
                                if (encontrado)
                                    break;
                                x++;
                                k = 0;
                            }
                            System.out.println("ENCONTRE L VALOr " + x);
                            if (x <= exercises.size()) {
                                exercise = exercises.get(x);
                                description = exercise.getString("description");
                            } else
                                description = "No description.";
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
                        date = PikerDate.Companion.toDateFormatView(workout.getString("creation_date"));
                        difficulty = workout.getInt("difficulty");
                        tv1.setText(Html.fromHtml("<b>" + workout.getString("name") + "</b>" + "<br><br>" + description));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    tv1.setTextColor(root.getResources().getColor(R.color.GrayText));
                    tv1.setPadding(30, 30, 30, 30);

                    ll.addView(tv1);

                    LinearLayout ll1 = new LinearLayout(root.getContext());
                    ll1.setOrientation(LinearLayout.HORIZONTAL);
                    ll1.setGravity(Gravity.CENTER_VERTICAL);

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

    /**
     * Realiza GET de la url solicitada para obtener los datos del backend.
     *
     * @param path      = Url
     * @param id        = WorkoutId, Workout/Exercises ID
     * @param listObj   = Workouts/Exercises
     * @param hashObj   = Pair (Workout,List<Exercises>)
     * @param root      = This
     * @param mListener = Target
     * @param func      = Next Call.
     * @return
     */
    private Call getBackendResponse(String path, int id, ArrayList<JSONObject> listObj, HashMap<Integer, ArrayList<Integer>> hashObj, View root, View.OnClickListener mListener, Runnable func) {
        OkHttpRequest request = new OkHttpRequest(new OkHttpClient());
        String url = MainActivity.PATH + path + id;
        System.out.println(url);

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
                            System.out.println("EXERCISES " + json.get("id"));
                            listObj.add(json);

                        }
                    } else if (jsonType instanceof JSONArray) {// Si es JSONArray -> workoust/workout_exercise
                        JSONArray json = new JSONArray(responseData);
                        for (int i = 0; i < json.length(); i++) {
                            if (hashObj == null) {
                                if ((int) json.getJSONObject(i).get("done") == 0) {
                                    System.out.println("WORKOUT " + json.getJSONObject(i).getInt("id"));
                                    listObj.add(json.getJSONObject(i));
                                }
                            } else {
                                String time = json.getJSONObject(i).get("time").toString();
                                int id = json.getJSONObject(i).getInt("exercise_id");
                                times.put(id, time);
                                System.out.println("Exercices id " + id);
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

    /**
     * Ultimo llamado al backend.
     *
     * @param path    = Url
     * @param id      = ExerciseId
     * @param listObj = ExercisesList
     * @return
     */
    private Call getLastResponse(String path, int id, ArrayList<JSONObject> listObj) {
        OkHttpRequest request = new OkHttpRequest(new OkHttpClient());
        String url = MainActivity.PATH + path + id;
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

    /**
     * Creacion de Workouts locales para Serverless.
     *
     * @throws JSONException
     */
    private void addWorkoutsRoom() throws JSONException {
        // En esta funcion guardo todos los exercises y los workouts_exercises para el serverless.
        UserRepository userRepository = new UserRepository(getActivity().getApplication());
        List<Workouts> workouts_room = workoutsRepository.getAll();
        boolean in_room;
        for (int i = 0; i < workouts.size(); i++) {
            int workout_id = workouts.get(i).getInt("id");
            in_room = false;
            for (int j = 0; j < workouts_room.size(); j++) {
                if (workout_id == workouts_room.get(j).getId_backend()) {
                    in_room = true;
                    break;
                }
            }
            System.out.println("NO ESTA EN ROOM? " + in_room + " Workout ID " + workout_id);
            // Se crea y agrega a la base de datos local si es que no existe aun.
            if (!in_room) {
                Integer device_user_id_room = deviceUsersRepository.getDeviceUserId(userRepository.getId(MainActivity.email));
                int cant = 0;
                while (device_user_id_room == 0 && cant <3) {
                    System.out.println("LOOP 23");
                    device_user_id_room = deviceUsersRepository.getDeviceUserId(userRepository.getId(MainActivity.email));
                    cant++;
                }
                if (device_user_id_room == 0)
                    device_user_id_room = null;
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

    /**
     * Creacion de Exercises locales para Serverless.
     *
     * @param exercises_room = Exercises de la base local.
     * @throws JSONException
     */
    private void addExercisesRoom(List<Exercises> exercises_room) throws JSONException {
        ExercisesRepository exercisesRepository = new ExercisesRepository(getActivity().getApplication());
        boolean in_room;
        System.out.println(exercises.size());

        for (int i = 0; i < exercises.size(); i++) {
            System.out.println("LOOP 1");
            int exercises_id = 0;
            try {
                while (exercises_id == 0) {
                    exercises_id = exercises.get(i).getInt("id");
                    System.out.println("LOOP 2");

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            in_room = false;
            for (int j = 0; j < exercises_room.size(); j++) {
                 System.out.println("EXERCICE ID ROOM: " + exercises_room.get(j).getId_backend() + "Y EL ID DEL BACK: " + exercises_id);
                if (exercises_room.get(j).getId_backend() == exercises_id) {
                    in_room = true;
                    break;
                }
            }
               System.out.println("EXERCICE EN ROOM?: " + in_room);
            if (!in_room) {
                Exercises exercises_to_insert = null;
                try {
                    exercises_to_insert = new Exercises(exercises.get(i).getString("description"), exercises.get(i).getString("path"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                exercises_to_insert.setId_backend(exercises_id);
                exercisesRepository.insert(exercises_to_insert);
            }
        }

    }

    /**
     * Creacion Wrokout_Exercises para Serverless.
     */
    private void addWorkoutsExercisesRoom() {
        Map<Integer, ArrayList<Integer>> map = new HashMap<Integer, ArrayList<Integer>>(workoutExercises);
        //workoutsExercisesRepository.deleteAll();
        System.out.println("LOOP 31");

        boolean in_room;
        for (Integer key : map.keySet()) {
            List<Integer> v = map.get(key);
            int workout_id_room = workoutsRepository.getWourkoutIdRoom(key);
            for (int i = 0; i < v.size(); i++) {
                int exercises_id_room = exercisesRepository.getExercisesIdBackend(v.get(i));
                while (exercises_id_room == 0)
                    exercises_id_room = exercisesRepository.getExercisesIdBackend(v.get(i));
                in_room = workoutsExercisesRepository.existExercisesWorkout(workout_id_room, exercises_id_room);
                 System.out.println("ESTA EN ROOM? : WorkoutID " + workout_id_room + "  ExerciseID" + exercises_id_room + " " + in_room);
                if (!in_room) {
                    System.out.println("ENTRO A INSERTAR EL WORKOUT: " + workout_id_room + exercises_id_room + "CON UN TIEMPO: " + times.get(key));
                    WorkoutExercises to_insert = new WorkoutExercises(workout_id_room, exercises_id_room, times.get(key));
                    workoutsExercisesRepository.insert(to_insert);
                }
            }
            System.out.println("FINISHHHH");

        }
    }

}