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
import android.widget.EditText;
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
    private ArrayList<String> valuesLanguage = new ArrayList<>();
    private ArrayList<String> valuesInsurance = new ArrayList<>();
    private ArrayList<String> valuesSpeciality;

    private int stateId = -1;
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
    private int nsrncID;
    private LinkedList lnggs = new LinkedList();
    private long countryActual;
    private boolean actualize = false;
    // fin valores de variables

    private String dType;

    private ArrayList<Integer> calls = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate((MainActivity.PROFILETYPE.equals("d"))?  R.layout.fragment_profile_user : R.layout.fragment_profile_medical, container, false);
        cont = root.getContext();

        mProgressDialog = new ProgressDialog(root.getContext(),R.style.AppCompatAlertDialogStyle);
        mProgressDialog.setTitle(R.string.loading);
        mProgressDialog.setMessage(getResources().getString(R.string.please_wait));
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        Spinner spGender = root.findViewById(R.id.gender);
        ArrayList<String> values = new ArrayList();
        values.add("Other");
        values.add("Male");
        values.add("Female");
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(root.getContext(),
                R.layout.spinner_color, values);
        arrayAdapter2.setDropDownViewResource(R.layout.spinner_color);
        spGender.setAdapter(arrayAdapter2);

        doAsync.execute(new Runnable() {
            @Override
            public void run() {
                //consulta información de usuario
                getInfoUser(root);
            }
        });

        doAsync.execute(new Runnable() {
            @Override
            public void run() {
                //consulta idiomas
                inicSpinner(root,MainActivity.PATH+"language/", null, valuesLanguage);
            }
        });

        //consulta la lista de provincias para el pais seleccionado
        Spinner sp = root.findViewById(R.id.country);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp.getSelectedItemId() != countryActual-1) {
                    //Log.d("SALIDA COUNTRY", sp.getSelectedItemId()+" "+(countryActual-1));
                    mProgressDialog.show();
                    countryActual = sp.getSelectedItemId()+1;
                    //inicSpinner(root, MainActivity.PATH+"state/country_id&" + idsCountries.get((int) sp.getSelectedItemId()), idsStates, valuesCountry);
                    inicSpinner(root,MainActivity.PATH+"state/country_id&"+countryActual, idsStates = new ArrayList<>(), valuesState = new ArrayList<>());
                    actualize = true;
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
                if(spState.getSelectedItemId() != getIndex(stateId,idsStates) || actualize) {
                    //Log.d("SALIDA STATE", spState.getSelectedItemId()+" "+(stateId));
                    mProgressDialog.show();
                    stateId =  Integer.parseInt(idsStates.get((int)spState.getSelectedItemId())); //getIndex((int)spState.getSelectedItemId(),idsStates);
                    //Log.d("MUESTRA DE stateId", stateId+"");
                    inicSpinner(root,MainActivity.PATH+"city/state_id&"+stateId, idsCities = new ArrayList<>(), valuesCity = new ArrayList<>());
                    actualize = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        if (MainActivity.PROFILETYPE.equals("m")) {
            inicSpinner(root,MainActivity.PATH+"medicalspeciality/", null, valuesSpeciality = new ArrayList<>());
        }

        root.findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInfo();
            }
        });

        return root;
    }

    private synchronized int addCall(){
        calls.add(1);
        //Log.d("CALLS",calls.size()+"");
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

        int numCall = addCall();

        request.GET(conexion,jarr, new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                msjToast(e,4000);
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

                    if (MainActivity.PROFILETYPE.equals("m")){
                        medSpeciality = Integer.parseInt(json.getString("medical_speciality_id"))-1;
                    }
                    else{
                        if (!json.getString("weight").equals("null"))
                            w = json.getString("weight");

                        if (!json.getString("height").equals("null"))
                            h = json.getString("height");

                        gdr = (json.getString("gender").equals("null") || json.getString("gender").equals("O")) ? 0 : ((json.getString("gender").equals("M")) ? 1 : 2);

                        if (!json.getString("insurance_number").equals("null"))
                            nsrnc = json.getString("insurance_number");

                        nsrncID = Integer.parseInt(json.getString("insurance_id"))-1;

                        //consulta valores de obras socuales
                        doAsync.execute(new Runnable() {
                            @Override
                            public void run() {
                                inicSpinner(root, MainActivity.PATH + "insurance/", null, valuesInsurance);
                            }
                        });
                    }

                    ct = Integer.parseInt((json.getString("city_id").equals("null")) ? "1" : json.getString("city_id"));

                    dType = json.getString("document_type_id");

                    //consulta tipos de documentos
                    doAsync.execute(new Runnable() {
                        @Override
                        public void run() {
                            getDocumentTypes(dType);
                        }
                    });

                    getFK(root,MainActivity.PATH+"city/"+ct,"state_id");


                } catch (IOException e) {
                    msjToast(e,response.code());
                } catch (JSONException e) {
                    msjToast(e,response.code());
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
                msjToast(e,4000);
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
                        countryActual = Integer.parseInt(json.getString(fk));
                        inicSpinner(root,MainActivity.PATH+"country/",idsCountries, valuesCountry);
                        inicSpinner(root,MainActivity.PATH+"state/country_id&"+countryActual,idsStates, valuesState);
                    }

                } catch (IOException e) {
                    msjToast(e,response.code());
                } catch (JSONException e) {
                    msjToast(e,response.code());
                }finally {
                    finishCall(numCall);
                }
            }
        });
    }

    private void inicSpinner(View root, String path, ArrayList<String> arr, ArrayList<String> values){

        OkHttpRequest request = new OkHttpRequest(new OkHttpClient());
        String conexion = path;
        int numCall = addCall();

        request.GET(conexion, new JSONArray(), new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                msjToast(e, 4000);
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

                            //Log.d("SALIDAARREGLO",arr.get(i)+"  "+ values.get(values.size()-1));
                        }
                        //Log.d("SALIDAVARIABLE",json.getString("name"));
                    }

                } catch (IOException e) {
                    msjToast(e,response.code());
                } catch (JSONException e) {
                    msjToast(e,response.code());
                }
                finally {
                    finishCall(numCall);
                }
            }
        });
    }

    private void msjToast(Exception e, int code){
        mProgressDialog.dismiss();
        e.printStackTrace();
        Looper.prepare();
        Toast.makeText(root.getContext(), LoginActivity.error(cont,code), Toast.LENGTH_SHORT).show();
        Looper.loop();
    }


    private void getDocumentTypes(String index){
        OkHttpRequest request = new OkHttpRequest(new OkHttpClient());
        String conexion = MainActivity.PATH+"documenttype/"+index;

        int numCall = addCall();
        request.GET(conexion,new JSONArray(), new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                msjToast(e,4000);
                finishCall(numCall);
            }

            @Override
            public void onResponse(Call call, Response response) {

                try {
                    String responseData = response.body().string();
                    JSONObject json = new JSONObject(responseData);

                    docType = json.getString("name");

                } catch (IOException e) {
                    msjToast(e,response.code());
                } catch (JSONException e) {
                    msjToast(e,response.code());
                }finally {
                    finishCall(numCall);
                }
            }
        });
    }

    private void loadProfile(){
        calls = new ArrayList<>();

        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(root.getContext(), R.layout.spinner_color, valuesCountry);
        arrayAdapter1.setDropDownViewResource(R.layout.spinner_color);

        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(root.getContext(), R.layout.spinner_color, valuesCity);
        arrayAdapter2.setDropDownViewResource(R.layout.spinner_color);

        ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<String>(root.getContext(), R.layout.spinner_color, valuesState);
        arrayAdapter3.setDropDownViewResource(R.layout.spinner_color);

        ArrayList<StateV0> listVOs = new ArrayList<>();

        StateV0 st = new StateV0();
        st.setTitle(getResources().getString(R.string.language));
        st.setSelected(false);
        listVOs.add(st);

        for (int i = 0; i < valuesLanguage.size(); i++) {
            StateV0 stateVO = new StateV0();
            stateVO.setTitle(valuesLanguage.get(i));
            stateVO.setSelected(false);
            listVOs.add(stateVO);
        }

        MyAdapter myAdapter = new MyAdapter(root.getContext(), R.layout.spinner_color, listVOs);

        ArrayAdapter<String> arrayAdapter4 = new ArrayAdapter<String>(root.getContext(), R.layout.spinner_color, valuesInsurance);
        arrayAdapter4.setDropDownViewResource(R.layout.spinner_color);

        ArrayAdapter<String> arrayAdapter5 = new ArrayAdapter<String>(root.getContext(), R.layout.spinner_color, valuesSpeciality);
        arrayAdapter5.setDropDownViewResource(R.layout.spinner_color);


        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {

                    ((Spinner) root.findViewById(R.id.country)).setAdapter(arrayAdapter1);
                    ((Spinner) root.findViewById(R.id.city)).setAdapter(arrayAdapter2);
                    ((Spinner) root.findViewById(R.id.state)).setAdapter(arrayAdapter3);
                    ((Spinner) root.findViewById(R.id.language)).setAdapter(myAdapter);

                    ((TextView)root.findViewById(R.id.name)).setText(name);
                    ((TextView)root.findViewById(R.id.lastname)).setText(lastname);
                    ((TextView)root.findViewById(R.id.email)).setText(email);
                    ((TextView)root.findViewById(R.id.credential)).setText(document);
                    ((TextView)root.findViewById(R.id.birth_date)).setText(birtdate);
                    ((TextView)root.findViewById(R.id.address)).setText(ddrss);

                    if (MainActivity.PROFILETYPE.equals("m")){
                        ((Spinner) root.findViewById(R.id.interal_medicine)).setAdapter(arrayAdapter5);
                        ((Spinner) root.findViewById(R.id.interal_medicine)).setSelection(medSpeciality);
                    }else{
                        ((Spinner) root.findViewById(R.id.insurance_id)).setAdapter(arrayAdapter4);
                        ((TextView)root.findViewById(R.id.weight)).setText(w);
                        ((TextView)root.findViewById(R.id.height)).setText(h);
                        ((TextView)root.findViewById(R.id.insurance_numb)).setText(nsrnc);
                        ((Spinner) root.findViewById(R.id.gender)).setSelection(gdr);
                        ((Spinner)root.findViewById(R.id.insurance_id)).setSelection(nsrncID);
                    }

                    ((TextView)getActivity().findViewById(R.id.credential_type)).setText(docType);
                    ((Spinner)root.findViewById(R.id.country)).setSelection((int)(countryActual-1));
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
        mProgressDialog.show();

        String path = (MainActivity.PROFILETYPE.equals("m"))? "medicalinfo/" : "userinfo/";

        String gender;
        if (((Spinner) root.findViewById(R.id.gender)).getSelectedItemId() == 0)
            gender = "O";
        else {
            if (((Spinner) root.findViewById(R.id.gender)).getSelectedItemId() == 1)
                gender = "M";
            else
                gender = "F";
        }

        JSONObject js = new JSONObject();

        try {
            js.put("name", ((EditText)root.findViewById(R.id.name)).getText().toString());
            js.put("last_name", ((EditText)root.findViewById(R.id.lastname)).getText().toString());
            js.put("birth_date", PikerDate.Companion.toDateFormat(((EditText)root.findViewById(R.id.birth_date)).getText().toString()));
            js.put("gender", gender);
            js.put("document_type", dType);
            js.put("document_number", ((EditText)root.findViewById(R.id.credential)).getText().toString());
            js.put("email", ((EditText)root.findViewById(R.id.email)).getText().toString());
            js.put("password", "");
            js.put("city_id", idsCities.get((int) ((Spinner) root.findViewById(R.id.city)).getSelectedItemId()));
            js.put("address", ((EditText)root.findViewById(R.id.address)).getText().toString());

            if (MainActivity.PROFILETYPE.equals("m")){
                js.put("medical_speciality", (((Spinner) root.findViewById(R.id.interal_medicine)).getSelectedItemId()+1)+"");
            }else{
                js.put("weight", ((EditText)root.findViewById(R.id.weight)).getText().toString());
                js.put("height", ((EditText)root.findViewById(R.id.height)).getText().toString());
                js.put("insurance_id", (((Spinner) root.findViewById(R.id.insurance_id)).getSelectedItemId()+1)+"");
                js.put("insurance_number", ((EditText)root.findViewById(R.id.insurance_numb)).getText().toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("TESTING ",js.toString());

        JSONArray jarr = new JSONArray();
        try {
            JSONObject jobj = new JSONObject();
            jobj.put("x-access-token", MainActivity.TOKEN);
            jarr.put(jobj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkHttpRequest request = new OkHttpRequest(new OkHttpClient());
        String conexion = MainActivity.PATH + path;

        request.PUT(conexion, jarr, js, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mProgressDialog.dismiss();
                msjToast(e, 4000);
            }

            @Override
            public void onResponse(Call call, Response response) {
                mProgressDialog.dismiss();
                Looper.prepare();
                Toast.makeText(root.getContext(), LoginActivity.error(cont,response.code()), Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        });
    }
}