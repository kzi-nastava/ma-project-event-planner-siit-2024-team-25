package com.team25.event.planner.communication.fragments;

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
import com.team25.event.planner.communication.adapters.ChatRoomAdapter;
import com.team25.event.planner.communication.viewmodel.ChatRoomViewModel;
import com.team25.event.planner.communication.viewmodel.MyChatMessageViewModel;
import com.team25.event.planner.core.viewmodel.AuthViewModel;
import com.team25.event.planner.databinding.FragmentChatBinding;
import com.team25.event.planner.databinding.FragmentChatRoomBinding;
import com.team25.event.planner.offering.adapters.ProductPriceListAdapter;


public class ChatRoomFragment extends Fragment implements ChatRoomAdapter.OnClickChat {

    private FragmentChatRoomBinding binding;
    private ChatRoomViewModel viewModel;
    private AuthViewModel authViewModel;
    private Long senderId;
    private ListView listView;
    private NavController navController;
    private ChatRoomAdapter adapter;

    public ChatRoomFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatRoomBinding.inflate(inflater,container,false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        viewModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        senderId = authViewModel.getUserId();
        viewModel.senderId.postValue(senderId);
        binding.setViewModel(viewModel);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = binding.listView;
        setUpObservers();
        setUpListeners();

        viewModel.getChats();
    }

    @Override
    public void onResume() {
        super.onResume();
        listView = binding.listView;
        setUpObservers();
        setUpListeners();

        viewModel.getChats();
    }

    private void setUpListeners() {
    }

    private void setUpObservers() {
        viewModel.chats.observe(getViewLifecycleOwner(), res->{
            adapter = new ChatRoomAdapter(requireContext(), res);
            adapter.setOnClickListener(this);
            listView.setAdapter(adapter);
        });
    }

    @Override
    public void onButtonClick(Long senderId, Long receiverId) {

    }
}