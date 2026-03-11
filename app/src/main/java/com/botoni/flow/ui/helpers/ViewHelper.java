package com.botoni.flow.ui.helpers;

import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.math.BigDecimal;

/**
 * Helper utilitário para extração segura de dados de componentes de UI (Views).
 * <p>
 * Esta classe atua como uma ponte entre a interface do usuário e o {@link NumberHelper},
 * cuidando de verificações de nulidade e extração de texto antes da conversão.
 * </p>
 */
public class ViewHelper {

    @NonNull
    public static String getTexto(@Nullable TextView view) {
        if (view == null || view.getText() == null) return "";
        return view.getText().toString().trim();
    }

    /**
     * Extrai um valor Inteiro de um EditText.
     * Utiliza {@link NumberHelper#getInt(String)} para conversão segura.
     *
     * @param view O campo de edição.
     * @return O valor inteiro, ou 0 em caso de erro ou campo vazio.
     */
    @NonNull
    public static Integer getInt(@Nullable EditText view) {
        return NumberHelper.getInt(getTexto(view));
    }

    /**
     * Extrai um valor Double de um EditText.
     * Utiliza {@link NumberHelper#getDouble(String)} para conversão segura.
     *
     * @param view O campo de edição.
     * @return O valor double, ou 0.0 em caso de erro ou campo vazio.
     */
    @NonNull
    public static Double getDouble(@Nullable EditText view) {
        return NumberHelper.getDouble(getTexto(view));
    }

    /**
     * Extrai um valor BigDecimal de um EditText (ideal para valores monetários).
     * Utiliza {@link NumberHelper#getDecimal(String)} para conversão segura.
     *
     * @param view O campo de edição.
     * @return O valor BigDecimal, ou ZERO em caso de erro ou campo vazio.
     */
    @NonNull
    public static BigDecimal getBigDecimal(@Nullable EditText view) {
        return NumberHelper.getDecimal(getTexto(view));
    }
}