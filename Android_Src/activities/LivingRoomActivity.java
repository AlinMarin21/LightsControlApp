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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livingroom);

        LivingRoomBulb = (ImageView) findViewById(R.id.living_room_bulb);
        LightIntensityBar = (SeekBar) findViewById(R.id.light_intensity_bar);

        LightIntensityBar.setMax(255);

        if(LIGHT_OFF == GlobalBuffer.TxBuffer[1]) {
            LightIntensityBar.setVisibility(View.INVISIBLE);
            LivingRoomBulb.setImageResource(R.drawable.bulb_off2);
        }
        else if(LIGHT_ON == GlobalBuffer.TxBuffer[1]) {
            LightIntensityBar.setVisibility(View.VISIBLE);
            LightIntensityBar.setProgress(GlobalBuffer.TxBuffer[2] & 0xFF);
            LivingRoomBulb.setImageResource(R.drawable.bulb_on2);
        }

        LivingRoomBulb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(LIGHT_OFF == GlobalBuffer.TxBuffer[1]) {
                    LivingRoomBulb.setImageResource(R.drawable.bulb_on2);
                    GlobalBuffer.TxBuffer[1] = LIGHT_ON;
                    LightIntensityBar.setVisibility(View.VISIBLE);
                    LightIntensityBar.setProgress(GlobalBuffer.TxBuffer[2] & 0xFF);
                }
                else if(LIGHT_ON == GlobalBuffer.TxBuffer[1]) {
                    LivingRoomBulb.setImageResource(R.drawable.bulb_off2);
                    GlobalBuffer.TxBuffer[1] = LIGHT_OFF;
                    LightIntensityBar.setVisibility(View.INVISIBLE);
                }
            }
        });

        LightIntensityBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                GlobalBuffer.TxBuffer[2] = (byte) i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
