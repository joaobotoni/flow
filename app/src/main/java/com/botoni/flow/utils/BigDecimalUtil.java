package com.botoni.flow.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public final class BigDecimalUtil {
    private BigDecimalUtil() {}
    public static final BigDecimal CEM = new BigDecimal("100");
    public static final RoundingMode ARREDONDAMENTO_PADRAO = RoundingMode.HALF_EVEN;
    public static final RoundingMode ARREDONDAMENTO_FINANCEIRO = RoundingMode.HALF_UP;
    public static final RoundingMode ARREDONDAMENTO_TRUNCADO = RoundingMode.DOWN;
    public static final int ESCALA_CALCULO = 15;
    public static final int ESCALA_MONETARIA = 2;
    public static final int ESCALA_PERCENTUAL = 4;
    public static final MathContext CONTEXTO_CALCULO = new MathContext(ESCALA_CALCULO, ARREDONDAMENTO_PADRAO);
    public static final MathContext CONTEXTO_FINANCEIRO = new MathContext(ESCALA_MONETARIA, ARREDONDAMENTO_FINANCEIRO);
}