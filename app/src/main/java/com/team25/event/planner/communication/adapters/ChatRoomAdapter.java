package com.team25.event.planner.communication.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.team25.event.planner.R;
import com.team25.event.planner.communication.model.ChatRoom;
import com.team25.event.planner.core.listeners.OnDetailsClickListener;
import com.team25.event.planner.databinding.ChatItemBinding;

import java.util.List;

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ChatRoomViewHolder>{
    private List<ChatRoom> chats;
    private OnClickChat onClickChat;

    @NonNull
    @Override
    public ChatRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ChatItemBinding binding = ChatItemBinding.inflate(inflater,parent,false);
        return new ChatRoomViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRoomViewHolder holder, int position) {
        final ChatRoom chatRoom = chats.get(position);
        holder.bind(chatRoom, onClickChat,position);
    }

    @Override
    public int getItemCount() {
        return chats != null ? chats.size() : 0;
    }

    public interface OnClickChat {
        void onButtonClick(Long receiverId, String receiverName);

    }
    public ChatRoomAdapter(List<ChatRoom> list, OnClickChat listener){
        this.chats = list;
        this.onClickChat = listener;
    }

    public static class ChatRoomViewHolder extends RecyclerView.ViewHolder{
        private final ChatItemBinding binding;

        public ChatRoomViewHolder(ChatItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        public void bind(ChatRoom chatRoom, OnClickChat onClickChat, int position){
            binding.setChatRoom(chatRoom);
            binding.executePendingBindings();
            binding.userName.setText(chatRoom.getReceiver().getFirstName() + " " + chatRoom.getReceiver().getLastName());
            binding.userRole.setText(chatRoom.getReceiver().getUserRole().toString());
            binding.getRoot().setOnClickListener(v->onClickChat.onButtonClick(chatRoom.getReceiver().getId(),chatRoom.getReceiver().getFirstName() + " " + chatRoom.getReceiver().getLastName()));
        }
    }

}
