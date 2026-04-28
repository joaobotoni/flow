package com.botoni.flow.ui.fragments;

import static com.botoni.flow.ui.helpers.AlertHelper.showDialog;
import static com.botoni.flow.ui.helpers.PermissionHelper.hasPermissions;
import static com.botoni.flow.ui.helpers.PermissionHelper.register;
import static com.botoni.flow.ui.helpers.PermissionHelper.request;
import static com.botoni.flow.ui.helpers.TextWatcherHelper.SimpleTextWatcher;
import static com.botoni.flow.ui.helpers.ViewHelper.clearText;
import static com.botoni.flow.ui.helpers.ViewHelper.getDouble;
import static com.botoni.flow.ui.helpers.ViewHelper.isEmpty;
import static com.botoni.flow.ui.helpers.ViewHelper.isNotEmpty;
import static com.botoni.flow.ui.helpers.ViewHelper.orElse;
import static com.botoni.flow.ui.helpers.ViewHelper.setText;
import static com.botoni.flow.ui.helpers.ViewHelper.setVisible;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.botoni.flow.R;
import com.botoni.flow.databinding.FragmentPrecificacaoFreteBinding;
import com.botoni.flow.utils.mappers.domain.TransporteMapper;
import com.botoni.flow.utils.mappers.presentation.FreteResumoMapper;
import com.botoni.flow.ui.state.PrecificacaoFreteUiState;
import com.botoni.flow.ui.state.ResumoValoresUiState;
import com.botoni.flow.ui.state.RotaUiState;
import com.botoni.flow.ui.viewmodel.PrecificacaoFreteViewModel;
import com.botoni.flow.ui.viewmodel.ResumoValoresViewModel;
import com.botoni.flow.ui.viewmodel.RotaViewModel;
import com.botoni.flow.ui.viewmodel.TransporteViewModel;

import java.math.BigDecimal;
import java.util.Collections;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PrecificacaoFreteFragment extends Fragment {

    private static final String CHAVE_RESUMO_SIMULACAO_FRETE = "resumo_simulacao_frete";
    private static final String CHAVE_VIEWMODEL_SIMULACAO = "viewmodel_simulacao_frete";
    static final String CHAVE_RESULTADO_FRETE = "resultado_selecao_frete";
    static final String EXTRA_VALOR_FRETE = "valor_frete";

    private static final String[] PERMISSOES_LOCALIZACAO = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    @Inject
    FreteResumoMapper freteResumoMapper;
    @Inject
    TransporteMapper transporteMapper;

    private PrecificacaoFreteViewModel simulacaoFreteViewModel;
    private RotaViewModel rotaViewModel;
    private TransporteViewModel transporteViewModel;
    private ResumoValoresViewModel resumoSimulacaoFreteViewModel;

    private FragmentPrecificacaoFreteBinding binding;
    private TextWatcher distanciaWatcher;
    private ActivityResultLauncher<String[]> permissaoLauncher;
    private int cargaTotalDoLote;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configurarCicloDeVidaInicial();
    }

    @Override
    public void onStart() {
        super.onStart();
        verificarPermissoesDeLocalizacao();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPrecificacaoFreteBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iniciarSetup();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        recuperarEstadoDaInterface();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void configurarCicloDeVidaInicial() {
        prepararLauncherDePermissao();
        configurarComportamentoBotaoVoltar();
    }

    private void iniciarSetup() {
        extrairArgumentosDeNavegacao();
        instanciarViewModels();
        iniciarFragmentosEstaticos();
        registrarEventosDeInterface();
        configurarObservadores();
    }

    private void extrairArgumentosDeNavegacao() {
        cargaTotalDoLote = PrecificacaoFreteFragmentArgs.fromBundle(requireArguments()).getCargaTotal();
    }

    private void instanciarViewModels() {
        ViewModelProvider provider = new ViewModelProvider(requireActivity());
        rotaViewModel = provider.get(RotaViewModel.class);
        transporteViewModel = provider.get(TransporteViewModel.class);
        simulacaoFreteViewModel = provider.get(CHAVE_VIEWMODEL_SIMULACAO, PrecificacaoFreteViewModel.class);
        resumoSimulacaoFreteViewModel = provider.get(CHAVE_RESUMO_SIMULACAO_FRETE, ResumoValoresViewModel.class);
    }

    private void iniciarFragmentosEstaticos() {
        substituirFragmento(R.id.layout_container_rota, new RotaFragment());
        substituirFragmento(R.id.layout_container_transporte, new TransporteFragment());
        substituirFragmento(R.id.layout_container_frete, criarFragmentoResumoFrete());
    }

    private void substituirFragmento(int containerId, Fragment fragment) {
        getChildFragmentManager().beginTransaction().replace(containerId, fragment).commit();
    }

    private ResumoValoresFragment criarFragmentoResumoFrete() {
        return ResumoValoresFragment.newInstance(
                CHAVE_RESUMO_SIMULACAO_FRETE,
                getString(R.string.titulo_resumo_frete),
                getString(R.string.cartao_valor_total),
                getString(R.string.cartao_valor_por_kg_frete));
    }

    private void registrarEventosDeInterface() {
        configurarTextWatcherDistancia();
        configurarEventosDeClique();
    }

    private void configurarTextWatcherDistancia() {
        distanciaWatcher = SimpleTextWatcher(this::processarAlteracaoDistanciaManual);
        adicionarWatcherDistancia();
    }

    private void configurarEventosDeClique() {
        binding.cartaoExplorarRota.setOnClickListener(v -> abrirTelaBuscaDeLocalizacao());
        binding.botaoVoltar.setOnClickListener(v -> executarNavegacaoVoltar());
        binding.botaoContinuar.setOnClickListener(v -> processarConfirmacaoDeFrete());
    }

    private void configurarObservadores() {
        observarEstadoRota();
        observarEstadoTransporte();
        observarEstadoSimulacaoFrete();
    }

    private void observarEstadoRota() {
        rotaViewModel.getState().observe(getViewLifecycleOwner(), this::processarNovaRota);
    }

    private void observarEstadoTransporte() {
        transporteViewModel.getState().observe(getViewLifecycleOwner(), this::processarAtualizacaoTransporte);
    }

    private void observarEstadoSimulacaoFrete() {
        simulacaoFreteViewModel.getState().observe(getViewLifecycleOwner(), this::atualizarEstadoResumoFrete);
        resumoSimulacaoFreteViewModel.getState().observe(getViewLifecycleOwner(), this::atualizarVisibilidadeResumoFrete);
    }

    private void processarNovaRota(RotaUiState rota) {
        if (existeConflitoEntreRotaEDistancia(rota)) {
            rotaViewModel.limpar();
            return;
        }
        if (isNotEmpty(rota)) {
            limparCampoDistanciaSilenciosamente();
        }
        sincronizarInterfaceEFluxoDeCalculo();
    }

    private void processarAtualizacaoTransporte(Object transportes) {
        sincronizarInterfaceEFluxoDeCalculo();
    }

    private void processarAlteracaoDistanciaManual() {
        double distancia = lerDistanciaManual();
        salvarDistanciaNoViewModel(distancia);
        if (isNotEmpty(distancia)) {
            rotaViewModel.limpar();
        }
        sincronizarInterfaceEFluxoDeCalculo();
    }

    private void sincronizarInterfaceEFluxoDeCalculo() {
        atualizarVisibilidadeDosPaineis();
        iniciarFluxoDeCalculoDoFrete();
    }

    private void iniciarFluxoDeCalculoDoFrete() {
        double distanciaAtiva = determinarDistanciaParaCalculo();
        if (isDistanciaInvalida(distanciaAtiva)) {
            limparDadosDaSimulacao();
            return;
        }
        executarCalculoDoFreteNoViewModel(distanciaAtiva);
    }

    private void executarCalculoDoFreteNoViewModel(double distanciaAtiva) {
        simulacaoFreteViewModel.calcularFrete(
                transporteMapper.mapTo(orElse(transporteViewModel.getState().getValue(), Collections.emptyList())),
                distanciaAtiva,
                cargaTotalDoLote);
    }

    private void limparDadosDaSimulacao() {
        resumoSimulacaoFreteViewModel.setState(null);
        simulacaoFreteViewModel.limpar();
    }

    private void atualizarEstadoResumoFrete(PrecificacaoFreteUiState estadoFrete) {
        resumoSimulacaoFreteViewModel.setState(isEmpty(estadoFrete) ? null : freteResumoMapper.mapper(estadoFrete));
    }

    private void atualizarVisibilidadeResumoFrete(ResumoValoresUiState resumo) {
        setVisible(isNotEmpty(resumo), binding.layoutContainerFrete);
    }

    private void processarConfirmacaoDeFrete() {
        PrecificacaoFreteUiState estadoAtual = simulacaoFreteViewModel.getState().getValue();
        if (isEstadoFreteInvalidoParaConfirmacao(estadoAtual)) return;
        enviarResultadoParaTelaAnterior(orElse(estadoAtual.getValorTotal(), BigDecimal.ZERO));
        executarNavegacaoVoltar();
    }

    private void enviarResultadoParaTelaAnterior(BigDecimal valorTotal) {
        Bundle resultado = criarBundleDeResultado(valorTotal);
        getParentFragmentManager().setFragmentResult(CHAVE_RESULTADO_FRETE, resultado);
    }

    private Bundle criarBundleDeResultado(BigDecimal valorTotal) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_VALOR_FRETE, valorTotal.toPlainString());
        return bundle;
    }

    private void recuperarEstadoDaInterface() {
        aplicarDistanciaSalvaNaInterface();
        atualizarVisibilidadeDosPaineis();
    }

    private void aplicarDistanciaSalvaNaInterface() {
        if (isDistanciaManualPreenchida()) return;
        double distanciaSalva = lerDistanciaSalvaNoViewModel();
        if (distanciaSalva > 0) {
            atribuirTextoDistancia(String.valueOf(distanciaSalva));
        }
    }

    private void atualizarVisibilidadeDosPaineis() {
        boolean temRota = isRotaDefinida();
        boolean temDistanciaManual = isDistanciaManualPreenchida();
        setVisible(temRota && !temDistanciaManual, binding.layoutContainerRota);
        setVisible(temRota || temDistanciaManual, binding.layoutContainerTransporte);
    }

    private void limparCampoDistanciaSilenciosamente() {
        if (isDistanciaManualVazia()) return;
        removerWatcherDistancia();
        clearText(binding.entradaTextoDistancia);
        salvarDistanciaNoViewModel(0);
        adicionarWatcherDistancia();
    }

    private void prepararLauncherDePermissao() {
        permissaoLauncher = register(this, (concedida, result) -> {
            if (!concedida) tratarRecusaDePermissao();
        });
    }

    private void configurarComportamentoBotaoVoltar() {
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                executarNavegacaoVoltar();
            }
        });
    }

    private void verificarPermissoesDeLocalizacao() {
        if (!hasPermissions(requireContext(), PERMISSOES_LOCALIZACAO)) {
            solicitarPermissoesAoUsuario();
        }
    }

    private void solicitarPermissoesAoUsuario() {
        request(requireContext(), permissaoLauncher, PERMISSOES_LOCALIZACAO);
    }

    private void tratarRecusaDePermissao() {
        showDialog(
                requireContext(),
                getString(R.string.dialogo_titulo_permissao_localizacao),
                getString(R.string.dialogo_mensagem_permissao_localizacao),
                (d, w) -> navegarParaConfiguracoesDoApp(),
                (d, w) -> encerrarAtividadeAtual());
    }

    private void navegarParaConfiguracoesDoApp() {
        Uri uriConfiguracoes = Uri.fromParts("package", requireContext().getPackageName(), null);
        startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uriConfiguracoes));
    }

    private void abrirTelaBuscaDeLocalizacao() {
        new BuscaLocalizacaoFragment().show(getChildFragmentManager(), null);
    }

    private void executarNavegacaoVoltar() {
        NavHostFragment.findNavController(this).popBackStack();
    }

    private void encerrarAtividadeAtual() {
        requireActivity().finish();
    }

    private void adicionarWatcherDistancia() {
        binding.entradaTextoDistancia.addTextChangedListener(distanciaWatcher);
    }

    private void removerWatcherDistancia() {
        binding.entradaTextoDistancia.removeTextChangedListener(distanciaWatcher);
    }

    private void atribuirTextoDistancia(String texto) {
        setText(binding.entradaTextoDistancia, texto);
    }

    private void salvarDistanciaNoViewModel(double distancia) {
        simulacaoFreteViewModel.setDistancia(distancia);
    }

    private double determinarDistanciaParaCalculo() {
        double distanciaManual = lerDistanciaManual();
        if (isNotEmpty(distanciaManual)) return distanciaManual;
        return lerDistanciaDefinidaNaRota();
    }

    private double lerDistanciaManual() {
        return getDouble(binding.entradaTextoDistancia);
    }

    private double lerDistanciaDefinidaNaRota() {
        RotaUiState rota = rotaViewModel.getState().getValue();
        return isNotEmpty(rota) ? rota.getDistancia() : 0.0;
    }

    private double lerDistanciaSalvaNoViewModel() {
        return orElse(simulacaoFreteViewModel.getDistancia().getValue(), 0.0);
    }

    private boolean existeConflitoEntreRotaEDistancia(RotaUiState rota) {
        return isNotEmpty(rota) && isDistanciaManualPreenchida();
    }

    private boolean isEstadoFreteInvalidoParaConfirmacao(PrecificacaoFreteUiState estado) {
        return isEmpty(estado) || isEmpty(estado.getValorTotal());
    }

    private boolean isDistanciaManualPreenchida() {
        return isNotEmpty(lerDistanciaManual());
    }

    private boolean isDistanciaManualVazia() {
        return isEmpty(lerDistanciaManual());
    }

    private boolean isRotaDefinida() {
        return isNotEmpty(rotaViewModel.getState().getValue());
    }

    private boolean isDistanciaInvalida(double distancia) {
        return isEmpty(distancia);
    }
}