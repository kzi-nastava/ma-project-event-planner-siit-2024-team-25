package com.team25.event.planner.product.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentProductDetailsBinding;
import com.team25.event.planner.product.viewmodel.MyProductsViewModel;


public class ProductDetailsFragment extends Fragment {

    private FragmentProductDetailsBinding binding;
    private MyProductsViewModel viewModel;
    private final String OFFERING_ID = "OFFERING_ID";
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
        return inflater.inflate(R.layout.fragment_product_details, container, false);
    }
}