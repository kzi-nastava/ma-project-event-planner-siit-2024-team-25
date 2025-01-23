package com.team25.event.planner.event.fragments;

import android.app.AlertDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.team25.event.planner.MainActivity;
import com.team25.event.planner.R;
import com.team25.event.planner.core.viewmodel.AuthViewModel;
import com.team25.event.planner.databinding.FragmentPurchaseListBinding;
import com.team25.event.planner.event.adapters.PurchaseListAdapter;
import com.team25.event.planner.event.viewmodel.PurchaseViewModel;
import com.team25.event.planner.offering.adapters.ProductPurchaseAdapter;
import com.team25.event.planner.review.model.ReviewRequestDTO;
import com.team25.event.planner.review.model.ReviewType;
import com.team25.event.planner.review.viewmodels.ReviewViewModel;
import com.team25.event.planner.service.model.Purchase;


public class PurchaseListFragment extends Fragment implements PurchaseListAdapter.OnClickReviewListener{

    private boolean eventReview;
    private Long eventId;
    private Long offeringId;
    private String offeringEventName;
    private ListView listView;
    private PurchaseViewModel viewModel;
    private ReviewViewModel reviewViewModel;
    private AuthViewModel authViewModel;
    private FragmentPurchaseListBinding binding;
    private NavController navController;
    private PurchaseListAdapter adapter;
    private Long userId;
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
        reviewViewModel = new ReviewViewModel();
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        userId = authViewModel.getUserId();
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
        reviewViewModel.created.observe(getViewLifecycleOwner(), check ->{
            if(check){
                Toast.makeText(requireActivity(), "You successfully posted review.", Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(requireActivity(), "Review is not posted.", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onClick(Long id) {
        showRatingDialog(id);
    }

    private void showRatingDialog(Long id) {
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View dialogView = inflater.inflate(R.layout.dialog_rating, null);

        RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);
        EditText commentEditText = dialogView.findViewById(R.id.commentEditText);
        Button approveButton = dialogView.findViewById(R.id.approveButton);

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setCancelable(true)
                .create();

        approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rating = (int) ratingBar.getRating();
                String comment = commentEditText.getText().toString();

                if (rating == 0) {
                    Toast.makeText(requireActivity(), "Please select a rating.", Toast.LENGTH_SHORT).show();
                } else if (comment.isEmpty()) {
                    Toast.makeText(requireActivity(), "Comment cannot be empty.", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();

                    sendRatingToServer(rating, comment, id);
                }
            }
        });

        dialog.show();
    }

    private void sendRatingToServer(int rating, String comment, Long id){
        ReviewRequestDTO requestDTO = new ReviewRequestDTO(comment,rating, ReviewType.OFFERING_REVIEW,id,userId);
        reviewViewModel.postReview(requestDTO);
    }
}