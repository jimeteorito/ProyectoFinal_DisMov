package com.junrrein.proyectofinal.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.time.LocalDate;

public class FechaPickerDialogFragment extends DialogFragment {

    private LocalDate fechaVieja;
    private DatePickerDialog.OnDateSetListener listener;

    FechaPickerDialogFragment(LocalDate fechaVieja,
                              DatePickerDialog.OnDateSetListener listener) {
        this.fechaVieja = fechaVieja;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new DatePickerDialog(requireActivity(),
                listener,
                fechaVieja.getYear(),
                fechaVieja.getMonthValue() - 1,
                fechaVieja.getDayOfMonth());
    }
}
