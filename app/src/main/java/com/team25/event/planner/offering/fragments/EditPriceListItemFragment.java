package com.team25.event.planner.offering.fragments;

import static com.team25.event.planner.offering.fragments.OfferingCategoryBaseFragment.ID_ARG_NAME;
import static com.team25.event.planner.offering.fragments.ProductPriceListFragment.DISCOUNT_ARG_NAME;
import static com.team25.event.planner.offering.fragments.ProductPriceListFragment.ID_OFFERING_ARG_NAME;
import static com.team25.event.planner.offering.fragments.ProductPriceListFragment.PRICE_ARG_NAME;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentEditPriceListItemBinding;
import com.team25.event.planner.offering.viewmodel.PriceListViewModel;

import java.util.Objects;


public class EditPriceListItemFragment extends Fragment {

    private FragmentEditPriceListItemBinding binding;
    private NavController navController;
    private PriceListViewModel viewModel;
    public EditPriceListItemFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditPriceListItemBinding.inflate(inflater,container,false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        viewModel = new ViewModelProvider(this).get(PriceListViewModel.class);
        binding.setViewModel(viewModel);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        if(getArguments()!=null){
            Long offeringId = getArguments().getLong(ID_OFFERING_ARG_NAME);
            Double price = getArguments().getDouble(PRICE_ARG_NAME);
            Double discount = getArguments().getDouble(DISCOUNT_ARG_NAME);
            viewModel.offeringId.postValue(offeringId);
            viewModel.price.postValue(price);
            viewModel.discount.postValue(discount);
            viewModel.fillForm(price, discount);
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpObservers();
        setUpListeners();
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpObservers();
        setUpListeners();
    }

    private void setUpListeners() {
        binding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_editPriceListItemFragment_to_priceListPage);
            }
        });
        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.updatePriceListItem();
            }
        });
    }

    private void setUpObservers() {
        viewModel.serverError.observe(getViewLifecycleOwner(), mess->{
            if(mess != null){
                Toast.makeText(requireContext(), mess, Toast.LENGTH_SHORT).show();
            }
        });
        viewModel.editDone.observe(getViewLifecycleOwner(), check ->{
            if(check && Objects.requireNonNull(navController.getCurrentDestination()).getId() == R.id.editPriceListItemFragment){
                navController.navigate(R.id.action_editPriceListItemFragment_to_priceListPage);
            }
        });
    }
}