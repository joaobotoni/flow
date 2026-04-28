package com.botoni.flow.ui.fragments;

import static com.botoni.flow.ui.helpers.AlertHelper.showSnackBarErro;
import static com.botoni.flow.ui.helpers.FormatHelper.formatCurrency;
import static com.botoni.flow.ui.helpers.FormatHelper.formatInteger;
import static com.botoni.flow.ui.helpers.TextWatcherHelper.SimpleTextWatcher;
import static com.botoni.flow.ui.helpers.ViewHelper.anyEmpty;
import static com.botoni.flow.ui.helpers.ViewHelper.getBigDecimal;
import static com.botoni.flow.ui.helpers.ViewHelper.isEmpty;
import static com.botoni.flow.ui.helpers.ViewHelper.isNotEmpty;
import static com.botoni.flow.ui.helpers.ViewHelper.orElse;
import static com.botoni.flow.ui.helpers.ViewHelper.setText;
import static com.botoni.flow.ui.helpers.ViewHelper.setVisible;

import android.os.Bundle;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.botoni.flow.R;
import com.botoni.flow.databinding.FragmentNegociacaoBinding;
import com.botoni.flow.ui.adapters.CorretorAdapter;
import com.botoni.flow.ui.adapters.EmpresaAdapter;
import com.botoni.flow.utils.mappers.presentation.BezerroResumoMapper;
import com.botoni.flow.utils.mappers.presentation.FreteResumoMapper;
import com.botoni.flow.ui.state.CorretorUiState;
import com.botoni.flow.ui.state.EmpresaUiState;
import com.botoni.flow.ui.state.PrecificacaoBezerroUiState;
import com.botoni.flow.ui.state.PrecificacaoFreteUiState;
import com.botoni.flow.ui.state.ResumoValoresUiState;
import com.botoni.flow.ui.viewmodel.NegociacaoViewModel;
import com.botoni.flow.ui.viewmodel.PrecificacaoBezerroViewModel;
import com.botoni.flow.ui.viewmodel.PrecificacaoFreteViewModel;
import com.botoni.flow.ui.viewmodel.ResultadoViewModel;
import com.botoni.flow.ui.viewmodel.ResumoValoresViewModel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class NegociacaoFragment extends Fragment {
    private static final String CHAVE_RESUMO_BEZERRO = "resumo_bezerro";
    private static final String CHAVE_RESUMO_FRETE = "resumo_frete";
    private static final String CHAVE_RESUMO_COM_FRETE = "resumo_com_frete";
    private static final String CHAVE_RESULTADO_FINAL = "resultado_final";
    private static final String CHAVE_VIEWMODEL_SIMULACAO = "viewmodel_simulacao_frete";
    private static final String CHAVE_RESUMO_COMISSAO = "resumo_comissao";
    private static final String CHAVE_RESULTADO_FINAL_COM_COMISSAO = "resultado_final_com_comissao";

    @Inject BezerroResumoMapper bezerroResumoMapper;
    @Inject FreteResumoMapper freteResumoMapper;

    private FragmentNegociacaoBinding binding;
    private TextWatcher valorPorCabWatcher;
    private TextWatcher valorPorKgWatcher;
    private BigDecimal pesoUnitario;
    private int quantidade;

    private NegociacaoViewModel negociacaoViewModel;
    private PrecificacaoBezerroViewModel bezerroViewModel;
    private PrecificacaoFreteViewModel freteViewModel;
    private ResumoValoresViewModel resumoBezerroViewModel;
    private ResumoValoresViewModel resumoFreteViewModel;
    private ResumoValoresViewModel resumoComFreteViewModel;
    private ResumoValoresViewModel resumoComissaoViewModel;
    private ResultadoViewModel resultadoFinalViewModel;
    private ResultadoViewModel resultadoComComissaoViewModel;

    private EmpresaAdapter empresaAdapter;
    private CorretorAdapter corretorAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configurarComportamentoBotaoVoltar();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNegociacaoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iniciarSetup();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void iniciarSetup() {
        prepararTelaDeNegociacao();
        configurarRecyclerViewEmpresas();
        configurarAutoCompleteCorretor();
        configurarComponentesIniciais();
        registrarEventos();
        configurarObservadores();
        iniciarCalculoInicialFrete();
    }

    private void prepararTelaDeNegociacao() {
        configurarViewModels();
        recuperarParametrosDeNavegacao();
        exibirParametrosNaTela();
    }

    private void configurarViewModels() {
        negociacaoViewModel = new ViewModelProvider(this).get(NegociacaoViewModel.class);
        ViewModelProvider activityProvider = new ViewModelProvider(requireActivity());
        bezerroViewModel = activityProvider.get(PrecificacaoBezerroViewModel.class);
        freteViewModel = activityProvider.get(CHAVE_VIEWMODEL_SIMULACAO, PrecificacaoFreteViewModel.class);
        resumoBezerroViewModel = activityProvider.get(CHAVE_RESUMO_BEZERRO, ResumoValoresViewModel.class);
        resumoFreteViewModel = activityProvider.get(CHAVE_RESUMO_FRETE, ResumoValoresViewModel.class);
        resumoComFreteViewModel = activityProvider.get(CHAVE_RESUMO_COM_FRETE, ResumoValoresViewModel.class);
        resumoComissaoViewModel = activityProvider.get(CHAVE_RESUMO_COMISSAO, ResumoValoresViewModel.class);
        resultadoFinalViewModel = activityProvider.get(CHAVE_RESULTADO_FINAL, ResultadoViewModel.class);
        resultadoComComissaoViewModel = activityProvider.get(CHAVE_RESULTADO_FINAL_COM_COMISSAO, ResultadoViewModel.class);
    }

    private void recuperarParametrosDeNavegacao() {
        NegociacaoFragmentArgs args = NegociacaoFragmentArgs.fromBundle(requireArguments());
        pesoUnitario = new BigDecimal(args.getPesoMedio());
        quantidade = args.getQuantidadeBezerros();
    }

    private void exibirParametrosNaTela() {
        setText(binding.textoValorPesoMedio, formatCurrency(pesoUnitario));
        setText(binding.textoValorQuantidadeCabecas, formatInteger(quantidade));
    }

    private void configurarComponentesIniciais() {
        iniciarFragmentosEstaticos();
        preencherInputsComValoresDaPrecificacao();
        restaurarOverrideSalvo();
    }

    private void iniciarFragmentosEstaticos() {
        substituirFragmento(R.id.layout_container_valor_bezerro, criarFragmentoResumoBezerroSemFrete());
        substituirFragmento(R.id.layout_container_valor_frete, criarFragmentoResumoFrete());
        substituirFragmento(R.id.layout_container_valor_bezerro_com_frete, criarFragmentoResumoBezerroComFrete());
        substituirFragmento(R.id.layout_container_valor_comissao, criarFragmentoResumoComissao());
        substituirFragmento(R.id.layout_container_valor_total_final, ResultadoFragment.newInstance(CHAVE_RESULTADO_FINAL_COM_COMISSAO));
    }

    private ResumoValoresFragment criarFragmentoResumoBezerroSemFrete() {
        return ResumoValoresFragment.newInstance(
                CHAVE_RESUMO_BEZERRO,
                getString(R.string.titulo_resumo_bezerro),
                getString(R.string.cartao_valor_final_por_cabeca),
                getString(R.string.cartao_valor_final_por_kg));
    }

    private ResumoValoresFragment criarFragmentoResumoFrete() {
        return ResumoValoresFragment.newInstance(
                CHAVE_RESUMO_FRETE,
                getString(R.string.titulo_resumo_frete),
                getString(R.string.cartao_valor_total),
                getString(R.string.cartao_valor_por_kg_frete));
    }

    private ResumoValoresFragment criarFragmentoResumoBezerroComFrete() {
        return ResumoValoresFragment.newInstance(
                CHAVE_RESUMO_COM_FRETE,
                getString(R.string.titulo_resumo_com_frete),
                getString(R.string.cartao_valor_total),
                getString(R.string.cartao_valor_por_kg_frete));
    }

    private ResumoValoresFragment criarFragmentoResumoComissao() {
        return ResumoValoresFragment.newInstance(
                CHAVE_RESUMO_COMISSAO,
                getString(R.string.titulo_resumo_comissao),
                getString(R.string.cartao_comissao_por_cabeca),
                getString(R.string.cartao_total_comissao));
    }

    private void substituirFragmento(int containerId, Fragment fragment) {
        getChildFragmentManager().beginTransaction().replace(containerId, fragment).commit();
    }

    private void preencherInputsComValoresDaPrecificacao() {
        preencherValorFrete();
        preencherValorPorCab();
        preencherValorPorKg();
    }

    private void preencherValorFrete() {
        ResumoValoresUiState frete = resumoFreteViewModel.getState().getValue();
        if (isEmpty(frete) || isEmpty(frete.getValorPrincipal())) return;
        setText(binding.entradaTextoValorFrete, formatCurrency(frete.getValorPrincipal()));
    }

    private void preencherValorPorCab() {
        ResumoValoresUiState bezerro = resumoBezerroViewModel.getState().getValue();
        if (isEmpty(bezerro) || isEmpty(bezerro.getValorPrincipal())) return;
        setText(binding.entradaTextoValorPorCab, formatCurrency(bezerro.getValorPrincipal()));
    }

    private void preencherValorPorKg() {
        ResumoValoresUiState bezerro = resumoBezerroViewModel.getState().getValue();
        if (isEmpty(bezerro) || isEmpty(bezerro.getValorSecundario())) return;
        setText(binding.entradaTextoValorPorKg, formatCurrency(bezerro.getValorSecundario()));
    }

    private void restaurarOverrideSalvo() {
        PrecificacaoBezerroUiState override = negociacaoViewModel.getOverride().getValue();
        if (isEmpty(override)) return;
        preencherInputsComOverride(override);
    }

    private void preencherInputsComOverride(PrecificacaoBezerroUiState override) {
        setText(binding.entradaTextoValorPorCab, formatCurrency(override.getValorPorCabeca()));
        setText(binding.entradaTextoValorPorKg, formatCurrency(override.getValorPorKg()));
    }

    private void registrarEventos() {
        configurarTextWatcherInputs();
        configurarEventosDeClique();
    }

    private void configurarRecyclerViewEmpresas() {
        empresaAdapter = new EmpresaAdapter(this::aoSelecionarEmpresaNaLista);
        binding.recyclerEmpresas.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerEmpresas.setAdapter(empresaAdapter);
    }

    private void configurarAutoCompleteCorretor() {
        corretorAdapter = new CorretorAdapter(requireContext(), new ArrayList<>());
        binding.entradaTextoCorretor.setAdapter(corretorAdapter);
        binding.entradaTextoCorretor.setOnItemClickListener((parent, view, position, id) ->
                negociacaoViewModel.selecionarCorretor(corretorAdapter.getItem(position)));
    }

    private void configurarTextWatcherInputs() {
        TextWatcher entradaWatcher = SimpleTextWatcher(this::aoModificarValorFrete);
        valorPorCabWatcher = SimpleTextWatcher(this::aoModificarValorPorCab);
        valorPorKgWatcher = SimpleTextWatcher(this::aoModificarValorPorKg);
        binding.entradaTextoValorFrete.addTextChangedListener(entradaWatcher);
        binding.entradaTextoValorPorCab.addTextChangedListener(valorPorCabWatcher);
        binding.entradaTextoValorPorKg.addTextChangedListener(valorPorKgWatcher);
    }

    private void configurarEventosDeClique() {
        binding.botaoVoltar.setOnClickListener(v -> executarNavegacaoVoltar());
        binding.botaoContinuar.setOnClickListener(v -> executarNavegacaoDetalhes());
        binding.botaoFinalizar.setOnClickListener(v -> aoFinalizarClicado());
    }

    private void configurarComportamentoBotaoVoltar() {
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                executarNavegacaoVoltar();
            }
        });
    }

    private void configurarObservadores() {
        observarEmpresas();
        observarCorretores();
        observarCorretorSelecionado();
        observarComissaoCalculada();
        observarCalculosFrete();
        observarCalculosBezerro();
        observarResumosUi();
        observarResultadoFinal();
        observarErros();
    }

    private void observarEmpresas() {
        negociacaoViewModel.getEmpresas().observe(getViewLifecycleOwner(), this::atualizarSecaoEmpresas);
    }

    private void observarCorretores() {
        negociacaoViewModel.getCorretores().observe(getViewLifecycleOwner(), this::atualizarListaCorretores);
    }

    private void observarCalculosFrete() {
        freteViewModel.getState().observe(getViewLifecycleOwner(), this::atualizarEstadoResumoFrete);
        freteViewModel.getIncidencia().observe(getViewLifecycleOwner(), this::processarIncidenciaFrete);
    }

    private void observarCalculosBezerro() {
        bezerroViewModel.getState().observe(getViewLifecycleOwner(), this::processarEstadoBezerro);
        bezerroViewModel.getStateComFrete().observe(getViewLifecycleOwner(), this::atualizarEstadoResumoBezerro);
    }

    private void observarResumosUi() {
        resumoFreteViewModel.getState().observe(getViewLifecycleOwner(), this::atualizarVisibilidadeContainerFrete);
        resumoBezerroViewModel.getState().observe(getViewLifecycleOwner(), this::processarAtualizacaoResumoBezerroSemFrete);
        resumoComFreteViewModel.getState().observe(getViewLifecycleOwner(), this::processarAtualizacaoResumoComFrete);
    }

    private void observarResultadoFinal() {
        resultadoFinalViewModel.getState().observe(getViewLifecycleOwner(), this::atualizarVariacao);
    }

    private void observarErros() {
        negociacaoViewModel.getError().observe(getViewLifecycleOwner(), this::exibirErroCarregamento);
    }

    private void exibirErroCarregamento(Throwable erro) {
        showSnackBarErro(requireView(), getString(R.string.erro_generico));
    }

    private void iniciarCalculoInicialFrete() {
        if (isFreteDeclarado()) calcularPrecificacaoBase();
    }

    private void aoModificarValorFrete() {
        if (!isResumed()) return;
        processarFluxoCalculosComFrete();
    }

    private void aoModificarValorPorCab() {
        if (!isResumed()) return;
        if (!isValorPorCabPreenchido()) {
            limparCampoValorPorKg();
            calcularPrecificacaoBase();
            return;
        }
        processarOverrideValorPorCab();
    }

    private void aoModificarValorPorKg() {
        if (!isResumed()) return;
        if (!isValorPorKgPreenchido()) {
            limparCampoValorPorCab();
            calcularPrecificacaoBase();
            return;
        }
        processarOverrideValorPorKg();
    }

    private void processarFluxoCalculosComFrete() {
        if (dadosIncompletosParaCalculo()) {
            limparResultadosFrete();
            return;
        }
        calcularPrecificacaoBase();
    }

    private void calcularPrecificacaoBase() {
        bezerroViewModel.calcularNegociacaoComFrete(pesoUnitario, quantidade);
        freteViewModel.calcularIncidencia(lerValorFrete(), calcularPesoTotalLote());
    }

    private void processarEstadoBezerro(PrecificacaoBezerroUiState estado) {
        if (existeOverrideAtivo()) {
            reaplicarOverrideSalvo();
            return;
        }
        atualizarEstadoResumoComFrete(estado);
    }

    private void atualizarEstadoResumoBezerro(PrecificacaoBezerroUiState estado) {
        if (existeOverrideAtivo()) return;
        publicarResumoBezerroSemFrete(estado);
        publicarResultadoFinal(estado);
        sincronizarInputsComEstadoBezerro(estado);
    }

    private void publicarResumoBezerroSemFrete(PrecificacaoBezerroUiState estado) {
        resumoBezerroViewModel.setState(isEmpty(estado) ? null : bezerroResumoMapper.mapper(estado));
    }

    private void publicarResultadoFinal(PrecificacaoBezerroUiState estado) {
        definirResultadoFinal(isEmpty(estado) ? null : estado.getValorTotal());
    }

    private void sincronizarInputsComEstadoBezerro(PrecificacaoBezerroUiState estado) {
        if (isEmpty(estado)) return;
        atualizarCampoValorPorCabSilenciosamente(estado.getValorPorCabeca());
        atualizarCampoValorPorKgSilenciosamente(estado.getValorPorKg());
    }

    private void atualizarEstadoResumoComFrete(PrecificacaoBezerroUiState estado) {
        resumoComFreteViewModel.setState(isEmpty(estado) ? null : bezerroResumoMapper.mapper(estado));
    }

    private void atualizarEstadoResumoFrete(PrecificacaoFreteUiState estado) {
        resumoFreteViewModel.setState(isEmpty(estado) ? null : freteResumoMapper.mapper(estado));
    }

    private void processarIncidenciaFrete(BigDecimal incidencia) {
        if (incidenciaDeveSerIgnorada(incidencia)) {
            redefinirIncidenciaFrete();
            return;
        }
        aplicarIncidenciaFrete(incidencia);
    }

    private void redefinirIncidenciaFrete() {
        negociacaoViewModel.setIncidenciaFrete(BigDecimal.ZERO);
        limparResumoComFreteUiState();
        if (existeOverrideAtivo()) {
            restaurarEditTextsParaOverride();
            return;
        }
        calcularBezerroComDescontoFrete(BigDecimal.ZERO);
    }

    private void aplicarIncidenciaFrete(BigDecimal incidencia) {
        negociacaoViewModel.setIncidenciaFrete(incidencia);
        PrecificacaoBezerroUiState override = negociacaoViewModel.getOverride().getValue();
        if (override != null) {
            atualizarDisplayComFreteOverride(override, incidencia);
            return;
        }
        calcularBezerroComDescontoFrete(incidencia);
    }

    private void calcularBezerroComDescontoFrete(BigDecimal incidencia) {
        bezerroViewModel.calcularNegociacao(pesoUnitario, quantidade, incidencia);
    }

    private void atualizarDisplayComFreteOverride(PrecificacaoBezerroUiState override, BigDecimal incidencia) {
        BigDecimal valorPorKgComFrete = override.getValorPorKg().add(incidencia);
        BigDecimal valorPorCabComFrete = calcularCabecaPorKg(valorPorKgComFrete);
        BigDecimal valorTotalComFrete = calcularTotalPorCab(valorPorCabComFrete);
        atualizarCampoValorPorCabSilenciosamente(valorPorCabComFrete);
        atualizarCampoValorPorKgSilenciosamente(valorPorKgComFrete);
        resumoComFreteViewModel.setState(new ResumoValoresUiState(valorPorCabComFrete, valorPorKgComFrete));
        definirResultadoFinal(valorTotalComFrete);
    }

    private void restaurarEditTextsParaOverride() {
        PrecificacaoBezerroUiState override = negociacaoViewModel.getOverride().getValue();
        if (override == null) return;
        atualizarCampoValorPorCabSilenciosamente(override.getValorPorCabeca());
        atualizarCampoValorPorKgSilenciosamente(override.getValorPorKg());
        resumoBezerroViewModel.setState(new ResumoValoresUiState(override.getValorPorCabeca(), override.getValorPorKg()));
    }

    private void processarOverrideValorPorCab() {
        BigDecimal valorPorCab = lerValorPorCab();
        BigDecimal valorPorKg = calcularKgPorCabeca(valorPorCab);
        BigDecimal valorTotal = calcularTotalPorCab(valorPorCab);
        aplicarOverrideResumos(valorPorCab, valorPorKg, valorTotal);
        atualizarCampoValorPorKgSilenciosamente(valorPorKg);
    }

    private void processarOverrideValorPorKg() {
        BigDecimal valorPorKg = lerValorPorKg();
        BigDecimal valorPorCab = calcularCabecaPorKg(valorPorKg);
        BigDecimal valorTotal = calcularTotalPorCab(valorPorCab);
        aplicarOverrideResumos(valorPorCab, valorPorKg, valorTotal);
        atualizarCampoValorPorCabSilenciosamente(valorPorCab);
    }

    private void aplicarOverrideResumos(BigDecimal valorPorCab, BigDecimal valorPorKg, BigDecimal valorTotal) {
        salvarOverride(valorPorCab, valorPorKg, valorTotal);
        atualizarResumoBezerroComOverride(valorPorCab, valorPorKg);
        atualizarResultadosComFrete(valorPorKg);
    }

    private void salvarOverride(BigDecimal valorPorCab, BigDecimal valorPorKg, BigDecimal valorTotal) {
        negociacaoViewModel.setOverride(new PrecificacaoBezerroUiState(valorPorKg, valorPorCab, valorTotal));
    }

    private void atualizarResumoBezerroComOverride(BigDecimal valorPorCab, BigDecimal valorPorKg) {
        resumoBezerroViewModel.setState(new ResumoValoresUiState(valorPorCab, valorPorKg));
    }

    private void atualizarResultadosComFrete(BigDecimal valorPorKg) {
        BigDecimal incidencia = orElse(negociacaoViewModel.getIncidenciaFrete().getValue(), BigDecimal.ZERO);
        BigDecimal valorPorKgComFrete = valorPorKg.add(incidencia);
        BigDecimal valorPorCabComFrete = calcularCabecaPorKg(valorPorKgComFrete);
        BigDecimal valorTotalComFrete = calcularTotalPorCab(valorPorCabComFrete);
        resumoComFreteViewModel.setState(new ResumoValoresUiState(valorPorCabComFrete, valorPorKgComFrete));
        definirResultadoFinal(valorTotalComFrete);
    }

    private void reaplicarOverrideSalvo() {
        PrecificacaoBezerroUiState override = negociacaoViewModel.getOverride().getValue();
        if (override == null) return;
        aplicarOverrideResumos(override.getValorPorCabeca(), override.getValorPorKg(), override.getValorTotal());
    }

    private void atualizarSecaoEmpresas(List<EmpresaUiState> empresas) {
        atualizarListaEmpresas(empresas);
        atualizarBadgeQuantidadeEmpresas(empresas);
        atualizarTextoEmpresaSelecionada(empresas);
    }

    private void atualizarListaEmpresas(List<EmpresaUiState> empresas) {
        empresaAdapter.submitList(empresas);
        setVisible(empresas != null && !empresas.isEmpty(), binding.recyclerEmpresas);
    }

    private void atualizarBadgeQuantidadeEmpresas(List<EmpresaUiState> empresas) {
        setText(binding.quantidade, String.valueOf(empresas == null ? 0 : empresas.size()));
    }

    private void atualizarTextoEmpresaSelecionada(List<EmpresaUiState> empresas) {
        if (empresas == null || empresas.isEmpty()) {
            setText(binding.textoEmpresa, getString(R.string.rotulo_empresa_nao_cadastrada));
            return;
        }
        EmpresaUiState selecionada = encontrarEmpresaSelecionada(empresas);
        setText(binding.textoEmpresa, obterTextoEmpresa(selecionada));
        setVisible(true, binding.cardEmpresaSelecionada);
    }

    private String obterTextoEmpresa(EmpresaUiState selecionada) {
        if (selecionada == null) return getString(R.string.rotulo_empresa_nao_selecionada);
        return getString(R.string.rotulo_empresa_selecionada, selecionada.getNome());
    }

    private EmpresaUiState encontrarEmpresaSelecionada(List<EmpresaUiState> empresas) {
        if (empresas == null || empresas.isEmpty()) return null;
        return empresas.stream().filter(EmpresaUiState::isSelecionada).findFirst().orElse(null);
    }

    private void atualizarListaCorretores(List<CorretorUiState> corretores) {
        corretorAdapter.clear();
        corretorAdapter.addAll(corretores);
        corretorAdapter.notifyDataSetChanged();
    }

    private void aoSelecionarEmpresaNaLista(EmpresaUiState empresa) {
        negociacaoViewModel.selecionarEmpresa(empresa);
    }

    private void processarAtualizacaoResumoBezerroSemFrete(ResumoValoresUiState resumo) {
        atualizarVisibilidadeContainerSemFrete(resumo);
        atualizarVisibilidadeContainerResultado(resumo);
    }

    private void processarAtualizacaoResumoComFrete(ResumoValoresUiState resumo) {
        if (!isFreteDeclarado()) {
            ocultarContainerComFrete();
            return;
        }
        atualizarVisibilidadeContainerComFrete(resumo);
    }

    private void atualizarVisibilidadeContainerFrete(ResumoValoresUiState resumo) {
        setVisible(isNotEmpty(resumo), binding.layoutContainerValorFrete);
    }

    private void atualizarVisibilidadeContainerComFrete(ResumoValoresUiState resumo) {
        setVisible(isNotEmpty(resumo), binding.layoutContainerValorBezerroComFrete);
    }

    private void atualizarVisibilidadeContainerSemFrete(ResumoValoresUiState resumo) {
        setVisible(isNotEmpty(resumo), binding.layoutContainerValorBezerro);
    }

    private void atualizarVisibilidadeContainerResultado(ResumoValoresUiState resumo) {
        setVisible(isNotEmpty(resumo), binding.layoutContainerValorTotalFinal);
    }

    private void ocultarContainerComFrete() {
        setVisible(false, binding.layoutContainerValorBezerroComFrete);
    }

    private void atualizarVariacao(BigDecimal totalAtual) {
        negociacaoViewModel.registrarTotalOriginal(totalAtual);
        if (dadosInsuficientesParaVariacao(totalAtual)) return;
        exibirVariacaoPercentual(totalAtual);
    }

    private boolean dadosInsuficientesParaVariacao(BigDecimal totalAtual) {
        return isEmpty(negociacaoViewModel.getTotalOriginal().getValue()) || isEmpty(totalAtual);
    }

    private void exibirVariacaoPercentual(BigDecimal totalAtual) {
        BigDecimal original = negociacaoViewModel.getTotalOriginal().getValue();
        BigDecimal variacao = NegociacaoViewModel.calcularVariacaoPercentual(original, totalAtual);
        setText(binding.textoValorVariacao, formatCurrency(variacao));
    }

    private void atualizarCampoValorPorCabSilenciosamente(BigDecimal valor) {
        removerWatcherValorPorCab();
        setText(binding.entradaTextoValorPorCab, formatCurrency(valor));
        adicionarWatcherValorPorCab();
    }

    private void atualizarCampoValorPorKgSilenciosamente(BigDecimal valor) {
        removerWatcherValorPorKg();
        setText(binding.entradaTextoValorPorKg, formatCurrency(valor));
        adicionarWatcherValorPorKg();
    }

    private void limparCampoValorPorCab() {
        negociacaoViewModel.limparOverride();
        removerWatcherValorPorCab();
        setText(binding.entradaTextoValorPorCab, "");
        adicionarWatcherValorPorCab();
    }

    private void limparCampoValorPorKg() {
        negociacaoViewModel.limparOverride();
        removerWatcherValorPorKg();
        setText(binding.entradaTextoValorPorKg, "");
        adicionarWatcherValorPorKg();
    }

    private void removerWatcherValorPorCab() {
        binding.entradaTextoValorPorCab.removeTextChangedListener(valorPorCabWatcher);
    }

    private void adicionarWatcherValorPorCab() {
        binding.entradaTextoValorPorCab.addTextChangedListener(valorPorCabWatcher);
    }

    private void removerWatcherValorPorKg() {
        binding.entradaTextoValorPorKg.removeTextChangedListener(valorPorKgWatcher);
    }

    private void adicionarWatcherValorPorKg() {
        binding.entradaTextoValorPorKg.addTextChangedListener(valorPorKgWatcher);
    }

    private void limparResumoComFreteUiState() {
        resumoComFreteViewModel.setState(null);
    }

    private void limparResultadosFrete() {
        freteViewModel.limpar();
        limparResumoComFreteUiState();
    }

    private void definirResultadoFinal(BigDecimal valorBase) {
        resultadoFinalViewModel.setState(valorBase);
        CorretorUiState corretor = negociacaoViewModel.getCorretor().getValue();
        if (corretor == null || valorBase == null) {
            resultadoComComissaoViewModel.setState(valorBase);
            return;
        }
        dispararCalculoComissao(corretor, valorBase);
    }

    private void dispararCalculoComissao(CorretorUiState corretor, BigDecimal valorBase) {
        negociacaoViewModel.calcularComissaoPorPercentual(corretor, valorBase.doubleValue());
        negociacaoViewModel.calcularComissaoPorValorPorCabeca(corretor, quantidade);
    }

    private void ocultarContainersComissao() {
        setVisible(false, binding.layoutContainerValorComissao);
        resumoComissaoViewModel.setState(null);
    }

    private void observarCorretorSelecionado() {
        negociacaoViewModel.getCorretor().observe(getViewLifecycleOwner(),
                this::processarSelecaoCorretor);
    }

    private void observarComissaoCalculada() {
        negociacaoViewModel.getComissaoCalculada().observe(getViewLifecycleOwner(),
                this::atualizarUiComComissao);
    }

    private void atualizarUiComComissao(BigDecimal totalComissao) {
        if (isEmpty(totalComissao) || totalComissao.compareTo(BigDecimal.ZERO) == 0) return;
        if (isEmpty(negociacaoViewModel.getCorretor().getValue())) return;
        BigDecimal valorBase = resultadoFinalViewModel.getState().getValue();
        if (isEmpty(valorBase)) return;
        BigDecimal comissaoPorCabeca = totalComissao.divide(BigDecimal.valueOf(quantidade), 2, RoundingMode.HALF_UP);
        resumoComissaoViewModel.setState(new ResumoValoresUiState(comissaoPorCabeca, totalComissao));
        resultadoComComissaoViewModel.setState(valorBase.add(totalComissao));
        setVisible(true, binding.layoutContainerValorComissao);
    }

    private void processarSelecaoCorretor(CorretorUiState corretor) {
        if (corretor == null) {
            ocultarContainersComissao();
            resultadoComComissaoViewModel.setState(resultadoFinalViewModel.getState().getValue());
            return;
        }
        BigDecimal totalBase = resultadoFinalViewModel.getState().getValue();
        if (isNotEmpty(totalBase) && totalBase.compareTo(BigDecimal.ZERO) != 0) {
            dispararCalculoComissao(corretor, totalBase);
        }
    }

    private BigDecimal lerValorFrete() {
        return orElse(getBigDecimal(binding.entradaTextoValorFrete), BigDecimal.ZERO);
    }

    private BigDecimal lerValorPorCab() {
        return getBigDecimal(binding.entradaTextoValorPorCab);
    }

    private BigDecimal lerValorPorKg() {
        return getBigDecimal(binding.entradaTextoValorPorKg);
    }

    private int calcularPesoTotalLote() {
        return quantidade * pesoUnitario.intValue();
    }

    private BigDecimal calcularTotalPorCab(BigDecimal valorPorCab) {
        return valorPorCab.multiply(BigDecimal.valueOf(quantidade));
    }

    private BigDecimal calcularCabecaPorKg(BigDecimal valorPorKg) {
        return valorPorKg.multiply(pesoUnitario);
    }

    private BigDecimal calcularKgPorCabeca(BigDecimal valorPorCab) {
        if (isEmpty(pesoUnitario) || pesoUnitario.compareTo(BigDecimal.ZERO) == 0)
            return BigDecimal.ZERO;
        return valorPorCab.divide(pesoUnitario, 2, RoundingMode.HALF_UP);
    }

    private boolean existeOverrideAtivo() {
        return isNotEmpty(negociacaoViewModel.getOverride().getValue());
    }

    private boolean isFreteDeclarado() {
        return isNotEmpty(lerValorFrete());
    }

    private boolean isValorPorCabPreenchido() {
        return isNotEmpty(lerValorPorCab());
    }

    private boolean isValorPorKgPreenchido() {
        return isNotEmpty(lerValorPorKg());
    }

    private boolean dadosIncompletosParaCalculo() {
        return isEmpty(pesoUnitario) || quantidade <= 0;
    }

    private boolean incidenciaDeveSerIgnorada(BigDecimal incidencia) {
        return isEmpty(incidencia) || isEmpty(pesoUnitario) || quantidade <= 0;
    }

    private void executarNavegacaoVoltar() {
        NavHostFragment.findNavController(this).popBackStack();
    }

    private void executarNavegacaoDetalhes() {
        NavHostFragment.findNavController(this).navigate(
                NegociacaoFragmentDirections.actionNegociacaoFragmentToDetalhePrecificacaoFragment(quantidade, pesoUnitario.toPlainString())
        );
    }

    private void aoFinalizarClicado() {
        if (!isDadosFinalizacaoDisponiveis()) return;
        navegarParaSucessoFragment();
    }

    private boolean isDadosFinalizacaoDisponiveis() {
        return !anyEmpty(resumoBezerroViewModel.getState().getValue(),
                resultadoFinalViewModel.getState().getValue());
    }

    private void navegarParaSucessoFragment() {
        NavHostFragment.findNavController(this).navigate(
                NegociacaoFragmentDirections.actionNegociacaoFragmentToSucessoFragment(quantidade, pesoUnitario.toPlainString())
                        .setValorTotalFrete(orElse(lerValorFrete(), BigDecimal.ZERO).toPlainString())
                        .setOrigemDetalhe(false)
        );
    }
}
