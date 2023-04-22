package com.example.smartliving;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TerraceActivity extends AppCompatActivity {

    static final int LIGHT_ON = 1;
    static final int LIGHT_OFF = 0;

    static final int MANUAL_CONTROL = 0;
    static final int MOTION_CONTROL = 1;
    static final int DARKNESS_CONTROL = 2;

    static final int MOTION_DETECTED = 1;
    static final int DARKNESS_THRESHOLD = 40;

    Switch motionSwitch = null;
    Switch brightnessSwitch = null;
    Switch manualSwitch = null;

    ImageView terraceBulb = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terrace);

        motionSwitch = (Switch) findViewById(R.id.motion_switch);
        brightnessSwitch = (Switch) findViewById(R.id.brightness_switch);
        manualSwitch = (Switch) findViewById(R.id.manual_switch);

        terraceBulb = (ImageView) findViewById(R.id.terrace_bulb);

        if(MANUAL_CONTROL == GlobalBuffer.TxBuffer[13]) {
            manualSwitch.setChecked(true);

            if(LIGHT_OFF == GlobalBuffer.TxBuffer[12]) {
                terraceBulb.setImageResource(R.drawable.bulb_off2);
            }
            else if(LIGHT_ON == GlobalBuffer.TxBuffer[12]) {
                terraceBulb.setImageResource(R.drawable.bulb_on2);
            }
        }
        else if(MOTION_CONTROL == GlobalBuffer.TxBuffer[13]) {
            motionSwitch.setChecked(true);

            if(MOTION_DETECTED == GlobalBuffer.RxBuffer[1]) {
                terraceBulb.setImageResource(R.drawable.bulb_on2);
            }
            else {
                terraceBulb.setImageResource(R.drawable.bulb_off2);
            }
        }
        else if(DARKNESS_CONTROL == GlobalBuffer.TxBuffer[13]) {
            brightnessSwitch.setChecked(true);

            if(DARKNESS_THRESHOLD > GlobalBuffer.RxBuffer[2]) {
                terraceBulb.setImageResource(R.drawable.bulb_on2);
            }
            else {
                terraceBulb.setImageResource(R.drawable.bulb_off2);
            }
        }


        manualSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MANUAL_CONTROL != GlobalBuffer.TxBuffer[13]) {
                    manualSwitch.setChecked(true);
                    motionSwitch.setChecked(false);
                    brightnessSwitch.setChecked(false);

                    GlobalBuffer.TxBuffer[13] = MANUAL_CONTROL;
                }
                else {
                    manualSwitch.setChecked(true);
                    GlobalBuffer.TxBuffer[13] = MANUAL_CONTROL;
                }
            }
        });

        motionSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MOTION_CONTROL != GlobalBuffer.TxBuffer[13]) {
                    manualSwitch.setChecked(false);
                    motionSwitch.setChecked(true);
                    brightnessSwitch.setChecked(false);

                    GlobalBuffer.TxBuffer[13] = MOTION_CONTROL;

                    GlobalBuffer.TxBuffer[12] = LIGHT_OFF;
                }
                else {
                    motionSwitch.setChecked(true);
                    GlobalBuffer.TxBuffer[13] = MOTION_CONTROL;
                }

                if(MOTION_DETECTED == GlobalBuffer.RxBuffer[1]) {
                    terraceBulb.setImageResource(R.drawable.bulb_on2);
                }
                else {
                    terraceBulb.setImageResource(R.drawable.bulb_off2);
                }
            }
        });

        brightnessSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DARKNESS_CONTROL != GlobalBuffer.TxBuffer[13]) {
                    manualSwitch.setChecked(false);
                    motionSwitch.setChecked(false);
                    brightnessSwitch.setChecked(true);

                    GlobalBuffer.TxBuffer[13] = DARKNESS_CONTROL;

                    GlobalBuffer.TxBuffer[12] = LIGHT_OFF;
                }
                else {
                    brightnessSwitch.setChecked(true);
                    GlobalBuffer.TxBuffer[13] = DARKNESS_CONTROL;
                }

                if(DARKNESS_THRESHOLD > GlobalBuffer.RxBuffer[2]) {
                    terraceBulb.setImageResource(R.drawable.bulb_on2);
                }
                else {
                    terraceBulb.setImageResource(R.drawable.bulb_off2);
                }
            }
        });

        terraceBulb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MANUAL_CONTROL == GlobalBuffer.TxBuffer[13]) {
                    if (LIGHT_OFF == GlobalBuffer.TxBuffer[12]) {
                        terraceBulb.setImageResource(R.drawable.bulb_on2);
                        GlobalBuffer.TxBuffer[12] = LIGHT_ON;
                    } else if (LIGHT_ON == GlobalBuffer.TxBuffer[12]) {
                        terraceBulb.setImageResource(R.drawable.bulb_off2);
                        GlobalBuffer.TxBuffer[12] = LIGHT_OFF;
                    }
                }
                else {
                    //do nothing
                }
            }
        });
    }
}
