package com.botoni.flow.ui.viewmodel;

import static com.botoni.flow.utils.BigDecimalUtil.CEM;
import static com.botoni.flow.utils.BigDecimalUtil.ESCALA_MONETARIA;
import static com.botoni.flow.utils.BigDecimalUtil.ESCALA_PERCENTUAL;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.botoni.flow.data.repositories.CorretorRepository;
import com.botoni.flow.data.repositories.EmpresaRepository;
import com.botoni.flow.ui.helpers.TaskHelper;
import com.botoni.flow.utils.mappers.domain.CorretorMapper;
import com.botoni.flow.utils.mappers.domain.EmpresaMapper;
import com.botoni.flow.ui.state.CorretorUiState;
import com.botoni.flow.ui.state.EmpresaUiState;
import com.botoni.flow.ui.state.PrecificacaoBezerroUiState;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class NegociacaoViewModel extends ViewModel {
    private static final byte TIPO_CABECA = 0b01000011;
    private static final byte TIPO_PERCENTUAL = 0b01010000;
    private final MutableLiveData<PrecificacaoBezerroUiState> override = new MutableLiveData<>(null);
    private final MutableLiveData<BigDecimal> totalOriginal = new MutableLiveData<>(BigDecimal.ZERO);
    private final MutableLiveData<BigDecimal> incidenciaFrete = new MutableLiveData<>(BigDecimal.ZERO);
    private final MutableLiveData<List<EmpresaUiState>> empresas = new MutableLiveData<>(Collections.emptyList());
    private final MutableLiveData<EmpresaUiState> empresaSelecionada = new MutableLiveData<>(null);
    private final MutableLiveData<List<CorretorUiState>> corretores = new MutableLiveData<>(Collections.emptyList());
    private final MutableLiveData<CorretorUiState> corretorSelecionado = new MutableLiveData<>(null);
    private final MutableLiveData<BigDecimal> comissaoCalculada = new MutableLiveData<>(BigDecimal.ZERO);
    private final MutableLiveData<Throwable> error = new MutableLiveData<>(null);
    private final CorretorRepository corretorRepository;
    private final CorretorMapper corretorMapper;
    private final EmpresaRepository empresaRepository;
    private final EmpresaMapper empresaMapper;
    private final TaskHelper taskHelper;

    public LiveData<PrecificacaoBezerroUiState> getOverride() {
        return override;
    }

    @Inject
    public NegociacaoViewModel(CorretorRepository corretorRepository, CorretorMapper corretorMapper, EmpresaRepository empresaRepository, EmpresaMapper empresaMapper, TaskHelper taskHelper) {
        this.corretorRepository = corretorRepository;
        this.corretorMapper = corretorMapper;
        this.empresaRepository = empresaRepository;
        this.empresaMapper = empresaMapper;
        this.taskHelper = taskHelper;
        listarEmpresas();
        listarCorretores();
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

    public void registrarTotalOriginal(BigDecimal valor) {
        BigDecimal atual = totalOriginal.getValue();
        if (atual != null && atual.compareTo(BigDecimal.ZERO) != 0) return;
        if (valor == null || valor.compareTo(BigDecimal.ZERO) == 0) return;
        totalOriginal.setValue(valor);
    }

    public LiveData<BigDecimal> getIncidenciaFrete() {
        return incidenciaFrete;
    }

    public void setIncidenciaFrete(BigDecimal valor) {
        incidenciaFrete.setValue(valor);
    }

    public LiveData<Throwable> getError() {
        return error;
    }

    public static BigDecimal calcularVariacaoPercentual(BigDecimal original, BigDecimal atual) {
        return atual.subtract(original).divide(original, ESCALA_PERCENTUAL, RoundingMode.HALF_UP)
                .multiply(CEM).setScale(ESCALA_MONETARIA, RoundingMode.HALF_UP);
    }

    public LiveData<List<EmpresaUiState>> getEmpresas() {
        return empresas;
    }

    public LiveData<EmpresaUiState> getEmpresa() {
        return empresaSelecionada;
    }

    public LiveData<BigDecimal> getComissaoCalculada() {
        return comissaoCalculada;
    }

    public LiveData<List<CorretorUiState>> getCorretores() {
        return corretores;
    }

    public LiveData<CorretorUiState> getCorretor() {
        return corretorSelecionado;
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
        empresaSelecionada.setValue(selecionada);
    }


    private void listarCorretores() {
        taskHelper.execute(() -> corretorRepository.getAll().stream()
                        .map(corretorMapper::mapFrom)
                        .collect(Collectors.toList()),
                corretores::postValue,
                error::postValue
        );
    }

    public void selecionarCorretor(CorretorUiState selecionado) {
        if (corretores.getValue() == null) return;
        List<CorretorUiState> atualizados = corretores.getValue().stream()
                .map(corretor -> new CorretorUiState(
                        corretor.getId(),
                        corretor.getNome(),
                        corretor.getComissao(),
                        corretor.getTipoComissao(),
                        Objects.equals(corretor.getId(), selecionado.getId())))
                .collect(Collectors.toList());
        corretores.setValue(atualizados);
        corretorSelecionado.setValue(selecionado);
    }

    public void calcularComissaoPorPercentual(CorretorUiState corretor, double percentual) {
        executarCalculo(corretor, TIPO_PERCENTUAL,
                () -> corretorRepository.calcularValorPorPercentual(corretor.getComissao(), percentual));
    }

    public void calcularComissaoPorValorPorCabeca(CorretorUiState corretor, int quantidade) {
        executarCalculo(corretor, TIPO_CABECA,
                () -> corretorRepository.calcularValorPorCabeca(corretor.getComissao(), quantidade));
    }

    private void executarCalculo(CorretorUiState corretor, byte tipo, Supplier<BigDecimal> calculo) {
        taskHelper.execute(
                () -> resolverComissao(corretor, tipo, calculo),
                result -> { if (result != null) comissaoCalculada.postValue(result); },
                error::postValue
        );
    }

    private BigDecimal resolverComissao(CorretorUiState corretor, byte tipo, Supplier<BigDecimal> calculo) {
        if (isTipoComissao(corretor, tipo)) throw new RuntimeException("Comissão nula ou vazia para: " + corretor.getNome());
        return calculo.get();
    }

    private boolean isTipoComissao(CorretorUiState corretor, byte tipoEsperado) {
        return corretorRepository.buscarTipoDeComissaoPorId(corretor.getId())
                .map(tipo -> corretorRepository.isMesmoTipoComissao(tipo, tipoEsperado))
                .orElseThrow(() -> new RuntimeException("Comissão nula ou vazia para: " + corretor.getNome()));
    }
}
