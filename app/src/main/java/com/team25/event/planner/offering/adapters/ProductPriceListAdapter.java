package com.team25.event.planner.offering.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;
import com.team25.event.planner.R;
import com.team25.event.planner.core.listeners.OnEditButtonClickListener;
import com.team25.event.planner.offering.model.PriceListItemRequestDTO;
import com.team25.event.planner.offering.model.PriceListItemResponseDTO;

import java.util.List;

public class ProductPriceListAdapter extends ArrayAdapter<PriceListItemResponseDTO> {
    private List<PriceListItemResponseDTO> priceListItemResponseDTOS;

    private OnEditButtonClickListener listenerEditItem;

    public ProductPriceListAdapter(Context context, List<PriceListItemResponseDTO> list){
        super(context, R.layout.price_list_item, list);
        priceListItemResponseDTOS = list;
    }
    public void setListener(OnEditButtonClickListener l){
        listenerEditItem = l;
    }

    @Override
    public int getCount() {
        return this.priceListItemResponseDTOS.size();
    }

    @Nullable
    @Override
    public PriceListItemResponseDTO getItem(int position) {
        return this.priceListItemResponseDTOS.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        PriceListItemResponseDTO dto = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.price_list_item, parent, false);
        }
        TextView name = convertView.findViewById(R.id.offerName);
        TextView price = convertView.findViewById(R.id.offerPrice);
        TextView discount = convertView.findViewById(R.id.offerDiscount);
        TextView finalPrice = convertView.findViewById(R.id.offerFinalPrice);
        MaterialButton btn = convertView.findViewById(R.id.approveBtn);
        if(dto!= null){
            name.setText(dto.getName());
            price.setText(String.valueOf(dto.getPrice()));
            discount.setText(String.valueOf(dto.getDiscount()));
            finalPrice.setText(String.valueOf(dto.getPriceWithDiscount()));
            btn.setOnClickListener(v ->{
                if(listenerEditItem != null){
                    listenerEditItem.onEditButtonClick(dto.getOfferingId(), null);
                }
            });
        }

        return convertView;
    }
}
