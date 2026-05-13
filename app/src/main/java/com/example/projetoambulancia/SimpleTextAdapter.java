package com.example.projetoambulancia;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

public class SimpleTextAdapter extends RecyclerView.Adapter<SimpleTextAdapter.TextViewHolder> {

    private final List<String> items = new ArrayList<>();

    public void submitList(List<String> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_text_card, parent, false);
        return new TextViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TextViewHolder holder, int position) {
        holder.text.setText(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class TextViewHolder extends RecyclerView.ViewHolder {
        private final MaterialTextView text;

        TextViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text_primary);
        }
    }
}

