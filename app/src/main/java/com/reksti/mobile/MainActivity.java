package com.reksti.mobile;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.reksti.mobile.ui.*;

import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int id = item.getItemId();

                if (id == R.id.nav_dashboard) {
                    selectedFragment = new DashboardFragment();
                } else if (id == R.id.nav_register) {
                    selectedFragment = new RegisterFragment();
                } else if (id == R.id.nav_attend) {
                    selectedFragment = new AttendFragment();
                } else if (id == R.id.nav_class_info) {
                    selectedFragment = new ClassFragment();
                } else if (id == R.id.nav_create_class) {
                    selectedFragment = new CreateClassFragment();
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();
                }

                return true;
            }
        });

        // Set default fragment
        bottomNav.setSelectedItemId(R.id.nav_dashboard);
    }
}
