package com.team25.event.planner.offering.adapters;

import android.content.Context;
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
import com.team25.event.planner.offering.model.OfferingCard;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class HomeOfferingListAdapter extends ArrayAdapter<OfferingCard> {


    private List<OfferingCard> offeringCards;

    public HomeOfferingListAdapter(Context context, List<OfferingCard> events) {
        super(context, R.layout.home_page_top_event, events);
        this.offeringCards = events;
    }

    @Override
    public int getCount() {
        return offeringCards.size();
    }

    @Nullable
    @Override
    public OfferingCard getItem(int position) {
        return offeringCards.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        OfferingCard offeringCard = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.home_page_offering_card,
                    parent, false);
        }
        MaterialCardView offerCard = convertView.findViewById(R.id.home_offering_card_item);
        TextView offerName = convertView.findViewById(R.id.home_offering_name);
        TextView offerOwner = convertView.findViewById(R.id.home_offering_owner);
        TextView offerPrice = convertView.findViewById(R.id.home_offering_price);
        ImageView offerIcon = convertView.findViewById(R.id.home_offering_picture);
        TextView offerRating = convertView.findViewById(R.id.home_offer_rating);
        ImageView starImage = convertView.findViewById(R.id.home_offer_star_image);

        if(offeringCard != null){
            offerName.setText(offeringCard.getName());
            offerOwner.setText(offeringCard.getOwnerName());

            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
            String formattedPrice = currencyFormatter.format(offeringCard.getPrice());
            String formattedDate = formattedPrice;
            offerPrice.setText(formattedDate);
            offerIcon.setImageResource(R.drawable.ic_heart);

            NumberFormat ratingFormatter = NumberFormat.getNumberInstance();
            ratingFormatter.setMinimumFractionDigits(1);
            String formattedRating = ratingFormatter.format(offeringCard.getRating());
            offerRating.setText(formattedRating);
            starImage.setImageResource(R.drawable.ic_star);

            boolean[] isClicked = {false};
            offerIcon.setOnClickListener(v -> {
                isClicked[0] = !isClicked[0];
                if(isClicked[0]){
                    offerIcon.setImageResource(R.drawable.ic_heart_red);
                    Toast.makeText(getContext(), "You add " + offeringCard.getName() +
                            " to your favourite list", Toast.LENGTH_SHORT).show();
                }
                else{
                    offerIcon.setImageResource(R.drawable.ic_heart);
                    Toast.makeText(getContext(), "You remove " + offeringCard.getName() +
                            " from your favourite list", Toast.LENGTH_SHORT).show();
                }
            });


            offerCard.setOnClickListener(v -> {
                Toast.makeText(getContext(), "Clicked: " + offeringCard.getName() +
                        ", id: " + offeringCard.getId(), Toast.LENGTH_SHORT).show();
            });
        }

        return convertView;
    }
}
