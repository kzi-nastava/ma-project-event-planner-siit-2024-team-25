package com.team25.event.planner.product_service.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.team25.event.planner.R;
import com.team25.event.planner.product_service.model.Service;

import java.util.ArrayList;

public class ServiceListAdapter extends ArrayAdapter<Service> {
    private ArrayList<Service> aServices;

    public ServiceListAdapter(Context context, ArrayList<Service> services){
        super(context, R.layout.service_card, services);
        aServices = services;
    }

    ///Returns number of elements list
    @Override
    public int getCount() {
        return aServices.size();
    }

    ///Return elem from position
    @Nullable
    @Override
    public Service getItem(int position) {
        return aServices.get(position);
    }

    ///Position is good enough id when we talk about adapters
    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Service service = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.service_card,parent,false);
        }

        LinearLayout productCard = convertView.findViewById(R.id.service_card_item);
        ImageView imageView = convertView.findViewById(R.id.imageViewService);
        TextView productTitle = convertView.findViewById(R.id.nameService);
        TextView productDescription = convertView.findViewById(R.id.descriptionService);

        if(service != null){
//            imageView.setImageResource(service.getImageURL());
            productTitle.setText(service.getName());
            productDescription.setText(service.getDescription());
            productCard.setOnClickListener(v -> {
                Log.i("ShopApp", "Clicked: " + service.getName() + ", id: " +
                        service.getId());
                Toast.makeText(getContext(), "Clicked: " + service.getName()  +
                        ", id: " + service.getId(), Toast.LENGTH_SHORT).show();
            });
        }

        return  convertView;
    }
}
