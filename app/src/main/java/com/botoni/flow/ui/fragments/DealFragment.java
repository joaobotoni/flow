package com.botoni.flow.ui.fragments;

import static com.botoni.flow.ui.helpers.AlertHelper.showDialog;
import static com.botoni.flow.ui.helpers.PermissionHelper.hasPermissions;
import static com.botoni.flow.ui.helpers.PermissionHelper.request;

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

import com.botoni.flow.R;
import com.botoni.flow.databinding.FragmentDealBinding;
import com.botoni.flow.ui.adapters.CategoryAdapter;
import com.botoni.flow.ui.helpers.PermissionHelper;
import com.botoni.flow.ui.viewmodel.CalfResultViewModel;
import com.botoni.flow.ui.viewmodel.RouteViewModel;

import java.util.Arrays;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DealFragment extends Fragment {
    private static final String KEY_DEAL = "DealFragment";
    private static final String KEY_SEARCH_SHEET = "SearchBottomSheetFragment";
    private static final String[] LOCATION_PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private FragmentDealBinding binding;
    private CategoryAdapter categoryAdapter;
    private ActivityResultLauncher<String[]> permissionLauncher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerPermissionLauncher();
    }

    @Override
    public void onStart() {
        super.onStart();
        requestLocationPermissionsIfNeeded();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDealBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private void registerPermissionLauncher() {
        permissionLauncher = PermissionHelper.register(this, (granted, result) -> {
            if (!granted) onLocationPermissionDenied();
        });
    }

    private void requestLocationPermissionsIfNeeded() {
        if (!hasPermissions(requireContext(), LOCATION_PERMISSIONS)) {
            request(requireContext(), permissionLauncher, LOCATION_PERMISSIONS);
        }
    }

    private void onLocationPermissionDenied() {
        showDialog(
                requireContext(),
                getString(R.string.dialog_title_permission_location),
                getString(R.string.dialog_message_permission_location),
                (dialog, which) -> openAppSettings(),
                (dialog, which) -> {
                    if (isAdded()) requireActivity().finish();
                });
    }

    private void openAppSettings() {
        if (!isAdded()) return;
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", requireContext().getPackageName(), null));
        startActivity(intent);
    }
}