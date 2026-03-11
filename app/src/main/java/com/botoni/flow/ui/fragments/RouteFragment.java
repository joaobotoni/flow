package com.botoni.flow.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.botoni.flow.databinding.FragmentRouteBinding;
import com.botoni.flow.ui.adapters.TransportAdapter;
import com.botoni.flow.ui.viewmodel.RouteViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RouteFragment extends Fragment {
    private static final String TAG = "RouteFragment";
    private static final String KEY_DISTANCE = "distance";
    private static final String KEY_POINTS = "points";
    private static final String STATE_DISTANCE = "state_distance";
    private static final String STATE_POINTS = "state_points";
    private static final String REQUEST_KEY_DEAL_FRAGMENT = "DealFragment";
    private FragmentRouteBinding binding;
    private RouteViewModel viewModel;
    ArrayList<String> points = new ArrayList<>();
    double distance = 0.0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRouteBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(RouteViewModel.class);
        onBindViews();
        onAttachFragments();
        viewModel.getState().observe(getViewLifecycleOwner(), routeState -> {
            bindRouteData(routeState.getPoints(), routeState.getDistance());
        });
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putDouble(STATE_DISTANCE, distance);
        outState.putStringArrayList(STATE_POINTS, points);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void onBindViews() {
        setupRecyclerView();
    }

    private void onAttachFragments() {
        registerRouteResultListener();
    }

    private void setupRecyclerView() {
        if (binding == null || !isAdded()) return;
        TransportAdapter transportAdapter = new TransportAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(transportAdapter);
    }

    private void registerRouteResultListener() {
        getParentFragmentManager().setFragmentResultListener(
                REQUEST_KEY_DEAL_FRAGMENT, getViewLifecycleOwner(), (key, result) -> {
                    points = result.getStringArrayList(KEY_POINTS);
                    distance = result.getDouble(KEY_DISTANCE);
                    if (points != null && !points.isEmpty()) {
                        viewModel.setRoute(points, distance);
                    }
                }
        );
    }

    private void bindRouteData(List<String> points, double distance) {
        if (binding == null || points == null || points.size() < 2) return;

        String[] origin = parseRoutePoint(points.get(0));
        String[] destination = parseRoutePoint(points.get(1));

        binding.textoCidadeOrigem.setText(origin[0]);
        binding.textoEstadoOrigem.setText(origin[1]);
        binding.textoCidadeDestino.setText(destination[0]);
        binding.textoEstadoDestino.setText(destination[1]);
        binding.textoValorDistancia.setText(String.format(Locale.getDefault(), "%.2f", distance));
    }

    private String[] parseRoutePoint(String point) {
        return Arrays.stream(point.split(","))
                .map(String::trim)
                .toArray(String[]::new);
    }
}