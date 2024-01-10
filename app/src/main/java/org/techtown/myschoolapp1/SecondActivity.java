package org.techtown.myschoolapp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.techtown.myschoolapp1.Fragment.FragHome;
import org.techtown.myschoolapp1.Fragment.FragSearch;
import org.techtown.myschoolapp1.Fragment.FragSettings;

public class SecondActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    private String TAG = "메인";
    Fragment fragment_home;
    Fragment fragment_settings;
    Fragment fragment_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        fragment_home = new FragHome();
        fragment_search = new FragSearch();
        fragment_settings = new FragSettings();

        getSupportFragmentManager().beginTransaction().replace(R.id.second_layout, fragment_home).commit();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                       if(item.getItemId() == R.id.settings) {
                           getSupportFragmentManager().beginTransaction().replace(R.id.second_layout, fragment_settings).commit();
                       }
                       else if(item.getItemId() == R.id.search) {
                           getSupportFragmentManager().beginTransaction().replace(R.id.second_layout, fragment_search).commit();
                       }
                        return  true;
                    }
                }
        );


    }
}