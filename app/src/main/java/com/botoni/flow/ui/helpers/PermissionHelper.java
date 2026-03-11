package com.botoni.flow.ui.helpers;

import android.content.Context;
import android.content.pm.PackageManager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.Map;

/**
 * Helper utilitário para gerenciamento de permissões em tempo de execução.
 * <p>
 * Centraliza a verificação, solicitação e tratamento de permissões,
 * evitando duplicação de código entre fragments e activities.
 * </p>
 */
public class PermissionHelper {

    /**
     * Verifica se todas as permissões informadas foram concedidas.
     *
     * @param context     contexto utilizado para verificação.
     * @param permissions permissões a serem verificadas.
     * @return {@code true} se todas estiverem concedidas.
     */
    public static boolean hasPermissions(@Nullable Context context, @NonNull String... permissions) {
        if (context == null) return false;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * Registra o launcher para solicitação de múltiplas permissões em um {@link Fragment}.
     *
     * @param fragment fragment que registrará o launcher.
     * @param listener listener chamado com o resultado das permissões.
     * @return launcher registrado pronto para uso.
     */
    @NonNull
    public static ActivityResultLauncher<String[]> register(@NonNull Fragment fragment, @NonNull OnPermissionResultListener listener) {
        return fragment.registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> { if (result != null) listener.onResult(!result.containsValue(false), result); }
        );
    }

    /**
     * Verifica e solicita as permissões caso não tenham sido concedidas.
     *
     * @param context     contexto utilizado para verificação.
     * @param launcher    launcher previamente registrado.
     * @param permissions permissões a serem verificadas e solicitadas.
     */
    public static void request(@Nullable Context context, @Nullable ActivityResultLauncher<String[]> launcher, @NonNull String... permissions) {
        if (launcher == null || hasPermissions(context, permissions)) return;
        launcher.launch(permissions);
    }

    /**
     * Listener para receber o resultado da solicitação de permissões.
     */
    public interface OnPermissionResultListener {
        /**
         * @param granted {@code true} se todas as permissões foram concedidas.
         * @param result     mapa detalhado com cada permissão e seu status.
         */
        void onResult(boolean granted, @NonNull Map<String, Boolean> result);
    }
}