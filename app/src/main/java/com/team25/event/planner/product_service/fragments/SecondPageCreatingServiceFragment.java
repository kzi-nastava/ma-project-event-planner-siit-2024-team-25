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
import com.team25.event.planner.product_service.viewModels.ServiceAddFormViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SecondPageCreatingServiceFragment extends Fragment {

    private ServiceAddFormViewModel mViewModel;
    private NavController navController;
    private ToggleButton toggleButton;
    private ViewSwitcher viewSwitcher;
    private FragmentSecondPageCreatingBinding binding;
    private Button showDialogButton;
    // from event types vm getAll
    private List<String> selectedItems = new ArrayList<>();
    private List<String> allItems = Arrays.asList("Option 1", "Option 2", "Option 3", "Option 4");


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

        toggleButton = binding.toggleButton;
        viewSwitcher = binding.viewSwitcher;

        setOfferingCategories();
        showDialogButton = binding.chooseEventTypes;
        showDialogButton.setOnClickListener(v -> showMultiChoiceDialog());

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
        return binding.getRoot();
    }


    public void setOfferingCategories(){
        // call vm to get every single category from db
        String[] defaultCategories = {"Category 1", "Category 2", "Category 3", "Category 4"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, defaultCategories);
        AutoCompleteTextView autoCompleteTextView = binding.autocompleteDropdown;
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.showDropDown();
    }

    private void showMultiChoiceDialog() {
        boolean[] checkedItems = new boolean[allItems.size()];

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        selectedItems.clear();
        builder.setTitle("Select Multiple Options")
                .setMultiChoiceItems(allItems.toArray(new CharSequence[0]), checkedItems,
                        (dialog, which, isChecked) -> {
                            if (isChecked) {
                                selectedItems.add(allItems.get(which));
                            } else {
                                selectedItems.remove(allItems.get(which));
                            }
                        })
                .setPositiveButton("OK", (dialog, which) -> updateUI())
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }


    private void updateUI() {
        String selectedText = TextUtils.join(", ", selectedItems);
        Toast.makeText(requireContext(), "Selected items: " + selectedText, Toast.LENGTH_SHORT).show();
    }

    public void setObservers(Bundle argumentsBundle){
        mViewModel.secondToThird.observe(getViewLifecycleOwner(), navigate -> {
            if (navigate != null && navigate) {
                navController.navigate(R.id.action_secondPageCreatingServiceFragment_to_finishPageCreateingCerviceFragment,argumentsBundle);

                mViewModel.secondToThird.setValue(false);
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
                viewSwitcher.showNext();  // Switch to Layout 2
            } else {
                viewSwitcher.showPrevious();  // Switch to Layout 1
            }
        });
    }

}