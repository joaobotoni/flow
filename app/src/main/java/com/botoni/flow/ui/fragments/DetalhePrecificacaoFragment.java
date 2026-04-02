package com.botoni.flow.ui.fragments;

import static com.botoni.flow.ui.helpers.ViewHelper.anyEmpty;
import static com.botoni.flow.ui.helpers.ViewHelper.getBigDecimal;
import static com.botoni.flow.ui.helpers.ViewHelper.isNotEmpty;
import static com.botoni.flow.ui.helpers.ViewHelper.orElse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.botoni.flow.databinding.FragmentDetalhePrecificacaoBinding;
import com.botoni.flow.ui.adapters.CategoriaAdapter;
import com.botoni.flow.ui.adapters.DetalhePrecificacaoAdapter;
import com.botoni.flow.ui.helpers.ViewHelper;
import com.botoni.flow.ui.state.DetalhePrecoBezerroUiState;
import com.botoni.flow.ui.viewmodel.DetalhePrecificacaoViewModel;
import com.botoni.flow.ui.viewmodel.PrecificacaoFreteViewModel;
import com.botoni.flow.ui.viewmodel.ResumoValoresViewModel;
import com.botoni.flow.ui.viewmodel.RotaViewModel;
import com.botoni.flow.ui.viewmodel.TransporteViewModel;

import java.math.BigDecimal;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DetalhePrecificacaoFragment extends Fragment {
    private static final BigDecimal ARROBA = new BigDecimal("310");
    private static final BigDecimal AGIO = new BigDecimal("30");
    private FragmentDetalhePrecificacaoBinding binding;
    private DetalhePrecificacaoAdapter adapter;
    private DetalhePrecificacaoViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registrarCallbackVoltar();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDetalhePrecificacaoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iniciarSetup();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void iniciarSetup() {
        instanciarViewModels();
        iniciarAdapterDetalhes();
    }

    private void instanciarViewModels() {
        ViewModelProvider provider = new ViewModelProvider(this);
        viewModel = provider.get(DetalhePrecificacaoViewModel.class);
    }
    private void iniciarAdapterDetalhes() {
        adapter = new DetalhePrecificacaoAdapter(getOnDetalheActionListener());
        binding.listaPrecoBezerros.setAdapter(adapter);
    }
    private void voltar() {
        NavHostFragment.findNavController(this).popBackStack();
    }

    private DetalhePrecificacaoAdapter.OnDetalheActionListener getOnDetalheActionListener() {
        return new DetalhePrecificacaoAdapter.OnDetalheActionListener() {
            @Override public void onEdit(DetalhePrecoBezerroUiState detalhe) {}
            @Override public void onRemove(int id) {}
        };
    }

    private void registrarCallbackVoltar() {
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                voltar();
            }
        });
    }
}
