package com.example.healthsense.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        SharedPreferences preferencesEditor = this.getActivity().getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE);
        int profile = preferencesEditor.getInt("profileType", R.layout.fragment_profile_medical);

        View root = inflater.inflate(profile, container, false);

        Spinner credentialType = root.findViewById(R.id.credential_type);
        String[] credentialTypes = new String[]{"DNI", "OTRO"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(root.getContext(),
                R.layout.spinner_color, credentialTypes);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_color);
        credentialType.setAdapter(arrayAdapter);

        Spinner city = root.findViewById(R.id.city);
        String[] cities = new String[]{"Tandil", "Necochea", "Azul", "Mal del plata", "Miramar"};
        ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<>(root.getContext(),
                R.layout.spinner_color, cities);
        arrayAdapter3.setDropDownViewResource(R.layout.spinner_color);
        city.setAdapter(arrayAdapter3);

        TextView birthDate = root.findViewById(R.id.birth_date);

        cont = root.getContext();

        birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PikerDate(cont).obtenerFecha(birthDate);
            }
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
            inicMedicalUser(root);
        } else {
            inicUser(root);
        }

        return root;
    }

    private void inicUser(View root){

    }

    private void inicMedicalUser(View root){
        Spinner interarlMedicine = root.findViewById(R.id.interal_medicine);
        String[] interarlMedicines = new String[]{};
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(root.getContext(),
                R.layout.spinner_color, interarlMedicines);
        arrayAdapter2.setDropDownViewResource(R.layout.spinner_color);
        interarlMedicine.setAdapter(arrayAdapter2);

        OkHttpRequest request = new OkHttpRequest(new OkHttpClient());
        String conexion = "https://healthsenseapi.herokuapp.com/medicalspeciality/";

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

                    Spinner medicalSpeciality = root.findViewById(R.id.interal_medicine);
                    ArrayList<String> medicalSpecialities = new ArrayList();

                    for (int i=0;i< jsonArray.length(); i++){

                        JSONObject json = new JSONObject(jsonArray.get(i).toString());
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    medicalSpecialities.add(json.getString("name"));
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
                                    R.layout.spinner_color, medicalSpecialities);
                            arrayAdapter.setDropDownViewResource(R.layout.spinner_color);
                            medicalSpeciality.setAdapter(arrayAdapter);
                        }
                    });

                } catch (IOException e) {
                    Looper.prepare();
                    Toast.makeText(root.getContext(), LoginActivity.error(cont,response.code()), Toast.LENGTH_SHORT).show();
                    Looper.loop();
                } catch (JSONException e) {
                    Looper.prepare();
                    Toast.makeText(root.getContext(), LoginActivity.error(cont,response.code()), Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }

        });

    }
}