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
import com.botoni.flow.ui.adapters.TransporteAdapter;
import com.botoni.flow.ui.viewmodel.FluxoPrecificacaoViewModel;
import com.botoni.flow.ui.viewmodel.TransporteViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TransporteFragment extends Fragment {

    private FragmentTransportBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentTransportBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TransporteAdapter adapter = new TransporteAdapter();
        FluxoPrecificacaoViewModel viewModel = new ViewModelProvider(requireActivity()).get(FluxoPrecificacaoViewModel.class);
        binding.listTransport.setAdapter(adapter);
        viewModel.getListTransporteMutableLiveData().observe(getViewLifecycleOwner(), adapter::submitList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
