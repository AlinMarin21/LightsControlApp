package com.example.smartliving;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationChannelCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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

    static final int NO_MOTION = 0;
    static final int MOTION = 1;

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

    boolean transmission_allowed = false;
    boolean recovery_action = false;
    int last_motion_state = NO_MOTION;

    private Handler mHandler;

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

        NotificationChannel notificationChannel = new NotificationChannel("Motion Notification", "MOTION_NOTIFICATION", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(notificationChannel);

        new bluetoothConnectionTask().execute();

        mHandler = new Handler();
        startRepeatingTask();
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
            } else {
                if (!btAdapter.isEnabled()) {
                    bluetooth_action_result = BLUETOOTH_NOT_ENABLED;
                }
                if (BLUETOOTH_OK == bluetooth_action_result) {
                    Set<BluetoothDevice> all_devices = btAdapter.getBondedDevices();

                    hc05 = btAdapter.getRemoteDevice("00:22:06:01:78:5E");

                    if (!all_devices.contains(hc05)) {
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
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            connectivityAnimation.stop();

            if (BLUETOOTH_OK != bluetooth_action_result) {
                firstPageHouse.setVisibility(View.INVISIBLE);
                firstPageConnectivity.setVisibility(View.INVISIBLE);
                bluetoothInfoLayout.setVisibility(View.VISIBLE);

                if (BLUETOOTH_PERMISSION_NOT_GRANTED == bluetooth_action_result) {
                    bluetoothMessage.setText(R.string.permission_not_granted);
                } else if (BLUETOOTH_NOT_SUPPORTED == bluetooth_action_result) {
                    bluetoothMessage.setText(R.string.device_not_supported);
                } else if (BLUETOOTH_NOT_ENABLED == bluetooth_action_result) {
                    bluetoothMessage.setText(R.string.bluetooth_not_connected);
                } else if (DEVICE_NOT_PAIRED == bluetooth_action_result) {
                    bluetoothMessage.setText(R.string.device_not_paired);
                } else if (CONNECTION_NOT_ESTABLISHED == bluetooth_action_result) {
                    bluetoothMessage.setText((R.string.connection_not_established));
                }

                tryAgainButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(StartActivity.this, StartActivity.class));
                    }
                });
            } else {
                transmission_allowed = true;
                recovery_action = true;

                homeMenu_intent = new Intent(StartActivity.this, HomeMenuActivity.class);
                startActivity(homeMenu_intent);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                if (true == transmission_allowed) {
                    try {
                        if (true == recovery_action) {
                            TX_data.write(GlobalBuffer.TxBuffer[0]);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    try {
                        RX_data.read(GlobalBuffer.RxBuffer, 0, 16);

                        if (true == recovery_action) {
                            for (int i = 0; i < GlobalBuffer.RX_ELEMENTS; i++) {
                                GlobalBuffer.TxBuffer[i] = GlobalBuffer.RxBuffer[i];
                            }
                        }

                        /*workaround*/
                        if ((GlobalBuffer.RxBuffer[0] & 0xFF) != GlobalBuffer.SOB && (GlobalBuffer.RxBuffer[15] & 0xFF) != GlobalBuffer.EOB) {
                            for (int j = 0; j < 8; j++) {
                                int last_element = GlobalBuffer.RxBuffer[15] & 0xFF;
                                for (int k = GlobalBuffer.RX_ELEMENTS - 1; k > 0; k--) {
                                    GlobalBuffer.RxBuffer[k] = GlobalBuffer.RxBuffer[k - 1];
                                }
                                GlobalBuffer.RxBuffer[0] = (byte) last_element;
                            }
                        }

                        System.out.print("RX: ");
                        for (int i = 0; i < GlobalBuffer.RX_ELEMENTS; i++) {
                            System.out.print(GlobalBuffer.RxBuffer[i] + " ");
                        }
                        System.out.println("");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    try {
                        if (false == recovery_action) {
                            TX_data.write(GlobalBuffer.TxBuffer, 0, 16);
                        } else {
                            recovery_action = false;
                            TX_data.write(GlobalBuffer.TxBuffer, 1, 15);
                        }
                        System.out.print("TX: ");
                        for (int i = 0; i < GlobalBuffer.TX_ELEMENTS; i++) {
                            System.out.print(GlobalBuffer.TxBuffer[i] + " ");
                        }
                        System.out.println("");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    if (NO_MOTION == last_motion_state && MOTION == (GlobalBuffer.RxBuffer[1] & 0xFF)) {

                        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(StartActivity.this, "Motion Notification");

                        notificationBuilder.setContentTitle("Motion has been detected in your home");
                        notificationBuilder.setSmallIcon(R.drawable.home_image);
                        notificationBuilder.setAutoCancel(true);

                        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(StartActivity.this);
                        if (ActivityCompat.checkSelfPermission(StartActivity.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                            //do nothing
                        }
                        notificationManagerCompat.notify(1, notificationBuilder.build());
                    }
                    last_motion_state = GlobalBuffer.RxBuffer[1];
                }
            } finally {
                mHandler.postDelayed(mStatusChecker, 250);
            }
        }
    };

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }
}