package com.example.shohojogi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;

public class enterphonenumber extends AppCompatActivity
{
    CountryCodePicker ccp;
    EditText t1;
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterphonenumber);

        t1=(EditText)findViewById(R.id.t1);
        ccp=(CountryCodePicker)findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(t1);
        b1=(Button)findViewById(R.id.b1);

      t1.addTextChangedListener(new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence s, int start, int count, int after) {
          }

          @Override
          public void onTextChanged(CharSequence s, int start, int before, int count) {
              if(ccp.isValidFullNumber()){
                  b1.setEnabled(true);
                  b1.setBackgroundResource(R.color.ic_launcher_background);
              }
              else{
                  b1.setEnabled(false);
                  b1.setBackgroundResource(R.color.light_grey);
              }
          }

          @Override
          public void afterTextChanged(Editable s) {
          }
      });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(t1.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Blank Field can not be processed",Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent=new Intent(enterphonenumber.this,manageotp.class);
                    intent.putExtra("mobile",ccp.getFullNumberWithPlus().replace(" ",""));
                    startActivity(intent);
                }
            }
        });
    }
}