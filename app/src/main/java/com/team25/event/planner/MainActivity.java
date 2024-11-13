package com.team25.event.planner;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.team25.event.planner.databinding.ActivityMainBinding;
import com.team25.event.planner.home.HomePageBaseFragment;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_frame_container, new HomePageBaseFragment())
                    .commit();
        }
    }
}