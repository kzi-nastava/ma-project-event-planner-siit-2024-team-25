package com.team25.event.planner.event.adapters;

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
import com.team25.event.planner.event.model.PurchaseResponseDTO;

import java.util.List;

public class PurchaseListAdapter extends ArrayAdapter<PurchaseResponseDTO> {
    private List<PurchaseResponseDTO> purchaseList;

    private OnClickReviewListener onClickReviewListener;
    private boolean eventReview;
    public interface OnClickReviewListener {
        void onClick(Long id);
    }

    public PurchaseListAdapter(Context context, List<PurchaseResponseDTO> list, boolean eventReview){
        super(context, R.layout.purchase_list_item);
        purchaseList = list;
        this.eventReview = eventReview;
    }

    public void setListener(OnClickReviewListener l){this.onClickReviewListener = l;}

    @Override
    public int getCount() {
        return this.purchaseList.size();
    }

    @Nullable
    @Override
    public PurchaseResponseDTO getItem(int position) {
        return this.purchaseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        PurchaseResponseDTO dto = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.purchase_list_item,parent,false);
        }

        TextView name = convertView.findViewById(R.id.offeringOrEventName);
        TextView price = convertView.findViewById(R.id.pricePurchaseList);
        MaterialButton button = convertView.findViewById(R.id.postReviewBtn);
        if(dto!=null){
            if(eventReview){
                name.setText(dto.getEvent().getName());
            }else{
                name.setText(dto.getOffering().getName());
            }
            price.setText((int) dto.getPrice().getAmount());
            button.setOnClickListener(v->{
                if(onClickReviewListener!= null){
                    onClickReviewListener.onClick(dto.getId());
                }
            });
        }

        return convertView;
    }
}
