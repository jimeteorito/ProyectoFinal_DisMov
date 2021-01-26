package com.junrrein.proyectofinal.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.junrrein.proyectofinal.R;

import java.util.function.Consumer;

public class EditarDuracionDialogFragment extends DialogFragment {
    private int valorPrevio;
    private Consumer<Integer> onSuccessFunction;

    EditarDuracionDialogFragment(int valorPrevio,
                                        Consumer<Integer> onSuccessFunction) {
        this.valorPrevio = valorPrevio;
        this.onSuccessFunction = onSuccessFunction;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.editar_cantidad, null);
//        NumberPicker numberPicker = view.findViewById(R.id.numberPicker);
        NumberPicker numberPicker = (NumberPicker) view;
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(100);
        numberPicker.setValue(valorPrevio);
        numberPicker.requestFocus();

        builder.setView(view);
        builder.setTitle("Editar duración");
        builder.setPositiveButton("Editar",
                (dialog, which) -> onSuccessFunction.accept(numberPicker.getValue()));
        builder.setNegativeButton("Cancelar",
                (dialog, which) -> dialog.cancel());

        return builder.create();
    }
}
