package com.example.projetoambulancia;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

public class TestResultAdapter extends RecyclerView.Adapter<TestResultAdapter.TestResultViewHolder> {

    static class TestResult {
        final String title;
        final String detail;
        final String status;
        final boolean ok;

        TestResult(String title, String detail, String status, boolean ok) {
            this.title = title;
            this.detail = detail;
            this.status = status;
            this.ok = ok;
        }
    }

    private final List<TestResult> items = new ArrayList<>();

    public void submitList(List<TestResult> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TestResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_test_result, parent, false);
        return new TestResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestResultViewHolder holder, int position) {
        TestResult item = items.get(position);
        holder.title.setText(item.title);
        holder.detail.setText(item.detail);
        holder.status.setText(item.status);
        int color = item.ok
                ? holder.status.getContext().getColor(R.color.success)
                : holder.status.getContext().getColor(R.color.error);
        holder.status.setTextColor(color);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class TestResultViewHolder extends RecyclerView.ViewHolder {
        private final MaterialTextView title;
        private final MaterialTextView status;
        private final MaterialTextView detail;

        TestResultViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.text_test_title);
            status = itemView.findViewById(R.id.text_test_status);
            detail = itemView.findViewById(R.id.text_test_detail);
        }
    }
}
