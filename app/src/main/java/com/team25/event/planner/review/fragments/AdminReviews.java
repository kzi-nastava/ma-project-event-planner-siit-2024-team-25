package com.team25.event.planner.review.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.team25.event.planner.R;
import com.team25.event.planner.communication.adapters.NotificationsListAdapter;
import com.team25.event.planner.communication.model.Notification;
import com.team25.event.planner.communication.model.NotificationCategory;
import com.team25.event.planner.communication.viewmodel.MyNotificationViewModel;
import com.team25.event.planner.core.SharedPrefService;
import com.team25.event.planner.core.viewmodel.AuthViewModel;
import com.team25.event.planner.databinding.FragmentAdminReviewsBinding;
import com.team25.event.planner.databinding.FragmentNotificationBinding;
import com.team25.event.planner.event.fragments.EventArgumentNames;
import com.team25.event.planner.offering.dialogs.YesOrNoDialogFragment;
import com.team25.event.planner.product.fragments.ProductFormFragment;
import com.team25.event.planner.review.adapters.ReviewCardAdapter;
import com.team25.event.planner.review.dialogs.ApproveReviewDialog;
import com.team25.event.planner.review.model.ReviewCard;
import com.team25.event.planner.review.model.ReviewStatus;
import com.team25.event.planner.review.viewmodels.AdminReviewViewModel;
import com.team25.event.planner.user.model.UserRole;

import java.util.ArrayList;
import java.util.Objects;

public class AdminReviews extends Fragment {

    private AdminReviewViewModel _adminReviewViewModel;
    private FragmentAdminReviewsBinding _binding;
    private ReviewCardAdapter _adapter;
    private NavController _navController;

    public AdminReviews() {
    }

    public static AdminReviews newInstance(String param1, String param2) {
        AdminReviews fragment = new AdminReviews();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _binding = FragmentAdminReviewsBinding.inflate(inflater, container, false);
        _binding.setLifecycleOwner(getViewLifecycleOwner());
        _adminReviewViewModel = new ViewModelProvider(this).get(AdminReviewViewModel.class);
        _navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        setupReviewsList();
        setupObservers();

        _adminReviewViewModel.loadNextPage();

        return _binding.getRoot();
    }

    private void setupReviewsList() {
        _adapter = new ReviewCardAdapter(new ArrayList<>(), new ReviewCardAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(ReviewCard reviewCard, int position) {

                _adminReviewViewModel.review.observe(getViewLifecycleOwner(), review -> {
                    if(review != null && Objects.equals(reviewCard.getId(), review.getId())){
                        Toast.makeText(getContext(), "You updated review", Toast.LENGTH_SHORT).show();
                        _adapter.removeReview(position);
                        _adminReviewViewModel.isUpdate.setValue(true);
                        _adminReviewViewModel.loadNextPage();
                    }
                });

                ApproveReviewDialog dialog = new ApproveReviewDialog(new ApproveReviewDialog.ApproveReviewDialogListener() {

                    @Override
                    public void onApprove() {
                        _adminReviewViewModel.updateReview(reviewCard, ReviewStatus.APPROVED);
                    }

                    @Override
                    public void onReject() {
                        _adminReviewViewModel.updateReview(reviewCard, ReviewStatus.DENIED);
                    }

                    @Override
                    public void refresh() {
                    }
                });

                dialog.show(getParentFragmentManager(), "ConfirmDialogFragment");
            }
        });
        _binding.recyclerViewReviews.setAdapter(_adapter);

        _adminReviewViewModel.reviews.observe(getViewLifecycleOwner(), reviewCards -> {
            _adapter.addReview(reviewCards, this._adminReviewViewModel.isUpdate.getValue());
            _adminReviewViewModel.isUpdate.setValue(Boolean.FALSE.equals(_adminReviewViewModel.isUpdate.getValue()));
        });

        _binding.recyclerViewReviews.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager == null) return;

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!_adminReviewViewModel.isLoading() && !_adminReviewViewModel.isEndReached()) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0) {
                        _adminReviewViewModel.loadNextPage();
                    }
                }
            }
        });
    }

    private void setupObservers() {
        _adminReviewViewModel.isLoading.observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                _binding.loadingSpinner.setVisibility(View.VISIBLE);
            } else {
                _binding.loadingSpinner.setVisibility(View.GONE);
            }
        });

        _adminReviewViewModel.serverError.observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });


    }
}