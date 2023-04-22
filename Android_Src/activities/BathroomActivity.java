package com.example.smartliving;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BathroomActivity extends AppCompatActivity {

    static final int LIGHT_ON = 1;
    static final int LIGHT_OFF = 0;

    ImageView BathroomBulb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bathroom);

        BathroomBulb = (ImageView) findViewById(R.id.bathroom_bulb);

        if(LIGHT_OFF == GlobalBuffer.TxBuffer[14]) {
            BathroomBulb.setImageResource(R.drawable.bulb_off2);
        }
        else if(LIGHT_ON == GlobalBuffer.TxBuffer[14]) {
            BathroomBulb.setImageResource(R.drawable.bulb_on2);
        }

        BathroomBulb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(LIGHT_OFF == GlobalBuffer.TxBuffer[14]) {
                    BathroomBulb.setImageResource(R.drawable.bulb_on2);
                    GlobalBuffer.TxBuffer[14] = LIGHT_ON;
                }
                else if(LIGHT_ON == GlobalBuffer.TxBuffer[14]) {
                    BathroomBulb.setImageResource(R.drawable.bulb_off2);
                    GlobalBuffer.TxBuffer[14] = LIGHT_OFF;
                }
            }
        });
    }
}
