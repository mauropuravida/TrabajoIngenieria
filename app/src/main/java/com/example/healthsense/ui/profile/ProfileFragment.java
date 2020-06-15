package com.example.healthsense.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.healthsense.MainActivity;
import com.example.healthsense.R;
import com.example.healthsense.Resquest.OkHttpRequest;
import com.example.healthsense.data.PikerDate;
import com.example.healthsense.ui.login.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

import static com.example.healthsense.MainActivity.PREFS_FILENAME;

public class ProfileFragment extends Fragment {

    private Context cont;
    private ArrayList<String> idsCountries;
    private ArrayList<String> idsStates;

    private String stateId = "-1";
    private String countryId = "-1";
    private int profile;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        SharedPreferences preferencesEditor = this.getActivity().getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE);
        profile = preferencesEditor.getInt(MainActivity.email+"profile", R.layout.fragment_profile_medical);

        View root = inflater.inflate(profile, container, false);
        cont = root.getContext();

        Spinner spGender = root.findViewById(R.id.gender);
        ArrayList<String> values = new ArrayList();
        values.add("Other");
        values.add("Male");
        values.add("Female");
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(root.getContext(),
                R.layout.spinner_color, values);
        arrayAdapter2.setDropDownViewResource(R.layout.spinner_color);
        spGender.setAdapter(arrayAdapter2);

        //consulta información de usuario
        getInfoUser(root);

        //consulta la lista de provincias para el pais seleccionado
        Spinner sp = root.findViewById(R.id.country);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                inicSpinner(R.id.state, root, "https://healthsenseapi.herokuapp.com/state/country_id&" + idsCountries.get((int) sp.getSelectedItemId()), idsStates = new ArrayList<>());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        //consulta la lista de ciudades para la provincia seleccionada
        Spinner spState = root.findViewById(R.id.state);
        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                inicSpinner(R.id.city, root, "https://healthsenseapi.herokuapp.com/city/state_id&" + idsStates.get((int) spState.getSelectedItemId()), null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        final String[] select_qualification = {
                "Languages","Spanish", "Italian", "English", "Turkish"};
        Spinner spinner = root.findViewById(R.id.language);

        ArrayList<StateV0> listVOs = new ArrayList<>();

        for (int i = 0; i < select_qualification.length; i++) {
            StateV0 stateVO = new StateV0();
            stateVO.setTitle(select_qualification[i]);
            stateVO.setSelected(false);
            listVOs.add(stateVO);
        }
        MyAdapter myAdapter = new MyAdapter(root.getContext(), R.layout.spinner_color,
                listVOs);
        spinner.setAdapter(myAdapter);

        if (profile == R.layout.fragment_profile_medical) {
            inicSpinner(R.id.interal_medicine,root,"https://healthsenseapi.herokuapp.com/medicalspeciality/", null);
        } else {
            inicUser(root);
        }

        root.findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveInfo();
                }
            }
        );

        return root;
    }

    private int getIndex(String index, ArrayList<String> arr){
        for (int i=0 ;i<arr.size();i++){
            if (arr.get(i).equals(index))
                return i;
        }
        return 0;
    }

    private void getInfoUser(View root){
        OkHttpRequest request = new OkHttpRequest(new OkHttpClient());
        String conexion = (profile != R.layout.fragment_profile_medical) ? "https://healthsenseapi.herokuapp.com/userinfo/" : "https://healthsenseapi.herokuapp.com/medicalinfo/";

        JSONArray jarr = new JSONArray();
        try {
            JSONObject jobj = new JSONObject();
            jobj.put("x-access-token", MainActivity.TOKEN);
            jarr.put(jobj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("TOKEN ", MainActivity.TOKEN);

        request.GET(conexion,jarr, new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                //Toast.makeText(root.getContext(), "ERROR", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) {

                try {
                    String responseData = response.body().string();
                    JSONObject json = new JSONObject(responseData);

                    if(getActivity() == null)
                        return;

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if(getActivity() == null)
                                    return;
                                ((TextView)root.findViewById(R.id.name)).setText(json.getString("name"));
                                ((TextView)root.findViewById(R.id.lastname)).setText(json.getString("last_name"));
                                ((TextView)root.findViewById(R.id.email)).setText(json.getString("email"));
                                ((TextView)root.findViewById(R.id.credential)).setText(json.getString("document_number"));
                                ((TextView)root.findViewById(R.id.birth_date)).setText(PikerDate.Companion.toDateFormatView(json.getString("birth_date")));

                                if (!json.getString("address").equals("null"))
                                    ((TextView)root.findViewById(R.id.address)).setText(json.getString("address"));

                                ((Spinner) root.findViewById(R.id.gender)).setSelection((json.getString("gender").equals("null")) ? 0 :
                                        ((json.getString("gender").equals("Male")) ? 1 : 2));

                                if (profile == R.layout.fragment_profile_medical){
                                    ((Spinner) root.findViewById(R.id.interal_medicine)).setSelection(Integer.parseInt(json.getString("medical_speciality_id"))-1);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    String ct = (json.getString("city_id").equals("null")) ? "1" : json.getString("city_id");

                    getFK(root,"https://healthsenseapi.herokuapp.com/city/"+ct,"state_id");
                    getDocumentTypes(json.getString("document_type_id"));

                } catch (IOException e) {
                    e.printStackTrace();
                    Looper.prepare();
                    Toast.makeText(root.getContext(), LoginActivity.error(cont,response.code()), Toast.LENGTH_SHORT).show();
                    Looper.loop();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Looper.prepare();
                    Toast.makeText(root.getContext(), LoginActivity.error(cont,response.code()), Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
        });
    }


    private void getFK(View root, String url, String fk){
        OkHttpRequest request = new OkHttpRequest(new OkHttpClient());
        String conexion = url;

        final String[] value = {"-1"};

        request.GET(conexion, new JSONArray(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(root.getContext(), "ERROR", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) {

                try {
                    String responseData = response.body().string();
                    JSONObject json = new JSONObject(responseData);

                    if (fk.equals("state_id")) {
                        stateId = json.getString(fk);
                        getFK(root,"https://healthsenseapi.herokuapp.com/state/"+stateId,"country_id");
                    }
                    else {
                        countryId = json.getString(fk);
                        if(getActivity() == null)
                            return;

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(getActivity() == null)
                                    return;
                                //consulta la lista de paises
                                inicSpinner(R.id.country,root,"https://healthsenseapi.herokuapp.com/country/",idsCountries = new ArrayList<>());
                            }
                        });

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    Looper.prepare();
                    Toast.makeText(root.getContext(), LoginActivity.error(cont, response.code()), Toast.LENGTH_SHORT).show();
                    Looper.loop();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Looper.prepare();
                    Toast.makeText(root.getContext(), LoginActivity.error(cont, response.code()), Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
        });
    }

    private void inicUser(View root){

    }

    private void inicSpinner(int resourse ,View root, String path, ArrayList arr){
        Spinner sp = root.findViewById(resourse);
        ArrayList<String> values = new ArrayList();
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(root.getContext(),
                R.layout.spinner_color, values);
        arrayAdapter2.setDropDownViewResource(R.layout.spinner_color);
        sp.setAdapter(arrayAdapter2);

        OkHttpRequest request = new OkHttpRequest(new OkHttpClient());
        String conexion = path;

        request.GET(conexion, new JSONArray(), new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(root.getContext(), "ERROR", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) {

                try {
                    String responseData = response.body().string();
                    JSONArray jsonArray = new JSONArray(responseData);

                    Spinner sp = root.findViewById(resourse);
                    ArrayList<String> values = new ArrayList();

                    for (int i=0;i< jsonArray.length(); i++){

                        JSONObject json = new JSONObject(jsonArray.get(i).toString());

                        if(getActivity() == null)
                            return;

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if(getActivity() == null)
                                        return;
                                    values.add(json.getString("name"));
                                    if (arr != null){
                                        arr.add(json.getString("id"));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    if(getActivity() == null)
                        return;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if(getActivity() == null)
                                return;
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(root.getContext(),
                                    R.layout.spinner_color, values);
                            arrayAdapter.setDropDownViewResource(R.layout.spinner_color);
                            sp.setAdapter(arrayAdapter);
                            if (!countryId.equals("-1")) {
                                ((Spinner) root.findViewById(R.id.country)).setSelection((Integer.parseInt(countryId) - 1));
                                countryId = "-1";
                            }
                            if (!stateId.equals("-1")&& resourse == R.id.state){
                                ((Spinner)root.findViewById(R.id.state)).setSelection(getIndex(stateId,idsStates));
                                stateId = "-1";
                            }
                            if (countryId.equals("-1")&& resourse == R.id.city){
                                ((Spinner)root.findViewById(R.id.city)).setSelection(getIndex(stateId,idsStates));
                                stateId = "-1";
                            }
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                    Looper.prepare();
                    Toast.makeText(root.getContext(), LoginActivity.error(cont,response.code()), Toast.LENGTH_SHORT).show();
                    Looper.loop();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Looper.prepare();
                    Toast.makeText(root.getContext(), LoginActivity.error(cont,response.code()), Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
        });
    }

    private void getDocumentTypes(String index){
        OkHttpRequest request = new OkHttpRequest(new OkHttpClient());
        String conexion = "https://healthsenseapi.herokuapp.com/documenttype/"+index;

        request.GET(conexion,new JSONArray(), new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {}

            @Override
            public void onResponse(Call call, Response response) {

                try {
                    String responseData = response.body().string();
                    JSONObject json = new JSONObject(responseData);

                    if(getActivity() == null)
                        return;

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if(getActivity() == null)
                                    return;

                                ((TextView)getActivity().findViewById(R.id.credential_type)).setText(json.getString("name"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                } catch (IOException e) {
                    Looper.prepare();
                    Toast.makeText(getContext(), LoginActivity.error(cont,response.code()), Toast.LENGTH_SHORT).show();
                    Looper.loop();
                } catch (JSONException e) {
                    Looper.prepare();
                    Toast.makeText(getContext(), LoginActivity.error(cont,response.code()), Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
        });
    }

    private void saveInfo(){

    }
}