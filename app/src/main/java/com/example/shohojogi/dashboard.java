package com.example.shohojogi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class dashboard extends AppCompatActivity
{
    MeowBottomNavigation bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ImageView iv10;
        TextView tv10;
        LinearLayout lh1,historyTitle;
        FirebaseAuth mAuth;
        FirebaseFirestore fstore;

        bottomNavigation = findViewById(R.id.bottom_navigation);
         iv10 =(ImageView) findViewById(R.id.iv10);
         tv10 =(TextView) findViewById(R.id.tv10);
         lh1 = (LinearLayout) findViewById(R.id.lh1);
         historyTitle = (LinearLayout) findViewById(R.id.historyTitle);

         mAuth = FirebaseAuth.getInstance();
         fstore = FirebaseFirestore.getInstance();

        DocumentReference docRef = fstore.collection("users").document(mAuth.getCurrentUser().getUid());

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                tv10.setText(documentSnapshot.getString("Name"));
            }
        });


        bottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.ic_baseline_home_24 ));
        bottomNavigation.add(new MeowBottomNavigation.Model(2,R.drawable.ic_baseline_person_pin_24 ));
        bottomNavigation.add(new MeowBottomNavigation.Model(3,R.drawable.ic_baseline_history_24 ));
        bottomNavigation.add(new MeowBottomNavigation.Model(4,R.drawable.ic_baseline_settings_24 ));

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                Fragment fragment = null;

                switch (item.getId()){
                    case 1:
                        fragment = new homeFragment();
                        lh1.setVisibility(View.VISIBLE);
                        historyTitle.setVisibility(View.GONE);
                        break;
                    case 2:
                        fragment = new profileFragment();
                        lh1.setVisibility(View.GONE);
                        historyTitle.setVisibility(View.GONE);
                         break;
                    case 3:
                        fragment = new historyFragment();
                        lh1.setVisibility(View.GONE);
                        historyTitle.setVisibility(View.VISIBLE);
                        Bundle bundle = new Bundle();
                        bundle.putString("User", "Client");
                        fragment.setArguments(bundle);
                        break;
                    case 4:
                        fragment = new settingsFragment();
                        lh1.setVisibility(View.GONE);
                        historyTitle.setVisibility(View.GONE);
                        break;
                }

                loadFragment(fragment);

            }
        });


        bottomNavigation.show(1,true);

        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {

            }
        });

        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {

            }
        });



    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,fragment).commit();
    }
}