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
import com.team25.event.planner.core.listeners.OnDeleteButtonClickListener;
import com.team25.event.planner.core.listeners.OnEditButtonClickListener;
import com.team25.event.planner.event.model.BudgetItem;

import java.util.List;

public class BudgetItemAdapter extends ArrayAdapter<BudgetItem> {
    private List<BudgetItem> budgetItems;
    private OnEditButtonClickListener editButtonClickListener;
    private OnDeleteButtonClickListener deleteButtonClickListener;

    public void setOnEditButtonClickListener(OnEditButtonClickListener listener) {
        this.editButtonClickListener = listener;
    }
    public void setOnDeleteButtonClickListener(OnDeleteButtonClickListener listener){
        this.deleteButtonClickListener = listener;
    }
   public BudgetItemAdapter(Context context, List<BudgetItem> items){
       super(context, R.layout.list_item_budget_item, items);
       this.budgetItems = items;
   }

    @Override
    public int getCount() {
        return budgetItems.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        BudgetItem budgetItem = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_budget_item, parent, false);
        }
        TextView offeringCategoryName = convertView.findViewById(R.id.tvOfferingCategory);
        TextView budget = convertView.findViewById(R.id.tvBudget);
        MaterialButton editButton = convertView.findViewById(R.id.editBtn1);
        MaterialButton deleteButton = convertView.findViewById(R.id.deleteBtn1);

        if(budgetItem != null){
            String name;
            if(budgetItem.getOfferingCategory()!=null){
                name = budgetItem.getOfferingCategory().getName();
            } else {
                name = "";
            }
            offeringCategoryName.setText(name);
            budget.setText(String.valueOf(budgetItem.getBudget()));
            editButton.setOnClickListener(v -> {
                if (editButtonClickListener != null) {
                    editButtonClickListener.onEditButtonClick(budgetItem.getId(), name);
                }
            });
            deleteButton.setOnClickListener(v -> {
                if(deleteButtonClickListener != null){
                    deleteButtonClickListener.onDeleteButtonClick(budgetItem.getId(), name);
                }
            });
        }

        return convertView;
    }
}
