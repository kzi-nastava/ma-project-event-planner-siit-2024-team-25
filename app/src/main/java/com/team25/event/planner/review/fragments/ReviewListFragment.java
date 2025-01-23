package com.team25.event.planner.review.fragments;

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

import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentReviewListBinding;
import com.team25.event.planner.event.adapters.PurchaseListAdapter;
import com.team25.event.planner.event.fragments.PurchaseListFragment;
import com.team25.event.planner.review.adapters.ReviewRecyclerAdapter;
import com.team25.event.planner.review.viewmodels.ReviewViewModel;


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
    }

    private void setObservers() {
        viewModel.reviews.observe(getViewLifecycleOwner(), lists->{
            adapter = new ReviewRecyclerAdapter(requireContext(), lists);
            recyclerView.setAdapter(adapter);
        });
    }
}