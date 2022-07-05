package com.example.shohojogi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class manageotp extends AppCompatActivity
{
    EditText t2;
    TextView tv1,em;
    Button b2;
    ProgressBar progressBar;
    String phonenumber,ucat1,ucat2;
    String otpid;
    FirebaseAuth mAuth;
    FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manageotp);

        phonenumber=getIntent().getStringExtra("mobile").toString();
        ucat1 = "Client";
        ucat2= "Worker";
        t2=(EditText)findViewById(R.id.t2);
        tv1= (TextView)findViewById(R.id.tv1);
        em = (TextView) findViewById(R.id.em);
        b2=(Button)findViewById(R.id.b2);
        progressBar =(ProgressBar)findViewById(R.id.progressBar);
        mAuth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();

        tv1.setText("Your given Phone Number is "+phonenumber);

        initiateotp();

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(t2.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(),"Blank Field can not be processed",Toast.LENGTH_SHORT).show();
                else if(t2.getText().toString().length()!=6)
                    Toast.makeText(getApplicationContext(),"Invalid OTP",Toast.LENGTH_SHORT).show();
                else
                {
                    PhoneAuthCredential credential=PhoneAuthProvider.getCredential(otpid,t2.getText().toString());
                    signInWithPhoneAuthCredential(credential);
                    b2.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);
                    em.setVisibility(view.GONE);
                }

            }
        });
    }

    private void initiateotp()
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phonenumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks()
                {
                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken)
                    {
                        otpid=s;
                        b2.setEnabled(true);
                        b2.setBackgroundResource(R.color.dark_purple);
                    }

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential)
                    {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });        // OnVerificationStateChangedCallbacks

    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            DocumentReference docRef = fstore.collection("users").document(mAuth.getCurrentUser().getUid());
                            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if(documentSnapshot.exists()){

                                        if(documentSnapshot.getString("Category").equals(ucat1)){
                                            Intent intent=new Intent(manageotp.this,dashboard.class);
                                            intent.putExtra("mobile",phonenumber);
                                            startActivity(intent);
                                            finish();
                                        }

                                        else if(documentSnapshot.getString("Category").equals(ucat2)){
                                            Intent intent=new Intent(manageotp.this, workerprofile.class);
                                            intent.putExtra("mobile",phonenumber);
                                            startActivity(intent);
                                            finish();
                                        }

                                        else{
                                            Toast.makeText(manageotp.this,"Error",Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                    else {
                                        Intent intent=new Intent(manageotp.this, usercategory.class);
                                        intent.putExtra("mobile",phonenumber);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(getApplicationContext(),"Signin Code Error",Toast.LENGTH_SHORT).show();
                            b2.setEnabled(true);
                            progressBar.setVisibility(View.GONE);
                            em.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

}
