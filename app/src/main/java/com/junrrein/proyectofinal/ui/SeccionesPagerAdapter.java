package com.junrrein.proyectofinal.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SeccionesPagerAdapter extends FragmentPagerAdapter {

    private static final String[] nombreSecciones = {"Creados", "Interesado", "Todos"};

    public SeccionesPagerAdapter(@NonNull FragmentManager fm) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new ListaEventosFragment();
        Bundle arguments = new Bundle();

        switch (position) {
            case 0:
                arguments.putSerializable(ListaEventosFragment.TIPO_LISTA, ListaEventosFragment.TipoLista.CREADOS);
                break;
            case 1:
                arguments.putSerializable(ListaEventosFragment.TIPO_LISTA, ListaEventosFragment.TipoLista.INTERESADO);
                break;
            case 2:
                arguments.putSerializable(ListaEventosFragment.TIPO_LISTA, ListaEventosFragment.TipoLista.TODOS);
                break;
        }

        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return nombreSecciones[position];
    }
}
