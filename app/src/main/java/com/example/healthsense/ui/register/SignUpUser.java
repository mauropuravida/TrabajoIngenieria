package com.example.healthsense.ui.register;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
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
        String[] credentialTypes = new String[]{};

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
                        js.put("last_name", ((EditText) findViewById(R.id.lastname)).getText().toString());
                        js.put("birth_date", PikerDate.Companion.toDateFormat(((TextView) findViewById(R.id.birth_date)).getText().toString()));
                        js.put("gender", "m");
                        js.put("document_number", ((EditText) findViewById(R.id.credential)).getText().toString());
                        js.put("email", ((EditText) findViewById(R.id.email)).getText().toString());
                        js.put("password", ((EditText) findViewById(R.id.password)).getText().toString());
                        js.put("address", "m");
                        js.put("city_id", "1");
                        js.put("document_type", (((Spinner) findViewById(R.id.credential_type)).getSelectedItemId()+1)+"");
                        js.put("weight", "-1");
                        js.put("height", "-1");
                        js.put("insurance_number", "1");
                        js.put("insurance_id", "1");
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

                                } catch (IOException e) {
                                    Looper.prepare();
                                        Toast.makeText(getApplicationContext(), LoginActivity.error(cont,response.code()), Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                } catch (JSONException e) {
                                    Looper.prepare();
                                        Toast.makeText(getApplicationContext(), LoginActivity.error(cont,response.code()), Toast.LENGTH_SHORT).show();
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

    @SuppressLint("WrongConstant")
    private void loginAcepted(){
        //guardar preferencia de tipo de cuenta, medico o paciente
        SharedPreferences preferencesEditor = getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE);
        preferencesEditor.edit().putString(((EditText) findViewById(R.id.email)).getText().toString(), ((EditText) findViewById(R.id.name)).getText().toString()).apply();
        preferencesEditor.edit().putInt(((EditText) findViewById(R.id.email)).getText().toString()+"profile", R.layout.fragment_profile_user).apply();

        //iniciar login
        Intent intent;
        intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("user",((EditText) findViewById(R.id.email)).getText().toString());
        startActivity(intent);
        finish();
    }

    private void getDocumentTypes(Context v){
        OkHttpRequest request = new OkHttpRequest(new OkHttpClient());
        String conexion = "https://healthsenseapi.herokuapp.com/documenttype/";

        request.GET(conexion,new JSONArray(), new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {}

            @Override
            public void onResponse(Call call, Response response) {

                try {
                    String responseData = response.body().string();
                    JSONArray jsonArray = new JSONArray(responseData);

                    Spinner credentialType = findViewById(R.id.credential_type);
                    ArrayList<String> credentialTypes = new ArrayList();//new String[]{"DNI", "OTRO"};

                    for (int i=0;i< jsonArray.length(); i++){

                        JSONObject json = new JSONObject(jsonArray.get(i).toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    credentialTypes.add(json.getString("name"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(v,
                                    R.layout.spinner_color, credentialTypes);
                            arrayAdapter.setDropDownViewResource(R.layout.spinner_color);
                            credentialType.setAdapter(arrayAdapter);
                        }
                    });

                } catch (IOException e) {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), LoginActivity.error(cont,response.code()), Toast.LENGTH_SHORT).show();
                    Looper.loop();
                } catch (JSONException e) {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), LoginActivity.error(cont,response.code()), Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }

        });

    }
}
