package com.example.shohojogi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class usercategory extends AppCompatActivity {

    Button button1,button2;
    String num,cat1,cat2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usercat);

        button1 =(Button) findViewById(R.id.button1);
        button2 =(Button) findViewById(R.id.button2);
        num = getIntent().getStringExtra("mobile").toString();
        cat1 = "Client";
        cat2 = "Worker";

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clientReg();}
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workerReg();
            }
        });

    }
    public void clientReg(){
        Intent intent = new Intent(this, userReg.class);
        intent.putExtra("mobile",num);
        intent.putExtra("cat",cat1);
        startActivity(intent);
        finish();
    }

    public void workerReg(){
        Intent intent = new Intent(this, userReg.class);
        intent.putExtra("mobile",num);
        intent.putExtra("cat",cat2);
        startActivity(intent);
        finish();
    }

}