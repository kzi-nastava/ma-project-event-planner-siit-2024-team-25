package com.team25.event.planner.event.adapters;

import android.content.Context;
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

import com.google.android.material.card.MaterialCardView;
import com.team25.event.planner.R;

import com.team25.event.planner.event.model.EventCard;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TopEventsListAdapter extends ArrayAdapter<EventCard>{

    private List<EventCard> eventCards;

    public TopEventsListAdapter(Context context, List<EventCard> eventCards) {
        super(context, R.layout.home_page_top_event, eventCards);
        this.eventCards = eventCards;
    }

    @Override
    public int getCount() {
        return eventCards.size();
    }

    @Nullable
    @Override
    public EventCard getItem(int position) {
        return eventCards.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        EventCard event = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.home_page_top_event,
                    parent, false);
        }
        MaterialCardView eventCard = convertView.findViewById(R.id.top_event_card_item);
        TextView eventName = convertView.findViewById(R.id.top_event_name);
        TextView eventOrganizer = convertView.findViewById(R.id.top_event_organizer);
        TextView eventDate = convertView.findViewById(R.id.top_event_date);
        TextView eventTime = convertView.findViewById(R.id.top_event_time);
        ImageView eventIcon = convertView.findViewById(R.id.top_event_picture);
        ImageView eventLocationImage = convertView.findViewById(R.id.top_event_location_image);
        TextView eventLocation = convertView.findViewById(R.id.top_event_location);

        if(event != null){
            eventName.setText(event.getName());
            eventOrganizer.setText(event.getOrganizerName());


            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            String formattedDate = event.getStartDateTime().format(dateFormatter);
            String formattedTime = event.getStartDateTime().format(timeFormatter);

            eventDate.setText(formattedDate);
            eventTime.setText(formattedTime);


            eventIcon.setImageResource(R.drawable.ic_heart);
            eventLocation.setText(event.getCountry() + ", " + event.getCity());
            eventLocationImage.setImageResource(R.drawable.ic_location);


            boolean[] isClicked = {false};
            eventIcon.setOnClickListener(v -> {
                isClicked[0] = !isClicked[0];
                if(isClicked[0]){
                    eventIcon.setImageResource(R.drawable.ic_heart_red);
                    Toast.makeText(getContext(), "You add " + event.getName() +
                            " to your favourite list", Toast.LENGTH_SHORT).show();
                }
                else{
                    eventIcon.setImageResource(R.drawable.ic_heart);
                    Toast.makeText(getContext(), "You remove " + event.getName() +
                            " from your favourite list", Toast.LENGTH_SHORT).show();
                }
            });


            eventCard.setOnClickListener(v -> {
                Log.i("ShopApp", "Clicked: " + event.getName() + ", id: " +
                        event.getId());
                Toast.makeText(getContext(), "Clicked: " + event.getName() +
                        ", id: " + event.getId(), Toast.LENGTH_SHORT).show();
            });
        }

        return convertView;
    }
}

