package com.example.healthsense.data;

import com.example.healthsense.MainActivity;
import com.example.healthsense.R;
import com.example.healthsense.Resquest.OkHttpRequest;
import com.example.healthsense.data.model.LoggedInUser;
import com.example.healthsense.ui.login.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public void login(String username, String password, LoginActivity la) {
        /*
        Se realiza la consulta de datos de logeo al endpoint de backend para validar los datos
        Si la consulta es valida "200" se procede a logear.
         */

        OkHttpRequest request = new OkHttpRequest(new OkHttpClient());

        JSONObject js = new JSONObject();
        try {
            js.put("email", username);
            js.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String conexion = "https://healthsenseapi.herokuapp.com/signin/";


        request.POST(conexion, js,  new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                la.showLoginFailed(R.string.login_failed);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String responseData = response.body().string();
                    JSONObject json = new JSONObject(responseData);
                    MainActivity.TOKEN = json.getString("token");
                    MainActivity.PROFILETYPE = json.getString("type");
                    la.loginAcepted();
                } catch (IOException e) {
                    e.printStackTrace();
                    la.showLoginFailed(R.string.login_failed);
                } catch (JSONException e) {
                    e.printStackTrace();
                    la.showLoginFailed(R.string.login_failed);
                }
            }
        });
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
