package com.team25.event.planner.product_service.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.team25.event.planner.R;
import com.team25.event.planner.core.listeners.OnDeleteButtonClickListener;
import com.team25.event.planner.core.listeners.OnEditButtonClickListener;
import com.team25.event.planner.databinding.FragmentOwnerHomePageBinding;
import com.team25.event.planner.event.model.EventType;
import com.team25.event.planner.event.viewmodel.EventTypeListViewModel;
import com.team25.event.planner.offering.dialogs.YesOrNoDialogFragment;
import com.team25.event.planner.offering.model.OfferingCategory;
import com.team25.event.planner.offering.viewmodel.OfferingCategoryViewModel;
import com.team25.event.planner.product_service.viewModels.ServiceAddFormViewModel;
import com.team25.event.planner.product_service.viewModels.ServiceCardsViewModel;

import java.util.ArrayList;
import java.util.List;


public class OwnerHomePage extends Fragment  {
    public static final String SERVICE_ID_ARG_NAME = "serviceId";
    private ServiceCardsViewModel serviceCardsViewModel;
    private FragmentOwnerHomePageBinding binding;
    private NavController navController;
    private Spinner categorySpinner;
    private OfferingCategoryViewModel categoryViewModel;
    private Long offeringCategoryFilterId;
    private Spinner eventTypeSpinner;
    private EventTypeListViewModel eventTypeListViewModel;
    private Long eventTypeFilterId;

    private ServiceAddFormViewModel mViewModel;

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
        categoryViewModel = new ViewModelProvider(this).get(OfferingCategoryViewModel.class);
        eventTypeListViewModel = new ViewModelProvider(this).get(EventTypeListViewModel.class);
        mViewModel = new ViewModelProvider(
                NavHostFragment.findNavController(this).getViewModelStoreOwner(R.id.nav_graph)
        ).get(ServiceAddFormViewModel.class);
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

                Spinner spinner3 = dialogView.findViewById(R.id.spinnerAvailable);
                List<String> op = new ArrayList<>();
                op.add("Available: ALL");
                op.add("Available: YES");
                op.add("Available: NO");

                ArrayAdapter<String> adapter3 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, op);
                adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner3.setAdapter(adapter3);


                // offering category spinner
                setOfferingCategorySpinner(dialogView);
                categoryViewModel.allCategories.observe(getViewLifecycleOwner(), categories -> {
                    List<OfferingCategory> updatedCategories = new ArrayList<OfferingCategory>();
                    updatedCategories.add(new OfferingCategory(null, "Select an offering category"));
                    updatedCategories.addAll(categories);
                    ArrayAdapter<OfferingCategory> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, updatedCategories);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    categorySpinner.setAdapter(adapter);
                });

                // event type spinner
                setEventTypeSpinner(dialogView);
                eventTypeListViewModel.eventTypes.observe(getViewLifecycleOwner(), eventTypes -> {
                    List<EventType> updatedTypes = new ArrayList<EventType>();
                    updatedTypes.add(new EventType(null, "Select an event type", "", null));
                    updatedTypes.addAll(eventTypes);
                    ArrayAdapter<EventType> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, updatedTypes);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    eventTypeSpinner.setAdapter(adapter);
                });


                bottomSheetDialog.setContentView(dialogView);
                Button cancelButton = dialogView.findViewById(R.id.button);
                Button filterButton = dialogView.findViewById(R.id.saveFilterButton);
                ImageButton searchButton = binding.seacrhButton2;
                EditText text = dialogView.findViewById(R.id.priceFilter);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();

                    }
                });
                filterButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();

                        getChildFragmentManager().beginTransaction()
                                .replace(binding.scrollServices.getId(), new ServiceContainerFragment(serviceCardsViewModel,true, binding.searchText.getText().toString()
                                ,text.getText().toString(),getAvailable(spinner3),eventTypeFilterId, offeringCategoryFilterId))
                                .commit();

                    }
                });
                searchButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getChildFragmentManager().beginTransaction()
                                .replace(binding.scrollServices.getId(), new ServiceContainerFragment(serviceCardsViewModel,true, binding.searchText.getText().toString(),null,null, null, null))
                                .commit();
                        text.setText("");
                    }
                });
                bottomSheetDialog.show();
            }
        });

        if (savedInstanceState == null) {
            getChildFragmentManager().beginTransaction()
                    .replace(binding.scrollServices.getId(), new ServiceContainerFragment(serviceCardsViewModel,false, null,null, null,null,null))
                    .commit();
        }
    }

    private Boolean getAvailable(Spinner spinner){
        String selectedOption = spinner.getSelectedItem().toString();
        if(selectedOption.equals("Available: ALL")){
            return null;
        }else if(selectedOption.equals("Available: YES")){
            return true;
        }else{
            return false;
        }
    }

    private void setOfferingCategorySpinner(View dialogView){
        categorySpinner = dialogView.findViewById(R.id.spinner);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                OfferingCategory selectedCategory = (OfferingCategory) parent.getItemAtPosition(position);
                offeringCategoryFilterId = selectedCategory.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        categoryViewModel.fetchOfferingCategories();
    }

    private void setEventTypeSpinner(View dialogView){
        eventTypeSpinner = dialogView.findViewById(R.id.spinner2);

        eventTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                EventType eventType = (EventType) parent.getItemAtPosition(position);
                eventTypeFilterId = eventType.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        eventTypeListViewModel.fetchEventTypes();
    }

    @Override
    public void onResume() {
        super.onResume();
        getChildFragmentManager().beginTransaction()
                .replace(binding.scrollServices.getId(), new ServiceContainerFragment(serviceCardsViewModel,false,null,null,null,null, null))
                .commit();
    }


}