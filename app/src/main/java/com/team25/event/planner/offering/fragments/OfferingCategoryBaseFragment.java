package com.team25.event.planner.offering.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentOfferingCategoryBaseBinding;
import com.team25.event.planner.databinding.FragmentOwnerHomePageBinding;
import com.team25.event.planner.offering.adapters.OfferingCategoryAdapter;
import com.team25.event.planner.offering.model.OfferingCategory;
import com.team25.event.planner.offering.model.OfferingCategoryType;

import java.util.ArrayList;
import java.util.List;

public class OfferingCategoryBaseFragment extends Fragment {

    private NavController navController;
    private FragmentOfferingCategoryBaseBinding binding;
    private OfferingCategoryAdapter adapter;
    private ListView listView;

    List<OfferingCategory> offeringCategories;

    public OfferingCategoryBaseFragment() {
        // Required empty public constructor
    }

    /*public static OfferingCategoryBaseFragment newInstance(String param1, String param2) {

    }*/

    //initialize viewModels
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //init ui
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOfferingCategoryBaseBinding.inflate(inflater, container, false);
        navController = Navigation.findNavController(requireActivity(),R.id.nav_host_fragment );
        return binding.getRoot();
    }

    //init adapter, listener
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = binding.list;
        // mock
        OfferingCategory offering1 = new OfferingCategory(1L, "Category 1", "Description 1", OfferingCategoryType.ACCEPTED);
        OfferingCategory offering2 = new OfferingCategory(2L, "Category 2", "Description 2", OfferingCategoryType.PENDING);
        OfferingCategory offering3 = new OfferingCategory(3L, "Category 3", "Description 3", OfferingCategoryType.ACCEPTED);
        offeringCategories = new ArrayList<>();
        offeringCategories.add(offering1);
        offeringCategories.add(offering2);
        offeringCategories.add(offering3);
        //

        adapter = new OfferingCategoryAdapter(requireContext(), offeringCategories);
        listView.setAdapter(adapter);
    }
}