package com.team25.event.planner.event.fragments;

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
import android.widget.ListView;

import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentProductPurchaseListBinding;
import com.team25.event.planner.offering.adapters.HomeOfferingListAdapter;
import com.team25.event.planner.offering.adapters.ProductPurchaseAdapter;
import com.team25.event.planner.offering.model.ProductCard;
import com.team25.event.planner.offering.viewmodel.HomeOfferingViewModel;

import java.util.ArrayList;
import java.util.List;


public class ProductPurchaseListFragment extends Fragment {

   private FragmentProductPurchaseListBinding binding;
   private NavController navController;
   private ProductPurchaseAdapter adapter;
   private ListView listView;
 private HomeOfferingViewModel viewModel;

    public ProductPurchaseListFragment() {
        // Required empty public constructor
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
     viewModel = new ViewModelProvider(this).get(HomeOfferingViewModel.class);
     binding.setViewModel(viewModel);
     navController = Navigation.findNavController(requireActivity(),R.id.nav_host_fragment );
     return binding.getRoot();
    }

 @Override
 public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
  super.onViewCreated(view, savedInstanceState);
  listView = binding.list;
  List<ProductCard> list = new ArrayList<>();
  list.add(new ProductCard(1,"as",2,"as",1));
  adapter = new ProductPurchaseAdapter(requireContext(), list);
  listView.setAdapter(adapter);
 }


}