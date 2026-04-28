package com.botoni.flow.utils.mappers.domain;

import com.botoni.flow.data.source.local.entities.CategoriaFrete;
import com.botoni.flow.utils.mappers.BiMapper;
import com.botoni.flow.ui.state.ItemOpcaoUiState;

import javax.inject.Inject;

public class CategoriaMapper implements BiMapper<ItemOpcaoUiState, CategoriaFrete> {

    @Inject
    public CategoriaMapper() {
    }

    @Override
    public CategoriaFrete mapTo(ItemOpcaoUiState state) {
        return new CategoriaFrete(state.getId(), state.getDescricao());
    }

    @Override
    public ItemOpcaoUiState mapFrom(CategoriaFrete entity) {
        return new ItemOpcaoUiState(entity.getId(), entity.getDescricao(), false);
    }
}
