package com.botoni.flow.ui.helpers;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.NonNull;

/**
 * Helper utilitário para simplificar a implementação de ouvintes de texto ({@link TextWatcher}).
 * <p>
 * O objetivo é reduzir o código boilerplate (repetitivo), permitindo que o desenvolvedor
 * forneça apenas uma {@link Runnable} para a ação que deve ocorrer durante a digitação,
 * ignorando os métodos {@code beforeTextChanged} e {@code afterTextChanged} quando não necessários.
 */
public class TextWatcherHelper {

    /**
     * Cria um TextWatcher simples que executa a ação fornecida a cada alteração de texto.
     * Útil para validações em tempo real ou atualizações de UI imediatas.
     *
     * @param runnable A ação a ser executada quando o texto mudar.
     * @return Uma instância de {@link TextWatcher}.
     */
    @NonNull
    public static TextWatcher SimpleTextWatcher(@NonNull Runnable runnable) {
        return new TextWatcher() {
            @Override public void afterTextChanged(Editable s) {}

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                runnable.run();
            }
        };
    }

    /**
     * Cria um TextWatcher otimizado para campos de busca.
     * A ação só é executada se o texto contiver 3 ou mais caracteres.
     *
     * @param runnable A ação (busca) a ser executada.
     * @return Uma instância de {@link TextWatcher} com filtro de tamanho mínimo.
     */
    @NonNull
    public static TextWatcher SearchTextWatcher(@NonNull Runnable runnable) {
        return new TextWatcher() {
            @Override public void afterTextChanged(Editable s) {}
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 3) {
                    runnable.run();
                }
            }
        };
    }
}