package com.example.healthsense.ui.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Iterator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

import static com.example.healthsense.MainActivity.PREFS_FILENAME;

public class SignUpUser extends AppCompatActivity {

    private Context cont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_user);

        Spinner credentialType = findViewById(R.id.credential_type);
        String[] credentialTypes = new String[]{};//new String[]{"DNI", "OTRO"};

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_color, credentialTypes);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_color);
        credentialType.setAdapter(arrayAdapter);

        getDocumentTypes(this);


        TextView birthDate = findViewById(R.id.birth_date);
        TextView signIn = findViewById(R.id.sign_button);

        cont = this;

        birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PikerDate(cont).obtenerFecha(birthDate);
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((EditText) findViewById(R.id.password)).getText().toString().equals(((EditText) findViewById(R.id.confirm_password)).getText().toString())) {

                    OkHttpRequest request = new OkHttpRequest(new OkHttpClient());
                    String conexion = "https://healthsenseapi.herokuapp.com/signupuser/";

                    try {
                        JSONObject js = new JSONObject();
                        js.put("name", ((EditText) findViewById(R.id.name)).getText().toString());
                        js.put("lastname", ((EditText) findViewById(R.id.lastname)).getText().toString());
                        js.put("birth_date", PikerDate.Companion.toDateFormat(((TextView) findViewById(R.id.birth_date)).getText().toString()));
                        js.put("gender", "");
                        js.put("document_number", ((EditText) findViewById(R.id.credential)).getText().toString());
                        js.put("email", ((EditText) findViewById(R.id.email)).getText().toString());
                        js.put("password", ((EditText) findViewById(R.id.password)).getText().toString());
                        js.put("address", "");
                        js.put("city_id", "");
                        js.put("document_type", ((Spinner) findViewById(R.id.credential_type)).getSelectedItem().toString());
                        js.put("weight", "");
                        js.put("height", "");
                        js.put("insurance_number", "");
                        js.put("insurance_id", "");
                        request.POST(conexion, js, new Callback() {

                            @Override
                            public void onFailure(Call call, IOException e) {
                                Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(Call call, Response response) {
                                try {
                                    String responseData = response.body().string();
                                    JSONObject json = new JSONObject(responseData);
                                    MainActivity.TOKEN = json.getString("token");
                                    loginAcepted();
                                    finish();
                                } catch (IOException e) {
                                    Looper.prepare();
                                        Toast.makeText(getApplicationContext(), getString(R.string.unknow_error), Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                } catch (JSONException e) {
                                    Looper.prepare();
                                        Toast.makeText(getApplicationContext(), error(response.code()), Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    String msg = getString(R.string.password_fields);
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void loginAcepted(){
        Intent intent;
        intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void getDocumentTypes(Context v){
        OkHttpRequest request = new OkHttpRequest(new OkHttpClient());
        String conexion = "https://healthsenseapi.herokuapp.com/documenttype/";

        request.GET(conexion, new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) {

                try {
                    String responseData = response.body().string();
                    JSONObject json = new JSONObject(responseData);
                    Iterator<String> keys = json.keys();

                    Spinner credentialType = findViewById(R.id.credential_type);
                    String[] credentialTypes = new String[]{};//new String[]{"DNI", "OTRO"};

                    int index = 0;
                    while(keys.hasNext()) {
                        String key = keys.next();
                        if (json.get(key) instanceof JSONObject) {
                            credentialTypes[index] = json.getString(key);
                        }
                        index++;
                    }

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(v,
                            R.layout.spinner_color, credentialTypes);
                    arrayAdapter.setDropDownViewResource(R.layout.spinner_color);
                    credentialType.setAdapter(arrayAdapter);

                } catch (IOException e) {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), error(response.code()), Toast.LENGTH_SHORT).show();
                    Looper.loop();
                } catch (JSONException e) {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), error(response.code()), Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }

        });

    }

    private String error(int code){
        switch(code) {
            case 200:
                return getString(R.string.unknow_error);
            case 401:
                return getString(R.string.unknow_error);
            case 402:
                return getString(R.string.unknow_error);
            case 403:
                return getString(R.string.unknow_error);
            case 404:
                return getString(R.string.unknow_error);
            case 405:
                return getString(R.string.unknow_error);
            case 406:
                return getString(R.string.user_not_exist);
            case 407:
                return getString(R.string.unknow_error);
            case 410:
                return getString(R.string.unknow_error);
            case 411:
                return getString(R.string.empty_fields_error);
            case 412:
                return getString(R.string.unknow_error);
            case 413:
                return getString(R.string.date_format_error);
            case 414:
                return getString(R.string.unknow_error);
            case 415:
                return getString(R.string.email_format_error);
            case 416:
                return getString(R.string.unknow_error);
            case 417:
                return getString(R.string.unknow_error);
            case 500:
                return getString(R.string.unknow_error);
            default:
                return getString(R.string.unknow_error);
        }
    }
}
