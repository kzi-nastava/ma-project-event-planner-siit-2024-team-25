package com.team25.event.planner.product_service.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentOwnerHomePageBinding;
import com.team25.event.planner.product_service.adapters.ServiceCardsAdapter;
import com.team25.event.planner.product_service.viewModels.ServiceCardsViewModel;

import java.util.ArrayList;
import java.util.List;


public class OwnerHomePage extends Fragment {
    private ServiceCardsViewModel serviceCardsViewModel;
    private FragmentOwnerHomePageBinding binding;
    private NavController navController;


    public OwnerHomePage() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serviceCardsViewModel = new ServiceCardsViewModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOwnerHomePageBinding.inflate(inflater, container, false);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.floatingActionButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_ownerHomePage_to_serviceAddForm);
            }
        });

        binding.imageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.FullScreenBottomSheetDialog);
                View dialogView = getLayoutInflater().inflate(R.layout.fragment_filter_service, null);

                Spinner spinner = dialogView.findViewById(R.id.spinner);
                List<String> options = new ArrayList<>();
                options.add("Select a category"); // Placeholder
                options.add("category 1");
                options.add("category 2");
                options.add("category 3");

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, options);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);

                Spinner spinner2 = dialogView.findViewById(R.id.spinner2);
                List<String> options2 = new ArrayList<>();
                options2.add("Select an event type");
                options2.add("event type 1");
                options2.add("event type 3");
                options2.add("event type 2");

                ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, options2);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(adapter2);

                bottomSheetDialog.setContentView(dialogView);
                Button cancelButton = dialogView.findViewById(R.id.button);
                Button filterButton = dialogView.findViewById(R.id.saveFilterButton);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });
                filterButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        serviceCardsViewModel.setupFilter();
                        bottomSheetDialog.dismiss();
                    }
                });
                binding.seacrhButton2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        serviceCardsViewModel.setupFilter();
                    }
                });
                bottomSheetDialog.show();
            }
        });

        if (savedInstanceState == null) {
            getChildFragmentManager().beginTransaction()
                    .replace(binding.scrollServices.getId(), new ServiceContainerFragment(serviceCardsViewModel))
                    .commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}