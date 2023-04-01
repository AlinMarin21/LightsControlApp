package com.example.smartliving;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;


public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        new bluetoothConnectionTask().execute();


//        /*start the connectivity animation*/
//        ImageView firstPageConnectivity = findViewById(R.id.firstPageConnectivity);
//        AnimationDrawable connectivityAnimation = (AnimationDrawable) firstPageConnectivity.getDrawable();
//        connectivityAnimation.start();
//
//        /*the animmation will run for 6.6s*/
//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    @Override
//                    public void run() {
//                        /*after 6.6s, the animation stops and a new activity will start*/
//                        connectivityAnimation.stop();
//
//                    }
//                }, 6600);
    }

    private class bluetoothConnectionTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            System.out.println("animation start");
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("animation on background");

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            System.out.println("animation stop");
        }
    }


}