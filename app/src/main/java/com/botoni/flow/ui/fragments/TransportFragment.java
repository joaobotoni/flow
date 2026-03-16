package com.botoni.flow.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.botoni.flow.databinding.FragmentTransportBinding;
import com.botoni.flow.ui.adapters.TransportAdapter;
import com.botoni.flow.ui.viewmodel.FreightViewModel;
import com.botoni.flow.ui.viewmodel.RouteViewModel;
import com.botoni.flow.ui.viewmodel.TransportViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TransportFragment extends Fragment {

    private FragmentTransportBinding binding;
    private TransportViewModel viewModel;
    private TransportAdapter transportAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentTransportBinding.inflate(inflater, container, false);
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

}