package com.team25.event.planner.offering.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;
import com.team25.event.planner.R;
import com.team25.event.planner.offering.model.OfferingCard;
import com.team25.event.planner.offering.model.OfferingCategory;

import java.util.List;

public class OfferingCategoryAdapter extends ArrayAdapter<OfferingCategory> {
    private List<OfferingCategory> offeringCategories;

    public OfferingCategoryAdapter(Context context, List<OfferingCategory> offers){
        super(context, R.layout.list_item_offering_category, offers);
        this.offeringCategories= offers;
    }

    @Override
    public int getCount() {
        return offeringCategories.size();
    }

    @Nullable
    @Override
    public OfferingCategory getItem(int position) {
        return offeringCategories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        OfferingCategory offeringCategory = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_offering_category,parent,false);
        }

        LinearLayout layout = convertView.findViewById(R.id.categoryItem);
        TextView name = convertView.findViewById(R.id.column2);
        TextView description = convertView.findViewById(R.id.column3);
        MaterialButton editButton = convertView.findViewById(R.id.editButton);
        MaterialButton deleteButton = convertView.findViewById(R.id.deleteButton);

        if(offeringCategory!=null){
            name.setText(offeringCategory.getName());
            description.setText(offeringCategory.getDescription());
        }

        return convertView;
    }
}
