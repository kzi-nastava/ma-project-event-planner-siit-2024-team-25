package com.team25.event.planner;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.team25.event.planner.databinding.ActivityMainBinding;
import com.team25.event.planner.product_service.fragments.OwnerHomePage;
import com.team25.event.planner.product_service.fragments.OwnerPageBaseFragment;
import com.team25.event.planner.product_service.fragments.ProfilFragment;
import com.team25.event.planner.product_service.fragments.ServiceContainerFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            // Load TopEventsFragment dynamically
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_frame_container, new OwnerPageBaseFragment())
                    .commit();
        }
    }
}