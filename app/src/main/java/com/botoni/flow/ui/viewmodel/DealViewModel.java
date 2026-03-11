package com.botoni.flow.ui.viewmodel;

import static com.botoni.flow.domain.usecases.AvaliacaoPrecoAnimalUseCase.calcularValorTotalBezerro;
import static com.botoni.flow.domain.usecases.AvaliacaoPrecoAnimalUseCase.calcularValorTotalPorKg;
import static com.botoni.flow.domain.usecases.AvaliacaoPrecoAnimalUseCase.calcularValorTotalTodosBezerros;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.botoni.flow.data.repositories.local.CategoriaFreteRepository;
import com.botoni.flow.data.source.local.entities.CategoriaFrete;
import com.botoni.flow.domain.entities.Category;
import com.botoni.flow.ui.helpers.TaskHelper;
import com.botoni.flow.ui.state.DealUiState;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DealViewModel extends ViewModel {
    private final TaskHelper taskHelper;
    private final CategoriaFreteRepository repository;
    private static final BigDecimal ARROBA = new BigDecimal("310.0");
    private static final BigDecimal PERCENTUAL = new BigDecimal("30.0");
    private final MutableLiveData<DealUiState> _state = new MutableLiveData<>(new DealUiState());
    private final MutableLiveData<List<Category>> _categories = new MutableLiveData<>();
    private final MutableLiveData<Category> _selected = new MutableLiveData<>();
    private final MutableLiveData<Exception> _error = new MutableLiveData<>();

    @Inject
    public DealViewModel(CategoriaFreteRepository repository, TaskHelper taskHelper) {
        this.repository = repository;
        this.taskHelper = taskHelper;
        loadCategories();
    }
    public LiveData<DealUiState> getState() {
        return _state;
    }
    public LiveData<List<Category>> getCategories() {
        return _categories;
    }
    public LiveData<Category> getSelected() {
        return _selected;
    }
    public LiveData<Exception> getError() {
        return _error;
    }

    private void loadCategories() {
        taskHelper.execute(
                this::fetchCategories,
                _categories::setValue,
                _error::setValue
        );
    }

    private List<Category> fetchCategories() {
        List<Category> result = new ArrayList<>();
        for (CategoriaFrete item : repository.getAll()) {
            result.add(new Category(item.getDescricao(), false));
        }
        return result;
    }

    public void selectCategory(Category selected) {
        _selected.setValue(selected);
        List<Category> updated = new ArrayList<>();
        for (Category item : Objects.requireNonNull(_categories.getValue())) {
            updated.add(new Category(item.getDescription(), item == selected));
        }
        _categories.setValue(updated);
    }

    public void calculate(BigDecimal peso, int quantidade) {
        BigDecimal porKg = calcularValorTotalPorKg(peso, ARROBA, PERCENTUAL);
        BigDecimal porCabeca = calcularValorTotalBezerro(peso, ARROBA, PERCENTUAL);
        BigDecimal total = calcularValorTotalTodosBezerros(porCabeca, quantidade);
        _state.setValue(new DealUiState(porKg, porCabeca, total, true));
    }

    public void clear() {
        _state.setValue(new DealUiState());
    }
}