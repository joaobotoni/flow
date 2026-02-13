package com.botoni.flow.ui.fragments;

import static com.botoni.flow.domain.usecases.AvaliacaoPrecoAnimalUseCase.calcularValorTotalBezerro;
import static com.botoni.flow.domain.usecases.AvaliacaoPrecoAnimalUseCase.calcularValorTotalPorKg;
import static com.botoni.flow.domain.usecases.AvaliacaoPrecoAnimalUseCase.calcularValorTotalTodosBezerros;
import static com.botoni.flow.ui.helpers.NumberHelper.formatCurrency;
import static com.botoni.flow.ui.helpers.TextWatcherHelper.SearchTextWatcher;
import static com.botoni.flow.ui.helpers.TextWatcherHelper.SimpleTextWatcher;
import static com.botoni.flow.ui.helpers.ViewHelper.getBigDecimal;
import static com.botoni.flow.ui.helpers.ViewHelper.getInt;
import static com.botoni.flow.ui.helpers.ViewHelper.getTexto;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.botoni.flow.R;
import com.botoni.flow.data.repositories.local.CategoriaFreteRepository;
import com.botoni.flow.data.repositories.network.GeoLocationRepository;
import com.botoni.flow.databinding.FragmentMainBinding;
import com.botoni.flow.ui.adapters.LocationAdapter;
import com.botoni.flow.ui.helpers.TaskHelper;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.chip.Chip;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.snackbar.Snackbar;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainFragment extends Fragment {
    private static final String KEY_CATEGORIA = "key_categoria";
    private FragmentMainBinding binding;
    private BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior;
    private LocationAdapter locationAdapter;
    private ActivityResultLauncher<String[]> permissionLauncher;
    private String categoriaSelecionada;
    @Inject
    TaskHelper taskHelper;
    @Inject
    CategoriaFreteRepository categoriaFreteRepository;
    @Inject
    GeoLocationRepository geoLocationRepository;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            categoriaSelecionada = savedInstanceState.getString(KEY_CATEGORIA);
        }
        registerPermissionLauncher();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configureRecyclerView();
        configureBottomSheet();
        configureChips();
        setupTextWatchers();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        bottomSheetBehavior = null;
        locationAdapter = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CATEGORIA, categoriaSelecionada);
    }

    private void registerPermissionLauncher() {
        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
            if (result == null) return;

            boolean fineLocationGranted = Boolean.TRUE.equals(result.get(Manifest.permission.ACCESS_FINE_LOCATION));
            boolean coarseLocationGranted = Boolean.TRUE.equals(result.get(Manifest.permission.ACCESS_COARSE_LOCATION));

            if (fineLocationGranted || coarseLocationGranted) {
                if (bottomSheetBehavior != null) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                }
            } else {
                if (bottomSheetBehavior != null) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
                handleLocationDenied();
            }
        });
    }

    private void setupTextWatchers() {
        if (binding == null) return;

        TextWatcher searchWatcher = SearchTextWatcher(this::performAddressSearch);
        TextWatcher calculationWatcher = SimpleTextWatcher(this::calculateAndUpdateResults);

        binding.entradaPesquisaEndereco.addTextChangedListener(searchWatcher);
        binding.entradaTextoPesoAnimal.addTextChangedListener(calculationWatcher);
        binding.entradaTextoPrecoArroba.addTextChangedListener(calculationWatcher);
        binding.entradaTextoPercentual.addTextChangedListener(calculationWatcher);
        binding.entradaTextoQuantidadeAnimais.addTextChangedListener(calculationWatcher);
    }

    private void configureRecyclerView() {
        if (binding == null || !isAdded()) return;

        locationAdapter = new LocationAdapter(new ArrayList<>(), v -> {
        });
        binding.recyclerViewEnderecos.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerViewEnderecos.setAdapter(locationAdapter);
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


    private void configureBottomSheet() {
        if (binding == null) return;

        final float RATIO_COLLAPSED = 0.4f;
        final float RATIO_EXPANDED_KEYBOARD = 0.8f;

        bottomSheetBehavior = BottomSheetBehavior.from(binding.containerPrincipalBottonSheet);
        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setFitToContents(false);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior.setHalfExpandedRatio(RATIO_COLLAPSED);
        bottomSheetBehavior.setExpandedOffset((int) (getResources().getDisplayMetrics().heightPixels * 0.2f));

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    hideKeyboard();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        binding.cartaoAdicionarLocalizacao.setOnClickListener(v -> {
            if (hasPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                }
            } else {
                requestLocationPermissions();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            boolean isKeyboardVisible = insets.isVisible(WindowInsetsCompat.Type.ime());
            if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
                float targetRatio = isKeyboardVisible ? RATIO_EXPANDED_KEYBOARD : RATIO_COLLAPSED;
                bottomSheetBehavior.setHalfExpandedRatio(targetRatio);
                if (isKeyboardVisible) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                }
            }
            return insets;
        });
    }

    private void hideKeyboard() {
        if (binding != null && getActivity() != null) {
            WindowCompat.getInsetsController(getActivity().getWindow(), binding.getRoot())
                    .hide(WindowInsetsCompat.Type.ime());
        }
    }

    @SuppressLint("MissingPermission")
    private void performAddressSearch() {
        if (binding == null) return;
        String query = getTexto(binding.entradaPesquisaEndereco);

        if (query.isEmpty()) {
            if (locationAdapter != null) {
                locationAdapter.submitList(new ArrayList<>());
            }
            return;
        }

        taskHelper.execute(
                () -> geoLocationRepository.searchCityAndState(query),
                resultados -> {
                    if (isAdded() && locationAdapter != null) {
                        locationAdapter.submitList(resultados);
                    }
                },
                e -> {
                    if (binding != null && isAdded()) {
                        showSnackBar(binding.getRoot(), getString(R.string.error_search_address));
                    }
                    if (isAdded() && locationAdapter != null) {
                        locationAdapter.submitList(new ArrayList<>());
                    }
                }
        );
    }

    private void calculateAndUpdateResults() {
        if (binding == null) return;

        boolean allFieldsFilled = isFields(
                getTexto(binding.entradaTextoPesoAnimal),
                getTexto(binding.entradaTextoPrecoArroba),
                getTexto(binding.entradaTextoPercentual),
                getTexto(binding.entradaTextoQuantidadeAnimais)
        );

        int visibility = allFieldsFilled ? View.VISIBLE : View.GONE;
        binding.layoutContainerResultados.setVisibility(visibility);
        binding.layoutContainerAdicionarLocalizacao.setVisibility(visibility);

        if (allFieldsFilled) {
            BigDecimal peso = getBigDecimal(binding.entradaTextoPesoAnimal);
            BigDecimal precoArroba = getBigDecimal(binding.entradaTextoPrecoArroba);
            BigDecimal percentual = getBigDecimal(binding.entradaTextoPercentual);
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

    private boolean hasPermissions(String... permissions) {
        if (!isAdded() || permissions == null) return false;

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }

    private void requestLocationPermissions() {
        if (permissionLauncher != null) {
            permissionLauncher.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        }
    }

    private void handleLocationDenied() {
        if (!isAdded()) return;

        showDialog(
                requireContext(),
                getString(R.string.dialog_title_permission_location),
                getString(R.string.dialog_message_permission_location),
                (dialog, which) -> navigateToAppSettings(),
                (dialog, which) -> {
                    if (getActivity() != null) {
                        requireActivity().finish();
                    }
                }
        );
    }

    private void navigateToAppSettings() {
        if (!isAdded()) return;
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", requireContext().getPackageName(), null));
        startActivity(intent);
    }

    private void showSnackBar(View view, String message) {
        if (view != null && message != null) {
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
        }
    }

    private void showDialog(Context context, String title, String message,
                            DialogInterface.OnClickListener positiveListener,
                            DialogInterface.OnClickListener negativeListener) {
        if (context == null || !isAdded()) return;

        new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(getString(R.string.action_ok), positiveListener)
                .setNegativeButton(getString(R.string.action_no), negativeListener)
                .show();
    }
}