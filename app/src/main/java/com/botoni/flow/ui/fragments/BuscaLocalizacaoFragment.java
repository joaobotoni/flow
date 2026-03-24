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
import com.botoni.flow.ui.viewmodel.FluxoPrecificacaoViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class BuscaLocalizacaoFragment extends BottomSheetFragment {

    private FragmentSearchBinding binding;
    private LocationAdapter adapter;
    private FluxoPrecificacaoViewModel viewModel;
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
        FluxoPrecificacaoViewModel viewModel = new ViewModelProvider(requireActivity()).get(FluxoPrecificacaoViewModel.class);
        inicializar();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    private void inicializar() {
        configurarLista();
        configurarCampoBusca();
        configurarObservers();
    }


    private void configurarLista() {
        adapter = new LocationAdapter(viewModel::selecionarEndereco);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(adapter);
    }

    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    private void configurarCampoBusca() {
        binding.textInputEditText.addTextChangedListener(SearchTextWatcher(this::buscar));
    }


    private void configurarObservers() {
        observarResultados();
        observarErroBusca();
        observarRotaSelecionada();
        observarErroRota();
    }

    private void observarResultados() {
        viewModel.getBuscaLocalizacaoUiStateMutableLiveData().observe(getViewLifecycleOwner(), state ->
                adapter.submitList(state != null ? state.localizacoes : null));
    }

    private void observarErroBusca() {
        viewModel.getError().observe(getViewLifecycleOwner(), erro -> {
            if (erro == null) return;
            adapter.submitList(null);
            showSnackBar(requireView(), getString(R.string.error_search_address));
        });
    }

    private void observarRotaSelecionada() {
        viewModel.getRotaUiStateMutableLiveData().observe(getViewLifecycleOwner(), state -> {
            if (state != null) esconderTeclado();
        });
    }

    private void observarErroRota() {
        viewModel.getError().observe(getViewLifecycleOwner(), erro -> {
            if (erro == null) return;
            showSnackBar(requireView(), getString(R.string.error_search_address));
        });
    }


    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    private void buscar() {
        String texto = requireText(binding.textInputEditText);
        adapter.submitList(null);

        if (texto.isEmpty()) {
            return;
        }

        fusedClient.getLastLocation().addOnSuccessListener(location -> {
            double lat = location != null ? location.getLatitude() : 0;
            double lng = location != null ? location.getLongitude() : 0;
            viewModel.buscarEndereco(texto, lat, lng);
        });
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
        configurarAlturas(behavior);
        configurarFoco(behavior);
        configurarTeclado(behavior);
    }

    @Override
    protected void onStateChanged(@NonNull View bottomSheet, int newState) {
        boolean usuarioArrastouParaBaixo = newState == BottomSheetBehavior.STATE_DRAGGING;
        boolean sheetFechou = newState == BottomSheetBehavior.STATE_HIDDEN;
        if (usuarioArrastouParaBaixo || sheetFechou) {
            esconderTeclado();
            binding.textInputEditText.clearFocus();
        }
    }

    @Override
    protected void onSlide(@NonNull View bottomSheet, float slideOffset) {
        binding.getRoot().setAlpha(slideOffset);
        binding.textInputEditText.setAlpha(slideOffset);
    }


    private void configurarAlturas(@NonNull BottomSheetBehavior<View> behavior) {
        int altura = getResources().getDisplayMetrics().heightPixels;
        behavior.setHideable(true);
        behavior.setFitToContents(false);
        behavior.setPeekHeight((int) (altura * 0.4f));
        behavior.setHalfExpandedRatio(0.8f);
        behavior.setExpandedOffset((int) (altura * 0.2f));
    }

    private void configurarFoco(@NonNull BottomSheetBehavior<View> behavior) {
        binding.textInputEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) behavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
        });
    }

    private void configurarTeclado(@NonNull BottomSheetBehavior<View> behavior) {
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            boolean tecladoVisivel = insets.isVisible(WindowInsetsCompat.Type.ime());
            v.post(() -> atualizarEstadoSheet(behavior, tecladoVisivel));
            return ViewCompat.onApplyWindowInsets(v, insets);
        });
    }

    private void atualizarEstadoSheet(@NonNull BottomSheetBehavior<View> behavior, boolean tecladoVisivel) {
        if (binding == null) return;

        if (tecladoVisivel) {
            behavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
            return;
        }

        boolean rotaSelecionada = viewModel.getRotaUiStateMutableLiveData().getValue() != null;
        if (rotaSelecionada) {
            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            binding.textInputEditText.clearFocus();
        }
    }

    private void esconderTeclado() {
        WindowCompat.getInsetsController(requireActivity().getWindow(), binding.getRoot())
                .hide(WindowInsetsCompat.Type.ime());
    }
}