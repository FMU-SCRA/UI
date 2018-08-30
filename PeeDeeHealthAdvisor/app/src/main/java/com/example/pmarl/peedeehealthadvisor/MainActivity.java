/**
 Author: Patrick Marlowe
 Email Address: pmarlowe782@gmail.com
 Written: August 30, 2018
 Last Updated:

 Compilation: MainActivity.java
 Execution: activity_main.xml

 Description of Execution:
 This Class brings up the Home Screen in the App
 */
package com.example.pmarl.peedeehealthadvisor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity
{
    private Button MyHealthData;
    private Button  MyHealthResources;


    @Override
    protected void  onCreate(Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_main);

        this.MyHealthData = (Button) findViewById(R.id.MyHealthData);

        this.MyHealthData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchMyHealthData();
            }
        });

        this.MyHealthResources = (Button) findViewById(R.id.HealthResources);



        this.MyHealthResources.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchSearchServiceActivity();
            }
        });

    }


    private void launchMyHealthData()
    {
        Intent intent = new Intent (this, MyHealthDataActivity.class);
        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }

    private void launchSearchServiceActivity()
    {
        Intent intent = new Intent (this, SearchServiceActivity.class);
        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }
}