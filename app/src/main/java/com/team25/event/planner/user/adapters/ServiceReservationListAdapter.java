package com.team25.event.planner.user.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.card.MaterialCardView;
import com.team25.event.planner.R;
import com.team25.event.planner.user.model.PurchaseServiceCard;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class ServiceReservationListAdapter extends ArrayAdapter<PurchaseServiceCard> {

    private final List<PurchaseServiceCard> serviceCards;

    public ServiceReservationListAdapter(Context context, List<PurchaseServiceCard> serviceCards) {
        super(context, R.layout.item_service_reservation, serviceCards);
        this.serviceCards = serviceCards;
    }

    @Override
    public int getCount() {
        return serviceCards.size();
    }

    @Nullable
    @Override
    public PurchaseServiceCard getItem(int position) {
        return serviceCards.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        PurchaseServiceCard service = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_service_reservation,
                    parent, false);
        }

        MaterialCardView serviceCard = convertView.findViewById(R.id.service_card_item);
        TextView serviceName = convertView.findViewById(R.id.service_name);
        TextView eventName = convertView.findViewById(R.id.event_name);
        TextView serviceDates = convertView.findViewById(R.id.service_dates);
        TextView serviceTimes = convertView.findViewById(R.id.service_times);
        TextView servicePrice = convertView.findViewById(R.id.service_price);
        ImageView servicePicture = convertView.findViewById(R.id.service_picture);

        if (service != null) {
            serviceName.setText(service.getServiceName());
            eventName.setText(service.getEventName());

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
            String dateText;
            if (service.getStartDate().equals(service.getEndDate())) {
                dateText = service.getStartDate().format(dateFormatter);
            } else {
                dateText = service.getStartDate().format(dateFormatter) + " - " +
                        service.getEndDate().format(dateFormatter);
            }
            serviceDates.setText(dateText);

            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            String timeText = service.getStartTime().format(timeFormatter) + " - " +
                    service.getEndTime().format(timeFormatter);
            serviceTimes.setText(timeText);

            String priceText = String.format(Locale.US, "$%.2f", service.getPrice());
            servicePrice.setText(priceText);

            servicePicture.setImageResource(R.drawable.ic_shopping_basket);

            serviceCard.setOnClickListener(v -> {
                // Handle click if needed
            });
        }

        return convertView;
    }
}