package com.botoni.flow.ui.fragments;

import static com.botoni.flow.ui.helpers.NumberHelper.formatCurrency;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.botoni.flow.databinding.FragmentCalfResultBinding;
import com.botoni.flow.ui.state.PrecificacaoBezerroUiState;
import com.botoni.flow.ui.state.PrecificacaoFreteUiState;
import com.botoni.flow.ui.viewmodel.FluxoPrecificacaoViewModel;
import com.botoni.flow.ui.viewmodel.PrecificacaoBezerroViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class BezerroResultadoFragment extends Fragment {

    private FragmentCalfResultBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCalfResultBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FluxoPrecificacaoViewModel viewModel = new ViewModelProvider(requireActivity()).get(FluxoPrecificacaoViewModel.class);
        viewModel.getPrecificacaoBezerroUiStateMutableLiveData().observe(getViewLifecycleOwner(), this::bind);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
}

    private void bind(PrecificacaoBezerroUiState state) {
        if (state == null) return;
        binding.textoValorPorCabeca.setText(formatCurrency(state.valorPorCabeca));
        binding.textoValorPorKg.setText(formatCurrency(state.valorPorKg));
        binding.textoValorTotal.setText(formatCurrency(state.valorTotal));
    }
}
