package com.botoni.flow.ui.viewmodel;

import android.content.Context;
import android.location.Address;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.botoni.flow.R;
import com.botoni.flow.data.repositories.LocalizacaoRepository;
import com.botoni.flow.ui.helpers.TaskHelper;
import com.botoni.flow.ui.state.RotaUiState;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;

@HiltViewModel
public class RotaViewModel extends ViewModel {
    private final TaskHelper taskHelper;
    private final LocalizacaoRepository repositorio;
    private final Context context;
    private static final String DESTINO_QUERY = "Cuiabá";
    private final MutableLiveData<RotaUiState> uiState = new MutableLiveData<>(null);
    private final MutableLiveData<Boolean> visivel = new MutableLiveData<>(false);
    private final MutableLiveData<Exception> erro = new MutableLiveData<>(null);

    @Inject
    public RotaViewModel(TaskHelper taskHelper, LocalizacaoRepository repositorio, @ApplicationContext Context context) {
        this.taskHelper = taskHelper;
        this.repositorio = repositorio;
        this.context = context;
    }

    public void selecionar(Address origem) {
        taskHelper.execute(
                () -> {
                    try {
                        return calcularRota(origem);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                },
                state -> {
                    uiState.postValue(state);
                    visivel.postValue(state != null);
                },
                erro::postValue
        );
    }

    public void limpar() {
        uiState.postValue(null);
        visivel.postValue(false);
        erro.postValue(null);
    }

    public void resetarVisivel() {
        visivel.postValue(false);
    }

    public LiveData<RotaUiState> getUiState() { return uiState; }
    public LiveData<Boolean> getVisivel() { return visivel; }
    public LiveData<Exception> getErro() { return erro; }

    private RotaUiState calcularRota(Address origem) throws IOException {
        Address destino = repositorio.buscarDestino(DESTINO_QUERY);
        if (destino == null) throw new IOException(context.getString(R.string.erro_endereco_nao_encontrado));
        String resposta = repositorio.buscarRota(origem, destino);
        double distancia = repositorio.calcularDistanciaKm(resposta);
        List<String> pontos = Arrays.asList(formatar(origem), formatar(destino));
        return new RotaUiState(pontos, distancia);
    }

    private String formatar(Address endereco) {
        String cidade = endereco.getLocality() != null ? endereco.getLocality() : endereco.getSubAdminArea();
        String estado = endereco.getAdminArea();
        return cidade != null && estado != null
                ? String.format("%s, %s", cidade, estado)
                : endereco.getAddressLine(0);
    }
}