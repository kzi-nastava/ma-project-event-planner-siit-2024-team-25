package com.team25.event.planner.communication.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.team25.event.planner.R;
import com.team25.event.planner.communication.model.ChatRoom;
import com.team25.event.planner.core.listeners.OnDetailsClickListener;

import java.util.List;

public class ChatRoomAdapter extends ArrayAdapter<ChatRoom> {
    private List<ChatRoom> chats;
    private OnClickChat onClickChat;
    public interface OnClickChat {
        void onButtonClick(Long senderId, Long receiverId);
    }
    public ChatRoomAdapter(Context context, List<ChatRoom> list){
        super(context, R.layout.chat_item, list);
        this.chats = list;
    }

    public void setOnClickListener(OnClickChat listener){
        this.onClickChat = listener;
    }

    @Override
    public int getCount() {
        return this.chats.size();
    }

    @Nullable
    @Override
    public ChatRoom getItem(int position) {
        return this.chats.get(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ChatRoom chatRoom = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.chat_item,parent,false);
        }
        RelativeLayout layout = convertView.findViewById(R.id.layout);
        ImageView image = convertView.findViewById(R.id.profile_image);
        TextView name = convertView.findViewById(R.id.user_name);
        TextView role = convertView.findViewById(R.id.user_role);
        if(chatRoom!= null){
            Glide.with(this.getContext())
                    .load(chatRoom.getReceiver().getProfilePictureUrl())
                    .placeholder(R.drawable.profile_icon)
                    .into(image);
            name.setText(chatRoom.getReceiver().getFirstName() + " " + chatRoom.getReceiver().getLastName());
            role.setText(chatRoom.getReceiver().getUserRole().toString());
            layout.setOnClickListener( v ->{
                if(onClickChat!= null){
                    onClickChat.onButtonClick(chatRoom.getSender().getId(),chatRoom.getReceiver().getId());
                }
            });
        }
        return convertView;
    }
}
