package com.botoni.flow.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.botoni.flow.databinding.ItemCorretorBinding;
import com.botoni.flow.ui.state.CorretorUiState;

import java.util.List;
import java.util.Optional;

public class CorretorAdapter extends ArrayAdapter<CorretorUiState> {

    public CorretorAdapter(@NonNull Context context, @NonNull List<CorretorUiState> corretores) {
        super(context, 0, corretores);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return criarView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return criarView(position, convertView, parent);
    }

    private View criarView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ItemCorretorBinding binding = convertView != null ? ItemCorretorBinding.bind(convertView)
                : ItemCorretorBinding.inflate(LayoutInflater.from(getContext()), parent, false);

        Optional.ofNullable(getItem(position))
                .map(CorretorUiState::getNome)
                .ifPresent(binding.textoNomeCorretor::setText);

        return binding.getRoot();
    }
}