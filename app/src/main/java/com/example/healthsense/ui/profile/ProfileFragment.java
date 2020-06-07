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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.healthsense.R;
import com.example.healthsense.Resquest.OkHttpRequest;
import com.example.healthsense.Resquest.doAsync;
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
    private ArrayList<String> idsCities;
    private static boolean lock = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        SharedPreferences preferencesEditor = this.getActivity().getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE);
        int profile = preferencesEditor.getInt("profileType", R.layout.fragment_profile_medical);

        View root = inflater.inflate(profile, container, false);
        cont = root.getContext();

        inicSpinner(R.id.country,root,"https://healthsenseapi.herokuapp.com/country/",idsCountries = new ArrayList<>());

        Spinner sp = root.findViewById(R.id.country);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                inicSpinner(R.id.state, root, "https://healthsenseapi.herokuapp.com/state/country_id&" + idsCountries.get((int) sp.getSelectedItemId()), idsStates = new ArrayList<>());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

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

        return root;
    }

    private void inicUser(View root){

    }

    private void inicSpinner(int resourse ,View root, String path, ArrayList arr){
        lock = true;
        Spinner sp = root.findViewById(resourse);
        ArrayList<String> values = new ArrayList();
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(root.getContext(),
                R.layout.spinner_color, values);
        arrayAdapter2.setDropDownViewResource(R.layout.spinner_color);
        sp.setAdapter(arrayAdapter2);

        OkHttpRequest request = new OkHttpRequest(new OkHttpClient());
        String conexion = path;

        request.GET(conexion, new Callback(){
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
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
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

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(root.getContext(),
                                    R.layout.spinner_color, values);
                            arrayAdapter.setDropDownViewResource(R.layout.spinner_color);
                            sp.setAdapter(arrayAdapter);
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
                }finally {
                    lock= false;
                }
            }
        });
    }
}