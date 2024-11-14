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

import com.team25.event.planner.event.model.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TopEventsListAdapter extends ArrayAdapter<Event>{

    private ArrayList<Event> events;

    public TopEventsListAdapter(Context context, ArrayList<Event> events) {
        super(context, R.layout.home_page_top_event, events);
        this.events = events;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    /*
     * Ova metoda vraca pojedinacan element na osnovu pozicije
     * */
    @Nullable
    @Override
    public Event getItem(int position) {
        return events.get(position);
    }

    /*
     * Ova metoda vraca jedinstveni identifikator, za adaptere koji prikazuju
     * listu ili niz, pozicija je dovoljno dobra. Naravno mozemo iskoristiti i
     * jedinstveni identifikator objekta, ako on postoji.
     * */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
     * Ova metoda popunjava pojedinacan element ListView-a podacima.
     * Ako adapter cuva listu od n elemenata, adapter ce u petlji ici
     * onoliko puta koliko getCount() vrati. Prilikom svake iteracije
     * uzece java objekat sa odredjene poziciuje (model) koji cuva podatke,
     * i layout koji treba da prikaze te podatke (view) npr R.layout.product_card.
     * Kada adapter ima model i view, prosto ce uzeti podatke iz modela,
     * popuniti view podacima i poslati listview da prikaze, i nastavice
     * sledecu iteraciju.
     * */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Event event = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.home_page_top_event,
                    parent, false);
        }
        MaterialCardView eventCard = convertView.findViewById(R.id.top_event_card_item);
        TextView eventName = convertView.findViewById(R.id.top_event_name);
        TextView eventOrganizer = convertView.findViewById(R.id.top_event_organizer);
        TextView eventDate = convertView.findViewById(R.id.top_event_date);
        ImageView eventIcon = convertView.findViewById(R.id.top_offer_picture);

        if(event != null){
            eventName.setText(event.getName());
            eventOrganizer.setText(event.getOrganizer());

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            String formattedDate = dateFormat.format(event.getDate());
            eventDate.setText(formattedDate);
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

