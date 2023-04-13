package com.example.smartliving;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

public class LivingRoomActivity extends AppCompatActivity {

    static final int LIGHT_ON = 1;
    static final int LIGHT_OFF = 0;

    ImageView LivingRoomBulb;
    SeekBar LightIntensityBar;
    int bulb_state = LIGHT_OFF;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livingroom);

        LivingRoomBulb = (ImageView) findViewById(R.id.living_room_bulb);
        LightIntensityBar = (SeekBar) findViewById(R.id.light_intensity_bar);

        if(LIGHT_OFF == bulb_state) {
            LightIntensityBar.setVisibility(View.INVISIBLE);
        }
        else if(LIGHT_ON == bulb_state) {
            LightIntensityBar.setVisibility(View.VISIBLE);
        }

        LivingRoomBulb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(LIGHT_OFF == bulb_state) {
                    LivingRoomBulb.setImageResource(R.drawable.bulb_on2);
                    bulb_state = LIGHT_ON;
                    LightIntensityBar.setVisibility(View.VISIBLE);
                }
                else if(LIGHT_ON == bulb_state) {
                    LivingRoomBulb.setImageResource(R.drawable.bulb_off2);
                    bulb_state = LIGHT_OFF;
                    LightIntensityBar.setVisibility(View.INVISIBLE);
                }

                System.out.println(bulb_state);

            }
        });

    }
}
