package com.team25.event.planner.product.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.team25.event.planner.R;
import com.team25.event.planner.core.dialogs.DialogHelper;
import com.team25.event.planner.databinding.FragmentProductDetailsBinding;
import com.team25.event.planner.event.fragments.EventArgumentNames;
import com.team25.event.planner.event.viewmodel.PurchaseViewModel;
import com.team25.event.planner.product.adapters.ImageSliderProductAdapter;
import com.team25.event.planner.product.viewmodel.MyProductsViewModel;


public class ProductDetailsFragment extends Fragment {

    private NavController navController;
    private FragmentProductDetailsBinding binding;
    private MyProductsViewModel productViewModel;
    private PurchaseViewModel purchaseViewModel;
    private ListView listView;
    private final String BUY_PRODUCT = "BUY_PRODUCT";

    private Long _eventId;
    private Long _productId;

    public ProductDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProductDetailsBinding.inflate(inflater,container,false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        productViewModel = new ViewModelProvider(this).get(MyProductsViewModel.class);
        purchaseViewModel = new ViewModelProvider(this).get(PurchaseViewModel.class);
        binding.setViewModel(productViewModel);
        navController = Navigation.findNavController(requireActivity(),R.id.nav_host_fragment );
        if (getArguments() != null) {
            _eventId = getArguments().getLong(EventArgumentNames.ID_ARG);
            _productId = getArguments().getLong(ProductFormFragment.ID_ARG_NAME);
            purchaseViewModel.eventId.postValue(_eventId);
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = binding.listView;
        setObservers();
        setListeners();
        productViewModel.fetchProduct(_productId);
    }

    private void setObservers(){
        productViewModel.eventTypeNames.observe(getViewLifecycleOwner(), res ->{
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, res);
            listView.setAdapter(adapter);
        });
        productViewModel.images.observe(getViewLifecycleOwner(), res->{
            ImageSliderProductAdapter adapter = new ImageSliderProductAdapter(res);
            ViewPager2 viewPager = binding.imageSlider;
            viewPager.setAdapter(adapter);
        });
        productViewModel.available.observe(getViewLifecycleOwner(), check ->{
            if(check && _eventId != 0L){
                binding.buyButton.setVisibility(View.VISIBLE);
            }else{
                binding.buyButton.setVisibility(View.GONE);
            }
        });
        purchaseViewModel.purchaseResponse.observe(getViewLifecycleOwner(), check ->{
            if (check) {
                DialogHelper.showSuccessDialog(requireContext(), "Successfully bought product: " + productViewModel.name.getValue());
            } else {
                DialogHelper.showErrorDialog(requireContext(), "Error occurred, try again.");
            }
        });
    }

    private void setListeners(){
        binding.buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchaseViewModel.purchaseProduct(_productId);
            }
        });
    }
}