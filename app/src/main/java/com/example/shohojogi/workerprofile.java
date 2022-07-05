package com.example.shohojogi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

public class workerprofile extends AppCompatActivity {

    MeowBottomNavigation bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workerprofile);

        LinearLayout historyTitle;
        historyTitle = (LinearLayout) findViewById(R.id.historyTitle);

        bottomNavigation = findViewById(R.id.bottom_navigation);

        bottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.ic_baseline_person_pin_24 ));
        bottomNavigation.add(new MeowBottomNavigation.Model(2,R.drawable.ic_baseline_history_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(3,R.drawable.ic_baseline_settings_24 ));

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                Fragment fragment = null;

                switch (item.getId()){
                    case 1:
                        fragment = new profileFragment();
                        historyTitle.setVisibility(View.GONE);
                        break;
                    case 2:
                        fragment = new historyFragment();
                        historyTitle.setVisibility(View.VISIBLE);
                        Bundle bundle = new Bundle();
                        bundle.putString("User", "Worker");
                        fragment.setArguments(bundle);
                        break;
                    case 3:
                        fragment = new settingsFragment();
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