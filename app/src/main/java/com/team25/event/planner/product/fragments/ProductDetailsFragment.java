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
import android.widget.Toast;

import com.team25.event.planner.R;
import com.team25.event.planner.communication.fragments.ChatFragment;
import com.team25.event.planner.communication.model.ChatMessage;
import com.team25.event.planner.core.dialogs.DialogHelper;
import com.team25.event.planner.core.viewmodel.AuthViewModel;
import com.team25.event.planner.databinding.FragmentProductDetailsBinding;
import com.team25.event.planner.event.fragments.EventArgumentNames;
import com.team25.event.planner.event.fragments.ProductPurchaseListFragment;
import com.team25.event.planner.event.fragments.PurchaseListFragment;
import com.team25.event.planner.event.viewmodel.PurchaseViewModel;
import com.team25.event.planner.offering.adapters.ProductPurchaseAdapter;
import com.team25.event.planner.product.adapters.ImageSliderProductAdapter;
import com.team25.event.planner.product.viewmodel.MyProductsViewModel;
import com.team25.event.planner.review.fragments.ReviewListFragment;
import com.team25.event.planner.service.fragments.ServiceDetailsFragment;
import com.team25.event.planner.service.fragments.ServiceListFragment;
import com.team25.event.planner.user.model.UserRole;

import java.util.Objects;


public class ProductDetailsFragment extends Fragment {

    public final static String OFFERING_ID = "OFFERING_ID";
    public final static String OFFERING_NAME = "OFFERING_NAME";
    private NavController navController;
    private FragmentProductDetailsBinding binding;
    private MyProductsViewModel productViewModel;
    private PurchaseViewModel purchaseViewModel;
    private AuthViewModel authViewModel;
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
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        binding.setViewModel(productViewModel);
        navController = Navigation.findNavController(requireActivity(),R.id.nav_host_fragment );
        if (getArguments() != null) {
            _eventId = getArguments().getLong(EventArgumentNames.ID_ARG);
            _productId = getArguments().getLong(ProductPurchaseListFragment.PRODUCT_ID_ARG);
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
        if(Objects.requireNonNull(authViewModel.user.getValue()).getUserRole() == UserRole.EVENT_ORGANIZER){
            binding.chatButton.setVisibility(View.VISIBLE);
        }else{
            binding.chatButton.setVisibility(View.GONE);
        }
            if(_eventId != 0L){
                binding.buyButton.setVisibility(View.VISIBLE);
            }else{
                binding.buyButton.setVisibility(View.GONE);
            }

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

        purchaseViewModel.purchaseResponse.observe(getViewLifecycleOwner(), check ->{
            if (check) {
                DialogHelper.showSuccessDialog(requireContext(), "Successfully bought product: " + productViewModel.name.getValue());
            } else {
                DialogHelper.showErrorDialog(requireContext(), check.toString());
            }
        });
        purchaseViewModel.serverError.observe(getViewLifecycleOwner(), msg -> {
            Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show();
        });
    }

    private void setListeners(){
        binding.buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchaseViewModel.purchaseProduct(_productId);
            }
        });
        binding.chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putLong(ChatFragment.RECEIVER_ID_ARG, productViewModel.selectedProduct.getValue().getOwnerInfo().getId());
                bundle.putString(ChatFragment.RECEIVER_NAME_ARG, productViewModel.selectedProduct.getValue().getOwnerInfo().getName());
                navController.navigate(R.id.action_productDetailsFragment_to_chatFragment, bundle);
            }
        });
        binding.viewPurchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(PurchaseListFragment.EVENT_REVIEW, false);
                bundle.putLong(OFFERING_ID, _productId);
                bundle.putString(OFFERING_NAME, productViewModel.selectedProduct.getValue().getName());
                navController.navigate(R.id.action_productDetailsFragment_to_purchaseListFragment, bundle);
            }
        });
        binding.viewReviewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(PurchaseListFragment.EVENT_REVIEW, false);
                bundle.putLong(ReviewListFragment.OFFERING_ID,_productId);
                bundle.putString(ReviewListFragment.OFFERING_EVENT_NAME, productViewModel.selectedProduct.getValue().getName());
                navController.navigate(R.id.action_productDetailsFragment_to_reviewListFragment, bundle);
            }
        });
    }
}