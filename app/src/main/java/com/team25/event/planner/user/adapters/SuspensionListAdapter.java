package com.team25.event.planner.user.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.team25.event.planner.R;
import com.team25.event.planner.databinding.ReportCardBinding;
import com.team25.event.planner.databinding.SuspensionCardBinding;
import com.team25.event.planner.user.model.SuspensionResponse;
import com.team25.event.planner.user.model.UserReportResponse;

import java.util.List;

public class SuspensionListAdapter  extends RecyclerView.Adapter<SuspensionListAdapter.SuspensionViewHolder> {
    private final List<SuspensionResponse> _reports;
    private final SuspensionListAdapter.OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void suspendUser(UserReportResponse report, int position);
        void markReportAsViewed(UserReportResponse report, int position);
    }

    public SuspensionListAdapter(List<SuspensionResponse> reports, SuspensionListAdapter.OnItemClickListener onItemClickListener) {
        this._reports = reports;
        this.onItemClickListener = onItemClickListener;
    }

    public static class SuspensionViewHolder extends RecyclerView.ViewHolder {
        private final SuspensionCardBinding binding;

        public SuspensionViewHolder(@NonNull SuspensionCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(SuspensionResponse suspension, SuspensionListAdapter.OnItemClickListener listener, int position) {
            binding.setSuspension(suspension);
            binding.executePendingBindings();

            binding.suspendedImage.setImageResource(R.drawable.ic_delete);
        }
    }

    @NonNull
    @Override
    public SuspensionListAdapter.SuspensionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        SuspensionCardBinding binding = SuspensionCardBinding.inflate(inflater, parent, false);
        return new SuspensionListAdapter.SuspensionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SuspensionListAdapter.SuspensionViewHolder holder, int position) {
        final SuspensionResponse report = _reports.get(position);
        holder.bind(report, onItemClickListener, position);
    }

    @Override
    public int getItemCount() {
        return _reports != null ? _reports.size() : 0;
    }

    public void addSuspensions(List<SuspensionResponse> newSuspensions) {
        int oldSize = _reports.size();
        _reports.addAll(newSuspensions);
        notifyItemRangeInserted(oldSize, newSuspensions.size());
    }
}
