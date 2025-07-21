package com.team25.event.planner.review.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentReviewListBinding;
import com.team25.event.planner.event.adapters.PurchaseListAdapter;
import com.team25.event.planner.event.fragments.PurchaseListFragment;
import com.team25.event.planner.review.adapters.ReviewRecyclerAdapter;
import com.team25.event.planner.review.viewmodels.ReviewViewModel;

import java.util.ArrayList;


public class ReviewListFragment extends Fragment {

    public final static String OFFERING_ID = "OFFERING_ID";

    public final static String EVENT_ID = "EVENT_ID";
    public final static String OFFERING_EVENT_NAME = "OFFERING_EVENT_NAME";
    private Boolean eventReviews;
    private Long eventId;
    private Long offeringId;
    private String offeringEventName;
    private FragmentReviewListBinding binding;
    private ReviewViewModel viewModel;
    private RecyclerView recyclerView;
    private ReviewRecyclerAdapter adapter;
    private NavController navController;


    public ReviewListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReviewListBinding.inflate(inflater,container,false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        viewModel = new ReviewViewModel();
        binding.setViewModel(viewModel);
        navController = Navigation.findNavController(requireActivity(),R.id.nav_host_fragment );
        if(getArguments()!= null){
            Boolean eventReview = getArguments().getBoolean(PurchaseListFragment.EVENT_REVIEW);
            this.eventReviews = eventReview;
            viewModel._eventReview.postValue(this.eventReviews);
            if(eventReview){
                this.eventId = getArguments().getLong(EVENT_ID);
                this.offeringEventName = getArguments().getString(OFFERING_EVENT_NAME);

            }else{
                this.offeringId = getArguments().getLong(OFFERING_ID);
                this.offeringEventName = getArguments().getString(OFFERING_EVENT_NAME);
            }
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ReviewRecyclerAdapter(requireContext(), new ArrayList<>());
        recyclerView.setAdapter(adapter);
        binding.title.setText(offeringEventName + "`s reviews");
        setObservers();
        setListeners();
        if(eventReviews){
            viewModel.getReviewsByEvent(eventId);
        }else{
            viewModel.getReviewByOffering(offeringId);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setListeners() {
        binding.btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(eventReviews){
                    viewModel.getPreviousPage(eventId);
                }else{
                    viewModel.getPreviousPage(offeringId);
                }

            }
        });
        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(eventReviews){
                    viewModel.getNextPage(eventId);
                }else{
                    viewModel.getNextPage(offeringId);
                }

            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void setObservers() {
        viewModel.created.observe(getViewLifecycleOwner(), check -> {
            if(check){
                Toast.makeText(requireContext(), "You successfully created a review", Toast.LENGTH_SHORT).show();
            }
        });
        viewModel.reviews.observe(getViewLifecycleOwner(), lists->{
            adapter.updateData(lists);
        });
        viewModel.paginationChanged.observe(getViewLifecycleOwner(), check->{
            if(check){
                binding.tvPageInfo.setText(String.valueOf(viewModel.getCurrentPage()+1) + " / " + String.valueOf(viewModel.getTotalPages()));
                if(viewModel.getCurrentPage() == 0){
                    binding.btnPrevious.setEnabled(false);
                }else{
                    binding.btnPrevious.setEnabled(true);
                }
                if(viewModel.getCurrentPage()+1 == viewModel.getTotalPages()){
                    binding.btnNext.setEnabled(false);
                }else{
                    binding.btnNext.setEnabled(true);
                }
            }
        });
    }
}