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

    int bulb_state = LIGHT_OFF;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bathroom);

        BathroomBulb = (ImageView) findViewById(R.id.bathroom_bulb);

        BathroomBulb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(LIGHT_OFF == bulb_state) {
                    BathroomBulb.setImageResource(R.drawable.bulb_on2);
                    bulb_state = LIGHT_ON;
                }
                else if(LIGHT_ON == bulb_state) {
                    BathroomBulb.setImageResource(R.drawable.bulb_off2);
                    bulb_state = LIGHT_OFF;
                }

                System.out.println(bulb_state);

            }
        });
    }
}
