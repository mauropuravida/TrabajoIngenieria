package com.example.healthsense.ui.login;

import android.app.Activity;
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
import com.example.healthsense.Resquest.*;
import com.example.healthsense.ui.password.Password;
import com.example.healthsense.ui.register.ChoiseProfile;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_login);
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);

        doAsync.execute(
                new Runnable() {
                    @Override
                    public void run() {
                        isLoged();
                    }
                }
        );

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

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                CheckBox cb = findViewById(R.id.rememberme);
                if (cb.isChecked()) {
                    SharedPreferences preferencesEditor = getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE);
                    //Guardar datos de logeo, primera vez que se logea
                    preferencesEditor.edit().putString("User", usernameEditText.getText().toString()).apply();
                    preferencesEditor.edit().putString("Pass", passwordEditText.getText().toString()).apply();
                }

                //Terminar activity de login.
                finish();
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
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
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
    }

    private Call isLoged() {

        OkHttpRequest request = new OkHttpRequest(new OkHttpClient());
        SharedPreferences preferencesEditor = getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE);
        String user = preferencesEditor.getString("User", "");
        String pass = preferencesEditor.getString("Pass", "");

        JSONObject js = new JSONObject();
        try {
            js.put("email", user);
            js.put("password", pass);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String conexion = "https://healthsenseapi.herokuapp.com/signin/";

        return request.POST(conexion, js,  new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String responseData = response.body().string();
                    JSONObject json = new JSONObject(responseData);
                    MainActivity.TOKEN = json.getString("token");
                    loginAcepted();

                    MainActivity.TOKEN = json.getString("token");
                    loginAcepted();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loginAcepted(){
        Intent intent;
        intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void updateUiWithUser(LoggedInUserView model) {
        //Redireccionar a home
        loginAcepted();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed(){
        //no hacer nada si se está en login.
    }

    public static String error(Context v,int code){
        switch(code) {
            case 200:
                return "";
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
            default:
                return v.getString(R.string.eGeneric);
        }
    }
}
