package com.team25.event.planner.product_service.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.team25.event.planner.R;
import com.team25.event.planner.product_service.model.ServiceCard;

import java.util.List;

public class ServiceCardsAdapter extends ArrayAdapter<ServiceCard> {
    private List<ServiceCard> services;
    public ServiceCardsAdapter(Context context, List<ServiceCard> services){
        super(context, R.layout.service_card,services);
        this.services = services;
    }

    @Override
    public int getCount() {
        return services.size();
    }

    @Nullable
    @Override
    public ServiceCard getItem(int position) {
        return services.get(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ServiceCard serviceCard = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.service_card,parent,false);
        }

        LinearLayout productCard = convertView.findViewById(R.id.service_card_item);
        ImageView imageView = convertView.findViewById(R.id.imageViewService);
        TextView productTitle = convertView.findViewById(R.id.nameService);
        TextView productDescription = convertView.findViewById(R.id.descriptionService);
        ImageButton editButton = convertView.findViewById(R.id.editButton);
        ImageButton deleteButton = convertView.findViewById(R.id.deleteButton);

        if(serviceCard != null){
            imageView.setImageResource(R.drawable.profile_icon);
            productTitle.setText(serviceCard.getName());
            productDescription.setText(serviceCard.getDescription());
            productCard.setOnClickListener(v -> {
                Log.i("ShopApp", "Clicked: " + serviceCard.getName() + ", id: " +
                        serviceCard.getId());
                Toast.makeText(getContext(), "Clicked: " + serviceCard.getName()  +
                        ", id: " + serviceCard.getId(), Toast.LENGTH_SHORT).show();
            });
        }

        return convertView;
    }
}
