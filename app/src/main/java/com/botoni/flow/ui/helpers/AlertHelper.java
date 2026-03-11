package com.botoni.flow.ui.helpers;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

/**
 * Helper utilitário para exibição de alertas e notificações visuais ao usuário.
 * <p>
 * Fornece métodos estáticos para exibir {@link Snackbar} e diálogos
 * {@link com.google.android.material.dialog.MaterialAlertDialogBuilder},
 * centralizando a lógica de feedback visual da aplicação.
 */
public class AlertHelper {

    /**
     * Exibe uma {@link Snackbar} com a mensagem informada.
     *
     * @param view    view âncora utilizada para posicionar o Snackbar
     * @param message mensagem a ser exibida
     */
    public static void showSnackBar(View view, String message) {
        if (view != null && message != null) {
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * Exibe um diálogo de confirmação com botões positivo e negativo.
     *
     * @param context          contexto da Activity para criação do diálogo
     * @param title            título do diálogo
     * @param message          mensagem de corpo do diálogo
     * @param positiveListener ação executada ao confirmar
     * @param negativeListener ação executada ao cancelar
     */
    public static void showDialog(Context context, String title, String message,
                                  DialogInterface.OnClickListener positiveListener,
                                  DialogInterface.OnClickListener negativeListener) {
        if (context != null && title != null && message != null) {
            new MaterialAlertDialogBuilder(context)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton("Ok", positiveListener)
                    .setNegativeButton("Não", negativeListener)
                    .show();
        }
    }
}
