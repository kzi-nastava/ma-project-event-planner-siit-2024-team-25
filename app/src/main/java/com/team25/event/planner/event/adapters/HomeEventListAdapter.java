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
import java.util.ArrayList;
import java.util.List;

public class HomeEventListAdapter extends ArrayAdapter<EventCard> {

    private List<EventCard> eventCards;

    public HomeEventListAdapter(Context context, List<EventCard> eventCards) {
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
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.home_page_event_card,
                    parent, false);
        }
        MaterialCardView eventCard = convertView.findViewById(R.id.event_card_item);
        TextView eventName = convertView.findViewById(R.id.home_event_name);
        TextView eventOrganizer = convertView.findViewById(R.id.home_event_organizer);
        TextView eventDate = convertView.findViewById(R.id.home_event_date);
        ImageView eventIcon = convertView.findViewById(R.id.home_event_picture);
        ImageView eventLocationImage = convertView.findViewById(R.id.home_event_location_image);
        TextView eventLocation = convertView.findViewById(R.id.home_event_location);

        eventIcon.setImageResource(R.drawable.ic_heart);
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


        if (event != null) {
            eventName.setText(event.getName());
            eventOrganizer.setText(event.getOrganizer());

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            String formattedDate = dateFormat.format(event.getDate());
            eventDate.setText(formattedDate);
            eventLocation.setText(event.getLocation());
            eventLocationImage.setImageResource(R.drawable.ic_location_city);
            eventCard.setOnClickListener(v -> {
                // Handle click on the item at 'position'
                Log.i("ShopApp", "Clicked: " + event.getName() + ", id: " +
                        event.getId());
                Toast.makeText(getContext(), "Clicked: " + event.getName() +
                        ", id: " + event.getId(), Toast.LENGTH_SHORT).show();
            });
        }

        return convertView;
    }
}
