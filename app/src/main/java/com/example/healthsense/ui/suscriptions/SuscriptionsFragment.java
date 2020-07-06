package com.example.healthsense.ui.suscriptions;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthsense.MainActivity;
import com.example.healthsense.R;
import com.example.healthsense.Resquest.OkHttpRequest;
import com.example.healthsense.Resquest.doAsync;
import com.example.healthsense.data.PikerDate;
import com.example.healthsense.db.AppDatabase;
import com.example.healthsense.db.Repository.MedicRepository;
import com.example.healthsense.db.entity.Medic;
import com.example.healthsense.ui.login.LoginActivity;
import com.mercadopago.android.px.core.MercadoPagoCheckout;
import com.mercadopago.android.px.model.Payment;
import com.mercadopago.android.px.model.exceptions.MercadoPagoError;
import com.mercadopago.core.CustomServer;
import com.mercadopago.model.ApiException;
import com.mercadopago.preferences.CheckoutPreference;
import com.mercadopago.util.JsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

import static android.app.Activity.RESULT_CANCELED;

/*
Se realiza la consulta e inicialización del fragmento que contiene la lista de medicos disponibles para que el usuario se suscriba,
Ademas tambien se inicializa la lista de medicos que aceptaron la suscripción del usuario.
 */
public class SuscriptionsFragment extends Fragment {

    private static final String TAG = "SuscriptionsFragment";

    public static Fragment fg;
    private ArrayList<Integer> calls = new ArrayList<>();
    private View root;
    private ArrayList<String> valuesSpeciality = new ArrayList<>();
    private ArrayList<String> valuesCity = new ArrayList<>();
    private ProgressDialog mProgressDialog;
    private Context cont;
    private ArrayList<View> views = new ArrayList<>();

    private int medicalPayID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_suscriptions, container, false);
        cont = root.getContext();
        fg = this;

        mProgressDialog = new ProgressDialog(root.getContext(), R.style.AppCompatAlertDialogStyle);
        mProgressDialog.setTitle(R.string.loading);
        mProgressDialog.setMessage(getResources().getString(R.string.please_wait));
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        doAsync.execute(new Runnable() {
            @Override
            public void run() {
                getValues(valuesCity, "city/");
            }
        });

        doAsync.execute(new Runnable() {
            @Override
            public void run() {
                getValues(valuesSpeciality, "medicalspeciality/");
            }
        });

        LinearLayout list1 = root.findViewById(R.id.list1);
        root.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if (list1.getVisibility() == View.GONE){
                       list1.setVisibility(View.VISIBLE);
                   }else{
                       list1.setVisibility(View.GONE);
                   }


                   root.findViewById(R.id.llayout1).setBackground(getResources().getDrawable(R.drawable.button_option_press));

                   Handler handler = new Handler();
                   handler.postDelayed(new Runnable() {
                       public void run() {
                           root.findViewById(R.id.llayout1).setBackground(getResources().getDrawable(R.drawable.background_model_training));
                       }
                   }, 170);
               }
           }
        );

        ((TextView) root.findViewById(R.id.count1)).setText("0");

        LinearLayout list2 = root.findViewById(R.id.list2);
        root.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if (list2.getVisibility() == View.GONE){
                       list2.setVisibility(View.VISIBLE);
                   }else{
                       list2.setVisibility(View.GONE);
                   }

                   root.findViewById(R.id.llayout2).setBackground(getResources().getDrawable(R.drawable.button_option_press));

                   Handler handler = new Handler();
                   handler.postDelayed(new Runnable() {
                       public void run() {
                           root.findViewById(R.id.llayout2).setBackground(getResources().getDrawable(R.drawable.background_model_training));
                       }
                   }, 170);
               }
           }
        );

        ((TextView) root.findViewById(R.id.count2)).setText("0");

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                FragmentManager fragmentManager = requireFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, fg).addToBackStack(null).commit();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        return root;
    }


    @Override
    public void onStart() {
        super.onStart();

        //Funciones de regargar y quitar vistas si se realizó el pago
        //getSubscriptions(2);
    }


    /**
     * Creacion de nueva tarjeta con los datos del medico
     * @param root
     * @param medic = datos del medico
     * @param list = lista a la que pertenece la tarjeta
     * @param backgroundColor
     * @param bton = boton asociado (suscribirse o pagar)
     * @param viewVisibility
     */
    private void newSuscription(View root, Medic medic, LinearLayout list, int backgroundColor, Button bton, int viewVisibility) {
        String created = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            created = LocalDate.now().toString();
        }
        //----programación de formato de la lista de trainings

        Space s0 = new Space(root.getContext());
        s0.setMinimumHeight(30);

        LinearLayout ll = new LinearLayout(root.getContext());
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setGravity(Gravity.CENTER_VERTICAL);

        TextView tv1 = new TextView(root.getContext());

        tv1.setText(Html.fromHtml("<b>" + medic.getName() + " " + medic.getLast_name() +
                "</b><br><br> DNI: " + medic.getDocument_number() +
                "<br> Gender: " + medic.getGender() +
                "<br> Speciality: " + valuesSpeciality.get(medic.getMedical_speciality_id()) +
                "<br> Email: " + medic.getEmail() +
                "<br> City: " + valuesCity.get(medic.getCity_id()) +
                "<br> Address: " + medic.getAddress() +
                "<br><br> <b>" + getString(R.string.expires) + " </b>" + medic.getExpires()));


        tv1.setTextColor(root.getResources().getColor(android.R.color.white));
        tv1.setPadding(30, 30, 0, 30);

        ll.addView(tv1);

        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp1.weight = 1;
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp2.weight = 3f;

        //Linearlayout izquierdo
        LinearLayout llL = new LinearLayout(root.getContext());
        llL.setOrientation(LinearLayout.VERTICAL);
        llL.setGravity(Gravity.CENTER_VERTICAL);
        llL.setLayoutParams(lp1);

        llL.addView(ll);

        //Linear layout derecho
        EditText price = new EditText(root.getContext());
        price.setHintTextColor(getResources().getColor(R.color.White));
        price.setBackgroundTintList(ContextCompat.getColorStateList(root.getContext(), R.color.DarkerButton));
        price.setClickable(false);
        price.setEnabled(false);
        price.setText(medic.getPrice());
        price.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        price.setTextColor(getResources().getColor(android.R.color.black));
        price.setVisibility(viewVisibility);

        Space s1 = new Space(root.getContext());
        s1.setMinimumHeight(30);

        LinearLayout llR = new LinearLayout(root.getContext());
        llR.setOrientation(LinearLayout.VERTICAL);
        llR.setGravity(Gravity.CENTER_VERTICAL);
        llR.setLayoutParams(lp2);

        //if (Integer.parseInt(textPriceButton) <= 0)
        //bton.setEnabled(false);

        llR.addView(price);
        llR.addView(s1);
        llR.addView(bton);
        llR.setPadding(0, 30, 30, 30);

        //LAYOUT HORIZONTAL PARA TEXTO Y BOTON
        LinearLayout llSub = new LinearLayout(root.getContext());
        llSub.setOrientation(LinearLayout.HORIZONTAL);
        llSub.setGravity(Gravity.CENTER_VERTICAL);
        llSub.setBackgroundResource(backgroundColor);

        llSub.addView(llL);
        llSub.addView(llR);

        list.addView(s0);
        list.addView(llSub);

        if (list.getId() == root.findViewById(R.id.list2).getId()) {
            views.add(s0);
            views.add(llSub);
        }
        //------fin de programación del formato
    }

    /**
     * Creacion del boton y funcionalidad al clickearlo
     * @param root
     * @param text = mensaje del boton
     * @param fragment
     * @param id = id del personal medico de la tarjeta
     * @param pos = posicion de la tarjeta en la lista
     * @param price = precio por el servicio
     * @param email = email del medico
     * @param name = nombre del medico
     * @return
     */
    private Button createButton(View root, int text, Fragment fragment, int id, int pos, String price, String email, String name) {
        Button bt = new Button(root.getContext());
        bt.setText(text);
        bt.setBackground(getResources().getDrawable(R.drawable.button_state));
        bt.setTextColor(getResources().getColor(android.R.color.white));
        bt.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View v) {
                                      bt.setEnabled(false);

                                      if (text == R.string.suscribe) {
                                          Log.d(TAG, pos + "");
                                          suscribe(id);
                                          ((LinearLayout) root.findViewById(R.id.list2)).removeView(views.get(2 * pos));
                                          ((LinearLayout) root.findViewById(R.id.list2)).removeView(views.get((2 * pos) + 1));

                                          TextView count2 = root.findViewById(R.id.count2);
                                          count2.setText((Integer.parseInt(count2.getText().toString()) - 1) + "");
                                          //Log.d(TAG, ((LinearLayout)root.findViewById(R.id.list2)).get),
                                      } else {
                                          pay(price, id, email, name);
                                      }

                                    }
                              }
        );
        return bt;
    }

    private void finishCall(int i) {
        calls.set(i, 0);
        for (int j = 0; j < calls.size(); j++) {
            if (calls.get(j) == 1)
                return;
        }
        //loadProfile();
        //mProgressDialog.dismiss();
        if (valuesCity.size() > 0) {
            getMedicalList();
        }
    }

    private synchronized int addCall() {
        calls.add(1);
        //Log.d(TAG,calls.size()+"");
        return calls.size() - 1;
    }

    /**
     * Obtencion de la lista de medicos desde backend mediante metodo GET. Luego de obtenerlos y almacenarlos en la base de
     * datos local (si no se encontraban en esta), se ejecuta el metodo para la obtencion de las suscripciones
     */
    private void getMedicalList() {
        OkHttpRequest request = new OkHttpRequest(new OkHttpClient());
        String conexion = MainActivity.PATH + "medicallist/";

        //int numCall = addCall();
        request.GET(conexion, new JSONArray(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                msjToast(e, 4000);
                mProgressDialog.dismiss();
                //finishCall(numCall);
            }

            @Override
            public void onResponse(Call call, Response response) {

                Log.d(TAG, "CONSULTÓ MEDICOS");

                try {
                    String responseData = response.body().string();
                    JSONArray jsonArray = new JSONArray(responseData);

                    LinearLayout list2 = root.findViewById(R.id.list2);

                    int cantSubs = 0;

                    MedicRepository medicRepository = new MedicRepository(getActivity().getApplication());

                    if (jsonArray.length() == AppDatabase.getAppDatabase(getContext()).medicDAO().size()) {
                        addMedicViews(medicRepository.getAll());
                    } else {
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject json = new JSONObject(jsonArray.get(i).toString());

                            if (!medicRepository.contains(json.getInt("medical_personnel_id"))) {
                                int c = (json.getString("city_id").equals("null")) ? 1 : Integer.parseInt(json.getString("city_id"));
                                int s = (json.getString("medical_speciality_id").equals("null")) ? 1 : Integer.parseInt(json.getString("medical_speciality_id"));
                                String add = (json.getString("address").equals("null")) ? "" : json.getString("address");
                                String g = (json.getString("gender").equals("M")) ? "Male" : ((json.getString("gender").equals("F")) ? "Female" : "");

                                Medic medic = new Medic(json.getString("name"), json.getString("last_name"), g, json.getString("email"), json.getString("document_number"), add, c, s, json.getInt("medical_personnel_id"));
                                medic.setExpires("--/--/----");
                                medic.setPrice("-1");
                                medicRepository.insert(medic);

                                int pos = cantSubs;
                                cantSubs++;

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {

                                            newSuscription(root, medic, list2, R.drawable.background_target_waiting_training, createButton(root, R.string.suscribe, null, medic.getMedical_personnel_id(), pos, "0", medic.getEmail(), medic.getFullName()), View.GONE);

                                        } catch (Exception e) {
                                            msjToast(e, response.code());
                                        }
                                    }
                                });
                            }
                        }

                        String cantSubString = cantSubs + "";
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    ((TextView) root.findViewById(R.id.count2)).setText(cantSubString);
                                } catch (Exception e) {
                                    msjToast(e, response.code());
                                }
                            }
                        });
                    }
                    getSubscriptions();

                } catch (IOException | JSONException e) {
                    msjToast(e, response.code());
                }

            }
        });
    }


    /**
     * Se muestran los datos de los medicos ya almacenados en la base de datos local
     * @param medics = Lista de metodos obtenida de la base de datos
     */
    private void addMedicViews(List<Medic> medics) {
        Log.d(TAG, "addMedicViews: muestro ya cargados");
        int cantSubs = 0;
        LinearLayout list2 = root.findViewById(R.id.list2);
        for (Medic medic : medics) {
            int pos = cantSubs;

            if (Integer.parseInt(medic.getPrice()) < 0) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        newSuscription(root, medic, list2, R.drawable.background_target_waiting_training, createButton(root, R.string.suscribe, null, medic.getMedical_personnel_id(), pos, "0", medic.getEmail(), medic.getFullName()), View.GONE);
                    }
                });
                cantSubs++;
            }
        }
        String cantSubsString = cantSubs + "";

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TextView) root.findViewById(R.id.count2)).setText(cantSubsString);
            }
        });

    }


    /**
     * Obtencion mediante metodo GET de la lista de suscripciones. Se realiza la actualizacion de precios y vencimientos
     */
    private void getSubscriptions(){
        OkHttpRequest request = new OkHttpRequest(new OkHttpClient());
        String conexion = MainActivity.PATH + "subscriptions/get";

        Log.d(TAG, "getSubscriptions: ");
        //int numCall = addCall();

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
                msjToast(e, 4000);
                //finishCall(numCall);
            }

            @Override
            public void onResponse(Call call, Response response) {

                try {
                    String responseData = response.body().string();
                    Log.d(TAG, "onResponse: get subscriptions: " + response.code());
                    if (response.code() == 200) {
                        JSONArray jsonArray = new JSONArray(responseData);

                        LinearLayout list1 = root.findViewById(R.id.list1);

                        int cantSubs = 0;

                        MedicRepository medicRepository = new MedicRepository(getActivity().getApplication());

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject json = new JSONObject(jsonArray.get(i).toString());

                            Medic medic;
                            String expires;

                            if (!medicRepository.contains(json.getInt("Medical_Personnel_id"))) {
                                int c = (json.getString("city_id").equals("null")) ? 1 : Integer.parseInt(json.getString("city_id"));
                                int s = (json.getString("medical_speciality_id").equals("null")) ? 1 : Integer.parseInt(json.getString("medical_speciality_id"));
                                String add = (json.getString("address").equals("null")) ? "" : json.getString("address");
                                String g = (json.getString("gender").equals("M")) ? "Male" : ((json.getString("gender").equals("F")) ? "Female" : "");


                                medic = new Medic(json.getString("name"), json.getString("last_name"), g, json.getString("email"), json.getString("document_number"), add, c, s, json.getInt("Medical_Personnel_id"));
                                expires = (json.getString("expires").equals("null")) ? "--/--/----" : PikerDate.Companion.toDateFormatView(json.getString("expires"));
                                medic.setExpires(expires);
                                medic.setPrice(json.get("amount").toString());
                                medicRepository.insert(medic);

                            } else {
                                medic = medicRepository.getMedic(json.getInt("Medical_Personnel_id"));
                                expires = (json.getString("expires").equals("null")) ? "--/--/----" : PikerDate.Companion.toDateFormatView(json.getString("expires"));
                                medic.setExpires(expires);
                                medic.setPrice(json.get("amount").toString());
                                medicRepository.update(medic);
                            }
                            int pos = cantSubs;


                            Log.d(TAG, "onResponse: expires: " + expires);

                            final String textPriceButton = json.get("amount").toString();

                            int visibilidad = (json.getString("expires").equals("null") ? View.VISIBLE : View.VISIBLE);

                            if (Integer.parseInt(textPriceButton) > 0) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {

                                            newSuscription(root, medic, list1, R.drawable.background_model_training_history, createButton(root, R.string.pay, null, medic.getMedical_personnel_id(), pos, textPriceButton, medic.getEmail(), medic.getFullName()), visibilidad);

                                        } catch (Exception e) {
                                            msjToast(e, response.code());
                                        }
                                    }
                                });
                                cantSubs++;
                            }

                        }

                        String cantSubsString = cantSubs + "";
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    ((TextView) root.findViewById(R.id.count1)).setText(cantSubsString);
                                } catch (Exception e) {
                                    msjToast(e, response.code());
                                }
                            }
                        });

//                        getMedicalList();
                    }

                } catch (IOException | JSONException e) {
                    msjToast(e, response.code());
                } finally {
                    mProgressDialog.dismiss();
                    //finishCall(numCall);
                }
            }
        });
    }


    private void getValues(ArrayList arr, String path) {
        OkHttpClient innerClient = new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.MINUTES) // connect timeout
                .writeTimeout(2, TimeUnit.MINUTES) // write timeout
                .readTimeout(2, TimeUnit.MINUTES) // read timeout
                .build();
        OkHttpRequest request = new OkHttpRequest(innerClient);
        String conexion = MainActivity.PATH + path;

        int numCall = addCall();

        request.GET(conexion, new JSONArray(), new Callback() {
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

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json = new JSONObject(jsonArray.get(i).toString());
                        arr.add(json.getString("name"));
                    }

                } catch (IOException | JSONException e) {
                    msjToast(e, response.code());
                } finally {
                    finishCall(numCall);
                }
            }
        });
    }

    /**
     * Metodo POST para registrar la suscricion al servicio de un medico
     * @param id = id del personal medico
     */
    private void suscribe(int id) {
        OkHttpRequest request = new OkHttpRequest(new OkHttpClient());

        Log.d(TAG, "id: " + id);
        JSONObject js = new JSONObject();
        try {
            js.put("medical_personnel_id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JSONArray header = new JSONArray();
        JSONObject jsAux = new JSONObject();
        try {
            jsAux.put("x-access-token", MainActivity.TOKEN);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        header.put(jsAux);

        String conexion = MainActivity.PATH + "subscriptions/subscribe";


        request.POST(conexion, header, js, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                msjToast(e, 4000);
            }

            @Override
            public void onResponse(Call call, Response response) {
                msjToast(null, response.code());
            }
        });
    }


    private void msjToast(Exception e, int code) {
        mProgressDialog.dismiss();
        if (e != null)
            e.printStackTrace();
        Looper.prepare();
        Toast.makeText(root.getContext(), LoginActivity.error(cont, code), Toast.LENGTH_SHORT).show();
        Looper.loop();
    }


    private void pay(String price, int id, String textEmail, String textName) {


        Bundle datosAEnviar = new Bundle();
        datosAEnviar.putString("email", textEmail);
        datosAEnviar.putFloat("price", Float.parseFloat(price));
        datosAEnviar.putString("title", "Workout By " + textName);

        Map<String, Object> preferenceMap = new HashMap<>();
        preferenceMap.put("id", "1");
        preferenceMap.put("title", "Workout By " + textName);
        preferenceMap.put("quantity", 1);
        preferenceMap.put("currency_id", "ARS");
        preferenceMap.put("unit_price", Float.parseFloat(price));
        preferenceMap.put("email", textEmail);

        medicalPayID = id;

        CustomServer.createCheckoutPreference(root.getContext(), "https://mauropuravida.000webhostapp.com", "preference.php", preferenceMap, new com.mercadopago.callbacks.Callback<CheckoutPreference>() {

            @Override
            public void success(CheckoutPreference checkoutPreference) {
                startMercadoPagoCheckout(checkoutPreference.getId());

                //ONLY TESTING
                inicSubscription(id);
            }

            @Override
            public void failure(ApiException apiException) {
                Log.d(TAG, "ERROR EN LA SALIDA: " + apiException.getMessage());
            }
        });
    }



    private static String pk = "TEST-7d36b114-d09c-4884-8b5c-d9e9575533b3";
    private static final int REQUEST_CODE = 1;

    private void startMercadoPagoCheckout(String checkoutPreferenceId) {
        new MercadoPagoCheckout.Builder(pk, checkoutPreferenceId)
                .build()
                .startPayment(root.getContext(), REQUEST_CODE);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == MercadoPagoCheckout.PAYMENT_RESULT_CODE) {
                final Payment payment = (Payment) data.getSerializableExtra(MercadoPagoCheckout.EXTRA_PAYMENT_RESULT);

                //actualizar la subscripcion
                inicSubscription(medicalPayID);
                Looper.prepare();
                Toast.makeText(root.getContext(), "The operation was done" , Toast.LENGTH_SHORT).show();
                Looper.loop();

                //Done!
            } else if (resultCode == RESULT_CANCELED) {
                if (data != null && data.getStringExtra("mercadoPagoError") != null) {
                    MercadoPagoError mercadoPagoError = JsonUtil.getInstance().fromJson(data.getStringExtra("mercadoPagoError"), MercadoPagoError.class);
                    //((TextView) findViewById(R.id.mp_results)).setText("Error: " +  mercadoPagoError.getMessage());
                    //Resolve error in checkout
                } else {
                    //Resolve canceled checkout
                }
            }
        }
    }

    /**
     * Actualizacion de la suscripcion mediante metodo PUT
     * @param id = id del personal medico
     */
    private void inicSubscription(int id) {

        Log.d(TAG, "TESTING MEDICAL " +id);
        JSONArray jarr = new JSONArray();
        try {
            JSONObject jobj = new JSONObject();
            jobj.put("x-access-token", MainActivity.TOKEN);
            jarr.put(jobj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkHttpRequest request = new OkHttpRequest(new OkHttpClient());
        String conexion = MainActivity.PATH + "subscriptions/renew/"+id;

        request.PUT(conexion, jarr, new JSONObject(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                msjToast(e, 4000);
            }

            @Override
            public void onResponse(Call call, Response response) {
                msjToast(null, response.code());
                //reload
            }
        });
    }

}
