package com.example.smartliving;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import yuku.ambilwarna.AmbilWarnaDialog;

public class Room1Activity extends AppCompatActivity {

    static final int LIGHT_ON = 1;
    static final int LIGHT_OFF = 0;

    ImageView Room1Bulb;
    Button PickColorButton;
    FrameLayout ColorBox;

    int redC;
    int greenC;
    int blueC;

    int default_color = 0xFFFFFF;

    int bulb_state = LIGHT_OFF;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room1);

        Room1Bulb = (ImageView) findViewById(R.id.room1_bulb);
        PickColorButton = (Button) findViewById(R.id.room1_color_button);
        ColorBox = (FrameLayout) findViewById(R.id.room1_color_box);

        Room1Bulb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(LIGHT_OFF == bulb_state) {
                    Room1Bulb.setImageResource(R.drawable.bulb_on2);
                    bulb_state = LIGHT_ON;
                }
                else if(LIGHT_ON == bulb_state) {
                    Room1Bulb.setImageResource(R.drawable.bulb_off2);
                    bulb_state = LIGHT_OFF;
                }

                System.out.println(bulb_state);

            }
        });

        PickColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openColorPickerDialogue();
            }
        });
    }

    public void openColorPickerDialogue() {
        final AmbilWarnaDialog colorPickerDialogue = new AmbilWarnaDialog(this, default_color, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                default_color = color;
                ColorBox.setBackgroundColor(default_color);

                redC = (int) ((Math.pow(256,3) + default_color) / 65536);
                greenC = (int) (((Math.pow(256,3) + default_color) / 256 ) % 256 );
                blueC = (int) ((Math.pow(256,3) + default_color) % 256);

                System.out.println("red color: " + redC + "green color: " + greenC + "blue color: " + blueC);
            }
        });
        colorPickerDialogue.show();
    }
}
