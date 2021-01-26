package com.junrrein.proyectofinal.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.junrrein.proyectofinal.R;

import java.util.function.Consumer;

public class EditarCampoDialogFragment extends DialogFragment {
    private String nombreCampo;
    private String valorPrevio;
    private Consumer<String> onSuccessFunction;

    EditarCampoDialogFragment(String nombreCampo,
                              String valorPrevio,
                              Consumer<String> onSuccessFunction) {
        this.nombreCampo = nombreCampo;
        this.valorPrevio = valorPrevio;
        this.onSuccessFunction = onSuccessFunction;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.editar_campo, null);
        EditText campo = view.findViewById(R.id.campo);
        campo.setHint(nombreCampo);
        campo.setText(valorPrevio);
        campo.requestFocus();

        builder.setView(view);
        builder.setTitle("Editar " + nombreCampo);
        builder.setPositiveButton("Editar",
                (dialog, which) -> onSuccessFunction.accept(campo.getText().toString()));
        builder.setNegativeButton("Cancelar",
                (dialog, which) -> dialog.cancel());

        return builder.create();
    }
}
