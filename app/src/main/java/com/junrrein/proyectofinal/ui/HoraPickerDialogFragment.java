package com.junrrein.proyectofinal.ui;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.time.LocalTime;

public class HoraPickerDialogFragment extends DialogFragment {

    private LocalTime horaVieja;
    private TimePickerDialog.OnTimeSetListener listener;

    HoraPickerDialogFragment(LocalTime horaVieja,
                             TimePickerDialog.OnTimeSetListener listener) {
        this.horaVieja = horaVieja;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new TimePickerDialog(requireActivity(),
                listener,
                horaVieja.getHour(),
                horaVieja.getMinute(),
                DateFormat.is24HourFormat(requireActivity()));
    }
}
