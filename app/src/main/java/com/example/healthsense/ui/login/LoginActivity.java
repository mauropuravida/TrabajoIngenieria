package com.example.healthsense.ui.login;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.healthsense.MainActivity;
import com.example.healthsense.R;
import com.example.healthsense.Resquest.OkHttpRequest;
import com.example.healthsense.Resquest.doAsync;
import com.example.healthsense.data.PikerDate;
import com.example.healthsense.db.Repository.DeviceUsersRepository;
import com.example.healthsense.db.Repository.MedicalPersonnelRepository;
import com.example.healthsense.db.Repository.UserRepository;
import com.example.healthsense.db.entity.DeviceUsers;
import com.example.healthsense.db.entity.MedicalPersonnel;
import com.example.healthsense.db.entity.Users;
import com.example.healthsense.ui.password.Password;
import com.example.healthsense.ui.register.ChoiseProfile;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

import static com.example.healthsense.MainActivity.PREFS_FILENAME;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private LoginActivity act;
    private UserRepository userRepository;
    private  DeviceUsersRepository deviceUsersRepository;
    private MedicalPersonnelRepository medicalPersonnelRepository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_login);
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        userRepository = new UserRepository(getApplication());
        deviceUsersRepository = new DeviceUsersRepository(getApplication());
        medicalPersonnelRepository = new MedicalPersonnelRepository(getApplication());


        act = this;
        isLoged();

        final Button loginButton = findViewById(R.id.login);
        final Button registerButton = findViewById(R.id.register);

        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
       /* passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString(), act);
                    System.out.println("ENTRO EN EL PASSWORD");
                }
                return false;
            }
        });*/

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setEnabled(false);
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString(),act);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent intent;
                  intent = new Intent(getApplicationContext(), ChoiseProfile.class);
                  startActivity(intent);
              }
          }
        );

        findViewById(R.id.forgot_pass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getApplicationContext(), Password.class);
                startActivity(intent);
            }
        });
        //loginAcepted();
    }

    private void isLoged() {
        SharedPreferences preferencesEditor = getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE);
        String user = preferencesEditor.getString("User", "");
        String pass = preferencesEditor.getString("Pass", "");

        Log.d(user,pass);
        if (!user.equals("")) {
            findViewById(R.id.loading).setVisibility(View.VISIBLE);
            loginViewModel.login(user, pass, act);
        }
    }

    public void loginAcepted(){
        SharedPreferences preferencesEditor = getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE);
        CheckBox cb = findViewById(R.id.rememberme);
        if (cb.isChecked()) {
            if (!((EditText)findViewById(R.id.username)).getText().toString().equals("")) {
                //Guardar datos de logeo, primera vez que se logea
                preferencesEditor.edit().putString("User", ((EditText) findViewById(R.id.username)).getText().toString()).apply();
                preferencesEditor.edit().putString("Pass", ((EditText) findViewById(R.id.password)).getText().toString()).apply();
            }
        }

        runOnUiThread(
            new Runnable() {
                @Override
                public void run() {
                    findViewById(R.id.login).setEnabled(true);
                }
            }
        );

        //Si el usuario no esta en el Serverless lo agrego.
        String user = (preferencesEditor.getString("User","").equals("")) ? ((EditText) findViewById(R.id.username)).getText().toString() : preferencesEditor.getString("User","");
        if( !userRepository.exist(user)){
            doAsync.execute(new Runnable() {
                @Override
                public void run() {
                    getUserInformation();
                }
            });
        }
        else
            System.out.println("El usuario existe");


        Intent intent;
        intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("user", user);
        startActivity(intent);
        //Terminar activity de login.
        finish();
    }

    /**
     * Call backend para obtener informacion del usuario y alta de user en base local.
     */
    private void getUserInformation() {
        //call al backend y llamada al addUser
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

        request.GET(conexion, jarr, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = null;
                try {
                    responseData = response.body().string();
                    JSONObject json = new JSONObject(responseData);
                    String name = json.getString("name");
                    String lastname = json.getString("last_name");
                    String email = json.getString("email");
                    String document = json.getString("document_number");
                    String birtdate = PikerDate.Companion.toDateFormatView(json.getString("birth_date"));
                    String password =  ((EditText) findViewById(R.id.password)).getText().toString();
                    int dType = json.getInt("document_type_id");
                    Users user_to_insert = new Users(name, lastname,birtdate,dType,document,email,password);
                    userRepository.insert(user_to_insert);
                    int id = userRepository.getId(user_to_insert.getEmail());
                    while (id==0){
                        id = userRepository.getId(user_to_insert.getEmail());
                    }
                    if (MainActivity.PROFILETYPE.equals("d")){
                        DeviceUsers device_user_to_insert = new DeviceUsers(id);
                        deviceUsersRepository.insert(device_user_to_insert);
                    }else{
                        int speciality = json.getInt("medical_speciality_id");
                        MedicalPersonnel medicalPersonnel_to_insert = new MedicalPersonnel(id,speciality);
                        medicalPersonnelRepository.insert(medicalPersonnel_to_insert);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


    }

    public void showLoginFailed(@StringRes Integer errorString) {
        runOnUiThread(
            new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
                    findViewById(R.id.login).setEnabled(true);
                    findViewById(R.id.loading).setVisibility(View.INVISIBLE);
                }
            }
        );

    }

    @Override
    public void onBackPressed(){
        //no hacer nada si se está en login.
    }

    public static String error(Context v,int code){
        switch(code) {
            case 200:
                return v.getString(R.string.e200);
            case 401:
                return v.getString(R.string.e401);
            case 402:
                return v.getString(R.string.e402);
            case 403:
                return v.getString(R.string.e403);
            case 404:
                return v.getString(R.string.e404);
            case 405:
                return v.getString(R.string.e405);
            case 406:
                return v.getString(R.string.e406);
            case 407:
                return v.getString(R.string.e407);
            case 410:
                return v.getString(R.string.e410);
            case 411:
                return v.getString(R.string.e411);
            case 412:
                return v.getString(R.string.e412);
            case 413:
                return v.getString(R.string.e413);
            case 414:
                return v.getString(R.string.e414);
            case 415:
                return v.getString(R.string.e415);
            case 416:
                return v.getString(R.string.e416);
            case 417:
                return v.getString(R.string.e417);
            case 500:
                return v.getString(R.string.e500);
            case 4000:
                return v.getString(R.string.e4000);
            default:
                return v.getString(R.string.eGeneric);
        }
    }
}
