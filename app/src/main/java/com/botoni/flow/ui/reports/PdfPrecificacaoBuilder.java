package com.botoni.flow.ui.reports;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.botoni.flow.ui.helpers.FormatHelper;
import com.botoni.flow.ui.state.RotaUiState;
import com.botoni.flow.utils.pdf.PdfGenerator;
import com.botoni.flow.utils.pdf.PdfPageConfig;
import com.botoni.flow.utils.pdf.TextAlignment;
import com.botoni.flow.utils.pdf.bands.FooterBand;
import com.botoni.flow.utils.pdf.bands.RowBand;
import com.botoni.flow.utils.pdf.bands.SpacerBand;
import com.botoni.flow.utils.pdf.bands.TextBand;
import com.botoni.flow.utils.pdf.bands.TitleBand;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PdfPrecificacaoBuilder {

    private PdfPrecificacaoBuilder() {
    }

    @NonNull
    public static File gerarCapaNegociacao(
            @NonNull Context context,
            int quantidade,
            @NonNull BigDecimal pesoMedio,
            @NonNull BigDecimal valorPorKg,
            @NonNull BigDecimal valorPorCabeca,
            @NonNull BigDecimal valorPorKgComFrete,
            @NonNull BigDecimal valorPorCabecaComFrete,
            @NonNull BigDecimal valorTotal,
            @NonNull BigDecimal incidenciaFrete,
            @NonNull BigDecimal valorTotalFrete,
            double distancia,
            @Nullable RotaUiState rota
    ) throws IOException {
        String dataGeracao = new SimpleDateFormat("dd/MM/yyyy HH:mm", new Locale("pt", "BR")).format(new Date());

        boolean temFrete = valorTotalFrete.compareTo(BigDecimal.ZERO) > 0;
        boolean temRota = rota != null;
        double distFinal = (temRota && rota.getDistancia() > 0) ? rota.getDistancia() : distancia;
        boolean temDistancia = distFinal > 0;

        PdfGenerator generator = new PdfGenerator(PdfPageConfig.a4Portrait());
        generator.setFooter(new FooterBand("Flow — Negociação  •  " + dataGeracao));

        generator.addBand(new TitleBand("Negociação de Bezerros"));
        generator.addBand(new SpacerBand(10f));
        generator.addBand(new TextBand("Data: " + dataGeracao, 10f, TextAlignment.LEFT));
        generator.addBand(new SpacerBand(14f));

        generator.addBand(new RowBand(11f, 26f,
                new RowBand.Column("LOTE", 2.5f, TextAlignment.LEFT),
                new RowBand.Column("Valor", 2.5f, TextAlignment.RIGHT)
        ).asHeader());

        generator.addBand(new RowBand(10f, 22f,
                new RowBand.Column("Peso médio", 2.5f, TextAlignment.LEFT),
                new RowBand.Column(FormatHelper.formatCurrency(pesoMedio) + " kg", 2.5f, TextAlignment.RIGHT)
        ));
        generator.addBand(new RowBand(10f, 22f,
                new RowBand.Column("Quantidade", 2.5f, TextAlignment.LEFT),
                new RowBand.Column(FormatHelper.formatInteger(quantidade) + " cab.", 2.5f, TextAlignment.RIGHT)
        ));

        if (temRota || temDistancia) {
            generator.addBand(new SpacerBand(8f));

            generator.addBand(new RowBand(11f, 26f,
                    new RowBand.Column("ROTA / LOGÍSTICA", 2.5f, TextAlignment.LEFT),
                    new RowBand.Column("", 2.5f, TextAlignment.RIGHT)
            ).asHeader());

            if (temRota) {
                if (rota.getCidadeOrigem() != null && !rota.getCidadeOrigem().isEmpty()) {
                    generator.addBand(new RowBand(10f, 22f,
                            new RowBand.Column("Origem", 1.5f, TextAlignment.LEFT),
                            new RowBand.Column(rota.getCidadeOrigem() + " - " + rota.getEstadoOrigem(), 3.5f, TextAlignment.RIGHT)
                    ));
                }
                if (rota.getCidadeDestino() != null && !rota.getCidadeDestino().isEmpty()) {
                    generator.addBand(new RowBand(10f, 22f,
                            new RowBand.Column("Destino", 1.5f, TextAlignment.LEFT),
                            new RowBand.Column(rota.getCidadeDestino() + " - " + rota.getEstadoDestino(), 3.5f, TextAlignment.RIGHT)
                    ));
                }
            }

            if (temDistancia) {
                generator.addBand(new RowBand(10f, 22f,
                        new RowBand.Column("Distância", 2.5f, TextAlignment.LEFT),
                        new RowBand.Column(FormatHelper.formatDouble(distFinal) + " km", 2.5f, TextAlignment.RIGHT)
                ));
            }
        }

        if (temFrete) {
            generator.addBand(new SpacerBand(8f));

            generator.addBand(new RowBand(11f, 26f,
                    new RowBand.Column("FRETE", 2.5f, TextAlignment.LEFT),
                    new RowBand.Column("Valor", 2.5f, TextAlignment.RIGHT)
            ).asHeader());

            if (temDistancia) {
                generator.addBand(new RowBand(10f, 22f,
                        new RowBand.Column("Distância de referência", 2.5f, TextAlignment.LEFT),
                        new RowBand.Column(FormatHelper.formatInteger((int) distFinal) + " km", 2.5f, TextAlignment.RIGHT)
                ));
            }

            generator.addBand(new RowBand(10f, 22f,
                    new RowBand.Column("Total frete", 2.5f, TextAlignment.LEFT),
                    new RowBand.Column("R$ " + FormatHelper.formatCurrency(valorTotalFrete), 2.5f, TextAlignment.RIGHT)
            ));
            generator.addBand(new RowBand(10f, 22f,
                    new RowBand.Column("Incidência/kg", 2.5f, TextAlignment.LEFT),
                    new RowBand.Column("R$ " + FormatHelper.formatCurrency(incidenciaFrete), 2.5f, TextAlignment.RIGHT)
            ));
        }

        generator.addBand(new SpacerBand(8f));

        generator.addBand(new RowBand(11f, 26f,
                new RowBand.Column("VALORES", 2.5f, TextAlignment.LEFT),
                new RowBand.Column("Valor", 2.5f, TextAlignment.RIGHT)
        ).asHeader());

        generator.addBand(new RowBand(10f, 22f,
                new RowBand.Column("Valor kg sem frete", 2.5f, TextAlignment.LEFT),
                new RowBand.Column("R$ " + FormatHelper.formatCurrency(valorPorKg), 2.5f, TextAlignment.RIGHT)
        ));
        generator.addBand(new RowBand(10f, 22f,
                new RowBand.Column("Valor cab. sem frete", 2.5f, TextAlignment.LEFT),
                new RowBand.Column("R$ " + FormatHelper.formatCurrency(valorPorCabeca), 2.5f, TextAlignment.RIGHT)
        ));

        if (temFrete) {
            generator.addBand(new RowBand(10f, 22f,
                    new RowBand.Column("Valor kg com frete", 2.5f, TextAlignment.LEFT),
                    new RowBand.Column("R$ " + FormatHelper.formatCurrency(valorPorKgComFrete), 2.5f, TextAlignment.RIGHT)
            ));
            generator.addBand(new RowBand(10f, 22f,
                    new RowBand.Column("Valor cab. com frete", 2.5f, TextAlignment.LEFT),
                    new RowBand.Column("R$ " + FormatHelper.formatCurrency(valorPorCabecaComFrete), 2.5f, TextAlignment.RIGHT)
            ));
        }

        generator.addBand(new SpacerBand(10f));
        generator.addBand(new RowBand(12f, 26f,
                new RowBand.Column("TOTAL GERAL", 3.5f, TextAlignment.RIGHT),
                new RowBand.Column("R$ " + FormatHelper.formatCurrency(valorTotal), 1.5f, TextAlignment.RIGHT)
        ));

        String nomeArquivo = "negociacao_" + System.currentTimeMillis() + ".pdf";
        return generator.generate(context, nomeArquivo);
    }
}