package com.botoni.flow.utils.mappers.domain;

import com.botoni.flow.data.source.local.entities.Corretor;
import com.botoni.flow.utils.mappers.BiMapper;
import com.botoni.flow.ui.state.CorretorUiState;

import java.math.BigDecimal;

import javax.inject.Inject;

public class CorretorMapper implements BiMapper<CorretorUiState, Corretor> {
    @Inject
    public CorretorMapper() {
    }

    @Override
    public Corretor mapTo(CorretorUiState corretorUiState) {
        return new Corretor(
                corretorUiState.getId(),
                corretorUiState.getNome(),
                corretorUiState.getComissao().doubleValue(),
                corretorUiState.getTipoComissao()
        );
    }

    @Override
    public CorretorUiState mapFrom(Corretor corretor) {
        return new CorretorUiState(
                corretor.getIdCorretor(),
                corretor.getNome(),
                BigDecimal.valueOf(corretor.getComissao()),
                corretor.getTipoComissao(),
                false
        );
    }
}
