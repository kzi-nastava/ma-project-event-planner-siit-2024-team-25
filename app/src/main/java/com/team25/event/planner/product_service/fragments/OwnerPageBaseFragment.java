package com.team25.event.planner.product_service.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.navigation.NavigationView;
import com.team25.event.planner.R;


public class OwnerPageBaseFragment extends Fragment  implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_owner_page_base, container, false);

        toolbar = view.findViewById(R.id.toolbar);
        drawerLayout = view.findViewById(R.id.main);
        navigationView = view.findViewById(R.id.owner_nav_menu);

        // Set up toolbar and navigation
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(requireActivity(), drawerLayout, toolbar, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.main_layout, new OwnerHomePage())
                    .addToBackStack("homepageOwner")
                    .commit();
            navigationView.setCheckedItem(R.id.Home);
        }

        return view;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;

        if (item.getItemId() == R.id.Home) {
            selectedFragment = new OwnerHomePage();
        } else if (item.getItemId() == R.id.profile) {
            selectedFragment = new ProfilFragment();
        }

        if (selectedFragment != null) {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.main_layout, selectedFragment)
                    .addToBackStack(null)
                    .commit();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        drawerLayout.removeDrawerListener(null);
    }
}