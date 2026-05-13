package com.example.projetoambulancia;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetoambulancia.data.UnidadeSaude;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

public class OccupancyAdapter extends RecyclerView.Adapter<OccupancyAdapter.OccupancyViewHolder> {

    private final List<UnidadeSaude> items = new ArrayList<>();

    public void submitList(List<UnidadeSaude> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OccupancyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_occupancy, parent, false);
        return new OccupancyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OccupancyViewHolder holder, int position) {
        UnidadeSaude unidade = items.get(position);
        holder.title.setText(unidade.getNome());
        holder.subtitle.setText(unidade.getBairroNome() + " • " + unidade.getTipo());

        String livres = "Livres: " + unidade.getLeitosLivres();
        String ocupados = "Ocupados: " + unidade.getLeitosOcupados() + "/" + unidade.getLeitosTotais();
        holder.free.setText(livres);
        holder.used.setText(ocupados);

        holder.free.setTextColor(holder.free.getContext().getColor(R.color.success));
        holder.used.setTextColor(holder.used.getContext().getColor(R.color.warning));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class OccupancyViewHolder extends RecyclerView.ViewHolder {
        private final MaterialTextView title;
        private final MaterialTextView subtitle;
        private final MaterialTextView free;
        private final MaterialTextView used;

        OccupancyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.text_occupancy_title);
            subtitle = itemView.findViewById(R.id.text_occupancy_subtitle);
            free = itemView.findViewById(R.id.text_occupancy_free);
            used = itemView.findViewById(R.id.text_occupancy_used);
        }
    }
}

