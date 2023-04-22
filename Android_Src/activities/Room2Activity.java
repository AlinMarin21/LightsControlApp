package com.example.smartliving;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import yuku.ambilwarna.AmbilWarnaDialog;

public class Room2Activity extends AppCompatActivity {

    static final int LIGHT_ON = 1;
    static final int LIGHT_OFF = 0;

    ImageView Room2Bulb;
    Button PickColorButton;
    FrameLayout ColorBox;

    int red_RGB_value;
    int green_RGB_value;
    int blue_RGB_value;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room2);

        Room2Bulb = (ImageView) findViewById(R.id.room2_bulb);
        PickColorButton = (Button) findViewById(R.id.room2_color_button);
        ColorBox = (FrameLayout) findViewById(R.id.room2_color_box);

        if(LIGHT_OFF == GlobalBuffer.TxBuffer[8]) {
            Room2Bulb.setImageResource(R.drawable.bulb_off2);
        }
        else {
            Room2Bulb.setImageResource(R.drawable.bulb_on2);
        }

        red_RGB_value = GlobalBuffer.TxBuffer[9] & 0xFF;
        green_RGB_value = GlobalBuffer.TxBuffer[10] & 0xFF;
        blue_RGB_value = GlobalBuffer.TxBuffer[11] & 0xFF;

        GlobalBuffer.Room2LedColor = 65536 * red_RGB_value + 256 * green_RGB_value + blue_RGB_value;

        ColorBox.setBackgroundColor(GlobalBuffer.Room2LedColor);

        Room2Bulb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(LIGHT_OFF == GlobalBuffer.TxBuffer[8]) {
                    Room2Bulb.setImageResource(R.drawable.bulb_on2);
                    GlobalBuffer.TxBuffer[8] = LIGHT_ON;
                }
                else if(LIGHT_ON == GlobalBuffer.TxBuffer[8]) {
                    Room2Bulb.setImageResource(R.drawable.bulb_off2);
                    GlobalBuffer.TxBuffer[8] = LIGHT_OFF;
                }
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
        final AmbilWarnaDialog colorPickerDialogue = new AmbilWarnaDialog(this, GlobalBuffer.Room2LedColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                GlobalBuffer.Room2LedColor = color;
                ColorBox.setBackgroundColor(GlobalBuffer.Room2LedColor);

                red_RGB_value = (int) ((Math.pow(256,3) + color) / 65536);
                green_RGB_value = (int) (((Math.pow(256,3) + color) / 256 ) % 256 );
                blue_RGB_value = (int) ((Math.pow(256,3) + color) % 256);

                GlobalBuffer.TxBuffer[9] = (byte) red_RGB_value;
                GlobalBuffer.TxBuffer[10] = (byte) green_RGB_value;
                GlobalBuffer.TxBuffer[11] = (byte) blue_RGB_value;

            }
        });
        colorPickerDialogue.show();
    }
}
