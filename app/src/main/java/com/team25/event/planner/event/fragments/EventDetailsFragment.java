package com.team25.event.planner.event.fragments;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.team25.event.planner.R;
import com.team25.event.planner.communication.fragments.ChatFragment;
import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.core.fragments.MapFragment;
import com.team25.event.planner.core.viewmodel.AuthViewModel;
import com.team25.event.planner.databinding.FragmentEventDetailsBinding;
import com.team25.event.planner.event.model.Event;
import com.team25.event.planner.event.model.PrivacyType;
import com.team25.event.planner.event.viewmodel.EventDetailsViewModel;
import com.team25.event.planner.review.fragments.ReviewListFragment;
import com.team25.event.planner.user.model.UserRole;

import java.time.format.DateTimeFormatter;


public class EventDetailsFragment extends Fragment {
    private FragmentEventDetailsBinding binding;
    private NavController navController;
    private EventDetailsViewModel viewModel;
    private AuthViewModel authViewModel;
    private Long _eventId;
    private String _invitationCode;
    private FragmentEventDetailsBinding _binding;
    private final MediatorLiveData<Boolean> isOrganizer = new MediatorLiveData<>(false);

    public EventDetailsFragment() {
    }

    public static EventDetailsFragment newInstance() {
        EventDetailsFragment fragment = new EventDetailsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_details, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        viewModel = new ViewModelProvider(this).get(EventDetailsViewModel.class);
        binding.setViewModel(viewModel);

        setupObservers();
        setupListeners();

        if (getArguments() != null) {
            long eventId = getArguments().getLong(EventArgumentNames.ID_ARG);
            String invitationCode = getArguments().getString(EventArgumentNames.INVITATION_CODE_ARG);
            if (eventId != -1) {
                viewModel.loadEvent(eventId, invitationCode);
            }
        }

        return binding.getRoot();
    }

    private void setupObservers() {
        authViewModel.user.observe(getViewLifecycleOwner(), user -> {
            if (user == null) {
                viewModel.setUserId(null);
            } else {
                viewModel.setUserId(user.getId());
                if (user.getUserRole().equals(UserRole.ADMINISTRATOR)) {
                    binding.adminActions.setVisibility(View.VISIBLE);
                } else {
                    binding.adminActions.setVisibility(View.GONE);
                }
            }
        });

        isOrganizer.addSource(viewModel.event, event -> {
            Long userId = authViewModel.getUserId();
            isOrganizer.setValue(event != null && event.getOrganizer().getId().equals(userId));
        });

        isOrganizer.addSource(authViewModel.user, user -> {
            Event event = viewModel.event.getValue();
            Long userId = user == null ? null : user.getId();
            isOrganizer.setValue(event != null && event.getOrganizer().getId().equals(userId));
        });

        isOrganizer.observe(getViewLifecycleOwner(), isOrganizer -> {
            if (isOrganizer) {
                binding.organizerActions.setVisibility(View.VISIBLE);
                binding.viewAgendaButton.setText(R.string.edit_agenda);
            } else {
                binding.organizerActions.setVisibility(View.GONE);
                binding.viewAgendaButton.setText(R.string.view_agenda);
            }

            if (!isOrganizer && Boolean.TRUE.equals(viewModel.isAttending.getValue())) {
                binding.chatButton.setVisibility(View.VISIBLE);
            } else {
                binding.chatButton.setVisibility(View.GONE);
            }

            boolean isPublic = viewModel.event.getValue() != null && viewModel.event.getValue().getPrivacyType().equals(PrivacyType.PUBLIC);
            if (isPublic && !isOrganizer && !Boolean.TRUE.equals(viewModel.isAttending.getValue())) {
                binding.joinButton.setVisibility(View.VISIBLE);
            } else {
                binding.joinButton.setVisibility(View.GONE);
            }
        });

        viewModel.event.observe(getViewLifecycleOwner(), event -> {
            if (event == null) return;
            binding.dateTime.setText(getDateTimeString(event));
            if (event.getPrivacyType().equals(PrivacyType.PUBLIC)) {
                binding.privacyType.setText(R.string.public_event_label);
                binding.inviteButton.setVisibility(View.GONE);
            } else {
                binding.privacyType.setText(R.string.private_event_label);
                binding.inviteButton.setVisibility(View.VISIBLE);
            }
            if (event.getIsFavorite()) {
                binding.favoriteButton.setImageResource(R.drawable.ic_heart_red);
            } else {
                binding.favoriteButton.setImageResource(R.drawable.ic_favorite_border);
            }

            boolean isOrganizer = Boolean.TRUE.equals(this.isOrganizer.getValue());
            boolean isAttending = Boolean.TRUE.equals(viewModel.isAttending.getValue());
            if (isAttending && !isOrganizer) {
                binding.chatButton.setVisibility(View.VISIBLE);
            } else {
                binding.chatButton.setVisibility(View.GONE);
            }
            if (event.getPrivacyType().equals(PrivacyType.PUBLIC) && !isOrganizer && !isAttending) {
                binding.joinButton.setVisibility(View.VISIBLE);
            } else {
                binding.joinButton.setVisibility(View.GONE);
            }

            viewModel.checkAttendance();
        });

        viewModel.isAttending.observe(getViewLifecycleOwner(), isAttending -> {
            if (isAttending && !Boolean.TRUE.equals(isOrganizer.getValue())) {
                binding.chatButton.setVisibility(View.VISIBLE);
            } else {
                binding.chatButton.setVisibility(View.GONE);
            }

            boolean isPublic = viewModel.event.getValue() != null && viewModel.event.getValue().getPrivacyType().equals(PrivacyType.PUBLIC);
            if (isPublic && !isAttending && !Boolean.TRUE.equals(isOrganizer.getValue())) {
                binding.joinButton.setVisibility(View.VISIBLE);
            } else {
                binding.joinButton.setVisibility(View.GONE);
            }
        });

        viewModel.addToFavoritesSuccessSignal.observe(getViewLifecycleOwner(), succeeded -> {
            if (succeeded) {
                Toast.makeText(getContext(), R.string.added_to_favorites, Toast.LENGTH_SHORT).show();
                viewModel.addToFavoritesSuccessSignal.setValue(false);
            }
        });

        viewModel.removeFromFavoritesSuccessSignal.observe(getViewLifecycleOwner(), succeeded -> {
            if (succeeded) {
                Toast.makeText(getContext(), R.string.removed_from_favorites, Toast.LENGTH_SHORT).show();
                viewModel.removeFromFavoritesSuccessSignal.setValue(false);
            }
        });

        viewModel.serverError.observe(getViewLifecycleOwner(), errorMessage -> {
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
        });
    }

    private void setupListeners() {
        binding.showOnMapButton.setOnClickListener(v -> showOnMap());
        binding.viewAgendaButton.setOnClickListener(v -> goToAgenda());
        binding.inviteButton.setOnClickListener(v -> goToInvite());
        binding.budgetPlanButton.setOnClickListener(v -> goToBudgetPlan());
        binding.purchaseButton.setOnClickListener(v -> goToPurchase());
        binding.purchaseListButton.setOnClickListener(v -> goToPurchaseList());
        binding.joinButton.setOnClickListener(v -> joinEvent());
        binding.chatButton.setOnClickListener(v -> goToChat());
        binding.viewStatsButton.setOnClickListener(v -> goToStats());
        binding.viewStatsAdminButton.setOnClickListener(v -> goToStats());
        binding.downloadReportButton.setOnClickListener(v -> downloadReport());
        binding.downloadReportAdminButton.setOnClickListener(v -> downloadReport());
        binding.reviewsListButton.setOnClickListener(v->goToReviewList());
    }

    private void showOnMap() {
        final Event event = viewModel.event.getValue();
        if (event == null) return;

        Bundle args = new Bundle();
        args.putString(MapFragment.TITLE_ARG, event.getName());
        args.putParcelable(MapFragment.LOCATION_ARG, event.getLocation());
        navController.navigate(R.id.mapFragment, args);
    }

    private void goToAgenda() {
        final Event event = viewModel.event.getValue();
        if (event == null) return;

        Bundle args = new Bundle();
        args.putLong(EventArgumentNames.ID_ARG, event.getId());
        args.putString(EventArgumentNames.NAME_ARG, event.getName());
        args.putBoolean(EventArgumentNames.IS_ORGANIZER_ARG, Boolean.TRUE.equals(isOrganizer.getValue()));
        args.putBoolean(EventArgumentNames.CAME_FROM_DETAILS_ARG, true);
        navController.navigate(R.id.action_eventDetailsFragment_to_agendaFragment, args);
    }

    private void goToInvite() {
        final Event event = viewModel.event.getValue();
        if (event == null) return;

        Bundle args = new Bundle();
        args.putLong(EventArgumentNames.ID_ARG, event.getId());
        args.putString(EventArgumentNames.NAME_ARG, event.getName());
        args.putBoolean(EventArgumentNames.CAME_FROM_DETAILS_ARG, true);
        navController.navigate(R.id.action_eventDetailsFragment_to_eventInvitation, args);
    }

    private void goToBudgetPlan() {
        final Event event = viewModel.event.getValue();
        if (event == null) return;

        Bundle args = new Bundle();
        args.putLong(EventArgumentNames.ID_ARG, event.getId());
        args.putString(EventArgumentNames.NAME_ARG, event.getName());
        args.putLong(EventArgumentNames.EVENT_TYPE_ID, event.getEventType().id);
        navController.navigate(R.id.action_eventDetailsFragment_to_budgetItemFragment, args);
    }

    private void goToPurchase() {
        Bundle bundle = new Bundle();
        bundle.putLong(EventArgumentNames.ID_ARG, _eventId);
        navController.navigate(R.id.eventPurchaseFragment, bundle);
    }

    private void goToPurchaseList() {
        Bundle bundle = new Bundle();
        bundle.putLong(EventArgumentNames.ID_ARG, _eventId);
        bundle.putString(EventArgumentNames.NAME_ARG,viewModel.event.getValue().getName());
        bundle.putBoolean(PurchaseListFragment.EVENT_REVIEW, true);
        navController.navigate(R.id.action_eventDetailsFragment_to_purchaseListFragment,bundle);
    }
    private void goToReviewList(){
        Bundle bundle = new Bundle();
        bundle.putString(ReviewListFragment.OFFERING_EVENT_NAME, viewModel.event.getValue().getName());
        bundle.putLong(ReviewListFragment.EVENT_ID, _eventId);
        bundle.putBoolean(PurchaseListFragment.EVENT_REVIEW,true);
        navController.navigate(R.id.action_eventDetailsFragment_to_reviewListFragment, bundle);
    }

    private void joinEvent() {
        viewModel.joinEvent();
    }

    private void goToChat() {
        final Event event = viewModel.event.getValue();
        if (event == null) return;

        Bundle args = new Bundle();
        args.putLong(ChatFragment.RECEIVER_ID_ARG, event.getOrganizer().getId());
        args.putString(ChatFragment.RECEIVER_NAME_ARG, event.getOrganizer().getName());
        navController.navigate(R.id.action_eventDetailsFragment_to_chatFragment, args);
    }

    private void goToStats() {
        final Event event = viewModel.event.getValue();
        if (event == null) return;

        Bundle args = new Bundle();
        args.putLong(EventArgumentNames.ID_ARG, event.getId());
        args.putString(EventArgumentNames.NAME_ARG, event.getName());
        navController.navigate(R.id.action_eventDetailsFragment_to_eventStatsFragment, args);
    }

    private void downloadReport() {
        final Event event = viewModel.event.getValue();
        if (event == null) {
            Toast.makeText(getContext(), "Unable to download report", Toast.LENGTH_SHORT).show();
            return;
        }

        final String url = ConnectionParams.BASE_URL + "api/events/" + event.getId() + "/report";
        final String filename = "event_" + event.getId() + "_" + System.currentTimeMillis() + ".pdf";

        Log.d("TAG", url);

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle(event.getName() + " details report");
        request.setDescription("Downloading PDF report with event details for " + event.getName() + "...");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);

        DownloadManager downloadManager = (DownloadManager) requireContext().getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager != null) {
            downloadManager.enqueue(request);
            Toast.makeText(requireContext(), "Download started!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "Unable to download report", Toast.LENGTH_SHORT).show();
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        isOrganizer.removeSource(viewModel.event);
        isOrganizer.removeSource(authViewModel.user);
    }

    @NonNull
    private static String getDateTimeString(@NonNull Event event) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d MMM yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm");

        String startDate = event.getStartDate().format(dateFormatter);
        String startTime = event.getStartTime().format(timeFormatter);
        String endDate = event.getEndDate().format(dateFormatter);
        String endTime = event.getEndTime().format(timeFormatter);

        // If same day event, only show date once
        String formattedDateTime;
        if (event.getStartDate().equals(event.getEndDate())) {
            formattedDateTime = String.format("%s, %s - %s",
                    startDate,
                    startTime,
                    endTime
            );
        } else {
            formattedDateTime = String.format("%s %s - %s %s",
                    startDate,
                    startTime,
                    endDate,
                    endTime
            );
        }
        return formattedDateTime;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            _eventId = arguments.getLong(EventArgumentNames.ID_ARG);
        }
    }
}