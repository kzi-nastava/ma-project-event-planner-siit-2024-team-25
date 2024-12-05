package com.team25.event.planner.event.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavController;

import com.google.android.material.card.MaterialCardView;
import com.team25.event.planner.R;
import com.team25.event.planner.event.model.EventCard;
import com.team25.event.planner.event.model.Invitation;

import java.text.SimpleDateFormat;
import java.util.List;

public class EventInvitationsListAdapter extends ArrayAdapter<Invitation> {

    private List<Invitation> emails;
    private final MutableLiveData<Invitation> _deleteEmail = new MutableLiveData<>();
    public final LiveData<Invitation> deleteEmail = _deleteEmail;

    public EventInvitationsListAdapter(Context context, List<Invitation> emails) {
        super(context, R.layout.event_invitation_email, emails);
        this.emails = emails;
    }

    @Override
    public int getCount() {
        return emails.size();
    }


    @Nullable
    @Override
    public Invitation getItem(int position) {
        return emails.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Invitation email = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_invitation_email,
                    parent, false);
        }
        MaterialCardView emailCard = convertView.findViewById(R.id.event_invitation_card_item);
        TextView emailText = convertView.findViewById(R.id.event_invitation_email);
        ImageView deleteImage = convertView.findViewById(R.id.email_delete_icon);

        if(email != null){
            emailText.setText(email.getGuestEmail());
            deleteImage.setImageResource(R.drawable.delete_icon);

        }

        deleteImage.setOnClickListener(v -> {
            _deleteEmail.setValue(email);
        });

        return convertView;
    }
}