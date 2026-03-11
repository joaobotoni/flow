package com.botoni.flow.ui.fragments;

import static com.botoni.flow.ui.helpers.AlertHelper.showDialog;
import static com.botoni.flow.ui.helpers.AlertHelper.showSnackBar;
import static com.botoni.flow.ui.helpers.NumberHelper.formatCurrency;
import static com.botoni.flow.ui.helpers.PermissionHelper.hasPermissions;
import static com.botoni.flow.ui.helpers.PermissionHelper.request;
import static com.botoni.flow.ui.helpers.TextWatcherHelper.SimpleTextWatcher;
import static com.botoni.flow.ui.helpers.ViewHelper.getBigDecimal;
import static com.botoni.flow.ui.helpers.ViewHelper.getInt;
import static com.botoni.flow.ui.helpers.ViewHelper.getTexto;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.botoni.flow.R;
import com.botoni.flow.databinding.FragmentDealBinding;
import com.botoni.flow.ui.adapters.CategoryAdapter;
import com.botoni.flow.ui.helpers.PermissionHelper;
import com.botoni.flow.ui.state.DealUiState;
import com.botoni.flow.ui.viewmodel.DealViewModel;
import com.botoni.flow.ui.viewmodel.RouteViewModel;

import java.math.BigDecimal;
import java.util.Arrays;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DealFragment extends Fragment {

    private static final String TAG = "DealFragment";
    private static final String SEARCH_SHEET_KEY = "SearchBottonSheetFragment";
    private static final String[] LOCATION_PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private FragmentDealBinding binding;
    private DealViewModel dealViewModel;
    private RouteViewModel routeViewModel;
    private CategoryAdapter categoryAdapter;
    private ActivityResultLauncher<String[]> permissionLauncher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissionLauncher = PermissionHelper.register(this, (granted, result) -> {
            if (!granted) onPermissionDenied();
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!hasPermissions(requireContext(), LOCATION_PERMISSIONS)) {
            request(requireContext(), permissionLauncher, LOCATION_PERMISSIONS);
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
        setupViewModels();
        setupChildFragments(getChildFragmentManager(), savedInstanceState);
        setupViews();
        setupObservers();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setupViewModels() {
        dealViewModel = new ViewModelProvider(requireActivity()).get(DealViewModel.class);
        routeViewModel = new ViewModelProvider(requireActivity()).get(RouteViewModel.class);
    }

    private void setupViews() {
        setupCategoryAdapter();
        setupInputWatchers();
        binding.botaoAbrirBottomSheet.setOnClickListener(v -> openSearchSheet());
    }

    private void setupChildFragments(FragmentManager fragmentManager, Bundle savedInstanceState) {
        forwardSearchSheetResult(fragmentManager);
        if (savedInstanceState == null) attachRouteFragment(fragmentManager);
    }

    private void setupObservers() {
        observeFreightVisibility();
        observeDealState();
        dealViewModel.getCategories().observe(getViewLifecycleOwner(), categoryAdapter::submitList);
        dealViewModel.getError().observe(getViewLifecycleOwner(), error ->
                showSnackBar(binding.getRoot(), getString(R.string.error_generic)));
    }

    private void setupCategoryAdapter() {
        categoryAdapter = new CategoryAdapter(dealViewModel::selectCategory);
        binding.listaCategorias.setAdapter(categoryAdapter);
    }

    private void setupInputWatchers() {
        if (binding == null) return;
        binding.entradaTextoPesoAnimal.addTextChangedListener(SimpleTextWatcher(this::onInputChanged));
        binding.entradaTextoQuantidadeAnimais.addTextChangedListener(SimpleTextWatcher(this::onInputChanged));
    }

    private void observeFreightVisibility() {
        MediatorLiveData<Boolean> freightVisible = new MediatorLiveData<>();
        freightVisible.addSource(dealViewModel.getState(), ignored -> freightVisible.setValue(shouldShowFreight()));
        freightVisible.addSource(routeViewModel.getState(), ignored -> freightVisible.setValue(shouldShowFreight()));
        freightVisible.observe(getViewLifecycleOwner(), show ->
                binding.layoutContainerFrete.setVisibility(Boolean.TRUE.equals(show) ? View.VISIBLE : View.GONE));
    }

    private void observeDealState() {
        dealViewModel.getState().observe(getViewLifecycleOwner(), state -> {
            if (state == null) return;
            setResultsVisible(state.isCalculated());
            if (state.isCalculated()) bindResults(state);
        });
    }

    private void setResultsVisible(boolean visible) {
        int visibility = visible ? View.VISIBLE : View.GONE;
        binding.layoutContainerResultados.setVisibility(visibility);
        binding.layoutContainerBotoes.setVisibility(visibility);
    }

    private void bindResults(DealUiState state) {
        binding.textoValorPorCabeca.setText(formatCurrency(state.getValorPorCabeca()));
        binding.textoValorPorKg.setText(formatCurrency(state.getValorPorKg()));
        binding.textoValorTotal.setText(formatCurrency(state.getValorTotal()));
    }

    private boolean shouldShowFreight() {
        boolean isCalculated = dealViewModel.getState().getValue() != null
                && dealViewModel.getState().getValue().isCalculated();
        boolean hasRoute = routeViewModel.getState().getValue() != null
                && routeViewModel.getState().getValue().isFreightVisible();
        return isCalculated && hasRoute;
    }

    private void forwardSearchSheetResult(FragmentManager fragmentManager) {
        getParentFragmentManager().setFragmentResultListener(
                SEARCH_SHEET_KEY, getViewLifecycleOwner(),
                (key, result) -> fragmentManager.setFragmentResult(TAG, result));
    }

    private void attachRouteFragment(FragmentManager fragmentManager) {
        fragmentManager.beginTransaction()
                .replace(R.id.layout_container_frete, new RouteFragment())
                .commit();
    }

    private void openSearchSheet() {
        SearchBottonSheetFragment sheet = new SearchBottonSheetFragment();
        sheet.show(getParentFragmentManager(), sheet.getTag());
    }

    private void onInputChanged() {
        String weight = getTexto(binding.entradaTextoPesoAnimal);
        String quantity = getTexto(binding.entradaTextoQuantidadeAnimais);

        if (isFilled(weight, quantity)) {
            BigDecimal weight_ = getBigDecimal(binding.entradaTextoPesoAnimal);
            Integer quantity_ = getInt(binding.entradaTextoQuantidadeAnimais);
            dealViewModel.calculate(weight_, quantity_);
        } else {
            dealViewModel.clear();
        }
    }

    private boolean isFilled(String... fields) {
        if (binding == null) return false;
        return Arrays.stream(fields).noneMatch(String::isEmpty);
    }

    private void onPermissionDenied() {
        showDialog(
                requireContext(),
                getString(R.string.dialog_title_permission_location),
                getString(R.string.dialog_message_permission_location),
                (dialog, which) -> openAppSettings(),
                (dialog, which) -> { if (getActivity() != null) requireActivity().finish(); });
    }

    private void openAppSettings() {
        if (!isAdded()) return;
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", requireContext().getPackageName(), null));
        startActivity(intent);
    }
}