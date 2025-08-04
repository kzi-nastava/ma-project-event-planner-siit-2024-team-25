package com.team25.event.planner.offering.fragments;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.team25.event.planner.R;
import com.team25.event.planner.core.viewmodel.AuthViewModel;
import com.team25.event.planner.databinding.FragmentFavoriteOfferingBinding;
import com.team25.event.planner.offering.adapters.HomeOfferingListAdapter;
import com.team25.event.planner.offering.viewmodel.FavoriteOfferingViewModel;


public class FavoriteOfferingFragment extends Fragment {

    private NavController navController;
    private FragmentFavoriteOfferingBinding binding;
    private FavoriteOfferingViewModel viewModel;
    private AuthViewModel authViewModel;
    private Long AuthUserId;
    private ListView listView;
    private HomeOfferingListAdapter adapter;

    public FavoriteOfferingFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavoriteOfferingBinding.inflate(inflater, container,false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        viewModel = new ViewModelProvider(this).get(FavoriteOfferingViewModel.class);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        AuthUserId = authViewModel.getUserId();
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = binding.listViewFavorites;
        binding.btnProducts.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.primary)));
        binding.btnServices.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), android.R.color.darker_gray)));
        viewModel.getFavoriteProducts(AuthUserId);
        setObservers();
        setListeners();
    }

    private void setObservers(){
        viewModel.allOfferings.observe(getViewLifecycleOwner(), (offerings -> {
            NavController navController = Navigation.findNavController(requireView());
            adapter = new HomeOfferingListAdapter(getActivity(), offerings, navController, null);
            listView.setAdapter(adapter);
        }));
    }
    private void setListeners() {
        binding.btnProducts.setOnClickListener(v -> {
            highlightTab(true);
            viewModel.getFavoriteProducts(AuthUserId);
        });

        binding.btnServices.setOnClickListener(v -> {
            highlightTab(false);
            viewModel.getFavoriteServices(AuthUserId);
        });
    }
    private void highlightTab(boolean productsActive) {
        if (productsActive) {
            binding.btnProducts.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.primary)));
            binding.btnServices.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), android.R.color.darker_gray)));
        } else {
            binding.btnServices.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.primary)));
            binding.btnProducts.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), android.R.color.darker_gray)));
        }
    }

}