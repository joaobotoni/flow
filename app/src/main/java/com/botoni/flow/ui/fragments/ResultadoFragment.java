package com.botoni.flow.ui.fragments;

import static com.botoni.flow.ui.helpers.FormatHelper.formatCurrency;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.botoni.flow.databinding.FragmentResultadoBinding;
import com.botoni.flow.ui.viewmodel.ResultadoViewModel;

import java.math.BigDecimal;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ResultadoFragment extends Fragment {
    private static final String ARG_CHAVE = "chave";

    private FragmentResultadoBinding binding;

    public static ResultadoFragment newInstance(String chave) {
        ResultadoFragment fragment = new ResultadoFragment();
        fragment.setArguments(criarArgumentos(chave));
        return fragment;
    }

    private static Bundle criarArgumentos(String chave) {
        Bundle args = new Bundle();
        args.putString(ARG_CHAVE, chave);
        return args;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentResultadoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iniciarConfiguracoes();
    }

    private void iniciarConfiguracoes() {

        configurarObservadores();
    }

    private void configurarObservadores() {
        if (possuiArgumentosValidos() && obterArgumento(ARG_CHAVE) != null) {
            observarEstadoDaTela(obterArgumento(ARG_CHAVE));
        }
    }

    private boolean possuiArgumentosValidos() {
        return getArguments() != null;
    }

    private String obterArgumento(String chave) {
        return getArguments().getString(chave);
    }

    private void observarEstadoDaTela(String chave) {
        obterViewModel(chave).getState().observe(getViewLifecycleOwner(), this::atualizarInterface);
    }

    private ResultadoViewModel obterViewModel(String chave) {
        return new ViewModelProvider(requireActivity()).get(chave, ResultadoViewModel.class);
    }

    private void atualizarInterface(BigDecimal state) {
        if (state != null) {
            binding.textoValorTotal.setText(formatCurrency(state));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}