package com.botoni.flow.ui.fragments;

import static com.botoni.flow.ui.helpers.AlertHelper.showSnackBar;
import static com.botoni.flow.ui.helpers.TextWatcherHelper.SearchTextWatcher;
import static com.botoni.flow.ui.helpers.ViewHelper.requireText;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.botoni.flow.R;
import com.botoni.flow.databinding.FragmentSearchBinding;
import com.botoni.flow.ui.adapters.LocationAdapter;
import com.botoni.flow.ui.libs.BottomSheetFragment;
import com.botoni.flow.ui.viewmodel.BuscaViewModel;
import com.botoni.flow.ui.viewmodel.RotaViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class BuscaLocalizacaoFragmento extends BottomSheetFragment {

    private FragmentSearchBinding binding;
    private LocationAdapter adapter;
    private BuscaViewModel buscaViewModel;
    private RotaViewModel rotaViewModel;
    private boolean tecladoVisivel = false;
    private FusedLocationProviderClient fusedClient;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedClient = LocationServices.getFusedLocationProviderClient(requireActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iniciarViewModels();
        configurarFoco();
        iniciarRecycler();
        iniciarWatcher();
        observarBusca();
        observarRota();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @NonNull
    @Override
    protected View getBackgroundView() {
        return binding.getRoot();
    }

    @NonNull
    @Override
    protected View getBottomSheetView() {
        return binding.bottom;
    }

    @Override
    protected void onBind(@NonNull BottomSheetBehavior<View> behavior) {
        configurarBehavior(behavior);
        observarTeclado(behavior);
    }

    @Override
    protected void onStateChanged(@NonNull View bottomSheet, int newState) {
        tratarEstadoTeclado(newState);
    }

    @Override
    protected void onSlide(@NonNull View bottomSheet, float slideOffset) {
    }

    private void iniciarViewModels() {
        buscaViewModel = new ViewModelProvider(requireActivity()).get(BuscaViewModel.class);
        rotaViewModel = new ViewModelProvider(requireActivity()).get(RotaViewModel.class);
    }


    private void configurarFoco() {
        binding.textInputEditText.clearFocus();
        binding.textInputEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                BottomSheetBehavior.from(getBottomSheetView()).setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
            }
        });
    }

    private void iniciarRecycler() {
        adapter = new LocationAdapter(rotaViewModel::selecionar);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(adapter);
    }

    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    private void iniciarWatcher() {
        binding.textInputEditText.addTextChangedListener(SearchTextWatcher(this::onConsultaAlterada));
    }

    private void observarBusca() {
        buscaViewModel.getUiState().observe(getViewLifecycleOwner(), state ->
                adapter.submitList(state != null ? state.localizacoes : null));

        buscaViewModel.getErro().observe(getViewLifecycleOwner(), erro -> {
            if (erro == null) return;
            adapter.submitList(null);
            showSnackBar(requireView(), getString(R.string.error_search_address));
        });
    }

    private void observarRota() {
        rotaViewModel.getVisivel().observe(getViewLifecycleOwner(), visivel -> {
            if (visivel) {
                rotaViewModel.resetarVisivel();
                BottomSheetBehavior.from(getBottomSheetView()).setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

        rotaViewModel.getErro().observe(getViewLifecycleOwner(), erro -> {
            if (erro == null) return;
            showSnackBar(requireView(), getString(R.string.error_search_address));
        });
    }

    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    private void onConsultaAlterada() {
        String consulta = requireText(binding.textInputEditText);
        adapter.submitList(null);
        if (consulta.isEmpty()) buscaViewModel.limpar();
        fusedClient.getLastLocation().addOnSuccessListener(location -> {
            double lat = location != null ? location.getLatitude() : 0;
            double lng = location != null ? location.getLongitude() : 0;
            buscaViewModel.buscar(consulta, lat, lng);
        });
    }

    private void configurarBehavior(@NonNull BottomSheetBehavior<View> behavior) {
        int altura = getResources().getDisplayMetrics().heightPixels;
        behavior.setHideable(true);
        behavior.setFitToContents(false);
        behavior.setPeekHeight((int) (altura * 0.4f));
        behavior.setHalfExpandedRatio(0.8f);
        behavior.setExpandedOffset((int) (altura * 0.2f));
    }

    private void observarTeclado(@NonNull BottomSheetBehavior<View> behavior) {
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            boolean isKeyboardVisible = insets.isVisible(WindowInsetsCompat.Type.ime());
            if (isKeyboardVisible != tecladoVisivel) {
                tecladoVisivel = isKeyboardVisible;
                v.post(() -> {
                    if (tecladoVisivel) {
                        behavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                    } else {
                        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        binding.textInputEditText.clearFocus();
                    }
                });
            }
            return ViewCompat.onApplyWindowInsets(v, insets);
        });
    }

    private void tratarEstadoTeclado(int newState) {
        if (newState == BottomSheetBehavior.STATE_HIDDEN ||
                (newState == BottomSheetBehavior.STATE_DRAGGING && tecladoVisivel)) {
            esconderTeclado();
            binding.textInputEditText.clearFocus();
        }
    }

    private void esconderTeclado() {
        WindowCompat.getInsetsController(requireActivity().getWindow(), binding.getRoot())
                .hide(WindowInsetsCompat.Type.ime());
    }
}