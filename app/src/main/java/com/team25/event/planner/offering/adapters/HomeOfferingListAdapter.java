package com.team25.event.planner.offering.adapters;

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
import com.team25.event.planner.event.fragments.ProductPurchaseListFragment;
import com.team25.event.planner.offering.model.OfferingCard;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeOfferingListAdapter extends ArrayAdapter<OfferingCard> {


    private final String OFFERING_ID = "OFFERING_ID";
    private List<OfferingCard> offeringCards;
    private Long _eventId;

    private NavController _navController;


    public HomeOfferingListAdapter(Context context, List<OfferingCard> offerings, NavController navController, Long eventId) {
        super(context, R.layout.home_page_top_offer, offerings);
        this.offeringCards = offerings;
        this._eventId = eventId;
        this._navController = navController;
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
        //ImageView offerIcon = convertView.findViewById(R.id.home_offering_picture);
        TextView offerRating = convertView.findViewById(R.id.home_offer_rating);
        ImageView starImage = convertView.findViewById(R.id.home_offer_star_image);

        if(offeringCard != null){
            offerName.setText(offeringCard.getName());
            offerOwner.setText(offeringCard.getOwnerName());


            String formattedPrice = new DecimalFormat("#,##0.00 $").format(offeringCard.getPrice());
            offerPrice.setText(formattedPrice);
            String formattedDate = formattedPrice;
            offerPrice.setText(formattedDate);
            //offerIcon.setImageResource(R.drawable.ic_heart);


            NumberFormat ratingFormatter = NumberFormat.getNumberInstance();
            ratingFormatter.setMinimumFractionDigits(1);
            String formattedRating = ratingFormatter.format(offeringCard.getRating());
            offerRating.setText(formattedRating);
            starImage.setImageResource(R.drawable.ic_star);

            boolean[] isClicked = {false};
            /*offerIcon.setOnClickListener(v -> {
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
            });*/


            offerCard.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putLong(OFFERING_ID, offeringCard.getId());
                if(this._eventId != null){
                    bundle.putLong(EventArgumentNames.ID_ARG, this._eventId);
                    bundle.putBoolean("BOOK_SERVICE", true);
                    if(offeringCard.isService()){
                        _navController.navigate(R.id.action_eventPurchaseFragment_to_serviceDetailsFragment, bundle);
                    }else{
                        bundle.putLong(ProductPurchaseListFragment.PRODUCT_ID_ARG, offeringCard.getId());
                        _navController.navigate(R.id.action_eventPurchaseFragment_to_productDetailsFragment2, bundle);                    }
                }else{
                    if(offeringCard.isService()){
                        _navController.navigate(R.id.serviceDetailsFragment, bundle);
                    }else{
                        bundle.putLong(ProductPurchaseListFragment.PRODUCT_ID_ARG, offeringCard.getId());
                        _navController.navigate(R.id.productDetailsFragment, bundle);
                    }
                }

            });
        }

        return convertView;
    }
}
