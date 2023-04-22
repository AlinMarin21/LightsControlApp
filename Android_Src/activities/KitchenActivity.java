package com.example.smartliving;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class KitchenActivity extends AppCompatActivity {

    static final int LIGHT_ON = 1;
    static final int LIGHT_OFF = 0;

    ImageView KitchenBulb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen);

        KitchenBulb = (ImageView) findViewById(R.id.kitchen_bulb);

        if(LIGHT_OFF == GlobalBuffer.TxBuffer[3]) {
            KitchenBulb.setImageResource(R.drawable.bulb_off2);
        }
        else if(LIGHT_ON == GlobalBuffer.TxBuffer[3]) {
            KitchenBulb.setImageResource(R.drawable.bulb_on2);
        }

        KitchenBulb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(LIGHT_OFF == GlobalBuffer.TxBuffer[3]) {
                    KitchenBulb.setImageResource(R.drawable.bulb_on2);
                    GlobalBuffer.TxBuffer[3] = LIGHT_ON;
                }
                else if(LIGHT_ON == GlobalBuffer.TxBuffer[3]) {
                    KitchenBulb.setImageResource(R.drawable.bulb_off2);
                    GlobalBuffer.TxBuffer[3] = LIGHT_OFF;
                }
            }
        });
    }
}
