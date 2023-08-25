
package com.example.lmn__app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ActivityFive extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_five);
    }

    public void retreivePDF(View view){
        startActivity(new Intent(getApplicationContext(),retreivePDF.class));

    }


}