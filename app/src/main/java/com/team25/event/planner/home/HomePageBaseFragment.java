package com.team25.event.planner.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentHomePageBaseBinding;
import com.team25.event.planner.databinding.FragmentTopEventsBinding;
import com.team25.event.planner.event.fragments.TopEventsFragment;
import com.team25.event.planner.event.fragments.TopEventsListFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomePageBaseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomePageBaseFragment extends Fragment {


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
        // Inflate the layout for this fragment
        binding = FragmentHomePageBaseBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Load the TopEventsListFragment dynamically
        if (savedInstanceState == null) {
            getChildFragmentManager().beginTransaction()
                    .replace(binding.scrollHomePageContainer.getId(), new TopEventsFragment())
                    .commit();
        }
    }
}