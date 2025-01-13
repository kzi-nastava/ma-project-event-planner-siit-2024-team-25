package com.team25.event.planner.event.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;

import com.google.android.material.card.MaterialCardView;
import com.team25.event.planner.R;
import com.team25.event.planner.event.fragments.EventArgumentNames;
import com.team25.event.planner.event.model.EventCard;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class HomeEventListAdapter extends ArrayAdapter<EventCard> {

    private List<EventCard> eventCards;
    private NavController navController;

    private final FavoriteToggleListener favoriteToggleListener;

    public interface FavoriteToggleListener {
        void onFavoriteToggle(EventCard event);
    }

    public HomeEventListAdapter(Context context, List<EventCard> eventCards, NavController navController, FavoriteToggleListener favoriteToggleListener) {
        super(context, R.layout.home_page_event_card, eventCards);
        this.eventCards = eventCards;
        this.navController = navController;
        this.favoriteToggleListener = favoriteToggleListener;
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
        TextView eventTime = convertView.findViewById(R.id.home_event_time);
        ImageView eventIcon = convertView.findViewById(R.id.home_event_picture);
        ImageView eventLocationImage = convertView.findViewById(R.id.home_event_location_image);
        TextView eventLocation = convertView.findViewById(R.id.home_event_location);

        eventIcon.setImageResource(R.drawable.ic_heart);

        if (event != null) {
            eventName.setText(event.getName());
            eventOrganizer.setText(event.getOrganizerName());


            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            String formattedDate = event.getStartDateTime().format(dateFormatter);
            String formattedTime = event.getStartDateTime().format(timeFormatter);

            eventDate.setText(formattedDate);
            eventTime.setText(formattedTime);

            eventLocation.setText(event.getCountry() + ", " + event.getCity());
            eventLocationImage.setImageResource(R.drawable.ic_location);

            if (event.getIsFavorite()) {
                eventIcon.setImageResource(R.drawable.ic_heart_red);
            } else {
                eventIcon.setImageResource(R.drawable.ic_heart);
            }

            eventIcon.setOnClickListener(v -> {
                if (!event.getIsFavorite()) {
                    Toast.makeText(getContext(), "You add " + event.getName() +
                            " to your favourite list", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "You remove " + event.getName() +
                            " from your favourite list", Toast.LENGTH_SHORT).show();
                }

                favoriteToggleListener.onFavoriteToggle(event);
            });

            eventCard.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putLong(EventArgumentNames.ID_ARG, event.getId());
                navController.navigate(R.id.action_homeFragment_to_eventDetailsFragment, bundle);
            });
        }


        return convertView;
    }
}
