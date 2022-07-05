package com.example.shohojogi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class userReg extends AppCompatActivity{

    FirebaseAuth firebaseAuth;
    FirebaseFirestore fstore;
    EditText name,email,dateofbirth;
    TextView cate;
    Button cb1,cb2;
    Spinner spinner;
    ProgressBar progressBar;
    String userID,userphonenumber,usercat,date,gender;
    LinearLayout skill_lay;

    CheckBox bathroomCheck,floorCheck,furnitureCheck,carCheck,acCheck,fridgeCheck,kitchenCheck,
             clothesCheck,ironCheck,curtainsCheck,carpetCheck;

    DatePickerDialog.OnDateSetListener onDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reg);

        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        name = (EditText) findViewById(R.id.ct1);
        email = (EditText) findViewById(R.id.ct2);
        dateofbirth = (EditText) findViewById(R.id.ct3);
        cb1 =(Button)findViewById(R.id.cb1);
        cb2 =(Button)findViewById(R.id.cb2);
        cate = (TextView)findViewById(R.id.ctv1);
        spinner=(Spinner)findViewById(R.id.spinner);
        progressBar =(ProgressBar)findViewById(R.id.pb2);
        userphonenumber= getIntent().getStringExtra("mobile").toString();
        usercat = getIntent().getStringExtra("cat").toString();

        skill_lay = (LinearLayout) findViewById(R.id.skill_l);

        bathroomCheck = (CheckBox) findViewById(R.id.bathroom_cb);
        floorCheck = (CheckBox) findViewById(R.id.floor_cb);
        furnitureCheck = (CheckBox) findViewById(R.id.furniture_cb);
        carCheck = (CheckBox) findViewById(R.id.car_cb);
        acCheck = (CheckBox) findViewById(R.id.ac_cb);
        fridgeCheck = (CheckBox) findViewById(R.id.fridge_cb);
        kitchenCheck = (CheckBox) findViewById(R.id.kitchen_cb);
        clothesCheck = (CheckBox) findViewById(R.id.clothes_cb);
        ironCheck = (CheckBox) findViewById(R.id.iron_cb);
        curtainsCheck = (CheckBox) findViewById(R.id.curtains_cb);
        carpetCheck = (CheckBox) findViewById(R.id.carpet_cb);

        StringBuilder result=new StringBuilder();


        List<String> gen = new ArrayList<>();
        gen.add(0,"tap to select");
        gen.add("Female");
        gen.add("Male");

        ArrayAdapter<String> dataAdapter;

        dataAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,gen);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    gender = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if(usercat.equals("Client")){
            cate.setText("Register as a Client!");
            cb1.setVisibility(View.VISIBLE);
            cb2.setVisibility(View.GONE);
            skill_lay.setVisibility(View.GONE);
        }
        else if(usercat.equals("Worker")){
            cate.setText("Register as a Worker!");
            cb1.setVisibility(View.GONE);
            cb2.setVisibility(View.VISIBLE);
            skill_lay.setVisibility(View.VISIBLE);
        }

        firebaseAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();

        dateofbirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        userReg.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, onDateSetListener, year,month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });


        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                date = dayOfMonth+"/"+month+"/"+year;
                dateofbirth.setText(date);
            }
        };

        DocumentReference docref = fstore.collection("users").document(userID);

        cb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!name.getText().toString().isEmpty() && !email.getText().toString().isEmpty() && !dateofbirth.getText().toString().isEmpty() && !gender.equals("tap to select")){

                    progressBar.setVisibility(View.VISIBLE);
                    cb1.setEnabled(false);


                    String uname = name.getText().toString();
                    String uemail = email.getText().toString();
                    String upnum = userphonenumber;
                    String ucat = usercat;
                    String udate = date;
                    String ugender = gender;

                    DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Client").child(userID);
                    current_user_db.setValue(true);

                    Map<String,Object> user = new HashMap<>();
                    user.put("Name",uname);
                    user.put("email",uemail);
                    user.put("Number",upnum);
                    user.put("Category",ucat);
                    user.put("Date of Birth",udate);
                    user.put("Gender",ugender);

                    docref.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                if(usercat.equals("Client"))
                                    dashboard();

                            }
                            else{
                                Toast.makeText(userReg.this,"Data is Not inserted",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }

                else {

                    Toast.makeText(getApplicationContext(),"Fill up every field.",Toast.LENGTH_SHORT).show();

                }

                }
        });





        cb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!name.getText().toString().isEmpty() && !email.getText().toString().isEmpty() && !dateofbirth.getText().toString().isEmpty() && !gender.equals("tap to select")){
                    if(bathroomCheck.isChecked() || floorCheck.isChecked() || furnitureCheck.isChecked() || carCheck.isChecked() || acCheck.isChecked() || fridgeCheck.isChecked() || kitchenCheck.isChecked() ||
                            clothesCheck.isChecked() || ironCheck.isChecked() || curtainsCheck.isChecked() || carpetCheck.isChecked()){

                        if(bathroomCheck.isChecked()){
                            result.append("Bathroom Cleaning,");
                        }

                        if(floorCheck.isChecked()){
                            result.append("Floor Cleaning,");
                        }

                        if(furnitureCheck.isChecked()){
                            result.append("Furniture Cleaning,");
                        }

                        if(carCheck.isChecked()){
                            result.append("Car Cleaning,");
                        }

                        if(acCheck.isChecked()){
                            result.append("AC Cleaning,");
                        }

                        if(fridgeCheck.isChecked()){
                            result.append("Fridge Cleaning,");
                        }

                        if(kitchenCheck.isChecked()){
                            result.append("Kitchen Cleaning,");
                        }

                        if(clothesCheck.isChecked()){
                            result.append("Clothes Laundry,");
                        }

                        if(ironCheck.isChecked()){
                            result.append("Iron Laundry,");
                        }

                        if(curtainsCheck.isChecked()){
                            result.append("Curtains Laundry,");
                        }

                        if(carpetCheck.isChecked()){
                            result.append("Carpet Laundry,");
                        }

                        int len = result.length();
                        if(len>0){
                            result.deleteCharAt(len-1);
                        }

                        progressBar.setVisibility(View.VISIBLE);
                        cb2.setEnabled(false);

                        DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Worker").child(userID);
                        current_user_db.setValue(true);

                        String ski= result.toString();
                        HashMap map = new HashMap();
                        map.put("Skills", ski);
                        current_user_db.updateChildren(map);


                        String uname = name.getText().toString();
                        String uemail = email.getText().toString();
                        String upnum = userphonenumber;
                        String ucat = usercat;
                        String udate = date;
                        String ugender = gender;
                        String uskills = result.toString();


                        Map<String,Object> user = new HashMap<>();
                        user.put("Name",uname);
                        user.put("email",uemail);
                        user.put("Number",upnum);
                        user.put("Category",ucat);
                        user.put("Date of Birth",udate);
                        user.put("Gender",ugender);
                        user.put("Skills",uskills);

                        docref.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){

                                    if(usercat.equals("Worker"))
                                        workerprofile();

                                }
                                else{
                                    Toast.makeText(userReg.this,"Data is Not inserted",Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }

                    else {
                        Toast.makeText(getApplicationContext(),"Blank field cannot be proceed",Toast.LENGTH_SHORT).show();
                    }

                }

                else {

                    Toast.makeText(getApplicationContext(),"Blank field cannot be proceed",Toast.LENGTH_SHORT).show();

                }

            }
        });


    }

    public void dashboard(){
        Intent intent = new Intent(userReg.this,dashboard.class);
        startActivity(intent);
        finish();
    }
    public void workerprofile(){
        Intent intent = new Intent(userReg.this,workerprofile.class);
        startActivity(intent);
        finish();
    }
}