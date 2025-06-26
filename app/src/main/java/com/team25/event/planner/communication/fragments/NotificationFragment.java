package com.team25.event.planner.communication.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
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

import com.team25.event.planner.MainActivity;
import com.team25.event.planner.R;
import com.team25.event.planner.communication.adapters.NotificationsListAdapter;
import com.team25.event.planner.communication.model.Notification;
import com.team25.event.planner.communication.model.NotificationCategory;
import com.team25.event.planner.communication.viewmodel.MyNotificationViewModel;
import com.team25.event.planner.communication.viewmodel.NotificationViewModel;
import com.team25.event.planner.core.SharedPrefService;
import com.team25.event.planner.core.viewmodel.AuthViewModel;
import com.team25.event.planner.databinding.FragmentNotificationBinding;
import com.team25.event.planner.event.adapters.HomeEventListAdapter;
import com.team25.event.planner.event.adapters.MyEventAdapter;
import com.team25.event.planner.event.fragments.EventArgumentNames;
import com.team25.event.planner.event.viewmodel.MyEventsViewModel;
import com.team25.event.planner.product.fragments.ProductFormFragment;
import com.team25.event.planner.user.model.UserRole;

import java.util.ArrayList;
import java.util.Objects;

public class NotificationFragment extends Fragment {
    private AuthViewModel _authViewModel;
    private FragmentNotificationBinding _binding;
    private NotificationsListAdapter _adapter;
    private NavController _navController;
    private MyNotificationViewModel _notificationViewModel;

    public NotificationFragment() {
    }

    public static NotificationFragment newInstance() {
        NotificationFragment fragment = new NotificationFragment();
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
        _binding = FragmentNotificationBinding.inflate(inflater, container, false);
        _binding.setLifecycleOwner(getViewLifecycleOwner());
        _notificationViewModel = new ViewModelProvider(this).get(MyNotificationViewModel.class);
        _authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        SharedPrefService sharedPrefService = new SharedPrefService(getActivity());
        _authViewModel.initialize(sharedPrefService);
        _navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        setupNotificationList();
        setupObservers();
        setupListener();

        _notificationViewModel.loadNextPage();

        return _binding.getRoot();
    }

    private void setupListener() {
        _binding.toggleNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                _authViewModel.notificationOn();
            } else {
                _authViewModel.notificationOff();
            }
        });
    }


    private void setupNotificationList() {
        _adapter = new NotificationsListAdapter(new ArrayList<>(), new NotificationsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Notification notification) {
                _authViewModel.user.observe(getViewLifecycleOwner(),user -> {
                    if(user != null){
                        NotificationCategory notificationCategory = notification.getNotificationCategory();
                        UserRole userRole = user.getUserRole();
                        Bundle bundle = new Bundle();
                        _notificationViewModel.notification.observe(getViewLifecycleOwner(), currentNotification ->{
                            if(notificationCategory == NotificationCategory.OFFERING_CATEGORY){
                                if(userRole == UserRole.ADMINISTRATOR){
                                    _navController.navigate(R.id.offeringCategoryFragment);
                                }else {
                                    if(notification.getTitle().contains("Product")){
                                        _navController.navigate(R.id.myProductsFragment);
                                    } else if (notification.getTitle().contains("Service")) {
                                        _navController.navigate(R.id.ownerHomePage);
                                    }
                                }
                            }else if(notificationCategory == NotificationCategory.EVENT){
                                bundle.putLong(EventArgumentNames.ID_ARG, notification.getEntityId());
                                _navController.navigate(R.id.eventDetailsFragment, bundle);
                            }else if(notificationCategory == NotificationCategory.PRODUCT){
                                bundle.putLong(ProductFormFragment.ID_ARG_NAME, notification.getEntityId());
                                _navController.navigate(R.id.productDetailsFragment, bundle);
                            }else if(notificationCategory == NotificationCategory.SERVICE){
                                bundle.putLong("OFFERING_ID", notification.getEntityId());
                                _navController.navigate(R.id.serviceDetailsFragment, bundle);
                            }
                        });
                        notification.setIsViewed(true);
                        _notificationViewModel.updateNotification(notification);
                    }
                });
            }

            @Override
            public void onStatusIconClick(Notification notification, int position) {
                notification.setIsViewed(!notification.getIsViewed());
                _notificationViewModel.notification.observe(getViewLifecycleOwner(), currentNotification ->{
                    if(currentNotification != null && Objects.equals(notification.getId(), currentNotification.getId())){
                        _adapter.notifyItemChanged(position);
                    }
                });
                _notificationViewModel.updateNotification(notification);
            }
        });
        _binding.recyclerViewNotifications.setAdapter(_adapter);

        _notificationViewModel.notifications.observe(getViewLifecycleOwner(), _adapter::addNotification);

        _binding.recyclerViewNotifications.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager == null) return;

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!_notificationViewModel.isLoading() && !_notificationViewModel.isEndReached()) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0) {
                        _notificationViewModel.loadNextPage();
                    }
                }
            }
        });
    }

    private void setupObservers() {
        _notificationViewModel.isLoading.observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                _binding.loadingSpinner.setVisibility(View.VISIBLE);
            } else {
                _binding.loadingSpinner.setVisibility(View.GONE);
            }
        });

        _notificationViewModel.serverError.observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        _authViewModel.notification.observe(getViewLifecycleOwner(), notification -> {
            if(notification){
                ((MainActivity) requireActivity()).openWebSocket();
                _binding.toggleNotifications.setText("Notifications on");
                _binding.toggleNotifications.setChecked(true);
            }else if(!notification){
                ((MainActivity) requireActivity()).closeWebSocket();
                _binding.toggleNotifications.setText("Notifications off");
                _binding.toggleNotifications.setChecked(false);
            }
        });
    }
}