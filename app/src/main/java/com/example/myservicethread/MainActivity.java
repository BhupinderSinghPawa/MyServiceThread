package com.example.myservicethread;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // method to start the service
    public void startServiceButtonCB(View view) {
        // starting the service explicitly
        Log.i("myLog", "starting Service Explicitly");
        startService(new Intent(getBaseContext(), MyService.class));
    }

}
