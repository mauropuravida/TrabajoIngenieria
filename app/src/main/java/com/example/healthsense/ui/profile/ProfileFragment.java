package com.example.healthsense.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.healthsense.R;
import com.example.healthsense.data.PikerDate;

import java.util.ArrayList;

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

        //para mensajes en los layout
        // String msg = getString(R.string.pre_msg) + (checkedTextView.isChecked() ? getString(R.string.checked) : getString(R.string.unchecked));
        //Toast.makeText(root.getContext(), msg, Toast.LENGTH_SHORT).show();

        //CheckedTextView languages = root.findViewById(R.id.checkedTextView);

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
        String[] interarlMedicines = new String[]{"Traumatologo", "Clínico", "Cardioaco", "Respiratorio", "Kinesiologo"};
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(root.getContext(),
                R.layout.spinner_color, interarlMedicines);
        arrayAdapter2.setDropDownViewResource(R.layout.spinner_color);
        interarlMedicine.setAdapter(arrayAdapter2);
    }
}