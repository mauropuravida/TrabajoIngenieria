package com.example.healthsense.ui.device;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.example.healthsense.R;
import org.json.JSONException;
import org.json.JSONObject;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.UUID;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class DeviceFragment extends Fragment implements AdapterView.OnItemClickListener{

    private DeviceViewModel shareViewModel;
    private static final String TAG = "DeviceActivity";
    BluetoothAdapter nBluetoothAdapter;
    Button btnVisibilidad;
    Button btnONOFF;
    public ArrayList<BluetoothDevice> nBTDevices = new ArrayList<>();
    public DeviceListAdapter nDeviceListAdapter;
    ListView lvNewDevices;
    Button btnDescubrir;
    Button btnPlay;
    Button btnSpeed;
    Button btnReady;
    Switch switchOnOff;
    TextView incomingMessage;
    StringBuilder messages;
    Button btnStartConnection;
    EditText editTextSpeed;
    BluetoothConnectionService mBluetoothConnection;
    private static final UUID MY_UUID_INSECURE = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
    BluetoothDevice mBTDevice;
    Button btnStop;
    Button btnJson;
    EditText editTextPulso;
    private View root;
    public ArrayList<JSONObject> arrayJson=new ArrayList<JSONObject>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        shareViewModel =
                ViewModelProviders.of(this).get(DeviceViewModel.class);
        root = inflater.inflate(R.layout.fragment_device, container, false);

        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_devices);
        btnONOFF = (Button) root.findViewById(R.id.btnONOFF);
        // btnVisibilidad = (Button) findViewById(R.id.btnVisibilidad);
        lvNewDevices=(ListView) root.findViewById(R.id.lvNewDevices);
        nBTDevices = new ArrayList<>();
        btnDescubrir = (Button) root.findViewById(R.id.btnFindUnpairedDevices);
        nBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // editTextSpeed = (EditText) findViewById(R.id.editTextSpeed);

        //  final Switch switchOnOff=(Switch) findViewById(R.id.switchOnOff);
        btnStartConnection = (Button) root.findViewById(R.id.btnStartConnection);
        btnStartConnection.setEnabled(false);
      //  btnPlay = (Button) root.findViewById(R.id.btnPlay);
        // btnSpeed = (Button) findViewById(R.id.btnSpeed);
        //btnReady = (Button) root.findViewById(R.id.btnReady);
//        incomingMessage=(TextView)root.findViewById(R.id.MensajeEntrante);
        messages = new StringBuilder();
        LocalBroadcastManager.getInstance(root.getContext()).registerReceiver(mReceiver,  new IntentFilter("MensajeEntrante"));

        //cuando el bond realice cambios
        IntentFilter filter=new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        root.getContext().registerReceiver(nBroadcastReceiver4,filter);
        lvNewDevices.setOnItemClickListener(DeviceFragment.this);
        //PRENDER-APAGAR BLUETOOTH
        btnONOFF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onCLick: prendidendo/apagando");
                enableDisableBT();
                /////
                Log.d(TAG, "btnVisibilidad: Haciendo el dispositivo visible pr 300 segundos");
                Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                startActivity(discoverableIntent);

                IntentFilter intentFilter = new IntentFilter(nBluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
                root.getContext().registerReceiver(nBroadcastReceiver2, intentFilter);
            }
        });

        //VISIBILIDAD
      /*  btnVisibilidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "btnVisibilidad: Haciendo el dispositivo visible pr 300 segundos");
                Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                startActivity(discoverableIntent);

                IntentFilter intentFilter = new IntentFilter(nBluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
                registerReceiver(nBroadcastReceiver2, intentFilter);
            }

        });*/


        // Descubrir
        btnDescubrir.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)

            @Override
            public void onClick(View view) {
                Log.d(TAG, "btnDescubrir:mirar distintos dispositivos");

                if (nBluetoothAdapter.isDiscovering()) {
                    nBluetoothAdapter.cancelDiscovery();
                    Log.d(TAG, "btnDescubrir: cancelar descubrimiento");
                    //chequeo permisos en el manifest
                    checkBTPermissions();
                    nBluetoothAdapter.startDiscovery();
                    IntentFilter descubrirDispositivosIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                    root.getContext().registerReceiver(nBroadcastReceiver3, descubrirDispositivosIntent);
                }
                if (!nBluetoothAdapter.isDiscovering()) {
                    checkBTPermissions();
                    nBluetoothAdapter.startDiscovery();
                    IntentFilter descubrirDispositivosIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                    root.getContext().registerReceiver(nBroadcastReceiver3, descubrirDispositivosIntent);
                }
            }


        });

        btnStartConnection.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                StartConnection();
            }
        });

/*        btnPlay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String play="play";
                byte[] bytes = play.getBytes(Charset.defaultCharset());
                mBluetoothConnection.write(bytes);
            }
        });
        btnReady.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String ready="ready";
                byte[] bytes = ready.getBytes(Charset.defaultCharset());
                mBluetoothConnection.write(bytes);
            }
        });
        btnSpeed.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                byte[] bytes = editTextSpeed.getText().toString().getBytes(Charset.defaultCharset());
                Log.d(TAG,"speed:"+ editTextSpeed.getText());
                mBluetoothConnection.write(bytes);
                //editTextSpeed.setText(0);
            }
        });
        switchOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switchOnOff.isChecked()){
                    String on="on";
                    byte[] bytes = on.getBytes(Charset.defaultCharset());
                    mBluetoothConnection.write(bytes);
                }
                else{
                    String off="off";
                    byte[] bytes = off.getBytes(Charset.defaultCharset());
                    mBluetoothConnection.write(bytes);
                }
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mensaje="Stop";
                byte [] bytes = mensaje.getBytes(Charset.defaultCharset());
                mBluetoothConnection.write(bytes);
            }
        });
        btnJson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject js= new JSONObject();
                try{
                    js.put("Pulso",editTextPulso.getText());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                byte[] bytes= js.toString().getBytes();
                mBluetoothConnection.write(bytes);
                editTextPulso.setText("");
            }
        });*/

        return root;

    }

    //Crear BroadcasteReceiver
    private final BroadcastReceiver nBroadcastReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(nBluetoothAdapter.ACTION_STATE_CHANGED)){
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, nBluetoothAdapter.ERROR);

                switch(state){
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG,"nBroadcastReceiver1: APAGADO");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG,"nBroadcastReceiver1: APAGANDOSE");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG,"nBroadcastReceiver1: PRENDIDO");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG,"nBroadcastReceiver1: PRENDIENDO");
                        break;
                }

            }
        }
    };

    private final BroadcastReceiver nBroadcastReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)){
                int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);

                switch(mode){
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.d(TAG,"nBroadcastReceiver2: DISPOSITIVO VISIBLE");
                        break;
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.d(TAG,"nBroadcastReceiver2: INVISIBLE Y LISTO PARA RECIBIR CONEXIONES");
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.d(TAG,"nBroadcastReceiver2: INVISIBLE Y NO LISTO PARA RECIBIR CONEXIONES");
                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        Log.d(TAG,"nBroadcastReceiver2: CONECTANDO...");
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        Log.d(TAG,"nBroadcastReceiver2: CONECTADO");
                        break;
                }
            }
        }
    };

    private final BroadcastReceiver nBroadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "onReceive: ACTION FOUND");
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                nBTDevices.add(device);
                Log.d(TAG, "onReceive: " + device.getName() + ":" + device.getAddress());
                nDeviceListAdapter = new DeviceListAdapter(context, R.layout.device_adapter_view, nBTDevices);
                lvNewDevices.setAdapter(nDeviceListAdapter);
            }
        }
    };

    private BroadcastReceiver nBroadcastReceiver4 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action= intent.getAction();
            if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //3 casos
                //caso 1: bonded already
                if(mDevice.getBondState()==BluetoothDevice.BOND_BONDED){
                    Log.d(TAG,"BroadcastReceiver: BOND_BONDED");
                    mBTDevice = mDevice;
                }
                //caso 2 : creating a bone
                if(mDevice.getBondState()==BluetoothDevice.BOND_BONDING){
                    Log.d(TAG,"BroadcastReceiver: BOND_BONDING");
                }
                //caso 3 : nreaking a bond
                if(mDevice.getBondState()==BluetoothDevice.BOND_NONE){
                    Log.d(TAG,"BroadcastReceiver: BOND_NONE");
                }
            }
        }
    };


    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String text=intent.getStringExtra("elMensaje");
            if(text.contains("Pulso")){
                try {
                    JSONObject jsonCreado=new JSONObject(text);
                    messages.append("pulso:"+jsonCreado.getInt("Pulso") + "\n");
                    arrayJson.add(jsonCreado);
                } catch (JSONException e) {
                    e.printStackTrace();
                }}
            else
            if(text.contains("stop")){
                //se abre para mostrar ek grafico
                startActivity(new Intent(root.getContext(), Grafica.class));
            }
            else{
                messages.append(text + "\n");
                incomingMessage.setText(messages);}
        }
    };


   /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);
        btnONOFF = (Button) findViewById(root.id.btnONOFF);
        // btnVisibilidad = (Button) findViewById(R.id.btnVisibilidad);
        lvNewDevices=(ListView) findViewById(R.id.lvNewDevices);
        nBTDevices = new ArrayList<>();
        btnDescubrir = (Button) findViewById(R.id.btnFindUnpairedDevices);
        nBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // editTextSpeed = (EditText) findViewById(R.id.editTextSpeed);

        //  final Switch switchOnOff=(Switch) findViewById(R.id.switchOnOff);
        btnStartConnection = (Button) findViewById(R.id.btnStartConnection);
        btnPlay = (Button) findViewById(R.id.btnPlay);
        // btnSpeed = (Button) findViewById(R.id.btnSpeed);
        btnReady = (Button) findViewById(R.id.btnReady);
        incomingMessage=(TextView)findViewById(R.id.MensajeEntrante);
        messages = new StringBuilder();
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,  new IntentFilter("MensajeEntrante"));

        //cuando el bond realice cambios
        IntentFilter filter=new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(nBroadcastReceiver4,filter);
        lvNewDevices.setOnItemClickListener(DeviceActivity.this);
        //PRENDER-APAGAR BLUETOOTH
        btnONOFF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onCLick: prendidendo/apagando");
                enableDisableBT();
                /////
                Log.d(TAG, "btnVisibilidad: Haciendo el dispositivo visible pr 300 segundos");
                Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                startActivity(discoverableIntent);

                IntentFilter intentFilter = new IntentFilter(nBluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
                registerReceiver(nBroadcastReceiver2, intentFilter);
            }
        });

        //VISIBILIDAD
      /*  btnVisibilidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "btnVisibilidad: Haciendo el dispositivo visible pr 300 segundos");
                Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                startActivity(discoverableIntent);

                IntentFilter intentFilter = new IntentFilter(nBluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
                registerReceiver(nBroadcastReceiver2, intentFilter);
            }

        });


        // Descubrir
        btnDescubrir.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)

            @Override
            public void onClick(View view) {
                Log.d(TAG, "btnDescubrir:mirar distintos dispositivos");

                if (nBluetoothAdapter.isDiscovering()) {
                    nBluetoothAdapter.cancelDiscovery();
                    Log.d(TAG, "btnDescubrir: cancelar descubrimiento");
                    //chequeo permisos en el manifest
                    checkBTPermissions();
                    nBluetoothAdapter.startDiscovery();
                    IntentFilter descubrirDispositivosIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                    registerReceiver(nBroadcastReceiver3, descubrirDispositivosIntent);
                }
                if (!nBluetoothAdapter.isDiscovering()) {
                    checkBTPermissions();
                    nBluetoothAdapter.startDiscovery();
                    IntentFilter descubrirDispositivosIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                    registerReceiver(nBroadcastReceiver3, descubrirDispositivosIntent);
                }
            }


        });

        btnStartConnection.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                StartConnection();
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String play="play";
                byte[] bytes = play.getBytes(Charset.defaultCharset());
                mBluetoothConnection.write(bytes);
            }
        });
        btnReady.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String ready="ready";
                byte[] bytes = ready.getBytes(Charset.defaultCharset());
                mBluetoothConnection.write(bytes);
            }
        });
        btnSpeed.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                byte[] bytes = editTextSpeed.getText().toString().getBytes(Charset.defaultCharset());
                Log.d(TAG,"speed:"+ editTextSpeed.getText());
                mBluetoothConnection.write(bytes);
                //editTextSpeed.setText(0);
            }
        });
        switchOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switchOnOff.isChecked()){
                    String on="on";
                    byte[] bytes = on.getBytes(Charset.defaultCharset());
                    mBluetoothConnection.write(bytes);
                }
                else{
                    String off="off";
                    byte[] bytes = off.getBytes(Charset.defaultCharset());
                    mBluetoothConnection.write(bytes);
                }
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mensaje="Stop";
                byte [] bytes = mensaje.getBytes(Charset.defaultCharset());
                mBluetoothConnection.write(bytes);
            }
        });
        btnJson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject js= new JSONObject();
                try{
                    js.put("Pulso",editTextPulso.getText());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                byte[] bytes= js.toString().getBytes();
                mBluetoothConnection.write(bytes);
                editTextPulso.setText("");
            }
        });

    }*/

    public void StartConnection() {
        StartBTConnection(mBTDevice,MY_UUID_INSECURE);
    }


    public void StartBTConnection(BluetoothDevice device, UUID uuid){
        Log.d(TAG, "startBTConnection: Inicializando conexion de bluetooth RFCOM ");
        mBluetoothConnection.startClient(device, uuid);
    }

    public void enableDisableBT(){
        if(nBluetoothAdapter == null){
            Log.d(TAG, "enbleDisableBT: No tenes un adaptador Bluetooth");
        }
        if(!nBluetoothAdapter.isEnabled()){
            Log.d(TAG, "enableDisableBT: prendidendo");
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            root.getContext().registerReceiver(nBroadcastReceiver1, BTIntent);
        }
        if(nBluetoothAdapter.isEnabled()){
            Log.d(TAG, "enableDisableBT: apagando");
            nBluetoothAdapter.disable();
            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            root.getContext().registerReceiver(nBroadcastReceiver1, BTIntent);
        }
    } // habilita bluetooth


    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy:called");
        super.onDestroy();
        root.getContext().unregisterReceiver(nBroadcastReceiver1);
        root.getContext().unregisterReceiver(nBroadcastReceiver2);
        root.getContext(). unregisterReceiver(nBroadcastReceiver3);
        root.getContext(). unregisterReceiver(nBroadcastReceiver4);
    } /// Se destruye los receiver

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        btnStartConnection.setEnabled(true);
        nBluetoothAdapter.cancelDiscovery();
        Log.d(TAG,"onItemClick: el click en el dispositivo");
        String deviceName= nBTDevices.get(i).getName();
        String deviceAddress = nBTDevices.get(i).getAddress();
        Log.d(TAG, "onItemClick: deciveName =" + deviceName);
        Log.d(TAG, "onItemClick: deciveAddress =" + deviceAddress);

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            Log.d(TAG,"Trying to pair with "+ deviceName);
            nBTDevices.get(i).createBond();

            mBTDevice = nBTDevices.get(i);
            mBluetoothConnection =  new  BluetoothConnectionService ( root.getContext());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkBTPermissions() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = root.getContext().checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += root.getContext().checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }
        }else{
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    } // chekea version de android con permisos


}




