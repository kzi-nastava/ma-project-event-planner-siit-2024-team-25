package com.team25.event.planner.review.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.team25.event.planner.R;
import com.team25.event.planner.review.model.ReviewResponseDTO;

import java.text.SimpleDateFormat;
import java.util.List;

public class ReviewRecyclerAdapter extends RecyclerView.Adapter<ReviewRecyclerAdapter.ReviewViewHolder> {
    private List<ReviewResponseDTO> reviewList;
    private Context context;
    private SimpleDateFormat dateFormat;

    // Constructor
    public ReviewRecyclerAdapter(Context context, List<ReviewResponseDTO> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
        this.dateFormat = new SimpleDateFormat("dd:MM:yyyy");
    }

    // ViewHolder Class
    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView userName, comment, rating, date;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            comment = itemView.findViewById(R.id.comment);
            rating = itemView.findViewById(R.id.rating);
            date = itemView.findViewById(R.id.createdDate);
        }
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout for each item
        View view = LayoutInflater.from(context).inflate(R.layout.review_list_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        // Bind data to the views
        ReviewResponseDTO dto = reviewList.get(position);
        if (dto != null) {
            holder.userName.setText(dto.getUser().getFirstName());
            holder.comment.setText(dto.getComment());
            holder.rating.setText(String.valueOf(dto.getRating()));
            holder.date.setText(dateFormat.format(dto.getCreatedDate()));
        }
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }
}
