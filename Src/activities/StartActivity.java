package com.example.smartliving;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class StartActivity extends AppCompatActivity {

    static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    ImageView firstPageConnectivity = null;
    AnimationDrawable connectivityAnimation = null;

    BluetoothAdapter btAdapter = null;
    BluetoothDevice hc05 = null;
    BluetoothSocket btSocket = null;
    OutputStream TX_data = null;
    InputStream RX_data = null;

    Intent requestBluetoothPermission_intent = null;
    Intent homeMenu_intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        firstPageConnectivity = findViewById(R.id.firstPageConnectivity);
        connectivityAnimation = (AnimationDrawable) firstPageConnectivity.getDrawable();

        new bluetoothConnectionTask().execute();
    }

    private class bluetoothConnectionTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            connectivityAnimation.start();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }


            if (ActivityCompat.checkSelfPermission(StartActivity.this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//                requestBluetoothPermission_intent = new Intent(StartActivity.this, RequestBluetoothPermissionActivity.class);
//                startActivity(requestBluetoothPermission_intent);

                //request BT permission
            }

            btAdapter = BluetoothAdapter.getDefaultAdapter();

            if (btAdapter == null) {
                //device doesn't support BT activity
                System.out.println("BT not supported");
            } else {
                if (!btAdapter.isEnabled()) {
                    //BT is off on mobile device activity
                    System.out.println("BT off");
                }

                Set<BluetoothDevice> all_devices = btAdapter.getBondedDevices();
                if(all_devices.size() > 0) {
                    for (BluetoothDevice currentDevice : all_devices) {
                        System.out.println("Device Name " + currentDevice.getName() + " " + currentDevice.getAddress());
                    }
                    //device not paired activity
                }
            }


            hc05 = btAdapter.getRemoteDevice("00:22:06:01:78:5E");
            System.out.println(hc05.getName());

            int btChecks_counter = 0;
            do {
                try {
                    btSocket = hc05.createRfcommSocketToServiceRecord(mUUID);
                    System.out.println(btSocket);
                    btSocket.connect();
                    System.out.println(btSocket.isConnected());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                btChecks_counter++;
            } while(!btSocket.isConnected() && btChecks_counter < 10);

            //socket not created (HC05 not found) activity

            try {
                TX_data = btSocket.getOutputStream();
                RX_data = btSocket.getInputStream();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                TX_data.write(48);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                RX_data.skip(RX_data.available());

                for(int i = 0; i < 26; i++)
                {
                    byte b = (byte) RX_data.read();
                    System.out.println((char) b);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                btSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            connectivityAnimation.stop();

            homeMenu_intent = new Intent(StartActivity.this, HomeMenuActivity.class);
            startActivity(homeMenu_intent);
        }
    }

}