package com.team25.event.planner.service.adapters;

import android.annotation.SuppressLint;
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
import com.team25.event.planner.core.listeners.OnDeleteButtonClickListener;
import com.team25.event.planner.core.listeners.OnEditButtonClickListener;
import com.team25.event.planner.service.model.ServiceCard;

import java.util.List;

public class ServiceCardsAdapter extends ArrayAdapter<ServiceCard> {
    private List<ServiceCard> services;
    private OnEditButtonClickListener editButtonClickListener;
    private OnDeleteButtonClickListener deleteButtonClickListener;
    public ServiceCardsAdapter(Context context, List<ServiceCard> services){
        super(context, R.layout.service_card,services);
        this.services = services;
    }
    public void setOnEditButtonClickListener(OnEditButtonClickListener listener) {
        this.editButtonClickListener = listener;
    }
    public void setOnDeleteButtonClickListener(OnDeleteButtonClickListener listener){
        this.deleteButtonClickListener = listener;
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

    @SuppressLint("SetTextI18n")
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
        TextView priceText = convertView.findViewById(R.id.priceService);
        ImageButton editButton = convertView.findViewById(R.id.editButton);
        ImageButton deleteButton = convertView.findViewById(R.id.deleteButton);

        if(serviceCard != null){
            imageView.setImageResource(R.drawable.profile_icon);
            productTitle.setText(serviceCard.getName());
            productDescription.setText(serviceCard.getDescription());
            priceText.setText(serviceCard.getPrice().toString() + "e");
            productCard.setOnClickListener(v -> {
                Log.i("ShopApp", "Clicked: " + serviceCard.getName() + ", id: " +
                        serviceCard.getId());
                Toast.makeText(getContext(), "Clicked: " + serviceCard.getName()  +
                        ", id: " + serviceCard.getId(), Toast.LENGTH_SHORT).show();
            });
            editButton.setOnClickListener(v ->{
                if(editButtonClickListener != null){
                    Log.i("ffff", "Clicked: " + serviceCard.getName() + ", id: " +
                            serviceCard.getId());
                    editButtonClickListener.onEditButtonClick(serviceCard.getId(), null);
                }
            });
            deleteButton.setOnClickListener(v ->{
                if(deleteButtonClickListener != null){
                    deleteButtonClickListener.onDeleteButtonClick(serviceCard.getId(), serviceCard.getName());
                }
            });
        }

        return convertView;
    }
}
