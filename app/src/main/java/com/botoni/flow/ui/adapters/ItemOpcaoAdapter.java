package com.botoni.flow.ui.adapters;

import static com.botoni.flow.ui.helpers.ViewHelper.setText;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.botoni.flow.databinding.ItemOpcaoBinding;
import com.botoni.flow.ui.state.ItemOpcaoUiState;

import java.util.Objects;

public class ItemOpcaoAdapter extends ListAdapter<ItemOpcaoUiState, ItemOpcaoAdapter.ViewHolder> {

    public interface OnClickListener {
        void onClick(ItemOpcaoUiState categoria);
    }

    private final OnClickListener listener;

    public ItemOpcaoAdapter(OnClickListener listener) {
        super(new DiffCallback());
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemOpcaoBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position), listener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemOpcaoBinding binding;

        ViewHolder(ItemOpcaoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(ItemOpcaoUiState estado, OnClickListener listener) {
            setText(binding.chipText, estado.getDescricao());
            binding.chipCard.setChecked(estado.isSelecionada());
            binding.chipCard.setOnClickListener(v -> listener.onClick(estado));
        }
    }

    private static class DiffCallback extends DiffUtil.ItemCallback<ItemOpcaoUiState> {
        @Override
        public boolean areItemsTheSame(@NonNull ItemOpcaoUiState oldItem, @NonNull ItemOpcaoUiState newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull ItemOpcaoUiState oldItem, @NonNull ItemOpcaoUiState newItem) {
            return oldItem.getId() == newItem.getId()
                    && Objects.equals(oldItem.getDescricao(), newItem.getDescricao())
                    && oldItem.isSelecionada() == newItem.isSelecionada();
        }
    }
}