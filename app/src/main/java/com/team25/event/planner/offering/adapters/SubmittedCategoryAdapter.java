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
import com.team25.event.planner.core.listeners.OnApproveButtonClickListener;
import com.team25.event.planner.core.listeners.OnEditButtonClickListener;
import com.team25.event.planner.offering.model.OfferingCategory;
import com.team25.event.planner.offering.model.SubmittedOfferingCategory;

import java.util.List;

public class SubmittedCategoryAdapter extends ArrayAdapter<SubmittedOfferingCategory> {
    private List<SubmittedOfferingCategory> categories;
    private OnApproveButtonClickListener approveButtonClickListener;
    public SubmittedCategoryAdapter(Context context, List<SubmittedOfferingCategory> offers){
        super(context, R.layout.submitted_category_item, offers);
        this.categories= offers;
    }
    public void setOnApproveButtonClickListener(OnApproveButtonClickListener listener) {
        this.approveButtonClickListener = listener;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Nullable
    @Override
    public SubmittedOfferingCategory getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        SubmittedOfferingCategory category = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.submitted_category_item,parent,false);
        }
        LinearLayout layout = convertView.findViewById(R.id.submittedCategoryItem);
        TextView offeringName = convertView.findViewById(R.id.offerName);
        TextView categoryName = convertView.findViewById(R.id.catName);
        TextView description = convertView.findViewById(R.id.descriptionCat);
        MaterialButton approveButton = convertView.findViewById((R.id.approveBtn));

        if(category != null){
            offeringName.setText(category.getOfferingName());
            categoryName.setText(category.getCategoryName());
            description.setText(category.getDescription());

            approveButton.setOnClickListener(v->{
                if(approveButtonClickListener != null){
                    approveButtonClickListener.onApproveButtonClick(category.getOfferingId(),category.getCategoryId());
                }
            });
        }

        return convertView;
    }
}
