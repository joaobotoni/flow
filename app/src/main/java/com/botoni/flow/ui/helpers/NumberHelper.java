package com.botoni.flow.ui.helpers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

/**
 * Helper utilitário para manipulação, conversão e formatação de valores numéricos e monetários.
 * <p>
 * Configurado para o padrão brasileiro (pt-BR) e focado em conversões seguras
 * que evitam {@link NumberFormatException} retornando valores padrão (0).
 */
public class NumberHelper {
    private static final Locale LOCALE_BR = new Locale("pt", "BR");
    private static final DecimalFormatSymbols SYMBOLS = new DecimalFormatSymbols(LOCALE_BR);
    public static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("#,##0.00", SYMBOLS);

    /**
     * Converte uma String para Integer de forma segura.
     *
     * @param value A string numérica a ser convertida.
     * @return O valor inteiro convertido, ou 0 se a string for nula, vazia ou inválida.
     */
    @NonNull
    public static Integer getInt(@Nullable String value) {
        if (value == null || value.trim().isEmpty()) return 0;
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Converte uma String para Double de forma segura.
     *
     * @param value A string numérica a ser convertida.
     * @return O valor double convertido, ou 0.0 se a string for nula, vazia ou inválida.
     */
    @NonNull
    public static Double getDouble(@Nullable String value) {
        if (value == null || value.trim().isEmpty()) return 0.0;
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    /**
     * Converte uma String formatada (ex: moeda) para BigDecimal.
     * Utiliza o formatador local para interpretar pontuação (vírgula decimal, ponto de milhar).
     *
     * @param value A string formatada (ex: "1.200,50").
     * @return O BigDecimal correspondente, ou ZERO em caso de erro de parse.
     */
    @NonNull
    public static BigDecimal getDecimal(@Nullable String value) {
        if (value == null || value.trim().isEmpty()) return BigDecimal.ZERO;
        try {
            CURRENCY_FORMAT.setParseBigDecimal(true);
            Number number = CURRENCY_FORMAT.parse(value.trim());
            if (number == null) return BigDecimal.ZERO;
            return new BigDecimal(number.toString());
        } catch (ParseException e) {
            return BigDecimal.ZERO;
        }
    }

    /**
     * Formata um valor BigDecimal para o padrão monetário brasileiro.
     *
     * @param value O valor a ser formatado.
     * @return String formatada (ex: "1.234,56") ou "0,00" se o valor for nulo.
     */
    @NonNull
    public static String formatCurrency(@Nullable BigDecimal value) {
        if (value == null) return "0,00";
        return CURRENCY_FORMAT.format(value);
    }
}