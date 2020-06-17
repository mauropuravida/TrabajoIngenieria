package com.example.healthsense.ui.profile;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.example.healthsense.MainActivity;
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
import java.util.LinkedList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class ProfileFragment extends Fragment {

    private Context cont;
    private ArrayList<String> idsCountries= new ArrayList<>();
    private ArrayList<String> idsStates= new ArrayList<>();
    private ArrayList<String> idsCities= new ArrayList<>();

    private ArrayList<String> valuesCity = new ArrayList<>();
    private ArrayList<String> valuesState = new ArrayList<>();
    private ArrayList<String> valuesCountry = new ArrayList<>();
    private ArrayList<String> valuesSpeciality;

    private int stateId = -1;
    private int countryId = -1;
    private boolean first=true;
    private ProgressDialog mProgressDialog;
    private View root;

    //variables para valores de las vistas
    private String name = "-1";
    private String lastname;
    private String email;
    private String document;
    private String docType;
    private String birtdate;
    private int medSpeciality;
    private int gdr;
    private String w="";
    private String h="";
    private int ct;
    private String ddrss="";
    private String nsrnc;
    private LinkedList lnggs = new LinkedList();
    // fin valores de variables

    private ArrayList<Integer> calls = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate((MainActivity.PROFILETYPE.equals("d"))?  R.layout.fragment_profile_user : R.layout.fragment_profile_medical, container, false);
        cont = root.getContext();

        mProgressDialog = ProgressDialog.show(getContext(), getResources().getString(R.string.loading), getResources().getString(R.string.please_wait), false, false);

        Spinner spGender = root.findViewById(R.id.gender);
        ArrayList<String> values = new ArrayList();
        values.add("Other");
        values.add("Male");
        values.add("Female");
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(root.getContext(),
                R.layout.spinner_color, values);
        arrayAdapter2.setDropDownViewResource(R.layout.spinner_color);
        spGender.setAdapter(arrayAdapter2);

        inicSpinnerView(R.id.country);
        inicSpinnerView(R.id.state);
        inicSpinnerView(R.id.city);

        doAsync.execute(new Runnable() {
            @Override
            public void run() {
                //consulta información de usuario
                getInfoUser(root);
            }
        });


        //consulta la lista de provincias para el pais seleccionado
        Spinner sp = root.findViewById(R.id.country);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!first) {
                    mProgressDialog = ProgressDialog.show(getContext(), getResources().getString(R.string.loading), getResources().getString(R.string.please_wait), false, false);
                    inicSpinner(root, MainActivity.PATH+"state/country_id&" + idsCountries.get((int) sp.getSelectedItemId()), idsStates, valuesCountry);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        //consulta la lista de ciudades para la provincia seleccionada
        Spinner spState = root.findViewById(R.id.state);
        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!first) {
                    mProgressDialog = ProgressDialog.show(getContext(), getResources().getString(R.string.loading), getResources().getString(R.string.please_wait), false, false);
                    inicSpinner(root, MainActivity.PATH+"city/state_id&" + idsStates.get((int) spState.getSelectedItemId()), null, valuesState);
                }
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

        if (MainActivity.PROFILETYPE.equals("m")) {
            inicSpinner(root,MainActivity.PATH+"medicalspeciality/", null, valuesSpeciality = new ArrayList<>());
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

    private synchronized int addCall(){
        calls.add(new Integer(1));
        Log.d("CALLS",calls.size()+"");
        return calls.size()-1;
    }

    private int getIndex(int index, ArrayList<String> arr){
        for (int i=0 ;i<arr.size();i++){
            if (arr.get(i).equals(index+""))
                return i;
        }
        return 0;
    }

    private void getInfoUser(View root){
        OkHttpRequest request = new OkHttpRequest(new OkHttpClient());
        String conexion = (MainActivity.PROFILETYPE.equals("d")) ? MainActivity.PATH+"userinfo/" : MainActivity.PATH+"medicalinfo/";

        JSONArray jarr = new JSONArray();
        try {
            JSONObject jobj = new JSONObject();
            jobj.put("x-access-token", MainActivity.TOKEN);
            jarr.put(jobj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("TOKEN ", MainActivity.TOKEN);

        int numCall = addCall();

        request.GET(conexion,jarr, new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                //Toast.makeText(root.getContext(), "ERROR", Toast.LENGTH_SHORT).show();
                finishCall(numCall);
            }

            @Override
            public void onResponse(Call call, Response response) {

                try {
                    String responseData = response.body().string();
                    JSONObject json = new JSONObject(responseData);

                    name = json.getString("name");
                    lastname = json.getString("last_name");
                    email = json.getString("email");
                    document = json.getString("document_number");
                    birtdate = PikerDate.Companion.toDateFormatView(json.getString("birth_date"));

                    if (!json.getString("address").equals("null"))
                        ddrss = json.getString("address");

                    if (!json.getString("weight").equals("null"))
                        w = json.getString("weight");

                    if (!json.getString("height").equals("null"))
                        h = json.getString("height");

                    gdr = (json.getString("gender").equals("null")) ? 0 : ((json.getString("gender").equals("M")) ? 1 : 2);

                    if (MainActivity.PROFILETYPE.equals("m")){
                        medSpeciality = Integer.parseInt(json.getString("medical_speciality_id"))-1;
                    }

                    if (!json.getString("insurance_number").equals("null"))
                        nsrnc = json.getString("insurance_number");

                    ct = Integer.parseInt((json.getString("city_id").equals("null")) ? "1" : json.getString("city_id"));

                    getFK(root,MainActivity.PATH+"city/"+ct,"state_id");

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
                }finally {
                    finishCall(numCall);
                }
            }
        });
    }

    private void finishCall(int i){
        calls.set(i,0);
        for (int j = 0; j< calls.size();j++) {
            if (calls.get(j) == 1)
                return;
        }
        loadProfile();
    }

    private void getFK(View root, String url, String fk){
        OkHttpRequest request = new OkHttpRequest(new OkHttpClient());
        String conexion = url;
        int numCall = addCall();

        request.GET(conexion, new JSONArray(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(root.getContext(), "ERROR", Toast.LENGTH_SHORT).show();
                finishCall(numCall);
            }

            @Override
            public void onResponse(Call call, Response response) {

                try {
                    String responseData = response.body().string();
                    JSONObject json = new JSONObject(responseData);

                    if (fk.equals("state_id")) {
                        stateId = Integer.parseInt(json.getString(fk));
                        inicSpinner(root,MainActivity.PATH+"city/state_id&"+stateId,idsCities, valuesCity);
                        getFK(root,MainActivity.PATH+"state/"+stateId,"country_id");
                    }
                    else {
                        countryId = Integer.parseInt(json.getString(fk));
                        inicSpinner(root,MainActivity.PATH+"country/",idsCountries, valuesCountry);
                        inicSpinner(root,MainActivity.PATH+"state/country_id&"+countryId,idsStates, valuesState);
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
                }finally {
                    finishCall(numCall);
                }
            }
        });
    }

    private void inicUser(View root){

    }

    private void inicSpinnerView(int resourse){
        Spinner sp = root.findViewById(resourse);
        ArrayList<String> values = new ArrayList();
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(root.getContext(),
                R.layout.spinner_color, values);
        arrayAdapter2.setDropDownViewResource(R.layout.spinner_color);
        sp.setAdapter(arrayAdapter2);
    }

    private void inicSpinner(View root, String path, ArrayList arr, ArrayList<String> values){

        OkHttpRequest request = new OkHttpRequest(new OkHttpClient());
        String conexion = path;
        int numCall = addCall();

        request.GET(conexion, new JSONArray(), new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(root.getContext(), "ERROR", Toast.LENGTH_SHORT).show();
                finishCall(numCall);
            }

            @Override
            public void onResponse(Call call, Response response) {

                try {
                    String responseData = response.body().string();
                    JSONArray jsonArray = new JSONArray(responseData);

                    for (int i=0;i< jsonArray.length(); i++){

                        JSONObject json = new JSONObject(jsonArray.get(i).toString());

                        values.add(json.getString("name"));
                        if (arr != null){
                            arr.add(json.getString("id"));
                        }
                        //Log.d("SALIDAVARIABLE",json.getString("name"));
                        //Log.d("SALIDAARREGLO",values.get(values.size()-1));

                    }

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
                finally {
                    finishCall(numCall);
                }
            }
        });
    }

    private void getDocumentTypes(String index){
        OkHttpRequest request = new OkHttpRequest(new OkHttpClient());
        String conexion = MainActivity.PATH+"documenttype/"+index;

        int numCall = addCall();
        request.GET(conexion,new JSONArray(), new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                finishCall(numCall);
            }

            @Override
            public void onResponse(Call call, Response response) {

                try {
                    String responseData = response.body().string();
                    JSONObject json = new JSONObject(responseData);

                    docType = json.getString("name");

                } catch (IOException e) {
                    Looper.prepare();
                    Toast.makeText(getContext(), LoginActivity.error(cont,response.code()), Toast.LENGTH_SHORT).show();
                    Looper.loop();
                } catch (JSONException e) {
                    Looper.prepare();
                    Toast.makeText(getContext(), LoginActivity.error(cont,response.code()), Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }finally {
                    finishCall(numCall);
                }
            }
        });
    }

    private void loadProfile(){
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(root.getContext(), R.layout.spinner_color, valuesCountry);
        arrayAdapter1.setDropDownViewResource(R.layout.spinner_color);

        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(root.getContext(), R.layout.spinner_color, valuesCity);
        arrayAdapter2.setDropDownViewResource(R.layout.spinner_color);

        ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<String>(root.getContext(), R.layout.spinner_color, valuesState);
        arrayAdapter3.setDropDownViewResource(R.layout.spinner_color);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {

                    ((Spinner) root.findViewById(R.id.country)).setAdapter(arrayAdapter1);
                    ((Spinner) root.findViewById(R.id.city)).setAdapter(arrayAdapter2);
                    ((Spinner) root.findViewById(R.id.state)).setAdapter(arrayAdapter3);

                    //inicSpinnerView(R.id.interal_medicine);
                    ((TextView)root.findViewById(R.id.name)).setText(name);
                    ((TextView)root.findViewById(R.id.lastname)).setText(lastname);
                    ((TextView)root.findViewById(R.id.email)).setText(email);
                    ((TextView)root.findViewById(R.id.credential)).setText(document);
                    ((TextView)root.findViewById(R.id.birth_date)).setText(birtdate);
                    ((TextView)root.findViewById(R.id.address)).setText(ddrss);
                    ((TextView)root.findViewById(R.id.weight)).setText(w);
                    ((TextView)root.findViewById(R.id.height)).setText(h);
                    ((TextView)root.findViewById(R.id.insurance_numb)).setText(nsrnc);

                    ((Spinner) root.findViewById(R.id.gender)).setSelection(gdr);

                    if (MainActivity.PROFILETYPE.equals("m")){
                        ((Spinner) root.findViewById(R.id.interal_medicine)).setSelection(medSpeciality);
                    }

                    ((TextView)getActivity().findViewById(R.id.credential_type)).setText(docType);
                    ((Spinner)root.findViewById(R.id.country)).setSelection(countryId-1);
                    ((Spinner)root.findViewById(R.id.state)).setSelection(getIndex(stateId,idsStates));
                    ((Spinner)root.findViewById(R.id.city)).setSelection(getIndex(ct,idsCities));

                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    mProgressDialog.dismiss();
                }
            }
        });
    }

    private void saveInfo(){

    }
}