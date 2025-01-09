package com.team25.event.planner.event.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.team25.event.planner.R;
import com.team25.event.planner.core.dialogs.DialogHelper;
import com.team25.event.planner.core.listeners.OnPurchaseClickListener;
import com.team25.event.planner.databinding.FragmentProductPurchaseListBinding;
import com.team25.event.planner.event.viewmodel.PurchaseViewModel;
import com.team25.event.planner.offering.adapters.HomeOfferingListAdapter;
import com.team25.event.planner.offering.adapters.ProductPurchaseAdapter;
import com.team25.event.planner.offering.model.ProductCard;
import com.team25.event.planner.offering.viewmodel.HomeOfferingViewModel;

import java.util.ArrayList;
import java.util.List;


public class ProductPurchaseListFragment extends Fragment implements OnPurchaseClickListener {

   private FragmentProductPurchaseListBinding binding;
   private NavController navController;
   private ProductPurchaseAdapter adapter;
   private ListView listView;
    private PurchaseViewModel viewModel;
    private String productName;
    private Long eventid;

    public ProductPurchaseListFragment(Long eventId) {
        this.eventid = eventId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
     binding = FragmentProductPurchaseListBinding.inflate(inflater,container,false);
     binding.setLifecycleOwner(getViewLifecycleOwner());
     viewModel = new ViewModelProvider(this).get(PurchaseViewModel.class);
     binding.setViewModel(viewModel);
     navController = Navigation.findNavController(requireActivity(),R.id.nav_host_fragment );

     return binding.getRoot();
    }

 @Override
 public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
  super.onViewCreated(view, savedInstanceState);
  listView = binding.list;
  viewModel.eventId.postValue(this.eventid);
  setObservers();
  viewModel.getAllProducts();
 }

    @Override
    public void onResume() {
        super.onResume();
        setObservers();
        viewModel.getAllProducts();
    }

    private void setObservers(){
    viewModel.products.observe(getViewLifecycleOwner(), res ->{
        adapter = new ProductPurchaseAdapter(requireContext(), res);
        adapter.setPurchaseListener(this);
        listView.setAdapter(adapter);
    });
    viewModel.purchaseResponse.observe(getViewLifecycleOwner(), check ->{
        if (check) {
            DialogHelper.showSuccessDialog(requireContext(), "Successfully bought product: " + productName);
        } else {
            DialogHelper.showErrorDialog(requireContext(), "Error occurred, try again.");
        }
    });
 }



    @Override
    public void onPurchaseProductClick(Long productId, String productName) {
        this.productName = productName;
        viewModel.purchaseProduct(productId);
    }
}