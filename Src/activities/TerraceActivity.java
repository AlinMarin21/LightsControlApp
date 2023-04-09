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

    int current_mode = MANUAL_CONTROL;
    int bulb_state = LIGHT_OFF;

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

        manualSwitch.setChecked(true);

        manualSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(MANUAL_CONTROL != current_mode) {
                    manualSwitch.setChecked(true);
                    motionSwitch.setChecked(false);
                    brightnessSwitch.setChecked(false);

                    current_mode = MANUAL_CONTROL;
                }
                else {
                    manualSwitch.setChecked(true);
                    current_mode = MANUAL_CONTROL;
                }

            }
        });

        motionSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(MOTION_CONTROL != current_mode) {
                    manualSwitch.setChecked(false);
                    motionSwitch.setChecked(true);
                    brightnessSwitch.setChecked(false);

                    current_mode = MOTION_CONTROL;
                }
                else {
                    motionSwitch.setChecked(true);
                    current_mode = MOTION_CONTROL;
                }

            }
        });

        brightnessSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(DARKNESS_CONTROL != current_mode) {
                    manualSwitch.setChecked(false);
                    motionSwitch.setChecked(false);
                    brightnessSwitch.setChecked(true);

                    current_mode = DARKNESS_CONTROL;
                }
                else {
                    brightnessSwitch.setChecked(true);
                    current_mode = DARKNESS_CONTROL;
                }

            }
        });

        terraceBulb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(MANUAL_CONTROL == current_mode) {
                    if (LIGHT_OFF == bulb_state) {
                        terraceBulb.setImageResource(R.drawable.bulb_on2);
                        bulb_state = LIGHT_ON;
                    } else if (LIGHT_ON == bulb_state) {
                        terraceBulb.setImageResource(R.drawable.bulb_off2);
                        bulb_state = LIGHT_OFF;
                    }
                }
                else {
                    //do nothing
                }

            }
        });

    }
}
