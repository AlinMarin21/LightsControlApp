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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class StartActivity extends AppCompatActivity {

    static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    static final int BLUETOOTH_OK = 0;
    static final int BLUETOOTH_PERMISSION_NOT_GRANTED = 1;
    static final int BLUETOOTH_NOT_SUPPORTED = 2;
    static final int BLUETOOTH_NOT_ENABLED = 3;
    static final int DEVICE_NOT_PAIRED = 4;
    static final int CONNECTION_NOT_ESTABLISHED = 5;

    int bluetooth_action_result = BLUETOOTH_OK;

    ImageView firstPageConnectivity = null;
    ImageView firstPageHouse = null;
    AnimationDrawable connectivityAnimation = null;
    RelativeLayout bluetoothInfoLayout = null;
    TextView bluetoothMessage = null;
    Button tryAgainButton = null;

    BluetoothAdapter btAdapter = null;
    BluetoothDevice hc05 = null;
    BluetoothSocket btSocket = null;
    OutputStream TX_data = null;
    InputStream RX_data = null;

    Intent homeMenu_intent = null;

//    int sec = 0;
//    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        firstPageConnectivity = (ImageView) findViewById(R.id.firstPageConnectivity);
        firstPageHouse = (ImageView) findViewById(R.id.firstPageHouse);
        connectivityAnimation = (AnimationDrawable) firstPageConnectivity.getDrawable();
        bluetoothInfoLayout = (RelativeLayout) findViewById(R.id.text_layout);
        bluetoothMessage = (TextView) findViewById(R.id.message_BT);
        tryAgainButton = (Button) findViewById(R.id.try_again_button);

        firstPageHouse.setVisibility(View.VISIBLE);
        firstPageConnectivity.setVisibility(View.VISIBLE);
        bluetoothInfoLayout.setVisibility(View.INVISIBLE);

        new bluetoothConnectionTask().execute();

//        mHandler = new Handler();
//        startRepeatingTask();
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
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (ActivityCompat.checkSelfPermission(StartActivity.this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                bluetooth_action_result = BLUETOOTH_PERMISSION_NOT_GRANTED;
            }

            btAdapter = BluetoothAdapter.getDefaultAdapter();

            if (btAdapter == null) {
                bluetooth_action_result = BLUETOOTH_NOT_SUPPORTED;
            }
            else {
                if (!btAdapter.isEnabled()) {
                    bluetooth_action_result = BLUETOOTH_NOT_ENABLED;
                }
                if(BLUETOOTH_OK == bluetooth_action_result) {
                    Set<BluetoothDevice> all_devices = btAdapter.getBondedDevices();

                    hc05 = btAdapter.getRemoteDevice("00:22:06:01:78:5E");

                    if(!all_devices.contains(hc05)) {
                        bluetooth_action_result = DEVICE_NOT_PAIRED;
                    }

                    int btChecks_counter = 0;
                    do {
                        try {
                            btSocket = hc05.createRfcommSocketToServiceRecord(mUUID);
                            btSocket.connect();
                        } catch (IOException e) {
                            bluetooth_action_result = CONNECTION_NOT_ESTABLISHED;
                        }
                        btChecks_counter++;
                    } while (!btSocket.isConnected() && btChecks_counter < 5);

                    if (bluetooth_action_result != CONNECTION_NOT_ESTABLISHED) {
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

                            for (int i = 0; i < 26; i++) {
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
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            connectivityAnimation.stop();

            if(BLUETOOTH_OK != bluetooth_action_result) {
                firstPageHouse.setVisibility(View.INVISIBLE);
                firstPageConnectivity.setVisibility(View.INVISIBLE);
                bluetoothInfoLayout.setVisibility(View.VISIBLE);

                if(BLUETOOTH_PERMISSION_NOT_GRANTED == bluetooth_action_result) {
                    bluetoothMessage.setText(R.string.permission_not_granted);
                }
                else if(BLUETOOTH_NOT_SUPPORTED == bluetooth_action_result) {
                    bluetoothMessage.setText(R.string.device_not_supported);
                }
                else if(BLUETOOTH_NOT_ENABLED == bluetooth_action_result) {
                    bluetoothMessage.setText(R.string.bluetooth_not_connected);
                }
                else if(DEVICE_NOT_PAIRED == bluetooth_action_result) {
                    bluetoothMessage.setText(R.string.device_not_paired);
                }
                else if(CONNECTION_NOT_ESTABLISHED == bluetooth_action_result) {
                    bluetoothMessage.setText((R.string.connection_not_established));
                }

                tryAgainButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(StartActivity.this, StartActivity.class));
                    }
                });
            }
            else {
                homeMenu_intent = new Intent(StartActivity.this, HomeMenuActivity.class);
                startActivity(homeMenu_intent);
            }
        }
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        stopRepeatingTask();
//    }
//
//    Runnable mStatusChecker = new Runnable() {
//        @Override
//        public void run() {
//            try {
//                sec++;
//                System.out.println("sec: " + sec);
//            } finally {
//                // 100% guarantee that this always happens, even if
//                // your update method throws an exception
//                mHandler.postDelayed(mStatusChecker, 1000);
//            }
//        }
//    };
//
//    void startRepeatingTask() {
//        mStatusChecker.run();
//    }
//
//    void stopRepeatingTask() {
//        mHandler.removeCallbacks(mStatusChecker);
//    }

}