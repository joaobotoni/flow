package com.botoni.flow.ui.fragments;

import static com.botoni.flow.domain.usecases.AvaliacaoPrecoAnimalUseCase.calcularValorTotalBezerro;
import static com.botoni.flow.domain.usecases.AvaliacaoPrecoAnimalUseCase.calcularValorTotalPorKg;
import static com.botoni.flow.domain.usecases.AvaliacaoPrecoAnimalUseCase.calcularValorTotalTodosBezerros;
import static com.botoni.flow.ui.helpers.NumberHelper.formatCurrency;
import static com.botoni.flow.ui.helpers.TextWatcherHelper.SimpleTextWatcher;
import static com.botoni.flow.ui.helpers.ViewHelper.getBigDecimal;
import static com.botoni.flow.ui.helpers.ViewHelper.getInt;
import static com.botoni.flow.ui.helpers.ViewHelper.getTexto;

import android.os.Bundle;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.botoni.flow.R;
import com.botoni.flow.data.repositories.local.CategoriaFreteRepository;
import com.botoni.flow.databinding.FragmentDealBinding;
import com.botoni.flow.ui.helpers.TaskHelper;
import com.google.android.material.chip.Chip;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.snackbar.Snackbar;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DealFragment extends Fragment {
    private static final String KEY_CATEGORIA = "key_categoria";
    private FragmentDealBinding binding;
    private String categoriaSelecionada;
    @Inject
    TaskHelper taskHelper;
    @Inject
    CategoriaFreteRepository categoriaFreteRepository;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            categoriaSelecionada = savedInstanceState.getString(KEY_CATEGORIA);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDealBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configureChips();
        setupTextWatchers();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CATEGORIA, categoriaSelecionada);
    }

    private void setupTextWatchers() {
        if (binding == null) return;
        TextWatcher calculationWatcher = SimpleTextWatcher(this::calculateAndUpdateResults);
        binding.entradaTextoPesoAnimal.addTextChangedListener(calculationWatcher);
        binding.entradaTextoQuantidadeAnimais.addTextChangedListener(calculationWatcher);
    }


    private void configureChips() {
        taskHelper.execute(
                categoriaFreteRepository::getDescricoes,
                categorias -> {
                    if (binding != null && isAdded()) {
                        for (String categoria : categorias) {
                            Chip chip = createChip(categoria);
                            if (chip != null) {
                                binding.grupoChipsCategorias.addView(chip);
                            }
                        }
                    }
                },
                e -> {
                    if (binding != null && isAdded()) {
                        showSnackBar(binding.getRoot(), getString(R.string.error_generic));
                    }
                }
        );
    }

    private Chip createChip(String texto) {
        if (!isAdded()) return null;
        Chip chip = new Chip(requireContext());
        chip.setText(texto);
        chip.setCheckable(true);
        chip.setCheckedIconVisible(true);
        chip.setCheckedIcon(ContextCompat.getDrawable(requireContext(), R.drawable.check_24px));
        chip.setShapeAppearanceModel(ShapeAppearanceModel.builder()
                .setAllCorners(CornerFamily.ROUNDED, 50f)
                .build());
        chip.setOnClickListener(v -> categoriaSelecionada = texto);
        chip.setChecked(Objects.equals(texto, categoriaSelecionada));
        return chip;
    }


    private void calculateAndUpdateResults() {
        if (binding == null) return;

        boolean allFieldsFilled = isFields(
                getTexto(binding.entradaTextoPesoAnimal),
                getTexto(binding.entradaTextoQuantidadeAnimais)
        );

        int visibility = allFieldsFilled ? View.VISIBLE : View.GONE;
        binding.layoutContainerResultados.setVisibility(visibility);

        if (allFieldsFilled) {
            BigDecimal peso = getBigDecimal(binding.entradaTextoPesoAnimal);
            BigDecimal precoArroba = new BigDecimal("310.0");  // vai ser parametrizado
            BigDecimal percentual = new BigDecimal("30.0"); // vai ser parametrizado
            int quantidade = getInt(binding.entradaTextoQuantidadeAnimais);

            BigDecimal valorPorKg = calcularValorTotalPorKg(peso, precoArroba, percentual);
            BigDecimal valorPorCabeca = calcularValorTotalBezerro(peso, precoArroba, percentual);
            BigDecimal valorTotal = calcularValorTotalTodosBezerros(valorPorCabeca, quantidade);

            binding.textoValorPorKg.setText(formatCurrency(valorPorKg));
            binding.textoValorPorCabeca.setText(formatCurrency(valorPorCabeca));
            binding.textoValorTotal.setText(formatCurrency(valorTotal));
        }
    }

    private boolean isFields(String... fields) {
        if (binding == null) return false;
        return Arrays.stream(fields).noneMatch(String::isEmpty);
    }

    private void showSnackBar(View view, String message) {
        if (view != null && message != null) {
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
        }
    }
}
