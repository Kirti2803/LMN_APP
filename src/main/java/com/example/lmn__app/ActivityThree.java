package com.example.lmn__app;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class ActivityThree extends AppCompatActivity {
    private Button button4;
    private Button button5;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three);
        button4 = findViewById(R.id.ActivityFour);
        button5 = findViewById(R.id.ActivityFive);

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ActivityThree.this, ActivityFour.class);
                startActivity(intent);


            }


        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ActivityThree.this, ActivityFive.class);
                startActivity(intent);


            }


        });
    }
}