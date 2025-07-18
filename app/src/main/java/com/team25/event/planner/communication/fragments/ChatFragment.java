package com.team25.event.planner.communication.fragments;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.team25.event.planner.R;
import com.team25.event.planner.communication.model.ChatMessage;
import com.team25.event.planner.communication.viewmodel.BlockViewModel;
import com.team25.event.planner.communication.viewmodel.MyChatMessageViewModel;
import com.team25.event.planner.core.SharedPrefService;
import com.team25.event.planner.core.viewmodel.AuthViewModel;
import com.team25.event.planner.databinding.FragmentChatBinding;
import com.team25.event.planner.user.model.User;

import java.util.Objects;

public class ChatFragment extends Fragment {

    public static final String RECEIVER_ID_ARG = "RECEIVER_ID_ARG";
    public static final String RECEIVER_NAME_ARG = "RECEIVER_NAME_ARG";
    private NavController navController;
    private FragmentChatBinding binding;
    private MyChatMessageViewModel viewModel;
    private BlockViewModel blockViewModel;
    private LinearLayout chatMessagesContainer;
    private AuthViewModel authViewModel;
    private Long receiverId;
    private String receiverName;
    private Long senderId;

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater,container,false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        viewModel = new ViewModelProvider(this).get(MyChatMessageViewModel.class);
        blockViewModel = new ViewModelProvider(this).get(BlockViewModel.class);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        binding.setViewModel(viewModel);
        binding.setBlockViewModel(blockViewModel);
        navController = Navigation.findNavController(requireActivity(),R.id.nav_host_fragment );
        if(getArguments()!= null){
            receiverId= getArguments().getLong(RECEIVER_ID_ARG, -1L);
            receiverName=getArguments().getString(RECEIVER_NAME_ARG, "");
        }
        senderId = authViewModel.getUserId();
        viewModel.receiverId.postValue(receiverId);
        viewModel.receiverName.postValue(receiverName);
        viewModel.senderId.postValue(senderId);
        blockViewModel.currentUser.setValue(senderId);
        blockViewModel.blockedUser.setValue(receiverId);
        //openWebSocketConnection();
        return binding.getRoot();
    }

    private void openWebSocketConnection(){
        User user = authViewModel.user.getValue();
        if(user != null){
            viewModel.connectToSocket(user);
        }
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        chatMessagesContainer = binding.chatMessagesContainer;
        setUpObservers();
        setUpListeners();

        blockViewModel.isChatBlocked();
    }

    @Override
    public void onResume() {
        super.onResume();
        chatMessagesContainer = binding.chatMessagesContainer;
        setUpObservers();
        setUpListeners();

        blockViewModel.isChatBlocked();
    }
    private void setUpListeners() {
        binding.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.sendMessage();
            }
        });
    }
    private void setUpObservers() {
        blockViewModel.isBlocked.observe(getViewLifecycleOwner(), isBlocked ->{
            if(isBlocked){
                binding.sendButton.setEnabled(false);
                binding.messageInput.setText(R.string.current_user_was_blocked);
                binding.messageInput.setEnabled(false);
                binding.blockUserButton.setEnabled(false);
            }else{
                binding.sendButton.setEnabled(true);
                viewModel.getChat(senderId,receiverId);
            }
        });
        viewModel.chatMessages.observe(getViewLifecycleOwner(), res->{
            clearMessages();
            for (int i = res.size() - 1; i >= 0; i--) {
                ChatMessage cm = res.get(i);

                if(Objects.equals(cm.getSender().getId(), viewModel.senderId.getValue())){
                    addMessageTextView(cm.getContent(),true, cm.getTimestamp().toString());
                }else{
                    addMessageTextView(cm.getContent(),false, cm.getTimestamp().toString());
                }
            }



        });
        viewModel.serverError.observe(getViewLifecycleOwner(), mess->{
            if(mess != null){
                Toast.makeText(requireContext(), mess, Toast.LENGTH_SHORT).show();
            }
        });
        viewModel.isLoading.observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                binding.loadingSpinner.setVisibility(View.VISIBLE);
            } else {
                binding.loadingSpinner.setVisibility(View.GONE);
            }
        });
        viewModel.sendMessage.observe(getViewLifecycleOwner(), res->{
            if(res){
                viewModel.message.postValue("");
                viewModel.setCurrentPage(0);
            }
        });

        viewModel.canOpenConnection.observe(getViewLifecycleOwner(), check -> {
            if(check){
                openWebSocketConnection();
            }
        });
    }
    public void addMessageTextView(String message, boolean isSender, String date){
        LinearLayout messageLayout = new LinearLayout(requireContext());
        messageLayout.setOrientation(LinearLayout.VERTICAL);

        TextView messageView = new TextView(requireContext());
        messageView.setText(message);
        messageView.setPadding(16, 8, 16, 8);

        int leftMargin = 0;
        int rightMargin = 0;

        if (isSender) {
            messageView.setTextColor(Color.WHITE);
            messageView.setGravity(Gravity.END);
            leftMargin = 128;
            rightMargin = 8;
        } else {
            messageView.setTextColor(Color.BLACK);
            messageView.setGravity(Gravity.START);
            rightMargin = 128;
            leftMargin = 8;
        }

        GradientDrawable backgroundDrawable = new GradientDrawable();
        backgroundDrawable.setColor(ContextCompat.getColor(requireActivity(), isSender ? R.color.primary : R.color.ic_launcher_background));
        backgroundDrawable.setCornerRadius(16f);
        messageView.setBackground(backgroundDrawable);

        TextView dateView = new TextView(requireContext());
        dateView.setText(date);
        dateView.setTextSize(12);
        dateView.setTextColor(Color.GRAY);
        dateView.setGravity(isSender ? Gravity.END : Gravity.START);

        messageLayout.addView(messageView);
        messageLayout.addView(dateView);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(leftMargin, 8, rightMargin, 8);
        messageLayout.setLayoutParams(params);

        chatMessagesContainer.addView(messageLayout);
        chatMessagesContainer.requestLayout();
        chatMessagesContainer.invalidate();

    }
    public void clearMessages() {
        chatMessagesContainer.removeAllViews();
    }
}