package com.team25.event.planner.user.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.team25.event.planner.R;
import com.team25.event.planner.databinding.ReportCardBinding;
import com.team25.event.planner.user.model.UserReportResponse;

import java.util.List;

public class ReportListAdapter extends RecyclerView.Adapter<ReportListAdapter.ReportViewHolder> {
    private final List<UserReportResponse> _reports;
    private final ReportListAdapter.OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void suspendUser(UserReportResponse report);
        void markReportAsViewed(UserReportResponse report);
    }

    public ReportListAdapter(List<UserReportResponse> reports, ReportListAdapter.OnItemClickListener onItemClickListener) {
        this._reports = reports;
        this.onItemClickListener = onItemClickListener;
    }

    public static class ReportViewHolder extends RecyclerView.ViewHolder {
        private final ReportCardBinding binding;

        public ReportViewHolder(@NonNull ReportCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(UserReportResponse report, ReportListAdapter.OnItemClickListener listener, int position) {
            binding.setReport(report);
            binding.executePendingBindings();

            binding.suspendUser.setImageResource(R.drawable.ic_delete);

            binding.suspendUser.setOnClickListener(v -> {
                listener.markReportAsViewed(report);
            });

            binding.getRoot().setOnClickListener(v -> listener.suspendUser(report));
        }
    }

    @NonNull
    @Override
    public ReportListAdapter.ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ReportCardBinding binding = ReportCardBinding.inflate(inflater, parent, false);
        return new ReportListAdapter.ReportViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportListAdapter.ReportViewHolder holder, int position) {
        final UserReportResponse report = _reports.get(position);
        holder.bind(report, onItemClickListener, position);
    }

    @Override
    public int getItemCount() {
        return _reports != null ? _reports.size() : 0;
    }

    public void addNotification(List<UserReportResponse> newReports) {
        int oldSize = _reports.size();
        _reports.addAll(newReports);
        notifyItemRangeInserted(oldSize, newReports.size());
    }
}
