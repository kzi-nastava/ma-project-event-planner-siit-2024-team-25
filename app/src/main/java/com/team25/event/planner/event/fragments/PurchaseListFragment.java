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
import com.team25.event.planner.databinding.FragmentPurchaseListBinding;
import com.team25.event.planner.event.adapters.PurchaseListAdapter;
import com.team25.event.planner.event.viewmodel.PurchaseViewModel;
import com.team25.event.planner.offering.adapters.ProductPurchaseAdapter;
import com.team25.event.planner.service.model.Purchase;


public class PurchaseListFragment extends Fragment implements PurchaseListAdapter.OnClickReviewListener{

    private boolean eventReview;
    private Long eventId;
    private Long offeringId;
    private String offeringEventName;
    private ListView listView;
    private PurchaseViewModel viewModel;
    private FragmentPurchaseListBinding binding;
    private NavController navController;
    private PurchaseListAdapter adapter;
    public static final String EVENT_REVIEW = "EVENT_REVIEW";

    public PurchaseListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPurchaseListBinding.inflate(inflater,container,false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        viewModel = new PurchaseViewModel();
        binding.setViewModel(viewModel);
        navController = Navigation.findNavController(requireActivity(),R.id.nav_host_fragment );
        if(getArguments()!= null){
            boolean eventReview = getArguments().getBoolean(EVENT_REVIEW);
            this.eventReview = eventReview;
            if(eventReview){
                Long id = getArguments().getLong(EventArgumentNames.ID_ARG);
                String name = getArguments().getString(EventArgumentNames.NAME_ARG);
                this.eventId = id;
                this.offeringEventName = name;
            }else{

            }
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = binding.list;
        if(eventReview){
            binding.offeringOrEventNameTextView.setText("Offering name");
        }else{
            binding.offeringOrEventNameTextView.setText("Event name");
        }
        setObservers();
        setListeners();
        viewModel.getPurchaseByEvent(this.eventId);
    }

    @Override
    public void onResume() {
        super.onResume();
        setObservers();
        setListeners();
        viewModel.getPurchaseByEvent(this.eventId);
    }

    private void setListeners() {
    }

    private void setObservers() {
        viewModel.purchaseList.observe(getViewLifecycleOwner(), purchaseResponseDTOS -> {
            adapter = new PurchaseListAdapter(requireContext(), purchaseResponseDTOS, eventReview);
            adapter.setListener(this);
            listView.setAdapter(adapter);
        });
    }

    @Override
    public void onClick(Long id) {

    }
}