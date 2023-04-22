package com.example.smartliving;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class HomeMenuActivity extends AppCompatActivity {

    private long pressedTime;

    Intent livingRoom_intent = null;
    Intent terrace_intent = null;
    Intent kitchen_intent = null;
    Intent bathroom_intent = null;
    Intent room1_intent = null;
    Intent room2_intent = null;

    Button livingRoomButton;
    Button kitchenButton;
    Button room1Button;
    Button room2Button;
    Button terraceButton;
    Button bathroomButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homemenu);

        livingRoomButton = (Button) findViewById(R.id.living_room_button);
        kitchenButton = (Button) findViewById(R.id.kitchen_button);
        room1Button = (Button) findViewById(R.id.room1_button);
        room2Button = (Button) findViewById(R.id.room2_button);
        terraceButton = (Button) findViewById(R.id.terrace_button);
        bathroomButton = (Button) findViewById(R.id.bathroom_button);

        livingRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                livingRoom_intent = new Intent(HomeMenuActivity.this, LivingRoomActivity.class);
                startActivity(livingRoom_intent);
            }
        });

        kitchenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kitchen_intent = new Intent(HomeMenuActivity.this, KitchenActivity.class);
                startActivity(kitchen_intent);
            }
        });

        room1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                room1_intent = new Intent(HomeMenuActivity.this, Room1Activity.class);
                startActivity(room1_intent);
            }
        });

        room2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                room2_intent = new Intent(HomeMenuActivity.this, Room2Activity.class);
                startActivity(room2_intent);
            }
        });

        terraceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                terrace_intent = new Intent(HomeMenuActivity.this, TerraceActivity.class);
                startActivity(terrace_intent);
            }
        });

        bathroomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               bathroom_intent = new Intent(HomeMenuActivity.this, BathroomActivity.class);
                startActivity(bathroom_intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finishAffinity();
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }
}
