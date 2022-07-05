package com.example.shohojogi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class splashscreenactivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 3500;

    Animation bottomanim,rightanim,leftanim;
    ImageView image1,image2;
    FirebaseAuth mAuth;
    FirebaseFirestore fstore;
    String ucat1,ucat2;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        ucat1 = "Client";
        ucat2= "Worker";

        mAuth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();

        bottomanim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        rightanim = AnimationUtils.loadAnimation(this,R.anim.right_animation);
        leftanim = AnimationUtils.loadAnimation(this,R.anim.left_animation);

        image1 = findViewById(R.id.img1);
        image2 = findViewById(R.id.img2);

        image1.setAnimation(leftanim);
        image2.setAnimation(rightanim);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    DocumentReference docRef = fstore.collection("users").document(mAuth.getCurrentUser().getUid());
                    docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.exists()){
                                if(documentSnapshot.getString("Category").equals(ucat1)){
                                    Intent intent = new Intent(splashscreenactivity.this,dashboard.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else if(documentSnapshot.getString("Category").equals(ucat2)){
                                    Intent intent = new Intent(splashscreenactivity.this,workerprofile.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }
                    });

                } else {
                    Intent intent = new Intent(splashscreenactivity.this,introduction.class);
                    startActivity(intent);
                    finish();
                }
            }
        },SPLASH_SCREEN);

    }
}