package com.botoni.flow.ui.fragments;

import static com.botoni.flow.ui.helpers.TextWatcherHelper.SearchTextWatcher;
import static com.botoni.flow.ui.helpers.ViewHelper.getTexto;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
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
import com.botoni.flow.data.repositories.network.LocationRepository;
import com.botoni.flow.databinding.FragmentRouteBinding;
import com.botoni.flow.ui.adapters.LocationAdapter;
import com.botoni.flow.ui.helpers.TaskHelper;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RouteFragment extends Fragment {
    private FragmentRouteBinding binding;
    @Inject
    TaskHelper taskHelper;
    @Inject
    LocationRepository locationRepository;
    private ActivityResultLauncher<String[]> permissionLauncher;
    private LocationAdapter locationAdapter;
    private BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior;
    private Address destination;
    private final double[] latLng = {34.06266637826144, -118.20323412642546}; // Los Angeles


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerPermissionLauncher();
        try {
            destination = Objects.requireNonNull(new Geocoder(requireContext())
                    .getFromLocation(latLng[0], latLng[1], 1)).get(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!hasPermissions(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)) {
            requestLocationPermissions();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRouteBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configureRecyclerView();
        configureBottomSheet();
        setupTextWatchers();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setupTextWatchers() {
        if (binding == null) return;
        TextWatcher searchWatcher = SearchTextWatcher(this::performAddressSearch);
        binding.entradaPesquisaEndereco.addTextChangedListener(searchWatcher);
    }


    private void configureRecyclerView() {
        if (binding == null || !isAdded()) return;
        locationAdapter = new LocationAdapter(new ArrayList<>(), this::getDistance);
        binding.recyclerViewEnderecos.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerViewEnderecos.setAdapter(locationAdapter);
    }

    private void getDistance(Address origin) {
        taskHelper.execute(() -> {
            String response = locationRepository.fetchRoute(origin, destination);
            return locationRepository.parseDistance(response);
        }, distance -> {
            if (binding == null || !isAdded()) return;
            Snackbar.make(requireView(), String.format(Locale.getDefault(), "%.2f", distance), Snackbar.LENGTH_SHORT).show();
        }, error -> {
            if (binding == null || !isAdded()) return;
            Snackbar.make(requireView(), "Erro ao calcular rota: " + error.getMessage(), Snackbar.LENGTH_SHORT).show();
        });
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
                () -> locationRepository.searchCityAndState(query),
                results -> {
                    if (isAdded() && locationAdapter != null) {
                        locationAdapter.submitList(results);
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


    private void configureBottomSheet() {
        if (binding == null) return;

        final float RATIO_COLLAPSED = 0.4f;
        final float RATIO_EXPANDED_KEYBOARD = 0.8f;

        bottomSheetBehavior = BottomSheetBehavior.from(binding.containerPrincipalBottonSheet);
        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.setFitToContents(false);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
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

    private boolean hasPermissions(String... permissions) {
        if (!isAdded() || permissions == null) return false;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(requireContext(), permission)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
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
