package com.team25.event.planner.product_service.fragments;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.ViewSwitcher;

import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentSecondPageCreatingBinding;
import com.team25.event.planner.event.model.EventType;
import com.team25.event.planner.event.viewmodel.EventTypeListViewModel;
import com.team25.event.planner.offering.model.OfferingCategory;
import com.team25.event.planner.offering.viewmodel.OfferingCategoryViewModel;
import com.team25.event.planner.product_service.viewModels.ServiceAddFormViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SecondPageCreatingServiceFragment extends Fragment {

    private ServiceAddFormViewModel mViewModel;
    private NavController navController;
    private ToggleButton toggleButton;
    private ViewSwitcher viewSwitcher;
    private FragmentSecondPageCreatingBinding binding;
    private Button showDialogButton;
    private OfferingCategoryViewModel offeringCategoryViewModel;
    private AutoCompleteTextView autoCompleteTextView;

    private EventTypeListViewModel eventTypeListViewModel;

    // from event types vm getAll
    private List<EventType> selectedItems = new ArrayList<>();
    private List<EventType> allItems = new ArrayList<>();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_second_page_creating, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        mViewModel = new ViewModelProvider(
                NavHostFragment.findNavController(this).getViewModelStoreOwner(R.id.nav_graph)
        ).get(ServiceAddFormViewModel.class);
        binding.setViewModel(mViewModel);
        offeringCategoryViewModel = new ViewModelProvider(this).get(OfferingCategoryViewModel.class);
        eventTypeListViewModel = new ViewModelProvider(this).get(EventTypeListViewModel.class);

        toggleButton = binding.toggleButton;
        viewSwitcher = binding.viewSwitcher;

        showDialogButton = binding.chooseEventTypes;


        if(getArguments() != null){
            TextView textView = binding.EditOrCreateServiceText;
            textView.setText(R.string.edit_the_service);
            mViewModel.fillTheForm();
            /*String productId = getArguments() != null ? getArguments().getString("productId") : null;*/
        }else{
            mViewModel.restart();
        }

        binding.setLifecycleOwner(this);


        setObservers(getArguments());
        setListeners();

        autoCompleteTextView = binding.autocompleteDropdown;
        offeringCategoryViewModel.allCategories.observe(getViewLifecycleOwner(), categories -> {
            ArrayAdapter<OfferingCategory> adapter = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    categories
            );
            autoCompleteTextView.setAdapter(adapter);
            autoCompleteTextView.setThreshold(0);
            autoCompleteTextView.showDropDown();
        });
        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            OfferingCategory selectedCategory = (OfferingCategory) parent.getItemAtPosition(position);
            mViewModel.offeringCategoryId.setValue(selectedCategory.getId());
        });
        autoCompleteTextView.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {

                mViewModel.offeringCategoryId.setValue(-1L);
                mViewModel.offeringCategoryNewName.setValue(autoCompleteTextView.getText().toString());
            }
        });

        eventTypeListViewModel.eventTypes.observe(getViewLifecycleOwner(), eventTypes -> {
            allItems = eventTypes;
        });

        showDialogButton.setOnClickListener(v -> showMultiChoiceDialog());
        offeringCategoryViewModel.fetchOfferingCategories();
        eventTypeListViewModel.fetchEventTypes();
        return binding.getRoot();
    }



    private void showMultiChoiceDialog() {
        boolean[] checkedItems = new boolean[allItems.size()];

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        selectedItems.clear();
        CharSequence[] itemNames = new CharSequence[allItems.size()];
        for (int i = 0; i < allItems.size(); i++) {
            itemNames[i] = allItems.get(i).getName(); // Prikazivanje samo imena
        }
        builder.setTitle("Select Multiple Options")
                .setMultiChoiceItems(itemNames, checkedItems,
                        (dialog, which, isChecked) -> {
                            if (isChecked) {
                                selectedItems.add(allItems.get(which));
                            } else {
                                selectedItems.remove(allItems.get(which));
                            }
                        })
                .setPositiveButton("OK", (dialog, which) -> updateUI()) // Ažuriraj UI
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss()) // Zatvori dijalog
                .show();
    }


    private void updateUI() {
        String selectedItemsString = selectedItems.stream()
                .map(EventType::getName)
                .collect(Collectors.joining(", "));

        TextView selectedItemsTextView = binding.eventTypesTextView;
        selectedItemsTextView.setText("Selected: " + selectedItemsString);

        List<Long> selectedIds = selectedItems.stream()
                .map(EventType::getId)
                .collect(Collectors.toList());

        mViewModel.eventTypeIds.setValue(selectedIds);
    }

    public void setObservers(Bundle argumentsBundle){
        mViewModel.secondToThird.observe(getViewLifecycleOwner(), navigate -> {
            if (navigate != null && navigate) {
                if(mViewModel.validateForm2()){
                    navController.navigate(R.id.action_secondPageCreatingServiceFragment_to_finishPageCreateingCerviceFragment,argumentsBundle);

                    mViewModel.secondToThird.setValue(false);
                }

            }
        });

        mViewModel.secondToFirst.observe(getViewLifecycleOwner(), navigate -> {
            if (navigate != null && navigate) {
                navController.navigateUp();
                mViewModel.secondToFirst.setValue(false);
            }
        });
    }

    public void setListeners(){
        toggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mViewModel.toggle.setValue(true);
                viewSwitcher.showNext();  // Switch to Layout 2
            } else {
                mViewModel.toggle.setValue(false);
                viewSwitcher.showPrevious();  // Switch to Layout 1
            }
        });
    }

}