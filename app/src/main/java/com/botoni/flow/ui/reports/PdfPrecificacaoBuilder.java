package com.botoni.flow.ui.reports;

import android.content.Context;

import androidx.annotation.NonNull;

import com.botoni.flow.ui.helpers.NumberHelper;
import com.botoni.flow.ui.state.DetalhePrecoBezerroUiState;
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
import java.util.List;
import java.util.Locale;

public class PdfPrecificacaoBuilder {

    private PdfPrecificacaoBuilder() {
    }

}
