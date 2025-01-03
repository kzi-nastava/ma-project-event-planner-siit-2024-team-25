package com.team25.event.planner.offering.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.team25.event.planner.R;
import com.team25.event.planner.core.listeners.OnPurchaseClickListener;
import com.team25.event.planner.offering.model.ProductCard;

import java.util.List;

public class ProductPurchaseAdapter extends ArrayAdapter<ProductCard> {
    private List<ProductCard> productCards;
    private OnPurchaseClickListener listenerPurchase;

    public ProductPurchaseAdapter(Context context, List<ProductCard> list){
        super(context, R.layout.product_purchase_card_item, list);
        this.productCards = list;
    }
    public void setPurchaseListener(OnPurchaseClickListener listener){
        this.listenerPurchase = listener;
    }

    @Override
    public int getCount() {
        return this.productCards.size();
    }

    @Nullable
    @Override
    public ProductCard getItem(int position) {
        return productCards.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ProductCard productCard = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.product_purchase_card_item,parent, false);
        }
        Button purchaseButton = convertView.findViewById(R.id.buyProductButton);
        TextView productName = convertView.findViewById(R.id.product_name);
        TextView productOwnerName = convertView.findViewById(R.id.product_owner_name);
        TextView productRating = convertView.findViewById(R.id.product_rating);
        TextView productPrice = convertView.findViewById(R.id.home_offering_price);

        if(productCard != null){
            productName.setText(productCard.getName());
            productOwnerName.setText(productCard.getOwnerName());
            productRating.setText(String.valueOf(productCard.getRating()));
            productPrice.setText(String.valueOf(productCard.getPrice()));
            purchaseButton.setOnClickListener(v -> {
                if(listenerPurchase != null){
                    listenerPurchase.onPurchaseProductClick(productCard.getId(), productCard.getName());
                }
            });
        }

        return convertView;
    }
}
