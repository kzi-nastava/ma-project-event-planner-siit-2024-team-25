package com.team25.event.planner.event.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.card.MaterialCardView;
import com.team25.event.planner.R;
import com.team25.event.planner.event.model.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class HomeEventListAdapter extends ArrayAdapter<Event> {

    private ArrayList<Event> events;

    public HomeEventListAdapter(Context context, ArrayList<Event> events) {
        super(context, R.layout.home_page_top_event, events);
        this.events = events;
    }

    @Override
    public int getCount() {
        return events.size();
    }


    @Nullable
    @Override
    public Event getItem(int position) {
        return events.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Event event = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.home_page_top_event,
                    parent, false);
        }
        MaterialCardView eventCard = convertView.findViewById(R.id.product_card_item);
        TextView eventName = convertView.findViewById(R.id.top_event_name);
        TextView eventOrganizer = convertView.findViewById(R.id.top_event_organizer);
        TextView eventDate = convertView.findViewById(R.id.top_event_date);

        if (event != null) {
            eventName.setText(event.getName());
            eventOrganizer.setText(event.getOrganizer());

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            String formattedDate = dateFormat.format(event.getDate());
            eventDate.setText(formattedDate);
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
