package com.junrrein.proyectofinal.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ConfirmacionDialogFragment extends DialogFragment {

    private String titulo;
    private String mensaje;
    private String accionPrimaria;
    private Runnable onSuccessFunction;

    public ConfirmacionDialogFragment(String titulo, String mensaje, String accionPrimaria, Runnable onSuccessFunction) {
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.accionPrimaria = accionPrimaria;
        this.onSuccessFunction = onSuccessFunction;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        builder.setTitle(titulo);
        builder.setMessage(mensaje);
        builder.setPositiveButton(accionPrimaria, (dialog, which) -> onSuccessFunction.run());
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

        return builder.create();
    }
}
