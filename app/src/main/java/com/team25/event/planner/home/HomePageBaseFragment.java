package com.team25.event.planner.home;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentHomePageBaseBinding;
import com.team25.event.planner.event.fragments.EventsFragment;
import com.team25.event.planner.event.fragments.TopEventsFragment;
import com.team25.event.planner.offering.fragments.TopOfferingsFragment;


public class HomePageBaseFragment extends Fragment {


    private  Button eventButton;
    private Button psButton;
    private FragmentHomePageBaseBinding binding;
    public HomePageBaseFragment() {
    }

    public static HomePageBaseFragment newInstance() {
        return new HomePageBaseFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomePageBaseBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    private void eventButtonClick(){
        getChildFragmentManager().beginTransaction()
                .replace(binding.homeContainer.getId(), new EventsFragment())
                .commit();

        eventButton.setBackgroundColor(0);
        eventButton.setOnClickListener(null);
        psButton.setBackgroundColor(getResources().getColor(R.color.primary));

        psButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                psButtonClick();

            }
        });
    }


    private void psButtonClick(){
        psButton.setBackgroundColor(0);
        psButton.setOnClickListener(null);
        eventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventButtonClick();
            }
        });
        eventButton.setBackgroundColor(getResources().getColor(R.color.primary));
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState == null) {
            getChildFragmentManager().beginTransaction()
                    .replace(binding.homeTopEvents.getId(), new TopEventsFragment())
                    .replace(binding.homeTopOffers.getId(), new TopOfferingsFragment())
                    .commit();
        }

        eventButton = binding.eventButton;
        psButton = binding.psButton;

        eventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventButtonClick();
            }
        });

        psButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                psButtonClick();
            }
        });

    }
}