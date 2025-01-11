package com.team25.event.planner.communication.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.team25.event.planner.R;
import com.team25.event.planner.communication.model.ChatMessage;
import com.team25.event.planner.communication.viewmodel.MyChatMessageViewModel;
import com.team25.event.planner.core.viewmodel.AuthViewModel;
import com.team25.event.planner.databinding.FragmentChatBinding;

import java.util.Objects;

public class ChatFragment extends Fragment {

    public static final String RECEIVER_ID_ARG = "RECEIVER_ID_ARG";
    public static final String RECEIVER_NAME_ARG = "RECEIVER_NAME_ARG";
    private NavController navController;
    private FragmentChatBinding binding;
    private MyChatMessageViewModel viewModel;
    private LinearLayout chatMessagesContainer;
    private AuthViewModel authViewModel;

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
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        binding.setViewModel(viewModel);
        navController = Navigation.findNavController(requireActivity(),R.id.nav_host_fragment );
        /*if(getArguments()!= null){
            viewModel.receiverId.postValue(getArguments().getLong(RECEIVER_ID_ARG, -1L));
            viewModel.receiverName.postValue(getArguments().getString(RECEIVER_NAME_ARG, ""));
        }*/
        //mock
        viewModel.receiverId.postValue(1L);
        viewModel.receiverName.postValue("admins");
        viewModel.senderId.postValue(2L);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        chatMessagesContainer = binding.chatMessagesContainer;
        setUpObservers();
        setUpListeners();

        viewModel.getChat();
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpObservers();
        setUpListeners();

        viewModel.getChat();
    }
    private void setUpListeners() {}
    private void setUpObservers() {
        viewModel.chatMessages.observe(getViewLifecycleOwner(), res->{
            for (ChatMessage cm:res) {
                if(Objects.equals(cm.getSender().getId(), viewModel.senderId.getValue())){
                    addMessageTextView(cm.getContent(),true);
                }else{
                    addMessageTextView(cm.getContent(),false);
                }

            }
        });
        viewModel.serverError.observe(getViewLifecycleOwner(), mess->{
            if(mess != null){
                Toast.makeText(requireContext(), mess, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void addMessageTextView(String message, boolean isSender){
        TextView messageView = new TextView(getContext());
        messageView.setText(message);
        messageView.setPadding(16, 8, 16, 8);

        if (isSender) {
            messageView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primary));
            messageView.setTextColor(Color.WHITE);
            messageView.setGravity(Gravity.END);
        } else {
            messageView.setBackgroundColor(Color.GRAY);
            messageView.setTextColor(Color.BLACK);
            messageView.setGravity(Gravity.START);
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(8, 8, 8, 8);
        messageView.setLayoutParams(params);

        chatMessagesContainer.addView(messageView);
    }
    public void clearMessages() {
        chatMessagesContainer.removeAllViews();
    }
}