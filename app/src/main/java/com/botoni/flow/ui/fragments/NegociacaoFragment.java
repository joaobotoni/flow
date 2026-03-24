package com.botoni.flow.ui.fragments;

import static com.botoni.flow.ui.helpers.AlertHelper.showDialog;
import static com.botoni.flow.ui.helpers.PermissionHelper.hasPermissions;
import static com.botoni.flow.ui.helpers.PermissionHelper.register;
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
import com.botoni.flow.ui.viewmodel.CategoriaViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class NegociacaoFragment extends Fragment {

    private static final String[] PERMISSOES_LOCALIZACAO = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private FragmentDealBinding binding;
    private ActivityResultLauncher<String[]> permissaoLauncher;

    private CategoriaViewModel categoriaVm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registrarPermissao();
    }

    @Override
    public void onStart() {
        super.onStart();
        solicitarPermissaoSeNecessario();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDealBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iniciarFragmentosFilhos();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private void iniciarFragmentosFilhos() {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.layout_container_valores, new BezerroResultadoFragment())
                .replace(R.id.layout_container_rota, new RotaFragment())
                .replace(R.id.layout_container_transporte, new TransporteFragment())
                .replace(R.id.layout_container_frete, new FreteFragment())
                .commit();
    }


    private void abrirBusca() {
        BuscaLocalizacaoFragment sheet = new BuscaLocalizacaoFragment();
        sheet.show(getParentFragmentManager(), sheet.getTag());
    }

    private void registrarPermissao() {
        permissaoLauncher = register(this, (granted, result) -> {
            if (!granted) onPermissaoNegada();
        });
    }

    private void solicitarPermissaoSeNecessario() {
        if (!hasPermissions(requireContext(), PERMISSOES_LOCALIZACAO)) {
            request(requireContext(), permissaoLauncher, PERMISSOES_LOCALIZACAO);
        }
    }

    private void onPermissaoNegada() {
        showDialog(
                requireContext(),
                getString(R.string.dialog_title_permission_location),
                getString(R.string.dialog_message_permission_location),
                (dialog, which) -> abrirConfiguracoes(),
                (dialog, which) -> {
                    if (isAdded()) requireActivity().finish();
                });
    }

    private void abrirConfiguracoes() {
        if (!isAdded()) return;
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", requireContext().getPackageName(), null));
        startActivity(intent);
    }
}
