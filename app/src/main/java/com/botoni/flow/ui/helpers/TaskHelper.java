package com.botoni.flow.ui.helpers;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.inject.Inject;

/**
 * Helper para executar tarefas assíncronas com callbacks na UI thread.
 * Utiliza {@link Executor} para processamento em background e {@link Handler}
 * para retornar resultados à thread principal.
 */
public class TaskHelper {
    private final Executor executor;
    private final Handler handler;
    @Inject
    public TaskHelper(@NonNull Executor executor, @NonNull Handler handler) {
        this.executor = executor;
        this.handler = handler;
    }

    /**
     * Executa uma tarefa em background e retorna o resultado via callbacks.
     *
     * @param task Tarefa a ser executada em background
     * @param onSuccess Callback executado na UI thread em caso de sucesso
     * @param onError Callback executado na UI thread em caso de erro
     * @param <T> Tipo de retorno da tarefa
     */
    public <T> void execute(
            @NonNull Supplier<T> task,
            @NonNull Consumer<T> onSuccess,
            @NonNull Consumer<Exception> onError
    ) {
        executor.execute(() -> {
            try {
                T result = task.get();
                handler.post(() -> onSuccess.accept(result));
            } catch (Exception e) {
                handler.post(() -> onError.accept(e));
            }
        });
    }
}