package com.botoni.flow.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.botoni.flow.data.repositories.EmpresaRepository;
import com.botoni.flow.ui.helpers.TaskHelper;
import com.botoni.flow.ui.mappers.domain.EmpresaMapper;
import com.botoni.flow.ui.state.EmpresaUiState;
import com.botoni.flow.ui.state.PrecificacaoBezerroUiState;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class NegociacaoViewModel extends ViewModel {
    private final MutableLiveData<PrecificacaoBezerroUiState> override = new MutableLiveData<>(null);
    private final MutableLiveData<BigDecimal> totalOriginal = new MutableLiveData<>(BigDecimal.ZERO);
    private final MutableLiveData<List<EmpresaUiState>> empresas = new MutableLiveData<>(Collections.emptyList());
    private final MutableLiveData<Throwable> error = new MutableLiveData<>();
    private final EmpresaRepository empresaRepository;
    private final EmpresaMapper empresaMapper;
    private final TaskHelper taskHelper;

    public LiveData<PrecificacaoBezerroUiState> getOverride() {
        return override;
    }

    @Inject
    public NegociacaoViewModel(EmpresaRepository empresaRepository, EmpresaMapper empresaMapper, TaskHelper taskHelper) {
        this.empresaRepository = empresaRepository;
        this.empresaMapper = empresaMapper;
        this.taskHelper = taskHelper;
        listarEmpresas();
    }

    public void setOverride(PrecificacaoBezerroUiState estado) {
        override.setValue(estado);
    }

    public void limparOverride() {
        override.setValue(null);
    }

    public LiveData<BigDecimal> getTotalOriginal() {
        return totalOriginal;
    }

    public void setTotalOriginal(BigDecimal valor) {
        totalOriginal.setValue(valor);
    }

    public LiveData<List<EmpresaUiState>> getEmpresas() {
        return empresas;
    }

    private void listarEmpresas() {
        taskHelper.execute(
                () -> empresaRepository.getAll().stream()
                        .map(empresaMapper::mapFrom)
                        .collect(Collectors.toList()),
                empresas::postValue,
                error::postValue
        );
    }

    public void selecionarEmpresa(EmpresaUiState selecionada) {
        if (empresas.getValue() == null) return;
        List<EmpresaUiState> atualizados = empresas.getValue().stream()
                .map(empresa -> new EmpresaUiState(
                        empresa.getId(),
                        empresa.getNome(),
                        Objects.equals(empresa.getId(), selecionada.getId())))
                .collect(Collectors.toList());
        empresas.setValue(atualizados);
    }
}
